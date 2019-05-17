package com.sinosoft.ops.cimp.service.tabledata.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.constant.DeleteTypeEnum;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.constant.SysOpLogEnum;
import com.sinosoft.ops.cimp.constant.TableFieldLogicalDeleteFlagEnum;
import com.sinosoft.ops.cimp.dao.SysTableDao;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.Conditions;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ExecParam;
import com.sinosoft.ops.cimp.dao.domain.ResultParam;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.QueryDataParamBuilder;
import com.sinosoft.ops.cimp.dto.TranslateField;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableFieldInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.oplog.SysOperationLog;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.sinosoft.ops.cimp.service.sys.oplog.SysOperationLogService;
import com.sinosoft.ops.cimp.service.sys.systable.SysTableFieldService;
import com.sinosoft.ops.cimp.service.tabledata.SysTableModelInfoService;
import com.sinosoft.ops.cimp.util.CachePackage.OrganizationCacheManager;
import com.sinosoft.ops.cimp.util.HttpUtils;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SysTableModelInfoServiceImpl implements SysTableModelInfoService {

    private final SysTableInfoDao sysTableInfoDao;
    private final SysTableDao sysTableDao;
    private final com.sinosoft.ops.cimp.service.sys.systable.SysTableFieldService sysTableFieldService;
    private final SysOperationLogService sysOperationLogService;
    private final JdbcTemplate jdbcTemplate;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Autowired
    public SysTableModelInfoServiceImpl(SysTableInfoDao sysTableInfoDao, SysTableDao sysTableDao, SysTableFieldService sysTableFieldService, SysOperationLogService sysOperationLogService, JdbcTemplate jdbcTemplate) {
        this.sysTableInfoDao = sysTableInfoDao;
        this.sysTableDao = sysTableDao;
        this.sysTableFieldService = sysTableFieldService;
        this.sysOperationLogService = sysOperationLogService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public QueryDataParamBuilder saveData(QueryDataParamBuilder queryDataParam) throws BusinessException {
        //获取表类型名，信息集名，项目编号，主键字段
        String tableTypeNameEn = queryDataParam.getTableTypeNameEn();
        String tableNameEn = queryDataParam.getTableNameEn();
        String prjCode = queryDataParam.getPrjCode();
        String primaryKey = queryDataParam.getTableNameEnPK();
        String tableNameEnFK = queryDataParam.getTableNameEnFK();
        Object tableNameEnFKValue = queryDataParam.getTableNameEnFKValue();

        Map<String, Object> saveOrUpdateFormData = queryDataParam.getSaveOrUpdateFormData();

        //获取系统表结构信息
        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn, prjCode);
        if (tableInfo == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, tableTypeNameEn);
        }

        Map<String, List<SysTableInfoDTO>> sysTableInfoMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
        List<SysTableInfoDTO> sysTableInfoDTOList = sysTableInfoMap.get(tableNameEn);
        if (sysTableInfoDTOList == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "保存信息集必须在项目中存在");
        }
        List<String> sysTableFieldList = sysTableInfoDTOList.stream().map(SysTableInfoDTO::getFields).flatMap(List::stream).map(SysTableFieldInfoDTO::getFieldNameEn).collect(Collectors.toList());
        Map<String, String> sysFieldNameEnCnMap = Maps.newHashMap();
        String tableNameCn = "";
        for (SysTableInfoDTO sysTableInfoDTO : sysTableInfoDTOList) {
            tableNameCn = sysTableInfoDTO.getTableNameCn();
            for (SysTableFieldInfoDTO field : sysTableInfoDTO.getFields()) {
                String fieldNameEn = field.getFieldNameEn();
                String fieldNameCn = field.getFieldNameCn();

                sysFieldNameEnCnMap.put(fieldNameEn, fieldNameCn);
            }
        }
