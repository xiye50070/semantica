# M1.4 独立验收卡

对应唯一计划 §5 M1.4；本文件是操作与证据卡，不是第二进度账。状态只记唯一计划。M1.5 Reviewer 完整资格、模型构建/发布和 M2 均不在本节点。

## 产品路径

项目准备 → 目标与元数据 → 配置项目目标。项目编辑的目标摘要只读，不再接受手填 Native UUID。Atlas 当前项目管理员显式选择本项目有效的受管执行人员；原生所有调用使用该人员 OM_USER，管理发起人与执行人员分别展示，不回退共享管理员身份。

CREATE 强制 Owner 为选定人员的 mapped Native User；初始 Reviewer 默认空，可显式选择该本人。本人 Reviewer 的持久授权效果须提示；本节点完成不把 Reviewer 或完整项目就绪填绿。REUSE 只列 Atlas 已正式绑定、调用者当前有权看到且执行人员经逐 UUID 本人 GET 可读的目标；目录覆盖范围必须明示。

## 有限检查

| 对照原卡 | 必要证据 | 层级 |
| --- | --- | --- |
| 创建与准确绑定 | S6 无目标→预览→确认一次 create-only POST；真实 UUID ACK、独立 GET 名称/版本/owners/reviewers/初始配置一致；项目 CAS 指向同 UUID | 真实 Atlas UI + 原生读回 |
| 另一项目复用 | 新空测试项目，104 仅本项目 USER，沿既有 M1.2 为同一稳定身份配置本项目映射；选择前项新 G，确认仅本地绑定，Native 写数为 0；G 字段和 S6 指针不变 | 真实 Atlas UI + 原生读回 |
| 同名/404/未索引/并发 | 已占用名称不自动复用；精确 UUID + 当前授权/目录来源防伪；不把隐藏 404 或搜索空值当不存在；同名和跨项目 guard 竞争只一派 | 现有目标 UI + H2/owned HTTP fixture，分开标注 |
| ACK 丢失与部分成功 | 原 key 先持久、单派；丢 ACK 保 UNKNOWN/原意图，刷新与核对只 GET；已知 ACK + local-bind 失败只重做本地 CAS，Native create 仍一次；自身 v→v+1 可终结，旧计划拒绝 | 有限耐久 Store/真实 SDK fixture，不冒现场故障注入 |
| 当前权限 | 两主体任一失权、目标人员失效/凭据代际改变、Native 拒绝均不授成功；ACK 后失权保留原生事实；无管理员 fallback | 有限当前权限/owned HTTP fixture，现场正向身份另列 |
| 旧 API 旁路与维护 | 普通项目 create/update 不可注入/改变目标；更新其他字段省略目标；改绑展示旧绑定及计划失效，保留旧 G/发布指针 | 后端有限检查 + React 组件/路由检查 |

## 现场边界

正式新建目标名称固定 `AtlasM14_S6_20261024_R01`，S6=`3f10ce21-57fe-4efe-91c6-c9a3e4cf04f6`，实例 `om-p1-isolated`，执行人员 104；原 M1.3 四对象不作为新建证据。初始 Reviewer 选择空。另一新项目显示名 `M1.4 根目录复用验证`，先保存真实新项目 UUID，再绑定到该同一新 G。不得借用 S6 scope 消费另一项目请求。

现场业务只允许新 G 的 create-only 一次及精确只读；不修改既有 G、Term、Role、Policy、Reviewer，不刷新旧资格或重放 UNKNOWN。另一项目人员资格及 M1.2 映射属于显式本地配置；新原生身份、轮换或策略同步均不需要也不执行。当前已有104项目USER不冒另一项目已获权，实际权限变更仍走正常审批。

代理延续同一累计账，完整保留旧前缀、预算、UNKNOWN/latch/原意图；M1.4 只启用独立具名新目标 scope，不续已耗尽旧窗口。精确当前基准、有限调用预算、执行窗口及 SDK 实际请求绑定待产品固定后填写现场执行卡；未绑定不启动真实 HTTP/认证/SQL或服务动作。只对本节点新增本地表执行必要迁移，迁移源码、隔离数据库验证、现场执行分开记录。

## 完成与交付

有限测试与完整构建成功后，保存真实 Atlas 页面截图、逐步实际结果和必要安全投影。所有历史失败与未知事实保留。交监督 Root 独立验收与精确 Git 交付；本节点不自行标 ROOT_ACCEPTED、USER_ACCEPTED，也不提前实施 M1.5。


