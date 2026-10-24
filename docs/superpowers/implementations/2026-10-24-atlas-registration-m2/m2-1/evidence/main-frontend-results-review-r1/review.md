# M2.1 FE r6 实际结果与源 cut 复核

结论：原件与声称一致，无新差额。只读既有结果；本代理曾为此前端作者，本次结果核对不冒非作者产品验收或 Root 接受。

| 原运行 | 方法执行 | PASS | 历史 FAIL | runner exit |
|---|---:|---:|---:|---:|
| r3 | 17 | 10 | 7 | 1 |
| r4受影响 | 7 | 4 | 3 | 1 |
| r5受影响 | 3 | 3 | 0 | 0 |
| r6晚版本受影响 | 1 | 1 | 0 | 0 |
| 合计 | 28 | 18 | 10 | 分时 |

17个唯一逻辑方法。r6只是原晚GET方法扩充晚ACK子场景并改名，不能重复算第18个唯一方法，也没有最终17方法整套重跑。r2首定向tsc2未执行任何方法，不计28次。原10次失败与r2编译、两次stage缺依赖构建失败原件仍在。

最终 `revision-r6/live-shaped-full-build-r3/result.json`：2026-10-24 15:48:10.176798→15:48:34.541813 UTC，`pnpm run build` exit0。stdout实际包含 production mode gate、完整 `tsc --noEmit` 后的 Vite 成功（8270模块，built11.22s）。1065项 before/after 整 map 相等：1050原current/WIP＋5本功能新增＋10既有静态/legacy构建依赖。运行时 liveBeforeEqual/candidatesStable 均true；13件current候选逐项匹配实际build输入，13件HEAD候选逐项匹配r6 finite输入，物理候选哈希均仍吻合。

stderr精确332bytes/SHA1196a60de022919da82e0daedb872f68c1d7a24f4381df3b7db7ccd9302d6668，与此前M15 full-build-r6原警告完全相同，仅>750KB chunk提示。当前/HEAD map分别5e1855b5…/865a8adb…；完整证据路径、哈希与原失败位置见 review.json。

原 source-map 的 `E_ONLY_FIXED_PENDING_FINITE/testsRun:false` 为固定的运行前快照，未回填；实际运行状态取其后的 actual/result，不能据该标签误判没有运行，也不据此推断目前是否已由Main安装。本次未读取外置巨大执行树、未执行测试/进程/浏览器/HTTP/SQL或改源码，仅新增本报告。
