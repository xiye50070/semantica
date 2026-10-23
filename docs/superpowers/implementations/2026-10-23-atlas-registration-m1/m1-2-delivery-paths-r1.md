# M1.2 准确产品与仓内检查路径（交付范围核对）

本表仅合并既有开工前态与登记，**不是新的进度账、安装方案或提交动作**。共 **59个路径**：`atlas-cloud`44（后端Java/SQL/SDK38＋Worker/Python SDK6），`atlas-ui-admin-react`15；`semantica`本节点只承载计划、合同与E证据，没有本节点永久产品源。后续B2新增隔离样例仍在E，单列证据，不伪装生产路径。当前M1.2未ROOT_ACCEPTED、未Git交付、M1.3 NOT_GO。

以下路径均相对各自仓库，保持真实大小写；`E`=`m1-2/evidence`，相对本文目录。开工记录为[m1-2/backend-opening-r1/opening.json](m1-2/backend-opening-r1/opening.json)，包含三仓HEAD及旧status：后端6aae49c、前端2d1af4e、Semantica315216c。未新增全树hash或重新构建。

## atlas-cloud：后端38（36产品＋2仓内检查）

首批已有22路径完整清单：[manifest](m1-2/evidence/backend-candidate-r3/manifest.json)，其实际安装核对是`E/root-backend-fixed-readback-r1/report.json`的installation；不存在`backend-source-install-r1/manifest.json`。后续增量须按下面去重，不能把最后3个修复源或2627个全量build输入冒整节点diff。

### B1：首批新增 Java 产品，9条

新增；`E/backend-candidate-r3/manifest.json` 各项 `beforeSHA256=null`（首批22路径/原r1前态）。

```text
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/JdbcOntologyModelingAccessStore.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingAccessConfiguration.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingAccessContracts.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingAccessController.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingAccessExceptionAdvice.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingAccessService.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingIdentityConsumer.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingIdentityInternalController.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingTaskDispatcher.java
```

### B2：后续新增 Java 产品，6条

本节点新增；分别引用下方 B2 对照表。晚期source-before是节点内部中间态，不是M1.2开工前。

```text
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityModelingProvisionContinuation.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityModelingProfileContinuation.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityProfileNotForwardedCertificate.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityModelingProfileVerification.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityModelingProfileVerificationResume.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityModelingConfirmedReadonlyEpisode.java
```

### B3：开工已存在 Java 产品，8条

`m1-2/backend-source-before-r1/manifest.json`及`copies/<相对路径>`；前态等开工HEAD blob。

```text
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyOmIdentityDispatchGuard.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyOmIdentityRuntimeConfiguration.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyWorkerClient.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/om/OmConfiguration.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/om/OmInstanceRegistry.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/om/OmRestClient.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmOrdinarySessionBroker.java
atlas-module-infra/atlas-module-infra-api/src/main/java/cn/iocoder/atlas/module/infra/api/omidentity/OmIdentityJdbcLedger.java
```

### B4：首批追加登记的既有 Java 产品，2条

`m1-2/backend-source-before-r2/manifest.json`及同目录`<相对路径>`；前态等开工HEAD blob。

```text
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/JdbcOmIdentityTaskStore.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityBackgroundExecutor.java
```

### B5：后续首次改动的既有产品，9条

最早before引用见下方 B5 对照表；9个前态逐项等开工HEAD blob，其中最后1个为Java SDK。

```text
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/OntologyWorkerConfiguration.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/NativeOmIdentityClient.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/JdbcOmIdentityNativeJournal.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityTaskProcessor.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityNativeLeaseScope.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/OmIdentityNativeWorkflow.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/NativeOmSourceObservationBudget.java
atlas-module-infra/atlas-module-infra-server/src/main/java/cn/iocoder/atlas/module/infra/ontology/omidentity/NativeOmResourceInventory.java
integrations/om-identity/native/NativeTagSourceSemantics.java
```

### B6：新增 SQL 产品及仓内检查，4条

SQL2和AccessTest前态absent见首22manifest；SchedulerSwitchTest前态absent见`E/worker-scheduler-switch-r1/manifest.json`。SQL30为M1.1，不列本节点新增。

```text
sql/ontology/V20261023_31__ontology_modeling_access.sql
sql/ontology/dm8/V20261023_31__ontology_modeling_access.sql
atlas-module-infra/atlas-module-infra-server/src/test/java/cn/iocoder/atlas/module/infra/ontology/OntologyModelingAccessTest.java
atlas-module-infra/atlas-module-infra-server/src/test/java/cn/iocoder/atlas/module/infra/ontology/OntologyWorkerSchedulerSwitchTest.java
```

