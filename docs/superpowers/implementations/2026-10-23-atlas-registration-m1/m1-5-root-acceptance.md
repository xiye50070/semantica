# M1.5 Root 独立技术验收

2026-10-24 14:30 UTC，结论：`ROOT_ACCEPTED`，限唯一计划 §5 M1.5 的有限技术范围。Reviewer 当前资格、最小条件更新、独立读回和动态失效均有对应证据；真人最终验收仍 `PENDING`。精确 Git 交付另记，不以本报告冒已推送。

## 原四项与结论

| 原项 | 独核结论 | 验证边界 |
| --- | --- | --- |
| 主体资格 | 实际104对应稳定普通 User；存在、状态、当前映射及目标数组分别核验，空 Reviewer 不就绪。 | 真实正向仅104；空、无效、403、代际变化和未选主体用具名有限方法。Team 类型支持但完整资格未验，不放行。 |
| 条件更新与共享保护 | 真人具体授权后，S6 独立目标仅增补104；精确版本及原数组条件、唯一 PATCH、独立 GET 成立。原只读 AC2 不扩权。 | 原共享拒绝有现场事实；全有权多使用方正例、隐藏/跨租户、并发绑定与版本漂移为 H2/owned HTTP 有限验证。不能把这些合称现场全矩阵。 |
| 当前就绪与失效修复 | 本地映射禁用撤绿，恢复先待核验，随后新读回才就绪；120秒 TTL 到期正确失效。身份不可用的错误目标修复链接已最小修正为本项目身份配置入口。 | 真实维护与新读回复用；末导航差额2方法通过、接收页接线源码核验。没有为修链接重复禁用、PATCH或读取 Native。 |
| 未知效果与角色边界 | 原 key、ACK、UNKNOWN、17162失败和 stop latch保留；角色说明区分原生 Reviewer 与两类 Atlas 发布审批人。 | 实际 Reviewer 写为成功路径；失ACK、晚失权、并发、一次派发按具名有限方法接受，不冒现场故障注入或掉电耐久。 |

[原卡](m1-5/acceptance-card.md)、[Work交接](m1-5/README.md)、[人工清单](m1-5/manual-review.md)保留各自时点。本报告和唯一账给出最新 Root 结论。

## 实际结果与保护

新目标 `7daa15c1-5f4b-4272-bdcf-5575cca8ebe9`，名称 `AtlasM15_S6_20261024_R01`；S6项目v3，普通执行人104，Native User `2d0540a5-d6fd-4f24-a5a4-270eaa54dc26`。原意图 `309706cd-7494-45a7-8b59-c709c4784d36`、key `d24a8e23-5bf7-4de0-bd95-cd8d137fee17`。13:43真人原答复与新计划范围一致，未复用过期预览。

- seq17201唯一 PATCH200，版本0.1→0.2，Reviewer仅本人；ACK SHA256 `1822198e93f3866dbca766c7296a6d2fe6117fe986f34213322cc86d6c741af2`。seq17203独立GET200，随后完整GET保护投影与原before一致。精确test/replace及If-Match不触owners/tags/description/术语；不以PATCH ACK和GET全DTO相等证明。
- 同104/S6映射禁用意图 `12b11c34-349c-4201-86c6-d3247decad4e`、恢复意图 `a37e3594-bfe4-42e9-8532-2e68404d8fa0`，配置4→5→6，绑定v9/credentialEpoch2/desiredRevision3保持。两维护阶段Native HTTP增量0，无轮换、身份或Policy重发。
- 禁用时页面 `MODELING_IDENTITY_NOT_READY`；恢复先 `REVIEWER_TARGET_CHECK_REQUIRED`，原成功任务继续保留。17212–17219八个GET200后，14:00:26.616产生新READY。Root亲读四份关键AX及三PNG；截图只证明其可见区域，完整状态由AX及原回执补足。
- 17185→17219实际共34调用=33GET+1PATCH（刷新10GET、主段15GET+1PATCH、维护尾8GET）。终末GET17167/auth15/ordinary14/mutating52/object22/M15root1/grant1。旧前缀、namespace、UNKNOWN/latch保持依据公开安全投影；Root未重读私有全账。参见[主段证明](m1-5/evidence/grant-readonly-receipt-check-r1/proof.json)、[尾段证明](m1-5/evidence/grant-readonly-receipt-check-r1/tail-forward-proof.json)。

