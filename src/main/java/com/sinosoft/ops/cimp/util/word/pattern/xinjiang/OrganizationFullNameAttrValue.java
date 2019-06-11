package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  OrganizationFullNameAttrValue
 * @description: 单位名称全称
 * @author:        zhangxp
 * @date:            2017年12月4日 上午12:02:42
 * @version        1.0.0
 */
public class OrganizationFullNameAttrValue implements AttrValue{
    //属性与属性之间的排序，越小越靠前
    private final int order = 0;

    private final String key = "organizationFullName";

    public static String name = "";
    public static String id = "";
    public static String partyId = "";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String depId,ExportService exportWordService){
        String d001TableSql = "SELECT * FROM Dep_B001 WHERE DEP_ID = '%s'  and status = 0 ";
        d001TableSql = String.format(d001TableSql, depId);
        List b001TableList = exportWordService.findBySQL(d001TableSql);
        setPartyId(depId,exportWordService);
        if (b001TableList != null && b001TableList.size() > 0) {
            Map map = (Map) b001TableList.get(0);
            //将B001表的记录放到上下文中
            attrValueContext.put("B001", map);
            //获取名字字段并返回
            String name = StringUtil.obj2Str(map.get("B001001_B"));
            String id = StringUtil.obj2Str(map.get("TREE_LEVEL_CODE"));
            this.name = name;
            this.id = id;
            return name;
        }
        return "";
    }
    
    public void setPartyId(String depId,ExportService exportWordService){
    	String partysql = "SELECT PARTY_ID FROM PARTY_D001 WHERE INSTR(D001005_A,'%s') > 0";
    	partysql = String.format(partysql, depId);
    	List partyIdList = exportWordService.findBySQL(partysql);
    	if(partyIdList != null && partyIdList.size() > 0){
    		Map map = (Map)partyIdList.get(0);
    		String partyId = StringUtil.obj2Str(map.get("PARTY_ID"));
    		this.partyId = partyId;
    	}
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
    
    public static String getPartyId() {
        return partyId;
    }
    
    @Override
    public String getTitle() {
        return "单位名称全称";
    }    
}
