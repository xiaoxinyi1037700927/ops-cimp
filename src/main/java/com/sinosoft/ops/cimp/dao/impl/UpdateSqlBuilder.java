package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class UpdateSqlBuilder implements SqlBuilder {

    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
//        ResultSql resultSql = new ResultSql();
//
//        String entityName = daoParam.getTableTypeNameEn();
//        String prjCode = daoParam.getPrjCode();
//        String entityGroup = daoParam.getTableNameEn();
//
//        SysEntityInfo entityInfo = EntityManager.getInstance().getEntity(entityName, prjCode);
//        String saveTableName = entityInfo.getEntityAttrGroupNameAndSaveTableMap(entityGroup);
//        if (StringUtils.isEmpty(saveTableName)) {
//            throw new BusinessException(OpsErrorMessage.MODULE_NAME, "修改属性信息必须传递表名");
//        }
//        SysEntityGroupDef sysEntityGroupDef = entityInfo.getGroupMap().get(entityGroup);
//        List<SysEntityAttrDef> sysEntityAttrDefs = entityInfo.getEntityAttrGroupMap().get(entityGroup);
//
//        //属性只有一个存储表（属性组下的属性都在同一张表中）
//        if (sysEntityGroupDef != null) {
//            List<ExecParam> execParamList = daoParam.getExecParamList();
//            List<Conditions> conditionsList = daoParam.getConditionsList();
//
//            Map<String, Object> sqlParam = this.prepareSqlParam(execParamList, sysEntityAttrDefs);
//
//            //拼接成 UPDATE TABLE_NAME SET NAME="" WHERE ID="" 形式
//            StringBuilder sqlBuild = new StringBuilder("UPDATE ");
//            sqlBuild.append(saveTableName).append(" SET ");
//
//            StringBuilder modifyColumn = new StringBuilder();
//            for (Map.Entry<String, Object> execParam : sqlParam.entrySet()) {
//                String fieldName = execParam.getKey();
//                Object fieldValue = execParam.getValue();
//                modifyColumn.append(" \"")
//                        .append(fieldName)
//                        .append("\"")
//                        .append(" = ")
//                        .append("\'")
//                        .append(fieldValue)
//                        .append("\'")
//                        .append(",");
//            }
//            if (modifyColumn.length() > 0) {
//                sqlBuild.append(modifyColumn.substring(0, modifyColumn.length() - 1));
//            }
//            sqlBuild.append(" WHERE ");
//            if (conditionsList != null && conditionsList.size() > 0) {
//                Conditions conditions = conditionsList.get(0);
//                String conditionName = conditions.getConditionName();
//                Object conditionValue = conditions.getConditionValue();
//                String condition = conditions.getCondition();
//                sqlBuild.append("\"")
//                        .append(conditionName)
//                        .append("\"")
//                        .append(condition)
//                        .append("\'")
//                        .append(conditionValue)
//                        .append("\'");
//            } else {
//                throw new BusinessException(OpsErrorMessage.MODULE_NAME, "更新实体信息必须传递条件");
//            }
//            resultSql.setData(sqlParam.values().toArray());
//            resultSql.setSql(sqlBuild.toString());
//        }


        return null;
//        return resultSql;
    }
}
