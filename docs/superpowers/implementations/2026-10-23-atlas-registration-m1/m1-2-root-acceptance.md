# M1.2 Root 独立技术验收

记录时间：2026-10-23 14:47 UTC。结论：**ROOT_ACCEPTED，限本节点技术范围；真人最终验收 PENDING。** Git 交付事实另在唯一[里程碑计划](../../plans/2026-10-22-atlas-metadata-registration-milestones.md) §17 前向记录；本结论不冒已提交或已推送。

Root 已亲读关键源码差额、原始结果、实际终态及新补证，非作者完成对应有限复核。依据仍为原计划 §5 的四项 M1.2 判据，未删减或新增判据。实现范围见[59 路径清单](m1-2-delivery-paths-r1.md)，操作证据和人工入口见[验收卡](m1-2-acceptance-card.md)。本次不重跑已经充分且未受影响的检查。

| 原判据 | 独立结论及证据层级 |
| --- | --- |
| Atlas 受管身份及精确 scope 动态接入 | 原任务真实完成，稳定 UUID 保留；配置、绑定、凭据版本分别可追溯。注册表、模型客户端与 Worker 实际消费新映射，未手改人员凭据文件建立映射。必要源码修复部署重启与日常映射动态生效区分记录。 |
| 两个真实只读消费者，不放行模型写 | R14、禁用恢复后及轮换后的各自 cut 均有真实双消费者结果。最终 config4/binding7/credential2、同 UUID，MODEL_CLIENT 与 WORKER 各 VERIFIED；15 项建模写资格仍 NOT_VERIFIED，未因此解锁原生模型写。 |
| 无映射、陌生同名、伪造参数和越界拒绝 | 复用具名后端/Worker/前端隔离反例。补充当前生产类的无映射单方法 1/1：操作者与 scope 合法，实际 Service/Store 拒绝，两个消费者、broker/native 调用及逻辑行写入均为零。陌生同名复用未变的 P1 方法及原通过 XML，不新建真实冲突账号。 |
| 禁用、轮换、旧结果失效与 UNKNOWN 保留 | 真实禁用 config1→2、恢复→3，原生零写；同 UUID 仅一次真人提交的凭据轮换，原任务受控续接成功至 config4/b7/cred2。陈旧 cut、晚回执和 UNKNOWN 不重复开户由具名隔离反例验证。原失败、已确认 ACK 与审计历史保留。 |

## 两项最后补证

- B2：当前真实 Page/API、实际 AntD/owned-confirm 的“取消”和 Escape 两例，2026-10-23 14:20:37–41 UTC，exit0、2/2。默认取消初焦，关闭回原任务资格读取控件、URL/上下文保持，0 POST，原 key、pending 和完整 storage 不变。19 个执行输入和13个产品源绑定一致。原5例没有执行取消的缺口记录不改写。见 `m1-2/evidence/frontend-b2-cancel-escape-current-page-r1/actual-r1/{receipt.json,results.json}`。
- D2：当前生产包的 `authorizedExactScopeUnmappedPersonCannotStartEitherReadonlyConsumer`，14:29:40–41 UTC，compile0/test0、1/1；16 源、106 相关 class、25 输入绑定一致，仅编译测试源并执行该方法。见 `m1-2/evidence/d2-unmapped-consumer-current-r1/actual-r1/{actual.json,test-result.json,production-class-proof.json}`。同名陌生账号方法 `sameNameForeignUserIsConflictBeforeAnyNativeWrite` 的原 XML 为36例零失败、该例唯一通过；当前测试全文及 preflight/optionalUser 与开工 HEAD 相同，可复用原 cut。

## 实际结果与限制

读取放大已在 confirmed-only 最小修复。真实 R14 workflow 为2999 GET＋1普通登录，保留8次 scan 与5段 verifier 生命周期；299.161秒是原生首 reserve 至末 complete，不是全 UI 或纯 OM 耗时。离线同156对象的旧/新对照为管理 GET7013→2968、总 GET7044→2999，不能据此宣称现场百分比提速。

最终真实维护账为16999个完成操作，GET16976、mutating23、管理认证10、普通认证6、password1、logout1，0在途。原60条审计与5个 ACK 保持；轮换旧失败及之后成功单列。一次20模块 package 成功，但使用 `maven.test.skip=true`，不称全套测试通过。启动后进程85027消失的退出码、信号及原因 UNKNOWN；同包普通 main86412恢复，未再运行一次入口或重放轮换。原错误启动计数断言与随后安全读回分别保留。

B2现场键盘、真实 USER/部分拒绝与晚回执排列、B5完整导航抓取、全部历史 OM JWT 撤销及所有进程缓存失效没有现场全覆盖；它们按验收卡明确标注，隔离结果不冒真实或真人通过。原计划允许的具名隔离拒绝/失效判据已满足。本节点不接受 Glossary/Term/Reviewer、模型写、来源接入、业务行、发布或查询功能。

## 交付与后续

M1.2产品及仓内检查为 Cloud44＋React15。前端依赖已独验、尚未提交的 M1.1，因此先单独交付 M1.1 已接受基线（前后端各13路径），再交付本节点；混合文件只提交相应差额，旧空间管理等 WIP 留在工作区。该提交次序不扩产品范围、不重开 M1.1 验收。

Git远端核对完成后，由同一 Work 会话按原序进入 M1.3→M1.4→M1.5；每节点独立验收后继续，整个 M1 通过再进入 M2.1→M2.2。沿用户既有连续授权，不重复索取常规许可，不增加通用恢复/归档流程。只对必要新敏感权限、破坏动作、业务选择或未能解除的审批阻塞给出具体最小决定。

代码回退按本次精确提交逆序处理；不删除原生身份、已确认效果、任务、审计或秘密存储，不将已发生操作改成未发生。真实业务补偿另定范围。最终真人仍待验。
