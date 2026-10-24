package cn.iocoder.atlas.module.infra.ontology;

import cn.iocoder.atlas.module.infra.ontology.om.*;
import cn.iocoder.atlas.module.infra.ontology.omidentity.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.sun.net.httpserver.HttpServer;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

/** Real new Store/Service/Consumer/Registry/Broker/SDK. H2 IAM, identity and
 * already-applied policy are fixtures; owned fake HTTP is not native acceptance. */
class OntologyRootGlossaryFiniteTest extends OntologyModelingPermissionTest {
    static final ObjectMapper JSON=new ObjectMapper();
    static final List<Map<String,Object>> RESULTS=new ArrayList<>();
    static final Map<String,Object> WIRE=new LinkedHashMap<>();
    final Map<String,OntologyRootGlossaryContracts.Prepare> originalRequests=new HashMap<>();
    OntologyBearerIdentity realBearer;final AtomicInteger bearerChecks=new AtomicInteger();
    OntologyAuthorization.Subject currentSubject(){return Boolean.getBoolean("m14.secure.bearer")?realBearer.get():subject;}
    void installRealBearer(){
        var api=org.mockito.Mockito.mock(cn.iocoder.atlas.framework.common.biz.system.oauth2.OAuth2TokenCommonApi.class);
        org.mockito.Mockito.when(api.checkAccessToken(org.mockito.ArgumentMatchers.anyString())).thenAnswer(call->{
            assertFalse(org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive());
            assertEquals("OWNED_FIXTURE_ATLAS_BEARER",call.getArgument(0));bearerChecks.incrementAndGet();
            var dto=new cn.iocoder.atlas.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCheckRespDTO()
                .setUserId(Long.parseLong(subject.accountId())).setTenantId(Long.parseLong(subject.tenantId()))
                .setUserType(2).setUserInfo(Map.of()).setScopes(List.of()).setExpiresTime(LocalDateTime.now().plusHours(1));
            return cn.iocoder.atlas.framework.common.pojo.CommonResult.success(dto);
        });
        realBearer=new OntologyBearerIdentity(api);
        var request=new org.springframework.mock.web.MockHttpServletRequest("GET","/owned-root-finite");
        request.addHeader("Authorization","Bearer OWNED_FIXTURE_ATLAS_BEARER");request.addHeader("tenant-id","1");
        org.springframework.web.context.request.RequestContextHolder.setRequestAttributes(new org.springframework.web.context.request.ServletRequestAttributes(request));
    }
    @Override JdbcOntologyModelingPermissionStore newPermissionStore(){
        var value=new JdbcOntologyModelingPermissionStore(source,new JdbcAtlasOntologyAuthority(source),this::currentSubject,store,
            (scope,account,instance,identity,version,caps,targets)->new OntologyModelingPermissionContracts.Compilation(Map.of("name","AtlasModelingFinite","rules",caps),OntologyCanonicalJson.hash(Map.of("scope",scope,"version",version,"caps",caps,"targets",targets)),List.of(),List.of()),
            new OntologyModelingPermissionContracts.QualificationPort(){
                public OntologyModelingPermissionContracts.StepPlan plan(String cap,List<OntologyModelingPermissionContracts.Target> targets){return new OntologyModelingPermissionContracts.StepPlan("step-"+cap,cap,cap,targets.getFirst().targetId(),"finite explicit action","SUPPORTED");}
                public OntologyModelingPermissionContracts.Observation execute(OntologyModelingPermissionContracts.Admission a,OntologyModelingPermissionContracts.StepPlan p,OntologyModelingPermissionContracts.StepGuard g){return effect.run(a,p,g);}
            },clock);
        value.setQualificationKick(id->kicks.incrementAndGet());return value;
    }
    Case active;JdbcOntologyRootGlossaryStore roots;OntologyRootGlossaryService rootService;
    OntologyModelingIdentityConsumer consumer;OmInstanceRegistry registry;

