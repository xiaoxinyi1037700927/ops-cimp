package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class GenderAttrValue implements AttrValue {

    public static final String KEY = "gender";

    public static final int ORDER = 1;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            Map a01Map = (Map) attrValueContext.get("A01");
            final String genderInfoSql = "SELECT A01004 FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";

            if (a01Map != null) {
                String genderCode = StringUtil.obj2Str(a01Map.get("A01004"));
                if (StringUtils.isNotEmpty(genderCode)) {
                    genderCode = this.getGenderStr(genderInfoSql, empId, genderCode);
                } else {
                    return this.getGenderStr(genderInfoSql, empId, null);
                }
                return genderCode;
            } else {
                return this.getGenderStr(genderInfoSql, empId, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A001_A01004性别]");
        }
    }

    private String getGenderStr(String sql, String empId, String genderCode) throws Exception {
        String genderInfoSql = String.format(sql, empId);
        if (StringUtils.isNotEmpty(genderCode)) {
            return CodeTranslateUtil.codeToName("GB/T2261.1-2003", genderCode, ExportConstant.exportService);
        } else {
            List genderInfoList = ExportConstant.exportService.findBySQL(genderInfoSql);
            if (genderInfoList != null && genderInfoList.size() > 0) {
                Map genderMap = (Map) genderInfoList.get(0);
                if (genderMap != null) {
                    String gender_code = StringUtil.obj2Str(genderMap.get("A01004"));
                    return CodeTranslateUtil.codeToName("GB/T2261.1-2003", gender_code, ExportConstant.exportService);
                }
            }
        }

        return "";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
