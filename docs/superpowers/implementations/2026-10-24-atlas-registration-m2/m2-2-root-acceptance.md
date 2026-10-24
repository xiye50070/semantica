# M2.2 Root 独立技术验收

2026-10-24 20:46 UTC，裁定 **ROOT_ACCEPTED（有限技术验收）**。诊断能力已在批准的真实 Worker 上采用，能够呈现当前源权限与普通 OM 身份，并保守报告条件权限及失效。真人最终验收仍 **PENDING**，不是七层全部通过或生产全模式认证。M3 仍 **NOT_GO**，本次 M1/M2 授权不自动延伸。

## 原三卡与接受依据

| 原卡 | 接受依据 | 保留限制 |
| --- | --- | --- |
| 实际执行位置 | 批准 Worker45025 与 Web45239 分离部署；新诊断44579eeb-b23c-40d7-842a-fd594d68a68f绑定连接b02fd17d-fdbe-4b4a-a83b-47a855d70017、配置/秘密1/1与runtimePin c6f80157…afdf3。实际节点、源网络/TLS、源身份、元数据范围及普通OM身份五层通过，指定库可见2表8列。 | 不是浏览器/Web代探；仅当前账号可见元数据，不代表全部物理范围。NEW_SERVICE计划尚未创建；本次TLS为REQUIRED，不证明其他策略或跨环境证书验证。 |
| 分层失败 | 真实databaseService/Create HTTP200返回conditionalAllow，UI明确OM_PERMISSION_CONDITIONAL/目标权限未通过，版本层NOT_RUN。错误密码、不可达、TLS、不可见目标库、401/403/5xx等按原有限方法分层；5xx误归权限缺陷已最小修复并定向检查、实际采用。 | 不把HTTP200或前五层绿色当整体通过。Native GET虽读到2.0.0-SNAPSHOT，不代替版本层通过；其余负例没有全部现场复演。 |
| 防绕过与失效 | 固定批准地址、节点、scope和单次borrow/current/finish；无任意OM URL、重定向或隐式普通登录回退。源/身份/连接/秘密版本变化与晚回复撤旧绿依已核源码、有限方法及Java producer互通。真实报告到期后原id/key不变，Native仍17263，无新诊断提交。 | 本次到期现场是条件权限失败报告；成功后替换秘密、所有漂移和恶意地址的完整现场矩阵未重复。不得将旧报告当永久资格。 |

Root亲读官方新切换、Native安全原结果、终末idle输出、三份完整AX和两PNG，核当前30源/19保护及Git基线；三名有界非作者分别复核运行与终态、Native通道、原卡与前端，均无新增阻断。另有整M2依赖核对，未重开M1/M2.1。Root未执行产品测试、SQL、HTTP、浏览器或服务操作。

## 当前真实结果

新诊断原key为31c8cd72-dfb6-4b16-ae84-5aa8ff308a78，完成20:21:41.596、到期20:26:41.596 UTC。[当前AX](m2-2/evidence/real-ui-r1/08-r5-real-node-report-current.ax.txt)与[到期AX](m2-2/evidence/real-ui-r1/10-r5-original-report-expired.ax.txt)分别显示“未通过·当前有效”和“已到期，不作为当前通过”；[到期安全回读](m2-2/evidence/real-ui-r1/10-r5-expiry-readback.json)核同id/key、Native状态字节摘要不变。原M2.1禁用的项目引用保持，未暗启用业务取数。

新独立窗口0d8d590d-b7a8-4ea7-a936-9b1c19bb3614为20:18:53.066–20:33:53.066，现已过期。复用同一有效管理pair，仅准备检查，refresh/self/CAS均0；实际当前manager由随后startup最后一次SELF确认。普通身份经既有Atlas只读维护单独准备，双消费者显示当前6/9/2；没有在诊断内部登录、轮换或扩权限。

