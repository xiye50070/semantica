# M1.3 Root 独立技术验收

记录时间：2026-10-24 04:47 UTC。结论：**ROOT_ACCEPTED，限104/S6、om-p1-isolated和原M0粒度的本节点技术范围；真人最终验收PENDING。** 本文不冒Git已经交付；远端核对完成后才向原Work放行M1.4。唯一进度见[里程碑计划](../../plans/2026-10-22-atlas-metadata-registration-milestones.md) §17.69。

Root已亲读逐轮关键完整源码差额、原执行结果、有限检查、真实终态及两张页面截图/完整DOM；非作者分别复核新增源码、终态和原验收覆盖，无未了阻塞。充分且未变的先前证据按原cut复用，没有重新执行产品、测试、SQL、HTTP、浏览器或服务操作。原§5四项判据没有删除或替换；拒绝及晚失效按[联调卡](m1-3/runtime-integration-card-r1.md)允许的具名有限证据接受，不冒所有故障已在现场注入。

| 原判据 | 独立结论与证据层级 |
| --- | --- |
| 有限配置、原值/增量/影响和身份分离 | prepare/confirm的完整差异、共享影响、当前权限及版本再次校验已有源码与有限检查。104/S6真实批准11操作；管理通道只同步受管策略，业务使用原对应OM_USER。前端取消/Escape/dirty及原key保留为真实组件/浏览器mock检查，不称真实后端取消全过程。 |
| 策略及逐操作真实生效 | 原Policy/Role同步成功并独立读回；本人普通身份真实执行选定11项，最终页面11 VERIFIED、4 NOT_VERIFIED。配置、身份、策略和资格分别有证据，GET不代写资格。 |
| 五类拒绝和无高权fallback | Atlas/项目拒绝、普通查询用户、范围/父对象不匹配复用具名有限用例；当前9c1真实Store→Adapter→Registry→Consumer→Broker→SDK链新增OM403和只读成功不转写资格两方法，最终2/2通过。IAM、JWT及原生授权响应为fixture，没有执行真实OM RuleEvaluator/ViewBasic规则或现场拒权。 |
| 动态失效、晚ACK及UNKNOWN保护 | 原REVOKE/CANCEL/EPOCH有限方法和最终双父H2方法分别覆盖派发前cut变化拒绝、返回晚失权保ACK且不填绿、不再次领取。真实策略旧失败、原资格UNKNOWN及首子REQUIRES_OPERATOR均保留；最终两个旧行完整16列hash不变。未为补测再次轮换凭据或移除真实策略。 |

## 实际正向结果

原计划1a05f1d5-d90f-4c96-96f8-0b01bd7f7cf5、key5c2cd808-d335-41ac-8116-3abb70a59dba、root run08a268d3-1118-48f5-9e20-7aa0e8967549保留。原7项与固定后继3984bdb5-6d2e-3935-ba43-b019a92027f6五步合并（LEASE重叠），覆盖TARGET_CREATE/READ、TERM_CREATE/UPDATE、RELATIONSHIP_TYPE_WRITE、CHANGESET_CREATE、LEASE_ACQUIRE、MODEL_VALIDATE、CHANGESET_APPLY、MODEL_READBACK、REVIEWER_UPDATE。AXIOM_WRITE及三项METADATA未选，继续NOT_VERIFIED。

04:27–28 UTC新增startup5和后继25共30次真实HTTP均200；资源写仅新lease、conditional apply和reviewer各1。seq17104唯一apply后，原ChangeSet d98f7d53-67fe-4672-9414-2746fe2ef302独立读回APPLIED/0.2，原operation处理/应用1/1、失败0；原Term73baf489-acac-4dc0-8c75-d2cbe669a952到0.3，描述精确读回。seq17114最后才更新原Glossary bc616300-c357-49b8-bcd6-5be0f57758b8 reviewer，读回0.2且仅原普通人员UUID2d0540a5-d6fd-4f24-a5a4-270eaa54dc26；不能把该后置reviewer旁路当作先前策略授写的因果证明。

