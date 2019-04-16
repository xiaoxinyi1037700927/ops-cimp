package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;

import java.util.List;
import java.util.Map;


/**
 * @version 1.0.0
 * @classname: IsStopPartyAttrValue
 * @description: 是否停止党籍
 * @author: zhangxp
 * @date: 2017年12月3日 下午6:35:20
 */
public class IsStopPartyAttrValue implements AttrValue {

    private final String key = "isStopParty";
    private final int order = 26;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map a030Map = (Map) attrValueContext.get("A030");
        final String loseTrackDateInfoSql = "SELECT A030040 FROM Emp_A030  WHERE EMP_ID = '%s' and A030040 is not null";
        if (a030Map != null) {
            Object jobDateObj = a030Map.get("A030040");
            if (jobDateObj != null) {
                if (jobDateObj.equals("203") || jobDateObj.equals("20301") || jobDateObj.equals("20199")) {
                    return "是";
                } else {
                    return "否";
                }
            } else {
                return "否";
            }
        } else {
            Object jobDateObj = getJobDateObj(loseTrackDateInfoSql, empId, exportWordService);
            if (jobDateObj != null) {
                if (jobDateObj.equals("203") || jobDateObj.equals("20301") || jobDateObj.equals("20199")) {
                    return "是";
                } else {
                    return "否";
                }
            }
            return "否";
        }
    }

    private Object getJobDateObj(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A030040");
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
