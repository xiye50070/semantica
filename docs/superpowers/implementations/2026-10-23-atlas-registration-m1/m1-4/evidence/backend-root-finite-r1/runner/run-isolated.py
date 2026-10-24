"""Exactly eight new H2/owned-HTTP methods, only after the writer freezes source.
No runtime/DM/OM or product installation. Old M1.2/M1.3 test methods are not run.
"""
from pathlib import Path
import datetime as dt
import hashlib
import json
import subprocess
import sys

BASE=Path(__file__).resolve().parent.parent
E14=BASE.parent
E13=E14.parents[1]/'m1-3/evidence'
CLOUD=Path('/Users/wangmingzhang/atlas-cloud')
JAVA='/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/java'
JAVAC=JAVA.removesuffix('/java')+'/javac'
SOURCE_ROOT=E14/'backend-implementation-r1/first-fixed-r12/source'
# Filled only after the product author freezes a physical cut. A prepare/run
# cannot silently pick up whichever source the author happens to be editing.
PRODUCT_PINS={'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/JdbcOntologyModelingPermissionStore.java': '72d27d095496577d7d48bbf072ab8075c1a1304f139da31665c538cec41465c8', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/JdbcOntologyRootGlossaryStore.java': '3cee1393512a18b8edc304fd1d78828cd27e86231e9c4fef41152b59616e0768', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingIdentityConsumer.java': 'd1d7cffac34a37d449d44e026b24473aadbac0d4bf160393c2ffd5036741c0d5', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyP1Configuration.java': 'e94f72771422c37539a51561e7a9b1b1a4fb2821f4b8c16bf9e8cb63e241e768', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyPreparationService.java': 'd9e753614ffc541bb9186ce2b1791831b90d2b5eef4ded94b1d4e38c1260f509', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyProjectService.java': 'ea541eed9ebe58194dacf35ec33c73037ec141b71c075f958f527bd49aa99409', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyRootGlossaryConfiguration.java': '906a1b4695ce1cf4d1e10226a48cd698be0bb8afa6a7fe1a8c3bd3252d53ee4d', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyRootGlossaryContracts.java': 'd7236386bb9ee221694115bf3af64d32aa9f69d99d1391a3ad77ea9da19388b3', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyRootGlossaryController.java': '3bdd86129a6763e366fca98655899e3b22a6452e5257529eb57bb9c899f833a5', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyRootGlossaryService.java': '7eb2d1864db7b4a2ba9ecaf7de96523bac9df30d3087ea2642c13f9e54de0e8e', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/om/OmInstanceRegistry.java': '4260d208baa25f66ae79d31e47cc80a1f6f70ccb94659172d6d9ceb7c877efb3', 'atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/om/OmRestClient.java': 'eb43e65c35bd0d583e549020b5539901aa6cc359caa677db4a5eae700d883939'}
COMPILE_REFERENCE=E14/'backend-implementation-r1/compile-first-r1/argv.json'

def now():return dt.datetime.now(dt.timezone.utc).isoformat()
def meta(p):
    raw=p.read_bytes();return {'path':str(p),'sha256':hashlib.sha256(raw).hexdigest(),'bytes':len(raw)}
def save(path,value):
    with path.open('x') as stream:json.dump(value,stream,indent=2);stream.write('\n')
def run(label,selected=None,malformed_only=False):
    if PRODUCT_PINS is None:raise SystemExit('PREPARED_PRODUCT_SOURCE_NOT_FROZEN')
    assert isinstance(PRODUCT_PINS,dict) and len(PRODUCT_PINS)==12
    products=[SOURCE_ROOT/rel for rel in sorted(PRODUCT_PINS)]
    assert all(meta(p)['sha256']==PRODUCT_PINS[p.relative_to(SOURCE_ROOT).as_posix()] for p in products)
    args=json.loads(COMPILE_REFERENCE.read_text());cp=args[args.index('-cp')+1]
    assert not any('backend-root-finite-r1' in p for p in cp.split(':'))
    fixture=E13/'backend-candidate-r1/tests/cn/iocoder/atlas/module/infra/ontology'
    own=BASE/'tests/cn/iocoder/atlas/module/infra/ontology'
    sources=products+[fixture/'OntologyModelingAccessTest.java',fixture/'OntologyModelingPermissionTest.java',own/'OntologyRootGlossaryFiniteTest.java',own/'RootGlossaryFiniteRunner.java',own/'LegacyRootDescriptionService.java']
    sql=[CLOUD/'sql/ontology/V20261023_32__ontology_modeling_permissions.sql',SOURCE_ROOT/'sql/ontology/V20261024_33__ontology_root_glossary_binding.sql',CLOUD/'atlas-module-infra/atlas-module-infra-server/src/test/resources/ontology/p1-test-schema.sql',CLOUD/'integrations/om-identity/sql/h2/V20261001_16__om_identity_control.sql',CLOUD/'sql/ontology/V20261023_31__ontology_modeling_access.sql']
    before=[meta(p) for p in sources+sql]
    out=BASE/label;out.mkdir();classes=out/'classes';classes.mkdir()
    save(out/'inputs.json',{'files':before,'compileReference':meta(COMPILE_REFERENCE),'classpathNonJars':[p for p in cp.split(':') if not p.endswith('.jar')],'classpathDependencyJars':len([p for p in cp.split(':') if p.endswith('.jar')]),'compiledProductSources':12,'oldSuiteMethodsRun':0})
    commands=[('compile',[JAVAC,'-parameters','-proc:none','-sourcepath','','-cp',cp,'-d',str(classes),*[str(p) for p in sources]]),
              ('test',[JAVA,'-Dm14.secure.bearer=true']+(['-Dm14.malformed.only=true'] if malformed_only else [])+['-Dm13.sql='+str(sql[0]),'-Dm14.sql='+str(sql[1]),'-cp',str(classes)+':'+cp,'cn.iocoder.atlas.module.infra.ontology.RootGlossaryFiniteRunner',str(out)]+([] if selected is None else [selected]))]
    records=[]
    for phase,argv in commands:
        save(out/(phase+'-argv.json'),argv);start=now()
        with (out/(phase+'.stdout')).open('xb') as stdout,(out/(phase+'.stderr')).open('xb') as stderr:
            r=subprocess.run(argv,cwd=CLOUD,stdout=stdout,stderr=stderr,timeout=150)
        records.append({'phase':phase,'startedAt':start,'completedAt':now(),'exitCode':r.returncode,'argvReference':str(out/(phase+'-argv.json'))})
        if r.returncode:break
    after=[meta(p) for p in sources+sql]
    actual={'commands':records,'inputsStable':before==after,'before':before,'after':after,'realNativeHTTP':0,'realDM':0,'serviceActions':0,'productWrites':0,'testBoundary':'In-memory H2 IAM, mapping and applied-policy fixtures; owned fake HTTP. Actual new Store/Service/Consumer/Registry/Broker/SDK compiled from frozen source; no live qualification.'}
    save(out/'actual.json',actual);print(json.dumps({'actual':str(out/'actual.json'),'commands':records,'inputsStable':before==after}))
    assert before==after
    return records[-1]['exitCode']

if __name__=='__main__':
    if len(sys.argv)<3 or sys.argv[1]!='--execute':print('PREPARED_ONLY_NO_COMPILE_OR_TEST');raise SystemExit(0)
    raise SystemExit(run(sys.argv[2],None if len(sys.argv)<4 else sys.argv[3],len(sys.argv)>4 and sys.argv[4]=='--malformed-only'))
