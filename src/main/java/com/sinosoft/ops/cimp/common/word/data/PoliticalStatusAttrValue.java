package com.sinosoft.ops.cimp.common.word.data;

import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @version 1.0.0
 * @classname: PoliticalStatusAttrValue
 * @description: 政治面貌
 * @author: zhangxp
 * @date: 2017年12月3日 下午2:21:58
 */
public class PoliticalStatusAttrValue implements AttrValue {
    final private String key = "politicalStatus";
    final private int order = 22;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        Map a030Map = (Map) attrValueContext.get("A030");
        final String politicalStatusInfoSql = "SELECT A030001 FROM Emp_A030 Emp_A030 WHERE EMP_ID = '%s' order by Emp_A030.A030001 ASC";

        if (a030Map != null) {
            String politicalStatusCode = StringUtil.obj2Str(a030Map.get("A030001"));
            if (StringUtils.isNotEmpty(politicalStatusCode)) {
                politicalStatusCode = this.getPoliticalStatusStr(politicalStatusInfoSql, empId, politicalStatusCode, exportWordService);
            } else {
                return this.getPoliticalStatusStr(politicalStatusInfoSql, empId, null, exportWordService);
            }
            return politicalStatusCode;
        } else {
            return this.getPoliticalStatusStr(politicalStatusInfoSql, empId, null, exportWordService);
        }
    }

    private String getPoliticalStatusStr(String sql, String empId, String politicalStatusCode, ExportService exportWordService) {
        String genderInfoSql = String.format(sql, empId);
        if (StringUtils.isNotEmpty(politicalStatusCode)) {
            return CodeTranslateUtil.codeToName("BT0100", politicalStatusCode, exportWordService);
        } else {
            List genderInfoList = exportWordService.findBySQL(genderInfoSql);
            if (genderInfoList != null && genderInfoList.size() > 0) {
                Map IndividualStatusMap = (Map) genderInfoList.get(0);
                if (IndividualStatusMap != null) {
                    String IndividualStatus_Code = StringUtil.obj2Str(IndividualStatusMap.get("A030001"));
                    return CodeTranslateUtil.codeToName("BT0100", IndividualStatus_Code, exportWordService);
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
}