## 本次证据导航（固定cut）

- [最终后台包 r5](evidence/full-build-r5/receipt.json)：2647输入、20模块package0；测试跳过，必要具名有限方法另见[已知201说明编码方法](evidence/backend-root-finite-r1/isolated-eleventh-r1/actual.json)，不是现场反例。
- [真实S6原任务持久读回](evidence/original-intent-after-local-r1.json)：原UUID/version0.1、SUCCEEDED、项目1→2、原201字节与旧UNKNOWN永久历史；仅一次SELECT。原reader的otherEight布尔存在IntNode/LongNode表示假阴性，准确四字段比较及纯诊断分别见[主前向核对](evidence/original-intent-after-local-pointer-forward-r1.json)、[纯节点类型校准](evidence/original-intent-reader-r2/pointer-comparison-diagnostic-r1/safe-comparison.json)，不改旧结果或再查SQL。
- [原生累计与原始回执核对](evidence/native-local-bind-actual-readback-r1.json)：原17133全prefix/旧17/repair5/201保持；最终17141，原生create累计仍1，本地补绑步骤资源写0。
- [普通主类最终切换](evidence/normal-runtime-local-verify-candidate-r1/actual-m14-normal-atlas-cutover-r3-20261024/result.json)：一次TERM/Popen/空闲SELECT、原窗startup5；无Bootstrap，JVM初始化SQL未逐条测量。

这些原件分别支撑源码、隔离有限、真实Native与页面观察，状态仍只看唯一计划。真实跨新项目复用须先处理待确认的该项目104 USER及原身份本地映射；不能用S6成功或H2复用用例替代。


## 后续实际cut补充导航

- 新项目104 USER和原身份本地映射已经得到真人具体批准并通过原Atlas界面各保存一次，见[安全实际结果](evidence/ac2-authorized-local-setup-actual-r1.json)。上文固定cut的“待确认”保留为历史，当前进度只看唯一计划。
- [最后后台构建r6](evidence/full-build-r6/receipt.json)：唯一新DM34外置SQL纳入15产品路径/2648输入；20模块package0、skiptests、无输入漂移。当前服务仍r5相同Java，不因新SQL包重启。
- [DM隔离NULL约束实证](evidence/dm-null-unique-probe-r1/actual-r1/result.json)：仅owned fixture，NULL组合唯一冲突与CASE唯一索引的空值、非空冲突、并发争用分别验证，fixture已撤。
- [DM34首部分结果](evidence/dm34-nullable-unique-repair-candidate-r1/actual-r1/result.json)、[真实目录原始行](evidence/dm34-catalog-raw-reader-r1/actual-r1.json)、[只两DROP实际续接](evidence/dm34-drop-old-continuation-r1/actual-r1/result.json)：两个CREATE未重放；实际函数索引依赖位置均-1的原校验错误前向校准；最终旧两约束已撤、新两个条件唯一索引保留，S6原15字段安全投影前后与原一致。没有Root业务DML/Native请求/服务动作。
- 精确Git候选为[后台15路径交付](evidence/backend-delivery-15-r1/final-handoff-source-references-r1.json)与[前端11路径交付](evidence/frontend-delivery-11-r1/final-handoff-source-references-r1.json)。独占副本排除原ProjectService和三个前端路由文件的既有WIP；主已逐hash/size/精确集核，见[主读回](evidence/main-final-source-delivery-readback-r1.json)。本Work未操作Git/index。


## AC2最终实际读回

新项目真实复用已保存：原key03be0335-796e-4a88-adbf-339edcbb76cb、意图75149e86-6063-4216-89c1-4e8bff3c4fb4，SUCCEEDED/REUSE、同G0.1、项目1→2，顶部自动刷新。见[新意图持久回执](evidence/ac2-actual-reuse-intent-r1.json)、[九项目最终读回](evidence/ac2-local-reuse-after-r1.json)、[Native四GET与原账核对](evidence/ac2-reuse-native-final-readback-r1.json)及[真实截图](evidence/ac2-reuse-bound-20261024.jpg)。来源S6及其它八项目、原NativeG响应全字节不变；REUSE资源写增量0。完整固定cut说明与精确交付入口见[Root收包导航](handoff-for-root-review-r1.md)，监督Root独验/Git与最终真人仍是后续门。
