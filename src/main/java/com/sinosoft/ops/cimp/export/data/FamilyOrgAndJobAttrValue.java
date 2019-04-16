package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyOrgAndJobAttrValue implements AttrValue {

    private final String key = "fmOrgAndJob";
    private final int order = 28;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        List familyList = (List) attrValueContext.get("A36");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String fmStatus = StringUtil.obj2Str(map.get("A36041"));
            Object fmOrgAndJob = map.get("A36011");
            if (StringUtils.equals(fmStatus, "6")) {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob) + "（已退休）");
            } else if (StringUtils.equals(fmStatus, "7")) {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob) + "（已去世）");
            } else if (StringUtils.equals(fmStatus, "10")) {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob) + "（已离休）");
            } else {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob));
            }
        }
        return familyMap;
    }

//    private String getFmStatusStr(String fmStatusCode) {
//        if (StringUtils.isNotEmpty(fmStatusCode)) {
//            return CodeTranslateUtil.codeToName("DM125",fmStatusCode, ExportConstant.exportWordService);
//        } else {
//            return "";
//        }
//
//
//    }

    @Override
    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }
}
