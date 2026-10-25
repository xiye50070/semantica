# M3.1 发现元数据与精确选表

当前结论：**ROOT_ACCEPTED_FINITE_TECHNICAL**，Root于2026-10-25 15:18 UTC（北京时间23:18）完成独立验收。Cloud17与React6的精确代码提交、push及远端核同已完成，见[Git回执](evidence/git-delivery-main-r1/m31-code-delivery-final.md)。Semantica精选文档交付由Root执行；三仓远端核同后才进入M3.2。真人最终验收仍PENDING。

S1已由Atlas真实发现28表254列、28PK/26FK/23UNIQUE/25CHECK，S7独立真实发现3表13列、3PK/2FK。S1全部25CHECK明确UNSUPPORTED；两来源各两版精确选表及一次固定应用DM回读均通过，未选FK目标未自动补选。源业务行读取0、OM对象登记0。

- [Root独立验收](m3-1-root-acceptance.md)
- [S1/S7真实结果及边界](evidence/s1-s7-approved-execution-r1/m31-s1-s7-functional-handoff-r1.md)；同名JSON保存原扫描/操作键/TTL/hash/精确名单及实际回执。
- [最终23路径范围](evidence/source-install-r3/manifest.json)：Cloud17、React6，19产品/4检查，保原19处无关工作。

源发现只证当前账号配置库范围的返回结构；没有独立源SQL wire或全部AtlasHTTP抓包，14/源、28/两源是实现上限。真实200表、原生200%/IME、全部专业生命周期及业务全链路仍按有限/未验层级保留。到期快照保历史，不重扫续绿。

## 原2表8列交接历史（04:02 UTC cut）

以下完整原文只说明当时已完成的2/8样本及旧未验截面，其“S1/S7未验”已由上文新的真实证据和Root验收前向补齐；保留失败/UNKNOWN/构建cut和原证据入口，不作为当前S1/S7状态。

# M3.1 发现元数据与精确选表

Atlas 已实现只读发现、持久快照、服务端分页搜索及精确选表。2026-10-25 的真实浏览器验证从既有批准连接发现 **2 表、8 列**，读取复合外键，并将选择版本从仅子表 v1 增量变为子表和父表 v2；应用库独立只读回读与界面一致。未向 OM 登记对象。

状态为 **IMPLEMENTED_WITH_FINITE_AND_REAL_TWO_TABLE_EVIDENCE_PENDING_ROOT_REVIEW**。M3.1 尚未 ROOT_ACCEPTED、尚无本节点 Git 交付；M3.2 未放行，真人验收 PENDING。S1/S7 的完整真实物理样本仍为 `NOT_VERIFIED_ENVIRONMENT`，不得把本次两表样本或合成结果填入该栏。唯一进度账仍为原里程碑文档第 17 节，本页只作交接和证据导航。

## 范围与实现

- 精确范围为 23 路径：19 产品、4 检查，5 旧、18 新。见 [scope.json](evidence/scope-registry-r1/scope.json)；Cloud 原 3 处和 React 原 16 处无关工作、两个真实 Git 索引保留。
- Worker 只使用固定的 MySQL 元数据查询，在同一批准账号和配置库范围内进行两次读取并比较返回字段指纹；成功路径至多 14 条显式源语句。不读取业务表数据，不携带 OM 令牌，不调用 OM。未独立采集本次源 SQL wire，不把代码上限说成抓包计数。
- Cloud 保存快照及追加式选表版本；分页、搜索、精确名单、未选外键依赖均来自原快照。`ALL_MATCHING` 将匹配集合加入已有选择。超过 1,000 表或 50,000 列明确拒绝，不截断。
- React 使用现有 Aham 8.0 / React AntD。r8 修复资格失败后的旧可用状态，以及同账号配置版本变化；r9 仅在 scope 退役时清旧本地未受理历史的呈现。保留服务端原 state/id/key 和所有 UNKNOWN。cold 恢复仅适用 start 的精确 HTTP409/code409/ORDINARY_SESSION_REQUIRED 加一次原键合法读空；先保存并读回 LOCAL_NOT_ACCEPTED，再释放本地预约，不自动重发。

