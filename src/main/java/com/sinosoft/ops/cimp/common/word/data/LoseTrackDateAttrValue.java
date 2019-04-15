package com.sinosoft.ops.cimp.common.word.data;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import org.joda.time.DateTime;


/**
 * @version 1.0.0
 * @classname: LoseTrackDateAttrValue
 * @description: 失联日期
 * @author: zhangxp
 * @date: 2017年12月3日 下午6:07:46
 */
public class LoseTrackDateAttrValue implements AttrValue {

    private final String key = "loseTrackDate";
    private final int order = 25;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map c002Map = (Map) attrValueContext.get("C002");
        final String loseTrackDateInfoSql = "SELECT C002082 FROM Emp_C002  WHERE EMP_ID = '%s' and C002082 is not null";
        if (c002Map != null) {
            Object jobDateObj = c002Map.get("C002082");
            if (jobDateObj == null) {
                jobDateObj = getJobDateObj(loseTrackDateInfoSql, empId, exportWordService);
            }
            return new DateTime(jobDateObj).toString("yyyy.MM");
        } else {
            Object jobDateObj = getJobDateObj(loseTrackDateInfoSql, empId, exportWordService);
            if (jobDateObj != null && jobDateObj instanceof Date) {
                return new DateTime(jobDateObj).toString("yyyy.MM");
            } else {
                return "";
            }
        }
    }

    private Object getJobDateObj(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("C002082");
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