[终末安全结果](m1-5/evidence/final-local-state-reader-r1/safe-result.json)来自14:09:35.289556–.440961五条固定SELECT，官方exec-b8cbae59 exit0，无DML/DDL/HTTP/重试；非跨语句联合快照。Reviewer原行SUCCEEDED/v6、原key、ACK/readback匹配；S6新G/v3/config6，AC2旧G/v2/config1/enabled，两旧Root行SUCCEEDED/v10及v5、选定journal hash保持。维护SUCCEEDED按本地无Native task意图与配置链推导，不冒独立状态列。最新observation确在恢复后且cut匹配；14:09已过14:02:26.616 TTL，当前保守NOT_VERIFIED/NOT_READY正确，不为归档补绿。完整模型同步始终NOT_VERIFIED。

## 实施、构建及收尾差额

后台14路径（12 Java、2 SQL），前端7路径（5产品、2检查）。动态共享库存、同实例/G资源锁、UNKNOWN预约、当前权限与版本核对保留。DM35已按固定语句执行5DDL及8次显式前置/目录SELECT，非原子事务；普通Atlas运行包 `e7b970de494365fa6ed8e143e39cae0444487f67c6d2d72fa3b9084836249471`，20模块offline package0，2655输入稳定、skiptests。部署startup5有实际回执，JVM初始化SQL未量测。

后台6个不同方法分时闭合，真实Store/Service/Consumer/Registry/Broker/SDK、H2，IAM/OAuth/同步策略/Native响应仍有fixture。Projection入口实际调用有限方法中，readback UNKNOWN被原资格门挡住，未走新Reviewer门或NativeGET；readFailedApply未跑。不冒真实RuleEvaluator全矩阵或模型同步E2E。

前端原12个不同方法分时接受。隔离浏览器r7确认框/390与r3共享UNKNOWN原方法分别通过；r5 inner0但清理wrapper3历史保留，不冒整批exit0。末导航仅改Panel及test：identity=null指向本项目modeling-access，固定当前account/instance、保原任务参数；真实接收页只解释identityTask，原intent/reviewerIntent不被误作身份任务。原共享拒写仍指向独立目标。新方法+原共享方法于14:26:36–40两PASS、七skipped，原filtered=0计数错误另有校准；测试接收页是占位及浏览器历史返回，接收页实际接线依源码，不冒真实身份页面回归。

两源14:25:26安装，另外38项输入保持；Panel `dd9bc51f…/32493B`、test `e48f8862…/30619B`。14:27:16–40官方exec-c002f6a4前端production gate/完整tsc/Vite exit0，1050输入稳定，旧750kB告警保留。Root已核21当前源及19保护项；不重跑充分后端、Native或维护。新切片见[前端最终源](m1-5/evidence/frontend-identity-repair-nav-r1/final-seven-source-map.json)、[检查计数](m1-5/evidence/frontend-nav-finite-r1/report-count-clarification.json)。

Root与三个有界非作者分别核实际结果/终末reader、授权接合、原卡/前端范围；修后前端再次独核通过。Root亲读关键原结果、源码差额与保存的视觉证据，没有执行产品测试、SQL、HTTP、浏览器或服务。公开精选只含必要源/安全投影，实际argv、RAW、SQL返回行、会话、classes/JAR和大trace留本地。

## 后续门

M1.5精确Git完成后作整M1有限技术接受，再只放行M2.1。M1.1原有限本地/隔离结论不改；暗色、200%、完整IME、掉电耐久及真人最终仍未验。Team、未选建模能力和完整模型同步未验不填绿。既成Reviewer原生授权保留，源码回退不等于撤权；原UNKNOWN和失败不销项。
