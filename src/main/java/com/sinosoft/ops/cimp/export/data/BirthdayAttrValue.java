package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class BirthdayAttrValue implements AttrValue {

    public static final int ORDER = 2;

    public static final String KEY = "birthday";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            Map a01Map = (Map) attrValueContext.get("A01");
            final String birthdayInfoSql = "SELECT A01007 FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
            if (a01Map != null) {
                Object birthday = a01Map.get("A01007");
                if (birthday != null) {
                    return getDateStrAndYearsOld(birthday);
                } else {
                    Object birthdayDb = getBirthdayStr(birthdayInfoSql, empId);
                    return getDateStrAndYearsOld(birthdayDb);
                }
            } else {
                Object birthday = getBirthdayStr(birthdayInfoSql, empId);
                return getDateStrAndYearsOld(birthday);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A001_A01007出生日期]");
        }
    }


    private Object getBirthdayStr(String sql, String empId) {
        String birthdayInfoSql = String.format(sql, empId);
        List birthdayInfoList = ExportConstant.exportService.findBySQL(birthdayInfoSql);
        if (birthdayInfoList != null && birthdayInfoList.size() > 0) {
            Map birthdayMap = (Map) birthdayInfoList.get(0);
            if (birthdayMap != null) {
                return birthdayMap.get("A01007");
            }
        }
        return "";
    }

    private String getDateStrAndYearsOld(Object birthday) {
        String dateStr = "";
        int yearsOld;
        if (StringUtils.isNotEmpty(String.valueOf(birthday))) {
            dateStr = new DateTime(birthday).toString("yyyy.MM");
            yearsOld = Years.yearsBetween(
                    LocalDate.parse(new DateTime(birthday).toString("yyyy-MM-dd")),
                    LocalDate.now()).getYears();
        } else {
            yearsOld = 0;
        }

        // shixianggui-20180319, 任免审批表中出生年月换行
//        return dateStr + "\n" + "(" + yearsOld + "岁)";
        return dateStr + (char) 11 + "(" + yearsOld + "岁)";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
