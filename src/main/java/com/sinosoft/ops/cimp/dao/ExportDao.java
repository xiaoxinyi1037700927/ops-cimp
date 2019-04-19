package com.sinosoft.ops.cimp.dao;

import java.util.List;
import java.util.Map;

public interface ExportDao {
    /**
     * 根据sql查询
     *
     * @param sql
     * @return
     */
    List<Map<String, Object>> findBySQL(String sql);

    /**
     * @param sql
     * @param params
     * @return
     */
    List<Map<String, Object>> findBySQL(String sql, Map<String, Object> params);

}
