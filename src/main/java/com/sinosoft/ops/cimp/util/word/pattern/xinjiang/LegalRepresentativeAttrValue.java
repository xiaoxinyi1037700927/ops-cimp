package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  LegalRepresentativeAttrValue
 * @description: 法人代表
 * @author:        zhangxp
 * @date:            2017年12月4日 上午1:36:33
 * @version        1.0.0
 */
public class LegalRepresentativeAttrValue implements AttrValue {
    private final int order = 2;
    private final String key = "legalRepresentative";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String depId,ExportService exportWordService){
    	Map b001Map = (Map) attrValueContext.get("B001");
        final String organizationCodeInfoSql = "SELECT B001025 FROM Dep_B001  WHERE Dep_Id = '%s'  and status = 0 ";
        if (b001Map != null) {
            Object legalRepresentative = b001Map.get("B001025");
            if (legalRepresentative == null) {
                legalRepresentative = getLegalRepresentative(organizationCodeInfoSql, depId,exportWordService);
            }
            return legalRepresentative;
        } else {
            Object legalRepresentative = getLegalRepresentative(organizationCodeInfoSql, depId,exportWordService);
            if (legalRepresentative != null) {
                return legalRepresentative;
            } else {
                return "";
            }
        }
    }

    private Object getLegalRepresentative(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("B001025");
            }
        }
        return "";
    }

    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }
    
    @Override
    public String getTitle() {
        return "法人代表";
    }    
}
