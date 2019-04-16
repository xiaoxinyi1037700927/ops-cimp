package com.sinosoft.ops.cimp.dao;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseQueryDao
 * @Description: 用于word导出的sql查询
 * @Author: zhanxp
 * @Date: 2017年11月13日 下午12:32:55
 * @Version 1.0.0
 */
public interface ExportDao extends BaseDao {
    /**
     * 根据sql查询
     *
     * @param sql
     * @param params
     * @return List<Map   <   String   ,   Object>>
     */
    List<Map<String, Object>> findBySQL(String sql);

    List<Map<String, Object>> findBySQL1(String sql, String empId);

    //在单位管理模块，导出任免表，导出当前单位下所有干部的任免表，不同于新疆，wft    201712
    String getEmpsInUnitExportGbrmb(String[] depIds);

//	void exportPdf(List<String> empIds);

    List<String> getEmps();

    //获取所有的人员id，String
    String getStringEmps(String type);
}
