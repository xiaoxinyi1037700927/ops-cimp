package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import com.newskysoft.iimp.word.service.ExportService;

/**
 * 
 * @classname:  WorkTelephone1AttrValue
 * @description: 工作电话1
 * @author:        zhangxp
 * @date:            2017年12月3日 下午1:02:59
 * @version        1.0.0
 */
public class WorkTelephone1AttrValue implements AttrValue{

    private final String key = "workTelephone1";
    private final int order = 17;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a205Map = (Map) attrValueContext.get("A205");
        final String homeTelephoneInfoSql = "SELECT A205030_A FROM Emp_A205  WHERE EMP_ID = '%s'  and status = 0   order by A205000 DESC";
        if (a205Map != null) {
            Object workTelephone1 = a205Map.get("A205030_A");
            if (workTelephone1 == null) {
                workTelephone1 = getWorkTelephone1(homeTelephoneInfoSql, empId,exportWordService);
            }
            return workTelephone1;
        } else {
            Object workTelephone1 = getWorkTelephone1(homeTelephoneInfoSql, empId,exportWordService);
            if (workTelephone1 != null) {
                return workTelephone1;
            } else {
                return "";
            }
        }
    }

    private Object getWorkTelephone1(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A205030_A");
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
        return "工作电话1";
    }
}
