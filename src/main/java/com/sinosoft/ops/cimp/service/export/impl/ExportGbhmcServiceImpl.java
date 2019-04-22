package com.sinosoft.ops.cimp.service.export.impl;

import com.sinosoft.ops.cimp.common.excel.ExcelType;
import com.sinosoft.ops.cimp.common.excel.SExcelWriter;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.export.common.EnumType;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.common.SetList;
import com.sinosoft.ops.cimp.export.data.ResumeOrganHandleNew;
import com.sinosoft.ops.cimp.service.export.ExportGbhmcService;
import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
import com.sinosoft.ops.cimp.util.FileUtils;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import com.sinosoft.ops.cimp.util.StringUtil;
import com.sinosoft.ops.cimp.vo.from.export.ExportGbhmcModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@Service
public class ExportGbhmcServiceImpl implements ExportGbhmcService {

    private static final String INCLUD_CHILD_PRESON_ORDER = " order by A05002_B_C,A01060_C,length(PPTR_C),DEP_ORDINAL_C,A02025_C,";
    private static final String INCLUD_CHILD_PRESON_FROM = ",a1.A05002_B A05002_B_C,a1.A02025 A02025_C,(case a1.pptrs when null then null else (case when instr(a1.pptrs, ',') >= 1 then substr(a1.pptrs, 1, instr(a1.pptrs, ',') - 1) else a1.pptrs end) end) PPTR_C," +
            "(case a1.dep_ordinals when null then null else (case when instr(a1.dep_ordinals, ',') >= 1 then substr(a1.dep_ordinals, 1, instr(a1.dep_ordinals, ',') - 1) else a1.dep_ordinals end) end) DEP_ORDINAL_C," +
            "(case a1.dep_seqids when null then null else (case when instr(a1.dep_seqids, ',') >= 1 then substr(a1.dep_seqids, 1, instr(a1.dep_seqids, ',') - 1) else a1.dep_seqids end) end) DEP_SEQID_C," +
            "(case when t1.A01060 like '1%' then '10' when t1.A01060 like '2%' then '20' when t1.A01060 like '4%' then '40' else '90' end) A01060_C from EMP_A001 t1 " +
            "left join (select emp_a001.emp_id,wm_concat(a2.A05002_B) A05002_Bs,min(a2.A05002_B) A05002_B,wm_concat(a2.dep_id) dep_ids,wm_concat(a2.pptr) pptrs,wm_concat(a2.seqid) dep_seqids,wm_concat(a2.dep_ordinal) dep_ordinals,MIN(NVL(a2.A02025, 999)) A02025 " +
            "from emp_a001 inner join (select emp_a02.emp_id,(case when A05002_B is null then null when A05002_B<'122' or (A05002_B>'2' and A05002_B<'222') then '00' || A05002_B " +
            "when A05002_B='122' or A05002_B='222' then '05' || A05002_B when (A05002_B='131' or A05002_B='231') and A02008 like '1%' then '10' || A05002_B when (A05002_B='132' or A05002_B='232') and A02008 like '1%' then '15' || A05002_B " +
            "when (A05002_B='131' or A05002_B='231') and (A02008 is null or A02008 like '2%') then '20' || A05002_B when (A05002_B='132' or A05002_B='232') and (A02008 is null or A02008 like '2%') then '25' || A05002_B " +
            "when (A05002_B='141' or A05002_B='241') and A02008 like '1%' then '30' || A05002_B when (A05002_B='142' or A05002_B='242') and A02008 like '1%' then '35' || A05002_B when A05002_B='141' and (A02008 is null or A02008 like '2%') then '40' || A05002_B " +
            "when A05002_B='142' and (A02008 is null or A02008 like '2%') then '45' || A05002_B when A05002_B='150' or A05002_B='250' then '50' || A05002_B when (A05002_B>'150' and A05002_B<'2') or A05002_B>'250' then '60' || A05002_B " +
            "else '90' || A05002_B end) A05002_B,dep_b001.dep_id,dep_b001.pptr,dep_b001.seqid,dep_b001.ordinal dep_ordinal,A02025 from emp_a02 left join dep_b001 on A02001_B=dep_b001.dep_id " +
            "left join emp_a05 on emp_a05.emp_id=emp_a02.emp_id and A05024='1' " +
            "where emp_a02.status=0 and emp_a02.A02055='2' and dep_b001.tree_level_code like :code order by A05002_B,length(dep_b001.pptr),dep_b001.ordinal) a2 " +
            "on a2.emp_id=emp_a001.emp_id group by emp_a001.emp_id) a1 on a1.emp_id=t1.emp_id ";

    private final ExportService exportService;

    private final SysCodeItemService sysCodeItemService;

    @Autowired
    public ExportGbhmcServiceImpl(ExportService exportService, SysCodeItemService sysCodeItemService) {
        this.exportService = exportService;
        this.sysCodeItemService = sysCodeItemService;
    }


    /**
     * 生成干部花名册
     */
    @Override
    public String generateGbhmc(ExportGbhmcModel model) {
        //获取导出数据
        Map<String, List<Map<Integer, String>>> data = model.getContainDutyDepart() ? dutyMc(model) : getHmcData(model);

        return generateExcel(data, model);
    }

