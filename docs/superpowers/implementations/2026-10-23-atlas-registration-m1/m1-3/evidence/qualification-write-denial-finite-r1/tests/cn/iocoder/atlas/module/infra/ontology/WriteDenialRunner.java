package cn.iocoder.atlas.module.infra.ontology;
import java.nio.file.*;
import java.lang.reflect.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
public final class WriteDenialRunner {
    public static void main(String[] args) throws Exception {
        var methods=Arrays.stream(OntologyModelingWriteDenialTest.class.getDeclaredMethods()).filter(m->m.isAnnotationPresent(Test.class)).sorted(Comparator.comparing(Method::getName)).toList();
        if(methods.size()!=2)throw new AssertionError("exact new two methods only");int failed=0;
        for(var method:methods){try{method.invoke(new OntologyModelingWriteDenialTest());System.out.println("PASS "+method.getName());}catch(InvocationTargetException e){failed++;System.out.println("FAIL "+method.getName());e.getCause().printStackTrace(System.err);}}
        Files.writeString(Path.of(args[0]),new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(Map.of("selected",2,"passed",2-failed,"failed",failed,"methodResults",OntologyModelingWriteDenialTest.RESULTS,"realNativeHTTP",0,"realDMSelects",0,"productWrites",0,"boundary","Real Store/Consumer/Registry/Broker/Adapter/SDK; H2 IAM/identity/applied-policy and synthetic JWT/native responses are fixtures; fake HTTP403 is not real OM authorization acceptance")));
        if(failed!=0)System.exit(1);
    }
}
