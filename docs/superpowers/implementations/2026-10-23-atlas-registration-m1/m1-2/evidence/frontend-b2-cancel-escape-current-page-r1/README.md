# B2 当前真实 Page：取消 / Escape 离线补证

实际进程 exit0，2/2 参数路径通过；开始 2026-10-23T14:20:37.264931+00:00，结束 2026-10-23T14:20:41.057156+00:00。本次直接导入当前仓库 Page `aa608c0d8322bdde3af575250d223dca1c5bef48fd1d367e64d847b857e25695` / 66156B 和当前 API，非旧候选Page。13 个关联产品源与测试/config/work副本的执行前后hash完全相等。只新增E/tmp测试材料，无产品写、HTTP、SQL或浏览器。

真实 AntD App + 当前 owned-confirm 生命周期未mock。分别以user-event点击取消和Escape，观察取消默认初焦，弹窗关闭；焦点回当前Page明确指定的『核对原资料续接资格』读取控件，原任务URL/标题/状态仍保留。没有在测试中调用focus、force或fake timers。本次满足保留原任务上下文这一卡项，不把它冒充精确回到『继续原资料步骤』提交按钮。

两例都硬断言0POST/prepare/confirm/profile-continuation；原pending JSON、operationKey、当前marker仍absent、四个历史marker及完整local/sessionStorage逐值不变。所有Axios接口由有限GET fixture接管，未知method/path拒绝，fetch/XHR/WS本地封锁。命令/实际exit/原stdout/stderr/JSON结果均在actual-r1。原6/9测试和完整build没有重跑。

采用 Aham8 pc浅色/当前React AntD，已读Skill入口及完整反馈、键盘、confirm-dialog/button契约。取消初焦/Esc关闭对应6.3/7.1；当前回读取控件是原Page业务上下文适配。stderr的AntD autoFocusButton弃用提示和jsdom pseudo-getComputedStyle限制完整保留，未隐藏或修改产品来消除提示。

这是源码与隔离样例补证，不能代替浏览器像素、真实接口或现场人工B2；现场B2仍PENDING。
