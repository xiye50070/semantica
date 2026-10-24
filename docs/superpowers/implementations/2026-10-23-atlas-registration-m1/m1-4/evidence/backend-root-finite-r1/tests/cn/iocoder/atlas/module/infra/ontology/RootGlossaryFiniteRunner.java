package cn.iocoder.atlas.module.infra.ontology;

import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class RootGlossaryFiniteRunner {
 public static void main(String[] args)throws Exception{
  var methods=Arrays.stream(OntologyRootGlossaryFiniteTest.class.getDeclaredMethods()).filter(m->m.isAnnotationPresent(Test.class)).sorted(Comparator.comparing(Method::getName)).toList();
  if(args.length>1&&args[1].equals("DIAGNOSE_CREATE")){new OntologyRootGlossaryFiniteTest().diagnoseCreate();return;}
  if(args.length>1&&args[1].equals("DIAGNOSE_REUSE")){new OntologyRootGlossaryFiniteTest().diagnoseReuse();return;}
  if(methods.size()!=11)throw new AssertionError("EXACT_ELEVEN_DEFINED_METHODS_REQUIRED");int failed=0;
  var selected=args.length>1?Set.of(args[1].split(",")):Set.<String>of();int ran=0;
  for(var method:methods){if(!selected.isEmpty()&&!selected.contains(method.getName()))continue;ran++;
   try{method.invoke(new OntologyRootGlossaryFiniteTest());System.out.println("PASS "+method.getName());}
   catch(InvocationTargetException e){failed++;System.out.println("FAIL "+method.getName());e.getCause().printStackTrace(System.err);}
  }
  var out=Path.of(args[0]);var json=new ObjectMapper();
  Files.writeString(out.resolve("method-results.json"),json.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of("selected",ran,"passed",ran-failed,"failed",failed,"methods",OntologyRootGlossaryFiniteTest.RESULTS,"realNativeHTTP",0,"realDM",0,"boundary","Actual Store/Service/Consumer/Registry/Broker/SDK; H2 IAM/identity/applied-policy, synthetic JWT and owned HTTP responses are fixtures. No live permission acceptance."))+"\n",StandardOpenOption.CREATE_NEW);
  if(failed==0)Files.writeString(out.resolve("actual-wire.json"),json.writerWithDefaultPrettyPrinter().writeValueAsString(OntologyRootGlossaryFiniteTest.WIRE)+"\n",StandardOpenOption.CREATE_NEW);
  if(failed!=0)System.exit(1);
 }
}