[Native实际安全回读](m2-2/evidence/runtime-http-status-fix-r1/main-execution-r1/native-final-actual-readback.json)记录17250→17263共13次：startup5GET、普通login1＋warm4GET、诊断3GET。三个诊断GET均200，依次为普通本人、版本、权限；conditionalAllow使检查保守结束，第四个目标GET未执行。末GET17206/auth18/ordinary16/mut57/object22，新增refresh0、OM语义写0。旧17250前缀、全部namespace及UPSTREAM_OR_ACK_UNKNOWN停止锁由Main私有账安全投影核同，Root未重算私有总账。源最多5条固定元数据SQL由适配器约束，未独立逐条抓取源SQL协议；不冒全网或数据库审计。

Worker与Web各一次TERM/Popen，ctime分别1792873190.742332、1792873230.299821；普通参数4/102，新111输入runtimePin采用完成，Java仍原4efe9a8a…包。每次切换前SQLite1＋DM1新鲜门均通过，初始化SQL未量测。20:26:25终末再两只读查询核15项活动/租约/未分类未知全0，指定inspection268a74e3…仍UNKNOWN、global2/qualification1保持。[终末安全回执](m2-2/evidence/runtime-http-status-fix-r1/known-inspection-idle-r1/main-read-final-r1/idle-select-receipt.json)不是联合快照，不删状态或重放检查。

## 修复、验证与证据层级

原两个final Controller使真实Web启动失败；仅去final并加一项真实ProxyFactory/no-store检查，原失败留存。普通R3构建19:20:55.785710–19:21:12.249147、2687输入/20模块offline package exit0，Maven tests skipped；Jar SHA256为4efe9a8adc10be5a967c5ea581313ba7cd7ca53e153734ac1c7757bd1c435be9、178779206B。12 Java/61类族及当前产品cut已核；Gateway未打包部署。后继Worker r5只将403归权限、其他异常归ACK_UNKNOWN，新增502定向方法PASS；两源安装、registry更新及本次真实进程采用分别有据，不以候选代部署。

后台原17个不同方法分时成功，随后代理性新增一方法；原编译失败和H2夹具异步收尾异常保留，修后仅受影响方法已干净收束。Worker原9方法分时/合成IO与后继HTTP分类方法、FE20方法分时及Java合成producer互通均按原层级复用。FE r8完整gate/tsc/Vite exit0，1065输入稳定、旧750kB告警保留。不是最终同cut全矩阵，也不将H2/standalone MVC当部署认证全链。

原DM37只在Atlas应用库2CREATE＋7显式SELECT，非原子DDL；源库无DML/DDL或业务行读取的实现路径，OM无业务写。真实适配器没有SELECT1解锁范围或manager/Reader业务fallback。

Root19:40曾误判borrow走catalog18585；已明确撤回。真实Managed.instance来自atlas.om-identity.endpoint即18587/api，原失败与停机代理502的联系是当时配置和冻结源码推导，未独立捕获原wire状态，不能回填b337 FAILED。所有旧失败、RAW、UNKNOWN、原key、停止锁、累计与一次已完成凭据轮换保留。新增过期inspection仅以精确id/scope/版本/审计保留，其余未知仍阻切换；证据目录冲突和后继r5三个协议标识校准已有实际成功采用，不重写旧attempt。

## 交付与边界

精确范围为Cloud25（含同仓Worker5）及React5，共21产品＋9检查，12旧before均同HEAD、18新路径、与19旧WIP无重叠。[最终cut](m2-2/evidence/main-final-source-runtime-cut-r1/proof.json) SHA256为2352605576c0163425a5004779eb68c4c71c5768fc0bee56782f31f90b9199c2。Git执行结果另见[m2-2-git-delivery.md](m2-2-git-delivery.md)，不能把本报告的技术裁定代替远端确认。

公开只选必要安全原件；完整构建/输入索引、运行argv、SQL行、私有RAW、会话、秘密及JAR留本地，不递归收齐引用。Work ready-r2保提交时状态和hash；Root仅校正acceptance-card中“Git后续M3”的单句授权边界，原提交记录不回填。未进行Git后的重建、重测、回滚或服务变更。后续任何业务都须按当时权限、配置和有效期再判断，当前历史报告不授予权限。