04:28:44一次终末SELECT三行，3984 SUCCEEDED/v23、五步匹配typed ACK；原08仍UNKNOWN/hash c45fc2ac265a3d3d68b63359924c09e79dbde21ebfe50c965d141d282be8c9b7，首子1a05仍REQUIRES_OPERATOR/hash91a5691e8682bf8bd4b6768fc10d98cfd72cde705e9ce9252acab674a7343421。Root核公开安全投影与官方执行结果，未读取私有原行或宣称重新计算私有全账。当前累计17116/GET17074/auth13/ordinary9/mut42/object19，旧前缀、失败和ACK保留。窗口04:33:15截止、预算已用完，不构成后续运行许可。

主要原件：[终末联合证明](m1-3/evidence/phase2-final-ui-readback-r1/main-native-terminal-ui-proof.json)、[原生正文安全语义](m1-3/evidence/phase2-final-ui-readback-r1/main-native-semantic-body-readback.json)、[三行终态安全投影](m1-3/evidence/remaining-five-phase2-terminal-reader-r1/m13-phase2-terminal-20261024-r1-safe-proof.json)、[完整DOM](m1-3/evidence/phase2-final-ui-readback-r1/matrix-visible-dom.txt)。两PNG分别上下滚动截面，不冒单图15项同屏。操作入口及证据分层见[交接卡](m1-3/root-review-handoff-r1.md)。

## 有限拒绝与修复边界

新两方法在04:37:35–38编译/测试0，最终2/2：`om403WriteRetainsDispatchAndNeverFallsBackOrReplays`、`onlyViewBasicReadSuccessNeverQualifiesReviewerWrite`。第一例普通bearer收到1次POST403，DISPATCH保留而ACK0、UNKNOWN、不重派；第二例GET200仅使TARGET_READ绿，PATCH403使REVIEWER_UPDATE保持NOT_VERIFIED，管理fallback0。首次遗漏/api的fixture失败、后续通过和最终status/bearer断言分别保留，不按重复运行累加unique数。见[完整边界](m1-3/evidence/qualification-write-denial-finite-r1/README.md)及其final actual/stdout。

其余有限方法按原cut引用：`callerAndTargetCurrentAuthorityScopeAndReservationAreReal`、`ordinarySelfReadsConfiguredMatrixWithoutManagementOrNativeCalls`、`targetOrParentMismatchIsZeroHttp`、`foreignOwnerReadbackDoesNotBecomeQualified`、`selectedValidationChecksFullPayloadAndFiniteReadSet`、`lateRevocationCancellationAndCredentialEpochCannotFinishPolicySuccess`、最终双父`doubleParentCurrentCutAndNonExactPhase1NeverSend`与`collisionOtherInflightAndLateLossKeepKnownAck`。这些是分时源码/fixture/实际产品链检查，不是最终cut单轮全矩阵真实故障验证。

最小修复包括Role引用href的CAS兼容、比较时精确空派生字段归一、保原任务与ACK的固定一次续接、代理查询编码及E-only启动准入误拒。最终Store/Adapter守卫、旧行hash、current cut和权限保持；E-only callback移除已准入后的readiness误拒，执行截止未延期。原失败原因不改写，未增加通用恢复入口或第三次循环。

## 交付与限制

[最终源清单](m1-3/evidence/phase2-final-ui-readback-r1/final-product-source-cut.json)为Cloud27＋React3。Cloud9c1完整offline package0但skiptests，有限检查另列；前端gate/tsc/Vite0。Root复用已核构建来源，没有重hash全树。Native为c2b单源兼容补丁、49类CP0覆盖包（6重编译/43保持），实际apply兼容成功；不是Native主checkout集成或完整reactor发布。提交Native源/补丁与必要构建说明，不提交classpath依赖JAR、classes、私有RAW、实际SQL行、argv或凭据。

技术接受不表示共享环境、全15能力、所有现场负例、生产发布或最终人工验收完成。M1.4创建/复用及项目绑定、M1.5完整reviewer就绪未在本节点接受。精确Git远端核对后同一Work进入M1.4；不重复原11项、策略同步、凭据轮换，也不沿用已耗尽窗口。源码回退不删除四个对象、已APPLIED效果、原任务/ACK/UNKNOWN；业务补偿须另有具体范围。
