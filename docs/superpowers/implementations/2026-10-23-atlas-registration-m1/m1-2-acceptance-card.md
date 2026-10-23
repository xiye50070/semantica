# M1.2 独立操作验收卡

依据唯一[里程碑计划](../../plans/2026-10-22-atlas-metadata-registration-milestones.md) §5 M1.2。本卡是独立操作与留证清单，不是第二进度账，也不授予执行权限；节点状态仍只由唯一计划记录。**本卡真实结果由 Root 按实际前向填写；真人验收仍为 PENDING。** 源码、隔离样例、真实接口、真人操作分别记录，不能互相替代。

范围限于受管建模身份、精确项目映射和真实消费者的只读身份消费。页面持续显示“建模写资格未通过”，15 项建模操作资格全部 `NOT_VERIFIED`。M1.3、Glossary/Term/Reviewer、原生模型写、业务行、来源接入、发布和查询未放行。

## 执行入口与固定对照

使用 Atlas 当前合法登录会话，从 S6 项目准备页“身份与操作资格”进入“建模权限”；页面路由为 `/next/ontology/projects/:projectId/modeling-access`。不读取或导出浏览器 Token，不编辑身份/provider/Worker 文件，不要求用户手填原生 UUID 或 credentialRef。日常映射应通过实际注册表与消费者动态生效，不靠重启建立。

以下是公开原件中的对照身份，不是本卡重新核实的当前状态；开始时由 Root 绑定实际前端/后台/Worker 版本、当前窗口和剩余预算：

| 对照项 | 原意图固定值 |
| --- | --- |
| tenant / account / 批准实例 | `1` / `104`（测试号，原普通人员）/ `om-p1-isolated` |
| S6 项目 | `3f10ce21-57fe-4efe-91c6-c9a3e4cf04f6` |
| 空间 | `1447855d-bf9e-41d7-a9b3-ed641290d6a7` |
| 原 Atlas 身份配置任务/意图 | `5e974f3c-1957-46aa-8ed0-ee70581be955` |
| 原生身份任务 | `81521ba3-4dbe-4db9-93e1-fa97a9acedc2` |
| 原 binding / 普通用户 UUID | `081dc4a9-a5a4-40e4-9141-634eda235409` / `2d0540a5-d6fd-4f24-a5a4-270eaa54dc26` |
| 原目标资料 | 显示名“测试号”；邮箱 `111@qq.com`；原生名称从原任务只读展示，不编辑 |
| 必须保留 | 原 operation key、Policy/Role/Team/User 四个创建 ACK、原 UNKNOWN/派发历史、原资料步骤的 credential epoch `1`及空 grants；后续本人授权维护已推进至 epoch `2`，原记录保留 |

原任务已产生四个对象 ACK，因此原资料续接不得再次执行开户、通用 PROVISION或生成替代 task/binding/key。2026-10-23另获真人明确批准的同UUID维护轮换，按本文末的独立维护记录执行，不重放原资料动作。仅在 Root 另行绑定的当前技术执行卡、完整 NOT_FORWARDED 证书和服务端资格都有效时，允许同原用户资料步骤一次。此卡不证明资格已具备，也不代替服务端证书。

## 一、人员、实例和范围（原计划操作卡 1）

| 编号 | 操作 | 独立判据与留证 | 真实结果 | 真人验收 |
| --- | --- | --- | --- | --- |
| A1 | 在 S6 选择人员104及批准实例 | 当前操作者具备人员管理权限与本项目 ADMIN 的交集；目录与页面均绑定 tenant/space/project/person/instance。没有任意 actor/scope/credentialRef 输入，实例不暴露 origin 或秘密路径 | 本轮真实：S6/104/批准实例及当前管理交集通过；见R14页面与3项prepare回执。USER路径另按D1隔离证据，不混为真实。 | PENDING |
| A2 | 读取原任务与受管身份 | 点“核对原任务”，仍为原 Atlas 意图、原生任务、binding 和普通用户 UUID；分别显示映射保存、同步状态、消费者检查。不能用配置保存成功推导原生效果或只读消费者通过 | 真实通过：同原意图/key/任务/UUID，SUCCEEDED、MAPPED/IN_SYNC；r14-terminal-task.json。 | PENDING |
| A3 | 核对精确映射与版本 | 配置版本、bindingVersion、credentialEpoch 来自当前服务端读回；写入使用当前 mapping CAS，陈旧 cut 拒绝。资料确认导致的合法 bindingVersion 前进单独记录，不能把它误写成凭据轮换。原资料阶段 credentialEpoch 为1；本人授权维护轮换后为2，grants仍为空 | 原资料真实通过：config1/b5/cred1；维护后当前config4/b7/cred2/grant0，同UUID。终末73审计SELECT与新consumer cut一致，旧cut不改写。 | PENDING |
| A4 | 核对现有对象及身份属性 | 同原用户 UUID，非 admin、非 bot、非 deleted；直接 Team/Role 成员与四个创建 ACK 保持，原开户/资料动作不重复创建对象、认领账号、扩权或重建凭据；另获本人批准的同UUID维护轮换单列。显示名/email按原任务目标核对 | 本轮真实：同UUID、普通属性非admin/bot/deleted，5ACK/对象5/profile1保持；原50审计hash完整相等。 | PENDING |

