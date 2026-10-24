# M1.3 精确Git交付

本节点已获[Root有限技术接受](m1-3-root-acceptance.md)，真人最终验收PENDING。2026-10-24本轮仅按[最终30路径](m1-3/evidence/phase2-final-ui-readback-r1/final-product-source-cut.json)提交产品文件。

| 仓库 / 分支 | 父提交 | 本节点提交 | 实际远端核对 |
| --- | --- | --- | --- |
| atlas-cloud / codex/atlas-om-semantica | f6e43062dc845192064bf8cf3fd3db757eebb773 | 28159a9ad05809e0b3b0d36df38eae01472f88bf | push成功，ls-remote同提交；27文件 |
| atlas-ui-admin-react / codex/atlas-om-semantica | 0a82bc19364bb4ede8e3b5f56b4c9f1fe5520a2a | 9d526d1383d751709a7bb4c132e7646472ff3d71 | push成功，ls-remote同提交；3文件 |

提交前各仓HEAD和远端均为上表父提交、index为空；仅命名路径暂存，staged路径集合及blob SHA与已验30源逐项匹配。提交后精确diff集合匹配，Cloud3个、React16个其它tracked WIP内容保持，未add整仓、reset、clean或stash。没有PR或合main。原临时Git实际结果保留于/private/tmp/m13-root-git-{backend,frontend}-actual.json，不另递归归档。

Semantica公开文档正在从原文档分支d6bf25bd3760831b7da0da313b56dd475da74f82精确交付；本地main315216c6808c99542ac912a0a2f26bef7b988346及347个无关既有提交保持。使用独立index构造精选树，保留分支既有文件；不改当前工作区/index或推本地main。公开选择仅接受报告、操作卡、原结果安全投影、必要有限检查和Native单源覆盖源码/补丁/构建说明，完整私有原件和大体积运行材料仍在本地。

文档内引用的部分原始证据仅在本地工作区可查；精选公开交付不承诺递归收齐每一条引用。Native单源编译依赖原部署classpath与旧覆盖包，公开源码/补丁可审，非离开原环境的一键重放包。classes、依赖JAR、RAW、SQL实际行、argv及session秘密不提交。旧失败/UNKNOWN不因Git而改写，原one-shot不得再次执行。

联调卡及Native构建README保留历史准备截面，其“未执行”不作为当前状态；当前结果以Root接受报告及终末原件为准。交接卡中22/78为先前校正截面，最后04:41:52的Work工具解析记录及本轮Root实时检查均为23/77；不改写旧原件。

文档远端核同后才放行同一Work进入M1.4；完整M1、M2及最终人工验收未在本节点完成。源码回退不撤销已发生业务效果，按Root接受报告保留任务、ACK、原对象及审计。
