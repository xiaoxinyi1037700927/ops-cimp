package com.sinosoft.ops.cimp.dao.domain;

import java.util.Arrays;

public class ResultSql {
    /**
     * 执行sql语句
     */
    private String sql;
    /**
     * 参数序列
     */
    private Object data[];

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultSql{" +
                "sql='" + sql + '\'' +
                ", data=" + Arrays.deepToString(data) +
                '}';
    }
}
