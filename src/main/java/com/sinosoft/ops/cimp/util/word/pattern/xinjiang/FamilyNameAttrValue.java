package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.newskysoft.iimp.word.pattern.data.xinjiang.AttrValue;
import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 *家庭主要成员和社会关系--姓名
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyNameAttrValue implements AttrValue {

    private final String key = "fmName";
    private final int order = 24;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        List familyList = (List) attrValueContext.get("A200");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String familyName = StringUtil.obj2Str(map.get("A200001"));
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
        return order;
    }

    public String getKey() {
        return key;
    }
    
    @Override
    public String getTitle() {
        return "家庭主要成员和社会关系--姓名";
    }    
}
