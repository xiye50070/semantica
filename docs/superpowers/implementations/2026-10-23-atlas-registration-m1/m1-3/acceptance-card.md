# M1.3 独立验收操作卡

依据唯一[计划](../../../plans/2026-10-22-atlas-metadata-registration-milestones.md)§5 M1.3。本卡列操作和判据，不另记节点进度，也不替代真实动作授权。当前实施进度只在计划§17.44及其后续段；源码、隔离样例、真实接口和真人验收分别记录。

入口沿用 `/next/ontology/projects/:projectId/modeling-access` 的“操作授权与写资格”。固定对照复用104/S6及批准实例 `om-p1-isolated`；稳定原生身份保持。执行前以当前完整cut为准，不能把开工config4/b7/cred2当永久资格。M1.2原轮换、维护和两个消费者已充分，不重做。

| 独立操作 | 通过判据 | 必须保留的边界 |
| --- | --- | --- |
| 1. 管理员选择人员/实例，准备有限操作与隔离目标，检查增减差异 | 预览列出原值、完整期望值、增加/撤销、全部共享影响与当前版本；未知目标、无权影响项目、过期或伪造cut拒绝。取消/Escape不提交，保原页上下文及焦点。 | 保存配置、原生已同步、操作实际验证是三个结论；管理通道只处理受管身份、策略、团队和自有范围标签。 |
| 2. 确认同步，再明确准备与确认有限写验证 | 受管策略同步有原意图、实际ACK和独立读回；业务资源请求走真实registry/factory与对应人员OM_USER，逐步耐久记录派发、ACK、读回。原生调用与预览中的精确目标/名称/持久父范围一致。 | 不藏写于旧只读检查；不管理员/reader fallback；不把HTTP200、ViewBasic或旧consumer VERIFIED当写资格。无原生安全粒度或未执行的项按原因保留。 |
| 3. 核正向及拒绝矩阵 | 本人Atlas允许且当前OM操作允许才成功；Atlas拒绝、OM拒绝、仅ViewBasic、范围外目标、普通查询用户五类反例各有具名证据。请求被Atlas提前拒绝时native dispatch为0；原生拒绝不得高权重试。 | 原生根Create仅类型级，精确项目/name由Atlas预留和前后重核；共享目标无法隔离的模式必须拒绝。不能发明UUID谓词或不支持的标签。 |
| 4. 撤销、范围/凭据变化和回执丢失 | 本地新cut先使旧资格失效；在途测试分别覆盖下一派发和解析返回重核。已有ACK保留；同步UNKNOWN只读原任务/键，重复确认不产生第二任务或重复原生效果。 | 不为补测真实重复M1.2轮换。代际变化可用有限隔离场景证明；真实撤销/恢复只在明确隔离范围执行。不能删UNKNOWN、换key、重建身份或扩大策略遮掩失败。 |

15项矩阵须完整显示 TARGET_CREATE、TARGET_READ、REVIEWER_UPDATE、TERM_CREATE、TERM_UPDATE、RELATIONSHIP_TYPE_WRITE、AXIOM_WRITE、CHANGESET_CREATE、LEASE_ACQUIRE、CHANGESET_APPLY、MODEL_VALIDATE、MODEL_READBACK、METADATA_CREATE、METADATA_READ、METADATA_CONSTRAINT_UPDATE。每项记录所选/未选、当前状态、目标范围、时间、完整cut与具名证据；未选不填绿，不支持不改N/A。N/A只限服务端已经证明的所选模型条件。M0有限验证结果是能力依据，不能直接抄成本节点普通身份的VERIFIED。

必要检查限新增编译器及实际安全接合、权限/资格耐久状态机、前端原意图和取消/dirty生命周期，以及受影响真实集成。当前实际实现与有限测试完成后，再做最终源码cut的完整构建和必要浏览器/隔离原生代表；不重跑M0或M1.2全矩阵、不另制大档案。交监督Root独验与精确Git交付后才能M1.4；真人最终验收始终独立。
