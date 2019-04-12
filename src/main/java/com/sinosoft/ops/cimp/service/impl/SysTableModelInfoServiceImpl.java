package com.sinosoft.ops.cimp.service.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SysTableDao;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ExecParam;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.QueryDataParamBuilder;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.SysTableModelInfoService;
import com.sinosoft.ops.cimp.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
        Map<String, Object> saveOrUpdateFormData = queryDataParam.getSaveOrUpdateFormData();

        //获取系统表结构信息
        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn);
        if (tableInfo == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, tableTypeNameEn);
        }
        //TODO 获取app定义信息对属性进行过滤
        List<String> sysTableFieldList = tableInfo.getTableNameEnAndFieldNameListMap().get(tableNameEn);
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

        ExecParam primaryKeyExecParam = new ExecParam(primaryKey, IdUtil.uuidWithoutMinus());
        execParamList.add(primaryKeyExecParam);
        DaoParam daoParam = new DaoParam();
        daoParam.addEntityName(tableTypeNameEn)
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
        return null;
    }

    @Override
    public void deleteData(QueryDataParamBuilder queryDataParam) throws BusinessException {

    }
}
