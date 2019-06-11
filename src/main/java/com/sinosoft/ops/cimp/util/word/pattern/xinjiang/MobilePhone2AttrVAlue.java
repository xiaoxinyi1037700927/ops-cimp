package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  MobilePhone1AttrVAlue
 * @description: 移动电话1
 * @author:        zhangxp
 * @date:            2017年12月3日 下午2:05:39
 * @version        1.0.0
 */
public class MobilePhone2AttrVAlue implements AttrValue{
	   private final String key = "mobilePhone2";
	    private final int order = 21;

	    @Override
	    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
	        Map a205Map = (Map) attrValueContext.get("A205");
	        final String mobilePhone2InfoSql = "SELECT A205035_B FROM Emp_A205  WHERE EMP_ID = '%s'  and status = 0   order by A205000 DESC ";
	        if (a205Map != null) {
	            Object mobilePhone2 = a205Map.get("A205035_B");
	            if (mobilePhone2 == null) {
	                mobilePhone2 = getMobilePhone2(mobilePhone2InfoSql, empId,exportWordService);
	            }
	            return mobilePhone2;
	        } else {
	            Object mobilePhone2 = getMobilePhone2(mobilePhone2InfoSql, empId,exportWordService);
	            if (mobilePhone2 != null) {
	                return mobilePhone2;
	            } else {
	                return "";
	            }
	        }
	    }

	    private Object getMobilePhone2(String sql, String empId,ExportService exportWordService){
	        String attrInfoSql = String.format(sql, empId);
	        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
	        if (attrInfoList != null && attrInfoList.size() > 0) {
	            Map map = (Map) attrInfoList.get(0);
	            if (map != null) {
	                return map.get("A205035_B");
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
            return "移动电话2";
        }    	    
}
