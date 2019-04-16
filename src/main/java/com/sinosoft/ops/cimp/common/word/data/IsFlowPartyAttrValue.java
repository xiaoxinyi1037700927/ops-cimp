package com.sinosoft.ops.cimp.common.word.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import org.springframework.util.StringUtils;

/**
 * @version 1.0.0
 * @classname: IsFlowPartyAttrValue
 * @description: 流动党员标示
 * @author: zhangxp
 * @date: 2017年12月3日 下午9:50:21
 */
public class IsFlowPartyAttrValue implements AttrValue {

    final private String key = "isFlowParty";
    final private int order = 27;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        final String isFlowPartyInfoSql = "SELECT * FROM Emp_C010  WHERE EMP_ID = '%s' and C010010 is not null";
        String c010TableSql = String.format(isFlowPartyInfoSql, empId);
        List c01TableList = exportWordService.findBySQL(c010TableSql);
        if (c01TableList != null && c01TableList.size() > 0) {
            if (c01TableList.size() == 1) {
                Map map = (Map) c01TableList.get(0);
                attrValueContext.put("C010", map);
            }
        }
        Map c010Map = (Map) attrValueContext.get("C010");
        if (c010Map != null) {
            Object isFlowParty = c010Map.get("C010010");
            if (isFlowParty == null) {
                isFlowParty = getisFlowParty(isFlowPartyInfoSql, empId, exportWordService);
            }
            if (StringUtils.isEmpty(isFlowParty)) {
                return "否";
            } else {
                return "是";
            }
        } else {
            Object isFlowParty = getisFlowParty(isFlowPartyInfoSql, empId, exportWordService);
            if (isFlowParty != null && isFlowParty instanceof Date) {
                return "是";
            } else {
                return "否";
            }
        }
    }

    private Object getisFlowParty(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("C010010");
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
}
