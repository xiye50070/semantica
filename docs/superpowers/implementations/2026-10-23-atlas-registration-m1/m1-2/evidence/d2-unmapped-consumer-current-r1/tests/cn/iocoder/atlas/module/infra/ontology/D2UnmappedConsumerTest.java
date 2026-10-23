package cn.iocoder.atlas.module.infra.ontology;

import cn.iocoder.atlas.module.infra.ontology.om.OmInstanceRegistry;
import cn.iocoder.atlas.module.infra.ontology.om.OmRestClient;
import cn.iocoder.atlas.module.infra.ontology.omidentity.*;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static cn.iocoder.atlas.module.infra.ontology.OntologyModelingAccessContracts.*;

/** One current-code, owned-H2 negative. No native socket or real secret is available. */
class D2UnmappedConsumerTest extends OntologyModelingAccessTest {
    @Test void authorizedExactScopeUnmappedPersonCannotStartEitherReadonlyConsumer() throws Exception {
        var scope = new OntologyContracts.Scope("1", space, p1);
        assertEquals("1", subject.accountId());
        var context = store.context(p1);
        assertTrue(context.canManage(), "the operator has current people and project authority");
        assertEquals(scope, context.scope());
        assertTrue(context.instances().stream().anyMatch(i -> i.instanceId().equals("om-locked") && i.available()));
        var person = store.candidates(p1, null, null, 20).items().stream()
                .filter(p -> p.accountId().equals("2")).findFirst().orElseThrow();
        assertTrue(person.eligibleRoles().contains("USER"));
        assertEquals("NOT_MAPPED", person.managedIdentityState());
        var unmapped = store.get(p1, "2", "om-locked");
        assertEquals(scope, unmapped.scope());
        assertEquals("NOT_MAPPED", unmapped.mappingState());
        assertEquals(1, unmapped.version());
        assertNull(unmapped.nativeUserId());
        assertFalse(store.managedPresence(scope, "2", "om-locked"));

        AtomicInteger modelRegistryCalls = new AtomicInteger(), workerCalls = new AtomicInteger();
        AtomicInteger brokerFactoryCalls = new AtomicInteger(), nativeManagement = new AtomicInteger(), nativeOrdinary = new AtomicInteger();
        Path emptySecrets = Files.createTempDirectory("d2-unmapped-empty-secret-root-");
        Files.setPosixFilePermissions(emptySecrets, PosixFilePermissions.fromString("rwx------"));
        var credentials = new OmUserCredentialStore(emptySecrets);
        var nativeClient = new NativeOmIdentityClient((method, path, body) -> {
            nativeManagement.incrementAndGet(); throw new AssertionError("native management must not be reached");
        }, (method, path, body, bearer) -> {
            nativeOrdinary.incrementAndGet(); throw new AssertionError("ordinary auth/native read must not be reached");
        }, () -> {}, () -> {});
        try (var broker = new OmOrdinarySessionBroker(credentials, instance -> {
            brokerFactoryCalls.incrementAndGet(); return nativeClient;
        }, clock)) {
            var consumer = new OntologyModelingIdentityConsumer(store, broker, clock);
            var tripwire = new OmInstanceRegistry.ManagedResolver() {
                public boolean contains(String i, OntologyContracts.Scope s, String a) {
                    modelRegistryCalls.incrementAndGet(); return consumer.contains(i, s, a);
                }
                public OmRestClient client(String i, OntologyContracts.Scope s, String a) {
                    modelRegistryCalls.incrementAndGet(); return consumer.client(i, s, a);
                }
            };
            var registry = new OmInstanceRegistry("", () -> tripwire);
            var service = new OntologyModelingAccessService(store, consumer, registry, inspection -> {
                workerCalls.incrementAndGet(); throw new AssertionError("Worker must not be reached");
            });
            var before = fixtureRows();
            var denied = assertThrows(OntologyApiException.class, () -> service.check(
                    new CheckWrite(p1, "2", "om-locked", 1, List.of("MODEL_READBACK"), "READ_ONLY"),
                    "d2-unmapped-readonly-one"));
            assertEquals(409, denied.code());
            assertEquals("MODELING_VERSION_CHANGED", denied.getMessage());
            // Same valid tuple, direct managed boundary: absence cannot become a session.
            var notReady = assertThrows(OntologyApiException.class, () -> store.managed(scope, "2", "om-locked"));
            assertEquals(409, notReady.code());
            assertEquals("MODELING_IDENTITY_NOT_READY", notReady.getMessage());
            assertEquals(before, fixtureRows(), "all owned H2 fixture rows remain exact");
            for (String table : List.of("ontology_modeling_access", "ontology_modeling_intent", "ontology_modeling_inspection",
                    "ontology_modeling_audit", "om_identity_binding", "om_identity_task", "om_identity_audit"))
                assertEquals(0, jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class), table);
            assertEquals(0, modelRegistryCalls.get());
            assertEquals(0, workerCalls.get());
            assertEquals(0, brokerFactoryCalls.get());
            assertEquals(0, nativeManagement.get());
            assertEquals(0, nativeOrdinary.get());
            try (var files = Files.list(emptySecrets)) { assertEquals(0, files.count(), "no credential file was read or created"); }
        } finally {
            Files.delete(emptySecrets);
        }
    }
    private Map<String, List<String>> fixtureRows() {
        Map<String, List<String>> snapshot = new TreeMap<>();
        for (String table : jdbc.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_TYPE='BASE TABLE' ORDER BY TABLE_NAME", String.class)) {
            List<String> rows = jdbc.queryForList("SELECT * FROM " + table).stream()
                    .map(row -> new TreeMap<>(row).toString()).sorted().toList();
            snapshot.put(table, rows);
        }
        return snapshot;
    }
}
