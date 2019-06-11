package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 奖惩情况
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class HornorAttrValue implements AttrValue {
    private static final Logger logger = LoggerFactory.getLogger(HornorAttrValue.class);
    
    private final String key = "hornor";
    private final int order = 21;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        final String hornorInfoSql = "SELECT * FROM Emp_A135 Emp_A135 WHERE EMP_ID = '%s'  and status = 0  ORDER BY Emp_A135.A135001 ASC,Emp_A135.A135015 ASC";
        //现任职务级别
        final String onJobInfoSql = "SELECT * FROM Emp_A010 Emp_A010 WHERE EMP_ID = '%s'  and status = 0  ORDER BY Emp_A010.A010005 ASC";
        String a14TableSql = String.format(hornorInfoSql, empId);
        String a05TableSql = String.format(onJobInfoSql, empId);
        List hornorInfoList = exportWordService.findBySQL(a14TableSql);
        List jobInfoList = exportWordService.findBySQL(a05TableSql);
        if (hornorInfoList == null) {
            attrValueContext.put("A135", new ArrayList<Map>());
            hornorInfoList = new ArrayList<Map>();
        }
        if (jobInfoList == null) {
            attrValueContext.put("A015", new ArrayList<Map>());
            jobInfoList = new ArrayList<Map>();
        }
        attrValueContext.put("A135", hornorInfoList);
        attrValueContext.put("A015", jobInfoList);

        List<String> unitLevel = new ArrayList<>(); //存放单位基本
        if (jobInfoList.size() > 0) {
            Map jobLevel = (Map) jobInfoList.get(0);
            if (jobLevel != null) {
                //级别大于等于县处级
                int level = NumberUtils.toInt(StringUtil.obj2Str(jobLevel.get("A010030")));

                //县处级干部只提取地、厅级以上奖励和记功，乡科级干部提取县处级以上奖励
                List<String> levelHight = new ArrayList<String>();
                levelHight.add("101");levelHight.add("102");levelHight.add("103");levelHight.add("104");
                levelHight.add("105");levelHight.add("106");levelHight.add("107");levelHight.add("108");
                if (levelHight.contains(level)) {  //县处级干部
                    unitLevel.add("01");
                    unitLevel.add("02");
                    unitLevel.add("03");
                    unitLevel.add("04");
                    unitLevel.add("05");
                    unitLevel.add("06");
                    unitLevel.add("07");
                    unitLevel.add("08");
                }else if (level >= 141 && level <= 142){ // 乡科级干部
                    unitLevel.add("01");
                    unitLevel.add("02");
                    unitLevel.add("03");
                    unitLevel.add("04");
                    unitLevel.add("05");
                    unitLevel.add("06");
                    unitLevel.add("07");
                    unitLevel.add("08");
                    unitLevel.add("09");
                    unitLevel.add("10");
                    unitLevel.add("11");
                    unitLevel.add("99");

                }
            }
        }
        // 奖惩情况
        StringBuilder hornorStringBuilder = new StringBuilder();
        for (Object hornorInfo : hornorInfoList) {
            Map rewardInfoMap = (Map) hornorInfo;
            String a14217 = StringUtil.obj2Str(rewardInfoMap.get("A135040"));            //人员奖励批准单位级别（BT0360）
            Object beginDate = rewardInfoMap.get("A135015");                             //人员奖励批准日期
//            String rewardCode = StringUtil.obj2Str(rewardInfoMap.get("A14221"));        //奖励原因（为代码）
            String rewardDept = StringUtil.obj2Str(rewardInfoMap.get("A135035_B"));      //奖励批准单位
            String rewardCode = StringUtil.obj2Str(rewardInfoMap.get("A135005"));      //人员受奖励代码
            String rewardName = StringUtil.obj2Str(rewardInfoMap.get("A135010"));      //人员受奖励名称
            String isPunishment = StringUtil.obj2Str(rewardInfoMap.get("A135001"));      //人员受奖惩类别   DM019
            String reason=CodeTranslateUtil.codeToName("BT0320", StringUtil.obj2Str(rewardInfoMap.get("A135020")), exportWordService);			//奖惩原因
            String pzwh=StringUtil.obj2Str(rewardInfoMap.get("A135022"));          //奖惩批准文号
            String cxDate=StringUtil.obj2Str(rewardInfoMap.get("A135050"));        //奖惩撤销时间
            String cxwh=StringUtil.obj2Str(rewardInfoMap.get("A135052"));	//奖惩撤销文号
            if(StringUtil.isNotEmptyOrNull(rewardCode) && StringUtil.isEmptyOrNull(rewardName)){
            	rewardName = CodeTranslateUtil.codeToName("BT0450", rewardCode, exportWordService);
            }
            //如果为奖励
            if (StringUtils.equals(isPunishment, "1") || StringUtils.equals(isPunishment, "2")) {
                if (unitLevel.size()>0) {
                    //县处级干部只提取地、厅级以上奖励和记功，乡科级干部提取县处级以上奖励
                    if (unitLevel.contains(a14217)){
                        try {
                            handleReward(beginDate,hornorStringBuilder,rewardDept,rewardName,reason,pzwh,cxDate,cxwh);
                        } catch (Exception e) {
                            throw new AttrValueException("处理奖惩情况时发生异常",e);
                        }
                    }
                }else {
                    try {
                        handleReward(beginDate,hornorStringBuilder,rewardDept,rewardName,reason,pzwh,cxDate,cxwh);
                    } catch (Exception e) {
                        throw new AttrValueException("处理奖惩情况时发生异常",e);
                    }
                }
            } else if (StringUtils.equals(isPunishment, "3")) {
//                String label = "批准受";
                Object discBeginDate = rewardInfoMap.get("A135015");                     //人员受惩戒批准日期
                String discReason = StringUtil.obj2Str(rewardInfoMap.get("A135010"));    //人员受惩戒说明
                String discDept = StringUtil.obj2Str(rewardInfoMap.get("A135035_B"));    //人员受惩戒批准单位名称
                String discCode = StringUtil.obj2Str(rewardInfoMap.get("A135005"));    //人员受惩戒名称
                
                String discName = "";
                if(StringUtil.isNotEmptyOrNull(discCode)){
                	discName = CodeTranslateUtil.codeToName("BT0450", discCode, exportWordService);
                }
                DateTime dateTime;
                if (StringUtil.isNotEmptyOrNull(discBeginDate)) {
                    try {
                        dateTime = new DateTime(discBeginDate);
                    }catch(Exception e) {
                        logger.error("日期有误！",e);
                        dateTime = new DateTime();
                    }
                } else {
                    dateTime = new DateTime();
                }
                int year = dateTime.getYear();
                int month = dateTime.getMonthOfYear();

                hornorStringBuilder.append(year)
                        .append("年").append(month)
                        .append("月").append("因")
                        .append(reason).append("经")
                        .append(discDept).append("批准受")
                        .append(discName).append(StringUtil.isNotEmptyOrNull(pzwh)?"（"+pzwh+"）":"");
                if(StringUtil.isNotEmptyOrNull(cxDate)) {
                	try {
                        dateTime = new DateTime(cxDate);
                    }catch(Exception e) {
                        logger.error("日期有误！",e);
                        dateTime = new DateTime();
                    }
                	int cxYear = dateTime.getYear();
                    int cxMonth = dateTime.getMonthOfYear();
                	hornorStringBuilder.append("，"+cxYear+"年"+cxMonth
                             +"月批准撤销"+discName)
                             .append(StringUtil.isEmptyOrNull(cxwh)?"":"（"+cxwh+"）")
                             .append("；"+(char)11);
                }else {
                	hornorStringBuilder.append("；"+(char)11);
                }
            }
        }

        String rewardStr = hornorStringBuilder.toString();
        if (rewardStr.contains("；")) {
            rewardStr = rewardStr.substring(0, rewardStr.length() - 1)+"。";
        }
        if (StringUtils.isEmpty(rewardStr)) {
            rewardStr = "无";
        }
        return rewardStr;
    }

    /**
     *  处理 奖励情况
     * @param beginDate
     * @param hornorStringBuilder
     * @param rewardDept
     * @param rewardName
     * @throws Exception
     */
    private void handleReward(Object beginDate,StringBuilder hornorStringBuilder,String rewardDept,String rewardName,String reason,String pzwh,String cxDate,String cxwh) throws Exception{
        String rewardValueSql = "SELECT * FROM SYSTEM_CODE_ITEM WHERE SYSTEM_CODE_ITEM.CODE_TYPE = 'BT0320' AND SYSTEM_CODE_ITEM.CODE = '%s'";
//        List rewardValueList = exportWordService.findBySQL(String.format(rewardValueSql, rewardCode));
//        String rewardValue = rewardValueList == null || rewardValueList.size() == 0 ? "" : StringUtil.obj2Str(((Map) rewardValueList.get(0)).get("DESCRIPTION"));
        DateTime dateTime;
        if (StringUtil.isNotEmptyOrNull(beginDate)) {
            try {
                dateTime = new DateTime(beginDate);
            }catch(Exception e) {
                logger.error("日期有误！",e);
                dateTime = new DateTime();
            }
        } else {
            dateTime = new DateTime();
        }
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        hornorStringBuilder
                .append(year).append("年").append(month).append("月，")
                .append("因").append(reason)
                .append("经").append(rewardDept).append("批准给予")
                .append(rewardName).append(StringUtil.isEmptyOrNull(pzwh)?"":"（"+pzwh+"）");
//              rewardName.startsWith("授予") || rewardName.contains("记") ? rewardName : (rewardName.contains("等功") ? "记" + rewardName : "授予" + rewardName))
    	if(StringUtil.isNotEmptyOrNull(cxDate)) {
        	try {
                dateTime = new DateTime(cxDate);
            }catch(Exception e) {
                logger.error("日期有误！",e);
                dateTime = new DateTime();
            }
        	int cxYear = dateTime.getYear();
            int cxMonth = dateTime.getMonthOfYear();
        	hornorStringBuilder.append("，"+cxYear+"年"+cxMonth
                     +"月批准撤销"+rewardName)
                     .append(StringUtil.isEmptyOrNull(cxwh)?"":"（"+cxwh+"）")
                     .append("；"+(char)11);
        }else {
        	hornorStringBuilder.append("；"+(char)11);
        }
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
        return "奖惩情况";
    }
}
