# M1.1 Root独立技术验收

记录时间：2026-10-22T20:09:18.761234+00:00。结论：`ROOT_ACCEPTED_LOCAL_IMPLEMENTATION_AND_ISOLATED_VALIDATION`。真人与真实共享环境验收 `PENDING`；没有将整个M1或原生能力标为完成。

依据[唯一计划§5 M1.1](../../../../plans/2026-10-22-atlas-metadata-registration-milestones.md#m11-新项目与可恢复的准备流程)和[独立操作卡](../../m1-1-acceptance-card.md)。本节点限定本地项目/草稿/准备意图，后续原生身份、写资格、目标、来源、执行包、发布与查询继续未就绪。

| 独立判据 | 结果与证据 |
| --- | --- |
| 新建及刷新/原键恢复 | 真实服务隔离持久库幂等，合成界面新建后进入就绪页；跨页签同槽排他只一POST，未知结果沿原键GET，不用第二键重放。 |
| 缺项与草稿 | 服务端恰12项，页面六组；仅本地PROJECT就绪。空说明先显式保存v1，草稿/项目版本CAS；本地任务成功不改变原生状态。 |
| claim/取消及UNKNOWN | 两线程、两个H2 SESSION_ID、事务内latch真实争用，两个胜出顺序分别通过；首次claim含RETRY_WAIT拒取消，旧attempt/owner/lease回执拒绝，UNKNOWN不重派。 |
| 真正进程恢复 | 第一JVM37062正常exit0后，第二37090重开同file-H2；原project/draft/intent及已选审计投影相同，重复原键不新增记录。不是仅同JVM重建service。 |
| 当前授权与晚到隔离 | 当前scope授权先历史命中；USER仅摘要readiness，无管理详情/草稿/任务请求。401/403撤下旧缓存和动作，原键保留；新准备GET403补例真实通过。 |
| Aham与必要键盘/布局 | pc8浅色/现有React AntD；内联放弃确认不叠弹窗，Portal局部ref握手hard-focus通过。Root亲看r6 1440/390 viewport PNG；真实Tab聚焦保存，字段底均低于footer顶、footer在1000高viewport内，无横溢。 |

Root已亲读关键源码、完整API/Editor/Readiness/CSS、路由及http精确差额、两独审报告、原结果/失败和关键trace；自己逐26个前后端路径、13前端副本/原前态与完整patch再生、71项最终前端引用SHA/size及无关WIP核对。前端6旧+7新、后端13路径均固定。Scope全文回原；共享http仅可选单请求POST headers和PUT AbortSignal，旧刷新/身份/不重放主体保持。

证据：

- [后端静态独审](../independent-r1/backend-final-static-review.md)、[原37项运行独审](../backend-independent-runtime-r1/review.md)：13新+15旧Project+9旧Space=37/37。旧14/23标签以[前向校准](../backend-count-clarification-r1/count-clarification.md)限定，不改原封件。
- [真实两进程恢复](../backend-process-restart-r1/review.md)及[审计投影限定](../backend-process-restart-r1/audit-projection-clarification.md)：独立1场景，不加进原37项；原89class纯编译比对与424输入稳定。
- [前端固定13路径](../frontend-source-cut-r1/manifest.json)、[最终验证](../frontend-final-verification-r1/report.md)：最终13新API/Editor定向项通过；既有ontologyApi16+Scope12分时复用，共41个不同方法，不冒单次最终41。完整production mode gate、tsc、Vite构建exit0，产物仅/tmp；保留chunk与JSDOM提示。
- [浏览器原件索引](../frontend-final-verification-r1/index.json)、[围栏前向独审](../frontend-browser-controls-independent-r2/review.md)：11不同代表分时各有成功，r1–r5失败含r4 timeout保留，未冒最终同cut全11矩阵。后期trace:on/实际SWblock/strict5297/五proxy9，精确origin-method-path-query，3HTTP+1WS有限负向拒绝。早期2PUT未完成及2GET无正文保留，不能说历史全部零unfinished。
- [Root源码核对](final-source-personal-readback-r1.json)、[真实重启读回](process-restart-personal-readback-r1.json)、[r7原trace亲读](frontend-r7-personal-trace-readback-r2.json)、[无关WIP](unrelated-wip-personal-readback-r2.json)、[最终索引](frontend-final-packet-personal-readback-r1.json)。

边界：MySQL/DM8迁移未应用、共享后端/前端未部署、真实界面与后端联合验收未运行；正常file-H2进程退出不代掉电/崩溃耐久。暗色、原生200%缩放、完整IME未验（仅合成composition事件）。没有OM/源业务读写、管理员fallback、M0再调用、Git提交/推送或用户最终接受。后续集成和整体M1交付仍必须补真实部署/消费者证据，不能拿本节点合成界面替代。

按用户最新连续开发授权，放行原M1.2的动态人员/精确scope身份接入；M1.3–M1.5仍逐节点门控。新原生操作需先固定本节点新的隔离范围与最小执行卡，不能沿用已耗尽M0预算/旧会话或重放旧对象。
