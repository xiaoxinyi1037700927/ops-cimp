package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ExecParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InsertSqlBuilder implements SqlBuilder {

    private final SysTableInfoDao sysTableInfoDao;

    @Autowired
    public InsertSqlBuilder(SysTableInfoDao sysTableInfoDao) {
        this.sysTableInfoDao = sysTableInfoDao;
    }

    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
        ResultSql resultSql = new ResultSql();

        //获取表类型名称
        String tableTypeNameEn = daoParam.getTableTypeNameEn();
        //获取信息集名称
        String tableNameEn = daoParam.getTableNameEn();

        //执行参数和数据
        List<ExecParam> execParamList = daoParam.getExecParamList();
        //获取表结构
        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn);

        List<String> execTableField = execParamList.stream().map(ExecParam::getFieldName).collect(Collectors.toList());

        //注意【List<List<String>>内List 0，1，2 分别为 属性英文名，数据库存储字段名称，数据库存储类型】
        List<List<String>> tableFieldList = tableInfo.getTableNameEnAndFieldMap().get(tableNameEn);

        Map<String, String> selectField = this.selectField(execTableField, tableFieldList);

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
        String saveTableName = tableInfo.getTableNameEnAndSaveTableMap().get(tableNameEn);
        if (StringUtils.isEmpty(saveTableName)) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "添加信息集数据主集表必须存在");
        }
        sqlBuilder.append(saveTableName);

        Map<String, List<ExecParam>> execParams = execParamList.stream().collect(Collectors.groupingBy(ExecParam::getFieldName));

        Object[] paramData = new Object[execTableField.size()];
        //SELECT xx
        StringBuilder insertField = new StringBuilder("(");
        int i = 0;
        for (Map.Entry<String, String> entry : selectField.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            insertField.append(value)
                    .append(",");
            List<ExecParam> params = execParams.get(key);
            if (params.size() > 0) {
                paramData[i] = params.get(0).getFieldValue();
            } else {
                paramData[i] = null;
            }
            i++;
        }
        int length = insertField.length();
        if (length > 1) {
            insertField = new StringBuilder(insertField.substring(0, length - 1)).append(")");
        }

        //占位符 VALUES (?,?,?)
        String placeholder = execTableField.stream().map(s -> "?").collect(Collectors.joining(",", "(", ")"));

        sqlBuilder.append(insertField)
                .append(" VALUES ")
                .append(placeholder);


        resultSql.setSql(sqlBuilder.toString());

        resultSql.setData(paramData);

        return resultSql;
    }
}
