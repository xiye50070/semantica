# M1.5 Work 交付说明（待监督 Root 独验与 Git）

本稿只归集已存在证据，不改变唯一进度账、产品、Git 或运行环境。范围为 ONLY M1.5：原生 Glossary Reviewer 与目标派生就绪；M2 = **NOT_GO**。具体 G/104 Reviewer 及同映射禁用、恢复一次已获真人授权并执行，阶段最终真人验收仍 **PENDING**。本稿不自行宣告 M1.5 已接受。

## 交付切片与原四卡

后台为 [BE final r5：14 路径](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-candidate-r1/revision-r5/source-map.json)（`483e1d72…`），前端为 [FE final r7：7 路径](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/frontend-implementation-r1/revision-r7/source-map.json)（`30f02065…`）。[current-installed-cut-r2](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/current-installed-cut-r2/installed.json)记录21条当前安装源、19条受保护 WIP；候选、已安装源、构建与现场证据分别保存。原 Project 相关 WIP 不因本次交付混入 Git；精确暂存与最终 Git 由监督 Root 后续处理。

依据 [原验收卡四组](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/acceptance-card.md)，证据按下列边界提交：

| 原卡 | 交付行为与证据 | 本次可陈述的验证范围 |
|---|---|---|
| 1 主体与资格 | 受管获准 User、当前映射与 Native 主体分别核；空/无效/未验证不填绿，未选候选不换凭据或扇出登录。 | 真实正向仅104本人 User；有限方法覆盖空、无效、403、代际变化与未选候选。Team 原生类型支持，但当前有效成员及完整资格仍未验证。 |
| 2 条件更新与共享保护 | 完整共享影响/current guard；精确 `test /reviewers` + `replace /reviewers` 与 `If-Match: W/"0.1"`；ACK 后独立 GET。只读共享方、隐藏/不明或跨租户引用不能静默忽略。 | 正向使用 S6 独立 G `7daa15c1-5f4b-4272-bdcf-5575cca8ebe9`。原 AC2 目标不升权。唯一 PATCH200，独读0.2、仅104 Reviewer；保护投影不变。共享阻断、全可写多方、资源 fence 与并发拒绝有真实 H2/ownedHTTP 有限证据。 |
| 3 派生就绪与动态失效 | 目标绑定、Reviewer 资格、当前执行人写条件、目标就绪四项分开；依赖失效撤绿，恢复不自动沿用旧绿；受管模型入口有当前 gate，历史只读不改义。 | 下述真实禁用→恢复→重新检查链成立。真实模型同步派发、Term/租约/完整模型同步资格**未验**；入口挂接与拒绝边界由源码/有限方法证明，不冒现场同步 E2E。 |
| 4 未知效果与角色边界 | durable once claim、先存原始 ACK、晚失权不授成功、UNKNOWN 保原 key/事实只读核对不二派；Reviewer 与 Atlas 审批角色分开。 | 本次实际写是成功路径；Reviewer lost ACK/迟到撤权/并发/crash 等反向为有限验证，未制造现场未知写。原 M1.3 UNKNOWN、旧17162 READ_FAILED 与 stop latch 全保，不用新成功涂改旧历史。 |

Reviewer 是 **G 级持久原生授权**，无法按 Atlas 项目隔离，并可能影响下属术语继承、有效权限及后续工作流受理人。本次两 op 不直接写 Term；`termCount=0` 只代表读回时点，不能推导所有模型或未来术语均无影响，也不把 Owner/ViewBasic/非空 UUID 当 Reviewer 资格。

## 有限方法、构建与部署事实

后台 [非作者核验与分时结果](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/review-and-results-r1.md)保存六个 unique 方法，分时绿，**不是单轮 r5 六项全部重跑**：

- `configuredAuthorityNative403AndGenerationDriftNeverBecomeReady`、`onceClaimRawAckLostResponseAndLateRevocationKeepOriginal`：[r1 两项原绿](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/isolated-first-r1/actual.json)。
- `sharedReadonlyAndUnprovenPointerCannotDispatch`：[r5 shared](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/isolated-fifth-r5/actual.json)。
- `candidateValidityEmptyInvalidAndTeamRemainUnverified`、`exactTwoOperationCasPreservesProtectedFieldsAndRejectsDrift`：[r5 subject/CAS](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/isolated-sixth-r5/actual.json)。
- `managedSyncFenceAndRealBearerPreparationKeepReadPurposeAvailable`：[r5 真实入口有限方法](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/isolated-sync-third-r5/actual.json)。旧读门的403→UNKNOWN仍保留；这不是已获 G/model 读写权的证明，`readFailedApply` 未重造旧完整任务矩阵。

