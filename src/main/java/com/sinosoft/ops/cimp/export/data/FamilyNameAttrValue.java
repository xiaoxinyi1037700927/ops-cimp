package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyNameAttrValue implements AttrValue {

    public static final String KEY = "fmName";
    public static final int ORDER = 24;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        List familyList = (List) attrValueContext.get("A36");

        Map<String, String> familyMap = new HashMap<>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String familyName = StringUtil.obj2Str(map.get("A36001"));
            if (familyName.length() == 2) {
                String firstName = familyName.substring(0, 1);
                String lastName = familyName.substring(1, familyName.length());
                familyMap.put("fmName_" + i, firstName + "  " + lastName);
            } else {
                familyMap.put("fmName_" + i, familyName);
            }
        }
        return familyMap;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
