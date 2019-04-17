package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
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
public class TechnicPositionAttrValue implements AttrValue {

    public static final String KEY = "technicPosition";
    public static final int ORDER = 9;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            final String technicPositionInfoSql = "SELECT * FROM EMP_A06 A06 WHERE EMP_ID = '%s'  and status=0 ORDER BY A06.A06002 ASC, A06.A06025 DESC";
            List techPositionList = ExportConstant.exportWordService.findBySQL(String.format(technicPositionInfoSql, empId));
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
                attrValueContext.put("A06", techPositionList);
                Map techPosition = (Map) techPositionList.get(0);
                Integer a06002 = NumberUtils.toInt(StringUtil.obj2Str(techPosition.get("A06002")));
                //新疆只有到一定小于420的级别才显示职称，毕节什么段位的都显示出来
                //  if (a06002 < 420) {

                //20180226  任免表中如果存在两个同等级的专业技术职务，则均需要显示出来
                String value = "";
                techPositionList.remove(0);
                for (int i = 0; i < techPositionList.size(); i++) {
                    Map otherPosition = (Map) techPositionList.get(i);
                    if (Integer.parseInt((String) techPosition.get("A06002")) == Integer.parseInt((String) otherPosition.get("A06002"))) {
                        value += "\r" + (String) (otherPosition.get("A06001_A") == null ? "" : otherPosition.get("A06001_A"));
                    }
                }

                if ("".equals(value)) {
                    return techPosition.get("A06001_A");
                } else {
                    return "" + techPosition.get("A06001_A") + value;
                }
//            } else {
//                return "";
//            }
            } else {
                attrValueContext.put("A06", new ArrayList<Map>());
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A06专业技术任职资格]");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
