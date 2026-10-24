package cn.iocoder.atlas.module.infra.ontology;

import cn.iocoder.atlas.module.infra.ontology.om.*;
import cn.iocoder.atlas.module.infra.ontology.omidentity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpServer;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static cn.iocoder.atlas.module.infra.ontology.OntologyModelingPermissionContracts.*;

/** Real installed Store, Consumer, Registry, Broker, Adapter and SDK. H2/IAM/applied-policy/native server are fixtures. */
class OntologyModelingWriteDenialTest extends OntologyModelingPermissionTest {
    static final ObjectMapper JSON = new ObjectMapper();
    static final String G = "bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb";
    static final List<Map<String,Object>> RESULTS = new ArrayList<>();
    Case active;

    @Override JdbcOntologyModelingAccessStore newStore() {
        var value = new JdbcOntologyModelingAccessStore(source,new JdbcAtlasOntologyAuthority(source),()->subject,clock,
                List.of(new OntologyModelingAccessContracts.ApprovedInstance("om-locked","owned fake OM",active.origin(),true)));
        value.setLegacyPresence((i,s,a)->legacy.get().present(i,s,a));
        return value;
    }
    @Override JdbcOntologyModelingPermissionStore newPermissionStore() {
        var consumer = new OntologyModelingIdentityConsumer(store,active.broker,clock);
        var registry = new OmInstanceRegistry("",()->consumer); // no legacy registry or management credential
        var adapter = new OntologyModelingQualificationNativeAdapter(registry::qualifiedClient,clock);
        var value = new JdbcOntologyModelingPermissionStore(source,new JdbcAtlasOntologyAuthority(source),()->subject,store,
                (scope,account,instance,identity,version,caps,targets)->new Compilation(
                        Map.of("name","AppliedPolicyFixture","rules",caps),
                        OntologyCanonicalJson.hash(Map.of("scope",scope,"version",version,"caps",caps,"targets",targets)),List.of(),List.of()),
                adapter,clock);
        consumer.setModelingPermissions(value);
        value.setQualificationKick(id->kicks.incrementAndGet()); // exact execution is explicit in each method
        return value;
    }

