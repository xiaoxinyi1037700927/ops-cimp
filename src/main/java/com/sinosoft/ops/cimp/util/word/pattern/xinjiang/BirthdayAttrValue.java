package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *	出生日期
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class BirthdayAttrValue implements AttrValue{

    private static final Logger logger = LoggerFactory.getLogger(BirthdayAttrValue.class);
    private final int order = 2;

    private final String key = "birthday";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String birthdayInfoSql = "SELECT A001015 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
        if (a001Map != null) {
            Object birthday = a001Map.get("A001015");
            if (birthday != null && birthday instanceof Date) {
                return getDateStrAndYearsOld(birthday);
            } else {
                Object birthdayDb = getBirthdayStr(birthdayInfoSql, empId,exportWordService);
                return getDateStrAndYearsOld(birthdayDb);
            }
        } else {
            Object birthday = getBirthdayStr(birthdayInfoSql, empId,exportWordService);
            return getDateStrAndYearsOld(birthday);
        }
    }


    private Object getBirthdayStr(String sql, String empId,ExportService exportWordService){
        String birthdayInfoSql = String.format(sql, empId);
        List birthdayInfoList = exportWordService.findBySQL(birthdayInfoSql);
        if (birthdayInfoList != null && birthdayInfoList.size() > 0) {
            Map birthdayMap = (Map) birthdayInfoList.get(0);
            if (birthdayMap != null) {
                return birthdayMap.get("A001015");
            }
        }
        return "";
    }

    private String getDateStrAndYearsOld(Object birthday) {
        String dateStr = "";
        int yearsOld;
        if (birthday != null && birthday instanceof Date) {
            try {
                dateStr = new DateTime(birthday).toString("yyyy.MM");
                yearsOld = Years.yearsBetween(
                        LocalDate.parse(new DateTime(birthday).toString("yyyy-MM-dd")),
                        LocalDate.now()).getYears();
            }catch(Exception e) {
                logger.error("出生日期有误！",e);
                yearsOld = 0;
            }
        } else {
            yearsOld = 0;
        }

        return dateStr + "\n" + "(" + yearsOld + "岁)";
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
        return "出生日期";
    }
}
