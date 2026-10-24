from pathlib import Path
import datetime, hashlib, json, os, subprocess, sys, zipfile
OUT = Path(__file__).resolve().parent
PREP_SHA = 'e306499c9ec2a794156b33de0682721792cfc80a519b40f1c449ee45aa9bbfad'
if __name__ != '__main__' or sys.argv[1:] != ['--execute']:
    raise SystemExit('PREPARED_ONLY_REQUIRE_ROOT_REVIEWED_EXECUTE')
def now(): return datetime.datetime.now(datetime.timezone.utc).isoformat()
def meta(p):
    assert p.is_file() and not p.is_symlink(), 'NOT_REGULAR_INPUT'
    b=p.read_bytes(); return {'sha256':hashlib.sha256(b).hexdigest(),'bytes':len(b)}
def save(name,value):
    with (OUT/name).open('x') as f: json.dump(value,f,ensure_ascii=False,indent=2); f.write('\n')
def write(p,b):
    p.parent.mkdir(parents=True,exist_ok=True)
    with p.open('xb') as f: f.write(b); f.flush(); os.fsync(f.fileno())
def classes(root):
    return {str(p.relative_to(root)):meta(p) for p in sorted(root.rglob('*.class'))}
assert meta(OUT/'preparation.json')['sha256']==PREP_SHA
p=json.loads((OUT/'preparation.json').read_text())
for key in ['candidate','oldSource','runtimeCodeOrigin','classpath']:
    item=p[key]; assert meta(Path(item['path']))=={k:item[k] for k in ('sha256','bytes')}, 'PREPARED_INPUT_CHANGED'
original=Path(p['originalOverrideRoot']); before=p['originalOverrideClasses']
assert classes(original)==before and len(before)==49
stage=Path(p['stage']); assert not stage.exists(), 'STAGE_ALREADY_EXISTS_NO_RETRY'
cp=json.loads(Path(p['classpath']['path']).read_text())
assert len(cp)==616 and cp[0]==str(original) and all(Path(x).exists() for x in cp)
# No old work/release/source writes. Only new stage/classes and deterministic class-only jar.
started=now(); save('build-intent.json',{'startedAt':started,'stage':str(stage),'source':p['candidate'],'ordinaryCompilerOnly':True,'HTTP':0,'SQL':0,'serviceActions':0,'automaticRetry':False})
stage.mkdir(parents=True,mode=0o700)
compiled=stage/'compiled'; compiled.mkdir()
argv=[str(Path(p['jdk'])/'bin/javac'),'-proc:none','-sourcepath','','-cp',':'.join(cp),'-d',str(compiled),p['candidate']['path']]
save('compile-argv.json',argv)
with (OUT/'compile.stdout').open('xb') as stdout,(OUT/'compile.stderr').open('xb') as stderr:
    result=subprocess.run(argv,cwd=stage,stdout=stdout,stderr=stderr,timeout=180)
if result.returncode:
    save('compile-failed.json',{'startedAt':started,'completedAt':now(),'exitCode':result.returncode,'noAutomaticRetry':True}); raise SystemExit(result.returncode)
generated=classes(compiled); prefix=p['classFamilyPrefix']
assert len(generated)==p['expectedClassFamilyCount']==6
assert set(generated)=={k for k in before if k==prefix+'.class' or k.startswith(prefix+'$')}, 'UNEXPECTED_COMPILED_CLASS_SET'
output=stage/'classes'; output.mkdir()
for path,wanted in before.items():
    src=compiled/path if path in generated else original/path
    assert meta(src)==(generated[path] if path in generated else wanted)
    write(output/path,src.read_bytes())
expected={**before,**generated}; assert classes(output)==expected
artifact=stage/'openmetadata-derived-empty-override.jar'
with zipfile.ZipFile(artifact,'x',compression=zipfile.ZIP_STORED) as z:
    for path in sorted(expected):
        info=zipfile.ZipInfo(path,(1980,1,1,0,0,0)); info.external_attr=0o644<<16; z.writestr(info,(output/path).read_bytes())
with zipfile.ZipFile(artifact) as z:
    assert len(z.namelist())==len(set(z.namelist()))==49 and set(z.namelist())==set(expected)
    assert all({'sha256':hashlib.sha256(z.read(k)).hexdigest(),'bytes':len(z.read(k))}==v for k,v in expected.items())
assert classes(original)==before and meta(Path(p['oldSource']['path']))=={k:p['oldSource'][k] for k in ('sha256','bytes')}
assert meta(Path(p['candidate']['path']))=={k:p['candidate'][k] for k in ('sha256','bytes')}
changed=[k for k in expected if expected[k]!=before[k]]
assert changed and all(k in generated for k in changed)
receipt={'startedAt':started,'completedAt':now(),'exitCode':0,'classesDirectory':str(output),'artifact':{'path':str(artifact),**meta(artifact)},'classCount':49,'compiledClassFamily':generated,'changedClasses':changed,'unchangedOtherClasses':43,'originalClassesUnchanged':True,'oldSourceUnchanged':True,'HTTP':0,'SQL':0,'serviceActions':0,'tests':'Prior pure checks reused; no tests rerun','switchExecuted':False,'limitations':'Single-source javac against current deployed runtime CP; not full Native reactor build or live compatibility/acceptance.'}
save('receipt.json',receipt); print(json.dumps(receipt,ensure_ascii=False))