    final class Case implements AutoCloseable {
        final HttpServer http;
        final Path credentialRoot;
        final OmUserCredentialStore credentials;
        OmOrdinarySessionBroker broker;
        final List<String> requests = new ArrayList<>();
        final List<Boolean> ordinaryBusinessBearers = new ArrayList<>();
        final List<Integer> businessStatuses = new ArrayList<>();
        final AtomicInteger ordinaryLogin = new AtomicInteger(), ordinarySelf = new AtomicInteger(), managementFallback = new AtomicInteger();
        final List<String> nativePaths = new ArrayList<>();
        String token;
        Case() throws Exception {
            http=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
            credentialRoot=Files.createTempDirectory("m13-write-denial-owned-");
            Files.setPosixFilePermissions(credentialRoot,PosixFilePermissions.fromString("rwx------"));
            credentials=new OmUserCredentialStore(credentialRoot);
            http.createContext("/",exchange->{
                String method=exchange.getRequestMethod(), path=exchange.getRequestURI().getRawPath();
                requests.add(method+" "+path);
                String authorization=exchange.getRequestHeaders().getFirst("Authorization");
                boolean ordinary=token!=null&&("Bearer "+token).equals(authorization);
                ordinaryBusinessBearers.add(ordinary);
                exchange.getRequestBody().readAllBytes();
                // This fixture's only native grant is ViewBasic for one existing glossary. It never creates G.
                int status=ordinary&&method.equals("GET")&&path.equals("/api/v1/glossaries/"+G)?200:403;
                businessStatuses.add(status);
                byte[] raw=(status==200?glossary().toString():"{\"message\":\"operation denied by finite ViewBasic-only fixture\"}").getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type","application/json");
                exchange.sendResponseHeaders(status,raw.length);exchange.getResponseBody().write(raw);exchange.close();
            });
            http.start();
        }
        URI origin(){return URI.create("http://127.0.0.1:"+http.getAddress().getPort());}
        ObjectNode glossary(){return JSON.createObjectNode().put("id",G).put("name","ApprovedExistingGlossary")
                .put("fullyQualifiedName","ApprovedExistingGlossary").put("version",0.1).put("deleted",false)
                .set("reviewers",JSON.createArrayNode());}
        void installBroker() {
            var nativeClient=new NativeOmIdentityClient((method,path,body)->{
                managementFallback.incrementAndGet();throw new AssertionError("management fallback forbidden");
            },(method,path,body,bearer)->{
                nativePaths.add(method+" "+path);
                if(method.equals("POST")&&path.equals("/v1/users/login")){
                    assertNull(bearer);ordinaryLogin.incrementAndGet();
                    String name=jdbc.queryForObject("SELECT native_name FROM om_identity_binding",String.class);
                    String email=jdbc.queryForObject("SELECT native_email FROM om_identity_binding",String.class);
                    var input=JSON.readTree(body);assertEquals(email,input.path("email").asText());assertFalse(input.path("password").asText().isBlank());
                    long exp=clock.instant().plusSeconds(600).getEpochSecond();
                    var claims=JSON.createObjectNode().put("sub",name).put("username",name).put("email",email)
                            .put("isBot",false).put("tokenType","OM_USER").put("iat",clock.instant().getEpochSecond()).put("exp",exp);
                    claims.putArray("roles");var encoder=Base64.getUrlEncoder().withoutPadding();
                    token=encoder.encodeToString("{\"alg\":\"RS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8))+"."
                            +encoder.encodeToString(JSON.writeValueAsBytes(claims))+".c3ludGhldGljLXNpZ25hdHVyZQ";
                    return new NativeOmIdentityClient.Response(200,JSON.writeValueAsBytes(JSON.createObjectNode().put("tokenType","Bearer")
                            .put("accessToken",token).put("refreshToken","finite-refresh-not-consumed").put("expiryDuration",exp*1000L)));
                }
                assertEquals("GET",method);assertEquals("/v1/users/loggedInUser?fields=roles,teams",path);
                assertEquals(token,new String(bearer));ordinarySelf.incrementAndGet();
                var user=JSON.createObjectNode().put("id",nativeId)
                        .put("name",jdbc.queryForObject("SELECT native_name FROM om_identity_binding",String.class))
                        .put("email",jdbc.queryForObject("SELECT native_email FROM om_identity_binding",String.class))
                        .put("isBot",false).put("isAdmin",false).put("deleted",false);
                return new NativeOmIdentityClient.Response(200,JSON.writeValueAsBytes(user));
            },()->{},()->{});
            broker=new OmOrdinarySessionBroker(credentials,instance->{assertEquals("om-locked",instance);return nativeClient;},clock);
        }
        @Override public void close() throws Exception {
            if(broker!=null)broker.close();http.stop(0);
            try(var files=Files.walk(credentialRoot)){for(Path p:files.sorted(Comparator.reverseOrder()).toList())Files.delete(p);}
        }
    }
    Case start() throws Exception {
        active=new Case();super.setup();active.installBroker();initialize();
        UUID binding=UUID.fromString(jdbc.queryForObject("SELECT id FROM om_identity_binding",String.class));
        var ref=active.credentials.createExclusive(binding,1);
        jdbc.update("UPDATE om_identity_binding SET credential_ref=? WHERE id=?",ref.opaqueRef(),binding.toString());
        return active;
    }
    void markPolicyAppliedFixture(PermissionIntent intent){
        // An already applied policy/identity is fixture baseline, not a native-policy synchronization assertion.
        jdbc.update("UPDATE om_identity_task SET state='SUCCEEDED',checkpoint='EFFECT_CONFIRMED',lease_owner=NULL,lease_deadline=NULL WHERE id=?",intent.nativeTaskId());
        jdbc.update("UPDATE om_identity_binding SET sync_state='IN_SYNC'");
        jdbc.update("UPDATE ontology_modeling_permission SET sync_state='IN_SYNC',applied_hash=policy_hash");
    }
    QualificationRun run(List<String> caps,List<String> targetIds,String key){
        long version=permissions.get(p1,"2","om-locked").permissionVersion();
        var plan=permissions.prepareQualification(new QualificationPrepare(p1,"2","om-locked",version,caps,targetIds),"prepare-"+key);
        return permissions.confirmQualification(confirmation(plan,key),"confirm-"+key);
    }
    void assertNoSecondSend(Case f,String runId,int sent){
        assertFalse(permissions.executeOnce(runId));assertEquals(sent,f.requests.size());
        assertEquals(0,f.managementFallback.get());assertEquals(1,f.ordinaryLogin.get());
        assertEquals(0,jdbc.queryForObject("SELECT COUNT(*) FROM ontology_modeling_inspection",Integer.class));
    }
    @Test void om403WriteRetainsDispatchAndNeverFallsBackOrReplays() throws Exception {
        try(Case f=start()){
            var intent=permissions.confirm(confirmation(permissionPlan(),"denied-policy"),"policy-confirm");markPolicyAppliedFixture(intent);
            var run=run(List.of("TARGET_CREATE"),List.of("glossary"),"om403-write");
            assertTrue(permissions.executeOnce(run.runId()));
            var result=permissions.result(p1,run.runId(),null);
            assertEquals("UNKNOWN",result.state());assertEquals(1,result.steps().size());
            assertEquals("DISPATCHED",result.steps().getFirst().state());assertNotNull(result.steps().getFirst().requestHash());
            assertNull(result.steps().getFirst().nativeId());assertNull(result.steps().getFirst().evidenceRef());
            assertEquals(List.of("POST /api/v1/glossaries"),f.requests);assertEquals(List.of(403),f.businessStatuses);assertEquals(List.of(true),f.ordinaryBusinessBearers);assertEquals(1,f.ordinarySelf.get());
            assertEquals(1,jdbc.queryForObject("SELECT COUNT(*) FROM ontology_modeling_audit WHERE action='M13_STEP_DISPATCH'",Integer.class));
            assertEquals("NOT_VERIFIED",permissions.get(p1,"2","om-locked").qualifications().stream().filter(q->q.capability().equals("TARGET_CREATE")).findFirst().orElseThrow().state());
            assertNoSecondSend(f,run.runId(),1);
            RESULTS.add(Map.of("method","om403WriteRetainsDispatchAndNeverFallsBackOrReplays","businessGET",0,"businessWriteAttempts",1,"businessWriteAccepted",0,"ordinaryLogin",1,"ordinarySelf",1,"dispatches",1,"acceptedStepACKs",0,"terminal","UNKNOWN"));
        }
    }
    @Test void onlyViewBasicReadSuccessNeverQualifiesReviewerWrite() throws Exception {
        try(Case f=start()){
            var existing=new Target("glossary","EXISTING_UUID","glossary",G,null,null);
            // Independently approved existing G is H2/native-DTO fixture. No create is sent by this ViewBasic-only principal.
            var baseline=Map.of("capabilities",List.of(),"targets",List.of(existing));String hash=OntologyCanonicalJson.hash(baseline);
            jdbc.update("INSERT INTO ontology_modeling_permission(tenant_id,space_id,project_id,account_id,instance_id,version,config_json,policy_hash,native_task_id,sync_state,applied_hash,native_state_json,updated_by,updated_at) VALUES('1',?,?,'2','om-locked',1,?,?,NULL,'IN_SYNC',?,'{}','1',CURRENT_TIMESTAMP)",space,p1,OntologyCanonicalJson.write(baseline),hash,hash);
            var plan=permissions.prepare(new PermissionPrepare(p1,"2","om-locked",1,List.of("TARGET_READ","REVIEWER_UPDATE"),List.of(existing),"REPLACE"),"view-basic-policy");
            var intent=permissions.confirm(confirmation(plan,"view-basic-policy"),"policy-confirm");markPolicyAppliedFixture(intent);
            var run=run(List.of("TARGET_READ","REVIEWER_UPDATE"),List.of("glossary"),"view-basic-read-and-denied-write");
            assertTrue(permissions.executeOnce(run.runId()));var result=permissions.result(p1,run.runId(),null);
            assertEquals("UNKNOWN",result.state());assertEquals(2,result.steps().size());
            var read=result.steps().stream().filter(s->s.capability().equals("TARGET_READ")).findFirst().orElseThrow();
            var write=result.steps().stream().filter(s->s.capability().equals("REVIEWER_UPDATE")).findFirst().orElseThrow();
            assertEquals("VERIFIED",read.state());assertEquals(G,read.nativeId());assertNotNull(read.evidenceRef());
            assertEquals("DISPATCHED",write.state());assertNull(write.nativeId());assertNull(write.evidenceRef());
            assertEquals(List.of("GET /api/v1/glossaries/"+G,"GET /api/v1/glossaries/"+G,"PATCH /api/v1/glossaries/"+G),f.requests);
            assertEquals(List.of(200,200,403),f.businessStatuses);assertEquals(List.of(true,true,true),f.ordinaryBusinessBearers);
            assertEquals(3,f.ordinarySelf.get());assertEquals(2,jdbc.queryForObject("SELECT COUNT(*) FROM ontology_modeling_audit WHERE action='M13_STEP_DISPATCH'",Integer.class));
            var matrix=permissions.get(p1,"2","om-locked").qualifications();
            assertEquals("VERIFIED",matrix.stream().filter(q->q.capability().equals("TARGET_READ")).findFirst().orElseThrow().state());
            assertEquals("NOT_VERIFIED",matrix.stream().filter(q->q.capability().equals("REVIEWER_UPDATE")).findFirst().orElseThrow().state());
            assertTrue(matrix.stream().filter(q->!q.capability().equals("TARGET_READ")).noneMatch(q->q.state().equals("VERIFIED")));
            assertNoSecondSend(f,run.runId(),3);
            RESULTS.add(Map.of("method","onlyViewBasicReadSuccessNeverQualifiesReviewerWrite","businessGET",2,"businessWriteAttempts",1,"businessWriteAccepted",0,"ordinaryLogin",1,"ordinarySelf",3,"dispatches",2,"acceptedStepACKs",1,"terminal","UNKNOWN"));
        }
    }
}
