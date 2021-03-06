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
 * 学位学历 -- 全日制教育
 * @author rain chen
 * @version 1.0
 * @since 1.0 Copyright (C) 2017. SinSoft All Rights Received
 */
public class FtDiplomaAndDegreeAttrValue implements AttrValue {

	private final String key = "ftDiplomaAndDegree";
	private final int order = 11;

	@Override
	public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService)
			throws AttrValueException {
		// 学历学位信息
		final String diplomaInfoSql = "SELECT * FROM Emp_A005 WHERE EMP_ID = '%s'  and status = 0  and (A005001_A is not null or A005050_A is not null) ORDER BY A005001_A,A005050_A ASC";
		String A005TableSql = String.format(diplomaInfoSql, empId);

		List diplomaInfoList = exportWordService.findBySQL(A005TableSql);

		if (diplomaInfoList == null) {
			attrValueContext.put("A005", new ArrayList<Map>());
			diplomaInfoList = new ArrayList();
		}
		attrValueContext.put("A005", diplomaInfoList);
		// 教育信息（学历、学位）
		// 将原数据结构进行重新调整
		/*
		 * 原始数据为List<Map> 根据Map中的某一个Key（教育类型）为条件进行分组即会产生Map<List<Map>>类型
		 */

		Map<String, List<Map>> fullEduMapList = new HashMap<String, List<Map>>(2);
		List<Map> xlList=new ArrayList<Map>();
		for (Object eduInfo : diplomaInfoList) {
			Map eduMap = (Map) eduInfo;
			String record = StringUtil.obj2Str(eduMap.get("A005001_A"));
			String eduType = StringUtil.obj2Str(eduMap.get("A005025"));// 教育类别
																		// 1全日制
																		// 2在职教育
			// 教育类别和学历都不为空的时候
			if (StringUtils.equals(eduType, "1") && StringUtil.isNotEmptyOrNull(record)) {
//				List<Map> maps = fullEduMapList.get(eduType);
//				if (maps == null) {
//					List<Map> temp = new ArrayList<Map>();
//					temp.add(eduMap);
//					fullEduMapList.put("xueli", temp);
//				} else {
//					maps.add(eduMap);
//					fullEduMapList.put("xueli", maps);
//				}
//				break;
				xlList.add(eduMap);
			}
		}
		fullEduMapList.put("xueli", xlList);
		attrValueContext.put("fullEduMapList", fullEduMapList);
		// 教育类型有相应的码值，包括A09014的值都不应该直接写死在代码里，应该由另一个码值控制
		List<Map> diploma = fullEduMapList.get("xueli"); // 学历
		List<String> diplomaAndDegreeList = new ArrayList<String>(2);
		String highRecord = "";
		if (diploma != null && diploma.size() > 0) {
			Map fullTimeMap = diploma.get(0);
			String highRecordCode = StringUtil.obj2Str(fullTimeMap.get("A005001_A"));
			if (StringUtil.isNotEmptyOrNull(highRecordCode)) {
				highRecord = CodeTranslateUtil.codeToName("BT0050", highRecordCode, exportWordService);
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
		if (highRecord == null || "".equals(highRecord)) {
			//有A005025=2(在职教育)，没有A005025=1(全日制教育)，有A005001A<32(学历)，填写“高中”
			boolean gaozhong025 = false;
			boolean gaozhong001A = false;
			for (Object eduInfo : diplomaInfoList) {
				Map eduMap = (Map) eduInfo;
				String record = StringUtil.obj2Str(eduMap.get("A005001_A"));
				String eduType = StringUtil.obj2Str(eduMap.get("A005025"));
				if ("1".equals(eduType)) {
					gaozhong025 = false;
					break;
				} else if ("2".equals(eduType)) {
					gaozhong025 = true;
				}
				if (record != null && "32".compareTo(record) > 0) {
					gaozhong001A = true;
				}
			}
			if (gaozhong025 && gaozhong001A) {
				highRecord = "高中";
			}
		}
		diplomaAndDegreeList.add(highRecord);
		List<Map> xwList=new ArrayList<>();
		for (Object highEdu : diplomaInfoList) {
			Map degreeMap = (Map) highEdu;
			String eduType = StringUtil.obj2Str(degreeMap.get("A005025"));// 教育类别
																			// 1全日制
																			// 2在职教育
			String degree = StringUtil.obj2Str(degreeMap.get("A005050_A"));
			// 教育类别和学历都不为空的时候
			if (StringUtils.equals(eduType, "1") && StringUtil.isNotEmptyOrNull(degree)) {
//				List<Map> maps = fullEduMapList.get(eduType);
//				if (maps == null) {
//					List<Map> temp = new ArrayList<Map>();
//					temp.add(degreeMap);
//					fullEduMapList.put("xuewei", temp);
//				} else {
//					maps.add(degreeMap);
//					fullEduMapList.put("xuewei", maps);
//				}
//				break;
				xwList.add(degreeMap);
			}
		}
		fullEduMapList.put("xuewei", xwList);
		List<Map> degree = fullEduMapList.get("xuewei");
		String jobRecord = "";
		if (degree != null && degree.size() > 0) {
			Map jobEduMap = degree.get(0);
			String jobRecordCode = StringUtil.obj2Str(jobEduMap.get("A005050_A"));
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
		return diplomaAndDegreeList;
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
        return "学位学历--全日制教育";
    }	
}
