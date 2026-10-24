# M2.2 连接与权限诊断交付说明

实现与有限现场验证已形成；Root独验、Git交付和真人业务验收待完成。本文不登记验收或进度，**M3 NOT_GO**。

## 实际入口

打开 [Atlas元数据接入连接](http://127.0.0.1:5188/next/ontology/data-sources/b02fd17d-fdbe-4b4a-a83b-47a855d70017?projectId=3f10ce21-57fe-4efe-91c6-c9a3e4cf04f6)，进入已保存连接详情，在“连接与权限诊断”中明确选择“诊断使用的 OM 身份”，再点“运行诊断”。即使只有一个候选也不自动选人；发起管理员与所选建模人员分别核验。缺映射或普通会话时沿页面现有身份维护入口处理，不使用管理身份或Reader兜底。

## 已实现范围

Cloud持久记录原operationKey、连接/秘密/映射/节点版本cut，当前项目ADMIN准入先于历史命中；Worker沿可信authorize、单次borrow、分阶段current、finish完成有界只读检查。秘密仅在受控调用内使用，M2.1配置和旧Worker四scope权限保持；没有为新项目增加全操作scope或Provider。

UI独立展示节点、源网络/TLS、源认证、元数据范围、OM身份、OM权限和版本兼容七层及报告当前性。替换秘密、禁用、版本/身份/节点变化或报告到期不能沿用旧绿；UNKNOWN只按原id/key读回，不自动重发。NEW_SERVICE只检查登记资格，不创建OM服务；不读取业务行，不写源库或OM模型，也不登记/采集来源。

## 分层证据与本次真实结果

- 源码与有限样例：Java隔离H2/MockMVC覆盖授权、幂等、单次借出、阶段/CAS、到期重启UNKNOWN及安全投影；前端严格parser/身份选择/当前性，Worker假IO与真实Java合成producer消费均有原始批次记录。普通Java包构建exit0；R5仅修HTTP状态分类并完成定向检查。批次不是最终同cut全矩阵，早期编译、夹具和断言失败原件保留。见[后端验证汇总](evidence/backend-candidate-r1/verification-summary.json)、[前端受影响验证](evidence/frontend-candidate-r1/revision-r8/README.md)、[Worker R5记录](evidence/worker-candidate-r1/revision-r5/manifest.json)、[最终Java包收据](evidence/backend-candidate-r1/full-build-r3/receipt.json)。H2不代表真实MySQL/DM执行。
- 正常运行：新Node PID45025、Web PID45239切换成功，采用runtimePin `c6f80157…afdf3`；Java仍为原普通R3包。见[Node收据](evidence/runtime-http-status-fix-r1/actual-worker-r5-known-inspection-r1/result.json)、[Web收据](evidence/runtime-http-status-fix-r1/actual-web-r5-known-inspection-r1/result.json)。
- 真实诊断 `44579eeb-b23c-40d7-842a-fd594d68a68f`，原key `31c8cd72-dfb6-4b16-ae84-5aa8ff308a78`：Source四层及对应普通OM身份通过；OM `databaseService/Create`实际HTTP200返回 **conditionalAllow**，UI安全保留 `OM_PERMISSION_CONDITIONAL`，没有扩大权限，也没有转为无条件通过。UI版本层为 **NOT_RUN**；Native虽实际GET到 `2.0.0-SNAPSHOT`，不能替代完整版本层通过。报告有效窗口为2026-10-24 20:21:41.596至20:26:41.596 UTC，到期后只作历史结果。
- 新独立验证窗口合计13次Native调用（12GET、1次普通身份预热login），其中诊断自身3GET；无新refresh或语义写。原17250行前缀、全部旧namespace、全局stop及22次历史object writes保持。源结果来自实际节点报告，源SQL协议未单独抓线，不冒逐条SQL抓线证明。见[真实Native安全读回](evidence/runtime-http-status-fix-r1/main-execution-r1/native-final-actual-readback.json)。

终末只读复核于20:26:25 UTC完成Worker/DM各1次显式SELECT，15项active/lease计数全0，已归属inspection UNKNOWN1及旧global UNKNOWN2、qualification UNKNOWN1保留。报告到期后，同id/key只读显示expired；Native仍为17263行，没有新诊断或源复跑。见[终末idle读回](evidence/runtime-http-status-fix-r1/known-inspection-idle-r1/main-read-final-r1)。Main另完成30源、19保护项、6进程7端口及3个HEAD的末态核对。

## 现场环境

本次真实验证使用18587受控原生读取窗口（20:18:53.066–20:33:53.066 UTC），复用同一有效管理会话，未再次刷新或扩大权限。该窗口及报告TTL都是有限条件；到期后的历史读回不产生新探测，不能把本次记录外推为任意时刻的生产可用性。新的真实验证须准备当时有效的隔离读取条件。当前计划服务仍未创建，本项目原M2.1接入引用保持禁用状态。

## 保留边界

旧b337 FAILED、所有UNKNOWN、停止latch、原请求/预算/审计及早期执行失败保留；修复不回填旧报告、不重放旧诊断。已归属的M1 inspection UNKNOWN只作精确历史保留，未归属UNKNOWN及活动/lease门仍拒绝。本次证明诊断链和条件权限的安全呈现，不证明全部配置/身份/服务模式均现场通过，本次TLS仅按配置的REQUIRED策略核验，不证明其他TLS策略、跨环境证书校验、业务发布或M3能力。
