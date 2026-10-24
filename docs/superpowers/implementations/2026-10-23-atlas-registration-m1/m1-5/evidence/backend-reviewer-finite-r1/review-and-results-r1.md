# M1.5 Reviewer：有界非作者核验与新增有限结果

产品作者为 backend peer；本文作者只读复核产品，独立编写本目录 H2/ownedHTTP 测试。没有产品安装、真实 Native/DM 请求、身份认证、代理、服务、Git 或实际账变更。

当前产品 cut 为 `backend-candidate-r1/revision-r5/source-map.json`，SHA256 `483e1d7242f0fe89ef6560c59bc517c5b5d5ec228effede6dee89cdae4cf1038`，14 路径/12 Java。r6 基于未证 SELF 因果产生，已撤回且未采纳；不得把它作为测试或构建通过版本。

## 实际结果

六个 unique 方法分时通过，不是单轮六通过，也不是六项全部在 r5 重跑。

| 方法 | 实际通过原件 | 产品 cut |
|---|---|---|
| configuredAuthorityNative403AndGenerationDriftNeverBecomeReady | `isolated-first-r1/actual.json`、`method-results.json` | r1；后续无关旧绿保留 |
| onceClaimRawAckLostResponseAndLateRevocationKeepOriginal | 同上 | r1；真实 claim/crash、并发、RAW ACK、迟到撤权、UNKNOWN 原 key/check 不二派 |
| sharedReadonlyAndUnprovenPointerCannotDispatch | `isolated-fifth-r5/actual.json`、`method-results.json` | r5；本轮四方法中只有此项通过，不把该 batch 冒全绿 |
| candidateValidityEmptyInvalidAndTeamRemainUnverified | `isolated-sixth-r5/actual.json`、`method-results.json` | r5；本轮三方法中此项与下项通过 |
| exactTwoOperationCasPreservesProtectedFieldsAndRejectsDrift | 同上 | r5 |
| managedSyncFenceAndRealBearerPreparationKeepReadPurposeAvailable | `isolated-sync-third-r5/actual.json`、`method-results.json` | r5；单方法 compile/test 0 |

上述通过切片均记录输入前后 SHA 相同。r1 共享组另在 `isolated-third-r1` 通过；r5 共享组因新增全局资源 fence/跨租户边界才再验证。

新增有限证据覆盖：合法本人/空与非法引用/Team 未验证；未选候选无额外 Native 请求或他人登录；所选执行人 cut 不变而新增主体禁用、撤权、轮换或 Native 版本漂移时 0 PATCH；共享只读、隐藏、legacy、跨租户关联 fail closed 且不泄详情；全部当前可写的多项目正例；两连接真实 Root bind 与 Reviewer reservation 互斥，完成后合法只读 REUSE 保持；完整两 op/If-Match、自然 name 排序与完整保留 ref、空 remove 成功但不就绪、矛盾 ACK 保 RAW 不填绿；真实 Bearer 与生产 P1Configuration provider 的本地事务接合。

模型入口边界：真实 Synchronizer.plan 在无本人 managed mapping 时 403/0 Native；execute 写门拒绝；readback 保留既有 `MODELING_OPERATION_NOT_QUALIFIED` 403→UNKNOWN、0 G HTTP/0 PATCH，并 instrument 实际 Registry 证明不调用新增 Reviewer gate。没有声称该 fixture 已获 Glossary/model 读写资格。`readFailedApply` 仅静审确认仍委派原只读 runtime，未重造旧三步/租约矩阵或声称真实原任务读回成功。modelSyncQualification 继续 NOT_VERIFIED。

## 真实 producer

`producer-r5/actual.json`：仅 producer helper javac 0、produce 0、输入稳定；复用 `isolated-sixth-r5` 已编译的固定 r5 产品/夹具类，没有重跑旧测试方法。

`producer-r5/actual-wire.json` 来自实际 H2/ownedHTTP + MockMVC Controller CommonResult，16 键：无 observation/有 observation/blocked Context、Subjects、两个新增 User 逆 name 输入的 Plan、terminal SUCCEEDED Intent、共享只读 Context/Subjects、UNKNOWN 原 Intent 及同 case Context/Subjects。原 Prepare/Confirm 对象原样序列化；after 为服务端真实排序；terminal action 为真实 INSPECT_CURRENT。已交前端作物理 parser 联核，不手造 DTO、不增加 action。

## 保留失败与因果校准

- r1 最初共享失败：用 super_admin 模拟失去项目读权不成立；改既有 manager 的真实 grant。并发失败：ownedHTTP 单线程阻塞第二请求；仅派生夹具加 HTTP executor，真实 SQL/current/tenant/resource 锁未被 mutex 或 fake grant 替代。
- `isolated-fourth-r4` 及 `isolated-root-precondition-r4`：Root 正式预建即 BLOCKED/NOT_SENT；fake broker SQL 错用 `account_id`，实际列为 `atlas_user_id`。没有到达 Reviewer 场景，不泛称四项业务失败。
- `diagnose-added-r5`、`diagnose-added-local-r5`：新增候选被本地 `ROOT_IDENTITY_PROFILE_CHANGED` 拒绝，因夹具篡改系统派生 nativeName。保原名字并动态构造排序后闭合，未放宽产品门。先前 SELF 因果推断明确撤回；Broker.withVerifiedBearer 不消费该 requireResource callback。
- CAS Array/Object 去 href 期望 helper 与 readback 原异常类型/UNKNOWN 预期差额均仅修 E 夹具。所有失败、原 stderr/命令/退出码、诊断和源 pin 保留。

非作者确定的 Native name 排序接缝已在产品 r2 前向修正，r5 真实多 User 子情景通过。当前关键差额未见新的确定阻断；这些有限结果不代替真实 Native Reviewer 验证、Root 独验或真人验收。
