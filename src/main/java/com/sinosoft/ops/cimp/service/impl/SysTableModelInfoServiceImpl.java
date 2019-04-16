package com.sinosoft.ops.cimp.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SysTableDao;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ExecParam;
import com.sinosoft.ops.cimp.dao.domain.ResultParam;
import com.sinosoft.ops.cimp.dto.QueryDataParamBuilder;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableFieldInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableInfoDTO;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.SysTableModelInfoService;
import com.sinosoft.ops.cimp.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysTableModelInfoServiceImpl implements SysTableModelInfoService {

    private final SysTableInfoDao sysTableInfoDao;
    private final SysTableDao sysTableDao;

    @Autowired
    public SysTableModelInfoServiceImpl(SysTableInfoDao sysTableInfoDao, SysTableDao sysTableDao) {
        this.sysTableInfoDao = sysTableInfoDao;
        this.sysTableDao = sysTableDao;
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
        //TODO 获取app定义信息对属性进行过滤
        Map<String, List<SysTableInfoDTO>> sysTableInfoMap = tableInfo.getTables().stream().collect(Collectors.groupingBy(SysTableInfoDTO::getTableNameEn));
        List<SysTableInfoDTO> sysTableInfoDTOList = sysTableInfoMap.get(tableNameEn);
        if (sysTableInfoDTOList == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "保存信息集必须在项目中存在");
        }
        List<String> sysTableFieldList = sysTableInfoDTOList.stream().map(SysTableInfoDTO::getFields).flatMap(List::stream).map(SysTableFieldInfoDTO::getFieldNameEn).collect(Collectors.toList());
//        List<String> sysTableFieldList = tableInfo.getTableNameEnAndFieldNameListMap().get(tableNameEn);
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
        ExecParam primaryKeyExecParam = new ExecParam(primaryKey, IdUtil.uuidWithoutMinus());
        execParamList.add(primaryKeyExecParam);
        if (fKeyExecParam != null) {
            execParamList.add(fKeyExecParam);
        }
        DaoParam daoParam = new DaoParam();
        daoParam.addTableTypeNameEn(tableTypeNameEn)
                .addTableNameEn(tableNameEn)
                .addExecParamList(execParamList);
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

        List<ExecParam> execParamList = sysTableInfoDTO.getFields().stream().map(field -> {
            ExecParam execParam = new ExecParam();
            execParam.setFieldName(field.getFieldNameEn());
            return execParam;
        }).collect(Collectors.toList());

        boolean masterTable = sysTableInfoDTO.isMasterTable();
        if (masterTable) {
            daoParam.addEqualCondition(tableNameEnPK, tableNameEnPKValue);
            execParamList.add(new ExecParam(tableNameEnPK, ""));
        } else {
            daoParam.addEqualCondition(tableNameEnFK, tableNameEnFKValue);
            execParamList.add(new ExecParam(tableNameEnPK, ""));
            execParamList.add(new ExecParam(tableNameEnFK, ""));
        }
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
                maps.put(s, value);
            }
            queryDataParam.setResultDataType("1");
            queryDataParam.setResultData(maps);
        } else {
            List<Map<String, Object>> resultList = Lists.newArrayList();

            List<String> resultFields = queryDataParam.getResultFields();
            Object[][] valueLists = resultParam.getValueLists();
            if (valueLists != null) {
                for (Object[] valueList : valueLists) {
                    Map<String, Object> values = Maps.newLinkedHashMap();
                    for (int i1 = 0; i1 < resultFields.size(); i1++) {
                        String s = resultFields.get(i1);
                        Object o = valueList[i1];
                        values.put(s, o);
                    }
                    resultList.add(values);
                }
            }
            queryDataParam.setResultDataType("2");
            queryDataParam.setResultMultiData(resultList);
        }
        return queryDataParam;
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
        List<String> sysTableFieldList = sysTableInfoDTOList.stream().map(SysTableInfoDTO::getFields).flatMap(List::stream).map(SysTableFieldInfoDTO::getFieldNameEn).collect(Collectors.toList());
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

        DaoParam daoParam = new DaoParam();

        daoParam.addTableTypeNameEn(tableTypeNameEn)
                .addTableNameEn(tableNameEn)
                .addExecParamList(execParamList)
                .addEqualCondition(tableNameEnPK, tableNameEnPKValue);

        sysTableDao.updateData(daoParam);
        return queryDataParam;
    }

    @Override
    public void deleteData(QueryDataParamBuilder queryDataParam) throws BusinessException {

    }
}
