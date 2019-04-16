package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

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
public class PtInstituteAndSpecialtyAttrValue implements AttrValue {

    private final String key = "ptInstituteAndSpecialty";
    private final int order = 14;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) {
        try {
            Map<String, List<Map>> inServiceEdu = (Map<String, List<Map>>) attrValueContext.get("inServiceEduMap");
            if (inServiceEdu == null || inServiceEdu.size() < 1) {
                return "";
            }

            //在职教育最高学历List
            List<Map> highDiplomaList = inServiceEdu.get("highDiplomaList");
            //在职教育最高学位List
            List<Map> highDegreeList = inServiceEdu.get("highDegreeList");
            StringBuffer ptInstituteAndSpecialty = new StringBuffer();
            //储存学校名称
            List<String> collegeList = new ArrayList<String>();
            if (highDiplomaList != null && highDiplomaList.size() > 0) {
                for (Map inServiceMap : highDiplomaList) {
                    String ptInstitute = StringUtil.obj2Str(inServiceMap.get("A08014"));//学校名称
                    String A08016 = StringUtil.obj2Str(inServiceMap.get("A08016"));//学院名称
                    String ptSpecialty = StringUtil.obj2Str(inServiceMap.get("A08024"));//所学专业名称
                    if (StringUtils.isNotBlank(ptInstitute) && StringUtils.isNotBlank(ptSpecialty)) {
                        collegeList.add(ptInstitute);
                        if (!StringUtils.equals(ptSpecialty, "")) {
                            ptSpecialty = ptSpecialty.endsWith("专业") ? ptSpecialty : (ptSpecialty + "专业");
                        }
                        if (!StringUtils.equals(A08016, "")) {
                            A08016 = A08016.endsWith("学院") || A08016.endsWith("系") ? A08016 : (A08016 + "学院");
                        }
                        ptInstituteAndSpecialty.append(ptInstitute).append(A08016).append(ptSpecialty).append((char) 11);
                    }
                }
            }
            if (highDegreeList != null && highDegreeList.size() > 0) {
                for (Map inServiceMap : highDegreeList) {
                    String ptInstitute = StringUtil.obj2Str(inServiceMap.get("A09007"));//学位授予单位
                    String inServiceDegree = StringUtil.obj2Str(inServiceMap.get("A09001_A"));//学位名称
                    if (!collegeList.contains(ptInstitute)) {
                        ptInstituteAndSpecialty.append(ptInstitute).append(inServiceDegree).append((char) 11);
                    }
                }
            }
//            if(ptInstituteAndSpecialty != null || ptInstituteAndSpecialty.length() > 0) {
//            	ptInstituteAndSpecialty.deleteCharAt(ptInstituteAndSpecialty.length()-1);
//            }

            //shixianggui-20180410, bug 377 学历学位问题: 学历集信息中，在职学习未到得学历的，其专业可以为空，让其生成任免表。
            if (ptInstituteAndSpecialty != null && ptInstituteAndSpecialty.length() > 0) {
                ptInstituteAndSpecialty.deleteCharAt(ptInstituteAndSpecialty.length() - 1);
            }

            return ptInstituteAndSpecialty.toString();
        } catch (Exception e) {
            throw new RuntimeException("任免表生成失败:学历信息取得失败");
        }

//    	
//    	if(inServiceEdu != null && inServiceEdu.size() > 1) {
//            Map inServiceMap = inServiceEdu.get(0);//学历Map
//            Map inServiceMap2 = inServiceEdu.get(1);//学位Map
//            
//            String ptInstitute = StringUtil.obj2Str(inServiceMap.get("A08014"));//学校名称
//            String A08016 = StringUtil.obj2Str(inServiceMap.get("A08016"));//学院名称
//            String ptSpecialty = StringUtil.obj2Str(inServiceMap.get("A08024"));//所学专业名称
//            String ptInstitute2 = StringUtil.obj2Str(inServiceMap2.get("A09007"));//学位授予单位
//            String inServiceDegree = StringUtil.obj2Str(inServiceMap2.get("A09001_A"));//学位名称
//            if(StringUtils.isNotEmpty(ptInstitute) && StringUtils.isNotEmpty(ptInstitute2) 
//            		&& StringUtils.equals(ptInstitute, ptInstitute2)) {
//                if (!StringUtils.equals(ptSpecialty,"")){
//                    ptSpecialty = ptSpecialty.endsWith("专业")?ptSpecialty:(ptSpecialty+"专业");
//                }
//                if (!StringUtils.equals(A08016,"")){
//                	A08016 = ptSpecialty.endsWith("学院") || ptSpecialty.endsWith("系")?ptSpecialty:(ptSpecialty+"学院");
//                }
//                ptInstituteAndSpecialty.append(ptInstitute).append(A08016).append(ptSpecialty);
//            }else {
//                if (!StringUtils.equals(ptSpecialty,"")){
//                    ptSpecialty = ptSpecialty.endsWith("专业")?ptSpecialty:(ptSpecialty+"专业");
//                }
//                if (!StringUtils.equals(A08016,"")){
//                	A08016 = ptSpecialty.endsWith("学院") || ptSpecialty.endsWith("系")?ptSpecialty:(ptSpecialty+"学院");
//                }
//                ptInstituteAndSpecialty.append(ptInstitute).append(A08016).append(ptSpecialty);
//                ptInstituteAndSpecialty.append((char)11).append(ptInstitute2).append(inServiceDegree);
//            }
//
//    	}else {
//            Map inServiceMap = inServiceEdu.get(0);//学历Map
//
//            String ptInstitute = StringUtil.obj2Str(inServiceMap.get("A08014"));//学校名称
//            String A08016 = StringUtil.obj2Str(inServiceMap.get("A08016"));//学院名称
//            String ptSpecialty = StringUtil.obj2Str(inServiceMap.get("A08024"));//所学专业名称
//            String ptInstitute2 = StringUtil.obj2Str(inServiceMap.get("A09007"));//学位授予单位
//            String inServiceDegree = StringUtil.obj2Str(inServiceMap.get("A09001_A"));//学位名称
//            
//            if(StringUtils.isNotEmpty(ptInstitute) || StringUtils.isNotEmpty(ptSpecialty)) {
//                if (!StringUtils.equals(ptSpecialty,"")){
//                    ptSpecialty = ptSpecialty.endsWith("专业")?ptSpecialty:(ptSpecialty+"专业");
//                }
//                if (!StringUtils.equals(A08016,"")){
//                	A08016 = ptSpecialty.endsWith("学院") || ptSpecialty.endsWith("系")?ptSpecialty:(ptSpecialty+"学院");
//                }
//                ptInstituteAndSpecialty.append(ptInstitute).append(A08016).append(ptSpecialty);
//            }else if(StringUtils.isNotEmpty(ptInstitute2) || StringUtils.isNotEmpty(inServiceDegree)){
//            	ptInstituteAndSpecialty.append(ptInstitute2).append(inServiceDegree);
//            }
//     	}
//    	List  diplomaInfoList = (List) attrValueContext.get("A08");
//    	List  degreeInfoList = (List) attrValueContext.get("A09");
//    	Map<String, List<Map>> eduMapList = (Map<String, List<Map>>) attrValueContext.get("eduMapList");

//        List<Map> inServiceEdu = new ArrayList<>();   //在职教育
//        for (String key : eduMapList.keySet()){
//            if (key.startsWith("2")){
//                inServiceEdu = eduMapList.get(key);
//                break;
//            }
//        }
//        if (inServiceEdu != null && inServiceEdu.size() > 0) {
//            Map inServiceMap = inServiceEdu.get(0);
//            String ptInstitute = StringUtil.obj2Str(inServiceMap.get("A08014"));
//            String ptSpecialty = StringUtil.obj2Str(inServiceMap.get("A08024"));
//            if (!StringUtils.equals(ptSpecialty,"")){
//                ptSpecialty = ptSpecialty.endsWith("专业")?ptSpecialty:(ptSpecialty+"专业");
//            }
//            ptInstituteAndSpecialty = ptInstitute + ptSpecialty;
//        }
//        return ptInstituteAndSpecialty.toString();
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
