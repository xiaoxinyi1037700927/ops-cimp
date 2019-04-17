package com.sinosoft.ops.cimp.dao.impl;

import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.Conditions;
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
public class UpdateSqlBuilder implements SqlBuilder {

    private final SysTableInfoDao sysTableInfoDao;

    @Autowired
    public UpdateSqlBuilder(SysTableInfoDao sysTableInfoDao) {
        this.sysTableInfoDao = sysTableInfoDao;
    }

    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
        ResultSql resultSql = new ResultSql();

        String tableTypeNameEn = daoParam.getTableTypeNameEn();
        String tableNameEn = daoParam.getTableNameEn();
        List<Conditions> conditionsList = daoParam.getConditionsList();

        List<ExecParam> execParamList = daoParam.getExecParamList();

        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn);
        List<String> execTableField = execParamList.stream().map(ExecParam::getFieldName).collect(Collectors.toList());
        Map<String, Object> execMap = Maps.newHashMap();
        execParamList.forEach(e -> execMap.put(e.getFieldName(), e.getFieldValue()));


        //注意【List<List<String>>内List 0，1，2 分别为 属性英文名，数据库存储字段名称，数据库存储类型】
        List<List<String>> tableFieldList = tableInfo.getTableNameEnAndFieldMap().get(tableNameEn);
        Map<String, String> tableFieldMap = Maps.newHashMap();
        tableInfo.getTableFields().forEach(f -> tableFieldMap.put(f.getNameEn(), f.getDbFieldName()));

        Map<String, String> selectField = this.selectField(execTableField, tableFieldList);

        StringBuilder sqlBuilder = new StringBuilder("UPDATE ");

        String saveTableName = tableInfo.getTableNameEnAndSaveTableMap().get(tableNameEn);

        if (StringUtils.isEmpty(saveTableName)) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, "修改属性信息必须传递表名");
        }
        sqlBuilder.append(saveTableName);
        sqlBuilder.append(" SET ");

        StringBuilder setBuilder = new StringBuilder();
        Object[] params = new Object[selectField.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : selectField.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Object o = execMap.get(key);
            params[i] = o;
            setBuilder.append(value)
                    .append(" = ")
                    .append(" ? ")
                    .append(",");
            i++;
        }
        if (setBuilder.length() > 0) {
            setBuilder = new StringBuilder(setBuilder.subSequence(0, setBuilder.length() - 1));
        }

        sqlBuilder.append(setBuilder);

        StringBuilder conditionSqlBuilder = new StringBuilder();
        if (conditionsList != null && conditionsList.size() > 0) {
            Conditions conditions = conditionsList.get(0);
            String condition = conditions.getCondition();
            String conditionName = conditions.getConditionName();
            Object conditionValue = conditions.getConditionValue();
            String saveField = tableFieldMap.get(conditionName);
            if (StringUtils.isNotEmpty(saveField)) {
                conditionSqlBuilder.append(" WHERE 1=1 ");
                if(condition.equals(Conditions.ConditionsEnum.IN))
                {
                    conditionSqlBuilder.append(" AND ")
                            .append(conditionName)
                            .append(" ")
                            .append(condition)
                            .append(" ")
                            .append("(")
                            .append(conditionValue)
                            .append(")");
                }
                else
                {
                    conditionSqlBuilder.append(" AND ")
                            .append(conditionName)
                            .append(" ")
                            .append(condition)
                            .append(" ")
                            .append("'")
                            .append(conditionValue)
                            .append("'");
                }

            } else {
                conditionSqlBuilder.append(" WHERE 1=2 ");
            }
        } else {
            conditionSqlBuilder.append(" WHERE 1=2 ");
        }
        sqlBuilder.append(conditionSqlBuilder);

        resultSql.setSql(sqlBuilder.toString());

        resultSql.setData(params);

        //属性只有一个存储表（属性组下的属性都在同一张表中）
//        if (sysEntityGroupDef != null) {
////            List<ExecParam> execParamList = daoParam.getExecParamList();
////            List<Conditions> conditionsList = daoParam.getConditionsList();
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
        return resultSql;
    }
}
