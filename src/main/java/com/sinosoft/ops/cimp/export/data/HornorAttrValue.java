package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;

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
public class HornorAttrValue implements AttrValue {

    public static final String KEY = "hornor";
    public static final int ORDER = 21;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            //final String hornorInfoSql = "SELECT * FROM EMP_A14 WHERE EMP_ID = '%s' and status=0 and year(sysdate)-year(case when left(A14201,1) ='1' then A14225 when left(A14201,1) ='2' then A14307 when left(A14201,1) ='3' then A14324  end)<=5 ORDER BY case when left(A14201,1) ='1' then A14225 when left(A14201,1) ='2' then A14307 when left(A14201,1) ='3' then A14324  end";
            final String hornorInfoSql = "SELECT * FROM EMP_A14 WHERE EMP_ID = '%s' and status=0 ORDER BY case when substr(A14201,0,1) ='1' then A14225 when substr(A14201,0,1) ='2' then A14307 when substr(A14201,0,1) ='3' then A14324  end";
            //现任职务级别
            final String onJobInfoSql = "SELECT * FROM EMP_A05 WHERE EMP_ID = '%s' AND A05024 = '1'  and status=0 ";
            String a14TableSql = String.format(hornorInfoSql, empId);
            String a05TableSql = String.format(onJobInfoSql, empId);
            List hornorInfoList = ExportConstant.exportWordService.findBySQL(a14TableSql);
            List jobInfoList = ExportConstant.exportWordService.findBySQL(a05TableSql);
            if (hornorInfoList == null) {
                attrValueContext.put("A14", new ArrayList<Map>());
                hornorInfoList = new ArrayList<Map>();
            }
            if (jobInfoList == null) {
                attrValueContext.put("A05", new ArrayList<Map>());
                jobInfoList = new ArrayList<Map>();
            }
            attrValueContext.put("A14", hornorInfoList);
            attrValueContext.put("A05", jobInfoList);

