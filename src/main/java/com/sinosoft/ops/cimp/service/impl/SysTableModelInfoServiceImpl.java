package com.sinosoft.ops.cimp.service.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SysTableDao;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ExecParam;
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
        return null;
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
