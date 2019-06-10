package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.newskysoft.iimp.word.pattern.data.xinjiang.AttrValue;
import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.CodeTranslateUtil;
import com.newskysoft.iimp.word.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import java.util.*;

/**
 * Created by rain chen on 2017/10/9.
 * 年度考核结果
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class EvaluationAttrValue implements AttrValue {

    private final String key = "evaluation";
    private final int order = 22;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        final String evaluationInfoSql = "SELECT * FROM Emp_A060 Emp_A060 where EMP_ID = '%s'  and status = 0  ORDER BY Emp_A060.A060005 DESC";
        String a15TableSql = String.format(evaluationInfoSql, empId);
        List evaluationList = exportWordService.findBySQL(a15TableSql);
        if (evaluationList == null) {
            attrValueContext.put("A060", new ArrayList<Map>());
            evaluationList = new ArrayList();
        }
        attrValueContext.put("A060", evaluationList);
        // 年度考核结果
        StringBuilder evaluationStringBuilder = new StringBuilder();
        if (evaluationList.size() > 0) {
            int len = evaluationList.size();
            if(len < 3){
            	for (int i = len-1 ; i >=0; i--) {
                    Map appraiseInfoMap = null;
                    try {
                        appraiseInfoMap = (Map) evaluationList.get(i);
                    } catch (Exception e) {
                        break;
//                        e.printStackTrace();
                    }
                    if (appraiseInfoMap != null && appraiseInfoMap.size() > 0) {
                        String evaluateCode = StringUtil.obj2Str(appraiseInfoMap.get("A060015"));
                        String evaluateResult = "";
                        if(StringUtil.isNotEmptyOrNull(evaluateCode)) {
                        	evaluateResult = CodeTranslateUtil.codeToName("BT0210", evaluateCode,exportWordService);
                        }
                        String evaluatePerson = "";  //郭芬要求暂时注释 wangyg StringUtil.obj2Str(appraiseInfoMap.get("A060020"));
                        if(StringUtil.isEmptyOrNull(evaluatePerson)) {
                        	evaluatePerson = "";
                        }
//                        List evaluateCodeList = exportWordService.findBySQL(String.format(evaluationCodeSql, evaluateCode));
//                        String evaluateResult = evaluateCodeList == null || evaluateCodeList.size() == 0 ? "" : StringUtil.obj2Str(((Map) evaluateCodeList.get(0)).get("DESCRIPTION"));
                        String evaluateDate = StringUtil.obj2Str(appraiseInfoMap.get("A060005"));
                        String evaluateYear = StringUtil.obj2Str(appraiseInfoMap.get("A060030"));
                        if (evaluateYear != null && evaluateYear.length() > 0) {
                            evaluationStringBuilder.append(evaluateYear).append("年度考核").append(evaluateResult).append(evaluatePerson).append("；");
                        }else if(evaluateDate != null && evaluateDate.length() > 4){
                        	evaluationStringBuilder.append(evaluateDate.substring(0, 4)).append("年度考核").append(evaluateResult).append(evaluatePerson).append("；");
                        }
                    }
                }
            }else{
            	 for (int i = 2 ; i >=0; i--) {
                    Map appraiseInfoMap = null;
                    try {
                        appraiseInfoMap = (Map) evaluationList.get(i);
                    } catch (Exception e) {
                        break;
//                        e.printStackTrace();
                    }
                    if (appraiseInfoMap != null && appraiseInfoMap.size() > 0) {
                    	String evaluateCode = StringUtil.obj2Str(appraiseInfoMap.get("A060015"));
                        String evaluateResult = "";
                        if(StringUtil.isNotEmptyOrNull(evaluateCode)) {
                        	evaluateResult = CodeTranslateUtil.codeToName("BT0210", evaluateCode,exportWordService);
                        }
                        String evaluatePerson = "";  //郭芬要求暂时注释 wangyg StringUtil.obj2Str(appraiseInfoMap.get("A060020"));
                        if(StringUtil.isEmptyOrNull(evaluatePerson)) {
                            evaluatePerson = "";
                        }
//                        List evaluateCodeList = exportWordService.findBySQL(String.format(evaluationCodeSql, evaluateCode));
//                        String evaluateResult = evaluateCodeList == null || evaluateCodeList.size() == 0 ? "" : StringUtil.obj2Str(((Map) evaluateCodeList.get(0)).get("DESCRIPTION"));

                        String evaluateDate = StringUtil.obj2Str(appraiseInfoMap.get("A060005"));
                        String evaluateYear = StringUtil.obj2Str(appraiseInfoMap.get("A060030"));
                        if (evaluateYear != null && evaluateYear.length() > 0) {
                            evaluationStringBuilder.append(evaluateYear).append("年度考核").append(evaluateResult).append(evaluatePerson).append("；");
                        }else if(evaluateDate != null && evaluateDate.length() > 4){
                            evaluationStringBuilder.append(evaluateDate.substring(0, 4)).append("年度考核").append(evaluateResult).append(evaluatePerson).append("；");
                        }
                    }
                }
            }

        }
        String evaluationStr = evaluationStringBuilder.toString();
        if (evaluationStr.contains("；")) {
            evaluationStr = evaluationStr.substring(0, evaluationStr.length() - 1)+"。";
        }
//        if (StringUtils.isEmpty(evaluationStr)) {
//            evaluationStr = "列入中央管理干部考核";
//        }
        return evaluationStr;
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
        return "年度考核结果";
    }    
}