B2新增源对应既有登记（顺序与上述6条一致）：

1. `E/modeling-provision-continuation-candidate-r4/manifest.json`
2. `E/modeling-profile-single-step-recovery-candidate-r5/manifest-final.json`（同时包含第2/3源）
3. 同上。
4. `E/modeling-confirmed-profile-verification-candidate-r3/manifest-source.json`
5. `E/modeling-confirmed-profile-verification-resume-candidate-r5/manifest-source.json`
6. `E/confirmed-profile-readonly-episode-candidate-r1/manifest-source.json`

B5最早前态对应：

- WorkerConfiguration：`E/worker-scheduler-switch-r1/manifest.json`与`before/<相对路径>`。
- NativeOmIdentityClient：`E/http1-native-transport-repair-r1/source-manifest.json.before`；这是Java HTTP1的37B修复，不是Python SDK。
- Journal、Processor、LeaseScope、Workflow：`E/modeling-profile-single-step-recovery-candidate-r1/source-before.json`与`before/<相对路径>`。
- ObservationBudget与NativeTagSourceSemantics：`E/confirmed-profile-observation-180-candidate-r1/manifest-source.json`与`source-before/<相对路径>`。
- ResourceInventory：`E/reading-amplification-diagnosis-r1/minimal-fix/candidate/manifest.json`与`source-before/<相对路径>`。

**后端混合前态：** 19个旧产品的上述最早before均已由只读复核与开工HEAD blob逐项相等，本次38路径不混入开工6个tracked WIP。必须排除：OntologyP1Configuration.java、OntologyProjectContracts.java、OntologyProjectController.java、OntologyProjectService.java、OntologyProjectApiTest.java、p1-test-schema.sql，它们已有M1.1/早期修改；只保存在before的OmIdentityRuntimeRegistry也不是本次改动。新增19=15main＋2SQL＋2test，在开工HEAD均无blob，不把晚期备份认成节点前就存在。

## atlas-cloud：Worker与共享Python SDK6（5产品＋1仓内检查）

已有完整[六路径manifest](m1-2/evidence/worker-candidate-r1/manifest.json)和[before manifest](m1-2/worker-source-before-r1/manifest.json)，前态原件在后者同目录`copies/<相对路径>`，实际安装`E/worker-install-r1/receipt.json`。前3产品及native_client四个旧文件前态等Git HEAD；modeling_identity与test_modeling_identity本节点开始前absent。全部6当前仍等原安装cut；没有后续Python漂移。

```text
integrations/ontology-worker/src/atlas_ontology_worker/deployment.py
integrations/ontology-worker/src/atlas_ontology_worker/runtime.py
integrations/ontology-worker/src/atlas_ontology_worker/http.py
integrations/ontology-worker/src/atlas_ontology_worker/modeling_identity.py
integrations/ontology-om-contracts/native_client.py
integrations/ontology-worker/tests/test_modeling_identity.py
```

最后1条为新增检查，其余5为产品。复用但**未改、不应列新增交付**的检查：`integrations/ontology-worker/tests/test_deployment.py`、`integrations/ontology-worker/tests/test_runtime.py`、`integrations/ontology-om-contracts/tests/test_native_client.py`，见`E/worker-local-verification-r1/report.json`。旧4产品是增量修改，不能整文件称为新实现。

## atlas-ui-admin-react：15（12产品＋3仓内检查）

首10路径完整清单[manifest](m1-2/evidence/frontend-source-cut-r1/manifest.json)；4个旧文件的M1.2准确before是[opening manifest](m1-2/evidence/frontend-opening-r1/manifest.json)及`files/<相对路径>`，opening明确all13AcceptedBytesStillEqual（前序M1.1接受cut）。前10最后3条分别为API检查、E2E、Playwright检查配置；后续5条为新增产品API。

```text
apps/console-react/src/App.tsx
apps/console-react/src/platform/routes.ts
apps/console-react/src/platform/standaloneRoutes.ts
apps/console-react/src/features/ontology/OntologyProjectReadinessPage.tsx
apps/console-react/src/features/ontology/OntologyModelingAccessPage.tsx
apps/console-react/src/features/ontology/modelingAccessApi.ts
apps/console-react/src/features/ontology/modelingAccess.css
apps/console-react/src/features/ontology/modelingAccessApi.test.ts
apps/console-react/tests/e2e/ontology-modeling-access.spec.ts
apps/console-react/playwright.modeling-access.config.ts
apps/console-react/src/features/ontology/modelingProvisionPreflightApi.ts
apps/console-react/src/features/ontology/modelingProfileContinuationApi.ts
apps/console-react/src/features/ontology/modelingProfileVerificationApi.ts
apps/console-react/src/features/ontology/modelingProfileVerificationResumeApi.ts
apps/console-react/src/features/ontology/modelingConfirmedReadonlyEpisodeApi.ts
```

