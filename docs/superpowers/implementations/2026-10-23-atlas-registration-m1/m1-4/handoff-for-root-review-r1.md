# M1.4 交监督 Root 独立验收

本文件是本次固定交付 cut 的证据导航；唯一进度账仍为计划第17节。Work技术收口，尚未ROOT_ACCEPTED、Git交付或USER_ACCEPTED；M1.5/M2不得由本Work提前实施。

## 实际结果

S6已通过Atlas创建根目录 `AtlasM14_S6_20261024_R01`，原生UUID `ab2c2d5f-05cb-4c5d-959e-a9f29d1fd4be`、版本0.1；原201和旧UNKNOWN的说明编码停因完整保留，按原意图同UUID只读核验后本地绑定成功。没有第二次create。原S6项目版本2、目标hash839d93d6…保持。

收到真人对104/新项目USER及原身份映射的具体批准后，通过原Atlas界面各保存一次。新项目 `ebed8ab8-a686-47ee-a7cb-cb778617730d` 已复用上述同G：新意图 `75149e86-6063-4216-89c1-4e8bff3c4fb4`、原key `03be0335-796e-4a88-adbf-339edcbb76cb`，SUCCEEDED/REUSE/row5，immutableSelection锁定0.1与observationHash，项目CAS1→2。新行不含create回执或dispatchHash，原生创建NOT_REQUIRED，独立GET VERIFIED、本地BOUND；页面顶部自动刷新到版本2及准确UUID。

| 证据 | 固定原件 | 边界 |
| --- | --- | --- |
| S6原意图/旧UNKNOWN/唯一201 | [持久原回执](evidence/original-intent-after-local-r1.json) | 不重开S6、不重放CREATE |
| 新项目授权与映射 | [实际本地结果](evidence/ac2-authorized-local-setup-actual-r1.json) | USER仅此项目；同一binding9/credential2/desired3；0Native task |
| 新REUSE持久回执 | [同原key一次SELECT](evidence/ac2-actual-reuse-intent-r1.json) | 新行有selection hash/version；来源意图引用由另钉S6回执提供，非新行存了sourceIntentId |
| 九项目与映射读回 | [最终一次SELECT](evidence/ac2-local-reuse-after-r1.json) | 仅AC2 v1→2；其它八项目包括S6逐四字段一致；S6完整选定回执/原201/UNKNOWN历史hash相同 |
| 实际Native4GET | [固定原账读回](evidence/ac2-reuse-native-final-readback-r1.json) | 两轮self→同G，4×200；G响应全字节等之前17146/17141；0登录、0资源写/0create |
| 页面 | [复用成功截图](evidence/ac2-reuse-bound-20261024.jpg) | 显示版本2/独立读回/绑定，当前viewport滚动cut；Reviewer与完整就绪仍待下一节点 |

现场最终累计17150/GET17103/ordinary12/management14/mutating47/object20/rootcreate1。原17146全部operation前缀、62旧ledger成员与旧namespace/denials/UNKNOWN全保；资源写数不增。当前只读目的已用完4GET，不再发Native请求。旧窗口10:12:34.057不延，另具名warm窗10:22:04.998–10:37:04.998，原104会话有效至10:58:26.730，仅恢复该现有会话到RAM，不更新共享管理员引用。

## 必要修复与历史

真实REUSE首预览在本地INSERT失败。owned DM fixture证明nullable组合UNIQUE对第二个相同非空前缀+NULL冲突，CASE条件唯一索引保留非空冲突及并发单获胜。只新增DM34；不改已应用DM33/Java/MySQL/权限。原两CREATE实际返回后，校验器误把DM函数索引三个依赖列的POS=-1当重复；原partial/真实12行和错误全部保留。独立两DROP续接后，两个新UNIQUE函数索引仍在、旧两约束已撤，Root原15字段安全投影前后/原均同，0业务DML/Native/服务动作。原件见验收卡后续导航。

warm续接首次default文件沙箱执行在第一私有intent写前/该处停止，未产生任何新私有文件/namespace、原PID13421和17146不变。纯只读九phase全通过，未采到OS errno，不能把精确沙箱错误当实证；原失败保留。改用正常require_escalated文件权限通道、同一脚本/input/window/budget执行一次通过，13421→19452；0HTTP、0Atlas/Native重启、0login或权限变更。这是正常权限模式修正，非自动审批拒绝或业务重放。

## 源码与构建交付

- [后台15路径](evidence/backend-delivery-15-r1/final-handoff-source-references-r1.json)：最后r12原14+唯一DM34。durable stageable源排除ProjectService原WIP，其余14等current，完整WIP patch保。
- [前端11路径](evidence/frontend-delivery-11-r1/final-handoff-source-references-r1.json)：7产品4check。最终known201承接与三入口context刷新已组合，durable stageable源排除App/standaloneRoutes/routes.test原WIP。
- [主物理读回](evidence/main-final-source-delivery-readback-r1.json)：15+11 current及durable源全SHA/size/精确集一致，后台old14完整patch prefix保持，只加DM34。
- [最后后台构建r6](evidence/full-build-r6/receipt.json)：2648输入/20模块offline package0，skiptests/sourceDrift空/stderr0。当前运行r5/04b9包Java同最终源，只有外置SQL新增，不重启Atlas。
- [最后前端构建](evidence/frontend-local-bind-context-refresh-r1/check-r3/actual.json)：production gate/完整tsc/Vite0，1045输入稳定。
- 后台原8方法、版本漂移/真实Bearer/已知201承接等必要增量与前端有限组件均沿分时成功cut复用。所有旧失败保留，不冒最后同一cut完整矩阵，也不把fixture当真实故障注入。

所有交付源码副本已在E内，不依赖temp作为唯一来源。本Work未执行Git/index；监督Root按上述stageable精确集独验和Git，勿包含旧WIP或推Semantica本地main的347个无关提交。

## 保持的未验与下一门

当前真实正向证明仅新建/同UUID读回/绑定及跨新项目只读复用。Reviewer完整资格、模型构建与取数发布、端到端查询属于后续里程碑；不得把M1.4绑定成功冒整个项目就绪。原生说明按存储字面值显示，预览里的数值实体不是新增HTML执行；不扩为通用解码。当前M1.3操作项NOT_CONFIGURED保持，不用只读复用结果填绿。旧UNKNOWN/latch、其它原生资源、其它项目与原服务保持。最终真人手动验收PENDING。
