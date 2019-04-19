package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class PartyDateAttrValue implements AttrValue {

    public static final String KEY = "partyDate";
    public static final int ORDER = 6;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            final String partyDateInfoSql = "SELECT * FROM EMP_A58 A58 WHERE EMP_ID = '%s'  and status=0";
            String a58TableSql = String.format(partyDateInfoSql, empId);
            List a58TableList = ExportConstant.exportWordService.findBySQL(a58TableSql);
            if (a58TableList != null && a58TableList.size() > 0) {
                if (a58TableList.size() == 1) {
                    Map map = (Map) a58TableList.get(0);
                    attrValueContext.put("A58", map);
                    Object partyDateObj = map.get("A58005");
                    String partyName = StringUtil.obj2Str(map.get("A58001_A"));
                    String partyCode = StringUtil.obj2Str(map.get("A58001_B"));
                    if (StringUtils.equals(partyCode, "01")) {
                        if (partyDateObj != null) {
                            return new DateTime(partyDateObj).toString("yyyy.MM");
                        }
                    } else if (!StringUtils.equals(partyCode, "01")) {
                        return StringUtils.isNotEmpty(partyName) ? partyName : "无党派";
                    }
                    if (partyDateObj != null) {
                        return new DateTime(partyDateObj).toString("yyyy.MM");
                    }
                    return "无党派";
                } else {
                    //如果假如中国共产党又加入其他党派则需要把时间显示到最前面
                    StringBuilder partyBuilder = new StringBuilder();
                    String partyDate = "";
                    for (Object o : a58TableList) {
                        Map map = (Map) o;
                        Object partyDateObj = map.get("A58005");
                        String partyName = StringUtil.obj2Str(map.get("A58001_A"));
                        String partyCode = StringUtil.obj2Str(map.get("A58001_B"));
                        //代码不等于01时再添加其他党派名称
                        if (!StringUtils.equals(partyCode, "01")) {
                            partyBuilder.append("；").append(partyName);
                        }
                        if (StringUtils.equals(partyCode, "01")) {
                            if (partyDateObj != null) {
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
                    return StringUtils.isNotEmpty(result) ? result : "无党派";
                }
            }
            return "无党派";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A58政治面貌]");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
