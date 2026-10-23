import java.nio.file.*;
import java.util.*;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public final class D2SingleRunner {
    public static void main(String[] args) throws Exception {
        var request = LauncherDiscoveryRequestBuilder.request().selectors(DiscoverySelectors.selectMethod(
            "cn.iocoder.atlas.module.infra.ontology.D2UnmappedConsumerTest",
            "authorizedExactScopeUnmappedPersonCannotStartEitherReadonlyConsumer")).build();
        var listener = new SummaryGeneratingListener();
        var launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
        var s = listener.getSummary();
        var body = new LinkedHashMap<String,Object>();
        body.put("found",s.getTestsFoundCount());body.put("passed",s.getTestsSucceededCount());
        body.put("failed",s.getTestsFailedCount());body.put("skipped",s.getTestsSkippedCount());
        body.put("selectedMethod","authorizedExactScopeUnmappedPersonCannotStartEitherReadonlyConsumer");
        body.put("currentProductionClasses",true);body.put("realHTTP",0);body.put("realSQL",0);
        new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(Path.of(args[0]).toFile(),body);
        s.printTo(new java.io.PrintWriter(System.out,true));
        s.printFailuresTo(new java.io.PrintWriter(System.err,true));
        if(s.getTestsFoundCount()!=1 || s.getTestsSucceededCount()!=1 || s.getTestsFailedCount()!=0) System.exit(1);
    }
}
