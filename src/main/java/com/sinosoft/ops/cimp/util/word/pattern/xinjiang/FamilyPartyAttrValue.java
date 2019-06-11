package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 * 家庭主要成员和社会关系--政治面貌
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyPartyAttrValue implements AttrValue {

    private final String key = "fmParty";
    private final int order = 26;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        List familyList = (List) attrValueContext.get("A200");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String fmPartyCode = StringUtil.obj2Str(map.get("A200030"));           // 政治面貌代码
            String fmParty = "";
            if (StringUtils.isNotEmpty(fmPartyCode)) {
            	fmParty = CodeTranslateUtil.codeToName("BT0100", fmPartyCode, exportWordService);
            	if(fmParty.length()>2){
            		fmParty = fmParty.substring(0, 2)+" "+fmParty.substring(2);
            	}
           }
            familyMap.put("fmParty_" + i, fmParty);
        }
        return familyMap;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public String getKey() {
        return key;
    }
    
    @Override
    public String getTitle() {
        return "家庭主要成员和社会关系--政治面貌";
    }
}
