import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import org.yaml.snakeyaml.Yaml;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import cn.iocoder.atlas.module.infra.ontology.OntologyCanonicalJson;

/** Five fixed SELECTs, one read-only connection. No product getter or Native IO. */
public final class DmM15FinalLocalStateRead {
 static final Path BASE=Path.of("/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/final-local-state-reader-r1");
 static final String S6="3f10ce21-57fe-4efe-91c6-c9a3e4cf04f6", SPACE="1447855d-bf9e-41d7-a9b3-ed641290d6a7", AC2="ebed8ab8-a686-47ee-a7cb-cb778617730d";
 static final String USER="2d0540a5-d6fd-4f24-a5a4-270eaa54dc26", BINDING="081dc4a9-a5a4-40e4-9141-634eda235409", INSTANCE="om-p1-isolated";
 static final String G="7daa15c1-5f4b-4272-bdcf-5575cca8ebe9", OLD_G="ab2c2d5f-05cb-4c5d-959e-a9f29d1fd4be";
 static final String INTENT="309706cd-7494-45a7-8b59-c709c4784d36", KEY="d24a8e23-5bf7-4de0-bd95-cd8d137fee17";
 static final String DISABLE="12b11c34-349c-4201-86c6-d3247decad4e", RESTORE="a37e3594-bfe4-42e9-8532-2e68404d8fa0";
 static final String ROOT_OLD="e0a61258-e126-4ceb-b344-cc1702fa1e98", ROOT_AC2="75149e86-6063-4216-89c1-4e8bff3c4fb4";
 static final ObjectMapper NATIVE=new ObjectMapper().enable(JsonParser.Feature.STRICT_DUPLICATE_DETECTION).enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);
 static int selects=0;
 static final String Q_REVIEWER="SELECT r.id,r.tenant_id,r.space_id,r.project_id,r.initiator,r.account_id,r.instance_id,r.glossary_id,r.operation_key,r.active_slot,r.request_hash,r.state,r.version,r.snapshot_json,r.journal_json,r.updated_at,(SELECT COUNT(*) FROM ontology_glossary_reviewer_intent x WHERE x.instance_id=r.instance_id AND x.glossary_id=r.glossary_id AND x.state IN('READY','RUNNING','UNKNOWN')) AS active_for_g FROM ontology_glossary_reviewer_intent r WHERE r.id='"+INTENT+"'";
 static final String Q_CURRENT="SELECT USER AS db_user,SYS_CONTEXT('USERENV','CURRENT_SCHEMA') AS db_schema,p.version AS project_version,p.om_target AS target_json,m.version AS configuration_version,m.enabled,b.id AS binding_id,b.native_user_id,b.binding_version,b.credential_epoch,b.desired_revision,b.state AS binding_state,b.sync_state,a.version AS ac2_project_version,a.om_target AS ac2_target_json,ma.version AS ac2_configuration_version,ma.enabled AS ac2_enabled FROM ontology_project p JOIN ontology_modeling_access m ON m.tenant_id=p.tenant_id AND m.space_id=p.space_id AND m.project_id=p.id AND m.account_id='104' AND m.instance_id='om-p1-isolated' JOIN om_identity_binding b ON b.tenant_id=p.tenant_id AND b.atlas_user_id='104' AND b.om_instance_id='om-p1-isolated' JOIN ontology_project a ON a.tenant_id=p.tenant_id AND a.id='"+AC2+"' JOIN ontology_modeling_access ma ON ma.tenant_id=a.tenant_id AND ma.space_id=a.space_id AND ma.project_id=a.id AND ma.account_id='104' AND ma.instance_id='om-p1-isolated' WHERE p.tenant_id='1' AND p.space_id='"+SPACE+"' AND p.id='"+S6+"'";
 static final String Q_MAINTENANCE="SELECT id,tenant_id,space_id,project_id,initiator,account_id,instance_id,operation,native_task_id,configuration_version,body_json,created_at FROM ontology_modeling_intent WHERE id IN('"+DISABLE+"','"+RESTORE+"') ORDER BY configuration_version";
 static final String Q_OBSERVATION="SELECT id,tenant_id,space_id,project_id,initiator,account_id,instance_id,glossary_id,cut_hash,observation_hash,snapshot_json,report_json,observed_at,expires_at FROM ontology_glossary_reviewer_observation WHERE tenant_id='1' AND space_id='"+SPACE+"' AND project_id='"+S6+"' AND account_id='104' AND instance_id='om-p1-isolated' AND glossary_id='"+G+"' ORDER BY observed_at DESC,id DESC";
 static final String Q_OLD_ROOTS="SELECT id,tenant_id,space_id,project_id,initiator,account_id,instance_id,operation_key,mode,state,version,snapshot_json,journal_json FROM ontology_root_glossary_intent WHERE id IN('"+ROOT_OLD+"','"+ROOT_AC2+"') ORDER BY id";
 static void need(boolean b,String c){if(!b)throw new IllegalStateException(c);}
 static Map<String,Object> m(Object...v){var out=new LinkedHashMap<String,Object>();for(int i=0;i<v.length;i+=2)out.put((String)v[i],v[i+1]);return out;}
 @SuppressWarnings("unchecked") static Map<String,Object> obj(Object v){need(v instanceof Map<?,?>,"JSON_OBJECT_REQUIRED");return (Map<String,Object>)v;}
 static String str(Object v){return v==null?null:v.toString();}
 static long num(Object v){need(v instanceof Number,"INTEGER_REQUIRED");try{return new java.math.BigDecimal(v.toString()).longValueExact();}catch(ArithmeticException e){throw new IllegalStateException("INTEGER_REQUIRED");}}
 static String jsonText(ResultSet rs,String key)throws Exception{String s=rs.getString(key);need(s!=null&&s.length()<=3*1048576,"JSON_BOUND");return s;}
 static Map<String,Object> portable(String s){return OntologyCanonicalJson.read(s,LinkedHashMap.class);}
 static String sha(String s)throws Exception{return "sha256:"+DmM15NormalRuntimeIdle.sha(s.getBytes(StandardCharsets.UTF_8));}
 static String sha(byte[] b)throws Exception{return "sha256:"+DmM15NormalRuntimeIdle.sha(b);}
 static void tuple(ResultSet r,String project)throws Exception{need("1".equals(r.getString("tenant_id"))&&SPACE.equals(r.getString("space_id"))&&project.equals(r.getString("project_id"))&&"1".equals(r.getString("initiator"))&&"104".equals(r.getString("account_id"))&&INSTANCE.equals(r.getString("instance_id")),"FIXED_ROW_TUPLE_CHANGED");}
 static void target(String text,String expected)throws Exception{var v=NATIVE.readTree(text);need(v.isObject()&&v.size()==2&&INSTANCE.equals(v.path("instanceId").textValue())&&expected.equals(v.path("glossaryId").textValue()),"PROJECT_POINTER_CHANGED");}
 static boolean own(JsonNode n){return USER.equals(n.path("id").textValue())&&"user".equals(n.path("type").textValue());}
 static Map<String,Object> safeFact(Object v){var f=obj(v);String state=str(f.get("state")),reason=str(f.get("reason"));need(state!=null&&state.matches("[A-Z_]{1,40}")&&reason!=null&&reason.matches("[A-Z0-9_]{1,160}"),"FACT_SAFE_CODE_REQUIRED");return m("state",state,"reason",reason);}
 static PreparedStatement query(Connection db,String sql,int max)throws Exception{need(sql.startsWith("SELECT ")&&!sql.contains(";")&&!sql.toUpperCase(Locale.ROOT).contains("FOR UPDATE"),"READ_ONLY_SELECT_REQUIRED");var st=db.prepareStatement(sql);st.setQueryTimeout(10);st.setMaxRows(max);selects++;return st;}
 static Map<String,Object> reviewer(Connection db)throws Exception{
  try(var st=query(db,Q_REVIEWER,2);var r=st.executeQuery()){
   need(r.next(),"REVIEWER_INTENT_MISSING");tuple(r,S6);String planText=jsonText(r,"snapshot_json"),journalText=jsonText(r,"journal_json");var p=portable(planText);var j=portable(journalText);var n=obj(j.get("native"));var raw=obj(j.get("rawAck"));
   need(INTENT.equals(r.getString("id"))&&KEY.equals(r.getString("operation_key"))&&G.equals(r.getString("glossary_id"))&&"SUCCEEDED".equals(r.getString("state"))&&r.getString("active_slot")==null&&r.getLong("active_for_g")==0,"REVIEWER_TERMINAL_HEADER_CHANGED");
   var frozen=new LinkedHashMap<String,Object>(p);frozen.put("planHash",null);need(r.getString("request_hash").equals(p.get("planHash"))&&r.getString("request_hash").equals(OntologyCanonicalJson.hash(m("plan",frozen,"immutable",j.get("immutable")))),"REVIEWER_IMMUTABLE_HASH_CHANGED");
   need(Boolean.TRUE.equals(j.get("claimed"))&&Objects.equals(j.get("dispatchHash"),raw.get("requestHash"))&&"DISPATCHED".equals(n.get("dispatchState"))&&"ACKNOWLEDGED".equals(n.get("ackState"))&&"ACKNOWLEDGED".equals(n.get("requestExecution"))&&num(raw.get("status"))==200&&num(n.get("httpStatus"))==200&&"VERIFIED".equals(n.get("readbackState"))&&"0.2".equals(n.get("nativeVersion")),"REVIEWER_ACK_OR_READBACK_CHANGED");
   byte[] bytes=Base64.getDecoder().decode(str(raw.get("bodyBase64")));need(bytes.length<=1048576&&sha(bytes).equals(raw.get("responseHash"))&&raw.get("responseHash").equals(n.get("responseHash")),"REVIEWER_RAW_ACK_HASH_CHANGED");var ack=NATIVE.readTree(bytes);Arrays.fill(bytes,(byte)0);
   need(G.equals(ack.path("id").textValue())&&ack.path("version").isNumber()&&ack.path("version").decimalValue().compareTo(new java.math.BigDecimal("0.2"))==0&&ack.path("reviewers").isArray()&&ack.path("reviewers").size()==1&&own(ack.path("reviewers").get(0)),"REVIEWER_RAW_ACK_TARGET_CHANGED");
   var out=m("intentId",INTENT,"operationKey",KEY,"state",r.getString("state"),"rowVersion",r.getLong("version"),"activeSlotIsNull",true,"activeReviewerIntentsForG",0,"dispatchState",n.get("dispatchState"),"ackState",n.get("ackState"),"httpStatus",200,"rawACKHashMatches",true,"rawACKTargetAndOnly104Reviewer",true,"nativeVersion",n.get("nativeVersion"),"independentReadback",n.get("readbackState"),"requestExecution",n.get("requestExecution"),"originalConfigurationVersion",obj(p.get("cut")).get("configurationVersion"),"snapshotRawSHA256",sha(planText),"journalRawSHA256",sha(journalText),"sharedImpactHash",obj(p.get("sharedImpacts")).get("impactHash"),"sharedCanApplyAtPlan",obj(p.get("sharedImpacts")).get("canApply"));
   need(!r.next(),"MULTIPLE_REVIEWER_ROWS");return out;
  }
 }
 static Map<String,Object> current(Connection db)throws Exception{
  try(var st=query(db,Q_CURRENT,2);var r=st.executeQuery()){
   need(r.next(),"CURRENT_MAPPING_MISSING");target(r.getString("target_json"),G);target(r.getString("ac2_target_json"),OLD_G);
   need("SYSDBA".equals(r.getString("db_user"))&&"RUOYI_VUE_PRO".equals(r.getString("db_schema"))&&BINDING.equals(r.getString("binding_id"))&&USER.equals(r.getString("native_user_id"))&&r.getLong("binding_version")==9&&r.getLong("credential_epoch")==2&&r.getLong("desired_revision")==3&&"BOUND".equals(r.getString("binding_state"))&&"IN_SYNC".equals(r.getString("sync_state"))&&r.getLong("project_version")==3&&r.getInt("enabled")==1,"CURRENT_IDENTITY_OR_MAPPING_CHANGED");
   need(r.getLong("ac2_project_version")==2&&r.getLong("ac2_configuration_version")==1&&r.getInt("ac2_enabled")==1,"AC2_MAPPING_CHANGED");
   var out=m("projectVersion",r.getLong("project_version"),"configurationVersion",r.getLong("configuration_version"),"mappingEnabled",true,"bindingId",BINDING,"nativeUserId",USER,"bindingVersion",9,"credentialEpoch",2,"desiredRevision",3,"bindingState","BOUND","syncState","IN_SYNC","s6Target",G,"ac2Target",OLD_G,"ac2ProjectVersion",2,"ac2ConfigurationVersion",1,"ac2MappingEnabled",true);need(!r.next(),"MULTIPLE_CURRENT_MAPPING_ROWS");return out;
  }
 }
 static Map<String,Object> maintenance(Connection db,Map<String,Object> cur,long originalConfig)throws Exception{
  var out=new LinkedHashMap<String,Object>();long disabled=-1,restored=-1;Instant at=null;
  try(var st=query(db,Q_MAINTENANCE,3);var r=st.executeQuery()){
   int count=0;while(r.next()){count++;tuple(r,S6);String id=r.getString("id");var body=portable(jsonText(r,"body_json"));var request=obj(body.get("request"));long config=r.getLong("configuration_version");boolean disable=DISABLE.equals(id);need(disable||RESTORE.equals(id),"FOREIGN_MAINTENANCE_INTENT");
    need(r.getString("native_task_id")==null&&"104".equals(request.get("accountId"))&&INSTANCE.equals(request.get("instanceId"))&&S6.equals(request.get("projectId"))&&config==num(body.get("configurationVersion"))&&num(request.get("expectedVersion"))+1==config,"LOCAL_MAINTENANCE_CONTENT_CHANGED");
    need((disable?"DISABLE_IDENTITY":"CONFIGURE_MODELING_ACCESS").equals(r.getString("operation"))&&(disable?"DISABLE":"CONFIGURE").equals(request.get("change")),"MAINTENANCE_OPERATION_CHANGED");
    if(disable)disabled=config;else{restored=config;at=r.getTimestamp("created_at").toInstant();}
    out.put(disable?"disable":"restore",m("intentId",id,"operation",r.getString("operation"),"statusPerLocalStore","SUCCEEDED","nativeTaskIsNull",true,"configurationVersion",config,"enabledAfter",!disable,"createdAt",r.getTimestamp("created_at").toInstant().toString()));
   }need(count==2&&disabled==originalConfig+1&&restored==disabled+1&&restored==num(cur.get("configurationVersion")),"MAINTENANCE_VERSION_CHAIN_CHANGED");
  }out.put("restoreAt",at.toString());out.put("sameMappingNoRotation",true);return out;
 }
 static Map<String,Object> observation(Connection db,Map<String,Object> cur,Map<String,Object> maintenance,Map<String,Object> reviewer)throws Exception{
  try(var st=query(db,Q_OBSERVATION,1);var r=st.executeQuery()){
   need(r.next(),"FINAL_OBSERVATION_MISSING");tuple(r,S6);need(G.equals(r.getString("glossary_id")),"OBSERVATION_TARGET_CHANGED");var cut=portable(jsonText(r,"snapshot_json"));var report=portable(jsonText(r,"report_json"));
   need(OntologyCanonicalJson.hash(cut).equals(r.getString("cut_hash"))&&OntologyCanonicalJson.hash(report).equals(r.getString("observation_hash")),"OBSERVATION_HASH_CHANGED");
   boolean versionsMatch=num(cut.get("projectVersion"))==num(cur.get("projectVersion"))&&num(cut.get("configurationVersion"))==num(cur.get("configurationVersion"))&&num(cut.get("bindingVersion"))==9&&num(cut.get("credentialEpoch"))==2&&num(cut.get("desiredRevision"))==3&&BINDING.equals(cut.get("bindingId"))&&USER.equals(cut.get("nativeUserId"))&&G.equals(cut.get("glossaryId"));
   Instant observed=r.getTimestamp("observed_at").toInstant(),expires=r.getTimestamp("expires_at").toInstant();boolean ttlCurrent=expires.isAfter(Instant.now());
   var qualified=safeFact(report.get("reviewerQualified"));var write=safeFact(report.get("executorCurrentWrite"));boolean storedReady="READY".equals(qualified.get("state"))&&"READY".equals(write.get("state"))&&Boolean.TRUE.equals(reviewer.get("sharedCanApplyAtPlan"))&&Objects.equals(cut.get("sharedImpactHash"),reviewer.get("sharedImpactHash"));
   need("0.2".equals(report.get("nativeVersion"))&&((List<?>)report.get("reviewers")).size()==1&&USER.equals(obj(((List<?>)report.get("reviewers")).getFirst()).get("id")),"LATEST_OBSERVATION_REVIEWER_CHANGED");
   return m("id",r.getString("id"),"observedAt",observed.toString(),"expiresAt",expires.toString(),"afterRestore",!observed.isBefore(Instant.parse(str(maintenance.get("restoreAt")))),"snapshotConfigurationVersion",cut.get("configurationVersion"),"currentTupleVersionsMatch",versionsMatch,"snapshotAndReportHashesMatch",true,"ttlCurrent",ttlCurrent,"nativeVersion","0.2","storedReviewerQualified",qualified,"storedExecutorCurrentWrite",write,"targetReadyDerivedFromStoredReportAndPlanSharedImpact",storedReady?"READY":"NOT_READY","currentConservativeReadiness",m("reviewerQualified",ttlCurrent&&versionsMatch?"NOT_RECOMPUTED":"NOT_VERIFIED","executorCurrentWrite",ttlCurrent&&versionsMatch?"NOT_RECOMPUTED":"NOT_VERIFIED","targetReady",ttlCurrent&&versionsMatch?"NOT_RECOMPUTED":"NOT_READY","reason",!ttlCurrent?"OBSERVATION_TTL_EXPIRED":!versionsMatch?"CURRENT_CUT_VERSION_CHANGED":"LIVE_IAM_NOT_RECOMPUTED_BY_READONLY_DIAGNOSTIC"),"modelSyncQualification","NOT_VERIFIED","liveIAMOrNativeRecomputed",false);
  }
 }
 static List<Map<String,Object>> oldRoots(Connection db)throws Exception{
  var out=new ArrayList<Map<String,Object>>();try(var st=query(db,Q_OLD_ROOTS,3);var r=st.executeQuery()){
   while(r.next()){String id=r.getString("id");boolean reuse=ROOT_AC2.equals(id);need(reuse||ROOT_OLD.equals(id),"FOREIGN_OLD_ROOT");tuple(r,reuse?AC2:S6);var j=portable(jsonText(r,"journal_json"));var n=obj(j.get("native"));var local=obj(j.get("localBinding"));target(OntologyCanonicalJson.write(local.get("target")),OLD_G);
    need("SUCCEEDED".equals(r.getString("state"))&&r.getLong("version")== (reuse?5:10)&&(reuse?"REUSE":"CREATE").equals(r.getString("mode"))&&OLD_G.equals(n.get("glossaryId"))&&"BOUND".equals(local.get("state"))&&num(local.get("projectVersionAfter"))==2,"OLD_ROOT_HISTORY_CHANGED");
    need((reuse?"03be0335-796e-4a88-adbf-339edcbb76cb":"9f5a44f4-ec68-4ebe-9824-e7ffd2c3e7ed").equals(r.getString("operation_key")),"OLD_ROOT_OPERATION_KEY_CHANGED");
    out.add(m("intentId",id,"projectId",reuse?AC2:S6,"mode",r.getString("mode"),"state","SUCCEEDED","rowVersion",r.getLong("version"),"historicalTarget",OLD_G,"localBoundProjectVersion",2,"journalRawSHA256",sha(jsonText(r,"journal_json")),"headerMatchesPriorAcceptedValues",true));
   }need(out.size()==2,"OLD_ROOT_HISTORY_INCOMPLETE");return out;
  }
 }
 @SuppressWarnings("unchecked") static void run(String[] args)throws Exception{
  need(args.length==1,"FIXED_OUTPUT_ARGUMENT_REQUIRED");Path output=Path.of(args[0]).toAbsolutePath();need(output.equals(BASE.resolve("safe-result.json"))&&!Files.exists(output),"EXCLUSIVE_FIXED_E_OUTPUT_REQUIRED");
  byte[] config=Files.readAllBytes(DmM15NormalRuntimeIdle.CONFIG);need(DmM15NormalRuntimeIdle.sha(config).equals(DmM15NormalRuntimeIdle.CONFIG_PIN),"CONFIG_CHANGED");Map<String,Object> primary=null;
  try{for(Object document:new Yaml().loadAll(new String(config,StandardCharsets.UTF_8))){if(!(document instanceof Map<?,?> root)||!root.containsKey("spring"))continue;var spring=DmM15NormalRuntimeIdle.map(root.get("spring"));if(!spring.containsKey("datasource"))continue;var dynamic=DmM15NormalRuntimeIdle.map(DmM15NormalRuntimeIdle.map(spring.get("datasource")).get("dynamic"));need("master".equals(dynamic.get("primary")),"PRIMARY_CHANGED");primary=DmM15NormalRuntimeIdle.map(DmM15NormalRuntimeIdle.map(dynamic.get("datasource")).get("master"));}}finally{Arrays.fill(config,(byte)0);}
  need(primary!=null,"PRIMARY_REQUIRED");String url=DmM15NormalRuntimeIdle.text(primary.get("url"));need(url.equals("jdbc:dm://127.0.0.1:5236?schema=RUOYI_VUE_PRO"),"FIXED_DM_ORIGIN_REQUIRED");Class.forName("dm.jdbc.driver.DmDriver");DriverManager.setLoginTimeout(10);
  var out=m("kind","M15_FINAL_LOCAL_STATE_READ_ONLY","startedAt",Instant.now().toString());
  try(Connection db=DriverManager.getConnection(url,DmM15NormalRuntimeIdle.text(primary.get("username")),DmM15NormalRuntimeIdle.text(primary.get("password")))){db.setReadOnly(true);var review=reviewer(db);var current=current(db);var maintenance=maintenance(db,current,num(review.get("originalConfigurationVersion")));out.put("reviewerIntent",review);out.put("currentMappingAndPointers",current);out.put("maintenance",maintenance);out.put("latestObservation",observation(db,current,maintenance,review));out.put("oldRootHistory",oldRoots(db));}
  need(selects==5,"EXACT_FIVE_SELECTS_REQUIRED");out.putAll(m("finishedAt",Instant.now().toString(),"SELECT",selects,"DML",0,"DDL",0,"HTTP",0,"automaticRetry",false,"productGetters",0,"acrossStatementSnapshotClaimed",false));DmM15NormalRuntimeIdle.save(output,out);System.out.println("M15_FINAL_LOCAL_READ_SAVED_NO_MUTATION");
 }
 public static void main(String[] args){try{run(args);}catch(Throwable failed){String code=failed.getMessage();if(!(failed instanceof IllegalStateException)||code==null||!code.matches("[A-Z0-9_]{1,160}"))code="LOCAL_READER_"+failed.getClass().getSimpleName().replaceAll("[^A-Za-z0-9]","").toUpperCase(Locale.ROOT);System.err.println("M15_FINAL_LOCAL_READ_STOPPED code="+code+" class="+failed.getClass().getSimpleName()+" SELECT="+selects);System.exit(1);}}
}
