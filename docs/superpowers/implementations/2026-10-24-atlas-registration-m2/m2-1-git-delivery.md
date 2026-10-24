# M2.1 精确 Git 交付

状态：产品两仓 **PUSHED_REMOTE_VERIFIED**（2026-10-24 17:17 UTC）；公开文档交付进行中。M2.1 已[技术接受](m2-1-root-acceptance.md)，真人 PENDING，M2.2 待全部交付核同后放行。

| 仓库/分支 | 父提交 | 已推送提交（ls-remote同） | 路径 |
| --- | --- | --- | --- |
| atlas-cloud / codex/atlas-om-semantica | 6090e9a3ec00ae0db202ceb6b6ac1017dc3598ab | c95a5cef89198aa59861d80a1a178d8854839658 | 21，16产品/5检查 |
| atlas-ui-admin-react / codex/atlas-om-semantica | 5b0780eae5198de33fe6f9afcc35bfb4b7308e07 | ee314de2bc29a14fd6c865e6790cd892f0484446 | 13，8产品/5检查 |
| semantica / codex/atlas-registration-m1-docs | 8f60d6feabbc03810f1df1b07c5de99869fb938a | PENDING | 56精选 |

产品34提交blob逐一匹配已验cut。前端13采用HEAD-only映射，7个重叠物理文件里的旧Spaces WIP未夹带；Cloud3、React16原tracked WIP及工作区字节保持，实际index提交后为空。原ProjectService99新增行不在本次提交。没有全仓git add、测试重跑、部署或回滚。

文档采用52份固定公开精选，加索引、Root验收/本交付2文和唯一账，共56路径。使用独立index/read-tree/commit-tree，保真实index、本地main315216c6808c99542ac912a0a2f26bef7b988346及其347无关提交；不推main、不建PR或合并。

6份具名AX保持原字节，只豁免其尾空白检查，不更改内容；其它文件仍严格diff--check。公开精选允许无敏感值的Maven构建argv与本地引用，未含私有运行100参数、会话/RAW/SQL实际行/秘密/JAR/classes/大trace，不递归补齐引用。

实际产品操作回执位于 /private/tmp/m21-root-git-backend-actual.json 与 /private/tmp/m21-root-git-frontend-actual.json，留本地，不另建进度账。
