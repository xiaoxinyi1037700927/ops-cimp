package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * 
 * @classname:  PoliticalStatusAttrValue
 * @description: 政治面貌
 * @author:        zhangxp
 * @date:            2017年12月3日 下午2:21:58
 * @version        1.0.0
 */
public class PoliticalStatusAttrValue implements AttrValue {
	final private String key = "politicalStatus";
	final private int order = 22;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a030Map = (Map) attrValueContext.get("A030");
        Map a001Map = (Map) attrValueContext.get("A001");
        final String politicalStatusInfoSql = "SELECT A030001,A030040 FROM Emp_A030 Emp_A030 WHERE EMP_ID = '%s'  and status = 0  order by Emp_A030.A030001 ASC";
        boolean isAgeCross27 = true;
        if(a001Map != null){
            Object birthday = a001Map.get("A001015");
            if (birthday != null && birthday instanceof Date) {
            	isAgeCross27 = ageCross27(birthday);
            }
        }
        if(a030Map == null){
        	String genderInfoSql = String.format(politicalStatusInfoSql, empId);
        	List genderInfoList = exportWordService.findBySQL(genderInfoSql);
        	if(genderInfoList != null && genderInfoList.size() >0){
        		a030Map = (Map)genderInfoList.get(0);
        	}
        }
        
        if (a030Map != null) {
            String politicalStatusCode = StringUtil.obj2Str(a030Map.get("A030001"));
            String outOfPartyCode = StringUtil.obj2Str(a030Map.get("A030040"));
            if(StringUtils.equals(politicalStatusCode, "01")  || StringUtils.equals(politicalStatusCode, "02")){
            	attrValueContext.put("isZhongGongDangYuan", true);
            }else{
            	attrValueContext.put("isZhongGongDangYuan", false);
            }
            //政治面貌为群众、其它的，信息确认表政治面设为空
            if(StringUtils.equals("13", politicalStatusCode) || StringUtils.equals("99", politicalStatusCode)){
            	attrValueContext.put("isShowZhengZhiMianMiaoTime", false);
            	return "";
            }
            //已经出党的政治面貌为空
            if(outOfPartyCode.startsWith("201") || outOfPartyCode.startsWith("205")){
            	attrValueContext.put("isShowZhengZhiMianMiaoTime", false);
            	return "";
            }
            //共青团员年龄超过27的设置为空
            if(StringUtils.equals("03", politicalStatusCode) && isAgeCross27){
            	attrValueContext.put("isShowZhengZhiMianMiaoTime", false);
            	return "";
            }
            //共青团员入党时间为空
            if(StringUtils.equals("03", politicalStatusCode)){
            	attrValueContext.put("isShowZhengZhiMianMiaoTime", false);
            }else{
            	attrValueContext.put("isShowZhengZhiMianMiaoTime", true);
            }
            if (StringUtils.isNotEmpty(politicalStatusCode)) {
                politicalStatusCode = this.getPoliticalStatusStr(politicalStatusInfoSql, empId, politicalStatusCode,exportWordService);
            } else {
                return "";
            }
            return politicalStatusCode;
        } else {
            return "";
        }
    }
    
    private String getPoliticalStatusStr(String sql, String empId, String politicalStatusCode,ExportService exportWordService){
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
    
    
    private boolean ageCross27(Object birthday) {
        int yearsOld;
        if (birthday != null && birthday instanceof Date) {
            yearsOld = Years.yearsBetween(
                    LocalDate.parse(new DateTime(birthday).toString("yyyy-MM-dd")),
                    LocalDate.now()).getYears();
        } else {
            yearsOld = 0;
        }

        return  yearsOld>27 ;
    }
    @Override
    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getTitle() {
        return "政治面貌";
    }
}
