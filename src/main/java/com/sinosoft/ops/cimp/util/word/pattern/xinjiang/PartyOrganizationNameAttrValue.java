package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;



/**
 * 
 * @classname:  PartyOrganizationAttrValue
 * @description: 党组织全称
 * @author:        zhangxp
 * @date:            2017年12月4日 下午12:30:15
 * @version        1.0.0
 */
public class PartyOrganizationNameAttrValue implements AttrValue{

    private final String key = "partyOrganization";
    private final int order = 5;
    public static String name = "";
    public static String id = "";
    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//        String partyId = OrganizationFullNameAttrValue.getPartyId();
        String d001TableSql = "SELECT * FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
        d001TableSql = String.format(d001TableSql, partyId);
        List b001TableList = exportWordService.findBySQL(d001TableSql);
        if (b001TableList != null && b001TableList.size() > 0) {
            Map map = (Map) b001TableList.get(0);
            //将D001表的记录放到上下文中
            attrValueContext.put("D001", map);
        }
    	Map d001Map = (Map) attrValueContext.get("D001");
        final String homeTelephoneInfoSql = "SELECT D001001_B,DESCRIPTION FROM Party_D001 WHERE PARTY_ID = '%s'";
        if (d001Map != null) {
            Object partyOrganization = d001Map.get("D001001_B");
            String id = StringUtil.obj2Str(d001Map.get("TREE_LEVEL_CODE"));
            
            if (partyOrganization == null || partyOrganization.equals("")) {
                partyOrganization = getPartyOrganization(homeTelephoneInfoSql, partyId,exportWordService);
            }
            this.id = id;
            this.name = StringUtil.obj2Str(partyOrganization);
            return partyOrganization;
        } else {
            Object partyOrganization = getPartyOrganization(homeTelephoneInfoSql, partyId,exportWordService);
            String id = StringUtil.obj2Str(d001Map.get("TREE_LEVEL_CODE"));
            if (partyOrganization != null) {
                this.id = id;
                this.name = StringUtil.obj2Str(partyOrganization);;
                return partyOrganization;
            } else {
                return "";
            }
        }
    }

    private Object getPartyOrganization(String sql, String partyId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, partyId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
            	Object temp = map.get("D001001_B");
            	if(temp == null || temp.equals("")){
            		temp = map.get("DESCRIPTION");
            	}
                return temp;
            }
        }
        return "";
    }

    public static String getName() {
		return name;
	}

	public static String getId() {
		return id;
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
        return "党组织全称";
    }
}