后5 API最早新增/absent及前向差额，按上列顺序引用：

- `E/frontend-provision-preflight-continuation-candidate-r1/{manifest.json,source-proof.json,forward.patch}`。
- `E/frontend-original-profile-continuation-candidate-r1/{manifest.json,source-proof.json,forward.patch}`（newProductAPIAbsentAtSeal=true）。
- `E/frontend-confirmed-profile-verification-candidate-r1/installation-bindings.json`（expectedBefore=ABSENT）。
- `E/frontend-stopped-profile-readonly-continuation-candidate-r1/installation-bindings.json`（expectedBefore=ABSENT）。
- `E/frontend-confirmed-readonly-episode-candidate-r1/installation-bindings.json`（products[].expectedBefore=null）。

**前端4个混合文件必须显式拆清：** App.tsx、routes.ts、standaloneRoutes.ts已有M1.1/更早改动；OntologyProjectReadinessPage.tsx在M1.1新增、现仍untracked，本节点只加建模权限入口。不能把Git HEAD→当前的4个整文件diff都算M1.2；以M1.2 opening→本节点current差额识别，并由Root处理前序已验M1.1的独立交付基线。尤其untracked不代表M1.2新增，准备页必须归M1.1依赖＋M1.2修改。

首manifest明确只改上述4个旧文件、新增6；其18个opening保护文件保持。其中http.ts（早期可选请求参数）、OntologyProjectEditor/ProjectsPage、projectReadinessApi及其测试/CSS、OntologyProjectScope、Models/Members/AssetGrants、ontologyApi、全局tokens/session helpers等都不是M1.2路径。`.gitignore`、shared/route-registry.ts及空间/来源/查询/导航旧WIP不得因工作区脏或路由同目录而带入本次。

## E-only检查和辅具的边界

以下仅定位既有具名检查，**不在59个永久产品/仓内检查路径计数内**，不据此重新生成全E索引：

- 初始后端隔离以首22manifest和`E/root-backend-fixed-readback-r1/report.json`所引作者索引为准；后续检查由各候选manifest的tests/payloads/evidence字段登记。
- 前端原任务、首派、资料、只读verification、resume、episode有限样例位于各`frontend-*-candidate-r1`目录的`*.test.tsx`与`vitest.config.ts`；都是E内，未安装到产品树。新增B2唯一入口`E/frontend-b2-cancel-escape-current-page-r1/cancel-escape.test.tsx`直接导入当前实际Page及API；点击取消/Escape参数化2/2通过，原始结果`actual-r1/{receipt.json,results.json,stdout,stderr}`，0产品改动/真实HTTP/SQL/浏览器，现场项仍PENDING，不继承旧候选Page结果。
- 已充分6/9分别见`E/modeling-rotation-integration-candidate-r3/manifest-source.json`与`E/d4-original-rotation-once-runner-candidate-r4/manifest.json`；E-only Bootstrap/Runner及6个外部class已从当前普通运行卸除，不算永久产品入口/恢复框架。
- 私有session/secret/启动argv、SQL真实状态、代理账与运行产物不在产品提交路径里；公共安全证据与历史失败按既有目录保留，不复制私有材料。


D2新增唯一E-only反例为`E/d2-unmapped-consumer-current-r1/tests/cn/iocoder/atlas/module/infra/ontology/D2UnmappedConsumerTest.java`中`authorizedExactScopeUnmappedPersonCannotStartEitherReadonlyConsumer`；Runner仅selectMethod该1例，compile/test均0、1/1 PASS。精确结果`actual-r1/{actual.json,test-result.json}`及`production-class-proof.json`，当前生产源不变，不增上述59永久路径。同名陌生账号不认领复用旧P1的`OmIdentityNativeWorkflowTest.sameNameForeignUserIsConflictBeforeAnyNativeWrite`及`verification-placement-b96-20261005/.../OmIdentityNativeWorkflowTest.xml`，准确完整位置见验收卡末节，无新真实冲突或重跑旧36例。
