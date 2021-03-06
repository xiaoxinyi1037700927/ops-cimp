package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 * 家庭主要成员和社会关系--称谓
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyRelAttrValue implements AttrValue {

    private final String key = "fmRel";
    private final int order = 23;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        String a200TableSql = "SELECT * FROM Emp_A200 WHERE EMP_ID = '%s'  and status = 0  ORDER BY A200005 ASC";
        a200TableSql = String.format(a200TableSql, empId);
        List a36TableList = exportWordService.findBySQL(a200TableSql);

        if (a36TableList == null) {
            attrValueContext.put("A200", new ArrayList<Map>());
            a36TableList = new ArrayList<Map>();
        }
        attrValueContext.put("A200", a36TableList);

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < a36TableList.size(); i++) {
            Map map = (Map) a36TableList.get(i);
            String familyRelCode = StringUtil.obj2Str(map.get("A200005"));
            String familyRel = "";
            if (StringUtils.isNotEmpty(familyRelCode)) {
            	familyRel = CodeTranslateUtil.codeToName("BT0095", familyRelCode, exportWordService);
            }
            familyMap.put("fmRel_" + i, familyRel);
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
        return "家庭主要成员和社会关系--称谓";
    }
}