## 二、原任务资料补齐与默认取消

| 编号 | 操作 | 独立判据与留证 | 真实结果 | 真人验收 |
| --- | --- | --- | --- | --- |
| B1 | 点击“核对原资料续接资格” | 是显式资格 GET；展示原任务、人员、项目、实例、原生名称/email、当前版本、当前→目标显示名。不可用时写明原因，只能只读核对；不定时轮询原生资料，不把新鲜 GET 当作“从未执行”的证明 | 既有真实GET：profile-context-r11-actual-r1，原task/scope/UUID及0.1/null→测试号。未扩大为完整历史无轮询证明。 | PENDING |
| B2 | 打开“继续原资料步骤”确认后先取消 | 确认说明只补齐同一身份、不开户/轮换/扩权；默认焦点在“取消”。键盘 Escape 或取消关闭后返回原任务上下文；取消不提交续接 POST、不建立许可、不产生新 key/对象或认证 | 现场实际仍PENDING；已新增当前真实Page/API的jsdom参数化点击取消/Escape两例2/2 PASS：取消初焦→关闭→回原任务资格读取控件/同上下文，0POST，pending/key/完整storage及4历史markers保持。原R14其它确认不替代本项，原5例也不冒取消通过。详末节B2补证。 | PENDING |
| B3 | 当前资格仍有效时，由原操作者明确确认一次 | 只提交服务端当前 contextHash 和说明，不提交 profile、UUID、证书、absence 或原生 payload。Web Locks 与非秘密持久 marker 在 POST 前保全；无锁/保存失败/旧会话不派发。只新增原任务专用一次许可/claim/epoch fence，不复跑通用开户 | 既有真实单POST/200、contextHash精确匹配GET、REQUESTED/replayed=false；WebLocks/保存失败/旧session仅5/5隔离证据。 | PENDING |
| B4 | 核对确认后的同一资料步骤 | 原资料 DISPATCH 与 UNKNOWN 保留，新发送/ACK 另有因果。仅同 owned 用户 email/displayName 最小 CAS；原四个 create 不重发，原 credential epoch1、不轮换、不新 secret/grant。最终资料快照与原目标一致、普通属性/成员保持；真实结论由 Root retained ACK 和独立终末读回填写 | 真实profile ACK与同UUID/hash/cred1保持；20→27及原50前缀已核，今60终末SUCCEEDED/e6/b5/grant0前向收口。原MANUAL失败不改。 | PENDING |
| B5 | 离开/重开、ACK未知或重复点击 | 原 marker、task、key、已发效果保留；入口改为“核对资料续接结果”/“核对原任务”，只 GET 原结果。没有新 key、重复资料 PATCH、自动重试、第二账户或重复许可。REQUESTED/CLAIMED/STOPPED/COMPLETED 分别呈现，不把离开页面当取消后台任务 | 已采段单POST后READ_ORIGINAL、同key/task；原navigationCaptureComplete=false。丢ACK/重开/重复点击和完整phase仅隔离，不补写真实通过。 | PENDING |

B2 是正常默认取消验收；B3/B4 是一次有明确当前执行卡的独立动作。若实际资格、证书、版本、窗口或预算不足，停在 B1/B2，B3/B4 继续 PENDING，不为完成本卡另开写操作。

## 三、两个真实消费者（原计划操作卡 2）

