# M1.4 Root Glossary wire v1

Only root Glossary CREATE / readonly REUSE and one precise Atlas project binding. M1.5 and M2 are outside this contract. The four M1.3 qualification objects/targets, policy, Role, historical runs and UNKNOWN evidence are unchanged.

All routes use `/admin-api/ontology/root-glossary`, the existing authenticated `CommonResult` envelope, `Cache-Control: no-store`, strict exact body/query keys and canonical UUIDs. IDs/account/tenant/instance are strings; versions are positive JS-safe integers (permissionVersion alone may be 0). `Hash` is `sha256:` + 64 lowercase hex. Times are UTC ISO8601. Null fields below are present as JSON null; no credential/token/reference/native raw body is public.

## Principals and modes

`actorId` is the current Atlas initiator. Manage calls require this actor's current project ADMIN and `system:user:update` for the explicitly selected `accountId`. That person must have current membership and a valid M1.2 mapping in this exact project/instance. The target person may be USER. Every native call uses that person's current ordinary OM_USER through the real Registry/Consumer/Broker; never the initiator's native administrator identity.

CREATE requires current per-project configured `TARGET_CREATE` + `TARGET_READ`, current managed policy IN_SYNC and current identity. It does not require old M1.3 qualification rows to retain projectVersion=1 or claim their four targets authorize this new name. A new immutable Root intent owns its own exact name/owner/reviewers/current cut and raw ACK.

REUSE makes no native write or permission grant. It requires current project ADMIN, the selected person's current project mapping/IN_SYNC identity and actual native GET authority for the chosen known candidate. It does NOT require a new project's M1.3 permission row or TARGET_READ configuration; it never creates/synchronizes Policy or Role. A new project needs its own legitimate membership and M1.2 mapping; an S6 context is not reusable as its authority.

CREATE forces `owners=[{id: identity.nativeUserId,type:'user'}]`. Client input cannot choose Owner. Initial reviewer selection is explicitly `[]` (default) or `[identity.nativeUserId]`; no other User/Team picker. Selecting self must display the persistent native Reviewer/Policy-bypass limitation. REUSE preserves all native owners/reviewers and accepts reviewerIds `[]` only.

## Shared exact shapes

- `Scope = {tenantId, spaceId, projectId}`.
- `Identity = {bindingId, nativeUserId, bindingVersion, credentialEpoch, desiredRevision}`; nullable only in blocked Context.
- `TargetPair = {instanceId, glossaryId}`; nullable for no existing local target.
- `OwnerRef = {id, type}` with type `user|team`; CREATE emits only mapped User.
- `Cut = {scope, accountId, instanceId, projectVersion, configurationVersion, bindingId, nativeUserId, bindingVersion, credentialEpoch, desiredRevision, permissionVersion, policyHash, authorityHash, currentTargetHash}`. `policyHash` is null when no per-project permission row; permissionVersion is then 0. No old qualification targetSetHash is repurposed as a Root reservation.
- `Primitive = {capability, configured, available, reason}`. The fixed ordered two capabilities are `TARGET_CREATE`, `TARGET_READ`; booleans concern current configured primitive eligibility, not proof a new Root resource has been authorized/read/created.
- `Binding = {state, intentId, instanceId, glossaryId, name, nativeVersion, boundProjectVersion, observedAt}`. state `LEGACY_UNVERIFIED|BOUND|STALE`; intentId/name/nativeVersion/boundProjectVersion/observedAt may be null for legacy. Context.binding is null when unbound. A plain legacy om_target never implies BOUND.

## Routes

`GET /context?projectId&accountId&instanceId`

Exact data: `{contractVersion:1, scope, actorId, accountId, instanceId, projectVersion, identity, cut, currentTarget, binding, primitives, canManage, allowedActions, reason}`. Cut is null only when no usable identity/mapping. `allowedActions` is a subset of `LIST_TARGETS|LIST_REVIEWERS|PREPARE_CREATE|PREPARE_REUSE`; only server facts enable buttons. A USER may read own Context but has canManage=false and no management actions. Reasons are safe strings, not native bodies.

