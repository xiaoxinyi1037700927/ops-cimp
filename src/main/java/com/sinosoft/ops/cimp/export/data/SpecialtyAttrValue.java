package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

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
public class SpecialtyAttrValue implements AttrValue {

    private final String key = "specialty";
    private final int order = 10;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            //A01087
            Map a01Map = (Map) attrValueContext.get("A01");
            final String specialtyInfoSql = "SELECT A01087 FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
            if (a01Map != null) {
                String specialtyStr = StringUtil.obj2Str(a01Map.get("A01087"));
                if (StringUtils.isEmpty(specialtyStr)) {
                    specialtyStr = this.getSpecialtyStr(specialtyInfoSql, empId);
                }
                return specialtyStr;
            } else {
                return this.getSpecialtyStr(specialtyInfoSql, empId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[A01087专长]");
        }
    }

    private String getSpecialtyStr(String sql, String empId) throws Exception {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = ExportConstant.exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A01087"));
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
}
