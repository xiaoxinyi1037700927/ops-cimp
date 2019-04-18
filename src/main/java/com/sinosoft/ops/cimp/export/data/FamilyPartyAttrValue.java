package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
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
public class FamilyPartyAttrValue implements AttrValue {

    public static final String KEY = "fmParty";
    public static final int ORDER = 26;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        List familyList = (List) attrValueContext.get("A36");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String fmParty = StringUtil.obj2Str(map.get("A36027_A"));               // 政治面貌
            String fmPartyCode = StringUtil.obj2Str(map.get("A36027_B"));           // 政治面貌代码
            if (StringUtils.isNotEmpty(fmParty)) {
                familyMap.put("fmParty_" + i, fmParty);
            } else {
                familyMap.put("fmParty_" + i, CodeTranslateUtil.codeToName("GB/T4762-1984", fmPartyCode, ExportConstant.exportWordService));
            }
        }
        return familyMap;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
