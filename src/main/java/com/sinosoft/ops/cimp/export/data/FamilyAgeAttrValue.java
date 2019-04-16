package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.time.Period;
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
public class FamilyAgeAttrValue implements AttrValue {

    private final String key = "fmAge";
    private final int order = 25;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        List familyList = (List) attrValueContext.get("A36");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            Object fmBirthday = map.get("A36007");
            String fmStatus = StringUtil.obj2Str(map.get("A36041"));
            if (fmBirthday != null) {
                if (StringUtils.equals(fmStatus, "7")) {
                    familyMap.put("fmAge_" + i, "");
                } else {
                    int yearsOld = Years.yearsBetween(
                            LocalDate.parse(new DateTime(fmBirthday).toString("yyyy-MM-dd")),
                            LocalDate.now()).getYears();
                    String yearOld = "";
                    if (yearsOld == 0) {
                        int result = 0;
                        Period d = Period.between(java.time.LocalDate.parse(new DateTime(fmBirthday).toString("yyyy-MM-dd")), java.time.LocalDate.now());
                        result = d.getMonths();
                        yearOld = result == 12 ? 1 + "" : result + "个月";
                    } else {
                        yearOld = StringUtil.obj2Str(yearsOld);
                    }

                    familyMap.put("fmAge_" + i, yearOld);
                }
                familyMap.put("fmBirthday_" + i, new DateTime(fmBirthday).toString("yyyy-MM-dd"));
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
