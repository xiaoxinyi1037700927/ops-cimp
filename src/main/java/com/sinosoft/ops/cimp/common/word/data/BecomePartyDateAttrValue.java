package com.sinosoft.ops.cimp.common.word.data;

import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import org.joda.time.DateTime;


/**
 * @version 1.0.0
 * @classname: BecomePartyDateAttrValue
 * @description: 转正时间
 * @author: zhangxp
 * @date: 2017年12月3日 下午5:36:02
 */
public class BecomePartyDateAttrValue implements AttrValue {
    private final String key = "becomePartyDate";
    private final int order = 23;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map a030Map = (Map) attrValueContext.get("A030");
        final String becomePartyDateInfoSql = "SELECT A030020 FROM Emp_A030  WHERE EMP_ID = '%s' order by A030001 ASC";
        if (a030Map != null) {
            Object becomePartyDate = a030Map.get("A030020");
            if (becomePartyDate == null) {
                becomePartyDate = getBecomePartyDate(becomePartyDateInfoSql, empId, exportWordService);
            }

            return new DateTime(becomePartyDate).toString("yyyy.MM");
        } else {
            Object becomePartyDate = getBecomePartyDate(becomePartyDateInfoSql, empId, exportWordService);
            if (becomePartyDate != null) {
                return new DateTime(becomePartyDate).toString("yyyy.MM");
            } else {
                return "";
            }
        }
    }

    private Object getBecomePartyDate(String sql, String empId, ExportService exportWordService) {
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return map.get("A030020");
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