| 编号 | 操作 | 独立判据与留证 | 真实结果 | 真人验收 |
| --- | --- | --- | --- | --- |
| C1 | 身份映射和同步具备条件后点击“只读检查” | 请求严格 `mode=READ_ONLY`、`capabilities=[MODEL_READBACK]`，取当前 person/instance/exactscope/配置/binding/credential cut；不开户、轮换或改策略。只读检查可有原普通认证成本，不能把原4次 native GET 冒作整个请求的全部成本 | 真实通过：一次POST，READ_ONLY/[MODEL_READBACK]；4 native GET另1 ordinary login，无资源写。 | PENDING |
| C2 | 核对模型客户端与 Worker 两条结果 | 经真实 `OmInstanceRegistry`/模型客户端与实际 Worker OmClient 消费同一普通身份，按原有限卡记录4次 native GET，不另增模型资源/业务行请求。分别显示当前配置版本、bindingVersion、credentialEpoch、UUID、时间和有限原因；不能只回显数据库或借旧启动 scope/管理身份代查 | 真实通过：原config1/b5/cred1按原cut保留；本人轮换后的新inspection1e08ad2b中MODEL_CLIENT 13:44:35.487551Z、WORKER 13:44:35.923311Z均VERIFIED，config4/b7/cred2/同UUID。 | PENDING |
| C3 | 核对部分失败与晚到结果 | 两条结果独立：一条 VERIFIED 不覆盖另一条 DENIED/UNAVAILABLE/UNKNOWN。账号、实例、项目、会话或任一版本变化即不采用旧结果；401/403 撤下受影响缓存数据和动作，保留原任务记录 | 隔离反例已验；本轮真实只覆盖两条成功，失权/版本漂移/晚回执现场不补写通过。 | PENDING |
| C4 | 核对权限边界 | 两个消费者即使均 VERIFIED，也只证明当前身份消费；页面仍“建模写资格未通过”，15项均NOT_VERIFIED，没有模型写/目标就绪/来源读取/发布入口被解锁，无 admin/reader fallback | 真实通过：返回15项NOT_VERIFIED，页面仍“建模写资格未通过”；未解锁写。 | PENDING |

## 四、拒绝、维护和人工边界（原计划操作卡 3、4）

| 编号 | 操作或复用隔离样例 | 独立判据与留证 | 真实结果 | 真人验收 |
| --- | --- | --- | --- | --- |
| D1 | 普通 USER 打开自身范围 | 仅本人映射和只读检查；没有人员目录、管理 Project GET、配置/轮换/禁用/资料续接管理动作；跨人员或项目由服务端拒绝，不能靠隐藏按钮建立授权 | 复用后端r6/r7与前端r1/r2有限反例；本轮未切换真实USER会话，真实PENDING。 | PENDING |
| D2 | 无映射、同名陌生账号、伪造 scope/actor/credentialRef、越界项目 | 拒绝、不认领既有账号、不换 UUID、不回显 Token/密码/秘密引用。仅用既有隔离反例或 Root 另定的有限真实反例，不在本卡临时创建冲突账号 | 具名隔离：无映射新H2单例1/1通过（当前service.check→MODELING_VERSION_CHANGED；双consumer/broker/native/写均0）；同名陌生PROVISION复用sameNameForeignUserIsConflictBeforeAnyNativeWrite旧36/36 XML，当前相关方法未变，不认领/不分配凭据。详末节D2引用；无新增真实冲突账号，其他具名边界依原r6/r7，现场仍PENDING。 | PENDING |
| D3 | 预览“禁用当前映射/撤销当前映射”和“轮换凭据”，默认取消 | 仅原任务完成且当前允许 PREPARE_NEW 时进入；UNKNOWN 不为验此项绕过原任务。前两项仅当前精确 tuple，不删除全局用户/不影响其他项目；轮换列出全部共享影响 scope 并核交集权限。无独立写卡时只预览取消，不借资料补齐执行维护写 | R14三项仅prepare/取消；新直接授权后实际禁用确认默认焦点为取消，Enter取消后关闭并返回“确认配置”，0提交；随后另一次明确确认才禁用。轮换最终确认已由真人12:54:10.920Z提交；后续仅沿原意图续接。 | PENDING |
| D4 | 复核禁用/轮换后的失效语义 | 旧配置、凭据 epoch 和晚到回执失效；同 task 不重复开户。实际维护写未授权或未完成时保持PENDING，可引用当前隔离回归但不填真实通过；同名冲突/失权/ACK未知均不自行换人或重新开户 | 有限真实通过：禁用config2→恢复config3，0 native且旧consumer撤下；恢复后的config3双consumer与轮换后的config4/b7/cred2双consumer均实际VERIFIED。同UUID仅1次原生改密，验证会话logout1；陈旧cut/晚回执拒绝仅隔离证据，全部历史原生JWT撤销及所有缓存失效未声称现场通过。 | PENDING |
| D5 | 窗口过期、预算不足、资格漂移或新异常 | 停止后续派发，原 counters/窗口/denials/UNKNOWN/ACK 永久保留；不清账、改旧窗口、自动续期、刷新/Basic/PAT fallback或重放。延期必须是具名的新技术决定与完整前态，不能把剩余预算当执行许可 | 真实旧过期/失败/UNKNOWN/ACK/计数保留，R14单episode及consumer消费后无重放；无续窗。 | PENDING |
| D6 | 判断需要人工处理 | 不符合一次资料续接的严格原 task/hash/epoch/证书/当前权限条件时只读原结果；保留 STOPPED/UNKNOWN/REQUIRES_OPERATOR，说明实际缺口。不能把通用“重试”或新许可当修复 | 真实边界保留：旧UNKNOWN历史不删除，当前成功只闭原task；本次维护写已有限验证，真人整节点验收与后续M1.3仍未通过。 | PENDING |

