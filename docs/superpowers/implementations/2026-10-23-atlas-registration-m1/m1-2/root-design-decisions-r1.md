# M1.2 采用的技术边界

Root记录于2026-10-22T20:18:56.221781+00:00；属于实现合同决定，唯一进度仍见计划§17，不另记状态账。

- 复用现有受管身份ledger/NativeWorkflow/TaskStore/凭据库/OrdinarySessionBroker，增加精确tenant/space/project/actor/instance目录及实际模型registry resolver；不能用M1.1 LOCAL_PREPARATION伪装万能原生操作。
- 新managed条目一经存在即为该tuple唯一权威；DISABLED/UNKNOWN不回落旧file。新旧同tuple冲突明确拒绝，不并权限。稳定nativeUUID复用只限已有平台受管记录，不能认领同名陌生账号。
- project binding停用只影响本tuple。全局人员credential rotation/native disable必须另具人员管理权限、共享影响和明确确认，不能projectADMIN即改其它项目。v1优先专用单绑定身份核验，范围外保持。
- 允许Worker新增明确只读`CHECK_MODELING_IDENTITY` operation，走真实configured runtime/路由及本人OM客户端；以Atlas持久qualification inspection ID的当前授权/cut/有界用途回调建立单scope admission。不能body自报actor、使用*、修改每日人员配置文件或靠重启冒动态生效。旧静态Principal只保旧操作；M6完整来源/row credential动态消费不提前冒通过。
- 模型Java实际factory必须使用同managed resolver；读取、派发、返回前均核当前scope/epoch。映射保存、consumer已消费版本、当前本人只读身份三个事实分开；本节点不授建模写权，M1.3仍未放行。
- 空grants的现NativeWorkflow仍会建quarantine Policy/Role/Team及ordinaryUser；不得写成“OM零策略对象”。身份专用实例可空controlledTables，不伪造tableId。这些身份管理副作用要列入本节点新有限执行卡；不沿已关闭的M0预算/会话去追加旧流程，不写Glossary/Term/来源或业务行。
- M1.1仅本地实现和隔离验证通过。真实集成须采用明确批准的本地入口/新S6项目与必要部署/迁移先后；不得将测试替身、管理表或并行echo接口冒真实consumer验证。常规技术选择由Root正常判定，未要求重复人工许可。
