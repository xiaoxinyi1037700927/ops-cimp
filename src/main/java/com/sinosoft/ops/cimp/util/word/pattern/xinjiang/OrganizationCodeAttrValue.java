package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  OrganizationCodeAttrValue
 * @description: 单位组织机构代码
 * @author:        zhangxp
 * @date:            2017年12月4日 上午1:26:39
 * @version        1.0.0
 */
public class OrganizationCodeAttrValue implements AttrValue{
    private final int order = 1;
    private final String key = "organizationCode";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String depId,ExportService exportWordService){
    	Map b001Map = (Map) attrValueContext.get("B001");
        final String organizationCodeInfoSql = "SELECT B001015 FROM Dep_B001  WHERE Dep_Id = '%s'  and status = 0 ";
        if (b001Map != null) {
            Object organizationCode = b001Map.get("B001015");
            if (organizationCode == null) {
                organizationCode = getOrganizationCode(organizationCodeInfoSql, depId,exportWordService);
            }
            return organizationCode;
        } else {
            Object organizationCode = getOrganizationCode(organizationCodeInfoSql, depId,exportWordService);
            if (organizationCode != null) {
                return organizationCode;
            } else {
                return "";
            }
        }
    }

    private Object getOrganizationCode(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("B001015");
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
        return "单位组织机构代码";
    }    
}
