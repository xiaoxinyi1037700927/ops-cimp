package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import java.util.List;
import java.util.Map;

import com.newskysoft.iimp.word.service.ExportService;

/**
 * 
 * @classname:  PostCodeAttrValue
 * @description: 邮政编码
 * @author:        zhangxp
 * @date:            2017年12月3日 下午12:47:03
 * @version        1.0.0
 */
public class PostCodeAttrValue implements AttrValue{
	 private final String key = "postCode";
	    private final int order = 16;

	    @Override
	    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
	        Map a205Map = (Map) attrValueContext.get("A205");
	        final String postCodeInfoSql = "SELECT A205020 FROM Emp_A205  WHERE EMP_ID = '%s'  and status = 0  order by A205000 DESC ";
	        if (a205Map != null) {
	            Object postCode = a205Map.get("A205020");
	            if (postCode == null) {
	                postCode = getPostCode(postCodeInfoSql, empId,exportWordService);
	            }
	            return postCode;
	        } else {
	            Object postCode = getPostCode(postCodeInfoSql, empId,exportWordService);
	            if (postCode != null) {
	                return postCode;
	            } else {
	                return "";
	            }
	        }
	    }

	    private Object getPostCode(String sql, String empId,ExportService exportWordService){
	        String attrInfoSql = String.format(sql, empId);
	        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
	        if (attrInfoList != null && attrInfoList.size() > 0) {
	            Map map = (Map) attrInfoList.get(0);
	            if (map != null) {
	                return map.get("A205020");
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
            return "邮政编码";
        }
}
