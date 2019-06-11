package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 现任职务
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class PositionAttrValue implements AttrValue {

    private final String key = "position";
    private final int order = 19;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        String a001TableSql = "SELECT A001002 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
        a001TableSql = String.format(a001TableSql, empId);
        List a001TableList = exportWordService.findBySQL(a001TableSql);

        if (a001TableList != null && a001TableList.size() > 0) {
            Map map = (Map) a001TableList.get(0);
            String depAndJob = StringUtil.obj2Str(map.get("A001002"));
            if(StringUtil.isNotEmptyOrNull(depAndJob)){
            	return depAndJob;
            }else{
            	 return "";
            }
        }
        return "";
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getTitle() {
        return "现任职务";
    }
}
