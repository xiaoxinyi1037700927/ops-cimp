package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/18.
 * 家庭主要成员和社会关系--工作单位及职务
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyOrgAndJobAndIdAttrValue implements AttrValue {

    private final String key = "fmOrgAndJob";
    private final int order = 27;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        List familyList = (List) attrValueContext.get("A200");

        Map<String, String> familyMap = new HashMap<String, String>();
        for (int i = 0; i < familyList.size(); i++) {
            Map map = (Map) familyList.get(i);
            String fmStatus = StringUtil.obj2Str(map.get("A200040"));
            Object fmOrgAndJob = map.get("A200020");                    // 工作单位及职务
            String familyId = StringUtil.obj2Str(map.get("A200035"));
            if(StringUtil.isEmptyOrNull(familyId)){
            	familyId = "";
            }
            if (StringUtils.equals(fmStatus, "2")) {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob) + "（已离休）("+familyId+")");
            }else if(StringUtils.equals(fmStatus, "3")){
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob) + "（已退休）("+familyId+")");
            } else if (StringUtils.equals(fmStatus, "8")) {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob) + "（已去世）("+familyId+")");
            } else {
                familyMap.put("fmOrgAndJob_" + i, fmOrgAndJob == null ? "" : StringUtil.obj2Str(fmOrgAndJob)+"("+familyId+")");
            }
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
        return "家庭主要成员和社会关系--工作单位及职务";
    }
}
