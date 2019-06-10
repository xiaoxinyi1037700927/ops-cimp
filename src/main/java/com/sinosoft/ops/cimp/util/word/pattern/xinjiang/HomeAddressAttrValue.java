package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import com.newskysoft.iimp.word.service.ExportService;

/**
 * @classname:  HomeAddressAttrValue
 * @description: 家庭住址
 * @author:        zhangxp
 * @date:            2017年12月3日 上午1:59:06
 * @version        1.0.0
 */
public class HomeAddressAttrValue implements AttrValue{

    private final String key = "homeAddress";
    private final int order = 10;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        final String familyInfoSql = "SELECT * FROM Emp_A205 Emp_A205 WHERE EMP_ID = '%s'  and status = 0 order by Emp_A205.A205000 DESC";
        String a205TableSql = String.format(familyInfoSql, empId);
        List a205TableList = exportWordService.findBySQL(a205TableSql);
        if (a205TableList != null && a205TableList.size() > 0) {
            if (a205TableList.size() == 1) {
                Map map = (Map) a205TableList.get(0);
                attrValueContext.put("A205", map);
            }
        }
    	Map a205Map = (Map) attrValueContext.get("A205");
        final String jobDateInfoSql = "SELECT A205010 FROM Emp_A205 Emp_A205 WHERE EMP_ID = '%s' order by Emp_A205.A205000 DESC";
        if (a205Map != null) {
            Object homeAddressObj = a205Map.get("A205010");
            if (homeAddressObj == null) {
                homeAddressObj = getHomeAddressObj(jobDateInfoSql, empId,exportWordService);
            }
            return homeAddressObj;
        } else {
            Object homeAddressObj = getHomeAddressObj(jobDateInfoSql, empId,exportWordService);
            if (homeAddressObj != null) {
                return homeAddressObj;
            } else {
                return "";
            }
        }
    }

    private Object getHomeAddressObj(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A205010");
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
        return "家庭住址";
    }    
}
