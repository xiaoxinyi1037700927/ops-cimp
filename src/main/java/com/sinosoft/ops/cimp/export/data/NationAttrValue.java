package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/29.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class NationAttrValue implements AttrValue {

    public static final int ORDER = 3;

    public static final String KEY = "nation";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            Map a01Map = (Map) attrValueContext.get("A01");
            final String nationInfoSql = "SELECT A01017 FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
            if (a01Map != null) {
                String nation = StringUtil.obj2Str(a01Map.get("A01017"));
                if (StringUtils.isNotEmpty(nation)) {
                    return this.getNationStr(nationInfoSql, empId, nation);
                } else {
                    return getNationStr(nationInfoSql, empId, null);
                }
            } else {
                return getNationStr(nationInfoSql, empId, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[民族]");
        }
    }

    private String getNationStr(String sql, String empId, String nationCode) throws Exception {
        String attrInfoSql = String.format(sql, empId);

        if (StringUtils.isNotEmpty(nationCode)) {
            return CodeTranslateUtil.codeToName("GB/T3304-1991", nationCode, ExportConstant.exportWordService);
        } else {
            List attrInfoList = ExportConstant.exportWordService.findBySQL(attrInfoSql);
            if (attrInfoList != null && attrInfoList.size() > 0) {
                Map map = (Map) attrInfoList.get(0);
                if (map != null) {
                    //GB/T3304-1991
                    String nation_code = StringUtil.obj2Str(map.get("A01017"));
                    return CodeTranslateUtil.codeToName("GB/T3304-1991", nation_code, ExportConstant.exportWordService);
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