`GET /candidates?projectId&accountId&instanceId`

Exact data: `{contractVersion:1, scope, actorId, accountId, instanceId, cut, coverage:'ATLAS_BOUND_AND_CURRENTLY_READABLE_ONLY', candidates, limitations}`.

Each candidate is `{glossaryId, name, displayName, description, nativeVersion, owners, reviewerIds, sourceProjectIds, observationHash, observedAt}`. displayName/description may be null. Candidate sources are locally known formal bindings visible under current Atlas scope; each UUID is then filtered by the selected person's actual GET. M1.3 qualification targets are not formal binding candidates. No global native list, manual UUID input, byName absence inference, or adoption after hidden404. Empty means no candidate in this bounded view. Limit 100; capacity overflow is explicit, not silent truncation.

`GET /reviewer-candidates?projectId&accountId&instanceId`

Exact data: `{contractVersion:1, scope, actorId, accountId, instanceId, cut, candidates, limitations}`. Only candidate shape `{id, type:'user', name, displayName, source:'CURRENT_MAPPED_USER'}` for the selected mapped person. No all-users/teams directory. Default selection remains [].

`POST /prepare`

Exact body: `{projectId, accountId, instanceId, mode, name, description, glossaryId, reviewerIds, cut, observationHash}`.

- mode `CREATE`: nonblank exact name (Unicode allowed; normalized trim frozen, no M1.3 ASCII reservation regex), description plain string length<=4000, glossaryId=null, observationHash=null, reviewerIds=[] or mapped self. Server normalizes/validates/reserves the exact name, rejects client Owner and freezes the complete native create request.
- mode `REUSE`: name=null, description=null, glossaryId from candidates, observationHash from that candidate, reviewerIds=[]. Server independently rechecks that UUID/current visibility and current Cut; no native mutation.

Exact data: `{contractVersion:1, planId, planHash, mode, scope, actorId, accountId, instanceId, cut, expiresAt, before, after, diff, blockedReasons, limitations}`. before is TargetPair|null. after is `{instanceId, glossaryId, name, description, owners, reviewerIds}` (glossaryId null for not-yet-created CREATE). diff is `{bindingWillChange, nativeCreate, nativeUpdates:false, nativeDeletes:false, previousTargetPreserved:true, otherProjectsChanged:false}`. Lists of blockedReasons/limitations are strings. Plan expiry is 900s. Owner/reviewer/native granularity and old/new binding must be visible before confirm.

`POST /confirm`

Exact body: `{planId, planHash, operationKey, cut}`. operationKey is generated once by the browser and kept through ambiguous ACK. Current authority and immutable body are checked before replay; same key/body returns original intent, changed contents reject. The intent is durable before native dispatch. Synchronous one-time execution is allowed. Duplicate confirm only reads/replays the original receipt; it cannot dispatch again.

Data is `Intent` below. GET never creates, executes or retries.

`GET /intents/by-key?projectId&accountId&instanceId&operationKey` returns Intent|null.

`GET /intents/{intentId}?projectId&accountId&instanceId` returns Intent. Scope, initiator, account and instance remain exact. Historical original cut/effects remain readable under current caller authorization and are not converted into current write authority.

`POST /intents/{intentId}/bind-local` body `{expectedContextHash}`. Only the original server action `BIND_LOCAL_ONLY` permits it; no new native-effect key/intent. It may GET the same already-known UUID through the same person/current guard, but never POST/PATCH/PUT/DELETE native data. It retries only the local binding CAS against the frozen current context. No new Glossary or policy is created.

`POST /intents/{intentId}/cancel` body `{expectedVersion}`. Only `CANCEL_BEFORE_DISPATCH` is accepted, before first claim/dispatch/ACK. No cancellation of an ambiguous or accepted native effect.

