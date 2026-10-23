# 本次85027退出：有界只读诊断

专属process.log和原logging.file本次85027启动段均出现正常Started（本地21:37:01.368）；本段未见ERROR/Exception、ApplicationFailed、ShutdownHook或Tomcat停止标记；专属process.log无WARN，共用logging.file本段有1条WARN（安全投影只列位置/类/分类，不把它当退出异常）。完整日志只在RAM读取，没有复制正文或输出凭据。应用日志没有记录本次exit code或signal。

当前切换器Popen没有stdin=DEVNULL，也没有start_new_session=True，因而默认继承父stdin/session/process group。这是确定源码差额。原process.json只保存pid/createTime/newJarSHA，没有PGID/SID/PPID或exit状态；PID已不存在，不能事后从这些原件证明其实际PGID或死亡信号。父工具生命周期清理是合理怀疑，尚非已证根因。候选Bootstrap只正常app.run+一次AppReady listener，没有System.exit/context.close；当前本次日志也没有对应应用异常退出证据。

最小后续正常持久运行方案：先以Root终末SELECT确认本次原task/credential效果，再由Root具名决定一次普通main启动。使用已经完成的同一三源新Jar，将原保存的普通-jar argv只替换jar路径；所有原JVM/app配置与三个scanner=false不变。不要使用PropertiesLauncher、loader.main、loader.path、外置Bootstrap/Runner或原once许可，绝不再派密码/新任务。Popen至少使用stdin=subprocess.DEVNULL、start_new_session=True、close_fds=True、stdout私有0600日志、stderr=STDOUT；保存新pid/createTime/ppid/pgid/sid与实际argv hash，再观察正常监听及父执行器返回后仍存活。这里的观察不等于重新验收native效果。

本报告没有生成或执行新启动器，没有SQL/HTTP/认证/信号/进程启动，也未改业务状态、旧失败或日志。