## 页面与留证要求

沿已选 Aham8 pc light / 当前 React19、AntD6。只核本页：中文业务文案、状态有文字、系统 UUID/版本只读、唯一主操作、取消初焦/关闭返焦、Tab可达、普通控件32px与必要窄屏44px、长说明及页脚不遮末字段。真实像素/键盘由实际截图与操作记录判断，组件调用 focus 或旧图不能代本次真人验收；未测暗色/其他视口不补写通过。

每组由 Root 填实际版本/窗口、命令或 Atlas 操作、原始证据链接、开始结束时间、请求/认证/对象计数、结果与未验项。公共卡只放安全元数据；不复制 token、密码、凭据/内部文件路径、完整原生响应或私有原件。只读消费者的普通认证与全部隐藏 HTTP 计入原有限账。

可复用材料仅用于定位与限界：[v1后台合同及后续澄清](m1-2/backend-contract-r1.md)、[前端有限隔离报告](m1-2/evidence/frontend-final-r1/report.md)、[原任务资料续接合同](m1-2/evidence/frontend-original-profile-continuation-candidate-r1/backend-api-contract.snapshot.md)、[资料页候选的5项隔离样例说明](m1-2/evidence/frontend-original-profile-continuation-candidate-r1/README.md)。这些原件含各自时点的候选/安装状态与失败历史；不能因链接存在就认定当前真实资料或消费者已通过。

代码回退仅回退应用源码/包和入口，不撤销已创建原生身份、四个对象 ACK、本地映射或已发资料效果；不删除任务/账本/秘密存储，不把已提交业务状态恢复成“从未执行”。如需业务补偿，应另定范围与执行卡，旧状态和证据保留。

## Root 实际填写区

以下为当前事实汇总；原R14、本人提交前及失败截面继续按原时点保留于后文。此区不代替监督Root的节点独立验收。

- 实际版本：前端沿08:35完整build后的同2源，本轮无前端改动；后台完整20模块构建JAR `d7e21d37…/178273113B`，仅3个产品Java源变化。当前普通main PID86412/create1792762920.00625、PGID=SID=86412，未携带一次Bootstrap/Runner；Worker83657、OM57991、System95809、Vite3832和代理84513均保持。
- 当前范围：本人12:54:10.920Z提交的同轮换意图57fd73b6、nativeTask91169272、原key166ecee0及原UUID；原D4截止不改。必要技术续接单独固定13:32:41.858864–14:17:41.858864Z，不清账或重开confirm；仅同M1管理员refresh+self一次、同步原M1两引用，没有M0/shared写。最终16999操作/GET16976/auth10/ordinary6/mut23/object6/profile1/password1/logout1，全部完成，0inflight，4denial与旧16962对象前缀保持。
- 原身份任务及历史：原81521任务SUCCEEDED/a6e6/desired1，60审计及5ACK完整hash `dd894417…`保持；当前binding合法前进到b7/cred2，不将原任务的当前关联binding冒作旧快照。
- 本次维护轮换：同UUID，SUCCEEDED/a2e2/desired2、BOUND/IN_SYNC、config4/b7/cred2、grant0、无lease/retry/cancel。13条轮换审计含原e1 MANUAL/PREFLIGHT_BLOCKED与新e2成功，hash `5e225202…`；单SELECT共73行且0DML/DDL/nativeHTTP。该reader顶层旧名含FAILURE只是诊断器标签，不覆盖tuple成功。原4行前缀在续接领取事务中锁内核验，终末reader未另输出其单独prefix hash，不冒有此额外证明。
- MODEL_CLIENT / WORKER：新inspection `1e08ad2b-ea87-4acd-ba20-3b3ce1490268`，13:44:35.487551Z/13:44:35.923311Z均VERIFIED，精确config4/b7/cred2/同UUID；一次只读检查新增4nativeGET+1普通login，没有资源写。15建模写资格仍NOT_VERIFIED。
- 技术交付建议：当前具备提交Root独验的有限证据，`READY_FOR_ROOT_REVIEW`仅为提交状态；唯一账M1.2仍IN_PROGRESS，未ROOT_ACCEPTED/未Git交付，M1.3+ NOT_GO。
- 明确保留：B2原profile确认的Cancel/Escape、初焦与返焦未现场补验；B5全导航捕获不完整；D1真实USER、冲突/失权/陈旧cut/晚回执现场反例没有新增，只复用具名隔离证据。旧原生JWT全部失效、同进程所有缓存失效均NOT_ASSERTED；验证会话logout不代所有会话撤销。普通映射曾已动态生效，此次为部署必要修复而重启，不表示每次新增映射必须重启。
- 运行限制：一次Bootstrap进程85027成功执行后消失，实际退出码/信号/因果UNKNOWN；无异常或shutdown标记不能推出没有问题。后续仅启动同JAR普通main86412，不重放任务。首普通main命令因错误期望“启动0 GET”exit1；真实5个必要安全GET/0写及之后独立存活读回另留证，不涂改该失败。
- 真人最终验收：PENDING。未授模型写权，未进入目标/reviewer/来源/发布/查询验证。

