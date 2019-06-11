package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * @classname:  FlowOutDateAttrValue
 * @description: 流出时间
 * @author:        zhangxp
 * @date:            2017年12月3日 下午9:57:13
 * @version        1.0.0
 */
public class FlowOutDateAttrValue implements AttrValue {
	final private String key = "flowOutDate";
	final private int order = 28;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map c010Map = (Map) attrValueContext.get("C010");
        final String flowOutDateInfoSql = "SELECT C010010 FROM Emp_C010  WHERE EMP_ID = '%s'  and status = 0  and C010010 is not null";
        if (c010Map != null) {
            Object flowOutDate = c010Map.get("C010010");
            if (flowOutDate == null) {
                flowOutDate = getFlowOutDate(flowOutDateInfoSql, empId,exportWordService);
            }
            return new DateTime(flowOutDate).toString("yyyy.MM");
        } else {
            Object jobDateObj = getFlowOutDate(flowOutDateInfoSql, empId,exportWordService);
            if (jobDateObj != null && jobDateObj instanceof Date) {
                return new DateTime(jobDateObj).toString("yyyy.MM");
            } else {
                return "";
            }
        }
    }

    private Object getFlowOutDate(String sql, String empId,ExportService exportWordService){
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
    
    @Override
    public String getTitle() {
        return "流出时间";
    }
}