## Intent and actions

Exact Intent data: `{contractVersion:1, intentId, operationKey, planId, planHash, mode, scope, actorId, accountId, instanceId, state, version, originalCut, currentProjectVersion, native, localBinding, allowedActions, localBindContext, replayed, reason}`.

state is `READY|RUNNING|SUCCEEDED|NEEDS_LOCAL_BIND|UNKNOWN|REQUIRES_OPERATOR|BLOCKED|CANCELLED`.

`native = {createState, readbackState, glossaryId, name, nativeVersion, responseHash, observedAt}`. createState is `NOT_REQUIRED|NOT_SENT|DISPATCHED|ACKNOWLEDGED|UNKNOWN`; readbackState is `NOT_RUN|VERIFIED|FAILED`. UUID/name/version/hash/time may be null before evidence. REUSE always createState=NOT_REQUIRED. CREATE raw201 is durably retained before JSON/current checks; known ACK is never silently erased by late revoke/local CAS failure. A malformed or lost ACK does not create permission to resend.

`localBinding = {state, projectVersionBefore, projectVersionAfter, target}`. state `NOT_BOUND|BOUND|CONFLICT`; after is null until committed; target is TargetPair|null.

`localBindContext = null | {contextHash, expiresAt, projectVersion, currentTarget}`. It is a current local-only admission context for the original known UUID, not new native authorization. GET can produce this context without native reads/writes; bind-local rechecks current authority and same UUID before CAS.

Allowed actions are a subset of `READ_ORIGINAL|CANCEL_BEFORE_DISPATCH|BIND_LOCAL_ONLY|PREPARE_NEW`:

- READY before first claim: READ_ORIGINAL/CANCEL_BEFORE_DISPATCH.
- RUNNING/UNKNOWN/REQUIRES_OPERATOR: READ_ORIGINAL only.
- NEEDS_LOCAL_BIND requires known native UUID + verified readback: READ_ORIGINAL/BIND_LOCAL_ONLY.
- SUCCEEDED/CANCELLED and safely BLOCKED-without-dispatch: READ_ORIGINAL/PREPARE_NEW. Preparing again still needs a fresh Context and a new plan; no automatic write.

Project binding uses exact local scope/version CAS and increments projectVersion once. Completion accepts only its own known v→v+1 transition. Other projects, old native objects, previous binding history, Role/Policy and credentials are untouched. Any old plan uses its frozen version and cannot survive changed binding/project cut. Readiness TARGET reads a durable matching binding receipt; nonempty legacy om_target is not proof.

## Product scope registered for implementation

S=`atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology`.

New: S/OntologyRootGlossaryContracts.java, S/JdbcOntologyRootGlossaryStore.java, S/OntologyRootGlossaryService.java, S/OntologyRootGlossaryController.java, S/OntologyRootGlossaryConfiguration.java; sql/ontology/V20261024_33__ontology_root_glossary_binding.sql and sql/ontology/dm8/V20261024_33__ontology_root_glossary_binding.sql.

Existing: S/JdbcOntologyModelingPermissionStore.java (mode-specific current operation grant only; old qualification semantics unchanged), S/OntologyModelingIdentityConsumer.java and S/om/OmInstanceRegistry.java (separate Root purpose), S/OntologyProjectService.java (two target-change guards only), S/OntologyPreparationService.java and S/OntologyP1Configuration.java (optional TARGET receipt resolver; old constructor preserved).

No edit to ProjectContracts/ProjectController or old M1.3 targets/runs. Checks are E14-only H2/fake-native tests bound to current sources; runtime/real SQL/native actions belong to a later explicit execution scope.

Contract clarification r1.1: CREATE description is required plain string (empty allowed only if native schema permits), max4000; REUSE description=null and never updates it. Name follows the pinned native validation, accepts ordinary Unicode names, and freezes normalized trim. Same-instance/name pending/unknown guard is global across projects. Historical GET never requires original projectVersion after its own binding; it reauthorizes the caller and returns original evidence, while every write independently requires current admission. UNKNOWN never has PREPARE_NEW.

