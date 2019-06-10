package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import com.newskysoft.iimp.word.service.ExportService;
/**
 * 
 * @classname:  MobilePhone1AttrVAlue
 * @description: 移动电话1
 * @author:        zhangxp
 * @date:            2017年12月3日 下午2:05:39
 * @version        1.0.0
 */
public class MobilePhone1AttrVAlue implements AttrValue{
	   private final String key = "mobilePhone1";
	    private final int order = 20;

	    @Override
	    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
	        Map a205Map = (Map) attrValueContext.get("A205");
	        final String mobilePhone1InfoSql = "SELECT A205035_A FROM Emp_A205  WHERE EMP_ID = '%s'  and status = 0   order by A205000 DESC";
	        if (a205Map != null) {
	            Object mobilePhone1 = a205Map.get("A205035_A");
	            if (mobilePhone1 == null) {
	                mobilePhone1 = getMobilePhone1(mobilePhone1InfoSql, empId,exportWordService);
	            }
	            return mobilePhone1;
	        } else {
	            Object mobilePhone1 = getMobilePhone1(mobilePhone1InfoSql, empId,exportWordService);
	            if (mobilePhone1 != null) {
	                return mobilePhone1;
	            } else {
	                return "";
	            }
	        }
	    }

	    private Object getMobilePhone1(String sql, String empId,ExportService exportWordService){
	        String attrInfoSql = String.format(sql, empId);
	        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
	        if (attrInfoList != null && attrInfoList.size() > 0) {
	            Map map = (Map) attrInfoList.get(0);
	            if (map != null) {
	                return map.get("A205035_A");
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
	        return "移动电话1";
	    }	    
}
