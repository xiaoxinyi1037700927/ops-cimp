package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.common.EnumType;
import com.sinosoft.ops.cimp.export.common.IntervalImpl;
import com.sinosoft.ops.cimp.export.common.SetList;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by rain chen on 2017/10/10.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class ResumeAttrValue extends ResumeAttrVauleHandle implements AttrValue {

    public static final String KEY = "resume";
    public static final int ORDER = 20;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {

//        try {
        // shixianggui-20180411, bug: 简历合并问题
//			final String jobInfoSql = "SELECT * FROM EMP_A02 WHERE EMP_ID = '%s'  and status=0 ORDER BY A02043,A02065";
        final String jobInfoSql = "SELECT b001.code DepCode, a02.* FROM EMP_A02 a02 left join dep_b001 b001 on b001.dep_id=a02.A02001_B WHERE a02.EMP_ID = '%s' and a02.status=0 ORDER BY a02.A02043,a02.A02065";

        // 学习（培训、进修）情况
        final String trainInfoSql = "SELECT * FROM EMP_A11 WHERE EMP_ID = '%s'  and status=0 ORDER BY A11007 DESC";
        //教育信息
        final String eduInfoSql = "SELECT * FROM EMP_A08 WHERE EMP_ID = '%s'  and status=0 ORDER BY A08004 ASC";
        ///wft 20180228 简历中添加专业技术信息集
        ///专业技术信息
        final String professionalInfoSql = "SELECT * FROM EMP_A06 WHERE EMP_ID = '%s'  and status=0 ORDER BY A06004 ASC";

        List trainInfoList = ExportConstant.exportWordService.findBySQL(String.format(trainInfoSql, empId));
        List jobInfoList = ExportConstant.exportWordService.findBySQL(String.format(jobInfoSql, empId));
        List eduInfoList = ExportConstant.exportWordService.findBySQL(String.format(eduInfoSql, empId));
        ///wft 20180228 简历中添加专业技术信息集
        List professionalInfoList = ExportConstant.exportWordService.findBySQL(String.format(professionalInfoSql, empId));

        if (professionalInfoList == null) {
            attrValueContext.put("A06", new ArrayList<Map>());
            trainInfoList = new ArrayList<Map>();
        }
        attrValueContext.put("A06", professionalInfoList);


        if (trainInfoList == null) {
            attrValueContext.put("A11", new ArrayList<Map>());
            trainInfoList = new ArrayList<Map>();
        }
        attrValueContext.put("A11", trainInfoList);

        this.setEduInfoList(eduInfoList);
        this.setJobInfoList(jobInfoList);
        this.setTrainInfoList(trainInfoList);
        ///
        this.setProfessionalInfoList(professionalInfoList);


        List<Map<String, Object>> resultResumeList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();
        List<String> emptyDateKeys = new ArrayList<>(); //存放简历结束日期为空的经历


        /**
         * 第一步 合并符合条件的任职经历和学历信息
         * 工作开始时间和结束时间相同的进行分组
         */
        Map<String, List<Map>> jobGroupByStartTime = new LinkedHashMap<String, List<Map>>();
        //把学历信息转换成map
        try {
            handleDateToKey(jobGroupByStartTime, eduInfoList, emptyDateKeys);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_学历信息]");
        }
        //把任职经历信息转换成Map
        try {
            handleDateToKey(jobGroupByStartTime, jobInfoList, emptyDateKeys);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_任职经历信息]");
        }

        //处理简历的结束日期为空的简历
        handResumeEndDateIsEmpty(emptyDateKeys, jobGroupByStartTime);

        //处理简历日期跨段
        Map<String, List<Map>> jobGroupByCorssTime = handelResumeDateCorss(jobGroupByStartTime);

        /**
         * 第二步处理经历的增任职务
         * 例如:
         * 2007/1/1	2012/12/1	贵州省大方县人民正科级检察员
         * 2007/1/1	2008/12/1	贵州省大方县人民检察院检察长
         * 2007/1/1	2009/12/1	贵州省大方县人民检察院检察委员会委员
         *
         * 结果：
         * 2007/1/1	2008/12/1	贵州省大方县人民检察院检察长、检察委员会委员、正科级检察员
         * 2007/1/1	2009/12/1	贵州省大方县人民检察院检察委员会委员、正科级检察员
         *
         * 此需求为同一人在同一时间段内任多个职务
         */
        try {
            handelDateSameAndAdd(jobGroupByCorssTime, jobGroupByStartTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_此人在同一时间段内任多个职务]");
        }

        //学历、任职经历重组之后按起始日期排序
        List<Map.Entry<String, List<Map>>> keySet = sortByResumeDate(jobGroupByCorssTime);

        // shixianggui-20180417, bug-377 学历学位问题, 只获得学位, 未获取到学历也是要显示在简历中
        Map<String, Map> onlyDegreeA09Map = getOnlyDegreeA09Map(attrValueContext);

        //第三步 合并增任职务
        for (Map.Entry<String, List<Map>> entry : keySet) {
            Map<String, Object> startDateAndContentMap = new HashMap<String, Object>();
            StringBuilder resumeContentStr = new StringBuilder();
            IntervalImpl interval = null;

            //获得 某个时间的职务信息集
            List<Map> maps = entry.getValue();

            //按职务主次序号排序
            sortByJobOrder(maps);

            //获取时间
            String[] startAndEndStrs = entry.getKey().split("_");
            String jobStartDate = startAndEndStrs[0];

            String jobEndDate = "";
            if (startAndEndStrs.length > 1) {
                jobEndDate = startAndEndStrs[1];
            }
            // System.out.println("jobStartDatejobStartDate=="+jobStartDate+"jobEndDatejobEndDate="+jobEndDate);
            SetList<String> deptNameSet = new SetList<>();
            for (Map map : maps) {
                StringBuilder appendStringBuilder = new StringBuilder("（");
                StringBuilder resumeStartDateStr = new StringBuilder();

                String jobDeptName = StringUtil.obj2Str(map.get("A02001_A"));   //任职机构名称
                String jobInterName = StringUtil.obj2Str(map.get("A02085_A"));  //内设机构名称
                String jobName = StringUtil.obj2Str(map.get("A02016_A"));       //职务名称
                String jobReason = StringUtil.obj2Str(map.get("A02049"));       //任职原因
                String a02019 = StringUtil.obj2Str(map.get("A02019"));          //任职范围类别

                String schoolName = StringUtil.obj2Str(map.get("A08014"));  //学校
                String a08016 = StringUtil.obj2Str(map.get("A08016"));      //院系名称
                String a08024 = StringUtil.obj2Str(map.get("A08024"));      //所学专业名称
                String eduName = StringUtil.obj2Str(map.get("A08002_A"));   //学历名称
                String eduLevel = StringUtil.obj2Str(map.get("A08002_B"));  //学历代码
                String A08020 = StringUtil.obj2Str(map.get("A08020"));  //教育类别
                String A08034 = StringUtil.obj2Str(map.get("A08034"));  //最高学历标识

                //根据一下两个日期判断 数据来自职务信息集还是学历信息集
                String a08004 = StringUtil.obj2Str(map.get("A08004"));              //入学时间
                String jobStart = StringUtil.obj2Str(map.get("A02043"));          //决定或批准任职的日期

                String A02004 = ParticularUtils.trim(map.get("A02004"), "");// 机构隶属关系Code
                String A02009 = ParticularUtils.trim(map.get("A02009"), "");//任职机构性质类别
                String depCode = ParticularUtils.trim(map.get("DEPCODE"), "");

                //只有中专及以上学历添加到简历 学历代码为GB/T4658-2006 中专为41
                List<String> list = Arrays.asList("1", "11", "12", "13", "19", "2", "21", "22", "29", "3", "31", "32", "39", "4", "41", "42");
                if (!StringUtils.equals(eduLevel, "") && !list.contains(eduLevel)) {
                    continue;
                }

                //A02049该属性需要翻译 挂职任职 DM004
                if (StringUtils.equals(jobReason, "4")) {
                    continue;
                }
                //借用（借调）
                if (StringUtils.equals(jobReason, "42")) {
                    continue;
                }
                //跟班学习
                if (StringUtils.equals(jobReason, "43")) {
                    continue;
                }
                if (StringUtils.equals(jobReason, "41")) {
                    continue;
                }
                if (StringUtils.equals(jobReason, "44")) {
                    continue;
                }
                if (StringUtils.equals(jobReason, "45")) {
                    continue;
                }
                if (StringUtils.equals(jobReason, "46")) {
                    continue;
                }
                if (StringUtils.equals(jobReason, "47")) {
                    continue;
                }
                //挂职增加其他
                if (StringUtils.equals(jobReason, "48")) {
                    continue;
                }
//			        if (StringUtils.equals(jobReason, "9")) {
//			            continue;
//			        }

                stringBuffer.setLength(0);
                stringBuffer.append(jobDeptName);
                stringBuffer.append(jobInterName);
                stringBuffer.append(jobName);
                stringBuffer.append(jobReason);
                //如果开始结束时间为空则结束时间为截止到当前
                checkStartAndEndDate(jobStartDate.replace(".", "-"), StringUtils.isNotEmpty(jobEndDate) ? jobEndDate.replace(".", "-") : new DateTime().toString("yyyy-MM-dd"), stringBuffer);
                if (StringUtils.isNotEmpty(jobEndDate)) {
                    interval = new IntervalImpl(new Interval(new DateTime(jobStartDate.replace(".", "-")), new DateTime(jobEndDate.replace(".", "-"))));
                } else {
                    interval = new IntervalImpl(new Interval(new DateTime(jobStartDate.replace(".", "-")), new DateTime()));
                }
                //添加任职时间
                resumeStartDateStr.append(jobStartDate).append("--").append(jobEndDate);

                //添加经历内容
                if (!StringUtils.equals(a08004, "")) { // 学历集信息
                    if (StringUtils.equals(A08020, "1")) {
                        deptNameSet.add(schoolName + a08016 + a08024 + "专业学习");
                    } else {
                        continue;
                    }
                } else if (!StringUtils.equals(jobStart, "")) { //职务信息
                    deptNameSet.add(jobDeptName + EnumType.UNIT_JOB_SPLIT.value + jobInterName + jobName
                            + EnumType.UNIT_JOB_SPLIT.value + " " + A02004
                            + EnumType.UNIT_JOB_SPLIT.value + " " + A02009
                            + EnumType.UNIT_JOB_SPLIT.value + " " + depCode);
                } else {
                    continue;
                }

                //专业技术职务（同时担任行政职务和专业技术职务的需要添加到简历段中）  ///20180227修改为专业技术职务就添加到简历中
                Object a06 = attrValueContext.get("A06");
                if (a06 != null) {
                    List a06List = (List) a06;
                    if (a06List.size() > 0) {
                        /// if (StringUtils.equals(a02019, "2")) {
                        for (Object o : a06List) {
                            Map a06Map = (Map) o;
                            Object a06004 = a06Map.get("A06004");
                            String a06001a = StringUtil.obj2Str(a06Map.get("A06001_A"));
                            if (a06004 != null) {
                                DateTime dateTime = new DateTime(a06004);
                                if (interval != null && interval.contains(dateTime)) {
                                    appendStringBuilder.append(dateTime.toString("yyyy.MM")).append("评为").append(a06001a).append("；");
                                }
                            }
                        }
                        ///}
                    }
                }
                if (appendStringBuilder.indexOf("；") != -1) {
                    appendStringBuilder = new StringBuilder(appendStringBuilder.substring(0, appendStringBuilder.length() - 1));
                    appendStringBuilder.append("）");
                }

                startDateAndContentMap.put("resumeStartDate", resumeStartDateStr.toString());
            }
            if (deptNameSet.size() == 0) {
                continue;
            }
            ///////////////////////////////////////////////其间数据//////////////////////////////////////////////////////
            StringBuilder betweenStringBuilder = new StringBuilder();
            buildBetweenInfo(interval, betweenStringBuilder, attrValueContext, onlyDegreeA09Map);
            ///////////////////////////////////////////////其间数据END//////////////////////////////////////////////////////
            // shixianggui-20180411, bug: 简历合并问题
//			    resumeContentStr.append(String.join(",", combineOrganAndDuty(deptNameSet))).append(betweenStringBuilder);
            resumeContentStr.append(String.join(",", ResumeOrganHandleNew.main(deptNameSet))).append(betweenStringBuilder);
            startDateAndContentMap.put("resumeContent", resumeContentStr.toString());
            resultResumeList.add(startDateAndContentMap);
        }

        //无论是在职教育或是全日制教育，如果上学时间与工作经历不重合的，作为一条经历单独显示。  20180412 modif by wangyg
        resultResumeList = Setedu2(resultResumeList, attrValueContext);

        return resultResumeList;
