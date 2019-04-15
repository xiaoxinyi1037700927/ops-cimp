package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/10.
 * 学历学位 -- 在职教育
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class PtDiplomaAndDegreeAttrValue implements AttrValue {

    private final String key = "ptDiplomaAndDegree";
    private final int order = 13;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) throws Exception {

        Map<String, List<Map>> jobEduMapList = new HashMap<String, List<Map>>(2);
        List degreeInfoList = (List) attrValueContext.get("A005");

        Map<String, Map> inServiceEdu = new HashMap<String, Map>();   //在职教育
        List<String> diplomaAndDegreeList = new ArrayList<String>(2);
        for (Object eduInfo : degreeInfoList) {
            Map eduMap = (Map) eduInfo;
            String record = StringUtil.obj2Str(eduMap.get("A005001_A"));
            String eduType = StringUtil.obj2Str(eduMap.get("A005025"));// 教育类别
            // 1全日制
            // 2在职教育
            // 教育类别和学历都不为空的时候
            if (StringUtils.equals(eduType, "2") && StringUtil.isNotEmptyOrNull(record)) {
                List<Map> maps = jobEduMapList.get(eduType);
                if (maps == null) {
                    List<Map> temp = new ArrayList<Map>();
                    temp.add(eduMap);
                    jobEduMapList.put("xueli", temp);
                } else {
                    maps.add(eduMap);
                    jobEduMapList.put("xueli", maps);
                }
                break;
            }
        }
        attrValueContext.put("jobEduMapList", jobEduMapList);
        List<Map> diploma = jobEduMapList.get("xueli"); // 学历
        String highRecord = "";
        if (diploma != null && diploma.size() > 0) {
            Map fullTimeMap = diploma.get(0);
            String highRecordCode = StringUtil.obj2Str(fullTimeMap.get("A005001_A"));
            if (StringUtil.isNotEmptyOrNull(highRecordCode)) {
                highRecord = CodeTranslateUtil.codeToName("BT0050", highRecordCode, exportWordService);
            }
        }
        diplomaAndDegreeList.add(highRecord);

        for (Object eduInfo : degreeInfoList) {
            Map degreeMap = (Map) eduInfo;
            String eduType = StringUtil.obj2Str(degreeMap.get("A005025"));// 教育类别
            // 1全日制
            // 2在职教育
            String degree = StringUtil.obj2Str(degreeMap.get("A005050_A"));
            // 教育类别和学历都不为空的时候
            if (StringUtils.equals(eduType, "2") && StringUtil.isNotEmptyOrNull(degree)) {
                List<Map> maps = jobEduMapList.get(eduType);
                if (maps == null) {
                    List<Map> temp = new ArrayList<Map>();
                    temp.add(degreeMap);
                    jobEduMapList.put("xuewei", temp);
                } else {
                    maps.add(degreeMap);
                    jobEduMapList.put("xuewei", maps);
                }
                break;
            }
        }
        List<Map> degree = jobEduMapList.get("xuewei");
        String jobRecord = "";
        if (degree != null && degree.size() > 0) {
            Map jobEduMap = degree.get(0);
            String jobRecordCode = StringUtil.obj2Str(jobEduMap.get("A005050_A"));
            if (StringUtil.isNotEmptyOrNull(jobRecordCode)) {
                jobRecord = CodeTranslateUtil.codeToName("BT0075", jobRecordCode, exportWordService);
            }
        }
        diplomaAndDegreeList.add(jobRecord);
		/*
        for (Object highEdu : degreeInfoList) {
            Map degreeMap = (Map) highEdu;
            String eduType = StringUtil.obj2Str(degreeMap.get("A005025"));
            if (eduType.startsWith("2")) {     //最高教育类型为全日制教育
                String ptDegree = StringUtil.obj2Str(degreeMap.get("A005001_B"));
//                if (diplomaAndDegreeList.size() > 0) {
                    if (StringUtils.isNotEmpty(ptDegree)) {
//                        if (inServiceEdu != null && inServiceEdu.size() > 0) {
//                            Map inServiceMap = inServiceEdu.get(0);
                            String A005020 = StringUtil.obj2Str(degreeMap.get("A005020"));     //学历从学单位类别
                            String A005001B = StringUtil.obj2Str(degreeMap.get("A005001_B"));     //学历名称
                            if (StringUtils.isNotEmpty(A005020) && Arrays.asList("101", "102", "103").contains(A005020)) {
                                String name = "";
                                switch (A005020) {
                                    case "101":
                                        name = EnumType.SCHOOL_UNIT_TYPE_101.name;
                                        break;
                                    case "102":
                                        name = EnumType.SCHOOL_UNIT_TYPE_102.name;
                                        break;
                                    case "103":
                                        name = EnumType.SCHOOL_UNIT_TYPE_103.name;
                                        break;
                                    default:
                                        break;
                                }
                                diplomaAndDegreeList.add(StringUtils.isNotEmpty(A005001B) ? name + A005001B : "");
                            } else {
                                diplomaAndDegreeList.add(A005001B);
                            }
//                        }
                    }
                    List<Map> maps = eduMapList.get(eduType);
                    if (maps == null) {
                        List<Map> temp = new ArrayList<Map>();
                        temp.add(degreeMap);
                        eduMapList.put(eduType, temp);
                    } else {
                        maps.add(degreeMap);
                        eduMapList.put(eduType, maps);
                    }
            }
        }
        */
        return diplomaAndDegreeList;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
