package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/10.
 * 学历学位 -- 在职教育
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
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {

        Map<String, List<Map>> jobEduMapList = new HashMap<String,List<Map>>(2);
        List degreeInfoList = (List) attrValueContext.get("A005");

        Map<String,Map> inServiceEdu = new HashMap<String,Map>();   //在职教育
        List<String> diplomaAndDegreeList = new ArrayList<String>(2);
        List<Map> xlList=new ArrayList<Map>();
		for (Object eduInfo : degreeInfoList) {
			Map eduMap = (Map) eduInfo;
			String record = StringUtil.obj2Str(eduMap.get("A005001_A"));
			String eduType = StringUtil.obj2Str(eduMap.get("A005025"));// 教育类别
																		// 1全日制
																		// 2在职教育
			
			// 教育类别和学历都不为空的时候
			if (StringUtils.equals(eduType, "2") && StringUtil.isNotEmptyOrNull(record)) {
//				List<Map> maps = jobEduMapList.get(eduType);
//				if (maps == null) {
//					List<Map> temp = new ArrayList<Map>();
//					temp.add(eduMap);
//					jobEduMapList.put("xueli", temp);
//				} else {
//					maps.add(eduMap);
//					jobEduMapList.put("xueli", maps);
//				}
//				break;
				xlList.add(eduMap);
			}
		}
		jobEduMapList.put("xueli", xlList);
		attrValueContext.put("jobEduMapList", jobEduMapList);
		List<Map> diploma = jobEduMapList.get("xueli"); // 学历
		String highRecord = "";
		if (diploma != null && diploma.size() > 0) {
			Map fullTimeMap = diploma.get(0);
			String highRecordCode = StringUtil.obj2Str(fullTimeMap.get("A005001_A"));
			String xxxs=StringUtil.obj2Str(fullTimeMap.get("A005020"));//学习形式
			String xs=getXxxs(xxxs,highRecordCode);
			if (StringUtil.isNotEmptyOrNull(highRecordCode)) {
				highRecord =xs+ CodeTranslateUtil.codeToName("BT0050", highRecordCode, exportWordService);
			}
			
			//20190529黄玉石新增，多学历
			String xlPre=highRecordCode.substring(0, 1);
			for(int i=1;i<diploma.size();i++) {
				Map o = diploma.get(i);
				String xl=StringUtil.obj2Str(o.get("A005001_A"));
				if(xl.startsWith(xlPre)&&!xl.equals(highRecordCode)) {
					highRecord =highRecord + (char)11 + CodeTranslateUtil.codeToName("BT0050", xl, exportWordService);
				}else {
					break;
				}
			}
		}
		diplomaAndDegreeList.add(highRecord);
		
		List<Map> xwList=new ArrayList<>();
		for (Object eduInfo : degreeInfoList) {
			Map degreeMap = (Map) eduInfo;
			String eduType = StringUtil.obj2Str(degreeMap.get("A005025"));// 教育类别
																			// 1全日制
																			// 2在职教育
			String degree = StringUtil.obj2Str(degreeMap.get("A005050_A"));
			// 教育类别和学历都不为空的时候
			if (StringUtils.equals(eduType, "2") && StringUtil.isNotEmptyOrNull(degree)) {
//				List<Map> maps = jobEduMapList.get(eduType);
//				if (maps == null) {
//					List<Map> temp = new ArrayList<Map>();
//					temp.add(degreeMap);
//					jobEduMapList.put("xuewei", temp);
//				} else {
//					maps.add(degreeMap);
//					jobEduMapList.put("xuewei", maps);
//				}
//				break;
				xwList.add(degreeMap);
			}
		}
		jobEduMapList.put("xuewei", xwList);
		List<Map> degree = jobEduMapList.get("xuewei");
		String jobRecord = "";
		if (degree != null && degree.size() > 0) {
			Map jobEduMap = degree.get(0);
			String jobRecordCode = StringUtil.obj2Str(jobEduMap.get("A005050_A"));
//			String xxxs=StringUtil.obj2Str(jobEduMap.get("A005020"));//学习形式
//			String xs=getXxxs(xxxs,jobRecordCode);
			if (StringUtil.isNotEmptyOrNull(jobRecordCode)) {
				jobRecord = CodeTranslateUtil.codeToName("BT0075", jobRecordCode, exportWordService);
			}
			
			//20190520黄玉石新增，多学位
			String xwPre=jobRecordCode.substring(0, 1);
			for(int i=1;i<degree.size();i++) {
				Map o = degree.get(i);
				String xw=StringUtil.obj2Str(o.get("A005050_A"));
				if(xw.startsWith(xwPre)&&!xw.equals(jobRecordCode)) {
					jobRecord =jobRecord + (char)11 + CodeTranslateUtil.codeToName("BT0075", xw, exportWordService);
				}else {
					break;
				}
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
    
    //研究生、本科、专科的党校学历前缀
    public String getXxxs(String xxxs,String code) {
    	if((code.startsWith("1")||code.startsWith("2")||code.startsWith("3"))
    			&& !code.endsWith("9")) {
    		if("101".equals(xxxs)) {
        		return "中央党校";
        	}else if("102".equals(xxxs)) {
        		return "省区市委党校";
        	}else if("103".equals(xxxs)) {
        		return "地市委党校";
        	}else {
        		return "";
        	}
    	}else {
    		return "";
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
        return "学历学位--在职教育";
    }
}