/*		} catch (Exception e) {
			e.printStackTrace();
		    throw new RuntimeException("任免表生成失败:[EMP_A02或EMP_A08或EMP_A11简历]");
		}*/
    }

    //在职教育的如果和简历中所有时间段都不重复,则单独增加这条
    public List<Map<String, Object>> Setedu2(List<Map<String, Object>> resultResumeList, Map<String, Object> attrValueContext) {
        List<Map<String, Object>> newResult = new ArrayList<Map<String, Object>>();

        List<Map> needDeal = new ArrayList<Map>();
        for (Object eduInfo : eduInfoList) {
            Map eduMap = (Map) eduInfo;
            String eduType = StringUtil.obj2Str(eduMap.get("A08020"));
            if (StringUtils.isNotEmpty(eduType)) {
                if (eduType.startsWith("2")) {
                    eduType = "2";

                    String a08004 = StringUtil.replaceSpeStr(StringUtil.obj2Str(eduMap.get("A08004")), " 00:00:00.0");              //入学时间
                    String a08007 = StringUtil.replaceSpeStr(StringUtil.obj2Str(eduMap.get("A08007")), " 00:00:00.0");              //毕（肄、结）业日期

                    a08004 = new DateTime(a08004).toString("yyyy-MM") + "-01";
                    a08007 = new DateTime(a08007).toString("yyyy-MM") + "-01";

                    boolean bDealed = false;

                    if (!a08004.isEmpty() && !a08007.isEmpty()) {
                        for (Map<String, Object> map : resultResumeList) {
                            String dateinv = map.get("resumeStartDate").toString();
                            String[] arrdate = dateinv.split("--");

                            String StartDate = arrdate[0].replace(".", "-") + "-01";

                            if (arrdate.length == 1) {
                                if (new DateTime(a08007).getMillis() > new DateTime(StartDate).getMillis()) {
                                    bDealed = true;
                                    break;
                                }
                                continue;
                            }

                            String EndDate = arrdate[1].replace(".", "-") + "-01";

                            if (new DateTime(a08004).getMillis() >= new DateTime(StartDate).getMillis() && new DateTime(a08004).getMillis() < new DateTime(EndDate).getMillis()) {
                                bDealed = true;
                                break;
                            }
                            if (new DateTime(a08007).getMillis() > new DateTime(StartDate).getMillis() && new DateTime(a08007).getMillis() <= new DateTime(EndDate).getMillis()) {
                                bDealed = true;
                                break;
                            }
                        }
                        if (!bDealed) {
                            needDeal.add(eduMap);
                        }
                    }
                }
            }
        }

        Object a09 = attrValueContext.get("A09");
        try {
            for (Map eduMap : needDeal) {
                Map<String, Object> maptemp = new HashMap<String, Object>();
                String a08004 = StringUtil.replaceSpeStr(StringUtil.obj2Str(eduMap.get("A08004")), " 00:00:00.0");              //入学时间
                String a08007 = StringUtil.replaceSpeStr(StringUtil.obj2Str(eduMap.get("A08007")), " 00:00:00.0");              //毕（肄、结）业日期
                String schoolName = StringUtil.obj2Str(eduMap.get("A08014"));  //学校
                String a08024 = StringUtil.obj2Str(eduMap.get("A08024"));      //所学专业名称
                String eduName = StringUtil.obj2Str(eduMap.get("A08002_A"));   //学历名称
                String a08002b = StringUtil.obj2Str(eduMap.get("A08002_B"));   //学历代码
                String a08016 = StringUtil.obj2Str(eduMap.get("A08016"));      //院系名称
                String a08015 = StringUtil.obj2Str(eduMap.get("A08015"));      //班级
                String a08021 = StringUtil.obj2Str(eduMap.get("A08021"));      //学历从学单位类别
                String eduType = StringUtil.obj2Str(eduMap.get("A08020"));     //教育类别


                //校验日期
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(schoolName);
                stringBuffer.append(a08016);
                stringBuffer.append(a08024);
                checkStartAndEndDate(a08004, a08007, stringBuffer);
                //党校标识
                List<String> list = Arrays.asList("7", "71", "72", "73", "74", "75", "76", "77", "79", "8", "81", "82", "83");
                DateTime dateTime = new DateTime(a08004);
                String eduStartDate = dateTime.toString("yyyy.MM");
                String eduEndDate = "";
                if (StringUtils.isNotEmpty(a08007)) {
                    eduEndDate = new DateTime(a08007).toString("yyyy.MM");
                }
                StringBuilder temp = new StringBuilder();

                //如果学历名称不为空
                if (StringUtils.isNotEmpty(eduName)) {
                    //党校学习经历
                    if (list.contains(a08021)) {
                        //党校研究生学历
                        if (StringUtils.equals(a08002b, "13")) { //研究生
                            temp.append(eduStartDate).append("--").append(eduEndDate).append(schoolName).append("在职研究生班").append(a08024).append("专业学习");
                        } else if (a08002b.equals("11") || a08002b.equals("12")) { //硕士，博士研究生
                            String a09001a = "";
                            if (a09 != null) {
                                List a09List = (List) a09;
                                if (a09List.size() > 0) {
                                    for (Object o : a09List) {
                                        Map a09Map = (Map) o;
                                        String a09007 = StringUtil.obj2Str(a09Map.get("A09007"));
                                        if (StringUtils.equals(schoolName, a09007)) {
                                            a09001a = StringUtil.obj2Str(a09Map.get("A09001_A"));
                                        }
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(a09001a)) {
                                temp.append(schoolName).append(a08016).append(a08024).append("专业研究生学习，获").append(a09001a);
                            } else {
                                temp.append(schoolName).append(a08016).append(a08024).append("专业研究生学习");
                            }
                        } else {
                            temp.append(schoolName).append(a08024).append("专业学习");
                        }
                    } else if (StringUtils.equals(a08021, "28")) {
                        temp.append("参加").append(schoolName).append("自学考试").append(a08024).append("专业学习");
                    } else if (StringUtils.equals(a08002b, "42")) {
                        temp.append("在").append(schoolName).append(a08024).append("专业学习");

                    } else if (StringUtils.equals(eduType, "29")) {
                        temp.append(eduEndDate).append("参加").append(schoolName).append("自学考试").append(a08024).append("专业学习");
                    } else {
                        temp.append(schoolName).append(a08016).append(a08024).append("专业学习");
                    }
                } else if (StringUtils.isEmpty(eduName)) {
                    temp.append(schoolName).append(a08016).append(a08024).append(a08015).append("学习");
                }

                if (checkGetTwoRecordOneSchool(schoolName, a08016, a08024, eduName, eduInfoList)) {
                    temp.append("<" + eduName + ">");
                }

                String isRealse = StringUtils.equals(eduType, "21") ? "<脱产>" : "";  //是否为脱产学习
                temp.append(isRealse);
                maptemp.put("resumeContent", temp.toString());
                maptemp.put("resumeStartDate", eduStartDate + "--" + eduEndDate);
                newResult.add(maptemp);
            }
            newResult.addAll(resultResumeList);
            return newResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_这个人的学历期间信息]");
        }
    }

    /**
     * shixianggui-20180417, bug-377 学历学位问题, 只获得学位, 未获取到学历也是要显示在简历中;
     * 获取只有学位而没有学历信息
     */
    public Map<String, Map> getOnlyDegreeA09Map(Map<String, Object> attrValueContext) {
        Object a09 = attrValueContext.get("A09");
        Map<String, Map> onlyDegreeA09Map = new HashMap<>();
        if (a09 instanceof List<?>) {
            List<?> a09List = (List<?>) a09;
            Map<?, ?> a09Map = null;
            for (Object a09Obj : a09List) {
                if (a09Obj instanceof Map<?, ?>) {
                    a09Map = (Map<?, ?>) a09Obj;
                    // 在职教育
                    if (ParticularUtils.trim(a09Map.get("A09081"), "").startsWith("2")) {
                        // 学位授予单位
                        onlyDegreeA09Map.put(ParticularUtils.trim(a09Map.get("A09007"), ""), a09Map);
                    }
                }
            }
        }

        Map<?, ?> eduMap = null;
        String A08014 = null;//学校
        for (Object eduInfo : eduInfoList) {
            if (eduInfo instanceof Map<?, ?>) {
                eduMap = (Map<?, ?>) eduInfo;
                if (!eduMap.isEmpty()) {
                    // A08020教育类型: 2-在职教育
                    if (ParticularUtils.trim(eduMap.get("A08020"), "").startsWith("2")) {
                        A08014 = ParticularUtils.trim(eduMap.get("A08014"), "");
                        if (onlyDegreeA09Map.containsKey(A08014)) {
                            onlyDegreeA09Map.remove(A08014);
                        }
                    }
                }
            }
        }
        return onlyDegreeA09Map;
    }

    // shixianggui-20180417, bug-377 学历学位问题, 只获得学位, 未获取到学历也是要显示在简历中; 添加入参 Map<String, Map> onlyDegreeA09Map
    public void buildBetweenInfo(IntervalImpl interval, StringBuilder betweenStringBuilder, Map<String, Object> attrValueContext, Map<String, Map> onlyDegreeA09Map) throws Exception {

        if (interval == null) {
            betweenStringBuilder.setLength(0);
            return;
        }
        String space = "                  ";
        /**
         * 学历学位信息
         */
        //学位信息
        Object a09 = attrValueContext.get("A09");

        //按A08020教育类型分组（1：全日制教育，2：在职教育）
        Map<String, List<Map>> eduMapList = new HashMap<String, List<Map>>(2);
        for (Object eduInfo : eduInfoList) {
            Map eduMap = (Map) eduInfo;
            String eduType = StringUtil.obj2Str(eduMap.get("A08020"));
            if (StringUtils.isNotEmpty(eduType)) {
                if (eduType.startsWith("2")) {
                    eduType = "2";
                }
                List<Map> maps = eduMapList.get(eduType);
                if (maps == null) {
                    List<Map> temp = new ArrayList<>();
                    temp.add(eduMap);
                    eduMapList.put(eduType, temp);
                } else {
                    maps.add(eduMap);
                    eduMapList.put(eduType, maps);
                }
            }
        }

        SetList strList = new SetList();
        SetList corssList = new SetList();
        List<Map> inServiceEdu = eduMapList.get("2");
        try {
            if (inServiceEdu != null) {
                for (Map eduMap : inServiceEdu) {
                    String a08004 = StringUtil.replaceSpeStr(StringUtil.obj2Str(eduMap.get("A08004")), " 00:00:00.0");              //入学时间
                    String a08007 = StringUtil.replaceSpeStr(StringUtil.obj2Str(eduMap.get("A08007")), " 00:00:00.0");              //毕（肄、结）业日期
                    String schoolName = StringUtil.obj2Str(eduMap.get("A08014"));  //学校
                    String a08024 = StringUtil.obj2Str(eduMap.get("A08024"));      //所学专业名称
                    String eduName = StringUtil.obj2Str(eduMap.get("A08002_A"));   //学历名称
                    String a08002b = StringUtil.obj2Str(eduMap.get("A08002_B"));   //学历代码
                    String a08016 = StringUtil.obj2Str(eduMap.get("A08016"));      //院系名称
                    String a08015 = StringUtil.obj2Str(eduMap.get("A08015"));      //班级
                    String a08021 = StringUtil.obj2Str(eduMap.get("A08021"));      //学历从学单位类别
                    String eduType = StringUtil.obj2Str(eduMap.get("A08020"));     //教育类别

                    IntervalImpl intervaltemp;
                    if (StringUtils.isNotEmpty(a08007)) {
                        intervaltemp = new IntervalImpl(new Interval(new DateTime(a08004.replace(".", "-")), new DateTime(a08007.replace(".", "-"))));
                    } else {
                        intervaltemp = new IntervalImpl(new Interval(new DateTime(a08004.replace(".", "-")), new DateTime()));
                    }
                    if (interval.getStartMillis() == intervaltemp.getStartMillis() && interval.getEndMillis() == intervaltemp.getEndMillis()) {
                        continue;
                    }

                    //校验日期
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(schoolName);
                    stringBuffer.append(a08016);
                    stringBuffer.append(a08024);
                    checkStartAndEndDate(a08004, a08007, stringBuffer);
                    //党校标识
                    List<String> list = Arrays.asList("7", "71", "72", "73", "74", "75", "76", "77", "79", "8", "81", "82", "83");
                    DateTime dateTime = new DateTime(a08004);
                    String eduStartDate = dateTime.toString("yyyy.MM");
                    String eduEndDate = "";
                    if (StringUtils.isNotEmpty(a08007)) {
                        eduEndDate = new DateTime(a08007).toString("yyyy.MM");
                    }
                    StringBuilder temp = new StringBuilder();

                    //如果学历名称不为空
                    if (StringUtils.isNotEmpty(eduName)) {
                        //党校学习经历
                        if (list.contains(a08021)) {
                            //党校研究生学历
                            if (StringUtils.equals(a08002b, "13")) { //研究生
                                temp.append(eduStartDate).append("--").append(eduEndDate).append(schoolName).append("在职研究生班").append(a08024).append("专业学习");
                            } else if (a08002b.equals("11") || a08002b.equals("12")) { //硕士，博士研究生
                                String a09001a = "";
                                if (a09 != null) {
                                    List a09List = (List) a09;
                                    if (a09List.size() > 0) {
                                        for (Object o : a09List) {
                                            Map a09Map = (Map) o;
                                            String a09007 = StringUtil.obj2Str(a09Map.get("A09007"));
                                            if (StringUtils.equals(schoolName, a09007)) {
                                                a09001a = StringUtil.obj2Str(a09Map.get("A09001_A"));
                                            }
                                        }
                                    }
                                }
                                if (StringUtils.isNotEmpty(a09001a)) {
                                    temp.append(eduStartDate).append("--").append(eduEndDate);
                                    temp.append(schoolName).append(a08016).append(a08024).append("专业研究生学习，获").append(a09001a);
                                } else {
                                    temp.append(eduStartDate).append("--").append(eduEndDate);
                                    temp.append(schoolName).append(a08016).append(a08024).append("专业研究生学习");
                                }
                            } else {
                                temp.append(eduStartDate).append("--").append(eduEndDate)
                                        .append(schoolName).append(a08024).append("专业学习");
                            }
                        } else if (StringUtils.equals(a08021, "28")) {
                            temp.append(eduStartDate).append("--").append(eduEndDate)
                                    .append("参加").append(schoolName).append("自学考试").append(a08024).append("专业学习");
                        } else if (StringUtils.equals(a08002b, "42")) {
                            temp.append(eduStartDate).append("--").append(eduEndDate)
                                    .append("在").append(schoolName).append(a08024).append("专业学习");

                            //shixianggui-20180319, 当教育类别 A08020=29(在职自学)时, 在简历上需要这般显示: xxxx.xx参加school自学考试xxx专业学习
                        } else if (StringUtils.equals(eduType, "29")) {
                            temp.append(eduEndDate).append("参加").append(schoolName).append("自学考试").append(a08024).append("专业学习");
                        } else {
                            // shixianggui-20180410, bug377: 在职教育院系名称有的不能在简历中体现; .append(a08016)
                            temp.append(eduStartDate).append("--").append(eduEndDate)
                                    .append(schoolName).append(a08016).append(a08024).append("专业学习");
                        }
                    } else if (StringUtils.isEmpty(eduName)) {
                        // shixianggui-20180319, 新的规范要求: 在职培训学历为空的, 则前缀不需要"在"字
//			            temp.append(eduStartDate).append("--").append(eduEndDate)
//			                    .append("在").append(schoolName).append(a08016).append(a08024).append(a08015).append("学习");
                        temp.append(eduStartDate).append("--").append(eduEndDate).append(schoolName).append(a08016).append(a08024).append(a08015).append("学习");
                    }

                    /**
                     * 如果是同一学校同一系同一专业先后取得不同层次学历的，在学习经历的描述后加括号注明。
                     * 如：“XXXX.XX—XXXX.XX xx学校xx系xx专业学习<大专>或<本科>”
                     */
                    if (checkGetTwoRecordOneSchool(schoolName, a08016, a08024, eduName, eduInfoList)) {
                        temp.append("<" + eduName + ">");
                    }

                    String isRealse = StringUtils.equals(eduType, "21") ? "<脱产>" : "";  //是否为脱产学习
                    temp.append(isRealse);

                    if (interval.contains(dateTime) && interval.contains(new DateTime(StringUtils.isEmpty(a08007) ? new DateTime() : a08007))) {
                        strList.add(temp.toString());
                    } else if (!interval.contains(dateTime) && interval.containsLeftGt(new DateTime(StringUtils.isEmpty(a08007) ? new DateTime() : a08007))) {
                        corssList.add(temp.toString());
                    }
                }
            }

            // shixianggui-20180417, bug-377 学历学位问题, 只获得学位, 未获取到学历也是要显示在简历中
            if (onlyDegreeA09Map != null && !(onlyDegreeA09Map.isEmpty())) {
                Set<String> keySet = onlyDegreeA09Map.keySet();
                Map a09Map = null;
                String A09003 = null;//入学时间
                DateTime A09003DateTime = null;
                String A09004 = null;// 学位授予日期
                Date A09004Date = null;
                for (String key : keySet) {
                    a09Map = onlyDegreeA09Map.get(key);
                    A09003 = ParticularUtils.trim(a09Map.get("A09003"), "");
                    A09004 = ParticularUtils.trim(a09Map.get("A09004"), "");
                    if (!"".equals(A09004)) {
                        A09004Date = ParticularUtils.getDateParse(A09004, ParticularUtils.PATTERN2, "yyyy.MM");
                        if (A09004Date != null) {
                            if ("".equals(A09003)) {
                                if (interval.getStartMillis() < A09004Date.getTime() && interval.getEndMillis() >= A09004Date.getTime()) {
                                    strList.add("未填写--" + ParticularUtils.getDataFormat(A09004Date, "yyyy.MM") + " " + ParticularUtils.trim(a09Map.get("A09007"), "") + ParticularUtils.trim(a09Map.get("A09001_A"), ""));
                                }
                            } else {
                                A09003DateTime = new DateTime(A09003.replace(" 00:00:00.0", ""));
                                if (interval.contains(A09003DateTime) && interval.contains(new DateTime(A09004.replace(" 00:00:00.0", "")))) {
                                    strList.add(A09003DateTime.toString("yyyy.MM") + "--" + ParticularUtils.getDataFormat(A09004Date, "yyyy.MM") + " " + ParticularUtils.trim(a09Map.get("A09007"), "") + ParticularUtils.trim(a09Map.get("A09001_A"), ""));
                                } else if (!interval.contains(A09003DateTime) && interval.containsLeftGt(new DateTime(A09004.replace(" 00:00:00.0", "")))) {
                                    corssList.add(A09003DateTime.toString("yyyy.MM") + "--" + ParticularUtils.getDataFormat(A09004Date, "yyyy.MM") + " " + ParticularUtils.trim(a09Map.get("A09007"), "") + ParticularUtils.trim(a09Map.get("A09001_A"), ""));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_这个人的学历期间信息]");
        }

        /**
         * 教育培训信息
         */
        try {
            for (Object trainInfo : trainInfoList) {
                Map trainMap = (Map) trainInfo;
                String trainStartDate = StringUtil.obj2Str(trainMap.get("A11007"));                      //学习（培训、进修）起始日期
                String trainEndDate = StringUtil.obj2Str(trainMap.get("A11011"));                        //学习（培训、进修）结束日期
                String trainDeptName = StringUtil.obj2Str(trainMap.get("A11021_A")); //在学单位名称
                String trainClassName = StringUtil.obj2Str(trainMap.get("A11031"));  //学习（培训、进修）班名称
                String trainMajorName = StringUtil.obj2Str(trainMap.get("A11037"));  //学习（培训、进修）专业名称
                double trainDays = 0;
                if (trainMap.get("A11057") != null && trainMap.get("A11057") != "") {
                    trainDays = Double.valueOf(StringUtil.obj2Str(trainMap.get("A11057")));
                }
                //参加的培训或进修的总时长（以天数为单位）
                if (StringUtils.isNotEmpty(trainStartDate)) {
                    trainStartDate = trainStartDate.substring(0, 7);
                    DateTime dateTime = new DateTime(trainStartDate);

                    String trainStartDateTimeStr = dateTime.toString("yyyy.MM");
                    String trainEndDateTimeStr;
                    if (trainDays >= 60) {
                        if (StringUtils.isNotEmpty(trainEndDate)) {
                            trainEndDate = trainEndDate.substring(0, 7);
                            DateTime trainEndDateTime = new DateTime(trainEndDate);

                            trainEndDateTimeStr = trainEndDateTime.toString("yyyy.MM");
                            if (interval.contains(dateTime) && interval.contains(trainEndDateTime)) {
                                strList.add(new StringBuilder().append(trainStartDateTimeStr).append("--").append(trainEndDateTimeStr)
                                        .append("在").append(trainDeptName).append(trainClassName).append(trainMajorName).append("学习").toString());
                            } else if (!interval.contains(dateTime) && interval.containsLeftGt(trainEndDateTime)) { //结束日期在时间断内，开始日期不在
                                corssList.add(new StringBuilder().append(trainStartDateTimeStr).append("--").append(trainEndDateTimeStr)
                                        .append("在").append(trainDeptName).append(trainClassName).append(trainMajorName).append("学习").toString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_这个人的教育培训信息]");
        }

        /**
         * 挂职信息
         */
        try {

            for (Object jobInfo : jobInfoList) {
                Map map = (Map) jobInfo;
                String jobReason = StringUtil.obj2Str(map.get("A02049"));       //职务说明
                //A02049该属性需要翻译 挂职任职 DM004
                if (!StringUtils.equals(jobReason, "4") && !StringUtils.equals(jobReason, "41") && !StringUtils.equals(jobReason, "42") && !StringUtils.equals(jobReason, "43") && !StringUtils.equals(jobReason, "44") && !StringUtils.equals(jobReason, "45") && !StringUtils.equals(jobReason, "46") && !StringUtils.equals(jobReason, "47") && !StringUtils.equals(jobReason, "48")) {
                    continue;
                }
                Object jobStartDate = map.get("A02043");                        //决定或批准任职的日期
                Object jobEndDate = map.get("A02065");                          //决定或批准免职的日期
                String jobDeptName = StringUtil.obj2Str(map.get("A02001_A"));   //任职机构名称
                String jobName = StringUtil.obj2Str(map.get("A02016_A"));       //职务

                String PerAdd = "挂职任";
                String EndAdd = "";
                if (StringUtils.equals(jobReason, "42")) {
                    // shixianggui-20180312, 根据赵科的要求: 去掉 借用/借调 其中一个;
                    //PerAdd ="借用（借调）到";
                    PerAdd = "借调到";
                    EndAdd = "工作";
                }
                if (StringUtils.equals(jobReason, "43")) {
                    PerAdd = "在";
                    EndAdd = "跟班学习";
                }
                ///20180302 wft
			  /*            其他类别具体表述形式如下：
			
			            借用 ：  ( XXXX年XX月 借用到XX单位工作)
			            跟班学习：( XXXX年XX月 在XX单位跟班学习)
			            实践锻炼：( XXXX年XX月 在XX单位参加实践锻炼)
			            支教（支医）：( XXXX年XX月 在XX单位支教)
			            抽调：( XXXX年XX月 抽调到XX单位工作)
			            驻村：( XXXX年XX月 到XX单位驻村)
			            扶贫工作队：（( XXXX年XX月 参加XX单位扶贫工作队)*/
                if (StringUtils.equals(jobReason, "44")) {
                    PerAdd = "在";
                    EndAdd = "实践锻炼";
                }
                if (StringUtils.equals(jobReason, "45")) {
                    PerAdd = "在";
                    if (!jobName.contains("支教") && !jobName.contains("支医")) {
                        EndAdd = "支教（支医）";
                    } else {
                        EndAdd = "";
                    }
                }
                if (StringUtils.equals(jobReason, "46")) {
                    PerAdd = "抽调到";
                    EndAdd = "工作";
                }
                if (StringUtils.equals(jobReason, "47")) {
                    PerAdd = "到";
                    EndAdd = "驻村";
                }
                if (StringUtils.equals(jobReason, "48")) {
                    PerAdd = "";
                    EndAdd = "";
                }
                if (StringUtils.equals(jobReason, "41")) {
                    PerAdd = "参加";
                    // EndAdd ="任"+jobName;
                }
//			    if(StringUtils.equals(jobReason, "9"))//其他任职原因，赵科规则:在后面加职务名称即可
//			    {
//			        PerAdd ="";
//			        EndAdd =" ";
//			        jobDeptName="";
//			    }
                //// TODO: 2018/11/27 在简历中如果存在多条挂职任职信息，需要按照相应规则进行合并

                if (StringUtils.isNotEmpty(StringUtil.obj2Str(jobStartDate))) {
                    DateTime startTime = new DateTime(jobStartDate);
                    String startTimeStr = startTime.toString("yyyy.MM");
                    boolean inNowTime = false;//现在是否还挂职标识
                    if (StringUtils.isEmpty(StringUtil.obj2Str(jobEndDate))) {
                        jobEndDate = new DateTime().toString("yyyy-MM-dd");
                        inNowTime = true;
                    }
                    DateTime endDateTime = new DateTime(jobEndDate);
                    String endTimeStr = endDateTime.toString("yyyy.MM");
                    if (interval.contains(startTime) && interval.contains(endDateTime) && !inNowTime) {
                        strList.add(new StringBuilder().append(startTimeStr).append("--").append(endTimeStr)
                                .append(PerAdd).append(jobDeptName).append(EnumType.UNIT_JOB_SPLIT.value).append(jobName).append(EndAdd).toString());
                    } else if (!interval.contains(startTime) && interval.containsLeftGt(endDateTime)) { //结束日期在时间断内，开始日期不在
                        corssList.add(new StringBuilder().append(startTimeStr).append("--").append(endTimeStr)
                                .append(PerAdd).append(jobDeptName).append(EnumType.UNIT_JOB_SPLIT.value).append(jobName).append(EndAdd).toString());
                    } else if (interval.contains(startTime) && interval.contains(endDateTime) && inNowTime) {
                        strList.add(new StringBuilder().append(startTimeStr).append("--").append("至今")
                                .append(PerAdd).append(jobDeptName).append(EnumType.UNIT_JOB_SPLIT.value).append(jobName).append(EndAdd).toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[简历_这个人的挂职信息]");
        }
        ///20180228  wft    把专业技术职务添加到简历中
        for (Object jobInfo : professionalInfoList) {
            Map map = (Map) jobInfo;

            // shixianggui-20180326, 任免表 简历bug: 被评为时间由 '聘任专业技术职务起始日期' 改为 '获得专业技术任职资格日期'
//        	  Object professStartDate = map.get("A06025");							//聘任专业技术职务起始日期
            Object professStartDate = map.get("A06004");//'获得专业技术任职资格日期'

            String professName = StringUtil.obj2Str(map.get("A06001_A"));            //专业技术任职资格名称
            if (professStartDate != null) {
                DateTime dateTime = new DateTime(professStartDate);
                if (interval != null && interval.containsLeftGt(dateTime)) {
                    strList.add(new StringBuilder().append(dateTime.toString("yyyy.MM")).append("评为").append(professName).toString());
                }
            }
        }
        if (strList.size() > 0) {
            betweenStringBuilder.append("（其间：").append(String.join(";", combineOrganAndDuty(strList))).append(")");
        }

        if (corssList.size() > 0) {
            betweenStringBuilder.append("\n" + space + "(").append(String.join(";", combineOrganAndDuty(corssList))).append(")");
        }
    }

    /**
     * 按职务主次序 排序
     *
     * @param maps 每条记录中包主次序字段
     */
    private void sortByJobOrder(List<Map> maps) {
        //按职务主次序排序
        maps.sort((Map o1, Map o2) -> {
            Integer integer = NumberUtils.toInt(StringUtil.obj2Str(o1.get("A02023")));
            Integer integer2 = NumberUtils.toInt(StringUtil.obj2Str(o2.get("A02023")));
            if (integer > integer2) {
                return 1;
            } else if (integer < integer2) {
                return -1;
            } else {
                return 0;
            }
        });
    }

    private boolean getMergeIsPass(String jobDeptName) throws IOException, JSONException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream("./config/merge_dic.json");// 凭证文件
        InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        String dic = "";
        BufferedReader bufferedReader = new BufferedReader(reader);
        dic = bufferedReader.readLine();
        inputStream.close();

        JSONArray jsonArray = new JSONArray(dic);
        JSONObject jsonObject = (JSONObject) jsonArray.get(jsonArray.length() - 1);
        JSONArray cities = (JSONArray) jsonObject.get("cities");
        for (int i = 0; i < cities.length(); i++) {
            String s = StringUtil.obj2Str(cities.get(i));
            if (jobDeptName.startsWith(s)) {
                return true;
            }
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray cities1 = (JSONArray) ((JSONObject) jsonArray.get(i)).get("cities");
            for (int j = 0; j < cities1.length(); j++) {
                String s = StringUtil.obj2Str(cities1.get(j));
                if (jobDeptName.startsWith(s)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 检查 开始日期是否大于结束日期
     *
     * @param start
     * @param end
     */
    public void checkStartAndEndDate(String start, String end, StringBuffer buffer) {
        if (StringUtils.isEmpty(start)) {
            throw new RuntimeException("任免表生成失败:干部的简历的开始日期有误【" + buffer.toString() + "】 其中开始日期为【" + start + "】 请修改后重试！");
        }
        if (StringUtils.isNotEmpty(end) && new DateTime(start).getMillis() > new DateTime(end).getMillis()) {
            throw new RuntimeException("任免表生成失败:开始日期[" + start + "]--结束日期[" + end + "]  " + "的" + buffer.toString() + "的开始日期大于结束日期，请修改后重试！");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
