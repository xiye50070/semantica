package cn.iocoder.atlas.module.infra.ontology;

import java.time.*;
import java.util.*;
import java.io.IOException;
import java.security.MessageDigest;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import cn.iocoder.atlas.module.infra.ontology.om.*;
import static cn.iocoder.atlas.module.infra.ontology.OntologyRootGlossaryContracts.*;
import static cn.iocoder.atlas.module.infra.ontology.JdbcOntologyRootGlossaryStore.*;

/** One explicit M1.4 intent. No scheduler, native retry, byName lookup, upsert or policy synchronization. */
public final class LegacyRootDescriptionService {
 private static final ObjectMapper JSON=new ObjectMapper();final JdbcOntologyRootGlossaryStore store;final OmInstanceRegistry instances;final Clock clock;
 public LegacyRootDescriptionService(JdbcOntologyRootGlossaryStore store,OmInstanceRegistry instances,Clock clock){this.store=store;this.instances=instances;this.clock=clock;}
 public Map<String,Object> context(String project,String account,String instance){return store.context(project,account,instance);}
 public Map<String,Object> reviewers(String project,String account,String instance){return store.reviewerCandidates(project,account,instance);}
 public Map<String,Object> candidates(String project,String account,String instance){var context=store.context(project,account,instance);var result=new ArrayList<Map<String,Object>>();for(var known:store.knownTargets(project,account,instance))try{result.add(observeCandidate(project,account,instance,str(known.get("glossaryId")),known));}catch(OmRestClient.NativeException denied){if(denied.status()!=403&&denied.status()!=404)throw denied;}
  return map("contractVersion",1,"scope",context.get("scope"),"actorId",context.get("actorId"),"accountId",account,"instanceId",instance,"cut",context.get("cut"),"coverage","ATLAS_BOUND_AND_CURRENTLY_READABLE_ONLY","candidates",result,"limitations",List.of("仅Atlas正式已绑定且当前本人逐UUID可读；空目录不证明原生不存在","不含M1.3资格四目标，不全扫、不按同名自动认领"));}
 private Map<String,Object> observeCandidate(String project,String account,String instance,String id,Map<String,Object> known){var a=store.readAdmission(project,account,instance,id);var body=instances.rootGlossaryClient(a).read();store.requireAdmissionCurrent(a);return observation(body,(List<?>)known.get("sourceProjectIds"));}
 public Map<String,Object> prepare(Prepare r){Map<String,Object> selected=null;if("REUSE".equals(r.mode())){var known=store.knownTargets(r.projectId(),r.accountId(),r.instanceId()).stream().filter(x->Objects.equals(r.glossaryId(),x.get("glossaryId"))).findFirst().orElseThrow(OntologyApiException::denied);selected=observeCandidate(r.projectId(),r.accountId(),r.instanceId(),r.glossaryId(),known);}return store.prepare(r,selected);}
 public Map<String,Object> confirm(Confirm r){var accepted=store.confirm(r);if(Boolean.TRUE.equals(accepted.get("replayed")))return accepted;execute(str(accepted.get("intentId")));return get(scopeProject(accepted),str(accepted.get("accountId")),str(accepted.get("instanceId")),str(accepted.get("intentId")));}
 private static String scopeProject(Map<String,Object> intent){return str(obj(intent.get("scope")).get("projectId"));}
 private void execute(String id){Admission a=null;String stage="CLAIM";try{a=store.claim(id);if(a==null)return;var p=store.plan(a);var after=obj(p.get("after"));String nativeId=str(after.get("glossaryId"));
   if(a.purpose().equals("CREATE")){stage="CREATE";ObjectNode created=instances.rootGlossaryClient(a).create();nativeId=created.path("id").textValue();uuid(nativeId);}
   stage="READBACK";Admission verify=store.verifying(a,nativeId);ObjectNode current=instances.rootGlossaryClient(verify).read();
   stage="VERIFY";verifySelected(p,verify,current,a.purpose().equals("CREATE"));store.verified(verify,observation(current,List.of()));stage="LOCAL_BIND";store.bind(verify);
  }catch(RuntimeException|AssertionError failure){if(a!=null)store.stopped(a,safeFailure(stage,failure));else throw failure;}}
 public Map<String,Object> get(String project,String account,String instance,String id){return store.get(project,account,instance,id);}
 public Map<String,Object> byKey(String project,String account,String instance,String key){return store.byKey(project,account,instance,key);}
 public Map<String,Object> cancel(String project,String account,String instance,String id,Cancel r){return store.cancel(project,account,instance,id,r.expectedVersion());}
 public Map<String,Object> bindLocal(String project,String account,String instance,String id,BindLocal r){var original=store.get(project,account,instance,id);Admission a=store.claimLocal(id,r.expectedContextHash());try{ObjectNode current=instances.rootGlossaryClient(a).read();var plan=store.plan(a);verifySelected(plan,a,current,"CREATE".equals(plan.get("mode")));store.verified(a,observation(current,List.of()));store.bind(a);}catch(RuntimeException|AssertionError failure){store.stopped(a,safeFailure("LOCAL_ONLY",failure));}return store.get(project,account,instance,id);}
 private void verifySelected(Map<String,Object> plan,Admission a,ObjectNode body,boolean initial){var after=obj(plan.get("after"));need(a.glossaryId().equals(body.path("id").textValue())&&Objects.equals(after.get("name"),body.path("name").textValue())&&body.path("deleted").isBoolean()&&!body.path("deleted").booleanValue(),"ROOT_NATIVE_IDENTITY_CHANGED");
  need(Objects.equals(after.get("description"),body.path("description").isNull()?null:body.path("description").textValue()),"ROOT_NATIVE_DESCRIPTION_CHANGED");
  need(hash(after.get("owners")).equals(hash(refs(body.get("owners"))))&&hash(after.get("reviewerIds")).equals(hash(refs(body.get("reviewers")).stream().map(x->x.get("id")).toList())),"ROOT_OWNER_OR_REVIEWER_CHANGED");need(nativeVersion(body.get("version")).equals(store.expectedNativeVersion(a)),"ROOT_NATIVE_VERSION_CHANGED");
  if(initial){need(body.path("namespaces").isArray()&&body.path("namespaces").isEmpty(),"ROOT_INITIAL_NAMESPACES_UNVERIFIED");var config=body.path("ontologyConfiguration");need(config.isObject()&&"L3".equals(config.path("layer").textValue())&&"{term}".equals(config.path("iriMintingPattern").textValue())&&config.path("readOnly").isBoolean()&&!config.path("readOnly").booleanValue()&&("https://open-metadata.org/ontology/"+a.glossaryId()+"/").equals(config.path("baseIri").textValue()),"ROOT_INITIAL_CONFIGURATION_UNVERIFIED");for(String key:List.of("imports","installedPacks","prefixes"))need(config.path(key).isArray()&&config.path(key).isEmpty(),"ROOT_INITIAL_CONFIGURATION_UNVERIFIED");}}
 static String safeFailure(String stage,Throwable failure){
  String code;if(failure instanceof OntologyApiException known&&known.getMessage()!=null&&known.getMessage().matches("[A-Z0-9_]{1,100}"))code=known.getMessage();
  else if(failure instanceof OmRestClient.NativeException nativeFailure)code="NATIVE_HTTP_"+(nativeFailure.status()>=100&&nativeFailure.status()<=599?nativeFailure.status():0)+"_"+(nativeFailure.code()!=null&&nativeFailure.code().matches("[A-Z0-9_]{1,60}")?nativeFailure.code():"FAILED");
  else code=switch(failure.getClass().getSimpleName()){case "NullPointerException"->"NULL_POINTER_EXCEPTION";case "IllegalArgumentException"->"ILLEGAL_ARGUMENT_EXCEPTION";case "IllegalStateException"->"ILLEGAL_STATE_EXCEPTION";case "AssertionError"->"ASSERTION_ERROR";default->"UNEXPECTED_FAILURE";};
  return "ROOT_"+stage+"_"+code;
 }
 static String nativeVersion(JsonNode value){need(value!=null&&value.isNumber()&&value.decimalValue().signum()>0,"ROOT_NATIVE_VERSION_REQUIRED");return value.decimalValue().stripTrailingZeros().toPlainString();}
 static List<Map<String,Object>> refs(JsonNode value){need(value!=null&&value.isArray(),"ROOT_COMPLETE_REFERENCES_REQUIRED");var result=new ArrayList<Map<String,Object>>();for(var ref:value){String id=ref.path("id").textValue();uuid(id);need(Set.of("user","team").contains(ref.path("type").asText()),"ROOT_REFERENCE_TYPE_INVALID");result.add(map("id",id,"type",ref.path("type").asText()));}return result;}
 Map<String,Object> observation(ObjectNode body,List<?> sourceProjects){String id=body.path("id").textValue();uuid(id);need(body.path("deleted").isBoolean()&&!body.path("deleted").booleanValue(),"ROOT_ENTITY_DELETED");var owners=refs(body.get("owners"));var reviewers=refs(body.get("reviewers"));var result=map("glossaryId",id,"name",body.path("name").textValue(),"displayName",body.path("displayName").textValue(),"description",body.path("description").textValue(),"nativeVersion",nativeVersion(body.get("version")),"owners",owners,"reviewerIds",reviewers.stream().map(x->x.get("id")).toList(),"sourceProjectIds",sourceProjects);String h=hash(result);result.put("observationHash",h);result.put("observedAt",clock.instant().toString());return result;}
}
