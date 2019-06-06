package com.sinosoft.ops.cimp.export.handlers.impl;

import com.sinosoft.ops.cimp.common.excel.ExcelType;
import com.sinosoft.ops.cimp.common.excel.SExcelWriter;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.handlers.AbstractExportWithPoi;
import com.sinosoft.ops.cimp.util.FileUtils;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ExportDwldzs extends AbstractExportWithPoi {

    private String filePath;

    private HttpServletRequest request;

    public ExportDwldzs(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 生成文件
     */
    @Override
    public boolean generate() throws Exception {
        generateExcel(request);
        return true;
    }

    /**
     * 获取生成的文件路径
     */
    @Override
    public String getFilePath() throws Exception {
        if (filePath == null) {
            String fileDir = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_DWLDZS_EXCEL;
            FileUtils.createDir(fileDir);
            filePath = fileDir + "单位领导职数_" + System.currentTimeMillis() + ExportConstant.SUFFIX_XLSX;
        }

        return filePath;
    }

    @Override
    public boolean isReuse() {
        return false;
    }



    @Transactional(readOnly = true)
    public List<Map<Integer, String>> LeadersSet(String[] treeId, int type, String conditions) {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
        List<String> depIds = getChildDepIds(type, treeId, conditions);
        List<List<String>>[] lists = getIdBatchLists(depIds);
        List<List<String>> batchDepIds = lists[0];
        List<List<String>> batchDepIdStr = lists[1];
        Integer[] sumArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int index = 0;
        for (int a = 0; a < batchDepIds.size(); a++) {
            List<String> depIdList = batchDepIds.get(a);
            List<String> depIdStrList = batchDepIdStr.get(a);
            String sql = "select B01001 unitName,(select name from SYS_CODE_ITEM where code_set_id=166 and code=B01027) unitLevel,(select name from SYS_CODE_ITEM where code_set_id=86 and code=B01031) unitType," +
                    "(CASE WHEN B01031 like '4%' and B01031 not like '41%' THEN B02031 WHEN B01031 like '41%' THEN B02032 ELSE (CASE SUBSTR(B01031, 1, 1) WHEN '1' THEN B02027 ELSE B02024 END) END) unitSum," +
                    "B02041 leaderSum,num unitReal,(select count(1) from EMP_A001 where A001004_A=dep_b001.dep_id and status=0) A001004_A,dep_b001.dep_id from dep_b001 " +
                    "left join DEP_B02 on dep_b001.dep_id=DEP_B02.dep_id and DEP_B02.status=0 " +
                    "left join (select A02001_B,count(1) num from emp_a02 where status=0 and A02055='2' group by A02001_B)  emp_a02 on dep_b001.dep_id=emp_a02.A02001_B " +
                    "where dep_b001.dep_id in :ids and dep_b001.status=0";
            List<Map<String, Object>> unitBatchList = getBatchList(depIdStrList, sql);
            sql = "select dep_id,B09002_B,B09002_A,sum(A02066) leaderReal,max(B09011) cnt,wm_concat(A01001) A01001,wm_concat(emp_id) EMP_IDS " +
                    "from (select distinct dep_b09.dep_id,B09002_B,B09002_A,(case when emp_a02.emp_id is not null and (A02066 is null or A02066 != '0') then 1 else 0 end) A02066,B09011,emp_a001.A01001,emp_a001.emp_id,dep_b09.ordinal from dep_b09 " +
                    "left join emp_a02 on dep_b09.dep_id=A02001_B and A02016_B=B09002_B and emp_a02.status=0 and (A02055='2' or A02055 is null) and (emp_a02.A02049 not like '4%' or emp_a02.A02049 is null) " +
                    "left join emp_a001 on emp_a02.emp_id=emp_a001.emp_id and emp_a001.status=0 " +
                    "where dep_b09.status=0 and dep_b09.dep_id in :ids " +
                    "and DEP_B09.B09004 like '1%') " +
                    "group by dep_id,B09002_B,B09002_A,ordinal " +
                    "order by ordinal";
            List<Map<String, Object>> leaderBatchList = getBatchList(depIdStrList, sql);
            sql = "select (CASE MAX(A02016_C) WHEN '' THEN A02016_A ELSE NVL(MAX(A02016_C), A02016_A) END) A02016_C,count(emp_a001.A01001) leaderReal,wm_concat(emp_a001.A01001) A01001,wm_concat(emp_a001.emp_id) EMP_IDS,'' A02016_A,emp_a02.A02001_B dep_id from emp_a02 " +
                    "left join emp_a001 on emp_a02.emp_id=emp_a001.emp_id and emp_a001.status=0 " +
                    "where emp_a02.status=0  and (emp_a02.A02055='2' or emp_a02.A02055 is null) and emp_a02.A02001_B in :ids " +
                    "and emp_a02.A02008 like '2%' " +
                    "and (emp_a02.A02049 not like '4%' or emp_a02.A02049 is null) " +
                    "group by emp_a02.A02016_A,emp_a02.ordinal,emp_a02.A02001_B " +
                    "order by emp_a02.ordinal";
            List<Map<String, Object>> noleaderBatchList = getBatchList(depIdStrList, sql);
            sql = "select EMP_ID,A01001,A02001_B DEP_ID from (select a1.*, a2.A02025, a2.A02001_B from EMP_A001 a1 left join (select emp_id emp_id, A02001_B, MIN(NVL(A02025, 999)) A02025 from EMP_A02 where status=0 and A02055 = '2' group by EMP_ID,A02001_B) a2 on a1.emp_id = a2.emp_id where a2.A02001_B in :ids) t1 order by A02025, t1.ORDINAL";
            List<Map<String, Object>> nameBatchList = getBatchList(depIdStrList, sql);
            for (String DEP_ID: depIdList) {
                List<Map<String, Object>> unitList = unitBatchList.stream().filter(item -> item.get("DEP_ID").equals(DEP_ID)).collect(Collectors.toList());
                List<Map<String, Object>> leaderList = leaderBatchList.stream().filter(item -> item.get("DEP_ID").equals(DEP_ID)).collect(Collectors.toList());
                List<Map<String, Object>> noleaderList = noleaderBatchList.stream().filter(item -> item.get("DEP_ID").equals(DEP_ID)).collect(Collectors.toList());
                List<Map<String, Object>> nameList = nameBatchList.stream().filter(item -> item.get("DEP_ID").equals(DEP_ID)).collect(Collectors.toList());
                Map<Integer, String> dataMap = new HashMap<Integer, String>();
                dataMap.put(0, String.valueOf(++index));
                String unitName = "";
                String unitLevel = "";
                String unitType = "";
                String unitSum = "0";
                String leaderSum = "0";
                String unitReal = "0";
                if (unitList != null && unitList.size() > 0) {
                    Map<String, Object> unitMap = unitList.get(0);
                    unitName = (String)unitMap.get("UNITNAME");//单位名称
                    unitLevel = (String)unitMap.get("UNITLEVEL");//机构规格
                    unitType = (String)unitMap.get("UNITTYPE");//机构性质
                    unitSum = unitMap.get("UNITSUM") == null ? "" : ((BigDecimal)unitMap.get("UNITSUM")).toString();//批准编制总数
                    leaderSum = unitMap.get("LEADERSUM") == null ? "" : ((BigDecimal)unitMap.get("LEADERSUM")).toString();//单位领导职数
                    //unitReal = unitMap.get("UNITREAL") == null ? "" : ((BigDecimal)unitMap.get("UNITREAL")).toString();//编制实配
                    unitReal = unitMap.get("A001004_A") == null ? "" : ((BigDecimal)unitMap.get("A001004_A")).toString();
                }
                if (unitType != null && (unitType.contains("党委班子") || unitType.contains("政府班子") || unitType.contains("人大班子") ||
                        unitType.contains("政协班子") || unitType.contains("纪检班子") || unitType.contains("法检班子") || unitType.contains("法院班子") ||
                        unitType.contains("检察院班子") || unitType.contains("纪委班子")) && (unitType.contains("（") || unitType.contains("("))){
                    if (unitType.contains("（")) {
                        unitType = unitType.substring(0, unitType.indexOf("（"));
                    } else if (unitType.contains("(")) {
                        unitType = unitType.substring(0, unitType.indexOf("("));
                    }
                }
                dataMap.put(1, unitName);
                dataMap.put(2, unitLevel);
                dataMap.put(3, unitType);
                dataMap.put(4, unitSum);
                dataMap.put(5, unitReal);
                int total = toInt(unitSum);
                int real = toInt(unitReal);
                dataMap.put(6, "".equals(unitSum) || "".equals(unitReal) || total - real == 0 ? "" : String.valueOf(total - real));
                sumArray[0] += total;
                sumArray[1] += real;
                if (!("".equals(unitSum) || "".equals(unitReal))) {
                    sumArray[2] += total - real;
                }
                dataMap.put(7, leaderSum);
                total = toInt(leaderSum);
                sumArray[3] += total;
                int leaderCnt = 0;
                if (leaderList == null) {
                    leaderList = new ArrayList<Map<String, Object>>();
                }
                if (leaderList != null && leaderList.size() > 0) {
                    leaderCnt = leaderList.size();
                }
                int noleaderCnt = 0;
                if (noleaderList == null) {
                    noleaderList = new ArrayList<Map<String, Object>>();
                }
                if (noleaderList != null && noleaderList.size() > 0) {
                    noleaderCnt = noleaderList.size();
                }
                int leaderMax = Math.max(leaderCnt, noleaderCnt);
                for (int n = 0; n < leaderMax; n++) {
                    Map<Integer, String> dataNewMap = new HashMap<Integer, String>();
                    for (Map.Entry<Integer, String> entry : dataMap.entrySet()) {
                        dataNewMap.put(entry.getKey(), entry.getValue());
                    }
                    if (n > 0) {
                        for (int m = 0; m <= 9; m++) {
                            dataNewMap.put(m, "");
                        }
                    }
                    if (n < leaderList.size()) {
                        Map<String, Object> leaderMap = leaderList.get(n);
                        String B09002_B = (String)leaderMap.get("B09002_B");
                        String B09002_A = (String)leaderMap.get("B09002_A");
                        String leaderReal = leaderMap.get("LEADERREAL") == null ? "" : ((BigDecimal)leaderMap.get("LEADERREAL")).toString();//领导配职数量
                        String leaderTotal = leaderMap.get("CNT") == null ? "" : ((BigDecimal)leaderMap.get("CNT")).toString();
                        //String leaderName = leaderMap.get("A01001")==null?"":leaderMap.get("A01001").toString();
                        String leaderTitle = leaderMap.get("A02016_A")==null?"":leaderMap.get("A02016_A").toString();
                        String EMP_IDS = leaderMap.get("EMP_IDS")==null?"":leaderMap.get("EMP_IDS").toString();
                        String leaderName = "";
                        if (EMP_IDS != null && !"".equals(EMP_IDS)) {
                            if (nameList != null) {
                                EMP_IDS = EMP_IDS.toUpperCase();
                                for (Map<String, Object> map : nameList) {
                                    String EMP_ID = (String)map.get("EMP_ID") == null ? "***" : ((String)map.get("EMP_ID")).toUpperCase();
                                    if (EMP_IDS.contains(EMP_ID)) {
                                        String name = (String)map.get("A01001");
                                        leaderName += "".equals(leaderName) ? name : "、" + name;
                                    }
                                }
                            }
                        }
                        dataNewMap.put(10, B09002_A);
                        dataNewMap.put(11, leaderTotal);
                        dataNewMap.put(12, leaderReal);
                        total = toInt(leaderTotal);
                        real = toInt(leaderReal);
                        dataNewMap.put(13,"".equals(leaderTotal) || "".equals(leaderReal)  || total - real == 0 ? "" : String.valueOf(total - real));
                        sumArray[6] += total;
                        sumArray[7] += real;
                        if (!("".equals(leaderTotal) || "".equals(leaderReal))) {
                            sumArray[8] += total - real;
                        }
                        dataNewMap.put(14, leaderName);
                        dataNewMap.put(15, leaderTitle);
                    }
                    if (n == 0) {
                        int leaderRealSum = 0;
                        String lessConditions = "";
                        for (int m = 0; m < leaderList.size(); m++) {
                            Map<String, Object> leaderMap = leaderList.get(m);
                            String leaderReal = leaderMap.get("LEADERREAL") == null ? "" : ((BigDecimal)leaderMap.get("LEADERREAL")).toString();
                            real = toInt(leaderReal);
                            leaderRealSum += real;
                            String leaderTotal = leaderMap.get("CNT") == null ? "" : ((BigDecimal)leaderMap.get("CNT")).toString();
                            total = toInt(leaderTotal);
                            int lessCnt = "".equals(leaderTotal) || "".equals(leaderReal)  || total - real == 0 ? 0 : total - real;
                            if (lessCnt > 0) {
                                String B09002_A = (String)leaderMap.get("B09002_A");
                                String aLess = B09002_A + lessCnt + "名";
                                lessConditions += "".equals(lessConditions) ? aLess : "、" + aLess;
                            }
                        }
                        dataNewMap.put(8, String.valueOf(leaderRealSum));
                        total = toInt(leaderSum);
                        dataNewMap.put(9, "".equals(leaderSum)  || total - leaderRealSum == 0 ? "" : String.valueOf(total - leaderRealSum));
                        sumArray[4] += leaderRealSum;
                        if (!"".equals(leaderSum)) {
                            sumArray[5] += total - leaderRealSum;
                        }
                        dataNewMap.put(16, "");
                    } else {
                        dataNewMap.put(8, "");
                        dataNewMap.put(9, "");
                        dataNewMap.put(16, "");
                    }
                    if (n >= leaderList.size()) {
                        for (int m = 10; m <= 16; m++) {
                            dataNewMap.put(m, "");
                        }
                    }
                    if (n < noleaderList.size()) {
                        Map<String, Object> noleaderMap = noleaderList.get(n);
                        String A02016_C = (String)noleaderMap.get("A02016_C");
                        String leaderReal = noleaderMap.get("LEADERREAL") == null ? "" : ((BigDecimal)noleaderMap.get("LEADERREAL")).toString();//领导实配数量
                        //String noleaderName = noleaderMap.get("A01001")==null?"":noleaderMap.get("A01001").toString();
                        String noleaderTitle =  noleaderMap.get("A02016_A")==null?"":noleaderMap.get("A02016_A").toString();
                        String EMP_IDS = noleaderMap.get("EMP_IDS")==null?"":noleaderMap.get("EMP_IDS").toString();
                        String noleaderName = "";
                        if (EMP_IDS != null && !"".equals(EMP_IDS)) {
                            if (nameList != null) {
                                EMP_IDS = EMP_IDS.toUpperCase();
                                for (Map<String, Object> map : nameList) {
                                    String EMP_ID = (String)map.get("EMP_ID") == null ? "***" : ((String)map.get("EMP_ID")).toUpperCase();
                                    if (EMP_IDS.contains(EMP_ID)) {
                                        String name = (String)map.get("A01001");
                                        noleaderName += "".equals(noleaderName) ? name : "、" + name;
                                    }
                                }
                            }
                        }
                        dataNewMap.put(17, A02016_C);
                        dataNewMap.put(18, leaderReal);
                        real = toInt(leaderReal);
                        sumArray[9] += real;
                        dataNewMap.put(19, noleaderName);
                        dataNewMap.put(20, "");
                    }
                    if (n >= noleaderList.size()) {
                        for (int m = 17; m <= 20; m++) {
                            dataNewMap.put(m, "");
                        }
                    }
                    list.add(dataNewMap);
                }
                if (leaderMax == 0) {
                    dataMap.put(8, "0");
                    total = toInt(leaderSum);
                    dataMap.put(9, total == 0 ? "" : leaderSum);
                    sumArray[5] += total;
                    for (int m = 10; m <= 20; m++) {
                        dataMap.put(m, "");
                    }
                    list.add(dataMap);
                }
            }
        }
        Map<Integer, String> sumMap = new HashMap<Integer, String>();
        sumMap.put(0, "　");
        sumMap.put(1, "总计");
        sumMap.put(2, "");
        sumMap.put(3, "");
        sumMap.put(4, String.valueOf(sumArray[0]));
        sumMap.put(5, String.valueOf(sumArray[1]));
        sumMap.put(6, String.valueOf(sumArray[2]));
        sumMap.put(7, String.valueOf(sumArray[3]));
        sumMap.put(8, String.valueOf(sumArray[4]));
        sumMap.put(9, String.valueOf(sumArray[5]));
        sumMap.put(10, "");
        sumMap.put(11, String.valueOf(sumArray[6]));
        sumMap.put(12, String.valueOf(sumArray[7]));
        sumMap.put(13, String.valueOf(sumArray[8]));
        sumMap.put(14, "");
        sumMap.put(15, "");
        sumMap.put(16, "");
        sumMap.put(17, "");
        sumMap.put(18, String.valueOf(sumArray[9]));
        sumMap.put(19, "");
        sumMap.put(20, "");
        //list.add(sumMap);
        return list;
    }

    List<String> getChildDepIds(int type, String[] treeId, String conditions) {
        String treeIds = "";
        if (treeId != null) {
            for (String aId : treeId) {
                treeIds += "".equals(treeIds) ? aId : "','" + aId;
            }
        }
        String sql = "select TREE_LEVEL_CODE from DEP_B001 where STATUS=0 and DEP_ID in ('" + treeIds + "') order by ordinal";
        List<Map<String, Object>> thisList = ExportConstant.exportService.findBySQL(sql);
        List<String> rootCodeList = new ArrayList<String>();
        String rootCodes = "";
        if (thisList != null && thisList.size() > 0) {
            for (Map<String, Object> thisMap : thisList) {
                String code = (String)thisMap.get("TREE_LEVEL_CODE");
                rootCodes += ("".equals(rootCodes)  ? "(" : " or ") + "TREE_LEVEL_CODE like '" + code + "%'";
                rootCodeList.add(code);
            }
        }
        rootCodes = "".equals(rootCodes) ? null : rootCodes + ")";
        List<String> depIds = new ArrayList<String>();
        if (rootCodes != null) {
            sql = "select DEP_ID,B01027,TREE_LEVEL_CODE,PPTR,ORDINAL from DEP_B001 where STATUS=0 and " + rootCodes + " order by pptr,ordinal";
            List<Map<String, Object>> depList = ExportConstant.exportService.findBySQL(sql);
            Map<String, String[]> tempMap = new HashMap<String, String[]>();
            for (Map<String, Object> aMap : depList) {
                String codeKey = (String)aMap.get("TREE_LEVEL_CODE");
                String ordinal = aMap.get("ORDINAL") == null ? "0" : ((BigDecimal)aMap.get("ORDINAL")).toString();
                String pptr = (String)aMap.get("PPTR");
                if (codeKey != null) {
                    tempMap.put(codeKey, new String[] {ordinal, pptr});
                }
            }
            Map<String, String[]> valueMap = new HashMap<String, String[]>();
            Set<String> codeSet = tempMap.keySet();
            for (Map.Entry<String, String[]> entry : tempMap.entrySet()) {
                String[] value = entry.getValue();
                if (codeSet.contains(value == null ? null : value[1]) || rootCodeList.contains(entry.getKey())) {
                    valueMap.put(entry.getKey(), entry.getValue());
                }
            }
            if (conditions != null && !"".equals(conditions)) {
                sql = "select DEP_ID,B01027,TREE_LEVEL_CODE from DEP_B001 where STATUS=0 and " + rootCodes + " " + conditions + " order by pptr,ordinal";
                depList = ExportConstant.exportService.findBySQL(sql);
            }
            List<Map<String, Object>> newDepList = new ArrayList<Map<String, Object>>();
            LOOP_TOP : for (Map<String, Object> aMap : depList) {
                String newB01027 = (String)aMap.get("B01027");
                if ((type == 1 && !("107".equals(newB01027) || "108".equals(newB01027))) ||
                        (type == 2 && !("106".equals(newB01027) || "109".equals(newB01027) || "110".equals(newB01027)))) {
                    continue LOOP_TOP;
                }
                String code = (String)aMap.get("TREE_LEVEL_CODE");
                String key = code;
                String[] valueArray = valueMap.get(key);
                for (int i = 0; i < 10; i++) {
                    if (rootCodeList.contains(key)) {
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
                }
                aMap.put("ORDER_KEY", code);
                newDepList.add(aMap);
            }
            Collections.sort(newDepList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> map0, Map<String, Object> map1) {
                    return ((String)map0.get("ORDER_KEY")).compareTo((String)map1.get("ORDER_KEY"));
                }
            });
            for (Map<String, Object> aMap : newDepList) {
                String newDepId = (String)aMap.get("DEP_ID");
                depIds.add(newDepId);
            }
        }
        return depIds;
    }


    private List<List<String>>[] getIdBatchLists(List<String> ids) {
        int batchCnt = 10000;
        int batchBase = 1000;
        List<List<String>> batchIds = new ArrayList<List<String>>();
        List<List<String>> batchIdStr = new ArrayList<List<String>>();
        List<String> aIdStrList = new ArrayList<String>();
        List<String> newIds = new ArrayList<String>();
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
                newIds = new ArrayList<String>();
                aIdStrList = new ArrayList<String>();
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
        return new List[] {batchIds, batchIdStr};
    }


    private List<Map<String, Object>> getBatchList(List<String> idStrList, String sql) {
        if (idStrList == null) {
            return new ArrayList<Map<String, Object>>();
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
            for (int j = 0; j < idStrList.size(); j++) {
                String idStr = idStrList.get(j);
                ids += "".equals(ids) ? "(" + field + " in (" + idStr + ")" : " or " + field + " in (" + idStr + ")";
            }
            ids += ")";
            allSql = allSql.substring(0, pos1) + ids + allSql.substring(pos2);
        }
        return ExportConstant.exportService.findBySQL(allSql);
    }

    private int toInt(String valueStr) {
        int value = 0;
        try {
            value = Integer.parseInt(valueStr);
        } catch (Exception e) {
        }
        return value;
    }



    private void generateExcel(HttpServletRequest request) throws Exception {

        try {

            final int PAGE_DEP_SIZE = 5000;
            String conditions = getConditionds(request);
            String [] treeId= StringUtils.split(request.getParameter("treeId"),",");

            int type = ParticularUtils.toNumber(request.getParameter("type"), 0);
            int lines = ParticularUtils.toNumber(request.getParameter("lines"), 0);
            List<Map<Integer, String>> list =LeadersSet(treeId,type,conditions);
            System.out.println(list.size());
            String templateFile = ExportConstant.EXPORT_BASE_PATH + ExportConstant.TEMPLATE_DWLDZS;
            String outFile = getFilePath();
            try {
                SExcelWriter excelWriter = new SExcelWriter(new File(outFile), new File(templateFile), 3);
                Workbook workbook = excelWriter.getWorkbook();
                CellStyle cellStyleSub = workbook.createCellStyle();
                cellStyleSub.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                cellStyleSub.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                cellStyleSub.setBorderTop(HSSFCellStyle.BORDER_THIN);
                cellStyleSub.setBorderRight(HSSFCellStyle.BORDER_THIN);
                cellStyleSub.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
                cellStyleSub.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                Font font = workbook.getFontAt(cellStyleSub.getFontIndex());
                font.setFontName("宋体");
                font.setFontHeightInPoints((short)10);
                cellStyleSub.setFont(font);
                int i = 2;
                int mergeStart = -1;
                int mergeEnd = -1;
                String preData = null;
                boolean merging = false;
                boolean newPage = false;
                boolean newOpen = false;
                Map<String, Object> styleMap = new HashMap<String, Object>();
                Integer[] sumArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                for (int n = 0; n < list.size(); n++) {
                    if (lines > 0 && n > lines) {
                        break;
                    }
                    ++i;
                    Map<Integer, String> value = list.get(n);
                    Set<Integer> column = value.keySet();
                    int sumIndex = 0;
                    for (Integer integer : column) {
                        String data = value.get(integer);
                        if (data == null || "".equals(data) || data.isEmpty()) {
                            data = " ";
                        }
                        ExcelType dateType = ExcelType.STRING;
                        if (integer == 4 || integer == 5 || integer == 6 || integer == 7 || integer == 8 ||
                                integer == 9 || integer == 11 || integer == 12 || integer == 13 || integer == 18) {
                            dateType = ExcelType.NUMBER;
                            sumArray = sumColums(sumIndex++, data, sumArray);
                        }
                        IndexedColors color = null;
                        if (integer == 6 || integer == 9 || integer == 13) {
                            int dataInt = 0;
                            try {
                                dataInt = Integer.parseInt(data);
                            } catch (Exception e) {
                            }
                            if (dataInt > 0) {
                                color = IndexedColors.YELLOW;
                            } else if (dataInt < 0) {
                                color = IndexedColors.RED;
                            }
                        }
                        boolean wrapText = false;
                        if (integer == 1 || integer == 2 || integer == 3 || integer == 14 || integer == 16 || integer == 19) {
                            wrapText = true;
                        }
                        short align = HSSFCellStyle.ALIGN_LEFT;
                        if (integer == 2 || integer == 3) {
                            align = HSSFCellStyle.ALIGN_CENTER;
                        }
                        boolean bold = false;
                        if (integer == 10 || integer == 17) {
                            bold = true;
                        }
                        excelWriter.writeColorData(i, integer, data, styleMap, dateType, cellStyleSub, color, wrapText, bold, align);
                        if (integer == 0 && (" ".equals(data) || "".equals(data)) && !(" ".equals(preData) || "".equals(preData))) {
                            mergeStart = i - 1;
                        }
                        if (integer == 0 && !(" ".equals(data) || "".equals(data)) && (" ".equals(preData) || "".equals(preData))) {
                            mergeEnd = i - 1;
                        }
                        if (integer == 0 && (" ".equals(data) || "".equals(data)) && n == list.size() - 1) {
                            mergeEnd = i;
                        }
                        if (mergeEnd > mergeStart) {
                            merger(excelWriter, mergeStart, mergeEnd);
                            mergeStart = -1;
                            mergeEnd = -1;
                        }
                        if (integer == 0) {
                            preData = data;
                        }
                    }
                    excelWriter.writeRowHeight(i, 22, 8, styleMap);
                    if (n < list.size() - 1) {
                        String nextData = list.get(n + 1).get(0);
                        if (" ".equals(nextData) || "".equals(nextData)) {
                            merging = true;
                        } else {
                            merging = false;
                        }
                    }
                    int index = 0;
                    try {
                        index = Integer.parseInt(value.get(0));
                    } catch (Exception e) {
                    }
                    if (index > 1 && index % 100 == 0) {
                        excelWriter.flushRows();
                    }
                    if (index > 1 && index % PAGE_DEP_SIZE == 0 && n < list.size() - 1) {
                        newPage = true;
                    }
                    if (!merging && newOpen) {
                        if (mergeStart > 0) {
                            mergeEnd = i;
                            merger(excelWriter, mergeStart, mergeEnd);
                        }
                        excelWriter.reOpen(new File(outFile), new File(templateFile));
                        i = 2;
                        merging = false;
                        newPage = false;
                        newOpen = false;
                        preData = null;
                        mergeStart = -1;
                        mergeEnd = -1;
                    } else if (!merging && newPage) {
                        if (mergeStart > 0) {
                            mergeEnd = i;
                            merger(excelWriter, mergeStart, mergeEnd);
                        }
                        excelWriter.newPage();
                        i = 2;
                        merging = false;
                        newPage = false;
                        preData = null;
                        mergeStart = -1;
                        mergeEnd = -1;
                    }
                }
                writeSum(++i, sumArray, excelWriter, styleMap, cellStyleSub);
                excelWriter.writeRowHeight(i, 22, 8, styleMap);
                excelWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String getConditionds(HttpServletRequest request) {
        String[][] params = {{"danWeiJiBie", "B01027"}, {"guanLiQuanXian", ""}, {"liShuDanWei", "B01044_B"},
                {"danWeiXingZhi", "B01031"}, {"danWeiBianZhiXingZhi", "B02021"}};
        String conditions = "";
        for (String[] paramArray : params) {

            String[]  param= StringUtils.split(request.getParameter(paramArray[0]),",");

            if (param != null && param.length > 0) {
                String yinHao = paramArray.length == 2 ? "'" : "";
                String con = "";
                for (String value : param) {
                    con += "".equals(con) ? yinHao + value + yinHao : "," + yinHao + value + yinHao;
                }
                if (paramArray[1].startsWith("B01")) {
                    conditions += " and " + paramArray[1] + " in (" + con + ")";
                } else if (paramArray[1].startsWith("B02")) {
                    conditions += " and DEP_ID in (select DEP_ID from DEP_B02 where STATUS=0 and " + paramArray[1] + " in (" + con + "))";
                }
            }
        }
        return conditions;
    }


    private Integer[] sumColums(int sumIndex, String data, Integer[] sumArray) {
        int dataInt = 0;
        try {
            dataInt = Integer.parseInt(data);
        } catch (Exception e) {
        }
        sumArray[sumIndex] += dataInt;
        return sumArray;
    }

    private void merger(SExcelWriter excelWriter, int startRow, int endRow) {
        if (startRow >= 0 && endRow > startRow) {
            for (int m = 0; m <= 9; m++) {
                excelWriter.merge(startRow, endRow, m, m);
            }
            excelWriter.merge(startRow, endRow, 16, 16);
        }
    }

    private void writeSum(int index, Integer[] sumArray,SExcelWriter excelWriter, Map<String, Object> styleMap, CellStyle cellStyleSub) {
        excelWriter.writeColorData(index, 0, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 1, "总计", styleMap, ExcelType.STRING, cellStyleSub, null, false, true, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 2, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 3, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 4, String.valueOf(sumArray[0]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 5, String.valueOf(sumArray[1]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 6, String.valueOf(sumArray[2]), styleMap, ExcelType.NUMBER, cellStyleSub, (sumArray[2] > 0 ? IndexedColors.YELLOW : (sumArray[2] < 0 ? IndexedColors.RED : null)), false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 7, String.valueOf(sumArray[3]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 8, String.valueOf(sumArray[4]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 9, String.valueOf(sumArray[5]), styleMap, ExcelType.NUMBER, cellStyleSub, (sumArray[5] > 0 ? IndexedColors.YELLOW : (sumArray[5] < 0 ? IndexedColors.RED : null)), false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 10, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 11, String.valueOf(sumArray[6]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 12, String.valueOf(sumArray[7]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 13, String.valueOf(sumArray[8]), styleMap, ExcelType.NUMBER, cellStyleSub, (sumArray[8] > 0 ? IndexedColors.YELLOW : (sumArray[8] < 0 ? IndexedColors.RED : null)), false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 14, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 15, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 16, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 17, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 18, String.valueOf(sumArray[9]), styleMap, ExcelType.NUMBER, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 19, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
        excelWriter.writeColorData(index, 20, "", styleMap, ExcelType.STRING, cellStyleSub, null, false, false, HSSFCellStyle.ALIGN_LEFT);
    }
}
