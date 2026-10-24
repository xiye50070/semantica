import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import java.lang.invoke.*;
import java.nio.file.*;
import java.util.*;

/** Six pure comparison categories, including the new real snapshot. No application or transport exists. */
public final class DerivedEmptyComparisonFinite {
 static final ObjectMapper JSON=new ObjectMapper();
 static final Path STRONG=Path.of("/Users/wangmingzhang/atlas-cloud/.local/ontology-p1/atlas-registration-m1-20261023/private/m13-strong-diagnostic-bb77fc96-a2cc-4fb3-ac5e-1e069c829bee/strong.response.raw.json");
 static MethodHandle normalize;
 static void need(boolean ok,String code){if(!ok)throw new AssertionError(code);}
 static ObjectNode normalized(ObjectNode value)throws Throwable {ObjectNode copy=value.deepCopy();normalize.invoke(copy);return copy;}
 static ObjectNode emptyVotes(){var votes=JSON.createObjectNode().put("upVotes",0).put("downVotes",0);votes.putArray("upVoters");votes.putArray("downVoters");return votes;}
 static void retained(ObjectNode value,ObjectNode baseline,String code)throws Throwable {need(!normalized(value).equals(normalized(baseline)),code);}
 public static void main(String[] args)throws Throwable {
  boolean nativeMode=args.length==1&&args[0].equals("native");Class<?> type=Class.forName(nativeMode?"org.openmetadata.service.ontology.OntologyConditionalApplyService":"cn.iocoder.atlas.module.infra.ontology.OntologyModelingQualificationNativeAdapter");
  var lookup=MethodHandles.privateLookupIn(type,MethodHandles.lookup());normalize=lookup.findStatic(type,"normalizeEmptyDerivedReadFields",MethodType.methodType(void.class,ObjectNode.class));
  var checks=new LinkedHashMap<String,Boolean>();JsonNode strong=JSON.readTree(Files.readAllBytes(STRONG));ObjectNode op=(ObjectNode)strong.path("changeSet").path("operations").path(0).path("term"),term=(ObjectNode)strong.path("terms").path(0);
  if(nativeMode){
   Class<?> termType=Class.forName("org.openmetadata.schema.entity.data.GlossaryTerm");var actual=lookup.findStatic(type,"payloadComparable",MethodType.methodType(JsonNode.class,termType));
   Object left=JSON.treeToValue(op,termType),right=JSON.treeToValue(term,termType);need(((JsonNode)actual.invoke(left)).equals((JsonNode)actual.invoke(right)),"REAL_NATIVE_TYPED_PAYLOAD_COMPARISON");
  }else{
   var actual=lookup.findStatic(type,"descriptionComparable",MethodType.methodType(JsonNode.class,JsonNode.class));need(((JsonNode)actual.invoke(op)).equals((JsonNode)actual.invoke(term)),"REAL_SDK_PAYLOAD_COMPARISON");
  }
  checks.put("realNewSnapshotExactEmptyPositive",true);
  ObjectNode base=JSON.createObjectNode().put("id","73baf489-acac-4dc0-8c75-d2cbe669a952").put("version",0.2).put("name","same");
  ObjectNode empty=base.deepCopy();empty.putArray("children");empty.set("votes",emptyVotes());need(normalized(empty).equals(base),"EXACT_EMPTY_EQUIVALENCE");
  ObjectNode children=base.deepCopy();children.putArray("children").addObject().put("id","bc616300-c357-49b8-bcd6-5be0f57758b8");retained(children,base,"NONEMPTY_CHILDREN_RETAINED");checks.put("nonemptyChildrenReject",true);
  for(String field:List.of("upVotes","downVotes","upVoters","downVoters")){
   ObjectNode votes=emptyVotes();if(field.endsWith("Votes"))votes.put(field,1);else((ArrayNode)votes.get(field)).addObject().put("id","2d0540a5-d6fd-4f24-a5a4-270eaa54dc26");
   ObjectNode nonzero=base.deepCopy();nonzero.set("votes",votes);retained(nonzero,base,"ANY_VOTE_RETAINED");
  }checks.put("nonzeroVotesAndVoterListsReject",true);
  for(int mode=0;mode<4;mode++){
   ObjectNode votes=emptyVotes();if(mode==0)votes.put("unknownZero",0);if(mode==1)votes.put("upVotes",0.0);if(mode==2)votes.put("upVotes",false);if(mode==3)votes.remove("downVoters");
   ObjectNode changed=base.deepCopy();changed.set("votes",votes);retained(changed,base,"ONLY_EXACT_FOUR_KEYS_INTEGER_ZERO_NORMALIZES");
  }checks.put("extraKeyFloatBooleanPartialVotesReject",true);
  for(String field:List.of("children","votes","unknownNull")){ObjectNode changed=base.deepCopy();changed.putNull(field);retained(changed,base,"EXPLICIT_NULL_RETAINED");}
  ObjectNode zero=base.deepCopy().put("unknownZero",0);retained(zero,base,"UNKNOWN_ZERO_RETAINED");ObjectNode flag=base.deepCopy().put("unknownFlag",false);retained(flag,base,"UNKNOWN_FALSE_RETAINED");checks.put("explicitNullAndUnknownZeroFalseReject",true);
  for(String field:List.of("id","version","name","owners","opaqueReadSet")){
   ObjectNode changed=base.deepCopy();if(field.equals("version"))changed.put(field,0.3);else if(field.equals("owners"))changed.putArray(field).addObject().put("id","00000000-0000-4000-8000-000000000001");else changed.put(field,"different");retained(changed,base,"BUSINESS_FIELDS_RETAINED");
  }checks.put("uuidVersionOwnerNameAndOpaqueFieldsReject",true);
  System.out.println(JSON.writeValueAsString(Map.of("mode",nativeMode?"native":"sdk","categories",6,"checks",checks,"newSnapshotNotOriginal17067",true,"HTTP",0,"SQL",0,"runReentry",false)));
 }
}
