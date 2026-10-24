# M2.2 实际执行位置诊断验收卡

状态只记唯一里程碑账。本节点 `IN_PROGRESS`，M3 `NOT_GO`；以下是待执行验收，不是已通过。

原M2.1连接、秘密和引用生命周期保持。新增诊断独立于配置状态；进入页面、刷新、保存或焦点变化不自动发起探测。浏览器明确选择本项目、批准实例下已有的建模人员，并提交连接ID、预期连接/秘密版本及该人员的 modelingAccountId；即使只有一个选项也不自动选择。发起人和对应OM身份可以是不同人员，报告分别绑定。浏览器不提交源主机、任意OM URL、SQL、项目scope、节点声明或秘密引用。

| 原卡 | 通过证据 | 拒绝或限制 |
| --- | --- | --- |
| 1 实际节点 | 批准的Worker从持久连接/当前秘密版本领取一次性短准入，真实核MySQL网络、TLS、身份及每个批准数据库的可见元数据范围；同节点以对应普通OM身份核身份、权限和锁定版本；页面显示实际节点与完整版本cut | Web成功、静态节点ID、自报身份或SELECT1不能代替；没有目标库可见权限是明确失败；空表不自动等于库不可见或全部权限通过 |
| 2 分层失败 | 错密码、不可达、TLS失败、可连但无目标库可见范围、OM拒绝分别有固定安全原因；未运行的层明确NOT_RUN，不用空列表、完成态或部分绿色覆盖其他层 | 只读有限适配器覆盖与真实现场结论分开；无管理员/Reader回退、无隐式登录或刷新、无异常原文/URL/秘密回显 |
| 3 防绕过与失效 | 未批准地址、云元数据地址、任意OM URL与重定向拒绝；scope/节点绑定/策略/连接或秘密版本/禁用/OM配置映射凭据epoch变化后，旧正常结果撤回，晚回包不能恢复旧绿 | 不借旧M1窗口、旧UNKNOWN或Provider补种；先以原服务身份核INSPECT_SOURCE，再由Cloud对本次持久诊断授权专用路由的精确scope，不扩大旧Worker四scope及其14项操作；不自动扩展人员权限、清累计、重放或恢复业务任务 |

诊断的运行状态与结果当前性独立。`SUCCEEDED`只表示本次检查执行完成；必须逐层核其结果、覆盖范围、有效期与当前cut，才可说明本次具体结论。任何诊断结果都不等于来源绑定、元数据已登记、业务行可读或可发布。

仅固定的网络/TLS/元数据和原生只读接口可以执行。不得源DML/DDL、读取业务行、写OM、注册来源、生成模型或发布；驱动/节点的一次性部署不替代项目权限。本次新增能力保持原19份WIP及已交付M2.1行为。

真实测试前须独立绑定当前批准节点、现有合法只读源配置、对应OM普通身份与新有限读取窗口。M2.1合成密码不可当工作凭据。专用诊断授权必须来自当前Atlas项目ADMIN、当前连接和所选既有映射的完整cut；请求体或借密结果不能自行建立scope权限。新Web进程的普通会话缓存为空时，诊断应明确拒绝受理；必要的原身份会话准备通过已有Atlas M1维护流程单独执行，不能藏在诊断内登录或刷新。新增敏感权限如确有必要，先完成具体配置差额和复核，再交最小授权决定。

Root独验及精确Git交付仅完成本次M1/M2授权范围，不自动放行M3；M3仍为NOT_GO，真人最终验收仍待完成。

## 本次有限交付结果（2026-10-25北京时间，原准备定义保留）

开发与必要有限验证交Root独验；当前总体仍IN_PROGRESS，Work提交为READY_FOR_ROOT_REVIEW，Root/Git/真人PENDING，M3 NOT_GO。该状态以唯一进度账最新章节为准。

实际入口、证据层级与限制见[交付说明](README.md)。新诊断44579eeb-b23c-40d7-842a-fd594d68a68f（key31c8cd72-dfb6-4b16-ae84-5aa8ff308a78）在新实际节点c6f…上Source四层与普通OM身份通过；目标权限原生conditionalAllow保守失败，版本UI NOT_RUN。20:26:41.596 UTC到期后，同id/key读回明确expired，Native计数不增；不扩大权限补绿、不重放旧b337或UNKNOWN。

新Node/Web正常采用、Native13次有限调用、30源/19保护项/HEAD不变和终末15项活动租约零值，分别见[evidence/runtime-http-status-fix-r1/main-execution-r1/native-final-actual-readback.json](evidence/runtime-http-status-fix-r1/main-execution-r1/native-final-actual-readback.json)、[最终源码运行cut](evidence/main-final-source-runtime-cut-r1/proof.json)及[终末只读门](evidence/runtime-http-status-fix-r1/known-inspection-idle-r1/main-read-final-r1/idle-select-receipt.json)。原读回器和准备脚本错误、启动失败、原停止锁/预算/审计及精确历史UNKNOWN均保留；分时验证不冒最终同cut全矩阵或真实业务验收。

受控回退须先停止新诊断并核无活动，按实际交付提交与当前服务配置处理；不删除DM37历史表、不抹除诊断/UNKNOWN、不重放在途操作。本次未实际回滚，也未推送Git。
