# M1.5 Reviewer wire v1 — frozen for implementation

仅 Reviewer 资格、最小字段修改和目标就绪。独立 namespace `/admin-api/ontology/glossary-reviewers`。Envelope 沿 `CommonResult {code,data,msg}`；code=0 为成功。所有 scope query 恰有一次 `projectId/accountId/instanceId`，禁止额外/重复键。POST body 严格键集。UUID canonical lowercase；SHA 为 `sha256:`+64hex；本地版本 safe integer；Native version 为正 canonical decimal string（可为 `"1"`），永不塞入旧 dependencyVersions。

## Common exact records

```ts
type Scope={tenantId:string;spaceId:string;projectId:string};
type Ref={id:string;type:'user'|'team'};
type ShownRef=Ref & {name:string|null;displayName:string|null};
type Fact={state:'READY'|'NOT_READY'|'NOT_VERIFIED'|'DENIED'|'UNAVAILABLE'|'STALE';reason:string;evidenceRef:string|null;observedAt:string|null};
type Identity={bindingId:string;nativeUserId:string;bindingVersion:number;credentialEpoch:number;desiredRevision:number};
type Target={instanceId:string;glossaryId:string;name:string;nativeVersion:string|null;bindingIntentId:string;boundProjectVersion:number};
type Cut={scope:Scope;accountId:string;instanceId:string;projectVersion:number;configurationVersion:number;bindingId:string;nativeUserId:string;bindingVersion:number;credentialEpoch:number;desiredRevision:number;permissionVersion:number;policyHash:string|null;authorityHash:string;rootBindingHash:string;glossaryId:string;observationId:string|null;observationHash:string|null;sharedImpactHash:string};
type Impact={scope:Scope;projectName:string;projectVersion:number;bindingIntentId:string;readOnlyConflict:boolean;canApply:boolean;reason:string};
type SharedImpacts={complete:boolean;impactHash:string;projects:Impact[];unresolvedCount:number;canApply:boolean;reason:string};
type Readiness={targetBinding:Fact;reviewerQualified:Fact;executorCurrentWrite:Fact;targetReady:Fact;modelSyncQualification:Fact};
type Context={contractVersion:1;scope:Scope;actorId:string;accountId:string;instanceId:string;projectVersion:number;identity:Identity|null;target:Target|null;cut:Cut|null;currentReviewers:ShownRef[]|null;readiness:Readiness;sharedImpacts:SharedImpacts|null;observationId:string|null;observedAt:string|null;expiresAt:string|null;allowedActions:('INSPECT_CURRENT'|'LIST_SUBJECTS'|'PREPARE_CHANGE')[];reason:string};
type Candidate={ref:ShownRef;accountId:string|null;source:'ATLAS_APPROVED_MANAGED_USER'|'PERSISTED_NATIVE_REVIEWER';nativeSupported:boolean;qualificationState:'VERIFIED'|'NOT_VERIFIED'|'INVALID'|'UNAVAILABLE';selectable:boolean;reason:string;evidenceRef:string|null};
type Subjects={contractVersion:1;scope:Scope;actorId:string;accountId:string;instanceId:string;cut:Cut;coverage:'CURRENT_PROJECT_APPROVED_MANAGED_USERS_AND_PERSISTED_REVIEWERS';supportedTypes:{type:'user'|'team';nativeSupported:boolean;qualificationSupport:'SUPPORTED'|'SUPPORTED_UNVERIFIED';reason:string}[];candidates:Candidate[];limitations:string[]};
```

Context identity/cut 同 null；无 usable target 时 target=null、currentReviewers=null，不伪造空数组。Context GET 是本地事实投影；Native 独立核验由显式 inspect 做，过期/当前 Atlas 或身份变化立即撤 READY。readonly AC2 可读，不取得写资格。

## Routes / request exact keys

- `GET /context?SCOPE3` → Context。
- `GET /subjects?SCOPE3` → Subjects。仅批准受管人员和已持久 Reviewer；不全扫 Native users/teams，不把 M1.4 self-only 目录冒完整目录。User 可选择以进入当前核验；selectable 不等于已资格通过。Team 原生类型合法，本期无完整有效成员证据则 `SUPPORTED_UNVERIFIED`、不可新增，保现值不盲删。
- `POST /inspect` body `{projectId,accountId,instanceId,cut}` → Context。只发本人 GET；在独立本地 observation 记录资格/当前目标版本及 TTL，不做 Native 写，不创建修改意图。空/非法 Reviewer 不 READY。
- `POST /prepare` body `{projectId,accountId,instanceId,cut,add:Ref[],remove:Ref[]}` → Plan。至少一项增删；各集合去重，不得同时 add/remove 同一 ref。server 保留全部未点名旧 refs 和顺序；只有候选准入可 add，remove 只能指向当前持久 ref。Native 最终数组只能多 User 或一个 Team。cut 必须指向本次 fresh inspect；版本、原 reviewers、保护字段或影响变化拒旧预览，不静默 rebase。
- `POST /confirm` body `{planId,planHash,operationKey,cut}` → Intent。确认前耐久受理，claim 后不取消/二派。重复同 key 同内容只返回原 intent。
- `GET /intents/by-key?SCOPE3&operationKey=...` → Intent|null；null 不授权丢 ACK 后新 key 重发。
- `GET /intents/{id}?SCOPE3` → Intent；保原 cut/原 UUID/原 key，GET 不写 Native、不隐式推进。
- `POST /intents/{id}/cancel?SCOPE3` body `{expectedVersion:number}` → Intent；仅 first claim 前。
- `POST /intents/{id}/check?SCOPE3` body `{expectedVersion:number}` → Intent；仅显式原意图只读核对，Native 仅 GET。保 UNKNOWN/原 dispatch/原 ACK 缺失事实，追加观察，不能重 PATCH、伪造 ACK 或改 key。

