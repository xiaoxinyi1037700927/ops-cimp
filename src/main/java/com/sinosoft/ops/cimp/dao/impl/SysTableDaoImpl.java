package com.sinosoft.ops.cimp.dao.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.SysTableDao;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.vip.vjtools.vjkit.base.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SysTableDaoImpl implements SysTableDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysTableDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final SqlBuilder insertSqlBuilder;
    private final SqlBuilder updateSqlBuilder;
    private final SqlBuilder deleteSqlBuilder;
    private final SqlBuilder querySqlBuilder;
    private final SysTableInfoDao sysTableInfoDao;

    @Autowired
    public SysTableDaoImpl(JdbcTemplate jdbcTemplate, SqlBuilder insertSqlBuilder, SqlBuilder updateSqlBuilder, SqlBuilder deleteSqlBuilder, SqlBuilder querySqlBuilder, SysTableInfoDao sysTableInfoDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertSqlBuilder = insertSqlBuilder;
        this.updateSqlBuilder = updateSqlBuilder;
        this.deleteSqlBuilder = deleteSqlBuilder;
        this.querySqlBuilder = querySqlBuilder;
        this.sysTableInfoDao = sysTableInfoDao;
    }

    @Override
    public int insertData(DaoParam daoParam) throws BusinessException {
        ResultSql executeSql = insertSqlBuilder.getExecuteSql(daoParam);
        String sql = executeSql.getSql();
        Object[] data = executeSql.getData();

        if (StringUtils.isNotEmpty(sql)) {
            jdbcTemplate.update(sql, data);
            LOGGER.debug("执行sql为：" + sql);
            LOGGER.debug("执行sql的参数为：" + ObjectUtil.toPrettyString(data));
        }
        return 0;
    }

    @Override
    public void updateData(DaoParam daoParam) throws BusinessException {
        ResultSql executeSql = updateSqlBuilder.getExecuteSql(daoParam);
        String sql = executeSql.getSql();
        Object[] data = executeSql.getData();

        if (StringUtils.isNotEmpty(sql)) {
            jdbcTemplate.update(sql, data);
            LOGGER.debug("执行sql为：" + sql);
            LOGGER.debug("执行sql的参数为：" + ObjectUtil.toPrettyString(data));
        }
    }

    @Override
    public void deleteData(DaoParam daoParam) throws BusinessException {
        ResultSql executeSql = deleteSqlBuilder.getExecuteSql(daoParam);
        String sql = executeSql.getSql();

        if (StringUtils.isNotEmpty(sql)) {
            jdbcTemplate.execute(sql);
            LOGGER.debug("执行sql为：" + sql);
        }
    }

    @Override
    public ResultParam queryPageData(DaoParam daoParam) throws BusinessException {
        ResultParam resultParam = new ResultParam();

        String tableTypeNameEn = daoParam.getTableTypeNameEn();
        String tableNameEn = daoParam.getTableNameEn();
        boolean isMultiData = (Boolean) daoParam.getParamByKey("isMultiData");

        SysTableModelInfo tableInfo = sysTableInfoDao.getTableInfo(tableTypeNameEn);
        List<List<String>> fieldNameAndTypeList = tableInfo.getTableNameEnAndFieldMap().getOrDefault(tableNameEn, Lists.newArrayList());
        Map<String, String> fieldNameAndTypeMap = Maps.newHashMap();
        for (List<String> field : fieldNameAndTypeList) {
            String fieldName = field.get(0);
            String fieldDbType = field.get(2);
            fieldNameAndTypeMap.put(fieldName, fieldDbType);
        }

        ResultSql executeSql = querySqlBuilder.getExecuteSql(daoParam);

        String sql = executeSql.getSql();
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

        Object[] singleData = new Object[0];
        Object[][] multiData = new Object[0][];
        //如果有返回结果
        if (mapList.size() > 0) {
            Map<String, Object> indexRow = mapList.get(0);
            List<String> attrList = Lists.newArrayList(indexRow.keySet());
            if (isMultiData) {
                int rowCount = mapList.size();
                int columnCount = mapList.get(0).keySet().size();
                multiData = new Object[rowCount][columnCount];
                resultParam.setValueType(ResultParam.ValueType.MORE);
            } else {
                int rowCount = attrList.size();
                singleData = new Object[rowCount];
                resultParam.setValueType(ResultParam.ValueType.ONE);
            }
            if (isMultiData) {
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> map = mapList.get(i);
                    for (int j = 0; j < map.size(); j++) {
                        for (int k = 0; k < attrList.size(); k++) {
                            String s = attrList.get(k);
                            String type = fieldNameAndTypeMap.get(s);
                            Object data = map.get(s);
                            multiData[i][k] = querySqlBuilder.convertValue(data, type);
                        }
                    }
                }
                resultParam.setValueLists(multiData);
            } else {
                int i = 0;
                for (Map.Entry<String, Object> entry : indexRow.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    String type = fieldNameAndTypeMap.get(key);
                    singleData[i] = querySqlBuilder.convertValue(value, type);
                    i++;
                }
                resultParam.setValues(singleData);
            }
        }
        return resultParam;
    }
}

