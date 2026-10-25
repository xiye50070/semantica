# M3.1 Root 独立技术验收

结论：**ROOT_ACCEPTED_FINITE_TECHNICAL**，2026-10-25 15:18 UTC（北京时间23:18）。M3.1 的可见元数据发现和精确选择通过原操作卡的分层验收。验收时Git待交付；15:30:59 UTC三仓产品与公开验收载荷已commit/push/remote核同，具体提交和后续回执见[m3-1-git-delivery.md](m3-1-git-delivery.md)，回执完成后进入M3.2。真人最终验收仍为 PENDING，不代表整个 M3、M4–M9 或业务全流程已完成。

授权依据为唯一进度账 §17.113 当前 Root 块所记录的真人 M3→M9 连续推进与委托审批指令（官方 userMessage `01a1737f-3eca-7e62-b8af-b044631217c2`）。Root 本次承担非作者验收，未执行产品测试、SQL、HTTP、浏览器操作或服务切换。

## 验收结果

| 原 M3.1 判据 | 实证及判断 |
| --- | --- |
| S1 与 S7 分别发现真实结构 | S1 为28表254列、28 PK/26 FK/23 UNIQUE/25 CHECK；S7 为3表13列、3 PK/2 FK。两次均 SUCCEEDED/FINISHED。非作者独立将全部返回字段、表类型/注释、列顺序/类型/精度/长度/默认值/可空/注释、约束名称与有序列、FK逐对目标及CHECK原文与既有公开物理元数据副本逐项比较，无差异。六份物理副本 pin 一致；未为复核追加源查询。 |
| CHECK支持边界 | S1全部25条CHECK在持久快照中均标为UNSUPPORTED，页面明确“不支持登记”。没有以源结构存在CHECK冒作已登记或受支持。 |
| 分页、搜索、跨页选择及精确集合 | S1真实20+8分页：第二页单选reg_equity_holding，搜索fin_后增量加入全部7张匹配表，v2精确8表；S7先选sales_order，再搜索payment增量加入，v2精确2表。Root亲读完整AX、两幅实际截图和两份存储回读；版本、名单及结构hash一致。原S4隔离200表、本页/全部匹配/取消/上限拒绝等有限证据复用，不冒真实源200表验收。 |
| 可见范围、缺项与未选外键 | 页面和快照保持CONFIGURED_DATABASES_VISIBLE_TO_CURRENT_ACCOUNT_ONLY、allPhysicalMetadataVerified=false；预期m31_expected_absent报告缺项。S1两条reg_equity_holding→reg_legal_entity、S7 sales_order→customer仍列为未选依赖，不自动补选。范围外名称隐藏沿用既有有限验证。 |
| 当前性、失效与漂移 | 复用已审后台/Worker有限检查及前端r8/r9受影响检查；连接/目标/选择变化、配置版本、失败后的旧可用状态及源双读漂移拒绝等按各自通过cut记录。没有在真实S1/S7注入DDL漂移或撤权，也不以当前成功替代这些有限证据。 |

## 两个真实样本

S1连接 `1dd0d13c-5198-4f7e-bc89-1dcb23cc912f`，发现 `66a1ea11-12b9-4eaa-a0a0-b6be6f899316`，原键 `885328f3-1c44-4c1d-b01f-7ae4d9fc3716`，15:03:22.766 UTC完成、15:18:22.889660到期。结构hash为 `sha256:d0c218b147804d332f5ddf9c396405aa370b563c22f65ddf2f81bfcbd027d75c`。v1只有reg_equity_holding；v2为fin_bank_account、fin_customer、fin_invoice、fin_invoice_line、fin_invoice_order_allocation、fin_receipt、fin_receipt_allocation及原reg_equity_holding。

S7连接 `90e0dc65-fd95-4b11-b546-36f222059b85`，发现 `cd405b8a-1b9d-41d8-89f8-9ba9c924ed98`，原键 `97e6152d-2e54-4f91-8e24-3726166242ef`，15:12:02.085 UTC完成、15:27:02.142638到期。结构hash为 `sha256:b734e7d3a22014f7ee2586e2fdd27cc705bed5817f007597cde19f754af2860c`。v1只有sales_order，v2为payment及sales_order。