## 原操作卡逐项证据

| 操作卡 | 实际证据及限制 |
| --- | --- |
| S1 与 S7 分别发现、字段及约束、25 CHECK 限制 | 有各自形状的有限合成验证；**没有本轮 S1/S7 完整物理源验证**。本轮真实源仅既有 M0 隔离库两表八列，含两个 PK、复合 UNIQUE 和一条两列 FK；实际 CHECK 为 0。未擅自建库、扩 GRANT 或读取 S7 业务行。 |
| S4 跨页、搜索、本页及全部匹配、明确上限 | 原真实 App 加隔离 mock 的 200 表场景及有限服务端验证保留。真实两表场景中，搜索父表时已有子表选择保留，全部匹配后精确集合为两表。未声明真实数据库 200 表验收。 |
| 账号可见范围、预期缺项、未选 FK 依赖 | 本次真实页面显示当前账号可发现 2/8，预期 `m31_expected_absent` 缺项；v1 只有 child_pair，列出 parent_pair 依赖且未自动补选。部分可见、范围外目标名隐藏另由有限验证支持。 |
| 连接/目标/选择失效、扫描中漂移 | 有限服务端、Worker 和 r8 当前性/历史恢复验证；r9 owner 残留单例。未向本次真实源注入 DDL 漂移、撤权或未知 ACK。结构哈希只覆盖返回字段，不代表全 DDL、全局事务快照或持续无漂移。 |

## 已执行的真实采用与读回

1. [源码 r1](evidence/source-install-r1/actual-r1/result.json)、[r8 前向 r2](evidence/source-install-r2/actual-r1/result.json)、[r9 前向 r3](evidence/source-install-r3/actual-r1/result.json) 分别保原收据；最终完整清单为 [r3 manifest](evidence/source-install-r3/manifest.json)。r2 写 5 处，r3 写 2 处，均为顺序非原子 CAS，无自动回滚。Main 已核 [最终 23 源和保护项](evidence/main-final-source-readback-r1.json)。
2. [DM38 实际执行](evidence/dm38-preparation-r1/actual-r1/result.json) 返回 3 条 CREATE，并由 7 条显式 SELECT 核两表结构、约束及索引，确认当时两表无记录；无 DML。DDL autocommit，不宣称原子迁移，未再次执行或删除表。
3. Worker 112 个公开输入已绑定并 verify-only；节点 registry 仅原 runtimePin 一处 CAS。原 8 键和 4 scope 保持，不改凭据权限。
4. 新 Native purpose 使用独立窗口 `d82a4e4c-1145-4f0b-a254-f6fbfd8ff8f8`，没有复用过期窗口。实际同管理员 refresh/self 两次、Web 启动五 GET、104 普通身份登录及四 SELF，共 12 次 HTTP200；GET +10、management_auth +1、ordinary_login +1、mutating 认证计数 +2。对象写入仍 22，旧 17,263 条前缀、旧 namespace 和业务停止闩保持。见 [安全读回](evidence/native-main-execution-r1/startup-warm-actual-safe.json)。
5. [Worker 采用](evidence/runtime-bindings-prepared-r1/actual-worker-normal-r1/result.json) 和 [Web 采用](evidence/runtime-bindings-prepared-r1/actual-web-normal-r1/result.json) 分别先读 1 次 Worker SQLite 和 1 次应用 DM，核 17 类空闲及原精确 UNKNOWN，再各一次 TERM/一次正常启动。新 PID 为 18031 / 18199；Worker 4 参数、Web 保原 102 参数并附加一个开关共 103 参数。没有 bootstrap/class overlay、健康 HTTP 或自动重试。采用守护当时绑定 **r2 源码 cut**；随后 r9 只改前端两路径，不伪称采用发生于 r3。
6. 原浏览器在 104/config6、binding9、epoch2 下完成只读身份消费，再对 `b02fd17d-fdbe-4b4a-a83b-47a855d70017` 点击发现一次。原记录 `a6f5118c-5453-42df-ac93-b30d20856d1b`，原键 `e3700d5d-1852-4106-9f07-4b2c001d33e6`，SUCCEEDED/FINISHED。结构指纹 `sha256:7e16fcdf757e23ab46cdbc016b2cace3db23f3caedd0e72f38ff67af39510612`。
7. 完成于 03:51:53.286 UTC，到期 04:06:53.357523 UTC。快照到期后应失效；不会为保持绿色自动重扫。两次选择为 v1 child_pair、v2 child_pair + parent_pair。03:59 的 [单 SELECT 持久回读](evidence/real-source-readback-prepared-r1/actual-r1/source-selection-readback.json) 核完整纯元数据快照、重算哈希及两条历史，无 capture_json、源查询或 Native 请求。浏览器动作、AX 与截图见 [动作读回](evidence/native-main-execution-r1/real-browser-action-readback.json)。没有独立捕获全部 Atlas HTTP 请求体/状态 wire，不以截图替代该层。

