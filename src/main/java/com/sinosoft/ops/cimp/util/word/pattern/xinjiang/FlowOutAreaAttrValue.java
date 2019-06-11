package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;



/**
 * 
 * @classname:  FlowOutAreaAttrValue
 * @description: 流出地行政区划
 * @author:        zhangxp
 * @date:            2017年12月3日 下午10:11:29
 * @version        1.0.0
 */
public class FlowOutAreaAttrValue implements AttrValue {

	  private final String key = "flowOutArea";
	  private final int order = 29;
	   
	    @Override
	    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
	        Map c010Map = (Map) attrValueContext.get("C010");
	        final String flowOutAreaInfoSql = "SELECT C010020 FROM Emp_C010 Emp_C010 WHERE Emp_C010.EMP_ID = '%s'  and status = 0 ";
	        if (c010Map != null) {
	            String flowOutArea = StringUtil.obj2Str(c010Map.get("C010020"));
	            if (StringUtils.isNotEmpty(flowOutArea)) {
	                return this.getFlowOutArea(flowOutAreaInfoSql, empId, flowOutArea,exportWordService);
	            } else {
	                return getFlowOutArea(flowOutAreaInfoSql, empId, null,exportWordService);
	            }
	        } else {
	            return getFlowOutArea(flowOutAreaInfoSql, empId, null,exportWordService);
	        }
	    }

	    private String getFlowOutArea(String sql, String empId, String nationCode,ExportService exportWordService){
	        String attrInfoSql = String.format(sql, empId);
//	        final String nationCodeSql = "SELECT * FROM SYSTEM_CODE_ITEM WHERE SYSTEM_CODE_ITEM.CODE_TYPE = 'BT0010' AND SYSTEM_CODE_ITEM.CODE = '%s'";

	        if (StringUtils.isNotEmpty(nationCode)) {
	        	return CodeTranslateUtil.codeToName("BT0005", nationCode,exportWordService);
//	            List nationValueList = exportWordService.findBySQL(String.format(nationCodeSql, nationCode));
//	            return nationValueList == null || nationValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) nationValueList.get(0)).get("DESCRIPTION"));
	        } else {
	            List attrInfoList = exportWordService.findBySQL(attrInfoSql);
	            if (attrInfoList != null && attrInfoList.size() > 0) {
	                Map map = (Map) attrInfoList.get(0);
	                if (map != null) {
	                    //GB/T3304-1991
	                    String nation_code = StringUtil.obj2Str(map.get("C010020"));
	                    String nation_name = CodeTranslateUtil.codeToName("BT0005", nation_code,exportWordService);
	                    return nation_name;
//	                    List nationValueList = exportWordService.findBySQL(String.format(nationCodeSql, nation_code));
//	                    return nationValueList == null || nationValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) nationValueList.get(0)).get("DESCRIPTION"));
	                }
	            }
	        }

	        return "";
	    }

	    @Override
	    public int getOrder() {
	        return order;
	    }

	    public String getKey() {
	        return key;
	    }
	    
	    @Override
	    public String getTitle() {
	        return "流出地行政区划";
	    }
}
