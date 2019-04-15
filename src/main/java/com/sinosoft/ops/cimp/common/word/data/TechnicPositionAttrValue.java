package com.sinosoft.ops.cimp.common.word.data;

import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/9.
 * 专业技术职务
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class TechnicPositionAttrValue implements AttrValue {

    private final String key = "technicPosition";
    private final int order = 9;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId, ExportService exportWordService) {
        final String technicPositionInfoSql = "SELECT * FROM Emp_A105 Emp_A105 WHERE EMP_ID = '%s' ORDER BY Emp_A105.A105015 ASC, Emp_A105.A105010 DESC";
        List techPositionList = exportWordService.findBySQL(String.format(technicPositionInfoSql, empId));
        //使用码表 代码类型为：GB/T12407-2008
        // 410 高级
        // 411 正高级
        // 412 副高级
        // 420 中级
        // 430 初级
        // 434 助理级
        // 435 员级
        // 499 未定职级专业技术人员
        //获取最高职级职称

        if (techPositionList != null && techPositionList.size() > 0) {
            attrValueContext.put("A105", techPositionList);
            Map techPosition = (Map) techPositionList.get(0);
            Integer a06002 = NumberUtils.toInt(StringUtil.obj2Str(techPosition.get("A105015")));
            if (a06002 < 420) {
                return techPosition.get("A105001_B");
            } else {
                return "";
            }
        } else {
            attrValueContext.put("A105", new ArrayList<Map>());
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
}
