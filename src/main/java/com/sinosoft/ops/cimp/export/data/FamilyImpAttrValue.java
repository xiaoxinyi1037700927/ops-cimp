package com.sinosoft.ops.cimp.export.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created  on 2018/07/18.
 *
 * @author kanglin
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyImpAttrValue implements AttrValue {

    private final String key = "fmImp";
    private final int order = 27;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        List familyList = (List) attrValueContext.get("A36");
        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String A36049 = (String)map.get("A36049");
            if ("1".equals(A36049)) {
            	familyMap.put("fmImp_" + i, "是");
            } else if ("0".equals(A36049)) {
            	familyMap.put("fmImp_" + i, "否");
            } else if ("2".equals(A36049)) {
            	familyMap.put("fmImp_" + i, "否");
            } else {
            	familyMap.put("fmImp_" + i, "");
            }
        }
        return familyMap;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }
}
