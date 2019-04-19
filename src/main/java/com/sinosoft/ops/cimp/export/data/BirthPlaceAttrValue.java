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
public class BirthPlaceAttrValue implements AttrValue {

    public static final String KEY = "birthPlace";
    public static final int ORDER = 5;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        Map a01Map = (Map) attrValueContext.get("A01");
        final String birthPlaceInfoSql = "SELECT A01014_A FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
        if (a01Map != null) {
            String birthPlace = StringUtil.obj2Str(a01Map.get("A01014_A"));
            if (StringUtils.isEmpty(birthPlace)) {
                birthPlace = getBirthPlaceStr(birthPlaceInfoSql, empId);
            }
            return birthPlace;
        } else {
            return getBirthPlaceStr(birthPlaceInfoSql, empId);
        }
    }

    private String getBirthPlaceStr(String sql, String empId) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = ExportConstant.exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get("A01014_A"));
            }
        }
        return "";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
