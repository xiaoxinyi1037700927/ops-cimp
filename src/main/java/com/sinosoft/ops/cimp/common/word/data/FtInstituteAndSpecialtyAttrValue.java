package com.sinosoft.ops.cimp.common.word.data;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/10.
 * 学位学历 -- 毕业学院系及专业
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FtInstituteAndSpecialtyAttrValue implements AttrValue {

    private final String key = "ftInstituteAndSpecialty";
    private final int order = 12;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) throws Exception {
        Map<String, List<Map>> fullEduMapList = (Map<String, List<Map>>) attrValueContext.get("fullEduMapList");
        List<Map> fullTimeEdu = fullEduMapList.get("xueli");    //全日制教育
        String ftInstituteAndSpecialty = "";
        if (fullTimeEdu != null && fullTimeEdu.size() > 0) {
            Map fullTimeMap = fullTimeEdu.get(0);
            String A005001A = StringUtil.obj2Str(fullTimeMap.get("A005001_A")); //学历代码
            //高中及以下学历， 毕业院系专业不显示
            if (StringUtils.isNotEmpty(A005001A) && NumberUtils.toInt(A005001A.substring(0, 1)) > 5) {
                return "";
            }

            String ftInstitute = StringUtil.obj2Str(fullTimeMap.get("A005040")); //毕业院校
            String ftSpecialtyCode = StringUtil.obj2Str(fullTimeMap.get("A005010_A")); //专业信息代码
            String ftSpecialty = StringUtil.obj2Str(fullTimeMap.get("A005010_B")); //专业信息名称
            if (!StringUtils.equals(ftSpecialty, "")) {
                ftSpecialty = ftSpecialty.endsWith("专业") ? ftSpecialty : (ftSpecialty + "专业");
            } else if (StringUtil.isNotEmptyOrNull(ftSpecialtyCode)) {
                ftSpecialty = CodeTranslateUtil.codeToName("BT0055", ftSpecialtyCode, exportWordService);
                ftSpecialty = ftSpecialty.endsWith("专业") ? ftSpecialty : (ftSpecialty + "专业");
            }
            ftInstituteAndSpecialty = ftInstitute + ftSpecialty;
        }
        return ftInstituteAndSpecialty;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
