package com.sinosoft.ops.cimp.export.data;


import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.common.EnumType;
import com.sinosoft.ops.cimp.export.common.SetList;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import com.sinosoft.ops.cimp.util.StringUtil;

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
public class PositionAttrValue implements AttrValue {

    public static final String KEY = "position";
    public static final int ORDER = 19;

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            // shixianggui-20180411, bug: 修改现任职务和简历的最一条简历相匹配
            // shixianggui-20180308  bug331:任免表中现任职务与简历中不匹配 , 夹限制条件 A02049 <> 4 (除了 挂职任职 外)
//            final String positionInfoSql = "SELECT * FROM EMP_A02 WHERE EMP_ID = '%s'  and A02055 = '2' and status=0 and A02049 <> 4 ORDER BY A02023";
            final String positionInfoSql = "SELECT b001.code DepCode, a02.* FROM EMP_A02 a02 left join dep_b001 b001 on b001.dep_id=a02.A02001_B WHERE a02.EMP_ID = '%s'  and a02.A02055 = '2' and a02.status=0 and a02.A02049 <> 4 ORDER BY a02.A02023";
            String a02TableSql = String.format(positionInfoSql, empId);
            List jobInfoList = ExportConstant.exportWordService.findBySQL(a02TableSql);
            if (jobInfoList == null) {
                attrValueContext.put("A02", new ArrayList<Map<String, Object>>());
                jobInfoList = new ArrayList<Map<String, Object>>();
            }
            attrValueContext.put("A02", jobInfoList);

            List<Map> jobInDutyList = new ArrayList<>();
            for (Object o : jobInfoList) {
                Map jobInfoMap = (Map) o;
                jobInDutyList.add(jobInfoMap);
            }

            //按照任职机构分组，同一个机构下任多个职多个职务应该使用“、”隔开，不同机构应使用“，”隔开
            SetList<String> setList = new SetList<>();
//			for (Map map : jobInDutyList) {
//			    //分组key
//			    String deptName = StringUtil.obj2Str(map.get("A02001_A"));
//			    String jobInterName = StringUtil.obj2Str(map.get("A02085_A"));  //内设机构名称
//			    String a02016A = StringUtil.obj2Str(map.get("A02016_A"));// 职务名称
//			    setList.add(deptName + EnumType.UNIT_JOB_SPLIT.value+ jobInterName + a02016A);
//			}
//
//			return String.join(",", ResumeOrganHandle.combineOrganAndDuty(setList));


            // shixianggui-20180411, bug: 修改现任职务和简历的最一条简历相匹配
            String deptName = null;
            String jobInterName = null;
            String a02016A = null;
            String A02004 = null;
            String A02009 = null;
            String depCode = null;
            for (Map map : jobInDutyList) {
                deptName = StringUtil.obj2Str(map.get("A02001_A"));
                jobInterName = StringUtil.obj2Str(map.get("A02085_A"));  //内设机构名称
                a02016A = StringUtil.obj2Str(map.get("A02016_A"));// 职务名称
                A02004 = ParticularUtils.trim(map.get("A02004"), "");// 机构隶属关系Code
                A02009 = ParticularUtils.trim(map.get("A02009"), "");//任职机构性质类别
                depCode = ParticularUtils.trim(map.get("DEPCODE"), "");

                setList.add(deptName + EnumType.UNIT_JOB_SPLIT.value + jobInterName + a02016A
                        + EnumType.UNIT_JOB_SPLIT.value + " " + A02004
                        + EnumType.UNIT_JOB_SPLIT.value + " " + A02009
                        + EnumType.UNIT_JOB_SPLIT.value + " " + depCode);
            }
            return ResumeOrganHandleNew.main(setList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("任免表生成失败:[EMP_A02职务信息]");
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
