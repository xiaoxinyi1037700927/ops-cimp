package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 * 性别
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class GenderAttrValue implements AttrValue {

    private final String key = "gender";

    private final int order = 1;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String genderInfoSql = "SELECT A001010 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";

        if (a001Map != null) {
            String genderCode = StringUtil.obj2Str(a001Map.get("A001010"));
            if (StringUtils.isNotEmpty(genderCode)) {
                genderCode = this.getGenderStr(genderInfoSql, empId, genderCode,exportWordService);
            } else {
                return this.getGenderStr(genderInfoSql, empId, null,exportWordService);
            }
            return genderCode;
        } else {
            return this.getGenderStr(genderInfoSql, empId, null,exportWordService);
        }
    }

    private String getGenderStr(String sql, String empId, String genderCode,ExportService exportWordService){
        String genderInfoSql = String.format(sql, empId);
        if (StringUtils.isNotEmpty(genderCode)) {
        	return CodeTranslateUtil.codeToName("BT0001", genderCode, exportWordService);
        } else {
            List genderInfoList = exportWordService.findBySQL(genderInfoSql);
            if (genderInfoList != null && genderInfoList.size() > 0) {
                Map genderMap = (Map) genderInfoList.get(0);
                if (genderMap != null) {
                    String gender_code = StringUtil.obj2Str(genderMap.get("A001010"));
                    return CodeTranslateUtil.codeToName("BT0001", gender_code, exportWordService);
                }
            }
        }

        return "";
    }

    @Override
    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }
    
    @Override
    public String getTitle() {
        return "性别";
    }    
}
