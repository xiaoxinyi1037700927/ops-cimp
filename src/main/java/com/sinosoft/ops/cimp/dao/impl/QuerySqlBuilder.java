package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class QuerySqlBuilder implements SqlBuilder {

    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
//        ResultSql resultSql = new ResultSql();
//
//        DaoParam.Page page = daoParam.getPage();
//        int startRow = 0;
//        int showRow = 0;
//        if (page != null) {
//            startRow = page.getStartRow();
//            showRow = page.getShowRow();
//        }
//
//        List<ExecParam> execParamList = daoParam.getExecParamList();
//        List<Conditions> conditionsList = daoParam.getConditionsList();
//        List<Sorting> sortingList = daoParam.getSortingList();
//
//        String entityName = daoParam.getTableTypeNameEn();
//        String prjCode = daoParam.getPrjCode();
//        String entityGroup = daoParam.getTableNameEn();
//
//        //组下所有属性集合
//        SysEntityInfo sysEntityInfo = EntityManager.getInstance().getEntity(entityName, prjCode);
//        String saveTable = sysEntityInfo.getEntityAttrGroupNameAndSaveTableMap(entityGroup);
//        List<SysEntityAttrDef> sysEntityAttrDefs = sysEntityInfo.getEntityAttrGroupMap().get(entityGroup);
//        Map<String, List<SysEntityAttrDef>> attrNameEnMap = sysEntityAttrDefs.stream().collect(Collectors.groupingBy(SysEntityAttrDef::getEntityAttrNameEn));
//
//        List<String> selectField = execParamList.stream().map(ExecParam::getFieldName).collect(Collectors.toList());
//        StringBuilder querySqlBuilder = new StringBuilder("SELECT ");
//
//        StringBuilder selectColumn = new StringBuilder();
//        for (String s : selectField) {
//            List<SysEntityAttrDef> sysEntityAttrDefs1 = attrNameEnMap.get(s);
//            if (sysEntityAttrDefs1.size() > 0) {
//                SysEntityAttrDef sysEntityAttrDef = sysEntityAttrDefs1.get(0);
//                String entitySaveField = sysEntityAttrDef.getEntitySaveField();
//                selectColumn
//                        .append(", ")
//                        .append(entitySaveField)
//                        .append(" AS ")
//                        .append("\"").append(s).append("\"");
//            }
//        }
//        if (selectColumn.length() > 0) {
//            querySqlBuilder.append(selectColumn.substring(1));
//            if (StringUtils.isNotEmpty(saveTable)) {
//                querySqlBuilder.append(" FROM ").append(saveTable);
//            }
//        }
//
//        StringBuilder conditionColumn = new StringBuilder(" WHERE 1=1 ");
//        if (conditionsList.size() > 0) {
//            for (Conditions conditions : conditionsList) {
//                String conditionName = conditions.getConditionName();
//                String condition = conditions.getCondition();
//                Object conditionValue = conditions.getConditionValue();
//
//                List<SysEntityAttrDef> sysEntityAttrDefs1 = attrNameEnMap.get(conditionName);
//                if (sysEntityAttrDefs1 != null && sysEntityAttrDefs1.size() > 0) {
//                    SysEntityAttrDef sysEntityAttrDef = sysEntityAttrDefs1.get(0);
//                    String entitySaveField = sysEntityAttrDef.getEntitySaveField();
//                    String entitySaveType = sysEntityAttrDef.getEntitySaveType();
//                    Object o = this.convertValue(conditionValue, entitySaveType);
//                    conditionColumn
//                            .append(" AND ")
//                            .append(entitySaveField)
//                            .append(" ")
//                            .append(condition)
//                            .append(" ")
//                            .append("'")
//                            .append(o)
//                            .append("'");
//                }
//            }
//        }
//        querySqlBuilder.append(conditionColumn);
//
//        StringBuilder sortColumn = new StringBuilder();
//
//        if (sortingList != null && sortingList.size() > 0) {
//            sortColumn.append(" ORDER BY ");
//            for (Sorting sorting : sortingList) {
//                String sortName = sorting.getSortName();
//                String sortValue = sorting.getSortValue();
//
//                List<SysEntityAttrDef> sysEntityAttrDefs1 = attrNameEnMap.get(sortName);
//                if (sysEntityAttrDefs1.size() > 0) {
//                    SysEntityAttrDef sysEntityAttrDef = sysEntityAttrDefs1.get(0);
//                    String entitySaveField = sysEntityAttrDef.getEntitySaveField();
//                    sortColumn.append(entitySaveField)
//                            .append(" ")
//                            .append(sortValue);
//                }
//            }
//        }
//
//        if (sortColumn.length() > 0) {
//            querySqlBuilder.append(sortColumn);
//        }
//
//        if ((startRow >= 0 && showRow > 0)) {
//            int endRow = (startRow == 0 ? 1 : showRow) * showRow;
//            String sql = this.addPager(querySqlBuilder.toString(), showRow, endRow);
//            resultSql.setSql(sql);
//        } else {
//            resultSql.setSql(querySqlBuilder.toString());
//        }
//        return resultSql;
//    }
//
//    private String addPager(String sql, int startRow, int endRow) {
//        String pageSqlPrefix = "SELECT * FROM ( SELECT PAGE_DATA.*, ROWNUM RN FROM ( ";
//        String pageSqlSuffixTemplate = ") PAGE_DATA WHERE ROWNUM <= %s ) WHERE RN >= %s";
//        String pageSqlSuffix = String.format(pageSqlSuffixTemplate, endRow, startRow);
//        sql = pageSqlPrefix + sql + pageSqlSuffix;
//        return sql;
//    }
        return null;
    }
}
