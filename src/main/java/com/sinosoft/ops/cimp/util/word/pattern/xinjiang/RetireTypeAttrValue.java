package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;


/**
 * 
 * @classname:  RetireTypeAttrValue
 * @description: 离退类别
 * @author:        zhangxp
 * @date:            2017年12月3日 下午10:40:03
 * @version        1.0.0
 */
public class RetireTypeAttrValue implements AttrValue{
	final private String key = "retireType";
	final private int order = 30;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        final String retireTypeInfoSql = "SELECT * FROM Emp_A231  WHERE EMP_ID = '%s'  and status = 0 ";
        String a231TableSql = String.format(retireTypeInfoSql, empId);
        List a231TableList = exportWordService.findBySQL(a231TableSql);
        if (a231TableList != null && a231TableList.size() > 0) {
            if (a231TableList.size() == 1) {
                Map map = (Map) a231TableList.get(0);
                attrValueContext.put("A231", map);
            }
        }
        Map c010Map = (Map) attrValueContext.get("A231");
        if (c010Map != null) {
            String retireType = StringUtil.obj2Str(c010Map.get("A231005"));
            if (StringUtils.isNotEmpty(retireType)) {
                return this.getRetireType(retireTypeInfoSql, empId, retireType,exportWordService);
            } else {
                return getRetireType(retireTypeInfoSql, empId, null,exportWordService);
            }
        } else {
            return getRetireType(retireTypeInfoSql, empId, null,exportWordService);
        }
    }

    private String getRetireType(String sql, String empId, String retireType,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        if (StringUtils.isNotEmpty(retireType)) {
        	return CodeTranslateUtil.codeToName("BT0615", retireType,exportWordService);
        } else {
            List attrInfoList = exportWordService.findBySQL(attrInfoSql);
            if (attrInfoList != null && attrInfoList.size() > 0) {
                Map map = (Map) attrInfoList.get(0);
                if (map != null) {
                    String nation_code = StringUtil.obj2Str(map.get("A231005"));
                    String nation_name = CodeTranslateUtil.codeToName("BT0615", nation_code,exportWordService);
                    return nation_name;
                }
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
        return "离退类别";
    }
}
