package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/10.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FtDiplomaAndDegreeAttrValue implements AttrValue {

    public static final String KEY = "ftDiplomaAndDegree";
    public static final int ORDER = 11;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            //学历信息
            final String diplomaInfoSql = "SELECT * FROM EMP_A08 WHERE EMP_ID = '%s' and status=0 ORDER BY A08004 DESC";
            //学位信息
            final String degreeInfoSql = "SELECT * FROM EMP_A09 WHERE EMP_ID = '%s' and status=0";
            String a08TableSql = String.format(diplomaInfoSql, empId);
            String a09TableSql = String.format(degreeInfoSql, empId);

            List diplomaInfoList = ExportConstant.exportWordService.findBySQL(a08TableSql);
            List degreeInfoList = ExportConstant.exportWordService.findBySQL(a09TableSql);

            if (diplomaInfoList == null) {
                attrValueContext.put("A08", new ArrayList<Map>());
                diplomaInfoList = new ArrayList();
            }
            if (degreeInfoList == null) {
                attrValueContext.put("A09", new ArrayList<Map>());
                degreeInfoList = new ArrayList();
            }
            attrValueContext.put("A08", diplomaInfoList);
            attrValueContext.put("A09", degreeInfoList);
            //重新组织数据结构，用来储存全日制学历学位和学校的信息
            Map<String, List<Map>> fullEduMap = new HashMap<String, List<Map>>();
            //全日制最高学历List
            List<Map> highDiplomaList = new ArrayList<Map>();
            //全日制最高学位List
            List<Map> highDegreeList = new ArrayList<Map>();
            //遍历，取出所有的全日制最高学历并组装在一起
            Map<String, List<Map>> eduMapList = new HashMap<String, List<Map>>(2);
            for (Object eduInfo : diplomaInfoList) {
                Map eduMap = (Map) eduInfo;
                String eduType = StringUtil.obj2Str(eduMap.get("A08020"));
                String isHighFlag = StringUtil.obj2Str(eduMap.get("A08034"));//是否最高学历标识
                if (StringUtils.isNotEmpty(eduType) && eduType.startsWith("1") && StringUtils.equals(isHighFlag, "1")) {
                    highDiplomaList.add(eduMap);
                }
            }
            //遍历，取出所有的全日制最高学位并组装在一起
            for (Object degree : degreeInfoList) {
                Map degreeMap = (Map) degree;
                String isHighestEdu = StringUtil.obj2Str(degreeMap.get("A09014"));
                String eduType = StringUtil.obj2Str(degreeMap.get("A09081"));
                if (StringUtils.isNotEmpty(eduType) && eduType.startsWith("1") && StringUtils.equals(isHighestEdu, "1")) {
                    highDegreeList.add(degreeMap);
                }
            }

            List<String> diplomaAndDegreeList = new ArrayList<String>(2);
            //将组装好的最高学位集合封装到学历学位Map中
            if (highDiplomaList != null && highDiplomaList.size() > 0) {
                fullEduMap.put("highDiplomaList", highDiplomaList);
                //学历
                StringBuffer xueli = new StringBuffer();
                //遍历
                for (Map highDip : highDiplomaList) {
                    if (StringUtil.obj2Str(highDip.get("A08002_A")).endsWith("大学专科")) {
                        xueli.append("大专");
                        break;
                    } else {
                        xueli.append(StringUtil.obj2Str(highDip.get("A08002_A")));
                        break;
                    }
                }
                if (xueli.length() > 0) {
                    diplomaAndDegreeList.add(xueli.toString());
                }

            }
            //将组装好的最高学历集合封装到学历学位Map中
            if (highDegreeList != null && highDegreeList.size() > 0) {
                fullEduMap.put("highDegreeList", highDegreeList);
                //学位
                StringBuffer xuewei = new StringBuffer();
                //遍历
                for (Map highDegree : highDegreeList) {
                    String ftDegree = StringUtil.obj2Str(highDegree.get("A09001_A"));
                    if (StringUtils.isNotEmpty(ftDegree)) {
                        xuewei.append(ftDegree).append(" ");
                    }
                }
                if (xuewei.length() > 0) {
                    diplomaAndDegreeList.add(xuewei.toString());
                }
            }
            attrValueContext.put("fullEduMap", fullEduMap);
            return diplomaAndDegreeList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A08学历或EMP_A09学位]");
        }

//        
//        //教育信息（学历、学位）
//        //将原数据结构进行重新调整
//         /*
//          * 原始数据为List<Map>
//          * 根据Map中的某一个Key（教育类型）为条件进行分组即会产生Map<List<Map>>类型
//          */
//        Map<String, List<Map>> eduMapList = new HashMap<String, List<Map>>(2);
//        for (Object eduInfo : diplomaInfoList) {
//            Map eduMap = (Map) eduInfo;
//            String eduType = StringUtil.obj2Str(eduMap.get("A08020"));
//            String isHighFlag = StringUtil.obj2Str(eduMap.get("A08034"));//是否最高学历标识
//            if (StringUtils.isNotEmpty(eduType) && StringUtils.equals(isHighFlag,"1")) {
//                List<Map> maps = eduMapList.get(eduType);
//                if (maps == null) {
//                    List<Map> temp = new ArrayList<Map>();
//                    temp.add(eduMap);
//                    eduMapList.put(eduType, temp);
//                } else {
//                    maps.add(eduMap);
//                    eduMapList.put(eduType, maps);
//                }
//            }
//        }
//        attrValueContext.put("eduMapList", eduMapList);
//        //教育类型有相应的码值，包括A09014的值都不应该直接写死在代码里，应该由另一个码值控制
//        List<Map> fullTimeEdu = eduMapList.get("1");    //全日制教育
//        List<String> diplomaAndDegreeList = new ArrayList<String>(2);
//        if (fullTimeEdu != null && fullTimeEdu.size() > 0) {
//            Map fullTimeMap = fullTimeEdu.get(0);
//            if(StringUtil.obj2Str(fullTimeMap.get("A08002_A")).endsWith("大学专科"))
//            {
//            	diplomaAndDegreeList.add("大专");
//            }
//            else
//            {
//            	diplomaAndDegreeList.add(StringUtil.obj2Str(fullTimeMap.get("A08002_A")));
//            }
//            for (Object highEdu : degreeInfoList) {
//                Map degreeMap = (Map) highEdu;
//                String isHighestEdu = StringUtil.obj2Str(degreeMap.get("A09014"));
//                String eduType = StringUtil.obj2Str(degreeMap.get("A09081"));
//                if (StringUtils.equals(eduType, "1") && StringUtils.equals(isHighestEdu, "1")) {     //最高教育类型为全日制教育
//                    String ftDegree = StringUtil.obj2Str(degreeMap.get("A09001_A"));
//                    if (diplomaAndDegreeList.size() > 0) {
//                        if (StringUtils.isNotEmpty(ftDegree)) {
//                            diplomaAndDegreeList.add(ftDegree);
//                        }
//                    } else {
//                        diplomaAndDegreeList.add(ftDegree);
//                    }
//                }
//            }
//        }
//        return diplomaAndDegreeList;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
