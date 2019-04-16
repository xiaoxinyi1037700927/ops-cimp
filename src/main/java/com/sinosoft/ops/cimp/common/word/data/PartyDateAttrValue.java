package com.sinosoft.ops.cimp.common.word.data;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 入党时间
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class PartyDateAttrValue implements AttrValue {

    private final String key = "partyDate";
    private final int order = 6;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        final String partyDateInfoSql = "SELECT * FROM Emp_A030 Emp_A030 WHERE EMP_ID = '%s' order by Emp_A030.A030001 ASC";
        String a030TableSql = String.format(partyDateInfoSql, empId);
        List a030TableList = exportWordService.findBySQL(a030TableSql);
        if (a030TableList != null && a030TableList.size() > 0) {
            if (a030TableList.size() == 1) {
                Map map = (Map) a030TableList.get(0);
                attrValueContext.put("A030", map);
                Object partyDateObj = map.get("A030005");
                String partyName = StringUtil.obj2Str(map.get("A030001"));
                String partyCode = StringUtil.obj2Str(map.get("A030001"));
                if (StringUtils.equals(partyCode, "01")) {
                    if (partyDateObj != null && partyDateObj instanceof Date) {
                        return new DateTime(partyDateObj).toString("yyyy.MM");
                    }
                } else if (!StringUtils.equals(partyCode, "01")) {
                    if (StringUtils.isNotEmpty(partyName)) {
                        return partyName;
                    } else {
                        return "无党派";
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
                for (Object o : a030TableList) {
                    Map map = (Map) o;
                    Object partyDateObj = map.get("A030005");
                    String partyName = StringUtil.obj2Str(map.get("A030001"));
                    String partyCode = StringUtil.obj2Str(map.get("A030001"));
                    //代码不等于01时再添加其他党派名称
                    if (!StringUtils.equals(partyCode, "01")) {
                        partyBuilder.append("；").append(partyName);
                    }
                    if (StringUtils.equals(partyCode, "01")) {
                        if (partyDateObj != null && partyDateObj instanceof Date) {
                            partyDate = new DateTime(partyDateObj).toString("yyyy.MM");
                        }
                    }
                }
                String result = "";
                if (partyBuilder.indexOf("；") != -1) {
                    if (StringUtils.isNotEmpty(partyDate)) {
                        result = partyDate + "；" + partyBuilder.substring(1);
                    } else {
                        result = partyBuilder.substring(1);
                    }
                }
                if (StringUtils.isNotEmpty(result)) {
                    return result;
                } else {
                    return "无党派";
                }
            }
        }
        return "无党派";
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