Native name proof: basic.json entityName is 1..256 Java/JS UTF-16 code units, forbids `::`, `>`, double quote and U+0000..001F. Atlas strips outer Unicode whitespace once and exposes that exact frozen name in preview. Native names containing dot have quoted FQN; name is not compared literally to FQN. NativeVersion is a canonical positive decimal string, e.g. `0.1`, not a local integer version.

Registered implementation scope r2 adds only S/om/OmRestClient.java: fixed Root GET minimal fields owners,reviewers,tags for catalog/REUSE; full fields owners,reviewers,tags,namespaces,ontologyConfiguration only for known CREATE ACK UUID to verify initial defaults. All old SDK methods remain unchanged.

Native DTO clarification r1.2: actual generated CreateGlossary @Size uses UTF-16 String.length, so name length 1..256 and Atlas description<=4000 use Java/JS UTF-16 code units (Chinese BMP accepted). Java strip normalization is shown in preview; FQN quoting is not a second name normalization.

Transport clarifications r1.3: both POST `/intents/{id}/bind-local` and `/cancel` require the same exact-single query projectId/accountId/instanceId as intent GET, in addition to their small body. NativeVersion strings may be positive integers or decimals (`1` and `0.1` are legal); local versions remain safe integers. RootController locally returns known 400/401/403/404/409 safe CommonResult errors/no-store; unexpected failures return503 `ROOT_OPERATION_UNAVAILABLE`, never raw exception/native/SQL text.


r1.4 版本冻结补充（wire 字段不变）：REUSE 的 `planHash` 同时绑定服务端现有 journal 中的 `immutableSelection={nativeVersion,observationHash}`；它是服务端不透明哈希，不是浏览器重算公开 Plan JSON 得到的值。确认保留该事实，实际绑定前普通用户 GET 的规范原生版本必须与选定版本相同。CREATE 的 GET 版本必须与已先耐久保存、经原始字节 SHA/严格 JSON/UUID/status 核验的 201 ACK 版本相同；任何版本漂移保留原意图和回执并停止本地绑定，不发额外原生写。旧 REUSE 行若缺版本证明，可读原历史，但不据此新 claim、补绑或作为正式目录来源；不删除旧目标，也不自动补造证明。


r1.5 已知 201 的说明编码核对（不增路由或 wire 键）：CREATE 说明优先逐字相等；仅原文不含 `<`、`&`、`>` 时，允许存储串逐码点使用原字符或一层严格十六进制/十进制数值实体。禁止命名实体、双重解码、markup 删除、trim、大小写及 Unicode 归一化；且 201 ACK.description 与 fresh GET.description 仍须存储串逐字相等。UUID、规范版本、Owner、Reviewer 与初始配置校验保持。

只有具名 `ROOT_VERIFY_ROOT_NATIVE_DESCRIPTION_CHANGED`、完整 CREATE201/UUID/原始字节 SHA 链及上述编码证明、当前本地权限仍可核对的 UNKNOWN，GET 原意图可投影 `reason=ROOT_KNOWN_201_DESCRIPTION_ENCODING_READBACK_REQUIRED`、`allowedActions=[READ_ORIGINAL,BIND_LOCAL_ONLY]` 及 fresh localBindContext。状态仍 UNKNOWN，readbackState=NOT_RUN；nativeVersion 来源为已知 ACK，observedAt 保持 null，不冒已核验。GET 不更新数据库或触发 Native。显式原 `bind-local` POST 才耐久保存旧 UNKNOWN/原因/native/版本/hash 历史，重新同 UUID full GET 后执行本地 CAS；绝不再 create。普通 UNKNOWN 仍只读原任务，原 key/plan/raw 不改。
