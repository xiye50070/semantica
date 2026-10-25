# M3.1 S1/S7 真实发现交接

状态：READY_FOR_ROOT_INDEPENDENT_REVIEW；尚未 ROOT_ACCEPTED、无本节点 Git，真人 PENDING。M3.2 等 Root 独验及 Git 后进入。

两次批准的 Atlas 接入保存和两次受理源扫描均已完成。Atlas 登录 actor=1；每次明确选择 OM 建模账号104/config6、binding9、epoch2，没有管理员 fallback。

| 样本 | 连接 / 发现 | 结构 | 精确选择 |
| --- | --- | --- | --- |
| S1 | 1dd0d13c-5198-4f7e-bc89-1dcb23cc912f / 66a1ea11-12b9-4eaa-a0a0-b6be6f899316 | 28表254列，28PK/26FK/23UNIQUE/25CHECK；25CHECK均UNSUPPORTED | v1 reg_equity_holding；v2持久精确8表：fin_bank_account、fin_customer、fin_invoice、fin_invoice_line、fin_invoice_order_allocation、fin_receipt、fin_receipt_allocation、reg_equity_holding |
| S7 | 90e0dc65-fd95-4b11-b546-36f222059b85 / cd405b8a-1b9d-41d8-89f8-9ba9c924ed98 | 3表13列，3PK/2FK | v1 sales_order；v2 payment、sales_order |

S1真实分页20+8，第二页选择跨页及搜索保留后增量加7张fin表；S7搜索payment增量加入。两者父表分别reg_legal_entity/customer均未自动补选。两来源各1条固定应用DM SELECT独立核SUCCEEDED/FINISHED、完整snapshot/recomputed hash、恰好2版精确集合；均exit0、stderr0，15:12:58.943812至15:12:59.363967UTC。该读回不读取capture_json、源秘密或业务行，不独立证明连接关联/config currentness；关联取原Browser回执。

原操作键、完成/TTL、snapshot与原始执行入口集中在[m31-s1-s7-functional-handoff-r1.json](m31-s1-s7-functional-handoff-r1.json)。S1首个请求明确NOT_ACCEPTED，原历史保留；按可见流程只读准备104会话后手动点击新发现，不自动重发受理操作。

Native仅原受限12次：同管理员refresh/self2、Web startup5、104普通login+SELF4共5；全部200。当前17287，旧17275前缀/旧namespace/业务STOP保持，对象写仍22，扫描后Native0新增。安全只读投影见atlas-real-s1-s7-r5/native12-current-safe.json；Native窗已过期，不再使用。Source准备尾段23读3连接0写成功，累计55读6连接66原写，绝不重做DDL/GRANT或密码。

源发现实现metadata-only双读上限每源14、合计28显式语句；没有独立源wire或全部Atlas HTTP抓包，不能把代码上限写成实际抓包计数。未读取业务行、未登记OM对象、未改M21默认禁用KEEP、未运行诊断或其他Native探针。S7六行业务样本留后继M7。快照过期后保原历史，不重扫续绿。

原最终R9前端gate/tsc/Vite与正常Java包及有限失败历史复用；本轮没有产品源码变化或重复完整build。原2/8及合成/200表层仍原证据层级；本件只前向补齐S1/S7真实栏，不改旧834索引。

非作者已完成原spec/Browser AX/两条实际DM结果同cut核对，并独立重算两snapshot hash；Main亲读完整报告及非重复语义结论。见[同cut核对](../s1-s7-discovery-readback-prepared-r1/same-cut-results-review-r1/review.md)。其未新增SQL/网络/私有读取，不代Root独验。
