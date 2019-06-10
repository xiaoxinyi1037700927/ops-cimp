package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.StringUtil;

/**
 * @classname:  DanweiOfPartyAttrValue
 * @description: 党组织所在单位名称(所有)
 * @author:        zhangxp
 * @date:            2017年12月4日 下午7:40:16
 * @version        1.0.0
 */
public class DanweiOfPartyAttrValue implements AttrValue{

	    private final String key = "danweiOfParty";
	    private final int order = 12;

	    @Override
	    public Object getAttrValue(Map<String, Object> attrValueContext, String partyId,ExportService exportWordService){
//	    	String partyId = OrganizationFullNameAttrValue.getPartyId();
	        final String danweiOfPartyInfoSql = "SELECT D001005_A FROM Party_D001 WHERE PARTY_ID = '%s'  and status = 0 ";
	        String danweiOfPartyIds = this.getPartyContact(danweiOfPartyInfoSql, partyId, exportWordService);
	        String[] depIds = danweiOfPartyIds.split(",");
	        String depSql = "SELECT B001001_B,DESCRIPTION FROM DEP_B001 WHERE DEP_ID = '%s'";
	        StringBuffer danweiOfParty = new StringBuffer();
	        String tempName = "";
	        for(int i=0;i<depIds.length;i++){
	        	tempName = this.getDepNameByDepId(depSql, depIds[i], exportWordService);
	        	danweiOfParty.append(tempName+"，");
	        }
	        String name = danweiOfParty.toString();
	        name = name.substring(0, name.length()-1);
	        return name;
	    }


	    private String getPartyContact(String sql, String depId,ExportService exportWordService){
	        String attrInfoSql = String.format(sql, depId);
	        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
	        if (attrInfoList != null && attrInfoList.size() > 0) {
	            Map map = (Map) attrInfoList.get(0);
	            if (map != null) {
	                return StringUtil.obj2Str(map.get("D001005_A"));
	            }
	        }
	        return "";
	    }
	    
	    private String getDepNameByDepId(String sql, String depId,ExportService exportWordService){
	        String attrInfoSql = String.format(sql, depId);
	        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
	        if (attrInfoList != null && attrInfoList.size() > 0) {
	            Map map = (Map) attrInfoList.get(0);
	            if (map != null) {
	            	String name = StringUtil.obj2Str(map.get("B001001_B"));
	            	if(StringUtil.isEmptyOrNull(name)){
	            		name = StringUtil.obj2Str(map.get("DESCRIPTION"));
	            	}
	                return name;
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
	        return "党组织所在单位名称";
	    }	    
}
