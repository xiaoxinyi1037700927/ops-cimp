package com.sinosoft.ops.cimp.dao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.*;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuerySqlBuilder implements SqlBuilder {

    private final SysTableInfoDao sysTableInfoDao;

    @Autowired
    public QuerySqlBuilder(SysTableInfoDao sysTableInfoDao) {
        this.sysTableInfoDao = sysTableInfoDao;
    }

    @Override
    public ResultSql getExecuteSql(DaoParam daoParam) throws BusinessException {
        ResultSql resultSql = new ResultSql();

        DaoParam.Page page = daoParam.getPage();
        int startRow = 0;
        int showRow = 0;
        if (page != null) {
            startRow = page.getStartRow();
            showRow = page.getShowRow();
        }

        List<ExecParam> execParamList = daoParam.getExecParamList();
        List<Conditions> conditionsList = daoParam.getConditionsList();
        List<Sorting> sortingList = daoParam.getSortingList();

        String tableTypeNameEn = daoParam.getTableTypeNameEn();
        String tableNameEn = daoParam.getTableNameEn();
        String prjCode = daoParam.getPrjCode();

        //组下所有属性集合
        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn);
        Map<String, String> fieldDdNameMap = Maps.newHashMap();
        tableInfo.getTableNameEnAndFieldListMap().getOrDefault(tableNameEn, Lists.newArrayList()).forEach(f -> fieldDdNameMap.put(f.getNameEn(), f.getDbFieldName()));
        String saveTable = tableInfo.getTableNameEnAndSaveTableMap().get(tableNameEn);

        List<String> selectField = execParamList.stream().map(ExecParam::getFieldName).collect(Collectors.toList());
        StringBuilder querySqlBuilder = new StringBuilder("SELECT ");

        StringBuilder selectColumn = new StringBuilder();
        for (String s : selectField) {

            String dbFieldName = fieldDdNameMap.get(s);
            if (StringUtils.isNotEmpty(dbFieldName)) {
                selectColumn
                        .append(", ")
                        .append(dbFieldName)
                        .append(" AS ")
                        .append("\"").append(s).append("\"");
            }
        }
        if (selectColumn.length() > 0) {
            querySqlBuilder.append(selectColumn.substring(1));
            if (StringUtils.isNotEmpty(saveTable)) {
                querySqlBuilder.append(" FROM ").append(saveTable);
            }
        }

        StringBuilder conditionColumn = new StringBuilder(" WHERE 1=1 ");
        if (conditionsList.size() > 0) {
            for (Conditions conditions : conditionsList) {
                String conditionName = conditions.getConditionName();
                String condition = conditions.getCondition();
                Object conditionValue = conditions.getConditionValue();

                String dbFieldName = fieldDdNameMap.get(conditionName);
                if (StringUtils.isNotEmpty(dbFieldName)) {
                    conditionColumn
                            .append(" AND ")
                            .append(dbFieldName)
                            .append(" ")
                            .append(condition)
                            .append(" ")
                            .append("'")
                            .append(conditionValue)
                            .append("'");
                }
            }
        }
        querySqlBuilder.append(conditionColumn);

        StringBuilder sortColumn = new StringBuilder();

        if (sortingList != null && sortingList.size() > 0) {
            sortColumn.append(" ORDER BY ");
            for (Sorting sorting : sortingList) {
                String sortName = sorting.getSortName();
                String sortValue = sorting.getSortValue();

                String dbFieldName = fieldDdNameMap.get(sortName);
                if (StringUtils.isNotEmpty(dbFieldName)) {
                    sortColumn.append(dbFieldName)
                            .append(" ")
                            .append(sortValue);
                }
            }
        }

        if (sortColumn.length() > 10) {
            querySqlBuilder.append(sortColumn);
        }

        if ((startRow >= 0 && showRow > 0)) {
            int endRow = (startRow == 0 ? 1 : showRow) * showRow;
            String sql = this.addPager(querySqlBuilder.toString(), showRow, endRow);
            resultSql.setSql(sql);
        } else {
            resultSql.setSql(querySqlBuilder.toString());
        }
        return resultSql;
    }

    private String addPager(String sql, int startRow, int endRow) {
        String pageSqlPrefix = "SELECT * FROM ( SELECT PAGE_DATA.*, ROWNUM RN FROM ( ";
        String pageSqlSuffixTemplate = ") PAGE_DATA WHERE ROWNUM <= %s ) WHERE RN >= %s";
        String pageSqlSuffix = String.format(pageSqlSuffixTemplate, endRow, startRow);
        sql = pageSqlPrefix + sql + pageSqlSuffix;
        return sql;
    }
}
