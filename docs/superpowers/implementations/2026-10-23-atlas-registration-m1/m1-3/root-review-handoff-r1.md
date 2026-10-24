# M1.3 独立验收交接截面

本件为技术交接，不是第二进度账。唯一进度见[里程碑计划 §17.68](../../../plans/2026-10-22-atlas-metadata-registration-milestones.md)。当前可交监督 Root 独验；Root/Git 与真人验收仍待完成，M1.4 未放行。

## 可独立查看的结果

在 Atlas 本体项目 **M1 S6身份准备验证 → 建模权限 → 操作权限与验证**，人员104、实例 `om-p1-isolated`，点击“更新操作资格”。现有入口：

`/next/ontology/projects/3f10ce21-57fe-4efe-91c6-c9a3e4cf04f6/modeling-access?account=104&instance=om-p1-isolated#modeling-operations`

15项完整矩阵中，批准选择的11项均为“已验证”；公理、创建元数据、读取元数据、更新元数据约束4项未选，保持“未验证”。保存配置、原生策略同步和逐操作实际验证分别显示。原操作键的 UNKNOWN 历史仍保留，当前资格汇总不会把它改成成功。

实际原四UUID保持。原C条件应用后为 APPLIED/0.2，原术语为0.3且描述读回精确，审核人最后才更新到G，未用该旁路反证之前的策略授写。后继3984任务 SUCCEEDED/五步骤VERIFIED；08仍UNKNOWN，1a05仍REQUIRES_OPERATOR，两旧行完整16列哈希不变。

- [真实终末、Native及页面联合证明](evidence/phase2-final-ui-readback-r1/main-native-terminal-ui-proof.json)
- [真实Native正文语义](evidence/phase2-final-ui-readback-r1/main-native-semantic-body-readback.json)
- [三个固定原行一次SELECT](evidence/remaining-five-phase2-terminal-reader-r1/m13-phase2-terminal-20261024-r1-safe-proof.json)
- [矩阵实际DOM文本](evidence/phase2-final-ui-readback-r1/matrix-visible-dom.txt)、[上部截图](evidence/phase2-final-ui-readback-r1/operations-11-verified.png)、[下部截图](evidence/phase2-final-ui-readback-r1/operations-verified-bottom.png)。两图是滚动截面，不冒单图全部15项同屏。

## 四类验收及证据层级

| 操作卡项目 | 可审结果 | 范围与原件 |
| --- | --- | --- |
| 权限预览、完整差异、共享影响、当前版本及取消 | 原有合同/有限检查及实际104/S6批准配置复用 | [操作卡](acceptance-card.md)、backend-candidate-r1/finite-first-r1；不是最终cut全矩阵重跑 |
| 策略同步与普通身份11项实际验证 | 原Policy/Role同步及独立读回；原7项加剩余5项，Lease重叠，当前11项实际绿 | 正向经真实Store、Registry/Consumer、普通Broker、SDK及Native；无管理员业务fallback。Root此前接受的原cut复用 |
| Atlas拒绝、普通查询用户、范围外、OM拒绝、仅ViewBasic | 各有具名有限场景；最后两个缺项已补2个测试并通过 | 原caller/target scope有限检查、native-adapter-candidate-r1/finite-r3；[新2项完整边界与结果](evidence/qualification-write-denial-finite-r1/README.md) |
| 撤权/代际变化、下一派发与返回后重核、已知ACK保留、同键不二派 | 原有限场景与最终双父H2场景复用，原旧任务实物始终保留 | qualification-remaining-five-phase2-candidate-r1/model-results-forward.json；IAM与QualificationPort为fixture，非真实撤权/轮换操作 |

新2项以当前9c1真实Store→Adapter→Registry.qualifiedClient→Consumer→Broker→SDK执行。H2的IAM、身份、已同步Policy/既有G，合成JWT及owned fakeHTTP权限响应均为夹具：写403保留DISPATCHED/UNKNOWN、无写ACK、无fallback/无二派；两次读200只使TARGET_READ绿，Reviewer PATCH403仍不绿。不能把它们写成现场OM权限策略/403、真实ViewBasic角色或真实撤销恢复已经验收。未为补测试改真实Role、开新身份或绕旧UNKNOWN再发现场任务。

## 最终源、构建与运行

[精确产品源截面](evidence/phase2-final-ui-readback-r1/final-product-source-cut.json)：后台27、前端3；全部2640后台最终构建输入及1032前端最终构建输入与当前字节一致。3项后台和原前端空间/导航等无关WIP排除，未reset/clean/stash或add全仓。

- 后台最终9c1包：完整offline Maven package0，skiptests；有限检查单独列证据，不把skiptests当测试通过。
- 前端：mode gate、完整tsc与Vite构建0；原chunk警告保留。后续只有读取和截图，无产品修改。
- Native：c2b单源覆盖包实际部署，CP0替换、其它615项与43个旧覆盖类保持，健康200；成功apply为真实兼容结果。主Native checkout未改，源码/差额/复现构建保存在E；不是完整Native reactor发布包。
- 当前Atlas50635/9c1；Native33558；System/Worker/Vite等未重启。外置one-shot只执行了固定3984，严禁把旧脚本当重试入口。
- 最后有限Native累计17116/get17074/auth13/login9/mut42/object19，最后3资源category各1。具名联调窗口04:33:15Z已结束；这不是开放代理或生产环境验收。

保留所有先前失败：原强态读拒收/UNKNOWN、首次URI门误拒、bootstrap时限过紧，以及有限夹具失败与额度证据标签前向更正。没有当前自动审批拒绝。普通周额度04:27:58Z截面22%用、78%余（最新原件见 evidence/phase2-final-ui-readback-r1/final-ordinary-quota.json）；未用点数/重置/切账号或key。

## 交付与回退边界

监督Root独验后按本30项产品源及本节点文档的精确清单交付Git；不要把其它WIP带入，也不要从本地Semantica main推送无关提交。第三方classpath JAR与本地classes不作为源提交，不force-add整套依赖；保留源码、补丁、构建参数和哈希回执即可。Native覆盖源码/复现步骤属于本地兼容交付范围，正式Native主树集成或全reactor发布须保持这一层级说明。

回退先停止新派发、保存原行/原键/ACK/预算，再按精确提交和已保存包逆序处理。SQL32及原任务、四对象和已APPLIED业务状态不随代码回退被删除或伪恢复；策略/凭据撤销不隐含在源码回退中。不自动rollback，不重放历史任务。真人最终验收PENDING。
