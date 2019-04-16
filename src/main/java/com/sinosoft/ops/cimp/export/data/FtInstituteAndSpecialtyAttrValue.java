package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
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
public class FtInstituteAndSpecialtyAttrValue implements AttrValue {

    private final String key = "ftInstituteAndSpecialty";
    private final int order = 12;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        Map<String, List<Map>> fullEduMap = (Map<String, List<Map>>) attrValueContext.get("fullEduMap");
        if (fullEduMap == null || fullEduMap.size() < 1) {
            return "";
        }
        //全日制最高学历List
        List<Map> highDiplomaList = fullEduMap.get("highDiplomaList");
        //全日制最高学位List
        List<Map> highDegreeList = fullEduMap.get("highDegreeList");
        StringBuffer ftInstituteAndSpecialty = new StringBuffer();
        //储存学历中的所有学校名称
        List<String> collegeList = new ArrayList<String>();
        String xueliCollege = "";
        if (highDiplomaList != null && highDiplomaList.size() > 0) {
            for (Map xueliMap : highDiplomaList) {
                String A08002B = StringUtil.obj2Str(xueliMap.get("A08002_B")); //学历代码
                //高中及以下学历， 毕业院系专业不显示
                if (StringUtils.isNotEmpty(A08002B) && NumberUtils.toInt(A08002B.substring(0, 1)) > 5) {
                    continue;
                }

                xueliCollege = StringUtil.obj2Str(xueliMap.get("A08014")); //毕业院校
                String faculty = StringUtil.obj2Str(xueliMap.get("A08016"));     //院系名称
                String ftSpecialty = StringUtil.obj2Str(xueliMap.get("A08024")); //专业信息
                if (!StringUtils.equals(ftSpecialty, "")) {
                    ftSpecialty = ftSpecialty.endsWith("专业") ? ftSpecialty : (ftSpecialty + "专业");
                }
                ftInstituteAndSpecialty.append(xueliCollege).append(faculty).append(ftSpecialty).append((char) 11);
                collegeList.add(xueliCollege);
            }
        }
        if (highDegreeList != null && highDegreeList.size() > 0) {
            for (Map xueweiMap : highDegreeList) {
                String degreeName = StringUtil.obj2Str(xueweiMap.get("A09001_A"));//学位名称
                String college = StringUtil.obj2Str(xueweiMap.get("A09007")); //毕业院校
                if (StringUtil.isNotEmptyOrNull(college)) {
                    if (!collegeList.contains(college)) {
                        ftInstituteAndSpecialty.append(college).append(degreeName).append((char) 11);
                    }
                }
            }
        }
        if (ftInstituteAndSpecialty != null && ftInstituteAndSpecialty.length() > 0) {
            ftInstituteAndSpecialty.deleteCharAt(ftInstituteAndSpecialty.length() - 1);
        }
        return ftInstituteAndSpecialty;


//    	
//    	
//    	//        Map<String, List<Map>> eduMapList = (Map<String, List<Map>>) attrValueContext.get("eduMapList");
//        List<Map<String,String>> diplomaInfoList = (List<Map<String,String>>) attrValueContext.get("A08");
//        List<Map<String,String>> degreeInfoList =  (List<Map<String,String>>) attrValueContext.get("A09");
//        StringBuffer ftInstituteAndSpecialty = new StringBuffer();
//        String xueliCollege = "";
//        for(Map xueliMap :diplomaInfoList) {
//        	String eduType = StringUtil.obj2Str(xueliMap.get("A08020")); //教育类别
//        	if(StringUtil.isEmptyOrNull(eduType) || !StringUtils.equals("1", eduType)) {
//        		continue;
//        	}
//        	String isHighFlag = StringUtil.obj2Str(xueliMap.get("A08034"));//是否最高学历标识
//        	if(StringUtil.isEmptyOrNull(isHighFlag) || !StringUtils.equals("1", isHighFlag)) {
//        		continue;
//        	}
//            String A08002B = StringUtil.obj2Str(xueliMap.get("A08002_B")); //学历代码
//            //高中及以下学历， 毕业院系专业不显示
//            if (StringUtils.isNotEmpty(A08002B) && NumberUtils.toInt(A08002B.substring(0, 1)) > 5) {
//            	continue;
//            }
//
//            xueliCollege = StringUtil.obj2Str(xueliMap.get("A08014")); //毕业院校
//            String faculty = StringUtil.obj2Str(xueliMap.get("A08016"));     //院系名称
//            String ftSpecialty = StringUtil.obj2Str(xueliMap.get("A08024")); //专业信息
//            if (!StringUtils.equals(ftSpecialty, "")) {
//                ftSpecialty = ftSpecialty.endsWith("专业") ? ftSpecialty : (ftSpecialty + "专业");
//            }
//            ftInstituteAndSpecialty.append(xueliCollege).append(faculty).append(ftSpecialty);
//            break;
//        }
//        String xueweiCollege = "";
//        for(Map xueweiMap :degreeInfoList) {
//        	String eduType = StringUtil.obj2Str(xueweiMap.get("A09081")); //教育类别
//        	if(StringUtil.isEmptyOrNull(eduType) || !StringUtils.equals("1", eduType)) {
//        		continue;
//        	}
//        	String isHighFlag = StringUtil.obj2Str(xueweiMap.get("A09014"));//是否最高学位标识
//        	if(StringUtil.isEmptyOrNull(isHighFlag) || !StringUtils.equals("1", isHighFlag)) {
//        		continue;
//        	}
//        	String degreeName = StringUtil.obj2Str(xueweiMap.get("A09001_A"));//学位名称
//        	String college = StringUtil.obj2Str(xueweiMap.get("A09007")); //毕业院校
//        	if(StringUtil.isNotEmptyOrNull(college)) {
//        		xueweiCollege = college+degreeName;
//        	}
//        }
//        if(StringUtil.isNotEmptyOrNull(xueliCollege) 
//        		&& StringUtil.isNotEmptyOrNull(xueweiCollege) && !StringUtils.equals(xueliCollege, xueweiCollege)) {
//        	ftInstituteAndSpecialty.append((char)11).append(xueweiCollege);
//        }
//        return ftInstituteAndSpecialty.toString();
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
