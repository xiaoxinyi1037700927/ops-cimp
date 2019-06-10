package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.StringUtil;

/**
 * @classname:  DepartmentFullNameAttrValue
 * @description: 统计关系所在单位名称
 * @author:        zhangxp
 * @date:            2017年12月3日 上午1:28:27
 * @version        1.0.0
 */
public class DepartmentFullNameAttrValue implements AttrValue {
    private final String key = "departmentFullName";
    private final int order = 9;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a01Map = (Map) attrValueContext.get("A001");
        final String jobDateInfoSql = "SELECT A001004_A FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
        if (a01Map != null) {
            Object Department = a01Map.get("A001004_A");
            if (Department == null) {
                Department = getDepartmentFullName(jobDateInfoSql, empId,exportWordService);
            }
            return Department;
        } else {
            Object Department = getDepartmentFullName(jobDateInfoSql, empId,exportWordService);
            if (Department != null) {
                return Department;
            } else {
                return "";
            }
        }
    }

    private Object getDepartmentFullName(String sql, String empId,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        Object depId = "";
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
            	depId =  map.get("A001004_A");
            	return getDepartmentFullNameHandler(depId,exportWordService);
            }
        }
        
        return "";
    }

    /**
     * 根据depId获取单位全称
     * @param depId
     * @param exportWordService
     * @return
     */
    private Object getDepartmentFullNameHandler(Object depId,ExportService exportWordService){
    	String sql = "SELECT  DEP_B001.B001001_B,DEP_B001.DESCRIPTION  FROM DEP_B001 DEP_B001 WHERE DEP_B001.DEP_ID = '"+depId+"'";
    	List attrInfoList  = exportWordService.findBySQL(sql);
    	Object name = "";
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
            	name =  map.get("B001001_B");
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
        return "统计关系所在单位名称";
    }    
}
