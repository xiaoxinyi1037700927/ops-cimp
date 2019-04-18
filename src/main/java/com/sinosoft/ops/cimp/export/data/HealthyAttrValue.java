package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class HealthyAttrValue implements AttrValue {

    public static final String KEY = "healthy";
    public static final int ORDER = 8;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            Map a01Map = (Map) attrValueContext.get("A01");
            final String healthyInfoSql = "SELECT A01028 FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
            String healthyValue = this.getHealthyCodeStr(healthyInfoSql, empId);
            return healthyValue;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A001_A01028健康状况描述]");
        }
    }

    private String getHealthyCodeStr(String sql, String empId) throws Exception {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = ExportConstant.exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A01028"));
            }
        }
        return "";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
