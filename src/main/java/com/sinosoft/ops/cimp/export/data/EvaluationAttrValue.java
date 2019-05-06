package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
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
public class EvaluationAttrValue implements AttrValue {

    public static final String KEY = "evaluation";
    public static final int ORDER = 22;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            final String evaluationInfoSql = "SELECT * FROM EMP_A15 where EMP_ID = '%s'  and status=0 ORDER BY A15021";
            String a15TableSql = String.format(evaluationInfoSql, empId);
            List evaluationList = ExportConstant.exportService.findBySQL(a15TableSql);
            if (evaluationList == null) {
                attrValueContext.put("A15", new ArrayList<Map>());
                evaluationList = new ArrayList();
            }
            attrValueContext.put("A15", evaluationList);
            // 年度考核结果
            StringBuilder evaluationStringBuilder = new StringBuilder();
            if (evaluationList.size() > 0) {
                int len = evaluationList.size();
                for (int i = (len >= 3 ? len - 3 : 0); i < len; i++) {
                    Map appraiseInfoMap = null;
                    try {
                        appraiseInfoMap = (Map) evaluationList.get(i);
                    } catch (Exception e) {
                        break;
                    }
                    if (appraiseInfoMap != null && appraiseInfoMap.size() > 0) {
                        String evaluateCode = StringUtil.obj2Str(appraiseInfoMap.get("A15017"));
                        String evaluateResult = CodeTranslateUtil.codeToName("DM018", evaluateCode, ExportConstant.exportService);
                        String evaluateDate = StringUtil.replaceSpeStr(StringUtil.obj2Str(appraiseInfoMap.get("A15021")), " 00:00:00.0");
                        if (StringUtils.isNotEmpty(evaluateDate)) {
                            int year = LocalDate.parse(new DateTime(String.valueOf(evaluateDate)).toString("yyyy")).getYear();
                            evaluationStringBuilder.append(year).append("年度考核").append(evaluateResult).append("、");
                        }
                    }
                }
            }

            String evaluationStr = evaluationStringBuilder.toString();
            if (evaluationStr.contains("、")) {
                evaluationStr = evaluationStr.substring(0, evaluationStr.length() - 1);
            }
            if (StringUtils.isEmpty(evaluationStr)) {
                evaluationStr = "请检查是否未参加过年度考核";
            }
            return evaluationStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A15考核情况]");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
