package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.newskysoft.iimp.word.service.ExportService;

/**
 * 
 * @classname:  RetireDateAttrValue
 * @description: 离退时间
 * @author:        zhangxp
 * @date:            2017年12月3日 下午10:53:59
 * @version        1.0.0
 */
public class RetireDateAttrValue implements AttrValue{
    private final String key = "retireDate";
    private final int order = 31;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a231Map = (Map) attrValueContext.get("A231");
        final String retireDateInfoSql = "SELECT A231010 FROM Emp_A231 WHERE EMP_ID = '%s'  and status = 0 ";
        if (a231Map != null) {
            Object retireDate = a231Map.get("A231010");
            if (retireDate == null) {
                retireDate = getRetireDate(retireDateInfoSql, empId,exportWordService);
            }
            return new DateTime(retireDate).toString("yyyy.MM");
        } else {
            Object retireDate = getRetireDate(retireDateInfoSql, empId,exportWordService);
            if (retireDate != null && retireDate instanceof Date) {
                return new DateTime(retireDate).toString("yyyy.MM");
            } else {
                return "";
            }
        }
    }

    private Object getRetireDate(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A231010");
            }
        }
        return "";
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getTitle() {
        return "离退时间";
    }
}