    @Override JdbcOntologyModelingAccessStore newStore(){
        var s=new JdbcOntologyModelingAccessStore(source,new JdbcAtlasOntologyAuthority(source),this::currentSubject,clock,
            List.of(new OntologyModelingAccessContracts.ApprovedInstance("om-locked","owned finite native",active.origin(),true)));
        s.setLegacyPresence((i,scope,a)->legacy.get().present(i,scope,a));return s;
    }
    final class Case implements AutoCloseable {
        final HttpServer http;final Path credentialRoot;final OmUserCredentialStore credentials;
        final List<String> requests=Collections.synchronizedList(new ArrayList<>());
        final List<String> queries=Collections.synchronizedList(new ArrayList<>());
        final Map<String,ObjectNode> objects=new ConcurrentHashMap<>();
        final AtomicInteger creates=new AtomicInteger(),login=new AtomicInteger(),self=new AtomicInteger(),fallback=new AtomicInteger();
        final CountDownLatch createEntered=new CountDownLatch(1),allowCreate=new CountDownLatch(1);
        volatile boolean holdCreate,denyCreate,malformedCreate,omitDefault,integerVersion,encodeNumericDescription;
        volatile Runnable afterCreate=()->{};
        volatile Number readVersion;volatile String token,invalidCreateKind;volatile byte[] lastRaw201;OmOrdinarySessionBroker broker;
        Case()throws Exception{
            credentialRoot=Files.createTempDirectory("m14-root-owned-").toRealPath();
            Files.setPosixFilePermissions(credentialRoot,PosixFilePermissions.fromString("rwx------"));
            credentials=new OmUserCredentialStore(credentialRoot);
            http=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
            http.createContext("/",ex->{
                String method=ex.getRequestMethod(),path=ex.getRequestURI().getRawPath();
                requests.add(method+" "+path);queries.add(ex.getRequestURI().getRawQuery());
                byte[] request=ex.getRequestBody().readAllBytes();int status=200;byte[] raw;
                boolean ordinary=token!=null&&("Bearer "+token).equals(ex.getRequestHeaders().getFirst("Authorization"));
                try{
                    if(!ordinary){status=403;raw="{\"message\":\"FINITE_ORDINARY_BEARER_REQUIRED\"}".getBytes(StandardCharsets.UTF_8);}
                    else if(method.equals("POST")&&path.equals("/api/v1/glossaries")){
                        creates.incrementAndGet();var body=(ObjectNode)JSON.readTree(request);assertEquals(Set.of("name","displayName","description","owners","reviewers"),keys(body));
                        assertEquals(nativeId,body.path("owners").path(0).path("id").asText());assertEquals("user",body.path("owners").path(0).path("type").asText());assertTrue(body.path("reviewers").isEmpty());
                        createEntered.countDown();if(holdCreate&&!allowCreate.await(10,TimeUnit.SECONDS))throw new IllegalStateException("FINITE_HOLD_TIMEOUT");
                        if(denyCreate){status=403;raw="{\"message\":\"FINITE_CREATE_DENIED\"}".getBytes(StandardCharsets.UTF_8);}
                        else if(objects.values().stream().anyMatch(x->x.path("name").asText().equals(body.path("name").asText()))){status=409;raw="{\"message\":\"FINITE_NAME_COLLISION\"}".getBytes(StandardCharsets.UTF_8);}
                        else{String id=UUID.randomUUID().toString();ObjectNode g=body.deepCopy().put("id",id).put("fullyQualifiedName",body.path("name").asText()).put("version",0.1).put("deleted",false);if(integerVersion)g.put("version",1);g.putArray("tags");g.putArray("namespaces");if(encodeNumericDescription)g.put("description",body.path("description").asText().replace("：","&#xff1a;").replace("；","&#xff1b;"));
                            ObjectNode conf=g.putObject("ontologyConfiguration");conf.put("layer","L3").put("baseIri","https://open-metadata.org/ontology/"+id+"/").put("iriMintingPattern","{term}").put("readOnly",false);conf.putArray("imports");conf.putArray("installedPacks");conf.putArray("prefixes");objects.put(id,g);
                            afterCreate.run();status=201;
                            if("duplicate".equals(invalidCreateKind))raw=("{\"id\":\""+id+"\",\"id\":\""+UUID.randomUUID()+"\"}").getBytes(StandardCharsets.UTF_8);
                            else if("trailing".equals(invalidCreateKind))raw=(JSON.writeValueAsString(g)+" {}").getBytes(StandardCharsets.UTF_8);
                            else raw=malformedCreate?"{\"id\":".getBytes(StandardCharsets.UTF_8):JSON.writeValueAsBytes(g);lastRaw201=raw.clone();}
                    }else if(method.equals("GET")&&path.startsWith("/api/v1/glossaries/")){
                        String id=path.substring("/api/v1/glossaries/".length());var original=objects.get(id);
                        if(original==null){status=404;raw="{\"message\":\"FINITE_MISSING\"}".getBytes(StandardCharsets.UTF_8);}
                        else{var body=original.deepCopy();if(readVersion!=null)body.set("version",JSON.valueToTree(readVersion));String query=URLDecoder.decode(Objects.toString(ex.getRequestURI().getRawQuery(),""),StandardCharsets.UTF_8);
                            assertTrue(query.contains("include=all"));assertTrue(query.contains("fields=owners,reviewers,tags"));
                            if(!query.contains("namespaces")){body.remove(List.of("namespaces","ontologyConfiguration"));}else if(omitDefault)body.remove("namespaces");raw=JSON.writeValueAsBytes(body);}
                    }else{status=403;raw="{\"message\":\"FINITE_NO_OTHER_ROUTE\"}".getBytes(StandardCharsets.UTF_8);}
                }catch(Throwable e){status=500;raw="{\"message\":\"FINITE_HANDLER_FAILURE\"}".getBytes(StandardCharsets.UTF_8);}
                ex.getResponseHeaders().set("Content-Type","application/json");ex.sendResponseHeaders(status,raw.length);ex.getResponseBody().write(raw);ex.close();
            });http.start();
        }
        URI origin(){return URI.create("http://127.0.0.1:"+http.getAddress().getPort());}
        void installBroker(){
            var nativeClient=new NativeOmIdentityClient((m,p,b)->{fallback.incrementAndGet();throw new AssertionError("NO_MANAGEMENT_FALLBACK");},(m,p,b,bearer)->{
                if(m.equals("POST")&&p.equals("/v1/users/login")){
                    login.incrementAndGet();assertNull(bearer);var input=JSON.readTree(b);assertEquals(jdbc.queryForObject("SELECT native_email FROM om_identity_binding",String.class),input.path("email").asText());
                    long exp=clock.instant().plusSeconds(3600).getEpochSecond();var claims=JSON.createObjectNode().put("sub",jdbc.queryForObject("SELECT native_name FROM om_identity_binding",String.class)).put("username",jdbc.queryForObject("SELECT native_name FROM om_identity_binding",String.class)).put("email",input.path("email").asText()).put("isBot",false).put("tokenType","OM_USER").put("iat",clock.instant().getEpochSecond()).put("exp",exp);claims.putArray("roles");var enc=Base64.getUrlEncoder().withoutPadding();token=enc.encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8))+"."+enc.encodeToString(JSON.writeValueAsBytes(claims))+".c3ludGhldGljLXNpZ25hdHVyZQ";
                    return new NativeOmIdentityClient.Response(200,JSON.writeValueAsBytes(JSON.createObjectNode().put("accessToken",token).put("refreshToken","FINITE_ONLY").put("tokenType","Bearer").put("expiryDuration",exp*1000)));
                }
                assertEquals("GET",m);assertEquals("/v1/users/loggedInUser?fields=roles,teams",p);assertEquals(token,new String(bearer));self.incrementAndGet();var u=JSON.createObjectNode().put("id",nativeId).put("name",jdbc.queryForObject("SELECT native_name FROM om_identity_binding",String.class)).put("email",jdbc.queryForObject("SELECT native_email FROM om_identity_binding",String.class)).put("isAdmin",false).put("isBot",false).put("deleted",false);return new NativeOmIdentityClient.Response(200,JSON.writeValueAsBytes(u));
            },()->{},()->{});
            broker=new OmOrdinarySessionBroker(credentials,i->{assertEquals("om-locked",i);return nativeClient;},clock);
        }
        public void close()throws Exception{org.springframework.web.context.request.RequestContextHolder.resetRequestAttributes();allowCreate.countDown();if(broker!=null)broker.close();http.stop(0);try(var files=Files.walk(credentialRoot)){for(var p:files.sorted(Comparator.reverseOrder()).toList())Files.delete(p);}}
    }
    static Set<String> keys(JsonNode n){var r=new HashSet<String>();n.fieldNames().forEachRemaining(r::add);return r;}
    Case start()throws Exception{
        active=new Case();if(Boolean.getBoolean("m14.secure.bearer"))installRealBearer();super.setup();active.installBroker();initialize();
        sql(Files.readString(Path.of(System.getProperty("m14.sql"))).replaceAll(" ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin", ""));
        String bid=jdbc.queryForObject("SELECT id FROM om_identity_binding",String.class);var ref=active.credentials.createExclusive(UUID.fromString(bid),1);jdbc.update("UPDATE om_identity_binding SET credential_ref=?,sync_state='IN_SYNC' WHERE id=?",ref.opaqueRef(),bid);
        var targets=reservation();var config=Map.of("capabilities",List.of("TARGET_CREATE","TARGET_READ"),"targets",targets);String hash=OntologyCanonicalJson.hash(config);
        jdbc.update("INSERT INTO ontology_modeling_permission(tenant_id,space_id,project_id,account_id,instance_id,version,config_json,policy_hash,native_task_id,sync_state,applied_hash,native_state_json,updated_by,updated_at) VALUES('1',?,?,'2','om-locked',1,?,?,NULL,'IN_SYNC',?,'{}','1',CURRENT_TIMESTAMP)",space,p1,OntologyCanonicalJson.write(config),hash,hash);
        roots=new JdbcOntologyRootGlossaryStore(source,new JdbcAtlasOntologyAuthority(source),this::currentSubject,permissions,clock);
        consumer=new OntologyModelingIdentityConsumer(store,active.broker,clock);consumer.setModelingPermissions(permissions);consumer.setRootGlossaries(roots);registry=new OmInstanceRegistry("",()->consumer);rootService=new OntologyRootGlossaryService(roots,registry,clock);return active;
    }
    Map<String,Object> context(String p){return rootService.context(p,"2","om-locked");}
    Map<String,Object> createPlan(String p,String name){var cut=JdbcOntologyRootGlossaryStore.obj(context(p).get("cut"));var request=new OntologyRootGlossaryContracts.Prepare(p,"2","om-locked","CREATE",name,"独立根说明",null,List.of(),cut,null);var result=rootService.prepare(request);originalRequests.put((String)result.get("planId"),request);return result;}
    OntologyRootGlossaryContracts.Confirm confirm(Map<String,Object> plan,String key){return new OntologyRootGlossaryContracts.Confirm((String)plan.get("planId"),(String)plan.get("planHash"),key,JdbcOntologyRootGlossaryStore.obj(plan.get("cut")));}
    Map<String,Object> create(String p,String name,String key){return rootService.confirm(confirm(createPlan(p,name),key));}
    Map<String,Object> obj(Object v){return JdbcOntologyRootGlossaryStore.obj(v);}
    String state(Map<String,Object> value){return (String)value.get("state");}
    String id(Map<String,Object> value){return (String)value.get("intentId");}
    void secondMapping(){confirmed(input(p2,"CONFIGURE",null),"SECOND_LOCAL_MAPPING");}
    void secondPolicyFixture(){jdbc.update("INSERT INTO ontology_modeling_permission(tenant_id,space_id,project_id,account_id,instance_id,version,config_json,policy_hash,native_task_id,sync_state,applied_hash,native_state_json,updated_by,updated_at) SELECT tenant_id,space_id,?,account_id,instance_id,version,config_json,policy_hash,NULL,'IN_SYNC',policy_hash,'{}',updated_by,updated_at FROM ontology_modeling_permission WHERE project_id=?",p2,p1);}
    String journal(String intent){return jdbc.queryForObject("SELECT journal_json FROM ontology_root_glossary_intent WHERE id=?",String.class,intent);}
    void record(String method,Case c){RESULTS.add(Map.of("method",method,"resourcePOST",c.creates.get(),"resourceGET",c.requests.stream().filter(x->x.startsWith("GET ")).count(),"ordinaryLogin",c.login.get(),"brokerSelf",c.self.get(),"adminFallback",c.fallback.get()));assertEquals(0,c.fallback.get());}

    void diagnoseCreate()throws Exception{
        try(Case f=start()){
            var plan=createPlan(p1,"DiagnosticRoot");System.out.println("DIAG_PREPARE_OK");
            var accepted=roots.confirm(confirm(plan,"DIAGNOSTIC_ROOT_ONCE"));System.out.println("DIAG_CONFIRM_OK");
            var a=roots.claim(id(accepted));System.out.println("DIAG_CLAIM_OK");
            var client=registry.rootGlossaryClient(a);System.out.println("DIAG_CLIENT_OK");
            client.create();System.out.println("DIAG_CREATE_OK");
        }
    }

    void diagnoseReuse()throws Exception{
        try(Case f=start()){
            var original=create(p1,"DiagnosticFormalRoot","DIAGNOSTIC_FORMAL");System.out.println("DIAG_CREATE_STATE "+state(original));
            secondMapping();System.out.println("DIAG_KNOWN_COUNT "+roots.knownTargets(p2,"2","om-locked").size());System.out.println("DIAG_BINDING_STATE "+roots.binding(p1).get("state"));System.out.println("DIAG_BUSINESS_REQUESTS "+f.requests.size());
            var row=jdbc.queryForMap("SELECT * FROM ontology_root_glossary_intent WHERE id=?",id(original));
            var method=JdbcOntologyRootGlossaryStore.class.getDeclaredMethod("boundSource",Map.class);method.setAccessible(true);
            try{method.invoke(roots,row);System.out.println("DIAG_BOUND_SOURCE_OK");}catch(java.lang.reflect.InvocationTargetException e){Throwable cause=e.getCause();System.out.println("DIAG_BOUND_SOURCE_CLASS "+cause.getClass().getSimpleName());if(cause instanceof OntologyApiException)System.out.println("DIAG_BOUND_SOURCE_CODE "+cause.getMessage());throw e;}
        }
    }

    @Test void chineseCreateFullDefaultsOwnTransitionOriginalUnknownAndSameKey()throws Exception{
        try(Case f=start()){
            f.integerVersion=true;effect=(a,p,g)->{g.beforeSend(OntologyCanonicalJson.hash("OLD_UNKNOWN_ONLY"));throw new IllegalStateException("OLD_ACK_LOST");};var qplan=permissions.prepareQualification(new OntologyModelingPermissionContracts.QualificationPrepare(p1,"2","om-locked",1,List.of("TARGET_READ"),List.of("glossary")),"OLD_Q_PREPARE");var q=permissions.confirmQualification(confirmation(qplan,"OLD_Q_KEY"),"OLD_Q_CONFIRM");permissions.executeOnce(q.runId());String old=jdbc.queryForObject("SELECT journal_json FROM ontology_modeling_qualification WHERE id=?",String.class,q.runId());assertEquals("UNKNOWN",jdbc.queryForObject("SELECT state FROM ontology_modeling_qualification WHERE id=?",String.class,q.runId()));
            long before=jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p1);var plan=createPlan(p1,"  中文根词汇表  ");var request=confirm(plan,"ROOT_CREATE_ONCE");var result=rootService.confirm(request);assertEquals("SUCCEEDED",state(result));assertEquals(before+1,jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p1));assertEquals("BOUND",obj(result.get("localBinding")).get("state"));assertEquals("BOUND",roots.readinessBinding(p1).get("state"));String uuid=(String)obj(result.get("native")).get("glossaryId");assertTrue(f.objects.containsKey(uuid));var durableRaw=JSON.readTree(journal(id(result))).path("rawCreateResponse");assertEquals(201,durableRaw.path("status").asInt());assertArrayEquals(JSON.writeValueAsBytes(f.objects.get(uuid)),Base64.getDecoder().decode(durableRaw.path("bodyBase64").asText()));assertEquals("中文根词汇表",f.objects.get(uuid).path("name").asText());assertEquals(1,f.creates.get());assertEquals(1,f.requests.stream().filter(x->x.startsWith("GET ")).count());assertTrue(f.queries.stream().filter(Objects::nonNull).anyMatch(x->x.contains("namespaces%2ContologyConfiguration")));
            assertEquals("SUCCEEDED",state(rootService.confirm(request)));assertEquals("SUCCEEDED",state(rootService.get(p1,"2","om-locked",id(result))));assertEquals(1,f.creates.get());assertEquals(old,jdbc.queryForObject("SELECT journal_json FROM ontology_modeling_qualification WHERE id=?",String.class,q.runId()));WIRE.put("selection",Map.of("actorId","1","accountId","2","instanceId","om-locked","scope",context(p1).get("scope")));
            WIRE.put("context",context(p1));WIRE.put("reviewerContext",context(p1));WIRE.put("reviewers",rootService.reviewers(p1,"2","om-locked"));WIRE.put("prepareRequest",originalRequests.get(plan.get("planId")));WIRE.put("plan",plan);WIRE.put("intent",rootService.get(p1,"2","om-locked",id(result)));WIRE.put("catalogContext",context(p1));WIRE.put("catalog",rootService.candidates(p1,"2","om-locked"));record("chineseCreateFullDefaultsOwnTransitionOriginalUnknownAndSameKey",f);jdbc.update("UPDATE om_identity_binding SET sync_state='UNKNOWN'");WIRE.put("blockedContext",context(p1));
        }
    }
    @Test void readOnlyReuseAcrossMappedProjectAndMetadataUpdateKeepsReceipt()throws Exception{
        try(Case f=start()){
            var original=create(p1,"FirstFormalRoot","FIRST_ROOT");assertEquals("SUCCEEDED",state(original));assertEquals("BOUND",roots.readinessBinding(p1).get("state"));String uuid=(String)obj(original.get("native")).get("glossaryId");secondMapping();assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM ontology_modeling_permission WHERE project_id=?",Integer.class,p2));
            jdbc.update("UPDATE ontology_scope_grant SET product_role='USER' WHERE account_id='4' AND scope_id=?",p1);subject=new OntologyAuthorization.Subject("4","1");
            var readable=rootService.candidates(p2,"2","om-locked");assertEquals(uuid,((Map<?,?>)((List<?>)readable.get("candidates")).getFirst()).get("glossaryId"));int beforeNoRole=f.requests.size(),selfBeforeNoRole=f.self.get();
            jdbc.update("UPDATE ontology_scope_grant SET active=0 WHERE account_id='4' AND scope_id=?",p1);assertTrue(((List<?>)rootService.candidates(p2,"2","om-locked").get("candidates")).isEmpty());assertEquals(beforeNoRole,f.requests.size());assertEquals(selfBeforeNoRole,f.self.get());subject=new OntologyAuthorization.Subject("1","1");
            var candidates=rootService.candidates(p2,"2","om-locked");var candidate=(Map<String,Object>)((List<?>)candidates.get("candidates")).getFirst();assertEquals(uuid,candidate.get("glossaryId"));int posts=f.creates.get();var reuseRequest=new OntologyRootGlossaryContracts.Prepare(p2,"2","om-locked","REUSE",null,null,uuid,List.of(),obj(context(p2).get("cut")),(String)candidate.get("observationHash"));var plan=rootService.prepare(reuseRequest);var bound=rootService.confirm(confirm(plan,"REUSE_ONLY"));assertEquals("SUCCEEDED",state(bound));assertEquals("NOT_REQUIRED",obj(bound.get("native")).get("createState"));assertEquals(posts,f.creates.get());
            var projects=new OntologyProjectService(source,new JdbcAtlasOntologyAuthority(source),()->subject);long v=jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p2);projects.update(new OntologyProjectContracts.ProjectWrite(p2,space,"Metadata changed","description only",List.of(),v,null,null,null,null),"METADATA_ONLY");var b=roots.readinessBinding(p2);assertEquals("BOUND",b.get("state"));assertEquals(uuid,b.get("glossaryId"));assertEquals(posts,f.creates.get());WIRE.put("reuseSelection",Map.of("actorId","1","accountId","2","instanceId","om-locked","scope",plan.get("scope")));WIRE.put("reuseContext",context(p2));WIRE.put("reusePrepareRequest",reuseRequest);WIRE.put("reusePlan",plan);WIRE.put("reuseIntent",bound);record("readOnlyReuseAcrossMappedProjectAndMetadataUpdateKeepsReceipt",f);
        }
    }
    @Test void native403AndLateRevocationPreserveAcceptedFactWithoutBinding()throws Exception{
        try(Case f=start()){
            f.denyCreate=true;var result=create(p1,"DeniedRoot","DENIED_CREATE");assertEquals("UNKNOWN",state(result));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p1));assertEquals("DISPATCHED",obj(result.get("native")).get("createState"));assertEquals(1,f.creates.get());rootService.byKey(p1,"2","om-locked","DENIED_CREATE");assertEquals(1,f.creates.get());record("native403AndLateRevocationPreserveAcceptedFactWithoutBinding/403",f);jdbc.update("UPDATE om_identity_binding SET sync_state='UNKNOWN'");try{WIRE.put("blockedContext",context(p1));}catch(OntologyApiException blocked){WIRE.put("blockedContextErrorCode",blocked.getMessage());}
        }
        try(Case f=start()){
            f.afterCreate=()->jdbc.update("UPDATE ontology_scope_grant SET active=0 WHERE account_id='2'");var result=create(p1,"AcceptedBeforeRevoke","LATE_REVOKE");assertEquals("UNKNOWN",state(result));var nativeFact=obj(result.get("native"));assertEquals("ACKNOWLEDGED",nativeFact.get("createState"));assertNotNull(nativeFact.get("responseHash"));assertNotNull(nativeFact.get("glossaryId"));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p1));assertEquals(1,f.creates.get());assertEquals(0,f.requests.stream().filter(x->x.startsWith("GET ")).count());record("native403AndLateRevocationPreserveAcceptedFactWithoutBinding/late-revoke",f);
        }
    }
    @Test void localCasFailurePointerDriftRequiresNewExplicitContextAndOnlyReads()throws Exception{
        try(Case f=start()){
            f.integerVersion=true;jdbc.execute("ALTER TABLE ontology_project ADD CONSTRAINT finite_local_fail CHECK(om_target IS NULL)");var result=create(p1,"KnownRootLocalFailure","LOCAL_ONLY");assertEquals("NEEDS_LOCAL_BIND",state(result));String intent=id(result),nativeUuid=(String)obj(result.get("native")).get("glossaryId");var oldContext=obj(result.get("localBindContext"));jdbc.execute("ALTER TABLE ontology_project DROP CONSTRAINT finite_local_fail");
            String other=UUID.randomUUID().toString();jdbc.update("UPDATE ontology_project SET om_target=?,version=version+1 WHERE id=?",OntologyCanonicalJson.write(Map.of("instanceId","om-locked","glossaryId",other)),p1);var current=rootService.get(p1,"2","om-locked",intent);assertTrue(((List<?>)current.get("allowedActions")).contains("BIND_LOCAL_ONLY"));WIRE.put("localBindBeforeContext",context(p1));var fresh=obj(current.get("localBindContext"));assertNotEquals(oldContext.get("contextHash"),fresh.get("contextHash"));int reads=f.requests.size(),posts=f.creates.get();assertThrows(OntologyApiException.class,()->rootService.bindLocal(p1,"2","om-locked",intent,new OntologyRootGlossaryContracts.BindLocal((String)oldContext.get("contextHash"))));assertEquals(reads,f.requests.size());long v=jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p1);var done=rootService.bindLocal(p1,"2","om-locked",intent,new OntologyRootGlossaryContracts.BindLocal((String)fresh.get("contextHash")));assertEquals("SUCCEEDED",state(done));assertEquals(v+1,jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p1));assertEquals(nativeUuid,obj(done.get("native")).get("glossaryId"));assertEquals(posts,f.creates.get());assertEquals(reads+1,f.requests.size());WIRE.put("localBindAfterContext",context(p1));WIRE.put("localBindBefore",current);WIRE.put("localBindAfter",done);WIRE.put("localBindContext",fresh);record("localCasFailurePointerDriftRequiresNewExplicitContextAndOnlyReads",f);
        }
    }
    @Test void crossProjectSameNameAndUnknownNeverCreateOrAdoptAgain()throws Exception{
        if(!Boolean.getBoolean("m14.malformed.only")){
        try(Case f=start()){
            secondMapping();secondPolicyFixture();var a=createPlan(p1,"ConcurrentName");var b=createPlan(p2,"ConcurrentName");f.holdCreate=true;var pool=Executors.newSingleThreadExecutor();try{var first=pool.submit(()->rootService.confirm(confirm(a,"NAME_FIRST")));assertTrue(f.createEntered.await(5,TimeUnit.SECONDS));assertThrows(RuntimeException.class,()->rootService.confirm(confirm(b,"NAME_SECOND")));assertEquals(1,f.creates.get());f.allowCreate.countDown();assertEquals("SUCCEEDED",state(first.get(10,TimeUnit.SECONDS)));}finally{f.allowCreate.countDown();pool.shutdownNow();}assertEquals(1,f.creates.get());record("crossProjectSameNameAndUnknownNeverCreateOrAdoptAgain/concurrent",f);
        }
        try(Case f=start()){
            var beforeUnknown=createPlan(p1,"PreparedBeforeUnknown");f.malformedCreate=true;var result=create(p1,"UnknownName","UNKNOWN_NAME");assertEquals("UNKNOWN",state(result));assertThrows(OntologyApiException.class,()->createPlan(p1,"DifferentNameAfterUnknown"));assertThrows(OntologyApiException.class,()->rootService.confirm(confirm(beforeUnknown,"NEW_KEY_FROM_OLD_PREVIEW")));subject=new OntologyAuthorization.Subject("4","1");assertThrows(OntologyApiException.class,()->createPlan(p1,"DifferentActorAfterUnknown"));subject=new OntologyAuthorization.Subject("1","1");secondMapping();secondPolicyFixture();assertThrows(OntologyApiException.class,()->createPlan(p2,"UnknownName"));assertEquals(1,f.creates.get());assertEquals("UNKNOWN",state(rootService.get(p1,"2","om-locked",id(result))));record("crossProjectSameNameAndUnknownNeverCreateOrAdoptAgain/unknown",f);
        }
        }
        for(String invalidKind:List.of("duplicate","trailing"))try(Case f=start()){
            f.invalidCreateKind=invalidKind;var result=create(p1,"AmbiguousAck_"+invalidKind,"AMBIGUOUS_"+invalidKind);assertEquals("UNKNOWN",state(result));var n=obj(result.get("native"));assertEquals("ACKNOWLEDGED",n.get("createState"));assertNull(n.get("glossaryId"));assertEquals("NOT_RUN",n.get("readbackState"));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p1));
            var raw=JSON.readTree(journal(id(result))).path("rawCreateResponse");assertEquals(201,raw.path("status").asInt());assertArrayEquals(f.lastRaw201,Base64.getDecoder().decode(raw.path("bodyBase64").asText()));assertEquals(OntologyCanonicalJson.sha256(f.lastRaw201),raw.path("responseHash").asText());assertEquals(1,f.creates.get());assertEquals(0,f.requests.stream().filter(x->x.startsWith("GET ")).count());
            var repeated=rootService.confirm(new OntologyRootGlossaryContracts.Confirm((String)result.get("planId"),(String)result.get("planHash"),(String)result.get("operationKey"),obj(result.get("originalCut"))));assertEquals("UNKNOWN",state(repeated));assertEquals(Boolean.TRUE,repeated.get("replayed"));assertEquals(1,f.creates.get());assertEquals(0,f.requests.stream().filter(x->x.startsWith("GET ")).count());record("crossProjectSameNameAndUnknownNeverCreateOrAdoptAgain/"+invalidKind,f);
        }
    }
    @Test void forgedAdmissionAndForgedSucceededCatalogRecordCannotReachHttp()throws Exception{
        try(Case f=start()){
            var plan=createPlan(p1,"ForgedAdmissionRoot");var accepted=roots.confirm(confirm(plan,"FORGED_RECORD"));var a=roots.claim(id(accepted));assertNotNull(a);var foreign=new OntologyRootGlossaryContracts.Admission(a.intentId(),a.scope(),a.actorId(),a.accountId(),a.instanceId(),"REUSE_READ",a.owner(),a.cut(),a.exactName(),UUID.randomUUID().toString());assertThrows(RuntimeException.class,()->registry.rootGlossaryClient(foreign).read());assertTrue(f.requests.isEmpty());assertEquals(0,f.self.get());
            String unknown=UUID.randomUUID().toString();jdbc.update("UPDATE ontology_root_glossary_intent SET state='SUCCEEDED',journal_json=? WHERE id=?",OntologyCanonicalJson.write(Map.of("native",Map.of("glossaryId",unknown))),id(accepted));assertTrue(((List<?>)rootService.candidates(p1,"2","om-locked").get("candidates")).isEmpty());assertTrue(f.requests.isEmpty());assertEquals(0,f.self.get());record("forgedAdmissionAndForgedSucceededCatalogRecordCannotReachHttp",f);
        }
    }
    @Test void publicParserCurrentUserAndDuplicateQueryRejectBeforeNative()throws Exception{
        try(Case f=start()){
            var mvc=MockMvcBuilders.standaloneSetup(new OntologyRootGlossaryController(rootService)).setControllerAdvice(new OntologyModelingAccessExceptionAdvice()).build();var cut=obj(context(p1).get("cut"));var request=new OntologyRootGlossaryContracts.Prepare(p1,"2","om-locked","CREATE","中文API根","说明",null,List.of(),cut,null);var raw=JSON.writeValueAsString(request);var good=mvc.perform(post("/admin-api/ontology/root-glossary/prepare").contentType("application/json").content(raw)).andReturn().getResponse();assertEquals(0,JSON.readTree(good.getContentAsString()).path("code").asInt(-1));
            var bad=(ObjectNode)JSON.readTree(raw);bad.put("credentialRef","FORBIDDEN_INPUT");var rejected=mvc.perform(post("/admin-api/ontology/root-glossary/prepare").contentType("application/json").content(bad.toString())).andReturn().getResponse();assertEquals(400,JSON.readTree(rejected.getContentAsString()).path("code").asInt());
            var duplicate=mvc.perform(get("/admin-api/ontology/root-glossary/context").queryParam("projectId",p1,p2).queryParam("accountId","2").queryParam("instanceId","om-locked")).andReturn().getResponse();assertEquals(400,JSON.readTree(duplicate.getContentAsString()).path("code").asInt());
            subject=new OntologyAuthorization.Subject("2","1");assertFalse((Boolean)context(p1).get("canManage"));assertThrows(OntologyApiException.class,()->rootService.prepare(request));assertTrue(f.requests.isEmpty());record("publicParserCurrentUserAndDuplicateQueryRejectBeforeNative",f);
        }
    }
    @Test void durableClaimCrashAndRepeatedKeyNeverReclaimOrDispatch()throws Exception{
        try(Case f=start()){
            var plan=createPlan(p1,"ClaimCrashRoot");var request=confirm(plan,"CLAIM_CRASH");var accepted=roots.confirm(request);assertNotNull(roots.claim(id(accepted)));assertNull(roots.claim(id(accepted)));var restarted=new JdbcOntologyRootGlossaryStore(source,new JdbcAtlasOntologyAuthority(source),this::currentSubject,permissions,clock);assertNull(restarted.claim(id(accepted)));assertEquals("RUNNING",state(restarted.confirm(request)));assertEquals("RUNNING",state(rootService.confirm(request)));assertTrue(f.requests.isEmpty());assertEquals(0,f.login.get());assertEquals(0,f.self.get());record("durableClaimCrashAndRepeatedKeyNeverReclaimOrDispatch",f);
        }
    }
    @Test void realBearerNestedRootAndProductionReadinessResolverKeepCurrentAuthority()throws Exception{
        assertTrue(Boolean.getBoolean("m14.secure.bearer"));
        try(Case f=start()){
            var tx=new org.springframework.transaction.support.TransactionTemplate(new org.springframework.jdbc.datasource.DataSourceTransactionManager(source));
            int prior=bearerChecks.get();
            tx.execute(status->{assertThrows(IllegalStateException.class,realBearer::get);return null;});
            assertEquals(prior,bearerChecks.get());
            var beans=new org.springframework.beans.factory.support.DefaultListableBeanFactory();beans.registerSingleton("actualRootStore",roots);
            var preparation=new OntologyP1Configuration().ontologyPreparationService(source,new JdbcAtlasOntologyAuthority(source),realBearer,beans.getBeanProvider(JdbcOntologyRootGlossaryStore.class));
            assertEquals("NOT_CONFIGURED",preparation.readiness(p1).steps().stream().filter(x->x.capability().equals("TARGET")).findFirst().orElseThrow().state());
            assertEquals(Boolean.TRUE,context(p1).get("canManage"));assertTrue(((List<?>)context(p1).get("allowedActions")).contains("PREPARE_CREATE"));assertTrue(f.requests.isEmpty());
            jdbc.execute("ALTER TABLE ontology_project ADD CONSTRAINT finite_nested_local_fail CHECK(om_target IS NULL)");
            var created=create(p1,"RealBearerLocalTarget","REAL_BEARER_LOCAL_ONCE");assertEquals("NEEDS_LOCAL_BIND",state(created));
            var original=rootService.byKey(p1,"2","om-locked","REAL_BEARER_LOCAL_ONCE");assertEquals(id(created),id(original));
            assertEquals("NEEDS_LOCAL_BIND",state(original));int before=f.requests.size();
            jdbc.execute("ALTER TABLE ontology_project DROP CONSTRAINT finite_nested_local_fail");
            var local=obj(original.get("localBindContext"));
            var done=rootService.bindLocal(p1,"2","om-locked",id(created),new OntologyRootGlossaryContracts.BindLocal((String)local.get("contextHash")));
            assertEquals("SUCCEEDED",state(done));assertEquals(before+1,f.requests.size());assertEquals(1,f.creates.get());
            assertEquals("READY",preparation.readiness(p1).steps().stream().filter(x->x.capability().equals("TARGET")).findFirst().orElseThrow().state());
            int after=f.requests.size();assertEquals("SUCCEEDED",state(rootService.byKey(p1,"2","om-locked","REAL_BEARER_LOCAL_ONCE")));assertEquals(after,f.requests.size());
            assertTrue(bearerChecks.get()>prior);jdbc.update("UPDATE om_identity_binding SET sync_state='UNKNOWN'");
            var blocked=context(p1);assertNull(blocked.get("identity"));assertNull(blocked.get("cut"));assertEquals(after,f.requests.size());
            record("realBearerNestedRootAndProductionReadinessResolverKeepCurrentAuthority",f);
        }
    }
    @Test void numericEntityKnown201UnknownHasOnlyExplicitSameUuidLocalReadback()throws Exception{
        assertTrue(OntologyRootGlossaryContracts.descriptionMatches("a：b；c","a&#xff1a;b&#65307;c"));
        for(String bad:List.of("changed business","a<b>b</b>；c","a&amp;#xff1a;b；c","a&colon;b；c","a:b；c"))
            assertFalse(OntologyRootGlossaryContracts.descriptionMatches("a：b；c",bad));
        for(String mode:List.of("SUCCESS","REVOKED","VERSION"))try(Case f=start()){
            f.encodeNumericDescription=true;
            var legacy=new LegacyRootDescriptionService(roots,registry,clock);
            var request=new OntologyRootGlossaryContracts.Prepare(p1,"2","om-locked","CREATE","NumericKnown201_"+mode,"说明：保留实体；不改业务。",null,List.of(),obj(context(p1).get("cut")),null);
            var plan=legacy.prepare(request);var confirmation=confirm(plan,"KNOWN201_"+mode);var stopped=legacy.confirm(confirmation);
            assertEquals("UNKNOWN",state(stopped));String originalId=id(stopped),oldJournal=journal(originalId);
            var oldJson=JSON.readTree(oldJournal);assertEquals("ROOT_VERIFY_ROOT_NATIVE_DESCRIPTION_CHANGED",oldJson.path("reason").asText());
            assertEquals("NOT_RUN",oldJson.path("native").path("readbackState").asText());String uuid=oldJson.path("native").path("glossaryId").asText();
            assertEquals(1,f.creates.get());assertEquals(1,f.requests.stream().filter(x->x.startsWith("GET ")).count());
            String frozenPlan=jdbc.queryForObject("SELECT snapshot_json FROM ontology_root_glossary_intent WHERE id=?",String.class,originalId);
            long originalRowVersion=jdbc.queryForObject("SELECT version FROM ontology_root_glossary_intent WHERE id=?",Long.class,originalId);
            roots=new JdbcOntologyRootGlossaryStore(source,new JdbcAtlasOntologyAuthority(source),this::currentSubject,permissions,clock);
            consumer=new OntologyModelingIdentityConsumer(store,f.broker,clock);consumer.setModelingPermissions(permissions);consumer.setRootGlossaries(roots);registry=new OmInstanceRegistry("",()->consumer);rootService=new OntologyRootGlossaryService(roots,registry,clock);
            var available=rootService.byKey(p1,"2","om-locked","KNOWN201_"+mode);
            assertEquals("UNKNOWN",state(available));assertEquals("ROOT_KNOWN_201_DESCRIPTION_ENCODING_READBACK_REQUIRED",available.get("reason"));
            assertTrue(((List<?>)available.get("allowedActions")).contains("BIND_LOCAL_ONLY"));assertEquals("0.1",obj(available.get("native")).get("nativeVersion"));
            assertNull(obj(available.get("native")).get("observedAt"));assertEquals(oldJournal,journal(originalId));assertEquals(1,f.creates.get());
            var local=obj(available.get("localBindContext"));int count=f.requests.size();
            if(mode.equals("REVOKED")){
                jdbc.update("UPDATE ontology_scope_grant SET active=0 WHERE account_id='2'");
                assertThrows(RuntimeException.class,()->rootService.bindLocal(p1,"2","om-locked",originalId,new OntologyRootGlossaryContracts.BindLocal((String)local.get("contextHash"))));
                assertEquals(count,f.requests.size());assertEquals(oldJournal,journal(originalId));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p1));
            }else{
                if(mode.equals("VERSION"))f.readVersion=0.2;
                long pv=jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p1);
                var result=rootService.bindLocal(p1,"2","om-locked",originalId,new OntologyRootGlossaryContracts.BindLocal((String)local.get("contextHash")));
                assertEquals(count+1,f.requests.size());assertEquals(1,f.creates.get());
                var saved=JSON.readTree(journal(originalId));var history=saved.path("known201DescriptionEncodingContinuation");
                assertEquals("UNKNOWN",history.path("stateBefore").asText());assertEquals("ROOT_VERIFY_ROOT_NATIVE_DESCRIPTION_CHANGED",history.path("reasonBefore").asText());
                assertEquals(originalRowVersion,history.path("rowVersionBefore").asLong());assertEquals(OntologyCanonicalJson.hash(OntologyCanonicalJson.read(oldJournal,Map.class)),history.path("journalHashBefore").asText());
                assertEquals(oldJson.path("rawCreateResponse"),saved.path("rawCreateResponse"));assertEquals(oldJson.path("dispatchHash"),saved.path("dispatchHash"));
                assertEquals(frozenPlan,jdbc.queryForObject("SELECT snapshot_json FROM ontology_root_glossary_intent WHERE id=?",String.class,originalId));
                if(mode.equals("SUCCESS")){assertEquals("SUCCEEDED",state(result));assertEquals(uuid,obj(result.get("native")).get("glossaryId"));assertEquals(pv+1,jdbc.queryForObject("SELECT version FROM ontology_project WHERE id=?",Long.class,p1));}
                else{assertEquals("UNKNOWN",state(result));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p1));}
                int after=f.requests.size();assertThrows(OntologyApiException.class,()->rootService.bindLocal(p1,"2","om-locked",originalId,new OntologyRootGlossaryContracts.BindLocal((String)local.get("contextHash"))));
                assertEquals(after,f.requests.size());assertEquals(1,f.creates.get());assertTrue((Boolean)rootService.confirm(confirmation).get("replayed"));assertEquals(after,f.requests.size());
            }
            record("numericEntityKnown201UnknownHasOnlyExplicitSameUuidLocalReadback/"+mode,f);
        }
    }
    @Test void nativeVersionDriftAfterCreateAckOrReusePreviewNeverBinds()throws Exception{
        try(Case f=start()){
            f.readVersion=0.2;var result=create(p1,"CreateAckVersionDrift","CREATE_VERSION_DRIFT");assertEquals("UNKNOWN",state(result));var n=obj(result.get("native"));assertEquals("ACKNOWLEDGED",n.get("createState"));assertNotNull(n.get("glossaryId"));assertNotEquals("VERIFIED",n.get("readbackState"));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p1));var raw=JSON.readTree(journal(id(result))).path("rawCreateResponse");assertArrayEquals(f.lastRaw201,Base64.getDecoder().decode(raw.path("bodyBase64").asText()));assertEquals(1,f.creates.get());assertEquals(1,f.requests.stream().filter(x->x.startsWith("GET ")).count());rootService.get(p1,"2","om-locked",id(result));assertEquals(1,f.creates.get());record("nativeVersionDriftAfterCreateAckOrReusePreviewNeverBinds/create",f);
        }
        try(Case f=start()){
            var original=create(p1,"ReuseVersionSource","REUSE_VERSION_SOURCE");assertEquals("SUCCEEDED",state(original));String uuid=(String)obj(original.get("native")).get("glossaryId");String originalJournal=journal(id(original));secondMapping();var list=rootService.candidates(p2,"2","om-locked");var candidate=(Map<String,Object>)((List<?>)list.get("candidates")).getFirst();assertEquals("0.1",candidate.get("nativeVersion"));var request=new OntologyRootGlossaryContracts.Prepare(p2,"2","om-locked","REUSE",null,null,uuid,List.of(),obj(context(p2).get("cut")),(String)candidate.get("observationHash"));var plan=rootService.prepare(request);int before=f.creates.get();f.readVersion=0.2;var result=rootService.confirm(confirm(plan,"REUSE_VERSION_DRIFT"));assertNotEquals("SUCCEEDED",state(result));assertNotEquals("VERIFIED",obj(result.get("native")).get("readbackState"));assertNull(jdbc.queryForObject("SELECT om_target FROM ontology_project WHERE id=?",String.class,p2));assertEquals(before,f.creates.get());assertEquals(originalJournal,journal(id(original)));assertEquals(uuid,obj(result.get("native")).get("glossaryId"));record("nativeVersionDriftAfterCreateAckOrReusePreviewNeverBinds/reuse",f);
        }
    }

}
