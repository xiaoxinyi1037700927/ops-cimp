package com.sinosoft.ops.cimp.common.word.data;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/29.
 * 民族
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class NationAttrValue implements AttrValue {

    private final int order = 3;

    private final String key = "nation";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map a001Map = (Map) attrValueContext.get("A001");
        final String nationInfoSql = "SELECT A001011 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'";
        if (a001Map != null) {
            String nation = StringUtil.obj2Str(a001Map.get("A001011"));
            if (StringUtils.isNotEmpty(nation)) {
                return this.getNationStr(nationInfoSql, empId, nation, exportWordService);
            } else {
                return getNationStr(nationInfoSql, empId, null, exportWordService);
            }
        } else {
            return getNationStr(nationInfoSql, empId, null, exportWordService);
        }
    }

    private String getNationStr(String sql, String empId, String nationCode, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
//        final String nationCodeSql = "SELECT * FROM SYSTEM_CODE_ITEM WHERE SYSTEM_CODE_ITEM.CODE_TYPE = 'BT0010' AND SYSTEM_CODE_ITEM.CODE = '%s'";

        if (StringUtils.isNotEmpty(nationCode)) {
            return CodeTranslateUtil.codeToName("BT0010", nationCode, exportWordService);
//            List nationValueList = exportWordService.findBySQL(String.format(nationCodeSql, nationCode));
//            return nationValueList == null || nationValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) nationValueList.get(0)).get("DESCRIPTION"));
        } else {
            List attrInfoList = exportWordService.findBySQL(attrInfoSql);
            if (attrInfoList != null && attrInfoList.size() > 0) {
                Map map = (Map) attrInfoList.get(0);
                if (map != null) {
                    //GB/T3304-1991
                    String nation_code = StringUtil.obj2Str(map.get("A001011"));
                    String nation_name = CodeTranslateUtil.codeToName("BT0010", nation_code, exportWordService);
                    return nation_name;
//                    List nationValueList = exportWordService.findBySQL(String.format(nationCodeSql, nation_code));
//                    return nationValueList == null || nationValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) nationValueList.get(0)).get("DESCRIPTION"));
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
}
