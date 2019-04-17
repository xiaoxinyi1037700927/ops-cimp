package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * Created by rain chen on 2017/9/20.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
@Component
public class NameAttrValue implements AttrValue {

    //属性与属性之间的排序，越小越靠前
    public static final int ORDER = 0;

    public static final String KEY = "name";

    private String name = "";
    private String cardNo = "";


    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            String a01TableSql = "SELECT * FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0";
            a01TableSql = String.format(a01TableSql, empId);
            List a01TableList = ExportConstant.exportWordService.findBySQL(a01TableSql);

            if (a01TableList != null && a01TableList.size() > 0) {
                Map map = (Map) a01TableList.get(0);
                //将A01表的记录放到上下文中
                attrValueContext.put("A01", map);
                //获取姓名字段并返回
                String name = StringUtil.obj2Str(map.get("A01001"));
                String cardNo = StringUtil.obj2Str(map.get("A001003"));//身份证号
                this.name = name;
//                this.id = id;
                this.cardNo = cardNo;
                if (name.length() == 2) {
                    String firstName = name.substring(0, 1);
                    String lastName = name.substring(1, name.length());
                    return firstName + "  " + lastName;
                } else {
                    return name;
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[姓名]");
        }
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