### 2026-10-23 R14 实际原件与待决维护范围

E为本目录`m1-2/evidence`：成功任务与一次消费者在`root-ui-native-intent-actual-r1/r14-terminal-task.json`、`r14-consumer-request.json`、`r14-consumer-result.json`；三项预览/取消在同目录`r14-maintenance-preview-network.json`和`r14-maintenance-plan-*`，只有3个prepare POST、没有confirm。预览会写本地临时plan及PREPARE审计，0原生调用；不是全系统零写。11:37:59Z代理读回仍16956操作全部HTTP_COMPLETED、4denial未变。

性能原件`reading-amplification-diagnosis-r1/live-r14-independent-r1/report.md/json`：全链8扫描，每扫描8页+312明细GET；两主观察710GET/68.243秒与810GET/77.253秒。Verifier保留必要4扫描，不能将整个workflow写成只有2扫描。Transport计时含代理，不冒纯OM耗时。

终末审计`first-live-validation-r14/terminal-audit-reader-r2/actual-r1/safe-projection.json`：11:38:32.180187–32.549824Z单SELECT，0DDL/DML/nativeHTTP，原payload仅RAM；原50完整不变，新增10为REQUEST/CLAIM/VERIFY/4RENEW/VERIFICATION_OBSERVATION/FINISH。初reader漏列产品本来写入的VERIFICATION_OBSERVATION，原r1失败与源码保持，r2仅补该类型严格同UUID/e6/普通属性检查后成功；没有重复任何业务操作。

**待明确的维护验收，仅限104/S6/om-p1-isolated当前映射和同UUID：** 先在Atlas禁用本项目映射一次，验证旧配置与晚回执被拒；再按当前CAS恢复本项目同一身份映射，不重建账号。随后同UUID受控轮换一次，核旧credential epoch/会话失效及新epoch的模型客户端与Worker分别消费；原任务/ACK/UNKNOWN全部保留，不扩权限、不碰其它账号或模型数据。当前轮换预览只列S6项目；执行前必须新鲜核对影响集合，若多出项目或身份/权限变化即停。R14没有此写许可，不在本窗口执行、不自动重开窗口；需单独明确维护动作授权，凭据改变的浏览器最终提交须真人操作。

自动审批拒绝记录：本轮尝试点击预览页“确认配置”（源码为打开确认框）被判定为实际禁用提交，理由是授权只覆盖预览/取消且R14禁止维护写。该分支未重试或改工具绕过；页面仍预览态，已取消。未到达确认框，不能引用源码autoFocus代替真实默认取消通过。

B组有限沿用依据：`root-ui-native-intent-actual-r1/profile-context-r11-actual-r1.json`、`profile-r11-receipt-r1.json`、`profile-r11-context-after-r1.json`、`profile-r11-ui-safe-network-r1.json`（本段POSTCount1/nontruncated，但navigationCaptureComplete=false）；`independent-design-r1/profile-confirmed-manual-r8-source-review-r1/review.md`。已完成的同资料步骤不再为补默认取消截图而重建资格或重发；未覆盖部分明确保留。


### 2026-10-23 新直接授权后的本地维护实际结果

