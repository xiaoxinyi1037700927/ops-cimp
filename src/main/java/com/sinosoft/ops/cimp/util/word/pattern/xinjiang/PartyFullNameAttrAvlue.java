package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.List;
import java.util.Map;



/**
 * 
 * @classname:  PartyFullNameAttrAvlue
 * @description: 所在党组织支部名称
 * @author:        zhangxp
 * @date:            2017年12月3日 下午2:49:37
 * @version        1.0.0
 */
public class PartyFullNameAttrAvlue implements AttrValue {
    private final String key = "partyFullName";
    private final int order =24;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        /*
    	final String partyFullNameInfoSql = "SELECT Emp_C001.* FROM Emp_C001 Emp_C001 WHERE EMP_ID = '%s'";
        String c001TableSql = String.format(partyFullNameInfoSql, empId);
        List c001TableList = exportWordService.findBySQL(c001TableSql);
        if (c001TableList != null && c001TableList.size() > 0) {
            if (c001TableList.size() == 1) {
                Map map = (Map) c001TableList.get(0);
                attrValueContext.put("C001", map);
            }
        }
        */
    	final String partyFullNameInfoSql = "SELECT Emp_C001.C001001_A FROM Emp_C001 Emp_C001 WHERE EMP_ID = '%s'  and status = 0 ";
        Map c001Map = (Map) attrValueContext.get("C001");
        if (c001Map != null) {
        	Object partyFullName = c001Map.get("C001001_A");
            if (partyFullName == null) {
                partyFullName = getPartyFullName(partyFullNameInfoSql, empId,exportWordService);
            }else{
            	partyFullName = getPartyFullNameHandler(partyFullName,exportWordService);
            }
            return partyFullName;
        } else {
        	Object partyFullName = getPartyFullName(partyFullNameInfoSql, empId,exportWordService);
            if (partyFullName != null) {
                return partyFullName;
            } else {
                return "";
            }
        }
    }

    private Object getPartyFullName(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        Object depId = "";
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
            	depId =  map.get("C001001_A");
            	return getPartyFullNameHandler(depId,exportWordService);
            }
        }
        
        return "";
    }

    /**
     * 根据partyId获取党支部全称
     * @param partyId
     * @param exportWordService
     * @return
     */
    private Object getPartyFullNameHandler(Object partyId,ExportService exportWordService){
    	String sql = "SELECT  PARTY_D001.D001001_B,PARTY_D001.DESCRIPTION  FROM PARTY_D001 PARTY_D001 WHERE PARTY_D001.PARTY_ID = '"+partyId+"'";
    	List attrInfoList  = exportWordService.findBySQL(sql);
    	Object name = "";
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
            	name =  map.get("D001001_B");
            	if(StringUtil.isNotEmptyOrNull(name)){
            		return name;
            	}else{
            		name = map.get("DESCRIPTION");
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
        return "所在党组织支部名称";
    }
}
