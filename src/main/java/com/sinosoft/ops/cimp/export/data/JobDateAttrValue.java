package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
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
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            Map a01Map = (Map) attrValueContext.get("A01");
            final String jobDateInfoSql = "SELECT A01034 FROM EMP_A001 A01 WHERE EMP_ID = '%s'  and status=0 ";
            if (a01Map != null) {
                Object jobDateObj = a01Map.get("A01034");
                if (jobDateObj == null) {
                    jobDateObj = getJobDateObj(jobDateInfoSql, empId);
                }
                return new DateTime(jobDateObj).toString("yyyy.MM");
            } else {
                Object jobDateObj = getJobDateObj(jobDateInfoSql, empId);
                if (StringUtils.isNotEmpty(String.valueOf(jobDateObj))) {
                    return new DateTime(jobDateObj).toString("yyyy.MM");
                } else {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[A01034参加工作日期]");
        }
    }

    private Object getJobDateObj(String sql, String empId) throws Exception {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = ExportConstant.exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A01034");
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
