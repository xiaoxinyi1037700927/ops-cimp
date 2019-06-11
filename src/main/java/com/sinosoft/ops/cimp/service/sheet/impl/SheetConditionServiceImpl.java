package com.sinosoft.ops.cimp.service.sheet.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.common.model.*;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionItemDao;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignConditionService;
import com.sinosoft.ops.cimp.util.ParticularUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @description: 函数实现
 */
@Service("sheetConditionService")
public class SheetConditionServiceImpl extends BaseEntityServiceImpl<SheetCondition> implements SheetConditionService {

    @Autowired
    private SheetConditionDao sheetConditionDao;
    @Autowired
    private SheetConditionItemDao sheetConditionItemDao;

    @Autowired
    private SheetDesignConditionService sheetDesignConditionService;

    @Autowired
    private SysInfoItemService sysInfoItemService;

    @Autowired
    private SysInfoSetService sysInfoSetService;

    private static String strNumList = "0123456789";

    private static String strLogicList = "()+*";

    @Transactional
    @Override
    public List<SheetCondition> findAll() {
        return sheetConditionDao.findAll();
    }

    @Transactional
    @Override
    public List<SheetCondition> GetDataByDataSourceID(String categoryId) {
        return sheetConditionDao.GetDataByDataSourceID(categoryId);
    }

    @Transactional
    @Override
    public List<SheetCondition> getConditionByCategoryId(String categoryId) {
        return sheetConditionDao.getConditionByCategoryId(categoryId);
    }

    @Transactional
    @Override
    public List<SheetCondition> getConditionByDesignId(String designId) {
        return sheetConditionDao.getConditionByDesignId(designId);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        sheetConditionDao.deleteById(UUID.fromString(id));
        for (SheetConditionItem sheetConditionItem : sheetConditionItemDao.GetDataByConditionID(UUID.fromString(id))) {
            sheetConditionItemDao.delete(sheetConditionItem);
        }
        return;
    }

    @Transactional
    @Override
    public SheetCondition GetConditionDataById(String id) {
        return sheetConditionDao.GetConditionDataById(id);
    }

    @Transactional
    @Override
    public void save(SheetCondition sysconditon) {
        sheetConditionDao.saveOrUpdate(sysconditon);
    }

    @Transactional
    @Override
    public void save(SheetCondition sysconditon, List<SheetConditionItem> SheetConditionItems) {
        sheetConditionDao.saveOrUpdate(sysconditon);
        for (SheetConditionItem SheetConditionItem : SheetConditionItems) {
            sheetConditionItemDao.saveOrUpdate(SheetConditionItem);
        }
    }

    @Transactional
    @Override
    public PageableQueryResult findByPage(PageableQueryParameter queryParameter) {
        return sheetConditionDao.findByPage(queryParameter);
    }

    @Transactional
    @Override
    public void setSqlTables(SheetCondition entity) {
        String sql = entity.getSql();
        String[] temps = sql.split("\\.|\\s+|,");
        String strTables = "";

        if (temps != null) {
            for (String temp : temps) {
                SysInfoSet sysInfoSet = sysInfoSetService.getByTableName(temp);
                if (sysInfoSet != null && !strTables.contains(sysInfoSet.getId().toString() + ","))
                    strTables = strTables + sysInfoSet.getId().toString() + ",";
            }
        }
        entity.setDescription(strTables);
    }

    @Transactional
    @Override
    public List<Map> getRefSituation(String id) {
        return sheetConditionDao.getRefSituation(id);
    }

