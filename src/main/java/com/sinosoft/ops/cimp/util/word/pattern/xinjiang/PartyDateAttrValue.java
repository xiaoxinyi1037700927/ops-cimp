package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
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
public class PartyDateAttrValue implements AttrValue {

    private final String key = "partyDate";
    private final int order = 6;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        final String partyDateInfoSql = "SELECT * FROM Emp_A030 Emp_A030 WHERE EMP_ID = '%s'  and status = 0  order by Emp_A030.A030001 ASC";
        String a030TableSql = String.format(partyDateInfoSql, empId);
        List a030TableList = exportWordService.findBySQL(a030TableSql);
        if (a030TableList != null && a030TableList.size() > 0) {
            if (a030TableList.size() == 1) {
                Map map = (Map) a030TableList.get(0);
                attrValueContext.put("A030", map);
                Object partyDateObj = map.get("A030005");
                String partyCode = StringUtil.obj2Str(map.get("A030001"));
                if (StringUtils.equals(partyCode, "01") || StringUtils.equals(partyCode, "02")) {
                	//20190520huangys修改
                	if(map.get("A030030")!=null&&map.get("A030050")==null) {
                    	return "";
                    }else {
                    	if (partyDateObj != null && partyDateObj instanceof Date) {
                            return new DateTime(partyDateObj).toString("yyyy.MM");
                        }
                    }                    
                } else if (!StringUtils.equals(partyCode, "01") || !StringUtils.equals(partyCode, "02")) {
                    if (StringUtils.equals("03", partyCode) || StringUtils.equals("13", partyCode) || StringUtils.equals("99", partyCode)) {
                        return "";
                    }else if(StringUtils.equals("12", partyCode)) {//20190520huangys修改
                    	return "无党派";
                    } else {
                    	return CodeTranslateUtil.codeToName("BT0100", partyCode, exportWordService);
                    }
                }
                if (partyDateObj != null && partyDateObj instanceof Date) {
                    return new DateTime(partyDateObj).toString("yyyy.MM");
                }
                return "无党派";
            } else {
                //如果假如中国共产党又加入其他党派则需要把时间显示到最前面
                StringBuilder partyBuilder = new StringBuilder();
                String partyDate = "";
                boolean isOut=false;
                for (Object o : a030TableList) {
                    Map map = (Map) o;
                    Object partyDateObj = map.get("A030005");
                    String partyCode = StringUtil.obj2Str(map.get("A030001"));
                    //代码不等于01时再添加其他党派名称
                    if (!StringUtils.equals(partyCode, "01") && !StringUtils.equals(partyCode, "02") &&!StringUtils.equals(partyCode, "03") && !StringUtils.equals(partyCode, "13") && !StringUtils.equals(partyCode, "99") ) {
                        partyBuilder.append("、").append(CodeTranslateUtil.codeToName("BT0100", partyCode, exportWordService));
                    }
                    if (StringUtils.equals(partyCode, "01") || StringUtils.equals(partyCode, "02"))  {
                    	//20190520huangys修改
                    	if(map.get("A030030")!=null&&map.get("A030050")==null) {
                    		partyDate="";
                    		isOut=true;
                        }else {
                        	if (partyDateObj != null && partyDateObj instanceof Date) {
                                partyDate = new DateTime(partyDateObj).toString("yyyy.MM");
                            }
                        }
                    }
                }
                String result = "";
                if (partyBuilder.indexOf("、") != -1) {
                    if (StringUtils.isNotEmpty(partyDate)) {
                        result = partyBuilder.substring(1)+"\n"+partyDate;
                    } else {
                        result = partyBuilder.substring(1);
                    }
                }
                if(isOut) {
                	return "";
                }
                if (StringUtils.isNotEmpty(result)) {
                    return result;
                } else {
                    return "无党派";
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
        return "入党时间";
    }
}
