package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.Conditions;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteSqlBuilder implements SqlBuilder {

    private final SysTableInfoDao sysTableInfoDao;

    @Autowired
    public DeleteSqlBuilder(SysTableInfoDao sysTableInfoDao) {
        this.sysTableInfoDao = sysTableInfoDao;
    }

    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
        ResultSql resultSql = new ResultSql();

        String tableTypeNameEn = daoParam.getTableTypeNameEn();
        String tableNameEn = daoParam.getTableNameEn();
        List<Conditions> conditionsList = daoParam.getConditionsList();
        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn);

        StringBuilder deleteSqlBuilder = new StringBuilder("DELETE FROM ");
        String saveTableName = tableInfo.getTableNameEnAndSaveTableMap().get(tableNameEn);
        if (StringUtils.isEmpty(saveTableName)) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, "删除属性信息必须传递表名");
        }

        deleteSqlBuilder.append(saveTableName);

        StringBuilder conditionSqlBuilder = new StringBuilder();
        if (conditionsList != null && conditionsList.size() > 0) {
            StringBuilder conditionColumn = new StringBuilder(" WHERE 1=1 ");
            for (Conditions conditions : conditionsList) {
                String conditionName = conditions.getConditionName();
                String condition = conditions.getCondition();
                Object conditionValue = conditions.getConditionValue();

                if (condition.equals(Conditions.ConditionsEnum.IN)) {
                    conditionSqlBuilder.append(" AND ")
                            .append(conditionName)
                            .append(" ")
                            .append(condition)
                            .append(" ")
                            .append("(")
                            .append(conditionValue)
                            .append(")");
                } else {
                    conditionSqlBuilder.append(" AND ")
                            .append(conditionName)
                            .append(" ")
                            .append(condition)
                            .append(" ")
                            .append("'")
                            .append(conditionValue)
                            .append("'");
                }

            }

        } else {
            conditionSqlBuilder.append(" WHERE 1=2 ");
        }
        deleteSqlBuilder.append(conditionSqlBuilder);

        resultSql.setSql(deleteSqlBuilder.toString());
        return  resultSql;
    }
}