    @Transactional
    @Override
    public void ResolveAndSave(HttpServletRequest request, UUID userId) throws Exception {
        String[] delItemsId = request.getParameterValues("delItemsId");
        String categoryId = request.getParameter("categoryId");
        String designId = request.getParameter("designId");
        byte status = 0;
        String ConditionName = request.getParameter("conditionName");
        String ConditionRelation = request.getParameter("conditionRelation");
        ConditionRelation = ConditionRelation.replaceAll("（", "(").replaceAll("）", ")");
        String[] JsonData = request.getParameterValues("conditionsJson");
        String ConditionsJson = request.getParameter("conditionsJson");
        Object OB = JSONObject.parse(ConditionsJson);
        UUID ConditionUUID = UUID.randomUUID();
        SheetCondition sheetCondition;
        boolean bExist = false;
        if (request.getParameter("conditionId") != null && !request.getParameter("conditionId").equals("")) {
            bExist = true;
            ConditionUUID = UUID.fromString(request.getParameter("conditionId"));
            sheetCondition = sheetConditionDao.getById(ConditionUUID);
            sheetCondition.setConditionRelation(ConditionRelation);
            sheetCondition.setConditionName(ConditionName);
        } else {
            if (categoryId == null) {
                sheetCondition = new SheetCondition(ConditionUUID, ConditionName, UUID.fromString(designId), ConditionRelation);
            } else {
                sheetCondition = new SheetCondition(ConditionUUID, UUID.fromString(categoryId), ConditionName, ConditionRelation);
            }

            if (sheetCondition.getOrdinal() == null) sheetCondition.setOrdinal(getNextOrdinal());
            sheetCondition.setStatus(status);
            sheetCondition.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            sheetCondition.setCreatedBy(userId);
        }

        sheetCondition.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        sheetCondition.setLastModifiedBy(userId);

        List<SheetConditionItem> sheetConditionItems = new ArrayList<SheetConditionItem>();
        String strSql = ConditionRelation;

        //默认一开始是数字 每次转换数字和字符后放入map
        List<Map> sqlmap = new ArrayList<Map>();
        String Num = "";
        String Logic = "";
        int type = 0;
        Map mapadd = new HashMap();
        char[] chars = strSql.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Character temp = chars[i];
            if (type == 0) {
                if (strNumList.indexOf(temp) == -1) {
                    mapadd.put("num", Num);
                    Logic = temp.toString();
                    Num = null;
                    type = 1;
                } else {
                    Num += temp.toString();
                }
            } else {
                if (strLogicList.indexOf(temp) == -1) {
                    mapadd.put("logic", Logic);
                    sqlmap.add(mapadd);
                    mapadd = new HashMap();
                    Logic = null;
                    Num = temp.toString();
                    type = 0;
                } else {
                    Logic += temp.toString();
                }
            }
            if (i == chars.length - 1) {
                if (Num != null) mapadd.put("num", Num);
                if (Logic != null) mapadd.put("logic", Logic);
                sqlmap.add(mapadd);
            }
        }
        strSql = "";
        String strTableIds = "";