[运行采用与 r9 非作者只读复核](evidence/runtime-adoption-independent-review-r1/review.md) 于 04:02 UTC 核当前进程和原件，确认源扫描后 Native 仍为 17,275 条、无新增请求，旧 namespace/停止闩保持；没有重跑 SQL、浏览器、测试或服务操作。

S6 项目原 `M21 … KEEP` 默认接入配置仍为禁用状态，未借此次发现改为当前连接。此次验证的是同项目中显式选择的 b02 接入，不宣称整个项目来源、业务取数或发布已就绪。旧 M2 诊断的 conditionalAllow 和到期状态也保留，没有当作写权限通过。

## 有限检查与最终构建

- 原后台 r6、Worker r5 及初版前端检查按各自成功 cut 复用；旧失败、命名/断言前提错误和未运行准备稿均保留，不拼成一次最终全矩阵。
- r8 的 14 个当前性/历史恢复方法与后加 4 个 cold 方法分时通过；[非作者只读复核](evidence/frontend-r8-independent-review-r1/review.md) 保留其原结论与窄 owner 残留。r9 仅新增一个受影响回归通过及定向类型检查；首轮额外断言前提错误保原。
- Java 最终正常离线 20 模块 package 通过，测试另按有限证据提供，不将 skiptests 称为测试通过。[最终 Jar](evidence/full-build-r2/receipt.json) 对应 2,698 输入及 12 overlays；[字节对应](evidence/full-build-r2/class-compatibility.json) 核 9 Java 族、47 类，正常 Jar 实际采用。
- [最终 r9 前端完整构建](evidence/frontend-final-build-r9/receipt.json) 于 03:57:10.449920–03:57:34.771055 UTC，production gate / 完整 tsc / Vite 均 0。**当前真实输入 1,067 = 新隔离 stage 1,067**，Main 逐项核前后/当前及 6 个构建控制。旧 r8 的 1,068 包含一个仅 stage 的兼容探针，原件保留；最终构建不包含该探针。[前向澄清](evidence/frontend-final-build-r9/correspondence-proof.json)。238 个新 stage 产物未部署；750kB 提示保留。

## 验收入口与剩余范围

现有本地入口：`http://127.0.0.1:5188/next/ontology/data-sources/b02fd17d-fdbe-4b4a-a83b-47a855d70017?projectId=3f10ce21-57fe-4efe-91c6-c9a3e4cf04f6`。明确选择 104/config6，按原扫描读回历史。不要运行旧诊断、重放原身份任务或为重现截图启动新扫描；确需新发现时须对已到期终态显式发起。

完整 S1/S7 真实源、真实 200 表、Native 200%/IME、所有专业生命周期及业务端到端仍按证据层级列为未验。M3.1 节点通过范围由 Root 独立裁定并精确 Git 交付后，才进入 M3.2；本件不替代该裁定。M4 元数据写、M5/P2、业务行摄取和发布未执行。

回退未执行。需要回退时先停止新增扫描并核在途记录/租约/UNKNOWN，再由明确版本切换或精确提交回退处理；本节点创建的发现和选择记录、DM38 两表及所有历史保留，不 drop、不自动重发或重建业务对象。
