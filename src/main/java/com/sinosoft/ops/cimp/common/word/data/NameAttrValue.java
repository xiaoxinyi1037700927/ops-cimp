package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;


/**
 * Created by rain chen on 2017/9/20.
 * 姓名
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class NameAttrValue implements AttrValue {
    //属性与属性之间的排序，越小越靠前
    private final int order = 0;

    private final String key = "name";

    public static String name = "";
    public static String id = "";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        String a001TableSql = "SELECT A001001,A001003 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'";
        a001TableSql = String.format(a001TableSql, empId);
        List a001TableList = exportWordService.findBySQL(a001TableSql);

        if (a001TableList != null && a001TableList.size() > 0) {
            Map map = (Map) a001TableList.get(0);
            //将A01表的记录放到上下文中
            attrValueContext.put("A001", map);
            //获取姓名字段并返回
            String name = StringUtil.obj2Str(map.get("A001001"));
            String id = StringUtil.obj2Str(map.get("A001003"));
            this.name = name;
            this.id = id;
            if (name.length() == 2) {
                String firstName = name.substring(0, 1);
                String lastName = name.substring(1, name.length());
                return firstName + "  " + lastName;
            } else {
                return name;
            }
        }
        return "";
    }

    /**
     * 根据empId获取任免表的名称
     *
     * @param empId
     * @param exportWordService
     * @return
     */
    public String getGbrmbName(String empId, ExportService exportWordService) {
        String a001TableSql = "SELECT A001001,A001003 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'";
        a001TableSql = String.format(a001TableSql, empId);
        List a001TableList = exportWordService.findBySQL(a001TableSql);

        if (a001TableList != null && a001TableList.size() > 0) {
            Map map = (Map) a001TableList.get(0);
            //获取姓名字段并返回
            String name = StringUtil.obj2Str(map.get("A001001"));
            String id = StringUtil.obj2Str(map.get("A001003"));
            return name + "（" + id + "）" + "任免表.docx";
        }
        return "";
    }

    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }

    public static String getName() {
        return name;
    }

    public static String getId() {
        return id;
    }
}
