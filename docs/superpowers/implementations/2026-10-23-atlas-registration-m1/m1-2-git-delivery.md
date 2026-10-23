# M1.2 精确 Git 交付

Root 技术验收见 [m1-2-root-acceptance.md](m1-2-root-acceptance.md)，唯一节点进度见计划 §17。真人最终验收仍 PENDING。

2026-10-23 实际代码提交及远端核对：

| 仓库／分支 | 已接受 M1.1 依赖 | M1.2 | 精确路径数 |
| --- | --- | --- | --- |
| atlas-cloud / codex/atlas-om-semantica | ba545a88546701030b48f222b9c6d0489ce541ff | f6e43062dc845192064bf8cf3fd3db757eebb773 | 13，再44 |
| atlas-cloud-ui-react / codex/atlas-om-semantica | 229e09e75fd817e6ad52777f917b6348890630cf | 0a82bc19364bb4ede8e3b5f56b4c9f1fe5520a2a | 13，再15 |

两次 push 均 exit0，随后独立 ls-remote 与两仓 M1.2 HEAD 精确相同。前端一并推送已存在、已由用户授权的旧组件规范清理提交2d1af4ec；未把它算入M1.2的15路径。后端从原6aae49c前进，前端远端从a2a0bc2d前进。

M1.1此前已独验而尚未提交，M1.2直接依赖其准备页/API，所以先独立提交前序基线。四份提交均用精确缓存区补丁，逐项核对blob及提交路径；工作树81个相关源字节保持。更早Spaces差额在后端两个共享源、前端三个路由中留作未提交WIP，其它未提交工作也保持。未reset/stash/clean，未修改运行包、服务或业务状态，未新增测试执行。

文档以Semantica远端main的e12eec40a1fd1e4f4b17dad90c608e418b0fe100为父版本，用独立`codex/atlas-registration-m1-docs`分支交付。本地main315216c和工作树不切换；本地已有347个未推送历史提交不随本次发布。文档只含54个已读公开文件及本说明，合55路径：PRD、唯一计划、合同与验收卡、Root接受报告、关键独审与有限原结果、两项新增隔离样例。

此为精选公开交付，不声称完整E档案已推送。其余原始失败/trace、执行argv与classpath、SQL/数据库tuple、构建输入与class清单以及私有session/secret均保留原本地位置，未递归加入Git。文档中的原件链接用于定位，部分只在当前工作区可访问；不删除或回填旧原件。代码仓内原P1测试与XML按已有版本复用。

Git代码回退应按M1.2、必要时M1.1逆序revert相应提交，先核后续依赖及部署兼容；不是恢复真实数据库或原生身份副作用。原任务/ACK/UNKNOWN/已轮换凭据不得靠代码回退重放或删除。

文档远端完成事实由唯一账下一条前向登记；这份说明生成时尚未将准备稿当成文档推送成功。


实际文档交付补记：55路径提交`4a3e0489ddb8df99eef9572ca6ff56538aedac92`已push成功，独立ls-remote精确同值。唯一账§17.43据此放行原Work的M1.3；本补记与该节另作两路径完成回执提交。文档主分支、本地main与其既有347提交保持，本节点未创建PR或合入远端main。
