package com.sinosoft.ops.cimp.common.word.data;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 * 家庭主要成员和社会关系--年龄
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyAgeAttrValue implements AttrValue {

    private final String key = "fmAge";
    private final int order = 25;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) throws Exception {
        List familyList = (List) attrValueContext.get("A200");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            Object fmBirthday = map.get("A200010");
            String fmStatus = StringUtil.obj2Str(map.get("A200040"));
            if (fmBirthday != null && fmBirthday instanceof Date) {
                if (StringUtils.equals(fmStatus, "8")) {
                    familyMap.put("fmAge_" + i, "");
                } else {
                    int yearsOld = Years.yearsBetween(
                            LocalDate.parse(new DateTime(fmBirthday).toString("yyyy-MM-dd")),
                            LocalDate.now()).getYears();
                    familyMap.put("fmAge_" + i, StringUtil.obj2Str(yearsOld));
                }
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
