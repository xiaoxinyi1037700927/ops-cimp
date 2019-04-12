package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class DeleteSqlBuilder implements SqlBuilder {
    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
//        ResultSql resultSql = new ResultSql();
//
//        String entityName = daoParam.getTableTypeNameEn();
//        String entityGroup = daoParam.getTableNameEn();
//        List<Conditions> conditionsList = daoParam.getConditionsList();
//
//        SysEntityInfo entityInfo = EntityManager.getInstance().getEntity(entityName, prjCode);
//        if (entityInfo == null) {
//            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请确认删除的实体和项目编号存在");
//        }
//        SysEntityGroupDef sysEntityGroupDef = entityInfo.getGroupMap().get(entityGroup);
//        if (sysEntityGroupDef == null) {
//            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "删除信息必须指定分组");
//        }
//
//        StringBuilder deleteSqlBuilder = new StringBuilder();
//        StringBuilder logicDeleteSqlBuilder = new StringBuilder();
//
//        //如果父组名为空则认为是主集，否则为子集，主集使用逻辑删除，子集使用物理删除
//        if (StringUtils.isEmpty(sysEntityGroupDef.getEntityGroupParentName())) {
//            String tableName = sysEntityGroupDef.getEntityGroupTableName();
//            if (conditionsList == null || conditionsList.size() == 0) {
//                throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "删除信息必须传递主键条件");
//            }
//
//            String logicalDeleteAttrSaveField = entityInfo.getLogicalDeleteAttrSaveField();
//
//
//            logicDeleteSqlBuilder.append("UPDATE ").append(tableName)
//                    .append(" SET ")
//                    .append(logicalDeleteAttrSaveField)
//                    .append(" = ")
//                    .append("'1'");
//            resultSql.setSql(logicDeleteSqlBuilder.toString());
//        } else {
//            String tableName = sysEntityGroupDef.getEntityGroupTableName();
//            if (conditionsList == null || conditionsList.size() == 0) {
//                throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "删除信息必须传递主键条件");
//            }
//            Conditions conditions = conditionsList.get(0);
//            String conditionName = conditions.getConditionName();
//            Object conditionValue = conditions.getConditionValue();
//            String condition = conditions.getCondition();
//
//            deleteSqlBuilder.append("DELETE FROM ")
//                    .append(tableName)
//                    .append(" WHERE ")
//                    .append(conditionName)
//                    .append(" ")
//                    .append(condition)
//                    .append(" ")
//                    .append("'")
//                    .append(conditionValue)
//                    .append("'");
//            resultSql.setSql(deleteSqlBuilder.toString());
//        }
//        return resultSql;
        return null;
    }
}
