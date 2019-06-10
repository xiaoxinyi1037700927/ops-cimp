package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import org.joda.time.DateTime;

import com.newskysoft.iimp.word.pattern.data.xinjiang.AttrValue;
import com.newskysoft.iimp.word.service.ExportService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9. 工作时间
 * 
 * @author rain chen
 * @version 1.0
 * @since 1.0 Copyright (C) 2017. SinSoft All Rights Received
 */
public class JobDateAttrValue implements AttrValue {

	private final String key = "jobDate";
	private final int order = 7;

	@Override
	public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
		Map a01Map = (Map) attrValueContext.get("A001");
		final String jobDateInfoSql = "SELECT A001055 FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
		if (a01Map != null) {
			Object jobDateObj = a01Map.get("A001055");
			if (jobDateObj != null && jobDateObj instanceof Date) {
				return new DateTime(jobDateObj).toString("yyyy.MM");
			} else {
				jobDateObj = getJobDateObj(jobDateInfoSql, empId, exportWordService);
				if (jobDateObj != null && jobDateObj instanceof Date) {
					return new DateTime(jobDateObj).toString("yyyy.MM");
				}else{
					return "";
				}
			}
		} else {
			Object jobDateObj = getJobDateObj(jobDateInfoSql, empId, exportWordService);
			if (jobDateObj != null && jobDateObj instanceof Date) {
				return new DateTime(jobDateObj).toString("yyyy.MM");
			} else {
				return "";
			}
		}
	}

	private Object getJobDateObj(String sql, String empId, ExportService exportWordService) {
		String attrInfoSql = String.format(sql, empId);
		List attrInfoList = exportWordService.findBySQL(attrInfoSql);
		if (attrInfoList != null && attrInfoList.size() > 0) {
			Map map = (Map) attrInfoList.get(0);
			if (map != null) {
				Object jobDateObj = map.get("A001055");
				if (jobDateObj == null) {
					return "";
				} else {
					return jobDateObj;
				}
			}
		}
		return "";
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
        return "工作时间";
    }	
}
