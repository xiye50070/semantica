# M1.5 精确 Git 交付

2026-10-24 14:31 UTC：产品两仓已实际提交、推送并独立核对远端。M1.5已有限技术ROOT_ACCEPTED；文档精选已实际交付，回执见下文。真人最终PENDING，不冒USER_ACCEPTED。

| 仓库/分支 | 父提交 → 本次提交 | 精确范围 |
| --- | --- | --- |
| atlas-cloud / codex/atlas-om-semantica | `2e6f0bcd348706698c12a6a8a83623b9ea611a8a` → `6090e9a3ec00ae0db202ceb6b6ac1017dc3598ab` | 14路径：12 Java＋2 SQL |
| atlas-ui-admin-react / codex/atlas-om-semantica | `bd8ac636d8024724565a976ba815dbacd25ea3d6` → `5b0780eae5198de33fe6f9afcc35bfb4b7308e07` | 7路径：5产品＋2检查 |

两仓push成功，ls-remote分别与本次提交一致。提交blob逐项匹配Root已核source-map和末导航两源；实际工作区字节保持，index原空/提交后空。Cloud3、React16既有tracked WIP未变、未夹带；没有PR、合main或全仓暂存。

前端末导航已包含在本次提交中。实际2 PASS/7 skipped与完整production gate/tsc/Vite0已独核；后台充分构建及具名有限方法复用，Git后未重建、重测或发Native调用。

文档仅选择验收卡、合同、交接、人工清单、必要源map/有限结果、安全投影与3张现场PNG。完整本地evidence-index、递归执行symlink、argv、原始SQL返回行、RAW/会话、classes/JAR、大trace不提交。未公开的本地链接不递归补齐，不把公开精选冒成独立可执行完整环境。

文档分支沿 `codex/atlas-registration-m1-docs` 的 `cda1cf6884285e2adf6df451f92dc9069dfb4520` 前进，采用独立index/read-tree/commit-tree；保本地main `315216c6808c99542ac912a0a2f26bef7b988346`、真实index及347个无关提交，禁止直接推本地main。

操作临时回执保于 `/private/tmp/m15-root-git-backend-actual.json` 和 `/private/tmp/m15-root-git-frontend-actual.json`，不另归档。完整验证边界见[Root验收](m1-5-root-acceptance.md)。整M1有限独验见[m1-root-acceptance.md](m1-root-acceptance.md)；本回执、整M1报告及唯一账一并推送完成后只放行M2.1。

文档首次预提交在diff --check停止：3份原始AX中5行列表标记含原始尾空格，尚未生成commit或更新分支。原失败临时回执保留；续接只豁免4份具名AX快照的空白检查，其他文件仍严格检查，所有选定原件字节不改。此为格式检查失败，不是自动审批拒绝。

## 文档实际交付

25项精选已由独立index提交 `e912667f6295b04ba32d3f3c0f54432d7749b866`，父为 `cda1cf6884285e2adf6df451f92dc9069dfb4520`；push成功，ls-remote精确同。本地main、实际index、所选工作区字节均保持。原首次格式检查失败回执留存，成功回执为 `/private/tmp/m15-root-docs-initial-r2-actual.json`。

本报告、整M1有限验收与唯一账形成后续3文回执提交；其精确commit/remote写入工具实际回执 `/private/tmp/m15-root-docs-receipt-actual.json` 和自动化检查点，不在自身内容递归嵌入自身commit哈希。没有另建历史账或推送本地main。