    private Map<String, List<Map<Integer, String>>> dutyMc(ExportGbhmcModel model) {
        Map<String, List<Map<Integer, String>>> result = new LinkedHashMap<>();
        //查询人员
        Map<String, List<Map<Integer, String>>> empList = getHmcData(model);
        //查询单位树
        Map<String, String> treeIds = addTreeChildren(model.getOrgId(), model.getContainChild(), true);
        //遍历单位树
        for (Map.Entry<String, String> entry : treeIds.entrySet()) {
            String aId = entry.getKey();
            String aName = entry.getValue();
            if (empList.containsKey(aId)) {
                List<Map<Integer, String>> aList = empList.get(aId);
                result.put(aName, aList);
            }
        }
        return result;
    }

    private Map<String, List<Map<Integer, String>>> getHmcData(ExportGbhmcModel model) {
        Map<String, List<Map<Integer, String>>> stringListMap = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(model.getOrgId())) {
            //性别
            List<SysCodeItem> codeItemsGender = sysCodeItemService.findByCodeSetName("GB/T2261.1-2003");

            //民族
            List<SysCodeItem> codeItemsNationality = sysCodeItemService.findByCodeSetName("GB/T3304-1991");

            Map<String, List> empIdStrMap = model.getContainDutyDepart() ? getDutyEmpIdsByTreeId(model) : getEmpIds(model);
            List<String[]> organEmpList = new ArrayList<>();
            List<String> empIds = new ArrayList<>();
            for (Map.Entry<String, List> entry : empIdStrMap.entrySet()) {
                String organ = entry.getKey();
                List<String> empList = entry.getValue();
                for (String empId : empList) {
                    if (empId != null) {
                        empIds.add(empId);
                        organEmpList.add(new String[]{organ, empId});
                    }
                }
            }
            List<List<String>>[] lists = getIdBatchLists(empIds);
            List<List<String>> batchEmpIds = lists[0];
            List<List<String>> batchEmpIdStr = lists[1];
            for (int a = 0; a < batchEmpIdStr.size(); a++) {
                List<String> empIdsList = batchEmpIds.get(a);
                List<String> empIdStrList = batchEmpIdStr.get(a);
                // 人员信息
                String sql = "select t1.EMP_ID,t1.A01001,t1.A001003,t1.A01004,t1.A01007,t1.A01017,t1.A01011_A ,t1.A01014_A,t1.A01034,t1.A01027,t1.A01087,t1.A01088 " +
                        INCLUD_CHILD_PRESON_FROM.replace(" dep_b001.tree_level_code like :code ", " (50=50) ") + " where t1.emp_id in :ids " +
                        INCLUD_CHILD_PRESON_ORDER + " t1.ordinal";
                List<Map<String, Object>> baseBatchList = getBatchList(empIdStrList, sql);
                // 教育信息（最高学历信息）
                sql = "select  EMP_ID,A08002_A from EMP_A08 where EMP_ID in :ids and A08034='1'  ORDER BY  A08002_B asc ";
                List<Map<String, Object>> eduBatchList = getBatchList(empIdStrList, sql);
                // 教育信息（最高学位信息）
                sql = "select  EMP_ID,A09001_A from EMP_A09 where EMP_ID in :ids and A09014='1'  ORDER BY  A09001_B asc ";
                List<Map<String, Object>> highEduBatchList = getBatchList(empIdStrList, sql);
                // 现任职务，任职起始年月
                sql = "SELECT b001.code DepCode, a02.* FROM EMP_A02 a02 left join dep_b001 b001 on b001.dep_id=a02.A02001_B WHERE a02.EMP_ID in :ids and a02.A02055 = '2' and a02.status=0 and a02.A02049 <> 4 order by a02.A02023,a02.a02008 ASC";
                List<Map<String, Object>> jobBatchList = getBatchList(empIdStrList, sql);
                // 现任职务级别
                sql = "select EMP_ID,A05002_A,A05004,A05024 from EMP_A05 where A05004 is not null and EMP_ID in :ids and A05024 = '1' and status = 0 order by A05004 desc";
                List<Map<String, Object>> jobLevelBatchList = getBatchList(empIdStrList, sql);
                // 入党时间
                sql = "SELECT * FROM EMP_A58 A58 WHERE EMP_ID in :ids  and status=0 order by A58005";
                List<Map<String, Object>> partyDateBatchList = getBatchList(empIdStrList, sql);
                // 最高学历学位信息
                sql = "select a.EMP_ID,a.A08002_A,a.A08020,a.A08014,a.A08024 ,a.SUB_ID A08SUBID, b.A09001_A ,b.SUB_ID A09SUBID from EMP_A08 a LEFT JOIN EMP_A09 b on a.EMP_ID=b.EMP_ID"
                        + " and a.a08020=b.a09081 where a.EMP_ID in :ids  and  a.a08020 in (2,21,22,23,29) AND (a.A08034='1' or b.A09014 ='1') ORDER BY  A08002_B,A09001_B asc ";
                List<Map<String, Object>> mbaBatchList = getBatchList(empIdStrList, sql);
                sql = "select a.EMP_ID,a.A08002_A,a.A08020,a.A08014,a.A08024 ,a.SUB_ID A08SUBID, b.A09001_A ,b.SUB_ID A09SUBID from EMP_A08 a LEFT JOIN EMP_A09 b on a.EMP_ID=b.EMP_ID"
                        + " and a.a08020=b.a09081 where a.EMP_ID in :ids  and a.a08020='1' AND (a.A08034='1' or b.A09014 ='1' ) ORDER BY  A08002_B,A09001_B asc ";
                List<Map<String, Object>> fmbaBatchList = getBatchList(empIdStrList, sql);
                List<String[]> thisOrganEmpList = organEmpList.stream().filter(item -> empIdsList.contains(item[1])).collect(Collectors.toList());
                for (String[] organEmp : thisOrganEmpList) {
                    String organ = organEmp[0];
                    String EMP_ID = organEmp[1];
                    List<Map<String, Object>> baseInfoList = baseBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                    List<Map<Integer, String>> mapList = new ArrayList<>();
                    // 信息填充
                    for (Map baseInfo : baseInfoList) {
                        List<Map<String, Object>> eduInfoList = eduBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                        List<Map<String, Object>> highEduInfoList = highEduBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                        List<Map<String, Object>> jobInfoList = jobBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                        List<Map<String, Object>> jobLevelInfoList = jobLevelBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                        List<Map<String, Object>> partyDateInfoList = partyDateBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                        List<Map<String, Object>> mbaSqlList = mbaBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());
                        List<Map<String, Object>> fmbaSqlList = fmbaBatchList.stream().filter(item -> item.get("EMP_ID").equals(EMP_ID)).collect(Collectors.toList());

                        String a01001 = StringUtil.obj2Str(baseInfo.get("A01001"));
                        if (a01001.length() == 2) {// 将2个字的姓名中间加入空格
                            a01001 = a01001.substring(0, 1) + "  " + a01001.substring(1);
                        }
                        Map<Integer, String> temp = new HashMap<>(17);
                        temp.put(0, a01001);

                        if (codeItemsGender.stream().filter(findA01004 -> findA01004.getCode().equals(baseInfo.get("A01004").toString())).count() == 1) {
                            SysCodeItem sysCodeItem = codeItemsGender.stream()
                                    .filter(findA01004 -> findA01004.getCode().equals(baseInfo.get("A01004").toString())).findFirst().get();
                            temp.put(3, StringUtil.obj2Str(sysCodeItem.getName()));
                        } else {
                            temp.put(3, StringUtil.obj2Str(baseInfo.get("A01004")));
                        }

                        if (codeItemsNationality.stream().filter(findA01017 -> findA01017.getCode().equals(baseInfo.get("A01017").toString())).count() == 1) {
                            SysCodeItem sysCodeItem = codeItemsNationality.stream()
                                    .filter(findA01017 -> findA01017.getCode().equals(baseInfo.get("A01017").toString())).findFirst().get();
                            temp.put(4, StringUtil.obj2Str(sysCodeItem.getName()));
                        } else {
                            temp.put(4, StringUtil.obj2Str(baseInfo.get("A01017")));
                        }

                        temp.put(5, StringUtil.obj2Str(baseInfo.get("A01011_A")));
                        temp.put(6, StringUtil.obj2Str(baseInfo.get("A01014_A")));
                        String a01007 = StringUtil.date2Str(baseInfo.get("A01007")); // 出生年月

                        temp.put(7, a01007);
                        String a01034 = StringUtil.date2Str(baseInfo.get("A01034")); // 参加工作时间

                        temp.put(8, a01034);

                        String a58005 = partyDateString(partyDateInfoList); // 入党时间
                        temp.put(9, a58005);

                        // lixianfu-20180623 修改bug: 花名册中 任现职务和干部任免表中的信息格式不一致
                        List<Map> jobInDutyList = new ArrayList<>();
                        for (Object o : jobInfoList) {
                            Map jobInfoMap = (Map) o;
                            jobInDutyList.add(jobInfoMap);
                        }
                        SetList setList = new SetList();
                        String deptName = null;
                        String jobInterName = null;
                        String a02016A = null;
                        String A02004 = null;
                        String A02009 = null;
                        String depCode = null;
                        for (Map map : jobInDutyList) {
                            deptName = StringUtil.obj2Str(map.get("A02001_A"));//任职机构名称
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
                        temp.put(1, ResumeOrganHandleNew.main(setList));

                        // 多个职务按逗号分割
                        StringBuilder timeValue = new StringBuilder();
                        List<Integer> k = new ArrayList<>();
                        boolean first = true;
                        for (int i = 0; i < jobInfoList.size(); i++) {
                            if (jobInfoList.get(i) != null) {
                                if (k.contains(i)) {
                                    continue;
                                }
                                Map jobMap = jobInfoList.get(i);
                                Object a02001_a = jobMap.get("A02001_A");//前缀
                                Object a02016_a = jobMap.get("A02016_A");//职务
                                Object A02085_A = jobMap.get("A02085_A");//

                                Object a02043 = StringUtil.date2Str(jobMap.get("A02043"));//时间

                                // 如果有两个或两个以上日期,做换行处理
                                timeValue.append(first ? a02043 : "\n" + a02043);

                                for (int j = i + 1; j < jobInfoList.size(); j++) {
                                    if (jobInfoList.get(i) != null) {
                                        Map objMap = jobInfoList.get(j);
                                        Object obja02001_a = objMap.get("A02001_A");

                                        if (StringUtils.equals(StringUtil.obj2Str(a02001_a),
                                                StringUtil.obj2Str(obja02001_a))) {
                                            k.add(j);
                                            timeValue.append("\n").append(StringUtil.date2Str(objMap.get("A02043")));
                                        }
                                    }
                                }
                            }
                            first = false;
                        }
                        temp.put(15, timeValue.toString().length() > 0 ? timeValue.toString().trim() : ""); // 按日期升序排序

                        if (jobLevelInfoList.size() > 0) {
                            Object jobLevelObj = jobLevelInfoList.get(0);
                            if (jobLevelObj != null) {
                                StringBuilder sb = new StringBuilder();

                                Map jobLevelObjMap = (Map) jobLevelObj;
                                String a05002_a = StringUtil.obj2Str(jobLevelObjMap.get("A05002_A"));
                                Object a05004 = jobLevelObjMap.get("A05004");

                                if (EnumType.positionEnum.四级职员.toString().equals(a05002_a)) {
                                    a05002_a = EnumType.positionEnum.四级职员.getValue();
                                } else if (EnumType.positionEnum.五级职员.toString().equals(a05002_a)) {
                                    a05002_a = EnumType.positionEnum.五级职员.getValue();
                                } else if (EnumType.positionEnum.六级职员.toString().equals(a05002_a)) {
                                    a05002_a = EnumType.positionEnum.六级职员.getValue();
                                } else if (EnumType.positionEnum.七级职员.toString().equals(a05002_a)) {
                                    a05002_a = EnumType.positionEnum.七级职员.getValue();
                                } else if (EnumType.positionEnum.八级职员.toString().equals(a05002_a)) {
                                    a05002_a = EnumType.positionEnum.八级职员.getValue();
                                }
                                sb.append(a05002_a);
                                if (jobInfoList.size() > 0) {
                                    Map jobInfoListMap = jobInfoList.get(jobInfoList.size() - 1);
                                    String a02008 = StringUtil.obj2Str(jobInfoListMap.get("A02008"));
                                    if (a02008.length() > 0) {
                                        if (a02008.startsWith("1")) {
                                            sb.append("领导职务");
                                        } else {
                                            sb.append("非领导职务");
                                        }
                                    }
                                }
                                temp.put(2, sb.toString());
                                temp.put(16, StringUtil.date2Str(a05004));
                            }
                        } else {
                            temp.put(2, "");
                            temp.put(15, "");
                        }

                        // 在职教育
                        if (mbaSqlList.size() == 0) {
                            temp.put(12, "");
                            temp.put(13, "");
                        } else {
                            Map map1 = mbaSqlList.get(0);
                            String a08002A = StringUtil.obj2Str(map1.get("A08002_A"));
                            String a08014 = StringUtil.obj2Str(map1.get("A08014"));
                            String a08024 = StringUtil.obj2Str(map1.get("A08024"));//专业
                            String a08SubId = StringUtil.obj2Str(map1.get("A08SUBID"));
                            String a09001A = StringUtil.obj2Str(map1.get("A09001_A"));
                            String a09SubId = StringUtil.obj2Str(map1.get("A09SUBID"));
                            if (a08002A.equals("") && a09001A.equals("")) {
                                temp.put(12, "");
                            } else {
                                temp.put(12, a08002A + "\n" + a09001A);

                            }
                            if (a08014.equals("") && a08024.equals("")) {
                                temp.put(13, "");
                            } else {
                                temp.put(13, a08014 + a08024 + "专业");
                            }
                        }
                        // 全日制教育
                        if (fmbaSqlList.size() == 0) {
                            temp.put(10, "");
                            temp.put(11, "");
                        } else {
                            Map map2 = fmbaSqlList.get(0);
                            String a08002A = StringUtil.obj2Str(map2.get("A08002_A"));
                            String a08014 = StringUtil.obj2Str(map2.get("A08014"));
                            String a08024 = StringUtil.obj2Str(map2.get("A08024"));
                            String a08SubId = StringUtil.obj2Str(map2.get("A08SUBID"));
                            String a09001A = StringUtil.obj2Str(map2.get("A09001_A"));
                            String a09SubId = StringUtil.obj2Str(map2.get("A09SUBID"));
                            if (a08002A.equals("") && a09001A.equals("")) {
                                temp.put(10, "");
                            } else {
                                temp.put(10, a08002A + "\n" + a09001A);
                            }
                            if (a08014.equals("") && a08024.equals("")) {
                                temp.put(11, "");
                            } else {
                                if (a08002A.contains(EnumType.educationEnum.high.getValue())
                                        || a08002A.contains(EnumType.educationEnum.juniorMiddle.getValue())
                                        || a08002A.contains(EnumType.educationEnum.primary.getValue())
                                        || a08002A.contains(EnumType.educationEnum.illiteracy.getValue())) {
                                    temp.put(11, "");
                                } else {
                                    temp.put(11, a08014 + a08024 + "专业");
                                }
                            }
                        }

                        // 最高学位学历
                        StringBuilder sb = new StringBuilder();
                        if (eduInfoList.size() > 0) {
                            Map eduInfo = eduInfoList.get(0);
                            String A08002_A = StringUtil.obj2Str(eduInfo.get("A08002_A"));
                            sb.append(A08002_A);
                        }

                        if (highEduInfoList.size() > 0) {
                            sb.append("\n");
                            Map highEduInfo = highEduInfoList.get(0);
                            String A09001_A = StringUtil.obj2Str(highEduInfo.get("A09001_A"));
                            sb.append(A09001_A);
                        }
                        if (sb.length() == 0) {
                            temp.put(14, "");
                        }
                        temp.put(14, sb.toString());

                        if (model.getContainIdCardNo()) {
                            temp.put(17, StringUtil.obj2Str(baseInfo.get("A001003")));
                        } else {
                            temp.put(17, " ");
                        }
                        mapList.add(temp);

                    }
                    if (stringListMap.containsKey(organ)) {
                        List<Map<Integer, String>> list = stringListMap.get(organ);
                        list.addAll(mapList);
                        stringListMap.put(organ, list);
                    } else {
                        stringListMap.put(organ, mapList);
                    }
                }
            }
        }
        return stringListMap;
    }

    private Map<String, List> getEmpIds(ExportGbhmcModel model) {
        StringBuilder sb = new StringBuilder("select t1.emp_id,b01.* from EMP_A001 t1, DEP_B001 b01 where t1.a001004_a = b01.dep_id ");

        //默认排除离退人员
        sb.append(" and t1.A01063 <> '2' ");

        if (StringUtils.isNotEmpty(model.getEmpIds())) {
            sb.append(" and t1.emp_id in ");
            sb.append(Stream.of(StringUtils.split(model.getEmpIds(), ','))
                    .collect(Collectors.joining("','", "('", "') ")));
        } else {
            sb.append(" and b01.dep_id is not null and t1.status=0 ");
            sb.append(" and b01.code ");
            //是否包含子节点
            if (model.getContainChild()) {
                sb.append(" like (select code || '%' ");
            } else {
                sb.append(" = (select code ");
            }
            sb.append(" from DEP_B001 where dep_id = '").append(model.getOrgId()).append("') ");
        }

        String strQuery = sb.toString();
        String srcFrom = "from EMP_A001 t1";
        int posFrom = strQuery.indexOf(srcFrom);
        strQuery = strQuery.substring(0, posFrom) + INCLUD_CHILD_PRESON_FROM.replace(":code", "'001%'") + strQuery.substring(posFrom + srcFrom.length());
        strQuery += INCLUD_CHILD_PRESON_ORDER + "t1.ordinal";

        List<Map<String, Object>> res = exportService.findBySQL(strQuery);

        //按机构名称分组，采用Map<String,List<Object></>></> 存储，key 为机构，value 为 机构对应的每条记录
        Map<String, List> listMap = new LinkedHashMap<>();
        String organ = null;
        String emp_id = null;
        for (Map<String, Object> map : res) {
            //按单位
            organ = StringUtil.obj2Str(map.get("DEP_ID"));
            emp_id = StringUtil.obj2Str(map.get("EMP_ID"));
            if (model.getContainDepartment() || model.getContainChild() || model.getOrgId().equalsIgnoreCase(organ)) {
                if (!model.getContainDepartment() && model.getContainChild()) {
                    organ = "DEPARTMENT";
                }

                List<String> list = listMap.containsKey(organ) ? listMap.get(organ) : new ArrayList<>();
                if (!list.contains(emp_id)) {
                    list.add(emp_id);
                }
                listMap.put(organ, list);
            }
        }
        if (!model.getContainDepartment()) {
            return listMap;
        }

        //查询单位树
        Map<String, List> result = new LinkedHashMap<>();
        //查询单位树
        Map<String, String> treeIds = addTreeChildren(model.getOrgId(), model.getContainChild(), false);
        //遍历单位树
        for (Map.Entry<String, String> entry : treeIds.entrySet()) {
            String aId = entry.getKey();
            String aName = entry.getValue();
            if (listMap.containsKey(aId)) {
                List<Map<Integer, String>> aList = listMap.get(aId);
                String deptName = aName == null ? "" : aName;
                result.put(deptName, aList);
            }
        }


        // 过滤掉某些机构下没有干部的数据 上面已经判断了 所以这一段没有必要
//        for (Map.Entry<String, List> entry : result.entrySet()) {
//            if (entry.getValue().isEmpty()) {
//                result.remove(entry.getKey());
//            }
//        }

        return result;
    }

    private Map<String, List> getDutyEmpIdsByTreeId(ExportGbhmcModel model) {
        String codeCondition1 = "";
        String codeCondition2 = "";
        if (model.getContainChild()) {
            String code = "";
            String sql = "select TREE_LEVEL_CODE from DEP_B001 where dep_id='" + model.getOrgId() + "'";
            List<Map<String, Object>> codeList = exportService.findBySQL(sql);
            if (codeList != null && codeList.size() > 0) {
                code = (String) codeList.get(0).get("TREE_LEVEL_CODE");
            }
            codeCondition1 = " and b02.TREE_LEVEL_CODE like '" + code + "%' ";
            codeCondition2 = " and b03.TREE_LEVEL_CODE like '" + code + "%' ";
        } else {
            codeCondition1 = " and b02.dep_id='" + model.getOrgId() + "' ";
            codeCondition2 = " and b03.dep_id='" + model.getOrgId() + "' ";
        }
        String sql = "select t1.emp_id,b01.dep_id,a1.A02001_Bs " + INCLUD_CHILD_PRESON_FROM.replace(" dep_b001.tree_level_code like :code ", " dep_b001.tree_level_code like '001%' ").replace("wm_concat(a2.A05002_B) A05002_Bs,", "wm_concat(a2.A05002_B) A05002_Bs,wm_concat(a2.A02001_B) A02001_Bs,").replace("select emp_a02.emp_id,", "select emp_a02.emp_id,emp_a02.A02001_B,") +
                ", DEP_B001 b01 where t1.status=0 and t1.A01063 = '1' and t1.a001004_a=b01.dep_id " +
                "and t1.emp_id in (select t2.emp_id from EMP_A001 t2 left join DEP_B001 b02 on t2.a001004_a= b02.dep_id where t2.status=0 " + codeCondition1 +
                " union all select a02.emp_id from EMP_A02 a02 left join DEP_B001 b03 on a02.A02001_B=b03.dep_id where a02.status=0 and a02.A02055='2' " + codeCondition2 + ") " +
                INCLUD_CHILD_PRESON_ORDER + "t1.ordinal";
        List<Map<String, Object>> res = exportService.findBySQL(sql);
        //职集单位
        Map<String, List> listMap = new LinkedHashMap<>();
        if (res != null && res.size() > 0) {
            String organ = null;
            String emp_id = null;
            for (Map<String, Object> map : res) {
                //按单位
                organ = StringUtil.obj2Str(map.get("DEP_ID"));
                emp_id = StringUtil.obj2Str(map.get("EMP_ID"));
                List list = listMap.containsKey(organ) ? listMap.get(organ) : new ArrayList<>();
                if (!list.contains(emp_id)) {
                    list.add(emp_id);
                }
                listMap.put(organ, list);
                //职集单位
                String A02001_BS = (String) map.get("A02001_BS");
                if (A02001_BS != null) {
                    for (String aDept : A02001_BS.split(",")) {
                        if (aDept != null && !aDept.equals(organ)) {
                            List list2 = listMap.containsKey(aDept) ? listMap.get(aDept) : new ArrayList<>();
                            if (!list2.contains(emp_id)) {
                                list2.add(emp_id);
                            }
                            listMap.put(aDept, list2);
                        }
                    }
                }
            }
            // 过滤掉某些机构下没有干部的数据 上面已经判断了 所以这一段没有必要
//            for (Map.Entry<String, List> entry : listMap.entrySet()) {
//                if (entry.getValue().isEmpty()) {
//                    listMap.remove(entry.getKey());
//                }
//            }
        }
        return listMap;
    }

    private Map<String, String> addTreeChildren(String treeId, boolean tree, boolean treeName) {
        Map<String, String> depIdNames = new LinkedHashMap<>();
        String rootName = "";
        String rootCode = "";
        String sql = "select B001001_B,TREE_LEVEL_CODE from DEP_B001 where DEP_ID='" + treeId + "'";
        List<Map<String, Object>> rootNameList = exportService.findBySQL(sql);
        if (rootNameList != null && rootNameList.size() > 0) {
            rootName = (String) rootNameList.get(0).get("B001001_B");
            rootCode = (String) rootNameList.get(0).get("TREE_LEVEL_CODE");
        }
        if (!tree) {
            depIdNames.put(treeId, rootName);
            return depIdNames;
        }
        if (rootCode != null) {
            sql = "select DEP_ID,TREE_LEVEL_CODE,PPTR,ORDINAL,B001001_B,B01094 from DEP_B001 where status=0 and TREE_LEVEL_CODE like '" + rootCode + "%' order by pptr,ordinal";
            List<Map<String, Object>> depList = exportService.findBySQL(sql);
            Map<String, String[]> tempMap = new HashMap<>();
            for (Map<String, Object> aMap : depList) {
                String codeKey = (String) aMap.get("TREE_LEVEL_CODE");
                String ordinal = aMap.get("ORDINAL") == null ? "0" : aMap.get("ORDINAL").toString();
                String pptr = (String) aMap.get("PPTR");
                String B001001_B = (String) aMap.get("B001001_B");
                String B01094 = (String) aMap.get("B01094");
                if (codeKey != null) {
                    tempMap.put(codeKey, new String[]{ordinal, pptr, B001001_B, B01094});
                }
            }
            Map<String, String[]> valueMap = new HashMap<>();
            Set<String> codeSet = tempMap.keySet();
            for (Map.Entry<String, String[]> entry : tempMap.entrySet()) {
                String[] value = entry.getValue();
                if (codeSet.contains(value == null ? null : value[1]) || rootCode.equalsIgnoreCase(entry.getKey())) {
                    valueMap.put(entry.getKey(), entry.getValue());
                }
            }
            List<Map<String, Object>> newDepList = new ArrayList<>();
            LOOP_TOP:
            for (Map<String, Object> aMap : depList) {
                String code = (String) aMap.get("TREE_LEVEL_CODE");
                String key = code;
                StringBuilder name = new StringBuilder(aMap.get("B001001_B") == null ? "" : (String) aMap.get("B001001_B"));
                boolean top = "1".equals(aMap.get("B01094"));
                String[] valueArray = valueMap.get(key);
                for (int i = 0; i < 10; i++) {
                    if (rootCode.equals(key)) {
                        break;
                    }
                    String upKey = key;
                    if (valueArray == null) {
                        continue LOOP_TOP;
                    }
                    String ordinal = valueArray[0];
                    key = valueArray[1];
                    if (ordinal == null || key == null || key.equals(upKey)) {
                        continue LOOP_TOP;
                    }
                    int pos = code.lastIndexOf(".");
                    if (pos < 0) {
                        break;
                    }
                    ordinal = "0000000000".substring(ordinal.length()) + ordinal;
                    code = key + "*" + ordinal + "-" + code.substring(pos + 1);
                    valueArray = valueMap.get(key);
                    if (!top && treeName) {
                        name.insert(0, (valueArray[2] == null ? "" : valueArray[2]) + " - ");
                    }
                    if ("1".equals(valueArray[3])) {
                        top = true;
                    }
                }
                aMap.put("ORDER_KEY", code);
                aMap.put("TREE_NAME", name.toString());
                newDepList.add(aMap);
            }
            newDepList.sort(Comparator.comparing(map -> (String) map.get("ORDER_KEY")));
            for (Map<String, Object> aMap : newDepList) {
                String newDepId = (String) aMap.get("DEP_ID");
                String newName = (String) aMap.get("TREE_NAME");
                depIdNames.put(newDepId, newName);
            }
        }
        return depIdNames;
    }

    private List<Map<String, Object>> getBatchList(List<String> idStrList, String sql) {
        if (idStrList == null) {
            return new ArrayList<>();
        }
        String key = ":ids";
        String allSql = sql;
        int cnt = (allSql.length() - allSql.replace(key, "").length()) / key.length();
        for (int i = 0; i < cnt; i++) {
            String field = allSql.substring(0, allSql.indexOf(key)).trim();
            field = field.substring(0, field.toLowerCase().lastIndexOf("in")).trim();
            int pos1 = Math.max(field.lastIndexOf(" "), field.lastIndexOf("(")) + 1;
            field = field.substring(pos1).trim();
            int pos2 = allSql.indexOf(key) + key.length();
            String ids = "";
            for (String idStr : idStrList) {
                ids += "".equals(ids) ? "(" + field + " in (" + idStr + ")" : " or " + field + " in (" + idStr + ")";
            }
            ids += ")";
            allSql = allSql.substring(0, pos1) + ids + allSql.substring(pos2);
        }
        return exportService.findBySQL(allSql);
    }

    private List<List<String>>[] getIdBatchLists(List<String> ids) {
        int batchCnt = 10000;
        int batchBase = 1000;
        List<List<String>> batchIds = new ArrayList<>();
        List<List<String>> batchIdStr = new ArrayList<>();
        List<String> aIdStrList = new ArrayList<>();
        List<String> newIds = new ArrayList<>();
        String newIdStr = "";
        int thisNum = 0;//List内序号
        int thisBase = 0;//batch内序号
        int batchNum = 0;//总List个数
        int baseNum = 0;//batch List个数
        int totalNum = 0;//总序号
        int totalBase = 0;//List内batch序号
        while (true) {
            if (thisBase >= batchBase || totalNum >= ids.size() || totalBase >= batchCnt) {
                aIdStrList.add("'" + newIdStr + "'");
                thisBase = 0;
                newIdStr = "";
                baseNum++;
            }
            if (thisNum >= batchCnt || totalNum >= ids.size()) {
                batchIds.add(newIds);
                batchIdStr.add(aIdStrList);
                thisNum = 0;
                thisBase = 0;
                baseNum = 0;
                newIds = new ArrayList<>();
                aIdStrList = new ArrayList<>();
                newIdStr = "";
                batchNum++;
            }
            totalNum = batchNum * batchCnt + thisNum;
            if (totalNum >= ids.size()) {
                break;
            }
            String aId = ids.get(totalNum);
            newIds.add(aId);
            newIdStr += "".equals(newIdStr) ? aId : "','" + aId;
            thisNum++;
            thisBase++;
            totalNum = batchNum * batchCnt + thisNum;
            totalBase = baseNum * batchBase + thisBase;
        }
        return new List[]{batchIds, batchIdStr};
    }


    private String partyDateString(List partyDateInfoList) {
        if (partyDateInfoList != null && partyDateInfoList.size() > 0) {
            if (partyDateInfoList.size() == 1) {
                Map map = (Map) partyDateInfoList.get(0);
                Object partyDateObj = map.get("A58005");
                String partyName = StringUtil.obj2Str(map.get("A58001_A"));
                String partyCode = StringUtil.obj2Str(map.get("A58001_B"));
                if (org.apache.commons.lang.StringUtils.equals(partyCode, "01")) {
                    if (partyDateObj != null) {
                        return new DateTime(partyDateObj).toString("yyyy.MM");
                    }
                }
            } else {
                String allNames = "";
                String partyNames = "";
                // 如果假如中国共产党又加入其他党派则需要把时间显示到最前面
                StringBuilder partyBuilder = new StringBuilder();
                String partyDate = "";
                for (Object o : partyDateInfoList) {
                    Map map = (Map) o;
                    Object partyDateObj = map.get("A58005");
                    String partyName = StringUtil.obj2Str(map.get("A58001_A"));
                    String partyCode = StringUtil.obj2Str(map.get("A58001_B"));
                    //入党时间
                    if (org.apache.commons.lang.StringUtils.equals(partyCode, "01")) {
                        if (partyDateObj != null) {
                            partyDate = new DateTime(partyDateObj).toString("yyyy.MM");
                        } else {
                            partyDate = partyName;
                        }
                    }
                    if (!"01".equals(partyCode) || StringUtils.isEmpty(partyDate)) {
                        //其他党派
                        partyNames += "".equals(partyNames) ? partyName : ";\n" + partyName;
                    }
                }
                if (partyDate != null && !"".equals(partyDate) && !"".equals(partyNames)) {
                    allNames = partyDate + ";\n" + partyNames;
                } else if (partyDate != null && !"".equals(partyDate)) {
                    allNames = partyDate;
                } else if (!"".equals(partyNames)) {
                    allNames = partyNames;
                }
                return allNames;
            }
        }
        return "";
    }

    private String disposeString(Object target) {
        return target == null ? "" : target.toString().trim();
    }

    /**
     * 生成花名册excel文件
     */
    private String generateExcel(Map<String, List<Map<Integer, String>>> data, ExportGbhmcModel model) {
        String templateFile = ExportConstant.EXPORT_BASE_PATH + ExportConstant.TEMPLATE_GBHMC;
        String excelPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_EXCEL_GBHMC;
        String outFile = excelPath + "花名册_" + System.currentTimeMillis() + ".xlsx";

        FileUtils.createDir(excelPath);

        final int PAGE_DEP_SIZE = 5000;
        SExcelWriter excelWriter = new SExcelWriter(new File(outFile), new File(templateFile), 3);
        Workbook workbook = excelWriter.getWorkbook();
        CellStyle cellStyleOrg = workbook.createCellStyle();
        cellStyleOrg.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyleOrg.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyleOrg.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyleOrg.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//				cellStyleOrg.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyleOrg.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);//垂直居左
        cellStyleOrg.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居左
        //生成一个字体
        Font font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);//HSSFColor.VIOLET.index //字体颜色
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);         //字体增粗
        cellStyleOrg.setFont(font);
        CellStyle cellStyleSub = workbook.createCellStyle();
        cellStyleSub.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        cellStyleSub.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        cellStyleSub.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        cellStyleSub.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//				cellStyleSub.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        cellStyleSub.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);//垂直居左
        int i = 2;
        int n = 0;
        boolean newPage = false;
        for (Map.Entry<String, List<Map<Integer, String>>> entry : data.entrySet()) {
            n++;
            // shixianggui-20180314, 任务350: 导出花名册增加: 干部花名册(不含单位) 按钮
            if (model.getContainDepartment() || model.getContainDutyDepart()) {

                int rowNum = ++i;
                //机构名称行， 合并单元格
                Sheet sheet = excelWriter.getWritableSheet();
                CellRangeAddress cra = new CellRangeAddress(rowNum, rowNum, 0, 17);
                sheet.addMergedRegion(cra);

                excelWriter.writeData(rowNum, 0, entry.getKey(), ExcelType.STRING, cellStyleOrg, true);
            }
            for (Map<Integer, String> value : entry.getValue()) {
                ++i;
                Set<Integer> column = value.keySet();
                for (Integer integer : column) {
                    String str = value.get(integer);
                    if ("".equals(str) || str.isEmpty()) {
                        str = " ";
                    }
                    if (integer == 0 || integer == 3 || integer == 4 || integer == 7 ||
                            integer == 8 || integer == 9 || integer == 15 || integer == 16) {
//								cellStyleSub.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
                        cellStyleSub.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居中
                        excelWriter.writeData(i, integer, str, ExcelType.STRING, cellStyleSub, false);
                    } else {
                        cellStyleSub.setWrapText(true);//自动换行
                        excelWriter.writeData(i, integer, str, ExcelType.STRING, cellStyleSub, false);
                    }
                }
                if (i > 1 && i % 100 == 0) {
                    excelWriter.flushRows();
                }
                if (i > 1 && i % PAGE_DEP_SIZE == 0 && n < data.size()) {
                    newPage = true;
                }
            }
            if (newPage) {
                excelWriter.newPage();
                i = 2;
                newPage = false;
            }
        }
        excelWriter.close();

        return outFile;
    }
}
