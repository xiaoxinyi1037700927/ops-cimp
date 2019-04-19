package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.EnumType;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by rain chen on 2017/10/10.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class PtDiplomaAndDegreeAttrValue implements AttrValue {

    public static final String KEY = "ptDiplomaAndDegree";
    public static final int ORDER = 13;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {

        Map<String, List<Map>> eduMapList = (Map<String, List<Map>>) attrValueContext.get("eduMapList");
        List diplomaInfoList = (List) attrValueContext.get("A08");
        List degreeInfoList = (List) attrValueContext.get("A09");
        //重新组织数据结构，用来储存在职教育学历学位和学校的信息
        Map<String, List<Map>> inServiceEduMap = new HashMap<>();
        //在职教育最高学历List
        List<Map> highDiplomaList = new ArrayList<>();
        //在职教育最高学位List
        List<Map> highDegreeList = new ArrayList<>();
        List<Map> inServiceEdu = new ArrayList<>();   //在职教育
        if (diplomaInfoList != null && diplomaInfoList.size() > 0) {
            for (Object o : diplomaInfoList) {
                Map dipomaInfoMap = (Map) o;
                String a08020 = StringUtil.obj2Str(dipomaInfoMap.get("A08020"));      //教育类别
                String A08034 = StringUtil.obj2Str(dipomaInfoMap.get("A08034"));      //是否最高学历
                if (StringUtils.isNotBlank(a08020)
                        && StringUtils.isNotBlank(A08034)
                        && StringUtils.equals("1", A08034)
                        && a08020.startsWith("2")) {
                    //在职教育的最高学历储存起来
                    highDiplomaList.add(dipomaInfoMap);
                }
            }
        }
        if (degreeInfoList != null && degreeInfoList.size() > 0) {
            for (Object highEdu : degreeInfoList) {
                Map degreeMap = (Map) highEdu;
                String isHighestEdu = StringUtil.obj2Str(degreeMap.get("A09014"));
                String eduType = StringUtil.obj2Str(degreeMap.get("A09081"));
                if (eduType.startsWith("2") && StringUtils.equals(isHighestEdu, "1")) {     //最高教育类型为全日制教育
                    //在职教育的最高学位储存起来
                    highDegreeList.add(degreeMap);
                }
            }
        }

        if (highDiplomaList.size() > 0) {
            inServiceEduMap.put("highDiplomaList", highDiplomaList);
        }
        if (highDegreeList.size() > 0) {
            inServiceEduMap.put("highDegreeList", highDegreeList);
        }
        attrValueContext.put("inServiceEduMap", inServiceEduMap);
        List<String> diplomaAndDegreeList = new ArrayList<String>(2);
        StringBuffer diplomaAndDegree = new StringBuffer();
        if (highDiplomaList.size() > 0) {
            for (Map inServiceMap : highDiplomaList) {
                String a08021 = StringUtil.obj2Str(inServiceMap.get("A08021"));      //学历从学单位类别
                String A08002A = StringUtil.obj2Str(inServiceMap.get("A08002_A"));      //学历名称
                String A08014 = StringUtil.obj2Str(inServiceMap.get("A08014"));      //学校名称
                if (StringUtils.isNotEmpty(a08021) && Arrays.asList("71", "72", "73").contains(a08021)) {
                    String name = "";
                    switch (a08021) {
                        case "71":
                            name = EnumType.SCHOOL_UNIT_TYPE_71.name;
                            break;
                        case "72":
                            name = EnumType.SCHOOL_UNIT_TYPE_72.name;
                            break;
                        case "73":
                            name = EnumType.SCHOOL_UNIT_TYPE_73.name;
                            break;
                        default:
                            break;
                    }
                    diplomaAndDegree.append(StringUtils.isNotEmpty(A08002A) ? name + A08002A : "").append((char) 11);
                } else {
                    if (A08014.contains("党校")) {
                        diplomaAndDegree.append(A08014.substring(A08014.indexOf("党校") - 2, A08014.indexOf("党校") + 2) + A08002A).append((char) 11);
                    } else {
                        diplomaAndDegree.append(A08002A).append((char) 11);
                    }
                }
                if (highDegreeList.size() > 0) {
//                	for(Map degreeMap:highDegreeList) {
//                        String degreeName = StringUtil.obj2Str(degreeMap.get("A09001_A"));
//                        String degreeShool = StringUtil.obj2Str(degreeMap.get("A09007"));//学位授予单位
//                        if (StringUtils.isNotEmpty(degreeName) && StringUtils.isNotEmpty(degreeShool) 
//                        		&& StringUtils.equals(degreeShool, A08014)) {    
//                        	//在职教育的最高学位储存起来
//                        	diplomaAndDegree.append(degreeName).append((char)11); 
//                        	highDegreeList.remove(degreeMap);
//                        }
//                    }

                    // shixianggui-20180312, 解决 java.util.ConcurrentModificationException 异常问题
                    String degreeName = null;
                    String degreeShool = null;
                    Iterator<Map> iterator = highDegreeList.iterator();
                    Map degreeMap = null;
                    while (iterator.hasNext()) {
                        degreeMap = iterator.next();
                        degreeName = StringUtil.obj2Str(degreeMap.get("A09001_A"));
                        degreeShool = StringUtil.obj2Str(degreeMap.get("A09007"));//学位授予单位
                        if (StringUtils.isNotEmpty(degreeName) && StringUtils.isNotEmpty(degreeShool)
                                && StringUtils.equals(degreeShool, A08014)) {
                            //在职教育的最高学位储存起来
                            diplomaAndDegree = new StringBuffer(degreeName).append((char) 11);
                            //diplomaAndDegree.append(degreeName).append((char)11);
                            iterator.remove();
                        }
                    }
                }
            }
        }
        if (highDegreeList.size() > 0) {
            for (Map degreeMap : highDegreeList) {
                String degreeName = StringUtil.obj2Str(degreeMap.get("A09001_A"));
                if (StringUtils.isNotEmpty(degreeName)) {
                    //在职教育的最高学位储存起来
                    diplomaAndDegree.append(degreeName).append((char) 11);
                }
            }
        }
        if (diplomaAndDegree.length() > 0) {
            diplomaAndDegreeList.add(diplomaAndDegree.substring(0, diplomaAndDegree.length() - 1));
        }
//        if(xuewei.length()>0) {
//        	diplomaAndDegreeList.add(xuewei.toString());
//        }
        return diplomaAndDegreeList;
//        
////        for (String key : eduMapList.keySet()) {
////            if (key.startsWith("2")) {
////                inServiceEdu = eduMapList.get(key);
////                break;
////            }
////        }
//        
//        if (inServiceEdu != null && inServiceEdu.size() > 0) {
//            Map inServiceMap = inServiceEdu.get(0);
//            attrValueContext.put("zaizhixueli", inServiceMap);
//            String a08021 = StringUtil.obj2Str(inServiceMap.get("A08021"));      //学历从学单位类别
//            String A08002A = StringUtil.obj2Str(inServiceMap.get("A08002_A"));      //学历名称
//            String A08014 = StringUtil.obj2Str(inServiceMap.get("A08014"));      //学校名称
//            if (StringUtils.isNotEmpty(a08021) && Arrays.asList("71", "72", "73").contains(a08021)) {
//                String name = "";
//                switch (a08021) {
//                    case "71":
//                        name = EnumType.SCHOOL_UNIT_TYPE_71.name;
//                        break;
//                    case "72":
//                        name = EnumType.SCHOOL_UNIT_TYPE_72.name;
//                        break;
//                    case "73":
//                        name = EnumType.SCHOOL_UNIT_TYPE_73.name;
//                        break;
//                    default:
//                        break;
//                }
//                diplomaAndDegreeList.add(StringUtils.isNotEmpty(A08002A) ? name + A08002A : "");
//            } else {
//            	if(A08014.contains("党校"))
//            	{
//            		diplomaAndDegreeList.add(A08014.substring(A08014.indexOf("党校")-2,A08014.indexOf("党校")+2) + A08002A);
//            	}
//            	else
//            	{
//            		diplomaAndDegreeList.add(A08002A);
//            	}
//            }
//        }
//        for (Object highEdu : degreeInfoList) {
//            Map degreeMap = (Map) highEdu;
//            String isHighestEdu = StringUtil.obj2Str(degreeMap.get("A09014"));
//            String eduType = StringUtil.obj2Str(degreeMap.get("A09081"));
//            if (eduType.startsWith("2") && StringUtils.equals(isHighestEdu, "1")) {     //最高教育类型为全日制教育
//                //在职教育的最高学位储存起来
//            	inServiceEdu.add(degreeMap);
//            	String ptDegree = StringUtil.obj2Str(degreeMap.get("A09001_A"));
//                if (diplomaAndDegreeList.size() > 0) {
//                    if (StringUtils.isNotEmpty(ptDegree)) {
//                        diplomaAndDegreeList.add(ptDegree);
//                    }
//                } else {
//                    diplomaAndDegreeList.add(ptDegree);
//                }
//            }
//        }
//        attrValueContext.put("inServiceEdu", inServiceEdu);
//        return diplomaAndDegreeList;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
