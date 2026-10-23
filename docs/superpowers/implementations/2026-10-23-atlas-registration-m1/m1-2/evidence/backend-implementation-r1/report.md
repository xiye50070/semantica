# M1.2 后端实现及有限隔离检查

22 个 Java/SQL 源路径已按固定 r3 清单精确安装，逐项读回相同；没有部署、迁移真实数据库或调用 OM。Root 的六个 Worker Python 路径未被改写。

实际管理链复用原身份 ledger、task store、workflow、ordinary broker；公开人员管理与每个影响项目 ADMIN 交集在历史 key、确认、派发和交付前重核。项目停用仅关闭原 tuple，全局轮换预览所有项目并重核。真实模型 factory 使用同一 managed resolver，仅允许本人/版本读取；禁用、UNKNOWN 与同 tuple legacy 冲突封闭回退，已持有 legacy client 每次调用也先核新 managed presence。

新 M1 durable dispatcher 仅选 linked 原 task 的 READY/attempt0/dispatched0，锁内首派竞争，提交后队列丢失可跨 store 恢复；UNKNOWN 不自动重派。真实 legacy resolver 和 MODELING gate 先装再启动相关 dispatcher，M1 disabled 原路径保持。Inspection 一次 borrow、前后 current cut、finish 与 receipt 同事务；失败/丢 ACK 与过期保全 UNKNOWN，已 FINISHED 不倒改。内回调局部异常处理避免 proof 进入全局异常日志，公开严格整数/字段/资源 Id64，operationKey/XRequest 仍按锁定合同128。

原 final r6 20/20 PASS；其后仅 retained-client 必要单例 r7 1/1 PASS，compile 0，输入首末稳定。共21 unique 分时有限场景，不是一次全产品矩阵。r1 原 9PASS/2FAIL、最初编译失败保留。H2 假 remote 与无网络启动顺序测试不代表原生开户、DM8/MySQL、共享部署或业务验收；M1.3 NOT_GO，15项建模写资格仍 NOT_VERIFIED。

原 argv、时间、退出码、stdout/stderr、输入 SHA/size 与历史失败保存在 runs/。产品前态/候选原件在 backend-source-before-r1/r2 与 backend-candidate-r1/r2/r3；不回填旧 cut。最终独立验收由 Root/独审完成，本报告不是自验替代。