```ts
type Plan={contractVersion:1;planId:string;planHash:string;scope:Scope;actorId:string;accountId:string;instanceId:string;target:Target;cut:Cut;expiresAt:string;before:{reviewers:ShownRef[];protectedHash:string};after:{reviewers:ShownRef[]};diff:{added:Ref[];removed:Ref[];retained:Ref[]};sharedImpacts:SharedImpacts;blockedReasons:string[];limitations:string[]};
type NativeReceipt={dispatchState:'NOT_SENT'|'DISPATCHED';ackState:'NONE'|'ACKNOWLEDGED';httpStatus:number|null;responseHash:string|null;nativeVersion:string|null;readbackState:'NOT_RUN'|'VERIFIED'|'MISMATCH'|'UNAVAILABLE';readbackHash:string|null;requestExecution:'NOT_DISPATCHED'|'ACKNOWLEDGED'|'UNKNOWN'|'NOT_PROVEN'};
type Intent={contractVersion:1;intentId:string;planId:string;planHash:string;operationKey:string;scope:Scope;actorId:string;accountId:string;instanceId:string;glossaryId:string;state:'READY'|'RUNNING'|'SUCCEEDED'|'BLOCKED'|'UNKNOWN'|'CANCELLED';version:number;originalCut:Cut;native:NativeReceipt;lastCheck:{state:'EXPECTED_STATE_OBSERVED'|'MISMATCH'|'UNAVAILABLE';observedAt:string;observationHash:string}|null;allowedActions:('READ_ORIGINAL'|'CANCEL_BEFORE_DISPATCH'|'CHECK_ORIGINAL'|'INSPECT_CURRENT'|'PREPARE_CHANGE')[];reason:string;replayed:boolean};
```

READY 未 claim 可 READ/CANCEL；RUNNING 只 READ；UNKNOWN 只 READ/CHECK，原 pending/key 必保。终态有无 PREPARE_CHANGE 由当前资格/共享硬门决定，不能只凭 SUCCEEDED 放写。401/403/未知错误不得抹原 key。安全错误 envelope 沿受控机器码，不返回 Throwable message/raw/凭据。

## Four facts and hard boundaries

1. `targetBinding` 是 M1.4 当前 pointer +耐久绑定；历史 boundVersion 不等于永远要求项目版本不变。
2. `reviewerQualified` 独立核批准主体、类型、当前有效状态/映射/必要本人证据及目标持久数组；不是非空 UUID/Owner/ViewBasic 自动通过。Native 没有单独 eligibility API。当前 `/permissions/glossary/{id}` 不传 user，只有 policy 投影，不包含 direct Reviewer bypass，不能单独作完整权限 oracle。
3. `executorCurrentWrite` 仅本目标的当前 Atlas TARGET_READ+REVIEWER_UPDATE/IN_SYNC 配置与当前本人实际 Native 强制执行；旧 S6 v1 资格 run 不是当前 v2 门，不重跑11写矩阵，不升级 Term/模型同步资格。
4. `targetReady` 要当前绑定、当前目标读回、Reviewer 合格及本目标必要写条件都满足；`modelSyncQualification` 本期始终 NOT_VERIFIED。AC2 仍只读，不能从共享目标 Reviewer 反推写绿。

持久 Reviewer 形成 G 级原生授权旁路，无项目维度。共享影响必须完整；任何关联只读/未获当前写授权/无法证明保护时 `canApply=false`、reason `REVIEWER_SHARED_READONLY_TARGET`。当前 S6+AC2 同 G 必阻断修改，提供现有 M1.4 独立目标路径；不升 AC2、不补 Policy、不采用旧资格四对象。

## Durable / integration

- 新具体 Reviewer intent + observation 持久化；复用现 transaction/current/单派/UNKNOWN 规则，不把 M1.1 LOCAL_PREPARATION 的 ACK_LOCAL_CHECK 当 Native ACK。
- W/"version" + `/reviewers` 完整旧值 test/最小 replace；不用 `/version` JSON test（会要求 EditAll）。只去各 ref 顶层 href 来匹配 Native PATCH 内部表示，其他旧 ref 字段/数组顺序保留。owners/tags/既有术语不直接写。原生缺显式 Reviewer 的下属 Term 会继承 G Reviewer，因此有效权限/后续 workflow assignee 可变，必须披露；不声称所有术语无影响。保护读取可请求 termCount，只证读取时点数量，不全扫或伪造 terms=[]。
- raw2xx resource ACK ≤1MiB 先耐久，后 parse/enrich、current 再核及独立 GET；晚失权保 ACK，不能填绿。raw 不公开；无管理员/旧 token fallback。
- 同步门仅限此次 M1 受管 identity +正式 Root receipt 的模式；legacy 非此模式保持既有能力。挂真实 `OmNativeProjectionSynchronizer` plan 与 execute client current fence；历史只读 readback 不变。Reviewer purpose 不调用该模型写门，禁止递归；下一 sync/send fresh 核 Reviewer，当前禁用/撤权要阻断。
- Preparation 的 Reviewer 状态走当前 observation + fresh local authority，不在已开启的本地事务里重新取 Bearer 或发 Native HTTP。原 Subject 外层验证后显式传入。
