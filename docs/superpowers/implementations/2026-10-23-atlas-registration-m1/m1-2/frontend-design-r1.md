# M1.2 前端实施切口（合同对齐中）

采用 Aham8.0/pc浅色、既有React19/AntD6和 M3AContentScope，不修改全局主题或旧资产授权页。页面 `/next/ontology/projects/:projectId/modeling-access`，由就绪页“身份与操作资格”进入，保留返回当前就绪页出口。

**页型：** 设置导航＋单据式整体编辑。连续白面，分区留白，不套状态卡片。显示态页头“配置身份”，编辑态固定实际滚动底栏“取消 / 确认配置”；人员和批准实例均从当前授权目录选择。精确 tenant/space/project scope、稳定原生 UUID、绑定与凭据版本均只读，不提供任意actor/scope/credentialRef/路径/URL输入。人员/实例变化退役旧结果。

**事实分层：** 受管身份映射、原生同步、当前真实消费者只读资格分列；只读检查不授予写权。本节点始终明确“建模写资格未通过”，不显示配置完成即原生建模可写。目标、Glossary、Reviewer、权限策略同步仍为后续节点。

**生命周期：** 配置/同步/轮换/禁用只用后端精确意图与当前版本。待核对只能按原意图读取，不新键开户。受理后可离开，旧结果与UNKNOWN不抹除。请求写前、返回后均用既有session/context/project owner门，缓存键含人员/实例/配置版本。失去401/403授权时撤下该范围数据与动作。

**目录与权限：** 分页cursor默认20/上限100，允许继续加载，不以首屏冒完整目录。ADMIN配置；普通USER仅当前本人有权身份/资格只读，不请求管理人员目录或草稿。最终请求/DTO将绑定后端实际合同，当前不假造API实装。

**验证安排：** 新API严格DTO/精确cut/未知字段与秘密字段拒绝；组件验证目录空/加载错、UNKNOWN、当前角色、配置变更晚到隔离、版本冲突和读回。必要隔离浏览器沿5297strictPort、SWblock、trace:on、五proxy9与origin/method/path/query白名单；执行前通知Root具体命令。真实外部身份副作用由本节点另列有限执行卡，当前未执行。

前态：`evidence/frontend-opening-r1/manifest.json`，22个当前文件；M1.1验收13文件逐byte相等。M1.1所有封存包不改，唯一进度账由Root维护。
