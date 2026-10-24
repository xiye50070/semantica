# M2.1 Root 独立技术验收

裁定：**ROOT_ACCEPTED（有限技术验收）**。2026-10-24 17:16 UTC。真人验收仍 **PENDING**；M2.2 在本节点精确 Git 交付并核远端后方可进入。本报告不宣告 M2 整体完成。

## 验收范围

依据唯一账 §6 原三卡，交付 Atlas 本地元数据连接生命周期与秘密管理。新 namespace 保留原 Connections/Providers、来源绑定、查询链和无关 WIP。34 个源码路径：Cloud 21（16 产品、5 检查），React 13（8 产品、5 检查）。实际 cut 见 [final-source-and-delivery-cut-r1](m2-1/evidence/final-source-and-delivery-cut-r1.json)，其 SHA256 为 `668c76aa4e6011060389a56cd814e292e38d29dda4b593c45b31d06069195eca`。

| 原卡 | 接受依据 | 证据边界 |
| --- | --- | --- |
| 连接生命周期 | S6 NEW 草稿 0286b1a4-458d-455d-a2bb-389d80f1ac91 创建并刷新；合法隔离 C 项目 EXISTING fed938cd-57c8-4746-a8fd-c3a8b74c4ca2 保存并刷新同 UUID/1、0 版本，保原服务 f3832cc7-18f2-4837-9db9-9e0ac104f52d | EXISTING 是当前 exact-scope 批准本地目录，BOUND_UNVERIFIED，Native 版本为空；保存未建立 OM 对象。重建 Service 对象的恢复有有限方法，真实新 JVM 后页面仍呈现旧项目引用；不冒整套重启后逐对象重测 |
| 秘密管理 | 初填 1/1→KEEP 2/1→明确替换 3/2；取消与完成清输入、回焦；实际 Cipher 对空格/Unicode 与不同 nonce 的布尔核验；运行日志真实漏点已最小修复并以两笔真实命令闭合 | 普通用户/跨 scope/伪 secretRef 与错误路径依真实 Store/Controller 的有限方法；本批匿名两次业务401不冒认证普通用户现场403。秘密消费的实际执行节点接合留 M2.2 |
| 引用与禁用 | 显式项目配置 v1 固定连接 3/2；完整影响 current1/history0/hidden0；取消不推进，确认禁用 4/2并刷新，引用与6操作/6安全审计保留；禁用的新使用 UI 拒绝，后台 resolve 拒绝由有限方法支持 | 不把所属项目等同消费引用；无业务取数、OM写、源DML/DDL或业务行查询。不是全项目/全历史遍历 |

Root 前轮已亲读关键完整 AX 和 PNG、本地终末安全结果；本轮亲读修后创建/替换两完整 AX 与保存 PNG、原日志安全结果、当前拦截器源码、安装回执、完整 Reactor 摘要与包类核对、官方关键运行结果。三名有界非作者分别复核日志/制品与公开精选、实际运行/交付差额、原三卡/前端层级，未发现新的验收阻断。Root 没有接管产品测试、SQL、HTTP、浏览器或服务。

## 日志真实失败与修复

原运行日志命中两个非工作合成密码标记，各1次。根因是独立注册的 MVC 开发日志拦截器在 Controller 前读取并打印 body/query；此前 filter/error/Gateway 有限证据不覆盖这一路径。

最小修复仅为 ApiAccessLogInterceptor 增加敏感 namespace 的前置抑制，保 HandlerMethod 归属和普通路由日志，不改变权限或路由。源 SHA256 为 `ab0832eba538b006f3e78e8998e536570f8e883715b56abed5b9be6773ba5616`。已有测试增加2具名方法，16:42:44–46 compile/test0、2PASS，原7方法不重跑。16:46:25只安装此产品文件及已有测试文件。

后台新包 `918ff2c19848b85a3fecca27c0b980f9ec039f32525bd3bfb8991e42f319c249` /178686406B，2673输入、21覆盖源、20模块离线 package0，Maven tests skipped。原11已核运行类字节保持，新增核定修后拦截器类；Gateway 未打包或部署，保留其有限验证层级。前端 r6 完整 gate/tsc/Vite0，1065输入稳定，旧750kB告警保持。

新 Atlas3557/ctime1792860940.948173 上仅补合成连接397a4e92-bf88-48ad-956b-41e85c985bfa的 CREATE 1/1、REPLACE 2/2，未重演已充分六动作。16:57:48安全日志结果新标记0/0，原日志旧标记1/1保留，日志正文未复制。修复后的原件见 [实际日志回读](m2-1/evidence/runtime-log-repair-real-affected-result-r1.json)；早期 logScanPending、旧 ACTUAL_LOG_LEAK_NOT_ACCEPTED 均保历史，不回填改写。

## 运行与持久化边界

DM36首次11CREATE均返回后因严格 CHECK 表达比较停止，原失败留存；只补2SELECT核等价表达，累计11DDL+7显式SELECT，未重放DDL。三条终末SELECT核本地新M2合成配置、秘密epoch、引用与操作/安全审计；Root仅读安全投影，不读秘密或完整SQL行。

修后新窗 f91eee11-e30b-4be7-a620-2d21fe42bbcd 为16:54:51.723–17:09:51.723，已过期。proxy94871→3307；同管理员当前引用只self1，refresh0/CAS0；Atlas95435→3557，原100参数仅Jar改变，一TERM一Popen，无Bootstrap/自动重试/回滚/额外health HTTP。紧邻idle SELECT1保原M1关键成功/UNKNOWN；JVM初始化SQL未量测。

17226→17232仅6GET且全200，末GET17179/auth16/ordinary14/mut53/object22；旧namespace、UNKNOWN、latch及history前缀保持由Main私有账回读的公开投影支持，Root未重算私有全账。不得复用已过期窗口。两个合成样本新增写仅Atlas本地，未轮换真实凭据、改OM身份/Policy或改变旧事实指针。

## 保留限制与后续

后台14、原日志7+新增2、前端17个不同方法均有分时成功；原编译、fixture、竞态和日志失败保留，不伪称最终同cut整套一次通过。H2/Jdbc真实链与standalone MVC、合成主体/Native响应及owned Axios分层标明，不冒真实认证链完整负例矩阵。浏览器为1512×806桌面代表，不冒窄屏/原生200%/跨浏览器/完整键盘验收。

节点/TLS只是配置；源连通、范围权限、OM诊断、SSRF/重定向防护的实际执行属于M2.2，不能以SELECT1或浏览器/Web节点成功替代。没有进入M3发现登记或建模发布。真人清单保持PENDING。

精确 Git 结果另见 [交付回执](m2-1-git-delivery.md)。必须以 React HEAD-only13映射提交；7重叠路径保旧Spaces WIP，不整投物理文件。公开只交精选证据/报告，私有运行argv、会话、RAW、SQL实际行、JAR/classes/大trace留本地。回退不得将漏点旧包重新用于本namespace；未执行回滚。
