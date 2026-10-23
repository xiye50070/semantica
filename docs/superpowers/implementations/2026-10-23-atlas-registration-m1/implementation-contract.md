# M1.1 Atlas本地准备合同 v1

本节点仅Atlas本地新项目、就绪摘要、草稿、可恢复LOCAL_PREPARATION意图。M0机器合同沿用12维readiness及INIT状态/首次claim/120秒lease/UNKNOWN原则；LOCAL_PREPARATION是明确新增contractVersion=1的本地operation，不能冒任何原生配置完成。无OM客户端、源读取、后台执行器、共享部署或浏览器claim/receipt端点。

所有浏览器返回CommonResult。项目创建复用POST `/admin-api/ontology/projects/create`和原ProjectWrite；X-Request-Id是稳定operationKey，前端跨关闭/刷新保原body/key，未知结果先GET `/projects/by-operation-key?spaceId&operationKey`。服务端先当前tenant/actor/space管理授权，再以tenant+actor+operationKey持久幂等；hash覆盖全部body、space/target和服务端actor/tenant，同键内容变化409 IDEMPOTENCY_CONTENT_CONFLICT，不因当前未收到ACK再创建。lookup仅返回本人的精确space原project快照或null，再由projects/get取当前版本。

GET `/projects/readiness?projectId`返回sealed Readiness：projectId/projectVersion/steps恰12/businessAcceptance=NOT_RUN。capability顺序PROJECT、IDENTITY_MAPPING、NATIVE_WRITE_QUALIFICATION、TARGET、REVIEWER、METADATA、PROJECT_SOURCE、USER_AUTHORIZATION、MODEL_PACKAGE、ROW_READ、FORMAL_PUBLICATION、QUERY。每项state/reason/requiredCapability(nullable)/nextPath(nullable)/dependencyVersions。PROJECT为READY；其余NOT_CONFIGURED，旧omTarget仅TARGET NOT_VERIFIED。nextPath只指可用Atlas当前readiness锚点，不假造后续路由。USER当前精确项目授权可读摘要；配置/草稿/意图仅ADMIN。

GET `/projects/preparation/draft?projectId`返回{projectId,projectVersion,draft:{version,notes,updatedAt}|null}。PUT同path body={projectId,projectVersion,expectedVersion,notes}，notes最多4000；无草稿expectedVersion=0，其余精确CAS；同事务校验当前projectVersion、当前授权、draft版本，返回同wrapper。notes只是本地记录，不声明native配置。项目切换/晚响应由前端projectId、scope、version及稳定请求context守门。

POST `/projects/preparation/intents` body={projectId,projectVersion,draftVersion,operationKey}。GET `/projects/preparation/intents/get?projectId&taskId`；GET `/projects/preparation/intents/by-key?projectId&operationKey`未找到data=null。POST `/projects/preparation/intents/cancel` body={projectId,taskId,expectedVersion}。所有新body/query拒未知字段，actor/tenant/space均由服务端验证上下文解析。当前ADMIN且本人可读/取消意图；同key跨project/draftVersion或内容改变409，先授权再命中历史。

LocalInitIntent={contractVersion:1,taskId,family:INIT,operation:LOCAL_PREPARATION,scope:{tenantId,spaceId,projectId},initiator,operationKey,requestHash,projectVersion,draftVersion,state,stage,attempt,firstClaimedAt,version,dispatched,ackReceived,readbackVerified,allowedActions,createdAt,updatedAt}。初始READY/WAITING_LOCAL_ADAPTER；所有readiness缺项仍未就绪。本节点无生产runner自动领取或将本地确认当整体初始化成功。

内部可信store及隔离适配器仅用于持久状态基础：claim/cancel同项目→任务行锁、首次claim永久firstClaimedAt、attempt/token/owner/120秒截止/taskVersion/cut双重fencing；领取后含RETRY_WAIT拒人工cancel。ACK和独立读回分别存储/计数，旧ACK不能由读回补造；UNKNOWN不能claim、旧receipt不能覆盖、同project未定效果不能换key绕过。LOCAL_CHECK_VERIFIED仅代表本地适配器证据，不把原生目标/权限/元数据就绪。无公开任意成功模拟端点。

有限错误码msg包括IDEMPOTENCY_CONTENT_CONFLICT、PROJECT_VERSION_CHANGED、PREPARATION_VERSION_CHANGED、INIT_FIRST_CLAIM_CANCEL_DENIED、INIT_STATE_NOT_CANCELLABLE、INIT_STALE_RECEIPT、INIT_UNKNOWN_RECONCILE_ONLY；CommonResult.code沿400/401/403/409。越权/不存在scope都为403 Ontology access denied，避免资源枚举。

验证用独占H2内存库、真实两线程/两连接row锁和重建service读同库；无网络/共享DB/原生动作。现有知识空间3文件WIP及SpaceApiTest保持原前态快照，只作本节点必要增量。M1.2以后未实施；本文件不是部署或业务验收。


实现校准：12步nextPath映射为readiness-checklist/identity/target/source/package/publication/query页面现有锚点；requiredCapability=null仍明确序列化。意图state全集READY/RUNNING/RETRY_WAIT/VERIFYING/PARTIAL/FAILED/BLOCKED/UNKNOWN/SUCCEEDED/CANCELLED。初次保存草稿得到version>=1后才能登记意图，draft=null不伪造版本。相同已受理请求在随后draft/project升级后仍返回原意图；其历史cut不能因此获得新claim资格。同key换projectVersion/draftVersion仍409。

已派发的同代lease可在过期或draft版本改变后保全为UNKNOWN，但taskId/请求hash/owner/token/attempt/taskVersion必须仍准确匹配；该保全不会重新派发或接受迟到成功。成功receipt仍要求当前授权/cut/未过期lease。撤权后的原始回执归档和生产runner不属M1.1，本节点不宣称该撤权E2E通过、无任何管理员/reader fallback。

实际隔离验证：JDK21 javac -parameters，输出/private/tmp/atlas-m1-backend-isolated-r1，14个新增H2用例及23个既有Project/Space回归合计37/37通过，exit0。首轮漏-parameters导致14个既有绑定失败，加上1个测试对既有global错误envelope的404假设，原件保留未覆盖。SQL仅新增MySQL/DM迁移文本及H2测试schema，未应用真实数据库；未部署、未运行浏览器验收。
