package com.sinosoft.ops.cimp.repository.export;

import com.sinosoft.ops.cimp.common.dao.BaseDao;

import java.util.List;
import java.util.Map;


/**
 * 
 * @ClassName:  BaseQueryDao
 * @Description: 用于word导出的sql查询
 * @Author:        zhanxp
 * @Date:            2017年11月13日 下午12:32:55
 * @Version        1.0.0
 */
public interface ExportDao extends BaseDao {
	/**
	 * 根据sql查询
	 * @param sql
	 * @param params
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> findBySQL(String sql);
	/**
	 * 保存个人简历
	 * @return
	 */
	public boolean saveResumeByEmpId(String empId, String resume);
	/**
	 * 获取到所有的emps
	 * @return
	 */
	public List<String> getAllEmps();
	//获取所有的人员id，String
	String getStringEmps(String type);
	
	/** 
	 * 获取两委人员标识集
	 * @return 两委人员标识集
	 * @author Ni
	 * @date:    2019年1月18日 下午4:44:11
	 * @since JDK 1.7
	 */
	List<String> getLWEmpIds();
}