真人直接“允许维护验收，轮换提交时交给我（推荐）”只覆盖104/S6/om-p1-isolated及同UUID。原R14禁止维护写和此前审批拒绝保历史，不将新授权倒填旧窗口。

- 默认取消实际：禁用确认弹窗的取消初焦；Enter取消，随后确认框不可见且焦点返回“确认配置”。原件 `root-ui-native-intent-actual-r1/d4-disable-cancel-focus.json` 与 `d4-disable-default-cancel.png`。
- 实际禁用本地意图 `0c7d3774-1ed8-4fa9-b803-5e422202b0fd`，SUCCEEDED/config2/DISABLED；恢复本地意图 `1de68927-3df5-4dad-a013-34f958af03be`，SUCCEEDED/config3/MAPPED。恢复POST3855.1302 HTTP200/code0，status3855.1305读回；均无native task，原UUID/b5/cred1及原任务保留。原结果撤下后未因恢复复活。
- 两操作native计数零增长，仍16956操作/GET16939/auth8/ordinary4/mut17/object5/profile1/password0/logout0。禁用时Network域未启用，`d4-disabled-network.json`空记录如实保留，不冒已捕获其POST；禁用config2的后续服务端读回、恢复POST/status实际捕获。见 `d4-local-maintenance-safe-projection.json`、`d4-restore-network.json`及对应response原件。
- R14成功链不重跑；本地禁用不等于撤销原生JWT，恢复可能复用原broker。现场主动提交旧cut或制造晚回执未做，只复用原隔离拒绝证据。轮换仍未提交、凭据仍1，15写资格不变。


### 2026-10-23 D4 当前人工提交前截面

config3恢复后的唯一consumer POST3855.1405 HTTP200/code0：MODEL_CLIENT 12:18:10.819248Z、WORKER 12:18:11.260875Z均VERIFIED（inspection fad1db0a），b5/cred1/同UUID。新增4nativeGET、0login；旧结果撤下后这是新检查，非回显config1旧结果。全部15写资格仍NOT_VERIFIED。

D4同账维护窗12:13:07.161015–12:58:07.161015Z，不延长R14。原M1同管理员一次refresh+self已完成；当前16962操作/GET16944/auth9/ordinary4/mut18/object5/profile1/password0/logout0，全部完成，无stop，旧16956前缀保留。轮换预览plan a83b64ff-7ecd-4519-8683-095b31b445af，S6/同UUID/cut3/5/1，20:33:34北京时间到期；已停在“轮换凭据？”弹窗，取消初焦。Root未点“确认轮换凭据”，最终本人提交。

轮换成功、新epoch双consumer、单终末审计及整M1.2独验/Git仍待；不把本次人工交接冒已轮换。原件 `root-ui-native-intent-actual-r1/d4-restored-consumer-result.json`、`d4-rotation-plan.json`、`d4-rotation-human-handoff.png` 与 `root-d4-maintenance-bound-r2/current-human-handoff-safe-projection.json`。


### 2026-10-23 本人实际提交后的失败截面

真人12:54:10.920Z提交原轮换intent57fd73b6 / key166ecee0 / plan26c19918；原status3855.1455返回REQUIRES_OPERATOR/MANUAL，映射config4/b6/cred2 UNKNOWN/FAILED，旧consumer已撤下。单SELECT解析真正nativeTask91169272-66af-4cb5-b1e4-f185a08dc3d7，PREFLIGHT_BLOCKED/dispatched0/a1e1/desired2，无lease/retry；新增4审计仅intent×2/claim/finish，0native-step。原60完整审计/5ACK保持，native仍16962/password0；不能把本地cred2当原生密码已改变，当前映射不可用。

已定位legacy Journal仅准SYSTEM_USER_ADMIN与MODELING入口合同不兼容。修复/隔离回归/同原task技术方案准备中，未真实重派；D4已过期不延长，不新confirm/key。D4轮换、新epoch消费者与整M1.2验收继续PENDING；原禁用恢复及config3双consumer成功按原cut保留，不复跑。原件 `d4-rotation-failure-diagnostic-reader-r1/root-actual-r1/safe-projection.json` 与 `root-ui-native-intent-actual-r1/d4-submitted-original-status.json`。


### 2026-10-23 本人原轮换成功与新代际双消费者（当前前向结果）

上节“本人实际提交后的失败截面”为真实历史，现由同一意图的一次受控续接闭合。3产品源完成最小MODELING合同接合、当前交集权限及永久续接审计围栏；6项真实H2集成与9项一次入口有限H2分别通过，native端为fixture，不冒真实原生测试。随后20模块完整package通过（`maven.test.skip=true`，不是完整测试套件），2627输入仅3源变化，其余输入/SDK保持。

