package com.sinosoft.ops.cimp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DDLUtil.class);

    /**
     * 校验表是否存在使用表模式
     */
    public static final String TABLE_TYPE = "TABLE";
    /**
     * 创建表模板
     */
    public static final String CREATE_TABLE_TEMPLATE_PERFIX = "CREATE TABLE ${TABLE_NAME} ( ";
    public static final String CREATE_TABLE_TEMPLATE = ", ${FILED_NAME} ${FILED_TYPE} ";
    public static final String CREATE_TABLE_TEMPLATE_DEFAULT_VALUE = " DEFAULT ${DEFAULT_VALUE} ";
    @Value(value = "${spring.jpa.properties.hibernate.default_schema}")
    public static String databaseSchema;
    //属性英文名
    private String entityAttrNameEn;

    //实体属性存储表
    private String entitySaveTable;

    //实体属性存储列
    private String entitySaveField;

    //实体存储类型
    private String entitySaveType;

    //默认值
    private String defaultValue;

    public DDLUtil(String entityAttrNameEn, String entitySaveTable, String entitySaveField, String entitySaveType, String defaultValue) {
        this.entityAttrNameEn = entityAttrNameEn;
        this.entitySaveTable = entitySaveTable;
        this.entitySaveField = entitySaveField;
        this.entitySaveType = entitySaveType;
        this.defaultValue = defaultValue;
    }

    public static boolean checkTableExist(Connection connection, String tableName) {
        return checkTableExist(connection, tableName, databaseSchema);
    }

    public static boolean checkTableExist(Connection connection, String tableName, String schema) {
//        try {
//            DatabaseMetaData dbMetaData = connection.getMetaData();
//            String[] types = {TABLE_TYPE};
//            try (ResultSet resultSet = dbMetaData.getTables(null, schema, tableName, types)) {
//                if (resultSet.next()) {
//                    return true;
//                }
//            }
//        } catch (Exception e) {
//            LOGGER.error("数据库连接异常：" + e);
//        }
        return false;
    }

    public void excuteSql(String sql) {
        String url = "jdbc:oracle:thin:@192.168.0.143:1521:ORCL";
        String username = "cimp";
        String password = "Sinosoft77675998";

        Connection myConnection = null;
        Statement myStatement = null;

        try {
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            myConnection = DriverManager.getConnection(url, username, password);
            myConnection.setAutoCommit(false);
            myStatement = myConnection.createStatement();
            System.out.println(sql);
            myStatement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (myStatement != null) {
                try {
                    myStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (myConnection != null) {
                try {
                    myStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}





