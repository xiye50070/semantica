# M1.4 Root 独立技术验收

2026-10-24 10:47 UTC，结论：`ROOT_ACCEPTED`，限唯一计划 §5 M1.4 与本节点验收卡的有限技术范围。新建、复用及项目绑定均有真实正向结果；未知效果、部分成功和拒绝边界按具名有限方法接受。真人最终验收仍 `PENDING`。Git 交付另见 [交付报告](m1-4-git-delivery.md)，完成前不进入 M1.5。

## 原项与证据

| 原项 | 独立核验结论 | 层级及边界 |
| --- | --- | --- |
| AC1 新建与准确绑定 | S6 从无目标开始，经 Atlas 一次 CREATE，seq17131/201；新 G `ab2c2d5f-05cb-4c5d-959e-a9f29d1fd4be`、名称 `AtlasM14_S6_20261024_R01`、版本0.1。Owner为本人104的稳定OM UUID；Reviewer为空，未冒完整就绪。独立GET及本地CAS最终指向同UUID，S6版本1→2。 | 真实Atlas动作、Native ACK/GET、一次持久终态读取。Root复用已验S6原件，未重复请求。 |
| AC2 跨项目复用 | 真人具体授权后，新项目 `ebed8ab8-a686-47ee-a7cb-cb778617730d` 配置104 USER及原身份本项目映射，未建身份或同步新Policy。新意图 `75149e86-6063-4216-89c1-4e8bff3c4fb4` / key `03be0335-796e-4a88-adbf-339edcbb76cb` 为REUSE/SUCCEEDED/row5，同G0.1，原生读回VERIFIED、本地BOUND、项目1→2。 | 真实普通OM_USER，Atlas当前权限与本人OM读取相交。新行固化选定版本与observationHash；来源S6意图引用来自独立旧回执，不是新行持久字段。 |
| AC3 同名、404、搜索延迟、并发、未知效果 | 同名不认领覆盖；精确目录/UUID范围约束；持久单派和UNKNOWN不重建；Native拒绝不填成功。 | 具名H2、owned HTTP与React检查分时闭合；IAM、JWT、Native响应有fixture。不是最终版本单轮全部现场故障注入。历史夹具/API/解析失败保留。 |
| AC4 部分成功与换绑定 | S6已知201后因Native说明编码差异曾停UNKNOWN；修复仅允许原意图、完整201证明、同UUID当前读回及本地CAS，不重复创建。换绑定影响、现权/版本重核及旧计划失效、旧API绕过拒绝按原有限方法核验。 | 真实S6部分成功→本地补绑；其他负向按有限方法。保留旧Glossary及发布指针，不将本节点扩成迁移或发布。 |

原卡：[M1.4验收卡](m1-4/acceptance-card.md)。最终固定交付与本地原件导航：[Work交接](m1-4/handoff-for-root-review-r1.md)。本报告与唯一进度账是当前结论；交接材料中的“待Root/Git”是交接时点。

## Root本轮亲读与非作者独核

Root亲读官方命令原结果、DM34剩余两DROP关键差额、AC2 warm-only helper及接线差额、真实安全投影和JPG。三个有界非作者分别完整核DM34续接、AC2四读/绑定终态、原项覆盖及精确交付范围，均无新增接受阻断。充分旧源码、有限检查和构建复用，不重跑。Root没有执行产品测试、SQL、HTTP、浏览器或服务操作。

