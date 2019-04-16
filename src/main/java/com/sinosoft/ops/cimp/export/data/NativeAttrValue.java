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
public class NativeAttrValue implements AttrValue {

    private final int order = 4;

    private final String key = "native";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        Map a01Map = (Map) attrValueContext.get("A01");
        final String nativeInfoSql = "SELECT A01011_A, A01011_B FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
        if (a01Map != null) {
            String nativeName = StringUtil.obj2Str(a01Map.get("A01011_A"));
            String nativeCode = StringUtil.obj2Str(a01Map.get("A01011_B"));
            if (StringUtils.isNotEmpty(nativeName)) {
                return nativeName;
            }
            if (StringUtils.isEmpty(nativeName)) {
                nativeCode = this.getNativeStr(nativeInfoSql, empId);
            }
            String nativeValue = "";
            if (StringUtils.isNotEmpty(nativeCode)) {
                nativeValue = CodeTranslateUtil.codeToName("GB/T2260-2007", nativeCode, ExportConstant.exportWordService);
            }
            return nativeValue;
        } else {
            String nativeCode = getNativeStr(nativeInfoSql, empId);
            String nativeValue = "";
            if (StringUtils.isNotEmpty(nativeCode)) {
                nativeValue = CodeTranslateUtil.codeToName("GB/T2260-2007", nativeCode, ExportConstant.exportWordService);
            }
            return nativeValue;
        }
    }

    private String getNativeStr(String sql, String empId) throws Exception {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = ExportConstant.exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A01011_B"));
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
}
