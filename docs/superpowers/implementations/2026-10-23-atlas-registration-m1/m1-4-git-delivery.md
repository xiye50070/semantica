# M1.4 精确 Git 交付

Root于2026-10-24 10:47 UTC完成[有限技术验收](m1-4-root-acceptance.md)。下列两仓代码已实际提交、push成功，并用独立 `ls-remote` 核对远端相同；工作区文件字节及既有WIP保持。公开精选文档也已实际提交、push并核对远端；本回执仅补记已发生的交付。真人最终验收仍PENDING。

| 仓库 / 分支 | 本节点父提交 | M1.4提交及已核远端 | 精确范围 |
| --- | --- | --- | --- |
| atlas-cloud / codex/atlas-om-semantica | `28159a9ad05809e0b3b0d36df38eae01472f88bf` | `2e6f0bcd348706698c12a6a8a83623b9ea611a8a` | 15路径：12 Java、3 SQL |
| atlas-ui-admin-react / codex/atlas-om-semantica | `9d526d1383d751709a7bb4c132e7646472ff3d71` | `bd8ac636d8024724565a976ba815dbacd25ea3d6` | 11路径：7产品、4检查 |
| Semantica / codex/atlas-registration-m1-docs | `e16447c51bbfd46e7158fcd27c7ad3b282e035fb` | `7ad55d1fa0d618c754cd3758ba0eeb6c30940b4d` | 仅节点验收、唯一账、安全投影及必要有限方法源 |

## 精确暂存及保护

提交前HEAD、分支、远端父提交与空index均核同。后端采用 `backend-delivery-15-r1/durable-stageable-source-map-15-r1.json`，前端采用 `frontend-delivery-11-r1/durable-stageable-source-map-11.json` 的已验字节；26份当前源码与map核同，26个提交blob与stageable字节核同，提交路径集合精确等于登记范围，`git diff --cached --check`通过。

ProjectService只提交M1.4增量，原99新增行保留未提交；前端App、standaloneRoutes、routes.test保留原Spaces WIP（合计13加1删）。其余后端2个、前端13个既有tracked WIP路径不入本节点，提交前后差额及文件字节保持。连同混合文件，后端原3个、前端原16个WIP继续保留。没有reset/clean/stash、整仓暂存或提交整个evidence。

临时Git回执在 `/private/tmp/m14-root-git-backend-actual.json` 与 `/private/tmp/m14-root-git-frontend-actual.json`，状态均 `PUSHED_REMOTE_VERIFIED`。这些用于本机操作核对，不另递归归档。

## 验证与公开材料边界

后台最后r6构建10:08:41.925894–10:08:58.338465 UTC，2648输入、20模块offline package退出0，sourceDrift为空；使用skiptests。产物SHA256 `3c39fe58804c24b882ceb8520a1196267009c12ea5688248c4c01dc5df98051b`，178507927B；stdout SHA256 `7f0e7d34397a429fa4e128bcfecf5aaeb825f1ba44f8eb5906e719dc140dd606`/40957B，stderr0。运行r5 Java相同，r6仅纳入外置SQL34，未为此重启。有限方法与真实正向分层见Root验收报告，Git交付没有重跑产品验证。

公开精选保留验收卡、交接说明、安全投影、真实JPG及必要有限测试源/具名结果说明。原件链接未随精选进入Git的仍是本地证据引用，不能从链接存在推导全档案已公开。尤其两份delivery proof、完整构建/执行argv、实际SQL行、私有RAW、会话资料、classes/JAR和大trace保持本地；不递归收齐所有引用。

Semantica使用独立index从专用文档分支父提交构树；本地main `315216c6808c99542ac912a0a2f26bef7b988346`、真实index与其它工作文件不切换、不重置。不得把本地main的347个无关提交推送出去。没有创建PR或合并main。公开精选25文件已经push并独立核远端 `7ad55d1fa0d618c754cd3758ba0eeb6c30940b4d`，本地main和真实index逐字保持。初次文档交付回执为 `/private/tmp/m14-root-docs-initial-actual.json`。本文件和唯一账的完成说明作为两个路径的后续回执提交，不递归增加档案；该回执提交的实际哈希在工具结果与监督检查点记录。M1.4的代码及文档交付门已完成，仅放行原Work的M1.5。
