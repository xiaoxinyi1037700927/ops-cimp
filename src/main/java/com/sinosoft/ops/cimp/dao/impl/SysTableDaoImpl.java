package com.sinosoft.ops.cimp.dao.impl;

import com.sinosoft.ops.cimp.dao.SqlBuilder;
import com.sinosoft.ops.cimp.dao.SysTableDao;
import com.sinosoft.ops.cimp.dao.domain.DaoParam;
import com.sinosoft.ops.cimp.dao.domain.ResultParam;
import com.sinosoft.ops.cimp.dao.domain.ResultSql;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.vip.vjtools.vjkit.base.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SysTableDaoImpl implements SysTableDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysTableDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final SqlBuilder insertSqlBuilder;
    private final SqlBuilder updateSqlBuilder;
    private final SqlBuilder deleteSqlBuilder;
    private final SqlBuilder querySqlBuilder;

    @Autowired
    public SysTableDaoImpl(JdbcTemplate jdbcTemplate, SqlBuilder insertSqlBuilder, SqlBuilder updateSqlBuilder, SqlBuilder deleteSqlBuilder, SqlBuilder querySqlBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertSqlBuilder = insertSqlBuilder;
        this.updateSqlBuilder = updateSqlBuilder;
        this.deleteSqlBuilder = deleteSqlBuilder;
        this.querySqlBuilder = querySqlBuilder;
    }

    @Override
    public int insertData(DaoParam daoParam) throws BusinessException {
        ResultSql executeSql = insertSqlBuilder.getExecuteSql(daoParam);
        String sql = executeSql.getSql();
        Object[] data = executeSql.getData();

        if (StringUtils.isNotEmpty(sql)) {
            jdbcTemplate.update(sql, data);
//            jdbcTemplate.execute(sql);
            LOGGER.debug("执行sql为：" + sql);
            LOGGER.debug("执行sql的参数为：" + ObjectUtil.toPrettyString(data));
        }
        return 0;
    }

    @Override
    public void updateData(DaoParam daoParam) throws BusinessException {

    }

    @Override
    public void deleteData(DaoParam daoParam) throws BusinessException {

    }

    @Override
    public ResultParam queryPageData(DaoParam daoParam) throws BusinessException {
        return null;
    }
