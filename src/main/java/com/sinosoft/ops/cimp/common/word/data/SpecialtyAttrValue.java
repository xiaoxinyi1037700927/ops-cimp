package com.sinosoft.ops.cimp.common.word.data;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 熟悉专业 有何专长
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
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        //A01087
        Map a01Map = (Map) attrValueContext.get("A001");
        final String specialtyInfoSql = "SELECT A105025 FROM Emp_A105 Emp_A105 WHERE EMP_ID = '%s' ORDER BY Emp_A105.A105015 ASC, Emp_A105.A105025 DESC";
        if (a01Map != null) {
            String specialtyStr = StringUtil.obj2Str(a01Map.get("A105025"));
            if (StringUtils.isEmpty(specialtyStr)) {
                specialtyStr = this.getSpecialtyStr(specialtyInfoSql, empId, exportWordService);
            }
            return specialtyStr;
        } else {
            return this.getSpecialtyStr(specialtyInfoSql, empId, exportWordService);
        }
    }

    private String getSpecialtyStr(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A105025"));
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