这些方法使用真实 Store/Consumer/Registry/Broker/SDK、H2 和 owned fake HTTP；IAM、身份、同步策略等前态是 fixture，不能等同真实 DM/Native Authorizer 验收。[真实 Controller producer](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/producer-r5/actual.json)及 [actual-wire](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/backend-reviewer-finite-r1/producer-r5/actual-wire.json)来自该链路序列化，供前端物理 parser 对接。

前端 [基础有限结果](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/frontend-implementation-r1/result-summary.json)为3个 API方法＋5个组件方法分时通过，覆盖四事实/假 READY、精确 body/query、owner scoped key/WebLock、共享阻断、默认取消/回焦、失 ACK 保原、晚403退事实、只读 USER。[r2 受影响增量](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/frontend-implementation-r1/revision-r2/result-summary.json)另核503/UNKNOWN、终态真实 `INSPECT_CURRENT`、TTL和集合差异；r3/r4补 owner URL 与在途 reload，r5–r7只修受影响界面布局/确认出口。原失败及其修正切片保留，**不宣称 r7 最终全套重跑**。隔离组件/浏览器的 stub API 证据不冒真实 Native；本节不新增执行。

| 既有实际 | 结果与限制 |
|---|---|
| [后台 offline full-build-r2](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/full-build-r2/receipt.json) | `mvn -o … -Dmaven.test.skip=true package` exit0；2655输入/源、候选、stage无漂移。Jar `e7b970de494365fa6ed8e143e39cae0444487f67c6d2d72fa3b9084836249471` /178566385B。skipTests不替代上述有限方法。 |
| [前端 normal full-build-r5](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/frontend-full-build-r5/result.json) | `pnpm run build` exit0，1050输入稳定；保留实际 stderr，未将构建当业务验收。 |
| [DM35 actual](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/dm35-migration-candidate-r1/actual-r1/result.json) | 5DDL returned、6SELECT，建3张具体 M1.5 元数据表及条件唯一索引，迁移时三表为空并精确核 schema；0DML/NativeHTTP/服务动作。DDL非联合原子；不重放33/34。迁移时空表不代表业务执行后仍空。 |
| [ordinary Atlas 切换 actual](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/normal-runtime-candidate-r1/revision-r2/actual-m15-normal-atlas-cutover-r1/result.json) | PID53275/create1792846666.327821，普通96参/Jar e7b970de，无 Bootstrap；startup5真实完成。控制器1SELECT/1TERM/1launch，不代表服务初始化SQL为0。 |

## 本次真实四阶段

实际目标为上述独立 G；Atlas 发起人1、原生执行人员104（非管理员 fallback），原生 User `2d0540a5-d6fd-4f24-a5a4-270eaa54dc26`。具体授权与冻结预览保于 [proposal](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/proposal.json)、[human verification](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/human-verification.json)；实际 intent `309706cd-7494-45a7-8b59-c709c4784d36`，原 operation key `d24a8e23-5bf7-4de0-bd95-cd8d137fee17`。

1. **唯一 Reviewer 写成功。** seq17201 PATCH200，`0.1→0.2`；seq17203独立目标 GET200，随后完整当前检查结束于17211。UI显示 `DISPATCHED / ACKNOWLEDGED / VERIFIED` 和 `REVIEWER_TARGET_READY`（13:57:01.258Z 的事实）。见 [原账摘要](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-native-grant-base-readback.json)、[AX](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-target-ready-ax.txt)、[截图](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-target-ready.png)。
2. **禁用同一项目映射后目标未就绪。** 原任务 `12b11c34-349c-4201-86c6-d3247decad4e` 完成，页面为已禁用、`MODELING_IDENTITY_NOT_READY`；同 Native User、映射版本9、凭据版本2保留。维护未轮换、Native调用为0。见 [禁用映射AX](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-mapping-disabled-ax.txt)、[目标撤绿AX](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-disabled-target-not-ready-ax.txt)、[截图](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-disabled-target-not-ready.png)。
3. **恢复后先未就绪。** 原任务 `a37e3594-bfe4-42e9-8532-2e68404d8fa0` 完成，恢复同一映射；仍须当前检查，不把映射已保存/已同步自动升成 Reviewer或写条件已验。见 [恢复映射AX](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-mapping-restored-ax.txt)、[重新检查前AX](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-restored-before-fresh-check-ax.txt)及 [两次维护Native0计数](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-mapping-maintenance-native-count.json)。
4. **尾段8GET后重新 READY。** seq17212–17219全部 GET200，无第二次 PATCH/登录；14:00:26.616Z UI目标重新 READY。Reviewer与当前执行人事实核验时间14:00:26.463658Z。见 [尾段原账](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-native-maintenance-tail-readback.json)、[最终AX](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-restored-target-ready-ax.txt)、[最终截图](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/reviewer-authorized-proposal-r1/actual-restored-target-ready.png)。这是该时点的当前事实，受产品120秒 observation TTL及依赖变更控制，不是永久绿；页面仍明确 `TERM_AND_MODEL_SYNC_QUALIFICATION_SEPARATE`。