//
//    @Override
//    public void updateData(DaoParam daoParam) throws BusinessException {
//        String entityName = daoParam.getTableTypeNameEn();
//        String prjCode = daoParam.getPrjCode();
//        String entityGroup = daoParam.getTableNameEn();
//
//        SysEntityInfo sysEntityInfo = EntityManager.getInstance().getEntity(entityName, prjCode);
//        //如果实体名称和项目编号不能获取到实体则抛出异常
//        if (sysEntityInfo == null) {
//            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, entityName + prjCode);
//        }
//        Map<String, List<SysEntityAttrDef>> entityAttrGroupMap = sysEntityInfo.getEntityAttrGroupMap();
//        List<SysEntityAttrDef> sysEntityAttrDefs = entityAttrGroupMap.get(entityGroup);
//        if (sysEntityAttrDefs.size() == 0) {
//            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100203, entityName + entityGroup);
//        }
//
//        ResultSql executeSql = updateSqlBuilder.getExecuteSql(daoParam);
//        String sql = executeSql.getSql();
//
//        if (StringUtils.isNotEmpty(sql)) {
//            //执行sql update语句
//            jdbcTemplate.execute(sql);
//            LOGGER.debug("执行sql为：" + sql);
//        }
//
//    }
//
//    @Override
//    public void deleteData(DaoParam daoParam) throws BusinessException {
//        String entityName = daoParam.getTableTypeNameEn();
//        String prjCode = daoParam.getPrjCode();
//        //获取sysEntityInfo实例
//        SysEntityInfo sysEntityInfo = EntityManager.getInstance().getEntity(entityName, prjCode);
//        //如果实体名称和项目编号不能获取到实体则抛出异常
//        if (sysEntityInfo == null) {
//            throw new SystemException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE_100202, entityName + prjCode);
//        }
//        ResultSql executeSql = deleteSqlBuilder.getExecuteSql(daoParam);
//        String sql = executeSql.getSql();
//        if (StringUtils.isNotEmpty(sql)) {
//            jdbcTemplate.execute(sql);
//            LOGGER.debug("执行sql语句为：" + sql);
//        }
//    }
//
//    @Override
//    public ResultParam queryPageData(DaoParam daoParam) throws BusinessException {
//        ResultParam resultParam = new ResultParam();
//        String entityName = daoParam.getTableTypeNameEn();
//        String prjCode = daoParam.getPrjCode();
//        String entityGroup = daoParam.getTableNameEn();
//
//        SysEntityInfo entityInfo = EntityManager.getInstance().getEntity(entityName, prjCode);
//        Optional<SysEntityGroupDef> entityGroupDef = entityInfo.getSysEntityGroupDefList().stream().filter(g -> StringUtils.equals(g.getEntityGroupNameEn(), entityGroup)).findFirst();
//
//        final boolean[] isMultiData = {false};
//        if (entityGroupDef.isPresent()) {
//            SysEntityGroupDef sysEntityGroupDef = entityGroupDef.get();
//            if (StringUtils.isNotEmpty(sysEntityGroupDef.getEntityGroupParentName())) {
//                isMultiData[0] = true;
//            }
//        }
//        daoParam.addParam("isMultiData", isMultiData[0]);
//        ResultSql executeSql = querySqlBuilder.getExecuteSql(daoParam);
//
//        String sql = executeSql.getSql();
//
//        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
//
//        Object[] singleData = new Object[0];
//        Object[][] multiData = new Object[0][];
//        //如果有返回结果
//        if (mapList.size() > 0) {
//            Map<String, Object> indexRow = mapList.get(0);
//            ArrayList<String> attrList = Lists.newArrayList(indexRow.keySet());
//            Map<String, List<SysEntityAttrDef>> attrMap = entityInfo.getEntityAttrGroupMap().getOrDefault(entityGroup, Lists.newArrayList()).stream().collect(Collectors.groupingBy(SysEntityAttrDef::getEntityAttrNameEn));
//            Map<String, String> attrSaveTypeMap = Maps.newHashMap();
//
//            for (String s : attrList) {
//                SysEntityAttrDef sysEntityAttrDef = attrMap.get(s).get(0);
//                if (sysEntityAttrDef != null) {
//                    String entityAttrNameEn = sysEntityAttrDef.getEntityAttrNameEn();
//                    String entitySaveType = sysEntityAttrDef.getEntitySaveType();
//                    attrSaveTypeMap.put(entityAttrNameEn, entitySaveType);
//                }
//            }
//
//            if (isMultiData[0]) {
//                int rowCount = mapList.size();
//                int columnCount = mapList.get(0).keySet().size();
//                multiData = new Object[rowCount][columnCount];
//                resultParam.setValueType(ResultParam.ValueType.MORE);
//            } else {
//                int rowCount = attrList.size();
//                singleData = new Object[rowCount];
//                resultParam.setValueType(ResultParam.ValueType.ONE);
//            }
//            if (isMultiData[0]) {
//                for (int i = 0; i < mapList.size(); i++) {
//                    Map<String, Object> map = mapList.get(i);
//                    for (int j = 0; j < map.size(); j++) {
//                        int k = 0;
//                        for (Map.Entry<String, String> entry : attrSaveTypeMap.entrySet()) {
//                            String key = entry.getKey();
//                            String type = entry.getValue();
//                            Object data = map.get(key);
//                            multiData[i][k] = querySqlBuilder.convertValue(data, type);
//                            k++;
//                        }
//                    }
//                }
//                resultParam.setValueLists(multiData);
//            } else {
//                int i = 0;
//                for (Map.Entry<String, Object> entry : indexRow.entrySet()) {
//                    String key = entry.getKey();
//                    Object value = entry.getValue();
//                    String type = attrSaveTypeMap.get(key);
//                    singleData[i] = querySqlBuilder.convertValue(value, type);
//                    i++;
//                }
//                resultParam.setValues(singleData);
//            }
//        }
//        return resultParam;


//        return jdbcTemplate.query(sql, new ResultSetExtractor<ResultParam>() {
//            int count = 0;
//
//            @Override
//            public ResultParam extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//                boolean hasNext = resultSet.next();
//                ResultSetMetaData metaData = resultSet.getMetaData();
//
//                int columnCount = metaData.getColumnCount();
//                List<String> columnNames = new ArrayList<>(columnCount);
//                Object[] singleData = new Object[columnCount];
//                Object[][] multiData = new Object[resultSet.getRow()][columnCount];
//
//                if (hasNext) {
//                    for (int i = 0; i < columnCount; i++) {
//                        String columnName = metaData.getColumnName(i + 1);
//                        columnNames.add(i, columnName);
//                        String columnType = metaData.getColumnTypeName(i + 1);
//
//                        Object object = resultSet.getObject(i + 1);
//                        try {
//                            Object o = querySqlBuilder.convertValue(object, columnType);
//                            singleData[i] = o;
//                            multiData[count][i] = o;
//                        } catch (BusinessException e) {
//                            LOGGER.error("数据库类型转化失败" + columnType);
//                            throw new RuntimeException("数据库类型转化失败" + columnType);
//                        }
//                    }
//                    count++;
//                }
//
//                resultParam.setAttrs(columnNames);
//                if (isMultiData[0]) {
//                    resultParam.setValueType(ResultParam.ValueType.MORE);
//                    resultParam.setValueLists(multiData);
//                } else {
//                    resultParam.setValues(singleData);
//                    resultParam.setValueType(ResultParam.ValueType.ONE);
//                }
//                return resultParam;
//            }
//        });
}

