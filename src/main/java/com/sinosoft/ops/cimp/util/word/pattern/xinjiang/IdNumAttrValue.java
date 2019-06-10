package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;
import com.newskysoft.iimp.word.service.ExportService;
/**
 * @classname:  IdNumAttrValue
 * @description: 身份证号
 * @author:        zhangxp
 * @date:            2017年12月3日 上午12:47:55
 * @version        1.0.0
 */
public class IdNumAttrValue implements AttrValue {

    private final String key = "idNum";
    private final int order = 8;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String idNumInfoSql = "SELECT A001003 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
        if (a001Map != null) {
            Object idNumObj = a001Map.get("A001003");
            if (idNumObj == null) {
                idNumObj = getIdNum(idNumInfoSql, empId,exportWordService);
            }
            return idNumObj;
        } else {
            Object idNumObj = getIdNum(idNumInfoSql, empId,exportWordService);
            if (idNumObj != null) {
                return idNumObj;
            } else {
                return "";
            }
        }
    }

    private Object getIdNum(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A001003");
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
        return "身份证号";
    }    
}