        for (JSONObject json : ((JSONArray) OB).toJavaList(JSONObject.class)) {
            Integer conditionNum = json.getIntValue("conditionNum");
            UUID itemuuid = UUID.randomUUID();
            if (json.getString("itemId") != null && !json.getString("itemId").equals("")) {
                itemuuid = UUID.fromString(json.getString("itemId"));
            }
            SheetConditionItem sheetConditionItem = new SheetConditionItem(itemuuid, ConditionUUID, json.toJSONString(), conditionNum);
            sheetConditionItem.setStatus(status);
            sheetConditionItem.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            sheetConditionItem.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetConditionItem.setDescription(json.getString("ConditionDes"));

            String strtemp = GetConditionSql(json);
            sheetConditionItem.setSql(strtemp);
            sheetConditionItems.add(sheetConditionItem);
            strTableIds += GetConditionTableIds(json);
            List<Map> templist = sqlmap.stream().filter(item -> item.get("num").equals(json.getString("conditionNum"))).collect(Collectors.toList());

            if (templist.size() == 0) {
                throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"配置不完整--第" + json.getString("conditionNum") + "行条件未配置,请检查逻辑表达式！");
            }
            for (Map temp : templist) {
                temp.put("sql", strtemp);
            }
        }
        for (Map temp : sqlmap) {
            if (!temp.get("num").equals("") && temp.get("sql") == null) {
                throw new BusinessException(OpsErrorMessage.MODULE_NAME,OpsErrorMessage.ERROR_MESSAGE,"配置不完整--请检查条件数目是否与逻辑表达式数目不符合");
            }
            if (temp.get("sql") != null)
                strSql += temp.get("sql");
            if (temp.containsKey("logic"))
                strSql += temp.get("logic").toString().replaceAll("\\+", " OR ").replace("*", " AND ");
        }
        sheetCondition.setSql(strSql);
        sheetCondition.setDescription(strTableIds);
        sheetConditionDao.saveOrUpdate(sheetCondition);
        if (!bExist && categoryId == null) {
            SheetDesignCondition sheetDesignCondition = new SheetDesignCondition();
            sheetDesignCondition.setId(UUID.randomUUID());
            sheetDesignCondition.setConditionId(sheetCondition.getId());
            sheetDesignCondition.setDesignId(sheetCondition.getDesignId());
            sheetDesignCondition.setConditionName(sheetCondition.getConditionName());
            sheetDesignCondition.setOrdinal(sheetDesignConditionService.getNextOrdinal());
            sheetDesignCondition.setStatus(status);
            sheetDesignCondition.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            sheetDesignCondition.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDesignConditionService.create(sheetDesignCondition);
        }
        for (SheetConditionItem SheetConditionItem : sheetConditionItems) {
            sheetConditionItemDao.saveOrUpdate(SheetConditionItem);
        }
        if (delItemsId != null && delItemsId.toString().length() > 0) {
            for (String id : delItemsId) {
                if (!id.equals(""))
                    sheetConditionItemDao.deleteById(UUID.fromString(id));
            }
        }
    }

    @Transactional
    @Override
    public void saveOrUpdate(SheetCondition sysconditon, List<SheetConditionItem> SheetConditionItems) {
        sheetConditionDao.update(sysconditon);
        for (SheetConditionItem SheetConditionItem : SheetConditionItems) {
            sheetConditionItemDao.saveOrUpdate(SheetConditionItem);
        }
    }

    @Transactional
    @Override
    public String ResolveToStrSql(String combineQueryParams) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder statusstring = new StringBuilder();
        JSONObject json = (JSONObject) JSONObject.parse(combineQueryParams);
        String strRelation = json.getString("conditionRelation");
        JSONArray jarray = json.getJSONArray("jsonData");
        strRelation = strRelation.replaceAll("\\+", " Or ").replaceAll("\\*", " and ");
        List<String> tables = new ArrayList<>();

        for (Integer i = 0; i < jarray.size(); i++) {
            JSONObject jstemp = (JSONObject) jarray.get(i);
            String strtemp = GetConditionSql(jstemp);
            if (!jstemp.get("inputType").toString().equals("Function")) {
                String tablesort = strtemp.substring(0, 3) + ".";
                if (strtemp.equals("1=1")) {
                    tablesort = "";
                }
                // 根据字段的前三个来确定这个字段该查哪张表 如：A01001 查emp_a01表
                else if (!tables.contains(tablesort)) {
                    tables.add(tablesort);
                }

                int index = strRelation.indexOf(String.valueOf(i + 1));
                int index2 = strRelation.indexOf(String.valueOf(i + 2));
                if (index2 == -1) {
                    index2 = strRelation.length();
                }
                if (index == 1) {
                    sb.append(strRelation.substring(0, index) + tablesort + strtemp
                            + strRelation.substring(index + 1, index2));
                } else {
                    sb.append(tablesort + strtemp + strRelation.substring(index + 1, index2));
                }
            } else {

                if (strtemp.contains(".")) {
                    String[] st = strtemp.split("\\.");
                    for (int s = 0; s < st.length; s++) {
                        if (s % 2 == 1) {
                            if (!tables.contains(st[s].toString().substring(0, 3))) {
                                tables.add(st[s].toString().substring(0, 3) + ".");
                            }
                        }
                    }


                }

                sb.append(strtemp);
                if (i < jarray.size() - 1) {
                    sb.append(" and ");
                }
            }


        }
        sb1.append(" select  distinct(A01.emp_id) from EMP_A001 A01 ");
        if (tables.size() > 1) {
            for (int n = 0; n < tables.size(); n++) {
                if (!tables.get(n).equals("A01.")) {
                    sb1.append(" left join EMP_" + tables.get(n).replace(".", ""));
                    sb1.append(" ");
                    sb1.append(tables.get(n).replace(".", ""));
                    sb1.append(" on A01.emp_id=");
                    sb1.append(tables.get(n) + "emp_id");

                    statusstring.append(" and " + tables.get(n) + "status=0");
                }
            }
            sb1.append(" where ");
            sb1.append(sb.toString());
            sb1.append(statusstring);

        } else if (tables.size() == 1) {

            if (!tables.get(0).replace(".", "").equals("A01")) {
                // 例如 select *from emp_A001,emp_A02
                sb1.append(" left join  EMP_" + tables.get(0).replace(".", ""));
                sb1.append(" ");
                sb1.append(tables.get(0).replace(".", ""));
                // 例如A05.emp_id=A01.emp_id
                sb1.append(" on A01.emp_id=");
                sb1.append(tables.get(0) + "emp_id");

                statusstring.append(" and " + tables.get(0) + "status=0");
            }
            sb1.append(" where ");
            sb1.append(sb.toString());
            sb1.append(statusstring);
        } else {
            System.out.println("走这里");
            sb1.append(" where ");
            sb1.append(sb.toString());
            sb1.append(statusstring);
        }
        System.out.println("2018-4-11==" + sb1.toString());

        return sb1.toString();
    }

    @Transactional
    @Override
    public String GetConditionSql(JSONObject json) {
        String operator = json.getString("operatorCode");
        String conditiontype = json.getString("conditionType");
        String valueType = json.getString("valueType");
        String strtemp;
        if ("UNARY".equals(conditiontype)) {
            AbstractCondition bc = new UnaryCondition(LeftResolve(json.toString()), UnaryCondition.UnaryOperator.valueOf(operator));
            strtemp = bc.toString();
        } else if ("BINARY".equals(conditiontype)) {
            AbstractCondition bc = new BinaryCondition(LeftResolve(json.toString()), BinaryCondition.BinaryOperator.valueOf(operator), RightResolve(json.get("rightJson1").toString()));
            strtemp = bc.toString();
        } else if ("BETWEEN".equals(conditiontype)) {
            //System.out.println("hhhh==="+json.toString());
            AbstractCondition bc = new BetweenCondition(LeftResolve(json.toString()), BetweenCondition.BetweenOperator.valueOf(operator), BetweenResolve(json.get("rightJson1").toString(), json.get("rightJson2").toString()));
            strtemp = bc.toString();
        } else if ("IN".equals(conditiontype)) {
            AbstractCondition bc = new InCondition(LeftResolve(json.toString()), InCondition.InOperator.valueOf(operator), InResolve(json.toString()));
            strtemp = bc.toString();
        } else {
            AbstractCondition bc = new BinaryCondition(LeftResolve(json.toString()), BinaryCondition.BinaryOperator.valueOf(operator), RightResolve(json.get("rightJson1").toString()));
            strtemp = bc.toString();
        }
        return strtemp;
    }

    @Transactional
    @Override
    public String GetConditionTableIds(JSONObject json) {
        String tableIds = "";
        String itemIds = "";
        String operator = json.getString("operatorCode");
        String conditiontype = json.getString("conditionType");
        String InputType = json.getString("inputType");
        if (InputType.equals("SetItem")) {
            itemIds += json.getString("informationSetId") + ",";
        } else {
            if (json.containsKey("paramId1")) {
                itemIds += json.getString("paramId1") + ",";
            }
            if (json.containsKey("paramId2")) {
                itemIds += json.getString("paramId2") + ",";
            }
            if (json.containsKey("paramId3")) {
                itemIds += json.getString("paramId3") + ",";
            }
        }
        JSONObject jsonright1 = JSONObject.parseObject(json.get("rightJson1").toString());
        if (jsonright1.containsKey("paramId1")) {
            itemIds += jsonright1.getString("paramId1") + ",";
        }
        if (jsonright1.containsKey("paramId2")) {
            itemIds += jsonright1.getString("paramId2") + ",";
        }
        if (jsonright1.containsKey("paramId3")) {
            itemIds += jsonright1.getString("paramId3") + ",";
        }

        if ("BETWEEN".equals(conditiontype)) {
            JSONObject jsonright2 = JSONObject.parseObject(json.get("rightJson2").toString());
            if (jsonright2.containsKey("paramId1")) {
                itemIds += jsonright2.getString("paramId1") + ",";
            }
            if (jsonright2.containsKey("paramId2")) {
                itemIds += jsonright2.getString("paramId2") + ",";
            }
            if (jsonright2.containsKey("paramId3")) {
                itemIds += jsonright2.getString("paramId3") + ",";
            }
        }
        Collection<Integer> ids = new ArrayList<>();
        for (String temp : itemIds.split(",")) {
            if (!temp.isEmpty() && !ids.contains(Integer.parseInt(temp))) {
                ids.add(Integer.parseInt(temp));
            }
        }
        Collection<SysInfoItem> sysInfoItems = sysInfoItemService.getByIds(ids);
        for (SysInfoItem sysInfoItem : sysInfoItems) {
            tableIds += sysInfoItem.getInfoSetId().toString() + ",";
        }

        return tableIds;
    }

