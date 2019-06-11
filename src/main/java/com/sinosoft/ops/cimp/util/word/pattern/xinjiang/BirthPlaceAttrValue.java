package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 出生地
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class BirthPlaceAttrValue implements AttrValue {

    private final String key = "birthPlace";
    private final int order = 5;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        Map a001Map = (Map) attrValueContext.get("A001");
        final String birthPlaceInfoSql = "SELECT A001140_A,A001140_B FROM Emp_A001 Emp_A001 WHERE EMP_ID = '%s'  and status = 0 ";
//        final String birthPlaceCodeInfoSql = "SELECT * FROM SYS_CODE_ITEM WHERE SYS_CODE_ITEM.CODE_TYPE = 'BT0760' AND SYS_CODE_ITEM.CODE = '%s'";
        if (a001Map != null) {
            String birthPlaceCode = StringUtil.obj2Str(a001Map.get("A001140_A"));
            String birthPlaceName = StringUtil.obj2Str(a001Map.get("A001140_B"));
            if (StringUtils.isEmpty(birthPlaceName)) {
            	birthPlaceName = getBirthPlaceStr(birthPlaceInfoSql, empId,"A001140_B",exportWordService);
            }
            if(StringUtils.isEmpty(birthPlaceName)){
            	birthPlaceCode = getBirthPlaceStr(birthPlaceInfoSql, empId,"A001140_A",exportWordService);
//                List birthPlaceList = exportWordService.findBySQL(String.format(birthPlaceCodeInfoSql, birthPlaceCode));
//                birthPlaceName = birthPlaceList == null || birthPlaceList.size() == 0 ? "" : StringUtil.obj2Str(((Map) birthPlaceList.get(0)).get("DESCRIPTION"));
                birthPlaceName = CodeTranslateUtil.codeToName("BT0760", birthPlaceCode,exportWordService);
            }
            return birthPlaceName;
        } else {
            return getBirthPlaceStr(birthPlaceInfoSql, empId,"A001140_B",exportWordService);
        }
    }

    private String getBirthPlaceStr(String sql, String empId,String field,ExportService exportWordService){
        String attrInfoSql = String.format(sql, empId);
        List attrInfoList = exportWordService.findBySQL(attrInfoSql);
        if (attrInfoList != null && attrInfoList.size() > 0) {
            Map map = (Map) attrInfoList.get(0);
            if (map != null) {
                return StringUtil.obj2Str(map.get(field));
            }
        }
        return "";
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
        return "出生地";
    }    
}
