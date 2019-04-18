package com.sinosoft.ops.cimp.util;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.entity.sys.systable.SysTableField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DDLUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DDLUtil.class);

    /**
     * 校验表是否存在使用表模式
     */
    private static final String TABLE_TYPE = "TABLE";

    @Value(value = "${spring.jpa.properties.hibernate.default_schema}")
    public static String databaseSchema;

    /***
     * 创建表或添加列
     * @return 返回是否成功
     */
    public static void getCreateTableModel(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {

        String sql = getSql(connection, tableName, sysTableFields);
        if (sql == null) {
            return;
        }

        System.out.println("拼接后SQL：" + sql);

        Statement myStatement = connection.createStatement();

        myStatement.executeUpdate(sql);
        //添加列描述
        addColumnDesc(connection, tableName, sysTableFields);
    }

    private static String getSql(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {

        //判断表是否存在
        boolean isExist = checkTableExist(connection, tableName);

        //如果表不存在
        if (!isExist) {
            String sql = createTable(tableName, sysTableFields);
            return sql;
        }

        List<SysTableField> adSysTableFields = check(connection, tableName, sysTableFields);
        for (int i = 0; i < sysTableFields.size(); i++) {
            if (sysTableFields.get(i).getDbFieldName() == null) {
                sysTableFields.remove(i);
            }
        }
        if (adSysTableFields.size() == 0) {
            return null;
        }
        String sql = createColumn(tableName, adSysTableFields);
        return sql;
    }

    private static List<SysTableField> check(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        //修改类型
        List<SysTableField> upSysTableFieldType = getColumnType(connection, tableName, sysTableFields);
        updateColumnTpye(connection, tableName, upSysTableFieldType);

        //修改描述
        List<SysTableField> upSysTableFieldDesc = getColumnDesc(connection, tableName, sysTableFields);
        updateColumnDesc(connection, tableName, upSysTableFieldDesc);


        //需要进行添加的
        List<SysTableField> adSysEntityAttrDefs = addColumn(connection, tableName, sysTableFields);
        return adSysEntityAttrDefs;
    }


    /***
     * 获取需要添加的列
     */
    private static List<SysTableField> addColumn(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        List<String> columnNames = getColumnName(connection, tableName);
        for (String columnName : columnNames) {
            for (int n = 0; n < sysTableFields.size(); n++) {
                if (columnName.equals(sysTableFields.get(n).getDbFieldName())) {
                    sysTableFields.remove(n);
                }
            }
        }
        return sysTableFields;
    }


    /***
     * 在已存在表中新增列
     * @return sql语句
     */
    private static String createColumn(String tableName, List<SysTableField> sysTableFields) {
        StringBuilder sql = new StringBuilder("alter systable " + tableName + " add (");
        for (int i = 0; i < sysTableFields.size(); i++) {
            sql.append(" ");
            sql.append(sysTableFields.get(i).getDbFieldName());
            sql.append(" ");
            sql.append(sysTableFields.get(i).getDbFieldDataType());
            if (i == sysTableFields.size() - 1) {
                break;
            }
            sql.append(",");
        }
        sql.append(")");
        return sql.toString();
    }

    /***
     * 创建表 以及表下的列
     * @return sql语句
     */
    private static String createTable(String tableName, List<SysTableField> sysTableFields) {
        StringBuilder sql = new StringBuilder();
        sql.append("create systable ");
        sql.append(tableName);
        sql.append(" (");

        for (int i = 0; i < sysTableFields.size(); i++) {
            SysTableField sysTableField = sysTableFields.get(i);
            String saveField = sysTableField.getDbFieldName();
            if (saveField == null) {
                continue;
            }
            String saveType = sysTableField.getDbFieldDataType();
            String isPk = sysTableField.getIsPK();
            sql.append(" ");
            sql.append(saveField);
            sql.append(" ");
            sql.append(saveType);
            sql.append(" ");
            if (isPk != null && isPk.equals("1")) {
                sql.append("primary key");
            }
            if (i == sysTableFields.size() - 1) {
                break;
            }
            sql.append(",");
        }
        sql.append(")");
        return sql.toString();
    }

    /***
     * 获取列名以及类型 用于判断修改列类型
     * 1. 获取数据库列名以及列类型
     * 2. 根据Attr表查出来的数据和表列对应 筛选出有所改变的
     * @param tableName 表名
     * @return 返回列名以及类型
     * @throws SQLException sql
     */
    private static List<SysTableField> getColumnType(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        String sql = String.format("SELECT COLUMN_NAME, DATA_TYPE,DATA_LENGTH FROM ALL_TAB_COLS WHERE TABLE_NAME = '%s'", tableName);
        System.out.println("获取数据类型SQL：" + sql);
        Map<String, String> columnTypeMap = new HashMap<>();

        Statement pst = connection.createStatement();
        ResultSet rs = pst.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString("DATA_TYPE"));
            if (rs.getString("DATA_TYPE").equalsIgnoreCase("date")) {
                columnTypeMap.put(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE"));
                continue;
            }
            columnTypeMap.put(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE") + "(" + rs.getString("data_length") + ")");
        }

        //需要修改的数据库列
        List<SysTableField> sysEntityAttrDefs = Lists.newArrayList();
        for (String key : columnTypeMap.keySet()) {
            for (SysTableField sysEntityAttrDef : sysTableFields) {
                if (key.equals(sysEntityAttrDef.getDbFieldName())) {
                    if (!columnTypeMap.get(key).equals(sysEntityAttrDef.getDbFieldDataType())) {
                        sysEntityAttrDefs.add(sysEntityAttrDef);
                    }
                }
            }
        }
        return sysEntityAttrDefs;
    }

    /***
     * 修改列类型
     * @param sysTableFields 列信息
     */
    private static void updateColumnTpye(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        Statement pst = connection.createStatement();
        StringBuilder updateSql = new StringBuilder();
        for (SysTableField sysTableField : sysTableFields) {
            String columnName = sysTableField.getDbFieldName();
            String columnType = sysTableField.getDbFieldDataType();
            updateSql.append("ALTER TABLE ");
            updateSql.append(" ");
            updateSql.append(tableName);
            updateSql.append(" MODIFY ");
            updateSql.append("(");
            updateSql.append(columnName);
            updateSql.append(" ");
            updateSql.append(columnType);
            updateSql.append(")");
            System.out.println(updateSql.toString());
            pst.executeUpdate(updateSql.toString());
            updateSql.setLength(0);
        }
    }

    private static void updateColumnDesc(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        Statement pst = connection.createStatement();
        StringBuilder updateSql = new StringBuilder();
        StringBuilder sql = new StringBuilder();
        for (SysTableField sysTableField : sysTableFields) {
            String columnName = sysTableField.getDbFieldName();
            String attrDesc = sysTableField.getDescription();
            if (attrDesc != null) {
                sql.append("COMMENT ON COLUMN ");
                sql.append(tableName);
                sql.append(".");
                sql.append(columnName);
                sql.append(" ");
                sql.append("IS");
                sql.append(" '");
                sql.append(attrDesc);
                sql.append("'");
                sql.append(" ");

                Statement statement = connection.createStatement();
                statement.executeUpdate(sql.toString());
                System.out.println("列添加描述SQL：" + sql);
                sql.setLength(0);
            }
        }

    }

    /***
     * 添加列的描述
     * @param sysTableFields 列信息
     */
    private static void addColumnDesc(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        StringBuilder sql = new StringBuilder();
        for (SysTableField sysTableField : sysTableFields) {
            String columnName = sysTableField.getDbFieldName();
            String columnDesc = sysTableField.getDescription();
            if (columnDesc != null && columnName != null) {
                sql.append("COMMENT ON COLUMN ");
                sql.append(tableName);
                sql.append(".");
                sql.append(columnName);
                sql.append(" ");
                sql.append("IS");
                sql.append(" '");
                sql.append(columnDesc);
                sql.append("'");
                sql.append(" ");

                Statement statement = connection.createStatement();
                statement.executeUpdate(sql.toString());
                System.out.println("列添加描述SQL：" + sql);
                sql.setLength(0);
            }
        }
    }

    /***
     * 判断表是否存在
     * @param connection 数据库连接
     * @param tableName 表名
     * @return 返回是否存在
     */
    private static boolean checkTableExist(Connection connection, String tableName) {
        return checkTableExist(connection, tableName, databaseSchema);
    }

    private static boolean checkTableExist(Connection connection, String tableName, String schema) {
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            String[] types = {TABLE_TYPE};
            try (ResultSet resultSet = dbMetaData.getTables(null, schema, tableName, types)) {
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("数据库连接异常：" + e);
        }
        return false;
    }

    /***
     * 获取列名，用于判断列是否存在
     * @param tableName 表名
     * @return
     */
    private static List<String> getColumnName(Connection connection, String tableName) throws SQLException {
        String sql = String.format("select t.column_name from user_col_comments t where t.table_name = '%s'", tableName);
        System.out.println("查询列名SQL为：" + sql);
        List<String> columns = Lists.newArrayList();
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            columns.add(rs.getString("COLUMN_NAME"));
        }
        return columns;
    }

    /***
     * 获取描述，用于判断列描述是否修改
     * @param tableName 表名
     * @return
     */
    private static List<SysTableField> getColumnDesc(Connection connection, String tableName, List<SysTableField> sysTableFields) throws SQLException {
        String sql = String.format("select t.COLUMN_NAME,t.COMMENTS from user_col_comments t where t.table_name = '%s'", tableName);
        System.out.println("查询列名SQL为：" + sql);
        Map<String, String> columnDescs = new HashMap<>();
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            columnDescs.put(rs.getString("COLUMN_NAME"), rs.getString("COMMENTS"));
        }

        List<SysTableField> sysEntityAttrDefs = Lists.newArrayList();
        for (String key : columnDescs.keySet()) {
            for (SysTableField sysTableField : sysEntityAttrDefs) {
                if (key.equals(sysTableField.getDbFieldName())) {
                    if (columnDescs.get(key) == null && sysTableField.getDescription() == null) {
                        continue;
                    }
                    if (!columnDescs.get(key).equals(sysTableField.getDescription())) {
                        sysEntityAttrDefs.add(sysTableField);
                    }
                }
            }
        }

        return sysEntityAttrDefs;
    }


}