只读独立复核 [主16调用/raw证明](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/grant-readonly-receipt-check-r1/proof.json)与 [同快照尾段证明](/Users/wangmingzhang/Env/semantica/docs/superpowers/implementations/2026-10-23-atlas-registration-m1/m1-5/evidence/grant-readonly-receipt-check-r1/tail-forward-proof.json)核了：唯一 grant PATCH；24 completed/raw均完整；pending空、未停；原17195之前全前缀、旧namespace/latch/history保持；独立读回及尾段的保护投影（排除明确版本/更新时间等易变字段）与原before相等，Reviewer恰104 User。该复核不作未查询的 DM任务/映射状态声明。

计数以实际 Native 账为准：**17185→17219 共34次调用 = 33 GET + 1 PATCH**，由只读刷新10GET、grant15GET＋1PATCH、维护后尾8GET组成；两次本地映射维护另记 Native0。最终 GET17167/auth15/ordinary14/mutating52/object22/M15 root1/grant1。不能写成“34GET＋1PATCH”。

## 保留项、能力限制与交接边界

- 原失败与诊断均保留：早期 H2/ownedHTTP 夹具问题、未采纳的 r6 SELF因果草稿、前端焦点/布局失败，以及原17162 HTTP200但代理观察器 READ_FAILED停点。脱敏 by-id email与SELF凭据不同，前向修订没有把旧停点改为成功；旧计划不改 expiry，旧窗口不暗延，过期 observation 正确撤绿。
- 原 M1.3 `08a268d3…` UNKNOWN、其他历史 UNKNOWN、原 Native ACK/raw和全局stop latch均保。没有清状态、重建身份、轮换、Drop、重做旧矩阵或冒用 M1.3资格对象作正式本目标。
- User真实正向仅104。Team语法/类型原生支持，但有效成员、必要资格及完整路径仍未验。全平台共享inventory目前保守上限100项目；超界、隐藏/跨租户无法证明的影响 fail closed，不声称任意规模支持。
- 目标 READY 不代表创建/绑定/Reviewer/当前写条件之外的业务已完成；没有实际Term、模型同步、执行包或事实发布，M2仍NOT_GO。当前同步入口反向有限证据与真实动态页面证据分列，不能合成完整模型同步 E2E。
- 源码回退**不等于撤回已生效 Native Reviewer授权**。已确认的 G级持久效果必须保留；禁止以 Drop、重建Glossary、SQL改状态或自动反向PATCH代替撤销流程。任何后续权限回收需另有具体当前授权与受控操作；本草稿不发起撤销。
- 监督 Root 的最终独验、精确 Git交付仍待处理；阶段最终真人验收PENDING。具体操作的真人授权已发生，不等同整个阶段最终验收通过。旧原件不回填；本稿是docs草稿，不是唯一账状态更新。


## Main终末本地核对与当前口径

[终末只读结果](evidence/final-local-state-reader-r1/safe-result.json)由Main在2026-10-24 14:09:35 UTC执行一次固定5条SELECT取得，exit0、stderr空、DML/DDL/HTTP均0，无重试；源码21024d7c…已Main全文及44B安全输出差额亲读，非作者独立静审，编译0。Reviewer原意图SUCCEEDED/v6，原key不变、activeSlot空、同G无活动写意图，rawACK与journal hash匹配、独立读回VERIFIED。

S6项目v3、配置4→5禁用→6恢复，原identity binding仍v9/credentialEpoch2/desiredRevision3、BOUND/IN_SYNC；AC2仍旧G、项目v2/配置1/enabled。两维护意图nativeTask均空，无额外Native写。两条旧M1.4 Root历史分别仍SUCCEEDED/v10和v5、原操作键和历史绑定v2保持。本查询只涉及指定行，不冒全平台所有项目联合快照。

最新observation实际14:00:26.616、expires14:02:26.616，snapshot配置6与当前cut匹配、报告与snapshot hash匹配，确在恢复后产生。14:09终末读取时TTL已正常过期，当前保守状态为NOT_VERIFIED/NOT_READY，**没有为归档继续发GET补绿**；不能用所存READY报告冒永久实时就绪。完整模型同步资格仍NOT_VERIFIED。

[最终21源及19保护项](evidence/final-source-cut-r1/observation.json)逐SHA/size核同已验cut，21份实际源码副本在同目录copies下。Cloud/React仍原HEAD与codex/atlas-om-semantica；Work未Git/index写入。人工检查路径见[手动验收清单](manual-review.md)，各项PENDING。

原始运行准备中的绝对路径symlink只是本机执行脚手架；最终evidence-index.json分别记录实体文件和executionAliases，不跟随链接、不把它们当前外部目标冒成历史冻结附件。完整本地原件、旧失败及准备态均保留，监督Root可据精确产品清单和必要证据作独验/交付，不以本说明冒Root验收。
