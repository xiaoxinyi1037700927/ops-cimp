package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 入党时间
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class XxcjPartyDateAttrValue implements AttrValue {

    private final String key = "xxcjPartyDate";
    private final int order = 33;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
    	boolean isShowZhengZhiMianMiaoTime = (boolean) (attrValueContext.get("isShowZhengZhiMianMiaoTime") == null ? false:attrValueContext.get("isShowZhengZhiMianMiaoTime")) ;
    	if(!isShowZhengZhiMianMiaoTime){
    		return "";
    	}
    	
    	final String partyDateInfoSql = "SELECT * FROM Emp_A030 Emp_A030 WHERE EMP_ID = '%s'  and status = 0  order by Emp_A030.A030001 ASC";
        String a030TableSql = String.format(partyDateInfoSql, empId);
        List a030TableList = exportWordService.findBySQL(a030TableSql);
        if (a030TableList != null && a030TableList.size() > 0) {
                Map map = (Map) a030TableList.get(0);
                attrValueContext.put("A030", map);
                Object partyDateObj = map.get("A030005");
                if (partyDateObj != null && partyDateObj instanceof Date) {
                    return new DateTime(partyDateObj).toString("yyyy.MM");
                }else{
                	return "";
                }
        }else{
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
        return "入党时间";
    }
}
