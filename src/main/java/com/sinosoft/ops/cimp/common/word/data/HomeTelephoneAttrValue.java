package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;

import java.util.List;
import java.util.Map;


/**
 * @version 1.0.0
 * @classname: HomeTelephoneAttrValue
 * @description: 家庭电话
 * @author: zhangxp
 * @date: 2017年12月3日 下午12:41:56
 */
public class HomeTelephoneAttrValue implements AttrValue {

    private final String key = "homeTelephone";
    private final int order = 15;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map a205Map = (Map) attrValueContext.get("A205");
        final String homeTelephoneInfoSql = "SELECT A205025 FROM Emp_A205  WHERE EMP_ID = '%s'";
        if (a205Map != null) {
            Object homeTelephone = a205Map.get("A205025");
            if (homeTelephone == null) {
                homeTelephone = getHomeTelephone(homeTelephoneInfoSql, empId, exportWordService);
            }
            return homeTelephone;
        } else {
            Object homeTelephone = getHomeTelephone(homeTelephoneInfoSql, empId, exportWordService);
            if (homeTelephone != null) {
                return homeTelephone;
            } else {
                return "";
            }
        }
    }

    private Object getHomeTelephone(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A205025");
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
