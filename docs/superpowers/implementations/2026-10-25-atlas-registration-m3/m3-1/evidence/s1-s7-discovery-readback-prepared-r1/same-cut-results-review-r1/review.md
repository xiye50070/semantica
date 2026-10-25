# S1/S7 真实发现与选表同cut只读核对

两份Main实际DM回读均exit0、stderr为空，每来源仅1固定SELECT。原spec、浏览器完整可用AX、DM同一discovery/key、结构指纹、两版选表全部一致。独立Python canonical(databases)重算两hash相等，原sourceSnapshotJSON与类型化对象整值相等。未追加任何SQL/网络/私有读取/产品写。

S1：28表254列，28PK/26FK/23UNIQUE/25CHECK；v1只有reg_equity_holding，v2为7个fin_*加原表，共8表。两条指向reg_legal_entity的未选FK均仍显示，没有自动补选；预期不存在表仍按缺项报告。

S7：3表13列，3PK/2FK；v1只有sales_order，v2为payment、sales_order。sales_order→customer仍为未选依赖，没有自动补选；预期缺项仍保留。

Atlas请求actor=1（admin）；OM选择器104/config6为浏览器可见上下文，未将其冒为DM actor或独立存储资格证明。coverage/allPhysicalMetadataVerified=false、capture未读、配置当前性未独证等边界保持。原首个未接受/no-change AX保留，未计成功；没有声称像素验收、OM登记、正式查询或最终真人通过。