//        sysTableInfoDTOList.stream().map(e -> e.getFields())
        List<String> execTableFieldList = Lists.newArrayList(saveOrUpdateFormData.keySet());

        boolean result = sysTableFieldList.containsAll(execTableFieldList);
        if (!result) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "保存属性必须全部在配置中存在");
        }

        List<ExecParam> execParamList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : saveOrUpdateFormData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            execParamList.add(new ExecParam(key, value));
        }
        //主键字段和系统配置主键一致则认为是主集信息保存
        String sysPrimaryKey = tableInfo.getPrimaryField();
        ExecParam fKeyExecParam = null;
        if (!StringUtils.equals(sysPrimaryKey, primaryKey) && StringUtils.equals(tableNameEnFK, sysPrimaryKey)) {
            fKeyExecParam = new ExecParam(tableNameEnFK, tableNameEnFKValue);
        }
        String primaryKeyValue = IdUtil.uuidWithoutMinus();
        queryDataParam.setTableNameEnPKValue(primaryKeyValue);
        ExecParam primaryKeyExecParam = new ExecParam(primaryKey, primaryKeyValue);
        execParamList.add(primaryKeyExecParam);
        if (fKeyExecParam != null) {
            execParamList.add(fKeyExecParam);
        }
        DaoParam daoParam = new DaoParam();
        daoParam.addTableTypeNameEn(tableTypeNameEn)
                .addTableNameEn(tableNameEn)
                .addExecParamList(execParamList);

        //异步生成日志
        if (StringUtils.equals(primaryKey, sysPrimaryKey)) {
            this.asyncCadreOpLog(primaryKeyValue, null, null, SysOpLogEnum.CREATE, tableNameCn, saveOrUpdateFormData, sysFieldNameEnCnMap);
        } else if (StringUtils.equals(tableNameEnFK, sysPrimaryKey)) {
            this.asyncCadreOpLog(String.valueOf(tableNameEnFKValue), null, null, SysOpLogEnum.CREATE, tableNameCn, saveOrUpdateFormData, sysFieldNameEnCnMap);
        }

        sysTableDao.insertData(daoParam);
        return queryDataParam;
    }

    @Override
    public QueryDataParamBuilder queryData(QueryDataParamBuilder queryDataParam) throws BusinessException {
        String prjCode = queryDataParam.getPrjCode();
        String tableTypeNameEn = queryDataParam.getTableTypeNameEn();
        String tableNameEn = queryDataParam.getTableNameEn();
        String tableNameEnPK = queryDataParam.getTableNameEnPK();
        String tableNameEnPKValue = String.valueOf(queryDataParam.getTableNameEnPKValue());
        String tableNameEnFK = queryDataParam.getTableNameEnFK();
        String tableNameEnFKValue = String.valueOf(queryDataParam.getTableNameEnFKValue());

        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn, prjCode);
        if (tableInfo == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, tableTypeNameEn);
        }

        Map<String, List<SysTableInfoDTO>> sysTableInfoMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
        List<SysTableInfoDTO> sysTableInfoDTOList = sysTableInfoMap.get(tableNameEn);
        if (sysTableInfoDTOList == null || sysTableInfoDTOList.size() == 0) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "查询信息集必须在项目中配置");
        }

        DaoParam daoParam = new DaoParam();
        daoParam.addPrjCode(prjCode)
                .addTableTypeNameEn(tableTypeNameEn)
                .addTableNameEn(tableNameEn);

        SysTableInfoDTO sysTableInfoDTO = sysTableInfoDTOList.get(0);

        List<SysTableFieldInfoDTO> fields = sysTableInfoDTO.getFields();
        Map<String, List<SysTableFieldInfoDTO>> fieldNameEnMap = fields.stream().collect(Collectors.groupingBy(SysTableFieldInfoDTO::getFieldNameEn));

        List<ExecParam> execParamList = fields.stream().map(field -> {
            ExecParam execParam = new ExecParam();
            execParam.setFieldName(field.getFieldNameEn());
            return execParam;
        }).collect(Collectors.toList());

        boolean masterTable = sysTableInfoDTO.isMasterTable();
        if (masterTable) {
            daoParam.addEqualCondition(tableNameEnPK, tableNameEnPKValue);
            ExecParam paramPk = new ExecParam(tableNameEnPK, "");
            if (!contains(execParamList, paramPk)) {
                execParamList.add(paramPk);
            }
        } else {
            daoParam.addEqualCondition(tableNameEnFK, tableNameEnFKValue);
            ExecParam paramPk = new ExecParam(tableNameEnPK, "");
            ExecParam paramFk = new ExecParam(tableNameEnPK, "");
            if (!contains(execParamList, paramPk)) {
                execParamList.add(paramPk);
            }
            if (!contains(execParamList, paramFk)) {
                execParamList.add(paramFk);
            }
        }
        //非逻辑删除条件
        daoParam.addCondition("STATUS", Conditions.ConditionsEnum.EQUAL, "0");
        daoParam.addSort("ORDINAL", "ASC");

        List<String> resultField = execParamList.stream().map(ExecParam::getFieldName).collect(Collectors.toList());
        queryDataParam.setResultFields(resultField);

        daoParam.addExecParamList(execParamList);

        //masterTable是否为主表，若非主表则为多条数据
        daoParam.addParam("isMultiData", !masterTable);
        //请求结果
        ResultParam resultParam = sysTableDao.queryPageData(daoParam);

        ResultParam.ValueType valueType = resultParam.getValueType();
        if (valueType == ResultParam.ValueType.ONE) {
            Map<String, Object> maps = Maps.newLinkedHashMap();
            Object[] values = resultParam.getValues();
            List<String> resultFields = queryDataParam.getResultFields();
            for (int i = 0; i < resultFields.size(); i++) {
                String s = resultFields.get(i);
                Object value = values[i];
                TranslateField translateField = this.getTranslateField(fieldNameEnMap, s, value);
                if (translateField != null) {
                    queryDataParam.setTranslateFields(translateField);
                }
                maps.put(s, value);
            }
            queryDataParam.setResultDataType("1");
            queryDataParam.setResultData(maps);
        } else {
            List<Map<String, Object>> resultList = Lists.newArrayList();

            List<String> resultFields = queryDataParam.getResultFields();
            Object[][] valueLists = resultParam.getValueLists();
            if (valueLists != null) {
                List<TranslateField> translateFields = Lists.newArrayList();
                for (Object[] valueList : valueLists) {
                    Map<String, Object> values = Maps.newLinkedHashMap();
                    for (int i1 = 0; i1 < resultFields.size(); i1++) {
                        String s = resultFields.get(i1);
                        Object o = valueList[i1];
                        TranslateField translateField = this.getTranslateField(fieldNameEnMap, s, o);
                        if (translateField != null) {
                            translateFields.add(translateField);
                        }
                        values.put(s, o);
                    }
                    resultList.add(values);
                }
                queryDataParam.setTranslateFields(translateFields);
            }
            queryDataParam.setResultDataType("2");
            queryDataParam.setResultMultiData(resultList);
        }
        return queryDataParam;
    }

    private boolean contains(List<ExecParam> execParamList, ExecParam paramPk) {
        for (ExecParam execParam : execParamList) {
            if (execParam.getFieldName().equalsIgnoreCase(paramPk.getFieldName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public QueryDataParamBuilder updateData(QueryDataParamBuilder queryDataParam) throws BusinessException {
        String prjCode = queryDataParam.getPrjCode();
        String tableTypeNameEn = queryDataParam.getTableTypeNameEn();
        String tableNameEn = queryDataParam.getTableNameEn();
        String tableNameEnPK = queryDataParam.getTableNameEnPK();
        String tableNameEnPKValue = String.valueOf(queryDataParam.getTableNameEnPKValue());
        Map<String, Object> saveOrUpdateFormData = queryDataParam.getSaveOrUpdateFormData();

        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn, prjCode);
        if (tableInfo == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, tableTypeNameEn);
        }
        //TODO 获取app定义信息对属性进行过滤
        Map<String, List<SysTableInfoDTO>> sysTableInfoMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
        List<SysTableInfoDTO> sysTableInfoDTOList = sysTableInfoMap.get(tableNameEn);
        if (sysTableInfoDTOList == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "保存信息集必须在项目中存在");
        }
        String tableNameCn = "";
        SysTableInfoDTO sysTableInfoDTO = sysTableInfoDTOList.get(0);
        if (sysTableInfoDTO != null) {
            tableNameCn = sysTableInfoDTO.getTableNameCn();
        }
        List<String> sysTableFieldList = sysTableInfoDTOList.stream().map(SysTableInfoDTO::getFields).flatMap(List::stream).map(SysTableFieldInfoDTO::getFieldNameEn).collect(Collectors.toList());
        List<String> execTableFieldList = Lists.newArrayList(saveOrUpdateFormData.keySet());

        Map<String, String> sysFieldNameEnCnMap = Maps.newHashMap();
        for (SysTableInfoDTO tableInfoDTO : sysTableInfoDTOList) {
            List<SysTableFieldInfoDTO> fields = tableInfoDTO.getFields();
            for (SysTableFieldInfoDTO field : fields) {
                String fieldNameEn = field.getFieldNameEn();
                String fieldNameCn = field.getFieldNameCn();
                sysFieldNameEnCnMap.put(fieldNameEn, fieldNameCn);
            }
        }
        boolean result = sysTableFieldList.containsAll(execTableFieldList);
        if (!result) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "保存属性必须全部在配置中存在");
        }
        List<ExecParam> execParamList = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : saveOrUpdateFormData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            execParamList.add(new ExecParam(key, value));
        }

        DaoParam daoParam = new DaoParam();

        daoParam.addTableTypeNameEn(tableTypeNameEn)
                .addTableNameEn(tableNameEn)
                .addExecParamList(execParamList)
                .addEqualCondition(tableNameEnPK, tableNameEnPKValue);

        String primaryField = tableInfo.getPrimaryField();
        if (StringUtils.equals(primaryField, tableNameEnPK)) {
            this.asyncCadreOpLog(tableNameEnPKValue, null, null, SysOpLogEnum.UPDATE, tableNameCn, saveOrUpdateFormData, sysFieldNameEnCnMap);
        } else {
            this.asyncCadreOpLog(null, tableNameEnPKValue, tableNameEn, SysOpLogEnum.UPDATE, tableNameCn, saveOrUpdateFormData, sysFieldNameEnCnMap);
        }
        sysTableDao.updateData(daoParam);
        return queryDataParam;
    }

    @Override
    public QueryDataParamBuilder deleteDataByFlag(QueryDataParamBuilder queryDataParam, String deleteType, String deleteFlagCode) throws BusinessException {
        String prjCode = queryDataParam.getPrjCode();
        String tableTypeNameEn = queryDataParam.getTableTypeNameEn();
        String tableNameEn = queryDataParam.getTableNameEn();
        Map<String, Object> saveOrUpdateFormData = queryDataParam.getSaveOrUpdateFormData();

        SysTableModelInfoDTO tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn, prjCode);
        if (tableInfo == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, tableTypeNameEn);
        }

        Map<String, List<SysTableInfoDTO>> sysTableInfoMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
        List<SysTableInfoDTO> sysTableInfoDTOList = sysTableInfoMap.get(tableNameEn);
        if (sysTableInfoDTOList == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "删除信息集必须在项目中存在");
        }

        SysTableInfoDTO sysTableInfoDTO = sysTableInfoDTOList.get(0);
        List<SysTableField> sysTableFields = sysTableFieldService.getSysTableFieldBySysTableId(sysTableInfoDTO.getId());

        String deleteFlagFieldEnName = new String("");
        String tableNameEnPK = new String("");

        //获取主键
        Optional<SysTableField> primaryKeyField = sysTableFields.stream().filter(x -> x.getIsPK().equals("1")).findFirst();

        //主键字段必须存在
        if (primaryKeyField.isPresent()) {
            SysTableField sysTableField = primaryKeyField.get();
            tableNameEnPK = sysTableField.getNameEn();
        } else {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100207, tableTypeNameEn);
        }

        //获取删除列
        Optional<SysTableField> logicalDeleteFlagField = sysTableFields.stream().filter(x -> x.getLogicalDeleteFlag().equals(TableFieldLogicalDeleteFlagEnum.删除.getCode())).findFirst();

        //删除字段必须存在
        if (logicalDeleteFlagField.isPresent()) {
            SysTableField sysTableField = logicalDeleteFlagField.get();
            deleteFlagFieldEnName = sysTableField.getDbFieldName();
        } else {
            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100208, tableTypeNameEn);
        }


        if (DeleteTypeEnum.物理删除.getCode().equals(deleteType)) {
            DaoParam daoParam = new DaoParam();
            List<String> idList = (List<String>) saveOrUpdateFormData.get(tableNameEnPK);
            String idJoinString = idList.stream().collect(Collectors.joining("','", "'", "'"));
            daoParam.addTableTypeNameEn(tableTypeNameEn)
                    .addTableNameEn(tableNameEn)
                    .addCondition(tableNameEnPK, Conditions.ConditionsEnum.IN, idJoinString)
                    .addCondition(deleteFlagFieldEnName, Conditions.ConditionsEnum.EQUAL, TableFieldLogicalDeleteFlagEnum.删除.getCode());
            sysTableDao.deleteData(daoParam);
        } else {
            List<ExecParam> execParamList = Lists.newArrayList();

            String key = deleteFlagFieldEnName;
            String value = deleteFlagCode;
            execParamList.add(new ExecParam(key, value));

            DaoParam daoParam = new DaoParam();

            List<String> idList = (List<String>) saveOrUpdateFormData.get(tableNameEnPK);
            String idJoinString = idList.stream().collect(Collectors.joining("','", "'", "'"));

            daoParam.addTableTypeNameEn(tableTypeNameEn)
                    .addTableNameEn(tableNameEn)
                    .addExecParamList(execParamList)
                    .addCondition(tableNameEnPK, Conditions.ConditionsEnum.IN, idJoinString);
            sysTableDao.updateData(daoParam);
        }
        return queryDataParam;
    }

    @Override
    public QueryDataParamBuilder deleteData(QueryDataParamBuilder queryDataParam) throws BusinessException {
        return deleteDataByFlag(queryDataParam, DeleteTypeEnum.逻辑删除.getCode(), TableFieldLogicalDeleteFlagEnum.删除.getCode());
    }

    @Override
    public QueryDataParamBuilder deleteDataRecover(QueryDataParamBuilder queryDataParam) throws BusinessException {
        return deleteDataByFlag(queryDataParam, DeleteTypeEnum.逻辑删除.getCode(), TableFieldLogicalDeleteFlagEnum.有效.getCode());
    }

    @Override
    public QueryDataParamBuilder deleteDataFinal(QueryDataParamBuilder queryDataParam) throws BusinessException {
        return deleteDataByFlag(queryDataParam, DeleteTypeEnum.物理删除.getCode(), TableFieldLogicalDeleteFlagEnum.删除.getCode());
    }

    private TranslateField getTranslateField(Map<String, List<SysTableFieldInfoDTO>> fieldNameEnMap, String fieldName, Object fieldValue) {
        //字段中有需要进行“单位树”翻译的字段
        TranslateField translateField = null;

        List<SysTableFieldInfoDTO> sysTableFieldInfoDTOS = fieldNameEnMap.get(fieldName);
        if (sysTableFieldInfoDTOS != null && sysTableFieldInfoDTOS.size() > 0) {
            SysTableFieldInfoDTO sysTableFieldInfoDTO = sysTableFieldInfoDTOS.get(0);
            String codeSetName = sysTableFieldInfoDTO.getCodeSetName();
            String codeSetType = sysTableFieldInfoDTO.getCodeSetType();
            if (StringUtils.equals(codeSetType, "1")) {
                if (StringUtils.isNotEmpty(codeSetName)) {
                    translateField = new TranslateField();
                    translateField.setFieldName(fieldName);
                    translateField.setFieldValue(fieldValue);
                    Organization organization = OrganizationCacheManager.getSubject().getOrganizationById(String.valueOf(fieldValue));
                    if (organization != null) {
                        translateField.setFieldTranslateValue(organization.getName());
                        return translateField;
                    }
                }
            }
        }
        return translateField;
    }

    //异步生成操作日志
    private void asyncCadreOpLog(String empId, String subId, String tableNameEn, SysOpLogEnum sysOpLogEnum, String tableNameCn, Map<String, Object> formMap, Map<String, String> sysFieldNameEnCnMap) {
        executorService.submit(() -> {
            String userId = "";
            String loginName = "";
            String userName = "";
            User currentUser = SecurityUtils.getSubject().getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getId();
                loginName = currentUser.getLoginName();
                userName = currentUser.getName();
            }
            String cardId = "";
            String cadreName = "";
            if (StringUtils.isNotEmpty(empId) || StringUtils.isNotEmpty(subId)) {
                String sql = "SELECT A001003 AS \"cardId\", A01001 AS \"cadreName\" FROM EMP_A001 WHERE EMP_ID = '%s'";
                String execSql = "";
                if (StringUtils.isNotEmpty(empId)) {
                    execSql = String.format(sql, empId);
                }
                if (StringUtils.isEmpty(empId) && StringUtils.isNotEmpty(subId)) {
                    execSql = String.format(sql, subId);
                }

                List<Map<String, Object>> listMap = jdbcTemplate.queryForList(execSql);
                if (listMap.size() > 0) {
                    Map<String, Object> map = listMap.get(0);
                    Object card_id = map.get("cardId");
                    if (card_id != null) {
                        cardId = String.valueOf(card_id);
                    }
                    Object cadre_name = map.get("cadreName");
                    if (cadre_name != null) {
                        cadreName = String.valueOf(cadre_name);
                    }
                }
            }

            String ipAddr = HttpUtils.getIpAddr();
            Integer opType = sysOpLogEnum.getOpType();
            SysOperationLog sysOperationLog = new SysOperationLog();
            sysOperationLog.setUserId(userId);
            sysOperationLog.setLoginName(loginName);
            sysOperationLog.setUserName(userName);
            sysOperationLog.setOpFromIp(ipAddr);
            sysOperationLog.setOpFromMac(HttpUtils.getMACAddress(ipAddr));
            sysOperationLog.setExecMethod(opType);

            if (formMap != null) {
                sysOperationLog.setExecDetail(JsonUtil.getJsonString(formMap));
            }
            StringBuilder logDetailBuilder = new StringBuilder();
            if (opType != null) {
                if (SysOpLogEnum.CREATE.getOpType().equals(opType)) {
                    logDetailBuilder.append("添加了").append("(").append(cardId).append(")")
                            .append("[").append(tableNameCn).append("]").append("信息集");

                    if (formMap != null) {
                        for (Map.Entry<String, Object> entry : formMap.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            String attrNameCn = sysFieldNameEnCnMap.get(key);
                            logDetailBuilder.append("新增").append("[").append(attrNameCn).append("]").append("属性值").append("【").append(value).append("】");
                        }
                    }
                    sysOperationLog.setLogDetail(logDetailBuilder.toString());
                }
                if (SysOpLogEnum.UPDATE.getOpType().equals(opType)) {
                    if (StringUtils.isNotEmpty(tableNameEn)) {
                        try {
                            SysTableModelInfo cadreInfo = sysTableInfoDao.getTableInfo("CadreInfo");
                            String tableSaveName = cadreInfo.getTableNameEnAndSaveTableMap().get(tableNameEn);
                            String sql = "SELECT EMP_ID as \"cadreId\", A01001 as \"cadreName\" FROM EMP_A001 WHERE EMP_ID = (SELECT EMP_ID FROM %s WHERE SUB_ID = '%s')";
                            if (StringUtils.isNotEmpty(tableSaveName) && StringUtils.isNotEmpty(subId)) {
                                String execSql = String.format(sql, tableSaveName, subId);
                                List<Map<String, Object>> maps = jdbcTemplate.queryForList(execSql);
                                if (maps.size() > 0) {
                                    Map<String, Object> map = maps.get(0);
                                    Object cadreId = map.get("cadreId");
                                    Object cadreName1 = map.get("cadreName");
                                    cardId = String.valueOf(cadreId);
                                    cadreName = String.valueOf(cadreName1);
                                }
                            }
                        } catch (BusinessException e) {
                            //如果有异常则不执行获取cadreId和cadreName
                        }
                    }
                    logDetailBuilder.append("修改了").append(cadreName).append("(").append(cardId).append(")")
                            .append("[").append(tableNameCn).append("]").append("信息集");

                    if (formMap != null) {
                        for (Map.Entry<String, Object> entry : formMap.entrySet()) {
                            String key = entry.getKey();
                            Object value = entry.getValue();
                            String attrNameCn = sysFieldNameEnCnMap.get(key);
                            logDetailBuilder.append("将").append("[").append(attrNameCn).append("]").append("属性的值修改为").append("【").append(value).append("】");
                        }
                    }
                    sysOperationLog.setLogDetail(logDetailBuilder.toString());
                }
                if (SysOpLogEnum.DELETE.getOpType().equals(opType)) {
                    try {
                        SysTableModelInfo cadreInfo = sysTableInfoDao.getTableInfo("CadreInfo");
                        String tableSaveName = cadreInfo.getTableNameEnAndSaveTableMap().get(tableNameEn);
                        String sql = "SELECT EMP_ID as \"cadreId\", A01001 as \"cadreName\" FROM EMP_A001 WHERE EMP_ID = (SELECT EMP_ID FROM %s WHERE SUB_ID = '%s')";
                        if (StringUtils.isNotEmpty(tableSaveName) && StringUtils.isNotEmpty(subId)) {
                            String execSql = String.format(sql, tableSaveName, subId);
                            List<Map<String, Object>> maps = jdbcTemplate.queryForList(execSql);
                            if (maps.size() > 0) {
                                Map<String, Object> map = maps.get(0);
                                Object cadreId = map.get("cadreId");
                                Object cadreName1 = map.get("cadreName");
                                cardId = String.valueOf(cadreId);
                                cadreName = String.valueOf(cadreName1);
                            }
                        }
                    } catch (BusinessException e) {
                        //如果有异常则不执行获取cadreId和cadreName
                    }
                    logDetailBuilder.append("删除了").append(cadreName).append("(").append(cardId).append(")")
                            .append("[").append(tableNameCn).append("]").append("信息集");
                }
                sysOperationLog.setLogDetail(logDetailBuilder.toString());
            }

            sysOperationLogService.saveLog(sysOperationLog);
        });
    }
}