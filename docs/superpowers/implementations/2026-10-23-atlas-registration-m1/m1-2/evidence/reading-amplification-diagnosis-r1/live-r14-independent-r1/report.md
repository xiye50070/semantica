# R14真实只读链计数与耗时独验

同原task本轮真实终态SUCCEEDED/IN_SYNC，同UUID，binding4→5、credential1；两个consumer另一次检查均VERIFIED(config1/binding5/cred1)。15项资格仍NOT_VERIFIED，不作USER_ACCEPTED或建模写资格通过。

原13939条账与四拒绝逐项保留。episode终末16951/GET16935/auth8/mut16/ordinary3；随后consumer单列+4本人GET/+1 ordinary登录，最终16956/GET16939/auth8/mut17/ordinary4。object5/profile1保持，无新对象/PATCH/轮换/策略写。

Workflow本体seq13952–16951：**2999GET+1登录**；UI资格/POST的两次资料GET13950–13951另计，consumer完全独立。逐seq核到8个完整scan：主BEFORE/AFTER各2，原verifier INITIAL/FINAL各2。每scan固定8页+312实体前后GET=320，N156/P8/K30/A34；主观察确为2次，不把保留的5次verifier lifecycle删掉或漏计。

|观察|真实GET|scan数|scope wall s|Atlas管理transport s|Σproxy reserve→complete s|
|---|---:|---:|---:|---:|---:|
|BEFORE_MAIN|710|2|68.243|46.484|18.421|
|AFTER_MAIN|810|2|77.253|56.200|22.153|
|VERIFIER_INITIAL_INVENTORY|640|2|64.569|46.070|17.916|
|VERIFIER_PERMISSION|99|0|10.751|7.178|2.818|
|VERIFIER_ORDINARY_GET|31|0|3.676|未单测|0.866|
|VERIFIER_FINAL_INVENTORY|640|2|66.664|48.222|18.666|
|VERIFIER_FINAL_PARENTS|68|0|7.259|5.106|1.983|

主两观察710/810 GET、68.243/77.253秒，均在180秒内。七lifecycle全valid、所有stage failed0；scope wall合计298.415秒，workflow首个native reserve至终末native complete 299.161秒。后一个不是完整UI请求耗时。

计时边界：transport包含本地proxy和OM往返，不能称纯OM；ordinary31GET未被管理transport计数器单测，表中留未知。proxy区间包含reserve落盘而不含pre-capacity及completion落盘，不能直接当OM耗时。各cost stage为嵌套inclusive，不能相加分摊；纯OM、本地guard、proxy encode/fsync与调度细分未知。旧真实R12在第四观察停止，7044旧完整GET只来自已核隔离baseline，未冒旧现场完成。

本审仅RAM聚合既有账和固定日志安全计数，未发HTTP/SQL、操作浏览器/服务/信号或新许可；无raw正文/凭据复制。