            List<String> unitLevel = new ArrayList<>(); //存放单位基本
            if (jobInfoList.size() > 0) {
                Map jobLevel = (Map) jobInfoList.get(0);
                if (jobLevel != null) {
                    //级别大于等于县处级
                    int level = NumberUtils.toInt(StringUtil.obj2Str(jobLevel.get("A05002_B")));

                    //县处级干部只提取地、厅级以上奖励和记功，乡科级干部提取县处级以上奖励
                    if (level >= 101 && level <= 132) {  //县处级干部
                        unitLevel.add("101");
                        unitLevel.add("102");
                        unitLevel.add("103A");
                        unitLevel.add("103B");
                        unitLevel.add("104A");
                        unitLevel.add("104B");
//			            unitLevel.add("105");
//			            unitLevel.add("106");
                    } else if (level >= 141 && level <= 142) { // 乡科级干部
                        unitLevel.add("105");
                        unitLevel.add("106");
                        unitLevel.add("101");
                        unitLevel.add("102");
                        unitLevel.add("103A");
                        unitLevel.add("103B");
                        unitLevel.add("104A");
                        unitLevel.add("104B");
                        unitLevel.add("105");
                        unitLevel.add("106");
//			            unitLevel.add("107");
                    }
                }
            }
            // 奖惩情况
            StringBuilder hornorStringBuilder = new StringBuilder();
            for (Object hornorInfo : hornorInfoList) {
                Map rewardInfoMap = (Map) hornorInfo;
                String a14217 = StringUtil.obj2Str(rewardInfoMap.get("A14217"));            //人员奖励批准单位级别（DM141）
                Object beginDate = rewardInfoMap.get("A14225");                             //人员奖励批准日期
//            String rewardCode = StringUtil.obj2Str(rewardInfoMap.get("A14221"));        //奖励原因（为代码）
                String rewardDept = StringUtil.obj2Str(rewardInfoMap.get("A14214_A"));      //奖励批准单位
                String rewardName = StringUtils.equals(StringUtil.obj2Str(rewardInfoMap.get("A14220")), "") ? StringUtil.obj2Str(rewardInfoMap.get("A14205_A")) : StringUtil.obj2Str(rewardInfoMap.get("A14220"));      //人员受奖励名称/说明  A14224
                String isPunishment = StringUtil.obj2Str(rewardInfoMap.get("A14201"));      //人员受奖惩类别   DM019

                //如果为奖励
                if (isPunishment.startsWith("1")) {
                    if (unitLevel.size() > 0) {
                        //县处级干部只提取地、厅级以上奖励和记功，乡科级干部提取县处级以上奖励
                        if (unitLevel.contains(a14217)) {
                            handleReward(beginDate, hornorStringBuilder, rewardDept, rewardName);
                        }
                    } else {
                        handleReward(beginDate, hornorStringBuilder, rewardDept, rewardName);
                    }
                } else if (isPunishment.startsWith("2")) {
                    String label = "给予";
                    Object discBeginDate = rewardInfoMap.get("A14307");//受惩戒批准时间
                    String discReason = StringUtil.obj2Str(rewardInfoMap.get("A14321"));    //人员受惩戒说明
                    String discDept = StringUtil.obj2Str(rewardInfoMap.get("A14311_A"));    //人员受惩戒批准单位名称
                    String discName = StringUtil.obj2Str(rewardInfoMap.get("A14305_A"));    //人员受惩戒名称
                    Object revokeDate = rewardInfoMap.get("A14324"); //人员受惩戒解除日期
                    if (revokeDate != null && discBeginDate != null) {
                        DateTime enddateTime = new DateTime(revokeDate);
                        int endyear = enddateTime.getYear();
                        int endmonth = enddateTime.getMonthOfYear();
                        DateTime dateTime = new DateTime(discBeginDate);
                        int year = dateTime.getYear();
                        int month = dateTime.getMonthOfYear();
                        hornorStringBuilder.append(year)
                                .append("年").append(month)
                                .append("月").append("因")
                                .append(discReason).append("经")
                                .append(discDept).append("批准，" + label)
                                .append(discName).append("；\n");

                        hornorStringBuilder.append(endyear)
                                .append("年").append(endmonth)
                                .append("月").append("经")
                                .append(discDept).append("批准，撤销")
                                .append(discName).append("；\n");
                    } else if (discBeginDate != null) {
                        DateTime dateTime = new DateTime(discBeginDate);
                        int year = dateTime.getYear();
                        int month = dateTime.getMonthOfYear();
                        hornorStringBuilder.append(year)
                                .append("年").append(month)
                                .append("月").append("因")
                                .append(discReason).append("经")
                                .append(discDept).append("批准，" + label)
                                .append(discName).append("；\n");

                    }
                } else if (isPunishment.startsWith("3")) {
                    Object discBeginDate = rewardInfoMap.get("A14307");//受惩戒批准时间
                    String discReason = StringUtil.obj2Str(rewardInfoMap.get("A14321"));    //人员受惩戒说明
                    String discDept = StringUtil.obj2Str(rewardInfoMap.get("A14311_A"));    //人员受惩戒批准单位名称
                    String discName = StringUtil.obj2Str(rewardInfoMap.get("A14305_A"));    //人员受惩戒名称
                    Object discEndDate = rewardInfoMap.get("A14324");   //人员受惩戒解除日期
                    if (discBeginDate != null && discEndDate != null) {
                        DateTime enddateTime = new DateTime(discEndDate);
                        int endyear = enddateTime.getYear();
                        int endmonth = enddateTime.getMonthOfYear();
                        DateTime dateTime = new DateTime(discBeginDate);
                        int year = dateTime.getYear();
                        int month = dateTime.getMonthOfYear();

                        if (isPunishment.startsWith("3")) {
                            hornorStringBuilder.append(year)
                                    .append("年").append(month)
                                    .append("月").append("因")
                                    .append(discReason).append("经")
                                    .append(discDept).append("批准，给予")
                                    .append(discName).append("；\n");

                            hornorStringBuilder.append(endyear)
                                    .append("年").append(endmonth)
                                    .append("月").append("经")
                                    .append(discDept).append("批准，撤销")
                                    .append(discName).append("；\n");
                        }
                    } else if (discBeginDate != null) {
                        DateTime dateTime = new DateTime(discBeginDate);
                        int year = dateTime.getYear();
                        int month = dateTime.getMonthOfYear();

                        if (isPunishment.startsWith("3")) {
                            hornorStringBuilder.append(year)
                                    .append("年").append(month)
                                    .append("月").append("因")
                                    .append(discReason).append("经")
                                    .append(discDept).append("批准，给予")
                                    .append(discName).append("；\n");
                        }
                    }
                }
            }
            String rewardStr = hornorStringBuilder.toString();
            if (rewardStr.contains("；")) {
                rewardStr = rewardStr.substring(0, rewardStr.length() - 2);
            }
            if (StringUtils.isEmpty(rewardStr)) {
                rewardStr = "无";
            }
            return rewardStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A14奖惩情况或EMP_A05职级情况]");
        }
    }

    /**
     * 处理 奖励情况
     *
     * @param beginDate
     * @param hornorStringBuilder
     * @param rewardDept
     * @param rewardName
     * @throws Exception
     */
    private void handleReward(Object beginDate, StringBuilder hornorStringBuilder, String rewardDept, String rewardName) throws Exception {
        if (beginDate != null) {
            DateTime dateTime = new DateTime(beginDate);
            int year = dateTime.getYear();
            int month = dateTime.getMonthOfYear();

            hornorStringBuilder
                    .append(year).append("年").append(month).append("月，")
                    .append("被").append(rewardDept).append(
                    rewardName.startsWith("授予") || rewardName.contains("记") ? rewardName : (rewardName.contains("等功") ? "记" + rewardName : "授予" + rewardName))
                    .append("；\n");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
