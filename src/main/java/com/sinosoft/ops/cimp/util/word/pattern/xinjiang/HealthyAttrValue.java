package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 健康状况
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class HealthyAttrValue implements AttrValue {

    private final String key = "healthy";
    private final int order = 8;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String healthyInfoSql = "SELECT A001040_A FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
        if (a001Map != null) {
            String healthyCode = StringUtil.obj2Str(a001Map.get("A001040_A"));
            if (StringUtils.isEmpty(healthyCode)) {
                healthyCode = this.getHealthyCodeStr(healthyInfoSql, empId, exportWordService);
            }
            String healthyValue = "";
            if (StringUtils.isNotEmpty(healthyCode)) {
                healthyValue = CodeTranslateUtil.codeToName("BT0015", healthyCode, exportWordService) ;
            }
            return healthyValue;
        } else {
            String healthyCode = this.getHealthyCodeStr(healthyInfoSql, empId, exportWordService);
            String healthyValue = "";
            if (StringUtils.isNotEmpty(healthyCode)) {
               healthyValue = CodeTranslateUtil.codeToName("BT0015", healthyCode, exportWordService) ;
            }
            return healthyValue;
        }
    }

    private String getHealthyCodeStr(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A001040_A"));
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
        return "健康状况";
    }
}
