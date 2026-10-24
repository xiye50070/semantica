# M21 日志修复实际控制只读核对

结论：实际控制与当前源切片一致；不代替 Root 验收或 Main 的两项真实日志回归。

- 真实新 proxy PID3307/ctime1792860900.869379，监听18587；新 Atlas PID3557/ctime1792860940.948173，监听48282，JAR918ff2c1…/178686406。当前进程/正常JarLauncher/独立session-group与实际launch回执相符。
- 原17226条operations完整前缀、所有旧namespace、旧history前缀、原UNKNOWN/pending/writesStopped及UPSTREAM_OR_ACK_UNKNOWN逐项相同；74个旧非state账文件SHA/bytes全部保持。只增加本窗history1条，current17232。
- 新尾部严格manager-self + startup0–4，共6GET/全部200；get只+6，management-auth/mutating/ordinary-login/object/Root/Reviewer/Policy等计数原样。新两个slot完成且无pending/stopped；原始回包只核私有文件mode/owner/size等metadata，没有读取或复制body。
- 同manager当前引用资格的实际保存proof为self1、refresh0、CAS0；session覆盖本窗end+30来自已保存proof，未重新计算live IAM。listening保持auth前opening，auth-proof17227-prefix与当前17232精确衔接。
- 原95435 incarnation已离开；私有old/new argv均100，唯一不同位置4即JAR，当前PID3557 argv与private launch一致。没有导出argv值/新digest或环境；不冒已独立恢复旧进程env。
- 实际累计34 source与新after、19保护文件当前逐SHA/bytes匹配；6组受控进程对应7端口（含Atlas新进程），ctime/owner/listener匹配。未重hash无关整仓、未做Git。

本次只有有界只读与E报告写入：无manager pair/token/key读取，无HTTP/SQL/信号/服务/私有修改/模型或产品测试重跑。Main提供的官方exec完成状态与磁盘实际proof分别记录，本审阅者未冒已亲读那些工具原始stdout。

原进程日志中的synthetic A/B历史和本次两项功能日志回归属于Main，本文不声称日志缺陷已通过真实回归或整个M2.1完成。完整public pin/计数/限制见report.json。