- [AC2原意图](m1-4/evidence/ac2-actual-reuse-intent-r1.json)、[九项目终态](m1-4/evidence/ac2-local-reuse-after-r1.json)、[原生四读](m1-4/evidence/ac2-reuse-native-final-readback-r1.json)：17147–17150为self/read/self/read，全部200，无新增登录、管理认证或原生资源写。最终17150 ops / GET17103 / ordinary12 / management14 / mutating47 / object20 / rootCreate1。两G响应1202B/hash `fd18a721889dc41f6b6d427a4013c4009999d96fac6ce0f6614a256b08bda599` 与前次逐字相同。原17146前缀、62旧成员、旧namespace/latch/denials由公开安全投影核对；Root未重读私有全账。
- 九项目选定字段比较仅AC2项目变化，其余八项及S6原回执相同。零资源写和返回字节相同支持本次未修改owners/tags/reviewers/术语；没有额外遍历全部术语关系或未返回字段。
- [真实截图](m1-4/evidence/ac2-reuse-bound-20261024.jpg)可见v2、同UUID、原key、复用无需创建、读回已核及本地已绑定，并保留Reviewer完整就绪待核/NOT_CONFIGURED。截图裁切项目名和URL，不能单独证明路由、登录主体或自动刷新过程；自动刷新检查另有组件证据和Work实际操作观察。
- [DM34最终两DROP](m1-4/evidence/dm34-drop-old-continuation-r1/actual-r1/result.json)：官方exec-7a21f351 exit0，10:15:32实际4SELECT、2DROP、0CREATE/业务DML/HTTP/服务动作。前后核两个既存函数唯一索引的精确依赖列集合、3行、POS=-1、UNIQUE/FUNCTION-BASED NORMAL；原两约束已撤，S6选定投影不变。两CREATE已在此前返回，不重放。四DDL分两次执行，不是原子事务；非空唯一语义由原固定表达式及已验真实owned DM样本支持。
- 新warm窗10:22:04.998–10:37:04.998仅消耗剩余4GET，总GET限17103及非GET预算未增加，原过期窗不改。首次默认文件权限执行失败原样保留；原OS errno未取得，不冒具体错误或自动审批拒绝。随后同脚本/输入/范围正常文件权限升级交接成功，0Native HTTP、0Atlas重启；新私有RAW hash在此次接合时计算，不冒旧ACK时已存在。

## 修复与验证限度

后端最终15路径：12 Java＋3 SQL；前端11路径：7产品＋4检查。核心包含持久意图、一次claim、ACK先留、版本/当前权限核对、本地绑定CAS、typed最小字段GET，以及DM nullable唯一约束兼容。说明编码只接受无 `< & >` 的纯文本与单层数值实体逐码点等价，CREATE耐久201与GET存储字符串仍严格相等；REUSE严格比较。Bearer修复传入已验证外层Subject，每次内层仍核当前权限，不放宽全局事务门。

最终后台r6 20模块offline package exit0，2648输入无漂移，`-Dmaven.test.skip=true`；不是新测试。最终前端production gate、完整tsc、Vite exit0。有限后台旧8方法及版本/Subject/已知201方法、wire5、前端URL隔离/确认/失响应/摘要刷新等按原具名分时结果接受，不累加成一次全矩阵。warm检查仅一个compound方法，旧r1失败、r2/r3通过，不计三个独立方法。

实际Atlas仍运行r5同Java包 `04b9c983482b5c54de61dc6c19e9f88a3c5c8e193c120a53de794d1736f03a05`；最终r6包 `3c39fe58804c24b882ceb8520a1196267009c12ea5688248c4c01dc5df98051b` 只新纳入外置SQL34，未为此切服务。DM33/34真实迁移与源码、构建分别记载。

精确交付须排除ProjectService原99新增行及三个前端路由文件原Spaces WIP（13加1删）；采用durable stageable副本，仅提交本节点差额。私有RAW、实际SQL行、argv、classes、JAR和大trace留本地。没有PR或合main。

## 后续门

M1.4精确Git与远端核对完成后，仅放行原Work的M1.5：Reviewer当前资格、最小条件更新、共享影响与独立读回、目标就绪/失效诊断。不能从Glossary存在或reviewer数组非空推导就绪；不提前建模、创建Term或发布。整个M1独验后才M2.1→M2.2。M1.1已有有限本地/隔离接受和最终人工边界保持；M1.2/M1.3不重开。