原本人确认不重发，旧e1失败/UNKNOWN/ACK不删；一次同task入口经锁内原tuple/4审计hash核验后a2e2，使用原Processor/guard/workflow。真实原生密码PUT16985、普通login16986、验证logout16988各一次HTTP200；0新增对象/资料PATCH/策略写/模型写。终末13条轮换审计和原60条完整保留，新UUID/task/key/credential-generation均未由基础设施生成。必要新窗口仅沿本人已提交意图，原D4窗口不延长。

同JAR正常运行恢复后，页面原任务成功；一次新只读POST3855.1666 HTTP200/code0，body严格expectedVersion4/READ_ONLY/[MODEL_READBACK]，两个消费者均读取新cut4/7/2。安全网络片段末3855.1667只有GET请求而无响应，保留捕获边界，不冒该GET完成。全部15操作资格NOT_VERIFIED，写入口未解锁。

当前原件（E为`m1-2/evidence`）：

- `modeling-rotation-integration-candidate-r3/isolated-r1/actual.json`、`d4-original-rotation-once-runner-candidate-r4/isolated-r1/actual.json`与各`test-result.json`：6/9有限回归。
- `modeling-rotation-full-build-candidate-r1/actual-root-r1/receipt.json`及`independent-design-r1/modeling-rotation-r4-runtime-physical-review.json`：3源安装、完整构建和真实class/package绑定。
- `d4-rotation-failure-diagnostic-reader-r1/post-rotation-original-readonly-r1/safe-projection.json`：原任务与轮换终末单SELECT。
- `root-d4-original-rotation-continuation-bound-r1/native-rotation-safe-projection-r1.json`、`final-maintenance-safe-projection-r1.json`及`normal-runtime-start-r1/forward-readback.json`：同账、两次各5启动安全GET、17轮换GET与1改密/1验证login/1logout、最终consumer4GET+1login及运行存活；15个既有pin文件全字节保持。
- `d4-post-cutover-process-exit-diagnosis-r1/report.md`与`current-segment-safe-projection-r2.json`：85027进程消失的有限诊断，退出原因UNKNOWN。
- `root-ui-native-intent-actual-r1/d4-rotated-consumer-result.json`、`d4-rotated-consumer-safe-network.json`、`d4-rotated-consumer-dom.txt`、`d4-rotated-consumers-viewport.png`：当前页面及新代际真实消费。

进入Root独验的剩余清单只有：对照唯一计划M1.2四项判据判定上述真实/隔离/未验边界，核最终精确代码与证据范围并完成Root结论及Git交付。B2等未验项必须在结论中明确处理，不能为补历史截图重放已完成profile/轮换。未Root接受前M1.3不放行，真人验收始终PENDING。


### 2026-10-23 B2 原资料确认关闭的最小隔离补证

监督Root亲读原5例后确认第四/第五例仍点正向提交，原“取消按钮存在/初焦”不能代点击取消或Escape。旧报告和现场PENDING保留，不重建已完成profile资格或重发真实操作。

本次仅新增`m1-2/evidence/frontend-b2-cancel-escape-current-page-r1/cancel-escape.test.tsx`的`it.each`两路径，直接导入当前仓库Page `aa608c0d…/66156B`及当前API，不导入旧候选。真实AntD/ownedConfirm未mock；测试不调用focus/force/fakeTimers。点击取消与user-event Escape各实到默认取消初焦→关闭→原Page明确指定的“核对原资料续接资格”读取控件有焦点，原任务URL/heading/status上下文保持，不冒精确回到正向提交按钮。两例均严格0POST/prepare/confirm/profile-continuation，原pending JSON/key、当前marker仍absent、4历史markers与整个local/sessionStorage相同。

实际14:20:37.264931→14:20:41.057156Z process exit0，2/2 PASS；13个关联产品源及测试/config/work副本前后相等。全部Axios为有限本地GET fixture，未知路径/method拒绝，fetch/XHR/WS封锁。0产品改动、0真实HTTP/SQL/浏览器，6/9旧充分样例与完整build不重跑。原stderr的AntD autoFocusButton弃用提示和jsdom伪元素getComputedStyle限制保留。仅jsdom技术补证，原现场B2/真人仍PENDING。