//    @Resource
//    private InfoSetService infoSetService;
//    @Resource
//    private InfoItemService infoItemService;

    //左解析函数
    public String LeftResolve(String LeftJson) {

        try {
            JSONObject jsontemp = JSONObject.parseObject(LeftJson);
            String strReturn = null;
            String InputType = jsontemp.get("inputType").toString();

            if ("Value".equals(InputType)) {
                return jsontemp.get("value1").toString();
            } else if ("SetItem".equals(InputType)) {
                return GetSetItemByID(jsontemp.get("informationSetId").toString());
            }
            strReturn = getFunctionData(jsontemp.get("functionName").toString(), jsontemp);
            System.out.println(strReturn);
            return strReturn;
        } catch (Exception e) {
            throw e;
        }
    }

    //右解析函数
    public Object RightResolve(String Json) {
        try {
            JSONObject jsontemp = JSONObject.parseObject(Json);
            String strReturn = null;
            boolean funFlg = jsontemp.containsKey("inputType");
            String valueType = jsontemp.get("valueType").toString();
            if (funFlg) {
                if ("sysParams".equals(valueType)) {
                    return "'" + jsontemp.get("value").toString() + "'";
                } else {
                    return getFunctionData(jsontemp.get("functionName").toString(), jsontemp);
                }
            }
            if ("text".equals(valueType)) {
                return "'" + jsontemp.get("value").toString() + "'";
            } else if ("date".equals(valueType)) {
                return "to_date('" + jsontemp.get("value").toString() + "','yyyy-MM-dd')";
            } else if ("SetItem".equals(valueType)) {
                if (valueType.equals("comboxtree")) {
                    return findCodeIdByText(jsontemp.get("value").toString());
                } else {
                    return jsontemp.get("value").toString();
                }

            } else if ("code".equals(valueType)) {
                return "'" + jsontemp.get("value").toString() + "'";
            } else {
                return "'" + jsontemp.get("value").toString() + "'";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private String getFunctionData(String functionName, JSONObject jsontemp) {
        String strReturn = "";
        switch (functionName) {
            case "INSTR": {
                strReturn = String.format("INSTR(%s,%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2), GetParaString(jsontemp, 3));
                break;
            }
            case "LENGTH": {
                strReturn = String.format("Len(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "UPPER": {
                strReturn = String.format("UPPER(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "LOWER": {
                strReturn = String.format("LOWER(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "LTRIM": {
                strReturn = String.format("LTRIM(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "RTRIM": {
                strReturn = String.format("RTRIM(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "TRIM": {
                strReturn = String.format("TRIM(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "REPLACE": {
                strReturn = String.format("REPLACE(%s,%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2), GetParaString(jsontemp, 3));
                break;
            }
            case "SUBSTR": {
                strReturn = String.format("SUBSTR(%s,%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2), GetParaString(jsontemp, 3));
                break;
            }
            case "ABS": {
                strReturn = String.format("ABS(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "CEIL": {
                strReturn = String.format("CEIL(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "FLOOR": {
                strReturn = String.format("FLOOR(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "LOG": {
                strReturn = String.format("LOG(%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "MOD": {
                strReturn = String.format("MOD(%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "POWER": {
                strReturn = String.format("POWER(%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "ROUND": {
                strReturn = String.format("ROUND(%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "SQRT": {
                strReturn = String.format("SQRT(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "TRUNC": {
                strReturn = String.format("TRUNC(%s,%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "TO_CHAR": {
                strReturn = String.format("TO_CHAR(%s,'%s')", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "TO_DATE": {
                strReturn = String.format("TO_DATE(%s,'%s')", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "TO_NUMBER": {
                strReturn = String.format("TO_NUMBER(%s)", GetParaString(jsontemp, 1));
                break;
            }
            case "NVL": {
                strReturn = String.format("NVL(%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2));
                break;
            }
            case "NVL2": {
                strReturn = String.format("NVL2(%s,%s,%s)", GetParaString(jsontemp, 1), GetParaString(jsontemp, 2), GetParaString(jsontemp, 3));
                break;
            }
            case "MONTHDIF": {
                String date1 = GetParaString(jsontemp, 1);
                String date2 = GetParaString(jsontemp, 2);
                strReturn = String.format("MONTHS_BETWEEN(%s,%s)", date1, date2);
                break;
            }
            case "AGE": {
                strReturn = String.format("floor(months_BETWEEN(sysdate,%s) / 12)", GetParaString(jsontemp, 1));
                break;
            }
            //年份差
            case "YEARDIF": {
                String date1 = GetParaString(jsontemp, 1);
                String date2 = GetParaString(jsontemp, 2);
                strReturn = String.format("floor(months_BETWEEN(%s,%s) / 12)", date1, date2);
                break;
            }
            case "DAYDIF": {
                String date1 = GetParaString(jsontemp, 1);
                String date2 = GetParaString(jsontemp, 2);
                strReturn = String.format("abs(to_date(%s)-to_date(%s))", date1, date2);
                break;
            }
            case "SYSDATE": {
                strReturn = String.format("SYSDATE");
                break;
            }
            //年差
            case "YEARNUMDIF": {
                String date1 = GetParaString(jsontemp, 1);
                String date2 = GetParaString(jsontemp, 2);
                strReturn = String.format("round(months_BETWEEN(%s,%s) / 12,2)", date1, date2);
                break;
            }
        }
        return strReturn;
    }

    private String findCodeIdByText(String text) {
        return "";
        //return infoItemService.findCodeIdByText(text);
    }

    //between解析函数
    public List<Object> BetweenResolve(String RightJson, String RightJson2) {
        try {
            List<Object> ob = new ArrayList<Object>();
            JSONObject jsontemp = JSONObject.parseObject(RightJson);
            ob.add(RightResolve(RightJson));
            ob.add(RightResolve(RightJson2));

            return ob;
        } catch (Exception e) {
            throw e;
        }
    }

    //In解析函数
    public Collection<String> InResolve(String RightJson) {
        try {
            List<Object> ob = new ArrayList<Object>();
            JSONObject jsontemp = JSONObject.parseObject(RightJson);
            Collection<String> values = Arrays.asList(jsontemp.get("value1").toString().split(","));
            Collection<String> result = new ArrayList<>();
            if (jsontemp.get("valueType1").equals("number")) {
                result = values;
            } else {
                for (String strtemp : values) {
                    result.add("'" + strtemp + "'");
                }
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    //取得参数
    private String GetParaString(JSONObject jsontemp, Integer ParaIndex) {
        try {
            String ParaInputType = jsontemp.get("paramType" + ParaIndex).toString();
            String paramValue = jsontemp.get("paramValue" + ParaIndex).toString();
            String ParamId = "";
            if (jsontemp.get("paramId" + ParaIndex) != null) {
                ParamId = jsontemp.get("paramId" + ParaIndex).toString();
                return GetSetItemByID(ParamId);
            } else {
                if (jsontemp.get("paramValue" + ParaIndex).equals("系统时间")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String time = sdf.format(new Date());
                    return ParticularUtils.FormatDataStr(time);
                } else {
                    if (ParticularUtils.isValidDate(jsontemp.get("paramValue" + ParaIndex).toString())) {
                        return ParticularUtils.FormatDataStr(jsontemp.get("paramValue" + ParaIndex).toString());
                    } else {
                        return "'" + jsontemp.get("paramValue" + ParaIndex).toString() + "'";
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    //根据itemId取得对应的table.column
    private String GetSetItemByID(String stritemId) {
        SysInfoItem sysInfoItem = sysInfoItemService.getById(Integer.parseInt(stritemId));
        SysInfoSet infoset = sysInfoSetService.getById(sysInfoItem.getInfoSetId());
        return infoset.getTableName() + "." + sysInfoItem.getColumnName();
    }

    private String GetSetItemByID(String stritemId, String tableAlis) {
        SysInfoItem sysInfoItem = sysInfoItemService.getById(Integer.parseInt(stritemId));
        return tableAlis + "." + sysInfoItem.getColumnName();
    }

    @Override
    @Transactional
    public boolean moveDown(SheetCondition entity, UUID categoryId) {
        UUID id = entity.getId();
        SheetCondition curr = sheetConditionDao.getById(entity.getId());
        int ordinal = curr.getOrdinal();
        UUID userName = entity.getLastModifiedBy();
        SheetCondition nextvious = sheetConditionDao.findNext(id, categoryId);
        if (nextvious != null) {
            UUID nextId = nextvious.getId();
            int nextOrdinal = nextvious.getOrdinal();
            int cnt = sheetConditionDao.updateOrdinal(nextId, ordinal, userName);
            if (cnt > 0) {
                cnt = sheetConditionDao.updateOrdinal(id, nextOrdinal, userName);
                if (cnt > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean moveUp(SheetCondition entity, UUID categoryId) {
        UUID id = entity.getId();
        SheetCondition curr = sheetConditionDao.getById(entity.getId());
        int ordinal = curr.getOrdinal();
        UUID userName = entity.getLastModifiedBy();
        SheetCondition previous = sheetConditionDao.findPrevious(id, categoryId);
        if (previous != null) {
            UUID preId = previous.getId();
            int preOrdinal = previous.getOrdinal();
            int cnt = sheetConditionDao.updateOrdinal(preId, ordinal, userName);
            if (cnt > 0) {
                cnt = sheetConditionDao.updateOrdinal(id, preOrdinal, userName);
                if (cnt > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
