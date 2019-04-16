package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;
import org.joda.time.DateTime;


import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 工作时间
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class JobDateAttrValue implements AttrValue {

    private final String key = "jobDate";
    private final int order = 7;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map a01Map = (Map) attrValueContext.get("A001");
        final String jobDateInfoSql = "SELECT A001055 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'";
        if (a01Map != null) {
            Object jobDateObj = a01Map.get("A001055");
            if (jobDateObj == null) {
                jobDateObj = getJobDateObj(jobDateInfoSql, empId, exportWordService);
            }
            return new DateTime(jobDateObj).toString("yyyy.MM");
        } else {
            Object jobDateObj = getJobDateObj(jobDateInfoSql, empId, exportWordService);
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
                return map.get("A001055");
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