准确原件：[补证说明](m1-2/evidence/frontend-b2-cancel-escape-current-page-r1/README.md)、[test](m1-2/evidence/frontend-b2-cancel-escape-current-page-r1/cancel-escape.test.tsx)、[实际receipt](m1-2/evidence/frontend-b2-cancel-escape-current-page-r1/actual-r1/receipt.json)、[实际两例JSON](m1-2/evidence/frontend-b2-cancel-escape-current-page-r1/actual-r1/results.json)及同目录stdout/stderr。M1.2仍待监督Root独验/Git，M1.3 NOT_GO。


### 2026-10-23 D2 无映射消费拒绝与同名陌生开户不认领的准确依据

原`backend-implementation-r1/runs/local-r6/result.json`20/20及local-r7 1/1没有精确“无SQL managed映射时消费者检查拒绝”的断言；P1静态registry未注册scope、legacy冲突和Worker异人不代此项。只读定位结论为该项旧执行证据缺失，因此本次仅补一个离线方法，未制造任何真实账号冲突或重跑旧套件。

**无映射：** [当前代码单例](m1-2/evidence/d2-unmapped-consumer-current-r1/tests/cn/iocoder/atlas/module/infra/ontology/D2UnmappedConsumerTest.java)的`authorizedExactScopeUnmappedPersonCannotStartEitherReadonlyConsumer`复用当前`OntologyModelingAccessTest.setup()` H2/IAM fixture，不调用ready/confirm/inspect；先硬核当前operator人员∩项目权限、person2项目USER、批准instance及scope全有效，当前`NOT_MAPPED/version1`。调用真实`OntologyModelingAccessService.check(READ_ONLY,[MODEL_READBACK],expectedVersion1)`，精确409/`MODELING_VERSION_CHANGED`来自Store.createInspection的row缺失；同tuple `store.managed`为409/`MODELING_IDENTITY_NOT_READY`，不改产品错误码。不是靠失权、错误项目或legacy冲突让反例通过。

真实consumer/registry/broker/native-client接合的tripwire全0；Worker亦0，空自有secret目录保持空；所有自有H2 fixture逻辑行前后相同，modeling/identity的access/intent/inspection/audit/binding/task表均0行。只selectMethod这1例，不执行继承旧20/21方法。14:29:40.213148→14:29:41.747181Z，compile0/test0，1/1 PASS，0skip。当前16个相关生产源等完整d7e21d37包镜像，106个相关main/inner class与BOOT逐字相等；classpath只用当前target/classes＋当前BOOT libs，额外仅已有test jars，无旧产品overlay，javac sourcepath为空且仅编3个测试源。25个输入前后稳定。见[bindings](m1-2/evidence/d2-unmapped-consumer-current-r1/bindings.json)、[运行脚本](m1-2/evidence/d2-unmapped-consumer-current-r1/run_once.py)、[actual](m1-2/evidence/d2-unmapped-consumer-current-r1/actual-r1/actual.json)、[1例结果](m1-2/evidence/d2-unmapped-consumer-current-r1/actual-r1/test-result.json)及同目录class-proof/输入/stdout/stderr。真实HTTP/SQL/服务/产品写0，只有自有H2内存SQL；不代真实环境无映射尝试。

**同名陌生原生账号：** 复用Atlas仓库`atlas-module-infra/atlas-module-infra-server/src/test/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityNativeWorkflowTest.java:247`的方法`sameNameForeignUserIsConflictBeforeAnyNativeWrite()`：构造陌生同名用户，实际flow.preflight(PROVISION)在任何开户派发前抛`BeforeDispatchFailure/NATIVE_IDENTITY_CONFLICT`，retryable=false、无POST且credentialRef仍null，不认领或分配凭据。旧实际XML为Atlas仓库`docs/ontology-p1/evidence/verification-placement-b96-20261005/engineering/surefire/atlas-module-infra-server--TEST-cn.iocoder.atlas.module.infra.ontology.omidentity.OmIdentityNativeWorkflowTest.xml`，suite36/errors0/skipped0/failures0，精确该testcase一次且无failure/error/skipped子项。当前test全文及Workflow.preflight/optionalUser方法与开工HEAD6aae49c字节相等；Workflow其它资料/观察路径的本节点修复不改此PROVISION同名拒绝前门。此为既有P1隔离绿例复用，没有新增真实陌生账号或重跑36例。

完整准确交付范围见[59路径及已有前态](m1-2-delivery-paths-r1.md)：Atlas44＋前端15，53产品＋6仓内检查，前端4个混有M1.1内容需按opening拆分，后端6个旧WIP排除。B2/D2补测均为E-only，不增加永久产品路径；当前无产品修复、Git或M1.3。M1.2仍待监督Root按原四项判据独验，真人验收PENDING。
