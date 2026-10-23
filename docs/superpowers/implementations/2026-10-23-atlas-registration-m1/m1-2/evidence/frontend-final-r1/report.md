# M1.2 前端实现与隔离验证

当前入口为 `/next/ontology/projects/:projectId/modeling-access`，由项目准备页“身份与操作资格”进入。已实现有权人员分页目录、批准实例选择、精确项目范围、受管 UUID/版本与同步事实、差异计划及显式确认、当前 tuple 禁用/撤销、全局轮换影响展示、原任务恢复，以及模型客户端与 Worker 分别显示的只读身份消费结果。始终显示“建模写资格未通过”；15 项建模操作资格仍全部 NOT_VERIFIED。M1.3、目标、Glossary、Reviewer 未实施。

权限采用服务端人员管理与项目管理交集。USER 仅本人读与检查，不请求人员目录或管理 Project GET。所有 async 结果保持 session/project/person/instance/version owner；401/403 会撤下旧身份、consumer 结果和管理动作，保留原任务键，重新读取通过整页当前授权读完成。确认前用 Web Locks 持久化无秘密原 key/body，成功清理按相同 key 比较删除；UNKNOWN 只读原任务，不换键开户。仅四个已证“未受理”409 码允许重新准备。

## 固定变更

源 cut：`../frontend-source-cut-r1/manifest.json`（290e8930…/12334）。4 修改：App、platform routes、standaloneRoutes、Readiness 页入口；6 新增：Page、API、CSS、API tests、隔离 browser spec/config。全部 10 文件正向/逆向系统 patch（fuzz0）成功；4 前态逐字恢复、6 新文件逆去。其余 18 个 opening 文件逐字未变，包括共享 Scope/http、旧项目编辑/恢复与已有 WIP。未使用 Git，未改唯一进度账或 M1.1 封件。

Aham 8.0 light / React19 / AntD6；设置页连续白底、整单差异预览、唯一主操作、只读系统字段、取消初焦确认、局部 Ctrl/Meta+Enter 只打开同一确认。390 底栏在同 main 的实际 flow 宿主内 Portal，不修改共享 Modal/Scope 或全局布局。

## 实际验证与边界

- API：当前 API98168/test42a557 的 12 个 Vitest 样例通过，实际工具 completion e7ceee exit0；原 10/11 的 header 参数错误和早期 TS narrowing 失败保留在 development-verification-r1。operationKey 使用独立 `[A-Za-z0-9_.:-]{1,128}`，普通 Id64 规则未误用。
- browser-r1：实际 exit1，6 通过/1 失败。通过部分覆盖预览不派发、显式确认、UUID稳定、consumer 分层、轮换影响/禁用预览、ACK503 原键 UNKNOWN 恢复、USER self、迟到 owner 结果、分页、1440 与严格负向围栏。390 原失败 footer.bottom=1012.421875；原日志/trace 保留。
- scroll-diagnostic-r1：实际 exit1，定位 footer 的短 plan 父容器约束；真实 Cancel→Tab→Save 与 focus-visible 已过，几何失败保持。没有以 sleep、force 或缩小控件处理。
- browser-r2：20:47:06.370808–20:47:15.070963Z，实际 exit0，仅三个受影响例全部通过：390 真实 Tab、CtrlEnter owned-confirm Cancel 初焦/Escape 返焦；USER 403 退役/重新授权；管理预鉴权403零 POST 与 confirm403 原 key 保留后只 GET。390 actual viewport PNG 已亲看，footer.bottom=1000、height121、host flow145、focus-visible=true，44px 控件和无横溢断言通过。首轮其余六成功未重跑；不是声称最终 cut 单次全8通过。
- 完整 build：20:48:03.847723–20:48:28.039674Z，mode/tsc/Vite 各 exit0；产物只在 `/private/tmp/atlas-registration-m1-2-frontend-production-build-r1`。保留 Vite 现有大 chunk 警告，无构建错误。

浏览器实际使用 Chromium、light/reduce、SW block、5297 strictPort、五代理至127.0.0.1:9；origin/method/path/query 精确白名单先拒绝未知 API，全部业务回执为本地隔离 fixture。原 strict probe 保留三 HTTP 拒绝及外部 WS 拒绝审计，未清数组。Root 独立读首轮全部7trace：950HTTP/9localWS/134API，130HTTP200/130JSON=128code0+403+503；另3负向 abort，ACK丢失例尾 GET 有 fulfilled marker 但 status=-1/无 body，不能冒全部完成。新 r2 trace/context 与 raw 结果另保，详情见 browser-observations.json。

真实后台建模 API、OM 账号开户/轮换/禁用、Worker native 消费、真实权限和业务验收均未由前端本轮执行。此处 VERIFIED 只属于合成 contract 样例，不代真实消费者证据；普通只读身份通过不授建模写权限。1440 是首轮旧 footer cut 的像素/程序 focus 样例；最终 footer 的 390 新像素与真实 Tab 分开记录。未冒其它视口、dark、200% 或完整无障碍矩阵通过。

所有原始失败与前态保留；当前源码已完成固定后全10文件读回。新源码未发生真实外部资源写、SQL、认证/共享引用变更或部署。
