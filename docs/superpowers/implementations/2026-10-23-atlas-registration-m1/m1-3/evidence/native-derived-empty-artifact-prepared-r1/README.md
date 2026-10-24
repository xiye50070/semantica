# Prepared only

`build-native-artifact-once.py --execute` compiles only c2b into a new private-tmp directory, preserves the 43 unrelated override classes, and emits a separate 49-class jar plus directory. It never changes the original M0 work, release, or source. It performs no service action, native HTTP, SQL, App startup, or old-run continuation. The controller is prepared, not executed.

Actual PID57991 uses release/classes at classpath index0. The main OpenMetadataApplication is from current open-metadata/service target/classes at index5; the old M0 service/spec jars exist but are not on that runtime classpath. Future switching would replace only index0 with the new classes directory and preserve other615 paths/configuration. No native switch controller or new execution scope is authorized or provided. Short Native service interruption, dependency boundary, owner/protected processes and a fresh concrete card remain separate decisions.

The native artifact is a single-source override build, not a full reactor package or runtime validation. SDK has its own single-source full-backend package preparation in ../backend-derived-empty-sdk-installed-build-r1. Existing seven VERIFIED steps and original UNKNOWN remain unchanged.