两来源各一次固定应用DM SELECT于15:12:58–59 UTC完成，均exit0、stderr空，均在快照TTL内；完整snapshot的databases规范JSON hash独立复算一致，selection原JSON与解析集合一致，历史恰好两版，无重复。该回读未读取capture_json、源凭据或业务行，未调用Source/Native；连接关联来自浏览器请求与回执，configurationCurrentnessEvaluated=false，不把DM回读单独当当前授权证明。到期后保历史，后继须正常获取有效计划/资格，不能重扫续绿或重用过期窗。

Atlas请求actor=1；OM建模选择为104/config6、binding9/epoch2。Root结合本次普通登录与四次SELF成功、原浏览器上下文核定该消费链，未将Atlas管理员actor冒成普通OM主体，也未以管理身份fallback。首个S1未受理尝试保留；明确准备普通会话后才手动启动新发现，未自动重放已受理操作。

## 运行、范围和复用证据

- Source隔离前置66笔写ACK及两个只读凭据保留。修正后23条只读尾段完成，累计55读/6连接/66原写ACK，旧r1/r2 UNKNOWN及r3本地CHECK停点均保；尾段无新写和密码生成。该前置准备不等于M3发现中的源写权限。
- 本次Native同账保原17275条前缀，新增12调用全部200：refresh/self2、Web startup5、普通104 login/SELF4共5；末17287，对象写仍22，原namespace/业务停止闩保。Source发现阶段Native新增0。Native a2f0窗口和global b759窗口均有独立截止，不作为后继许可。
- 两配置CAS成功，Web仅一次正常重载；Worker未重启，正常Jar `4a7b7346…`、Web runtime pin `e8ddc067…`。应用启动初始化SQL未量测，不声称生命周期零SQL。
- 后端、Worker、前端有限正负例及全部失败历史复用；r9完整production gate/tsc/Vite通过，Java正常20模块package通过，skiptests不称测试通过。完整判据、构建与运行对应见M3.1 README及原§17.114–116；未重复全矩阵。
- 本次Git只允许已注册23路径（19产品/4检查，5旧/18新）：Cloud17、React6。独立只读复核确认当前23文件与最终r3 manifest逐字一致；5旧HEAD与current-before相同；Cloud3/React16旧WIP保护字节保持，staged为空，范围外untracked全部排除。

源发现只读固定metadata双读，上限每源14、两源28条显式SQL；无独立Source wire或全部Atlas HTTP抓包，不能把代码上限报作实测wire。快照只覆盖返回字段，不证明全DDL、全局事务一致性或持续无漂移。TLS已证加密及1.2/1.3，未在此验收CA/hostname认证。源业务行读取0、OM对象登记0；S7六行业务试跑留M7，M21默认禁用KEEP未改变。

## 精选证据入口

- [本次S1/S7交接](evidence/s1-s7-approved-execution-r1/m31-s1-s7-functional-handoff-r1.md)及同名JSON，保存两来源原ID、时间、hash、精确选择和回执入口。
- [S1存储回读](evidence/s1-s7-discovery-readback-prepared-r1/s1-readback-actual-r1/source-selection-readback.json)、[S7存储回读](evidence/s1-s7-discovery-readback-prepared-r1/s7-readback-actual-r1/source-selection-readback.json)。Root读回sha分别为3b917b7e…/269627B、817ac82d…/15961B。
- [本次同cut非作者报告](evidence/s1-s7-discovery-readback-prepared-r1/same-cut-results-review-r1/review.md)、[实际页面和Native安全投影目录](evidence/s1-s7-approved-execution-r1/atlas-real-s1-s7-r5)。Root另有独立代理完成全部物理字段/有序关系比较及Git范围核对，均已收束。
- [精确源码范围](evidence/scope-registry-r1/scope.json)、[最终23路径manifest](evidence/source-install-r3/manifest.json)、[原充分证据导航](README.md)。公开交付仅精选安全证据，不递归收集全部引用，不含秘密、RAW会话、完整运行argv/SQL、classes/JAR或大trace。

Git完成状态与M3.2放行另见本节点Git交付回执和唯一进度账最新Root块。本报告不冒真人USER_ACCEPTED。
