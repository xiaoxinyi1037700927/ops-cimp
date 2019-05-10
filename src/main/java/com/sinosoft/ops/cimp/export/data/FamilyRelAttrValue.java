package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
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
public class FamilyRelAttrValue implements AttrValue {

    public static final String KEY = "fmRel";
    public static final int ORDER = 23;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            // shixianggui-20180312, 根据赵科要求只显示: 该人员的配偶、子女、父母、继父或养父、继母或养母;
            String a36TableSql = "SELECT * FROM EMP_A36 WHERE (A36049='1' or A36005_B in("
                    + "'01', '02'," // 本人
                    + "'10', '11', '12'," // 配偶
                    + "'20', '21', '22', '23', '24', '25', '26', '27', '29'," //儿子
                    + "'30', '31', '32', '33', '34', '35', '36', '37', '39'," //女儿
                    + "'50', '51', '52'," // 父母
                    + "'57', " // 继父或养父
                    + "'58'" //继母或养母
                    + ")) "
                    + " and EMP_ID = '%s' and status=0 ORDER BY A36047 ASC";
//			String a36TableSql = "SELECT * FROM EMP_A36 WHERE EMP_ID = '%s'  and status=0 ORDER BY A36047 ASC";

            a36TableSql = String.format(a36TableSql, empId);
            List a36TableList = ExportConstant.exportService.findBySQL(a36TableSql);

            if (a36TableList == null) {
                attrValueContext.put("A36", new ArrayList<Map>());
                a36TableList = new ArrayList<Map>();
            }
            Map<String, String> familyMap = new HashMap<>();

            Map map = null;
            Map mapNex = null;
            String familyRel = null;
            String A36005_B = null;
            String A36005_BNex = null;
            String sortArray[] = {"20", "21", "22", "23", "24", "25", "26", "27", "29", "30", "31", "32", "33", "34", "35", "36", "37", "39"};
            for (int i = 0; i < a36TableList.size(); i++) {
                //冒泡排序进行子女时间排序
                for (int j = 0; j < a36TableList.size() - 1; j++) {
                    map = (Map) a36TableList.get(j);
                    A36005_B = StringUtil.obj2Str(map.get("A36005_B"));
                    if (!ArrayUtils.contains(sortArray, A36005_B)) {
                        continue;
                    }
                    mapNex = (Map) a36TableList.get(j + 1);
                    A36005_BNex = StringUtil.obj2Str(mapNex.get("A36005_B"));
                    if (!ArrayUtils.contains(sortArray, A36005_BNex)) {
                        continue;
                    }
                    Object fmBirthday = map.get("A36007");
                    Object fmBirthdayNex = mapNex.get("A36007");
                    if (fmBirthday != null && fmBirthdayNex != null) {
                        if (!new DateTime(fmBirthday).isBefore(new DateTime(fmBirthdayNex))) {
                            Object temp = a36TableList.get(j);
                            a36TableList.set(j, a36TableList.get(j + 1));
                            a36TableList.set(j + 1, temp);
                        }
                    }
                }
            }
            // shixianggui-20180312, 称昵修改: 夫 --> 丈夫; 妻 --> 妻子; 独生子 --> 儿子; 独生女 --> 女儿;
            for (int i = 0; i < a36TableList.size(); i++) {
                map = (Map) a36TableList.get(i);
                A36005_B = StringUtil.obj2Str(map.get("A36005_B"));
                // 夫 --> 丈夫
                if ("11".equals(A36005_B)) {
                    familyRel = "丈夫";
                }
                // 妻 --> 妻子
                else if ("12".equals(A36005_B)) {
                    familyRel = "妻子";
                }
                // 独生子 --> 儿子
                else if ("21".equals(A36005_B)) {
                    familyRel = "儿子";
                }
                // 独生女 --> 女儿
                else if ("31".equals(A36005_B)) {
                    familyRel = "女儿";
                } else {
                    familyRel = StringUtil.obj2Str(map.get("A36005_A"));
                }
                familyMap.put("fmRel_" + i, familyRel);
            }
            attrValueContext.put("A36", a36TableList);
            return familyMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A36家庭成员及社会关系 ]");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

}
