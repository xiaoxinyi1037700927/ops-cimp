package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.newskysoft.iimp.word.pattern.IntervalImpl;
import com.newskysoft.iimp.word.pattern.SetList;
import com.newskysoft.iimp.word.pattern.data.xinjiang.AttrValue;
import com.newskysoft.iimp.word.pattern.data.xinjiang.NameAttrValue;
import com.newskysoft.iimp.word.pattern.data.xinjiang.ResumeAttrVauleHandle;
import com.newskysoft.iimp.word.service.ExportService;
import com.newskysoft.iimp.word.util.CodeTranslateUtil;
import com.newskysoft.iimp.word.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rain chen on 2017/10/10.
 * 简历
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class ResumeAttrValue extends ResumeAttrVauleHandle implements AttrValue {
    private static final Logger logger = LoggerFactory.getLogger(ResumeAttrValue.class);
    
    private final String key = "resume";
    private final int order = 20;
//    private static int count = 0;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {

    	//个人简历子集
    	final String resumeInfoSql = "SELECT * FROM EMP_A070 EMP_A070 WHERE EMP_ID = '%s'  and status = 0  ORDER BY EMP_A070.A070001 ASC";
        // 学习（培训、进修）情况
        final String trainInfoSql = "SELECT * FROM Emp_A155 Emp_A155 WHERE EMP_ID = '%s'  and status = 0  ORDER BY Emp_A155.A155005 DESC";
        //参加重要工作
        final String importantActiveInfoSql = "SELECT * FROM EMP_A145 EMP_A145 WHERE EMP_ID = '%s'  and status = 0 ORDER BY A145005 ASC";
        //任职情况
        final String jobInfoSql = "SELECT * FROM Emp_A010 Emp_A010 WHERE EMP_ID = '%s'  and status = 0  ORDER BY Emp_A010.A010005,Emp_A010.A010125";
        //教育信息
        final String eduInfoSql = "SELECT * FROM Emp_A005 Emp_A005 WHERE EMP_ID = '%s'  and status = 0 and A005035 is not null ORDER BY Emp_A005.A005015 ASC";

        List resumeInfoList = exportWordService.findBySQL(String.format(resumeInfoSql, empId));
        List importantActiveInfoList = exportWordService.findBySQL(String.format(importantActiveInfoSql, empId));
        List trainInfoList = exportWordService.findBySQL(String.format(trainInfoSql, empId));
        List jobInfoList = exportWordService.findBySQL(String.format(jobInfoSql, empId));
        List eduInfoList = exportWordService.findBySQL(String.format(eduInfoSql, empId));
        
        if (trainInfoList == null) {
            trainInfoList = new ArrayList<Map>();
        }
        attrValueContext.put("A155", trainInfoList);
       
        this.setResumeInfoList(resumeInfoList);
        this.setTrainInfoList(trainInfoList);
        this.setImportantActiveInfoList(importantActiveInfoList);
        this.setJobInfoList(jobInfoList);
        this.setEduInfoList(eduInfoList);

        List<Map<String, Object>> resultResumeList = new ArrayList<Map<String, Object>>();
        StringBuffer stringBuffer = new StringBuffer();
        List<String> emptyDateKeys = new ArrayList<>(); //存放简历结束日期为空的经历


        //第一步 合并符合条件的任职经历和学历信息
        //工作开始时间和结束时间相同的进行分组
        //例如
        //	2007/1/1	2008/12/1	贵州省大方县人民检察院	检察长
        //	2007/1/1	2008/12/1	贵州省大方县人民检察院	检察委员会委员
        //	2007/1/1	2008/12/1	贵州省大方县人民检察院	党组书记
        //将会被分组为：2007.01.01_2008.12.01 -> List<Map>
        //此需求为同一人在同一时间段内任多个职务
        Map<String, List<Map>> jobGroupByStartTime = new LinkedHashMap<String, List<Map>>();
        //把个人简历转成map
        handleDateToKey(jobGroupByStartTime, resumeInfoList, emptyDateKeys);
        //把学历培训转换成map
        handleDateToKey(jobGroupByStartTime, trainInfoList, emptyDateKeys);
        //把学历信息转换成map
        handleDateToKey(jobGroupByStartTime, importantActiveInfoList, emptyDateKeys);
        //把任参加重要工作转换成Map
//        if(count > 1) {
//        	System.out.println();
//        }
        handleDateToKey(jobGroupByStartTime, jobInfoList, emptyDateKeys);
        //把学历信息转换成map
        handleDateToKey(jobGroupByStartTime, eduInfoList, emptyDateKeys);

        //处理简历的结束日期为空的简历
        try {
            handResumeEndDateIsEmpty(emptyDateKeys, jobGroupByStartTime);
        } catch (Exception e1) {
            throw new AttrValueException("处理简历的结束日期为空的简历失败",e1);
        }
        //处理简历日期跨段
        Map<String, List<Map>> jobGroupByCorssTime = handelResumeDateCorss(jobGroupByStartTime);

        //第二步处理经历的增任职务
        try {
            handelDateSameAndAdd(jobGroupByCorssTime, jobGroupByStartTime);
        } catch (ClassNotFoundException | IOException e1) {
            throw new AttrValueException("处理简历增任职务失败",e1);
        }

        //按起始日期排序
        List<Map.Entry<String, List<Map>>> keySet = sortByResumeDate(jobGroupByCorssTime);

        //第三步 合并增任职务及时间相同的职务
        for (Map.Entry<String, List<Map>> entry : keySet) {
            Map<String, Object> startDateAndContentMap = new HashMap<String, Object>();
            StringBuilder resumeContentStr = new StringBuilder();
            IntervalImpl interval = null;

            //获得 某个时间的职务信息集
            List<Map> maps = entry.getValue();

            //按职务主次序号排序
//            sortByJobOrder(maps);

            //获取时间
            String[] startAndEndStrs = entry.getKey().split("_");
            String jobStartDate = startAndEndStrs[0];
            String jobEndDate = "";
            if (startAndEndStrs.length > 1) {
                jobEndDate = startAndEndStrs[1];
            }

            SetList<String> deptNameSet = new SetList<>();
            for (Map map : maps) {
                StringBuilder appendStringBuilder = new StringBuilder("（");
                StringBuilder resumeStartDateStr = new StringBuilder();
                
                String resumeStartTime = StringUtil.obj2Str(map.get("A070001"));//简历--开始时间
                String resumeEndTime = StringUtil.obj2Str(map.get("A070005"));//简历--结束时间
                String resumeDepName = StringUtil.obj2Str(map.get("A070010_B"));//简历--所在单位
                String resumeJob = StringUtil.obj2Str(map.get("A070015"));//简历--职位
                

                String jobDeptName = StringUtil.obj2Str(map.get("A010010_B"));   //任职机构名称
                String jobNameCode = StringUtil.obj2Str(map.get("A010001"));       //职务名称
                String jobName = CodeTranslateUtil.codeToName("BT0140", jobNameCode, exportWordService);
                String jobReason = StringUtil.obj2Str(map.get("A010020"));       //任职原因  任职类别
//                String a02019 = StringUtil.obj2Str(map.get("A02019"));          //任职范围类别  没有

                String schoolName = StringUtil.obj2Str(map.get("A005040"));  //学校
//                String a08016 = StringUtil.obj2Str(map.get("A08016"));      //院系名称   没有
                String A005010B = StringUtil.obj2Str(map.get("A005010_B"));      //所学专业名称
                String A005001B = StringUtil.obj2Str(map.get("A005001_B"));   //学历名称
                String eduLevel = StringUtil.obj2Str(map.get("A005001_A"));  //学历代码
                String A005025 = StringUtil.obj2Str(map.get("A005025"));  //教育类别

                //根据一下两个日期判断 数据来自职务信息集还是学历信息集
                String A005015 = StringUtil.obj2Str(map.get("A005015"));              //入学时间
                String jobStart = StringUtil.obj2Str(map.get("A010005"));          //决定或批准任职的日期

                //只有中专及以上学历添加到简历 学历代码为GB/T4658-2006 中专为41 代码同新疆
                List<String> list = Arrays.asList("1", "11", "12", "13", "19", "2", "21", "22", "29", "3", "31", "32", "39", "4", "41");
                if (!StringUtils.equals(eduLevel, "") && !list.contains(eduLevel)) {
                    continue;
                }

                //A02049该属性需要翻译 挂职任职 DM004
                //A010020该属性需要翻译 挂职 BT0150  代码71/排除挂职
                if (StringUtils.equals(jobReason, "71")) {
                    continue;
                }

                stringBuffer.setLength(0);
                stringBuffer.append(resumeDepName);
                stringBuffer.append(resumeJob);
                //如果开始结束时间为空则结束时间为截止到当前
                if (StringUtils.isNotEmpty(jobEndDate)) {
                	if(StringUtils.isNotEmpty(jobStartDate)){
                        checkStartAndEndDate(jobStartDate.replace(".", "-"), jobEndDate.replace(".", "-"), stringBuffer);
                        try {
                            interval = new IntervalImpl(new Interval(new DateTime(jobStartDate.replace(".", "-")), new DateTime(jobEndDate.replace(".", "-"))));
                        }catch(Exception e) {
                            interval = new IntervalImpl(new Interval(new DateTime(),new DateTime()));
                        }
                	}else{
                		jobStartDate = jobEndDate;
                		 checkStartAndEndDate(jobStartDate.replace(".", "-"), jobEndDate.replace(".", "-"), stringBuffer);
                		 try {
                		     interval = new IntervalImpl(new Interval(new DateTime(jobStartDate.replace(".", "-")), new DateTime(jobEndDate.replace(".", "-"))));
                		 }catch(Exception e) {
                             interval = new IntervalImpl(new Interval(new DateTime(),new DateTime()));
                         }
                	}
                } else {
                    try{
                    	interval = new IntervalImpl(new Interval(new DateTime(jobStartDate.replace(".", "-")), new DateTime()));
                    }catch(Exception e){
//                    	System.out.println("错误时间和对应字段jobStartDate:"+jobStartDate);
                    	interval = new IntervalImpl(new Interval(new DateTime(), new DateTime()));
                    }
                }
                //添加任职时间
                resumeStartDateStr.append(jobStartDate).append("--").append(jobEndDate);

                //添加经历内容
                if(!StringUtils.equals("", resumeStartTime)){
                	deptNameSet.add(resumeDepName+resumeJob);
                }else  if (!StringUtils.equals(A005015, "")) { // 学历集信息
                    //判断是否为全日制教育
                    if (!StringUtils.equals(A005025, "1")) {
                        continue;
                    }
                    deptNameSet.add(schoolName + A005010B + "专业学习");
                } else if (!StringUtils.equals(jobStart, "")) { //职务信息
                    deptNameSet.add(jobDeptName + jobName);
                } else {
                    continue;
                }

                //专业技术职务（同时担任行政职务和专业技术职务的需要添加到简历段中）
                Object A105 = attrValueContext.get("A105");
                if (A105 != null) {
                    List A105List = (List) A105;
                    if (A105List.size() > 0) {
//                        if (StringUtils.equals(a02019, "2")) {
                            for (Object o : A105List) {
                                Map A105Map = (Map) o;
                                Object A105010 = A105Map.get("A105010");
                                String A105001B = StringUtil.obj2Str(A105Map.get("A105001_B"));
                                if (A105010 != null) {
                                    DateTime dateTime;
                                    try {
                                        dateTime = new DateTime(A105010);
                                    }catch(Exception e) {
                                        logger.error("无效的日期："+A105010,e);
                                        dateTime = new DateTime();
                                    }
                                    if (interval != null && interval.contains(dateTime)) {
                                        appendStringBuilder.append(dateTime.toString("yyyy.MM")).append("评为").append(A105001B).append("；");
                                    }
                                }
                            }
//                        }
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
            try {
                buildBetweenInfo(interval, betweenStringBuilder, attrValueContext,exportWordService);
            } catch (ParseException e) {
                throw new AttrValueException("处理简历的其间数据失败",e);
            }
            ///////////////////////////////////////////////其间数据END//////////////////////////////////////////////////////
            resumeContentStr.append(org.apache.commons.lang3.StringUtils.join(deptNameSet, "、")).append(betweenStringBuilder);
            startDateAndContentMap.put("resumeContent", resumeContentStr.toString());
            resultResumeList.add(startDateAndContentMap);
        }
//
//        if(resultResumeList.size()<1) {
//        	count++;
//        }
//        if(count > 200) {
//        	System.out.println("count:"+count);
//        }
        return resultResumeList;
    }


    public void buildBetweenInfo(IntervalImpl interval, StringBuilder betweenStringBuilder, Map<String, Object> attrValueContext,ExportService exportWordService) throws ParseException {

        if (interval == null) {
            betweenStringBuilder.setLength(0);
            return;
        }
        String space = "                  ";
        /**
         * 学历学位信息
         */
        //学位信息
        Object A005 = attrValueContext.get("A005");

        //按A08020教育类型分组（1：全日制教育，2：在职教育）
        Map<String, List<Map>> eduMapList = new HashMap<String, List<Map>>(2);
        for (Object eduInfo : eduInfoList) {
            Map eduMap = (Map) eduInfo;
            String eduType = StringUtil.obj2Str(eduMap.get("A005025"));
            if (StringUtils.isNotEmpty(eduType)) {
                if (eduType.startsWith("2")) {
                    eduType = "2";
                }
                List<Map> maps = eduMapList.get(eduType);
                if (maps == null) {
                    List<Map> temp = new ArrayList<Map>();
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
        if (inServiceEdu != null) {
            for (Map eduMap : inServiceEdu) {
                String A005015 = StringUtil.obj2Str(eduMap.get("A005015"));              //入学时间
                String A005035 = StringUtil.obj2Str(eduMap.get("A005035"));              //毕（肄、结）业日期
                String schoolName = StringUtil.obj2Str(eduMap.get("A005040"));  //学校
                String A005010B = StringUtil.obj2Str(eduMap.get("A005010_B"));      //所学专业名称
                String eduName = StringUtil.obj2Str(eduMap.get("A005001_B"));   //学历名称
                String A005001A = StringUtil.obj2Str(eduMap.get("A005001_A"));   //学历代码
         //       String a08016 = StringUtil.obj2Str(eduMap.get("A08016"));      //院系名称  没有
          //      String a08015 = StringUtil.obj2Str(eduMap.get("A08015"));      //班级  没有
                String A005020 = StringUtil.obj2Str(eduMap.get("A005020"));      //学历从学单位类别   没有
                String eduType = StringUtil.obj2Str(eduMap.get("A005025"));     //教育类别

                //校验日期
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(schoolName);
//                stringBuffer.append(a08016);
                stringBuffer.append(A005010B);
                if(StringUtils.isEmpty(A005015) && StringUtils.isNotEmpty(A005035) && StringUtils.equals(A005020, "02")){
                	A005015 = A005035;
                }
                checkStartAndEndDate(A005015, A005035, stringBuffer);
                //党校标识
                List<String> list = Arrays.asList("101", "102", "103");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                DateTime dateTime = null;
                try{
                	dateTime = new DateTime(df.parse(A005015)); 
                }catch(Exception e){
                    logger.error("无效的日期："+A005015,e);
                	dateTime = new DateTime();
                }
                String eduStartDate = dateTime.toString("yyyy.MM");
                String eduEndDate = "";
                if (StringUtils.isNotEmpty(A005035)) {
                    try{
                        eduEndDate = new DateTime(df.parse(A005035)).toString("yyyy.MM");
                    }catch(Exception e){
                        logger.error("无效的日期："+eduEndDate,e);
                        eduEndDate = new DateTime().toString("yyyy.MM");
                    }
                }
                StringBuilder temp = new StringBuilder();

                //如果学历名称不为空
                if (StringUtils.isNotEmpty(A005001A)) {
                    //党校学习经历
                    if (list.contains(A005020)) {
                        //党校研究生学历
                        if (StringUtils.equals(A005001A, "13")) { //研究生
                            temp.append(eduStartDate).append("--").append(eduEndDate).append(schoolName).append("在职研究生班").append(A005010B).append("专业学习");
                        } else if (A005001A.equals("11") || A005001A.equals("12")) { //硕士，博士研究生
                            String A005050B = "";
                            if (A005 != null) {
                                List A005List = (List) A005;
                                if (A005List.size() > 0) {
                                    for (Object o : A005List) {
                                        Map A005Map = (Map) o;
                                        String a09007 = StringUtil.obj2Str(A005Map.get("A005075"));
                                        if (StringUtils.equals(schoolName, a09007)) {
                                            A005050B = StringUtil.obj2Str(A005Map.get("A005050_B"));
                                        }
                                    }
                                }
                            }
                            if (StringUtils.isNotEmpty(A005050B)) {
                                temp.append(eduStartDate).append("--").append(eduEndDate);
                                temp.append(schoolName).append(A005010B).append("专业研究生学习，获").append(A005050B);
                            } else {
                                temp.append(eduStartDate).append("--").append(eduEndDate);
                                temp.append(schoolName).append(A005010B).append("专业研究生学习");
                            }
                        } else {
                            temp.append(eduStartDate).append("--").append(eduEndDate)
                                    .append(schoolName).append(A005010B).append("专业学习");
                        }
                    } else if (StringUtils.equals(A005020, "02")) {
                        temp.append(eduEndDate)
                                .append(schoolName).append("自考").append(A005010B).append("专业毕业");
                    } else {
                        temp.append(eduStartDate).append("--").append(eduEndDate)
                                .append(schoolName).append(A005010B).append("专业学习");
                    }
                } else if (StringUtils.isEmpty(eduName)) {
                    temp.append(eduStartDate).append("--").append(eduEndDate)
                            .append("在").append(schoolName).append(A005010B).append("学习");
                }

                /**
                 * 如果是同一学校同一系同一专业先后取得不同层次学历的，在学习经历的描述后加括号注明。
                 * 如：“XXXX.XX—XXXX.XX xx学校xx系xx专业学习<大专>或<本科>”
                 */
                if (checkGetTwoRecordOneSchool(schoolName, A005010B, eduName, eduInfoList)) {
                    temp.append("<" + eduName + ">");
                }

//                String isRealse = StringUtils.equals(eduType, "21") ? "<脱产>" : "";  //是否为脱产学习
//                temp.append(isRealse);

                if (interval.contains(dateTime) && interval.contains(new DateTime(StringUtils.isEmpty(A005035) ? new DateTime() : df.parse(A005035)))) {
                    strList.add(temp);
                } else if (!interval.contains(dateTime) && interval.containsLeftGt(new DateTime(StringUtils.isEmpty(A005035) ? new DateTime() : df.parse(A005035)))) {
                    corssList.add(temp);
                }
            }
        }
        
        /**
         * 参与重大工作信息
         */
        for (Object importantActiveInfo : importantActiveInfoList) {
            Map map = (Map) importantActiveInfo;
            Object importantActiveStartDate = map.get("A145001");                        //开始时间
            Object importantActiveEndDate = map.get("A145005");                          //结束时间
            String importantActiveDepName = StringUtil.obj2Str(map.get("A145010"));   //重大工作单位
            String importantActiveJobName = StringUtil.obj2Str(map.get("A145020"));       //职务
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotEmpty(StringUtil.obj2Str(importantActiveStartDate))) {
                DateTime startTime;
                try {
                    startTime = new DateTime(df.parse(importantActiveStartDate.toString()));
                }catch(Exception e) {
                    logger.error("无效的日期："+importantActiveStartDate,e);
                    startTime = new DateTime();
                }
                String startTimeStr = startTime.toString("yyyy.MM");
                if (StringUtils.isNotEmpty(StringUtil.obj2Str(importantActiveEndDate))) {
                    DateTime endDateTime;
                    try {
                        endDateTime = new DateTime(importantActiveEndDate);
                    }catch(Exception e) {
                        logger.error("无效的日期："+importantActiveEndDate,e);
                        endDateTime = new DateTime();
                    }
                    String endTimeStr = endDateTime.toString("yyyy.MM");
                    if (interval.contains(startTime) && interval.contains(endDateTime)) {
                        strList.add(new StringBuilder().append(startTimeStr).append("--").append(endTimeStr)
                                .append(importantActiveDepName).append(importantActiveJobName));
                    } else if (interval.containsLeftGt(endDateTime) && !interval.contains(startTime)) { //结束日期在时间断内，开始日期不在
                        corssList.add(new StringBuilder().append(startTimeStr).append("--").append(endTimeStr)
                                .append(importantActiveDepName).append(importantActiveJobName));
                    }
                }
            }
        }

        /**
         * 教育培训信息
         */
        for (Object trainInfo : trainInfoList) {
            Map trainMap = (Map) trainInfo;
            String outOfJob = StringUtil.obj2Str(trainMap.get("A155001")); //学习方式 
            String outOfJobName = "";
            if(StringUtil.isNotEmptyOrNull(outOfJob)){
            	outOfJobName = CodeTranslateUtil.codeToName("", outOfJob, exportWordService);
            }
            String trainStartDate = StringUtil.obj2Str(trainMap.get("A155005"));   //起始日期
            String trainEndDate = StringUtil.obj2Str(trainMap.get("A155010"));     //结束日期
            String trainDeptName = StringUtil.obj2Str(trainMap.get("A155035_B")); //在学单位名称
            String trainClassName = StringUtil.obj2Str(trainMap.get("A155020"));  //学习（培训、进修）班名称
            String trainMajorName = StringUtil.obj2Str(trainMap.get("A155015"));  //学习（培训、进修）专业名称
            double trainDays = Double.valueOf(StringUtil.obj2Str(trainMap.get("A155050") == null ? 0:trainMap.get("A155050")));       //参加的培训或进修的总时长（以天数为单位）
            if (StringUtils.isNotEmpty(trainStartDate)) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                DateTime dateTime;
                try {
                    dateTime = new DateTime(df.parse(trainStartDate));
                }catch(Exception e) {
                    logger.error("无效的日期："+trainStartDate,e);
                    dateTime = new DateTime();
                }
                String trainStartDateTimeStr = dateTime.toString("yyyy.MM");
                String trainEndDateTimeStr;
                if (trainDays >= 60) {
                    if (StringUtils.isNotEmpty(trainEndDate)) {
                        DateTime trainEndDateTime;
                        try {
                            trainEndDateTime = new DateTime(df.parse(trainEndDate));
                        }catch(Exception e) {
                            logger.error("无效的日期："+trainEndDate,e);
                            trainEndDateTime = new DateTime();
                        }
                        trainEndDateTimeStr = trainEndDateTime.toString("yyyy.MM");
                        if (interval.contains(dateTime) && interval.contains(trainEndDateTime)) {
                            strList.add(new StringBuilder().append(trainStartDateTimeStr).append("--").append(trainEndDateTimeStr)
                                    .append("在").append(trainDeptName).append(trainClassName).append(trainMajorName).append(outOfJobName).append("学习"));
                        } else if (interval.containsLeftGt(trainEndDateTime) && !interval.contains(dateTime)) { //结束日期在时间断内，开始日期不在
                            corssList.add(new StringBuilder().append(trainStartDateTimeStr).append("--").append(trainEndDateTimeStr)
                                    .append("在").append(trainDeptName).append(trainClassName).append(trainMajorName).append(outOfJobName).append("学习"));
                        }
                    }
                }
            }
        }

        /**
         * 挂职信息
         */
        for (Object jobInfo : jobInfoList) {
            Map map = (Map) jobInfo;
            String jobReason = StringUtil.obj2Str(map.get("A010020"));       //职务说明
            //A010020该属性需要翻译 挂职任职 BT0150
            if (!StringUtils.equals(jobReason, "71")) {
                continue;
            }
            Object jobStartDate = map.get("A010005");                        //决定或批准任职的日期
            Object jobEndDate = map.get("A010125");                          //决定或批准免职的日期
            String jobDeptName = StringUtil.obj2Str(map.get("A010010_B"));   //任职机构名称
            String jobName = StringUtil.obj2Str(map.get("A010001_B"));       //职务
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isNotEmpty(StringUtil.obj2Str(jobStartDate))) {
                DateTime startTime;
                try {
                    startTime = new DateTime(df.parse(jobStartDate.toString()));
                }catch(Exception e) {
                    logger.error("无效的日期："+jobStartDate,e);
                    startTime = new DateTime();
                }
                String startTimeStr = startTime.toString("yyyy.MM");
                if (StringUtils.isNotEmpty(StringUtil.obj2Str(jobEndDate))) {
                    DateTime endDateTime;
                    try {
                        endDateTime = new DateTime(jobEndDate);
                    }catch(Exception e) {
                        logger.error("无效的日期："+jobEndDate,e);
                        endDateTime = new DateTime();
                    }
                    String endTimeStr = endDateTime.toString("yyyy.MM");
                    if (interval.contains(startTime) && interval.contains(endDateTime)) {
                        strList.add(new StringBuilder().append(startTimeStr).append("--").append(endTimeStr)
                                .append("挂职任").append(jobDeptName).append(jobName));
                    } else if (interval.containsLeftGt(endDateTime) && !interval.contains(startTime)) { //结束日期在时间断内，开始日期不在
                        corssList.add(new StringBuilder().append(startTimeStr).append("--").append(endTimeStr)
                                .append("挂职任").append(jobDeptName).append(jobName));
                    }
                }
            }
        }

        corssList.sort(new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    String stra = a.toString();
                    String strb = b.toString();
                    ret = stra.compareTo(strb);
                    return ret;
                }
                catch(Exception e)
                {
                    return  0;
                }
            }
        });
        strList.sort(new Comparator() {
            public int compare(Object a, Object b) {
                int ret = 0;
                try {
                    String stra = a.toString();
                    String strb = b.toString();
                    ret = stra.compareTo(strb);
                    return ret;
                }
                catch(Exception e)
                {
                    return  0;
                }
            }
        });

        if(strList.size() > 0 && corssList.size() > 0){
        	betweenStringBuilder.append("（其间：").append(org.apache.commons.lang3.StringUtils.join(strList, "；")).append("）");
        	betweenStringBuilder.append("\n" + space).append("（").append(org.apache.commons.lang3.StringUtils.join(corssList, "；")).append("）");
        }else{
            if (strList.size() > 0) {
                betweenStringBuilder.append("（其间：").append(org.apache.commons.lang3.StringUtils.join(strList, "；")).append("）");
            }

            if (corssList.size() > 0) {
                betweenStringBuilder.append("\n" + space + "（").append(org.apache.commons.lang3.StringUtils.join(corssList, "；")).append("）");
            }
        }
    }

    /**
     * 按职务主次序 排序
     *
     * @param maps 每条记录中包主次序字段
     */
    private void sortByJobOrder(List<Map> maps) {
        //按职务主次序排序
        Collections.sort(maps, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                Integer integer = NumberUtils.toInt(StringUtil.obj2Str(o1.get("A010052")));
                Integer integer2 = NumberUtils.toInt(StringUtil.obj2Str(o2.get("A010052")));
                if (integer > integer2) {
                    return 1;
                } else if (integer < integer2) {
                    return -1;
                } else {
                    return 0;
                }
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
     * @throws ParseException 
     */
    public void checkStartAndEndDate(String start, String end, StringBuffer buffer) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(start) && StringUtils.isNotEmpty(end)) {
            try {
                start = new DateTime(end.substring(0, 7)).minusMonths(1).toString("yyyy-MM-dd");
            }catch(Exception e) {
                logger.error("无效的日期："+end,e);
                start = new DateTime().toString("yyyy-MM-dd");
            }
//            throw new RuntimeException("[" + NameAttrValue.getName() + "]干部的简历的开始日期有误【" + buffer.toString() + "】 其中开始日期【" + start + "】 请检查采集表后重试！");
        }
        Date pasreStart = null;
        Date pasreEnd = null;
		try {
			pasreStart = df.parse(start);
			pasreEnd = df.parse(end);
		} catch (ParseException e) {
			//TODO Auto-generated catch block
			DateFormat df2 = new SimpleDateFormat("yyyy-MM");
			try{
				pasreStart = df2.parse(start);
				pasreEnd = df2.parse(end);
			}catch(ParseException e2){
//				System.out.println("日期转型错误："+e2);
			}
		}
        if (StringUtils.isNotEmpty(end) && pasreStart.getTime() > pasreEnd.getTime()) {
            throw new RuntimeException("开始日期[" + start + "]--结束日期[" + end + "]  " + "[" + NameAttrValue.getName() + "]的" + buffer.toString() + "的开始日期大于结束日期，请检查采集表后重试！");
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
        return "简历";
    }
}
