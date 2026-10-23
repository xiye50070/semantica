"""One owned-H2 method against the current complete production build; default does not run."""
from pathlib import Path
import argparse, datetime, hashlib, io, json, subprocess, tempfile, zipfile

D = Path(__file__).resolve().parent
def require(ok, code):
    if not ok: raise RuntimeError(code)
def meta(path):
    raw = Path(path).read_bytes()
    return {"sha256": hashlib.sha256(raw).hexdigest(), "bytes": len(raw)}
def save(path, value):
    with Path(path).open("x") as f: json.dump(value, f, ensure_ascii=False, indent=2); f.write("\n")
def now(): return datetime.datetime.now(datetime.timezone.utc).isoformat()
def execute():
    b = json.loads((D / "bindings.json").read_text())
    for item in b["sources"]:
        expected = {k:item[k] for k in ("sha256", "bytes")}
        require(meta(item["repo"]) == expected == meta(item["before"]), "SOURCE_OR_FIXTURE_DRIFT")
        if item["production"]: require(meta(item["mirror"]) == expected, "PRODUCTION_MIRROR_DRIFT")
    jar = Path(b["artifact"]["path"])
    require(meta(jar) == {k:b["artifact"][k] for k in ("sha256", "bytes")}, "CURRENT_ARTIFACT_DRIFT")
    output = D / "actual-r1"; output.mkdir(exist_ok=False)
    work = Path(tempfile.mkdtemp(prefix="atlas-d2-current-unmapped-one-"))
    classes=work/"classes"; classes.mkdir(); lib=work/"lib";lib.mkdir(); cwd=work/"working";cwd.mkdir()
    server_classes = jar.parent / "classes"
    class_proof=[]
    with zipfile.ZipFile(jar) as z:
        names=z.namelist();require(len(names)==len(set(names)),"DUPLICATE_ARTIFACT_ENTRY")
        for item in b["sources"]:
            if not item["production"] or "/infra-api/" in item["relativePath"] or "atlas-module-infra-api/" in item["relativePath"]: continue
            stem=item["relativePath"].split("/src/main/java/",1)[1][:-5]
            selected=[n for n in names if n=="BOOT-INF/classes/"+stem+".class" or n.startswith("BOOT-INF/classes/"+stem+"$") and n.endswith(".class")]
            require(bool(selected),"MISSING_PRODUCTION_CLASS")
            for n in selected:
                p=server_classes/n[len("BOOT-INF/classes/"):];raw=z.read(n)
                require(p.read_bytes()==raw,"CURRENT_TARGET_BOOT_CLASS_MISMATCH")
                class_proof.append({"entry":n,"sha256":hashlib.sha256(raw).hexdigest(),"bytes":len(raw)})
        libs=[]
        for name in names:
            if not name.startswith("BOOT-INF/lib/") or not name.endswith(".jar"): continue
            relative=name[len("BOOT-INF/lib/"):]
            require(Path(relative).name==relative and relative not in {p.name for p in libs},"UNSAFE_OR_DUPLICATE_LIBRARY")
            p=lib/relative;p.write_bytes(z.read(name));libs.append(p)
    # Current BOOT libs win; retain only missing test-only dependencies from the old fixture binding.
    prior=json.loads(Path(b["testDependencyBinding"]).read_text())["classpath"].split(":")
    existing={p.name for p in libs};extra=[]
    for value in prior:
        p=Path(value)
        if p.suffix!=".jar" or p.name in existing: continue
        require(p.is_file(),"MISSING_TEST_DEPENDENCY")
        require(not p.name.startswith("atlas-"),"OLD_PRODUCT_JAR_OVERLAY_FORBIDDEN")
        extra.append(p);existing.add(p.name)
    cp=":".join(map(str,[classes,server_classes,*libs,*extra]))
    for rel in b["schemas"]:
        target=cwd/rel;target.parent.mkdir(parents=True,exist_ok=True)
        target.write_bytes((D/"source-before"/rel).read_bytes())
    sources=[D/"source-before"/b["fixture"], D/"tests/cn/iocoder/atlas/module/infra/ontology/D2UnmappedConsumerTest.java", D/"tests/D2SingleRunner.java"]
    inputs=sources+[D/"bindings.json",Path(__file__)]+[Path(x["repo"]) for x in b["sources"]]
    before={str(p):meta(p) for p in inputs}
    save(output/"inputs-before.json",before)
    save(output/"production-class-proof.json",class_proof)
    save(output/"classpath.json",{"work":str(work),"classpath":cp,"productionClasses":str(server_classes),"oldProductOverlay":False,"extraTestJars":[{"path":str(p),**meta(p)} for p in extra]})
    javac=next(p for p in Path("/Library/Java/JavaVirtualMachines").glob("*/Contents/Home/bin/javac") if "21" in str(p))
    commands=[]
    def run(name,argv):
        at=now();p=subprocess.run(argv,cwd=cwd,capture_output=True,timeout=90)
        (output/(name+".stdout")).write_bytes(p.stdout);(output/(name+".stderr")).write_bytes(p.stderr)
        commands.append({"name":name,"argv":list(map(str,argv)),"cwd":str(cwd),"startedAt":at,"completedAt":now(),"exitCode":p.returncode})
        print(name,p.returncode,flush=True)
        return p.returncode
    started=now()
    code=run("compile",[str(javac),"--release","21","-parameters","-proc:none","-sourcepath","","-cp",cp,"-d",str(classes),*map(str,sources)])
    if code==0: code=run("test",[str(javac.with_name("java")),"-ea","-cp",cp,"D2SingleRunner",str(output/"test-result.json")])
    after={str(p):meta(p) for p in inputs};save(output/"inputs-after.json",after)
    stable=before==after
    save(output/"actual.json",{"startedAt":started,"completedAt":now(),"commands":commands,"exitCode":code,"inputsStable":stable,"productionSourceCount":b["sourceCountBound"],"compiledProductionSources":0,"compiledTestSources":3,"oldSuitesRerun":False,"oneMethodOnly":True,"realHTTP":0,"realSQL":0,"sharedServiceActions":0,"productWrites":0})
    require(stable,"INPUTS_CHANGED")
    if code: raise SystemExit(code)

if __name__=="__main__":
    p=argparse.ArgumentParser();p.add_argument("--execute",action="store_true");a=p.parse_args()
    if a.execute: execute()
    else: print("PREPARED_NOT_EXECUTED")
