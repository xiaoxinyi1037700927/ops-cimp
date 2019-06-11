package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/29.
 * 籍贯
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class NativeAttrValue implements AttrValue {
    private final int order = 4;

    private final String key = "native";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String nativeInfoSql = "SELECT A001025_A, A001025_B FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
       if (a001Map != null) {
            String nativeName = StringUtil.obj2Str(a001Map.get("A001025_B"));
            String nativeCode = StringUtil.obj2Str(a001Map.get("A001025_A"));
            if (StringUtils.isNotEmpty(nativeName)) {
                return nativeName;
            }
            if (StringUtils.isEmpty(nativeName)) {
                nativeCode = this.getNativeStr(nativeInfoSql, empId,exportWordService);
            }
            String nativeValue = "";
            if (StringUtils.isNotEmpty(nativeCode)) {
            	nativeValue = CodeTranslateUtil.codeToName("BT0760", nativeCode,exportWordService);
//                List nativeValueList = exportWordService.findBySQL(String.format(nativeCodeSql, nativeCode));
//                nativeValue = nativeValueList == null || nativeValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) nativeValueList.get(0)).get("DESCRIPTION"));
            }
            return nativeValue;
        } else {
            String nativeCode = getNativeStr(nativeInfoSql, empId,exportWordService);
            String nativeValue = "";
            if (StringUtils.isNotEmpty(nativeCode)) {
            	nativeValue = CodeTranslateUtil.codeToName("BT0760", nativeCode,exportWordService);
//                List nativeValueList = exportWordService.findBySQL(String.format(nativeCodeSql, nativeCode));
//                nativeValue = nativeValueList == null || nativeValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) nativeValueList.get(0)).get("DESCRIPTION"));
            }
            return nativeValue == null? "":nativeValue;
        }
    }

    private String getNativeStr(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A001025_A"));
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
        return "籍贯";
    }        
}
