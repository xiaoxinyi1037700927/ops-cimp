/**
 * @Project:     IIMP
 * @Title:          SysInfoItemDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.infostruct;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;

import java.util.Collection;
import java.util.List;
import java.util.Map;



/**
 * @ClassName:  SysInfoItemDao
 * @Description: 信息项数据访问对象
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:39:04
 * @Version        1.0.0
 */
public interface SysInfoItemDao extends BaseEntityDao<SysInfoItem> {
    /** 
     * 根据信息集标识获取信息项标识集
     * @param setId 信息集标识
     * @return 信息项标识集
     */
    Collection<Integer> getIdsBySetId(int setId);    
    /** 
     * 根据信息集标识获取信息项集合
     * @param setId 信息集标识
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetId(int setId);
    /** 
     * 根据信息集标识获取信息项集合
     * @param setId and appid 信息集标识
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetIdAndAppId(int setId, int appid);
    
    /** 
     * 根据信息集标识获取信息项集合
     * @param setId 信息集标识
     * @param type 类型（1为业务字段）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetId(int setId, Byte type);
    
    /** 
     * 根据信息集名称获取信息项集合
     * @param setName 信息集名称
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetName(String setName);
    
    /** 
     * 根据信息集名称获取信息项集合
     * @param setName 信息集名称
     * @param excludeUnnamed 是否排除未命名的（中文名为空的）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetName(String setName, boolean excludeUnnamed);
    
    /** 
     * 根据信息集名称获取信息项集合
     * @param setName 信息集名称
     * @param type 类型（1为业务字段）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetName(String setName, Byte type);
    
    /** 
     * 根据表名获取信息项集合
     * @param tableName 表名
     * @return 信息项集合
     */
    Collection<SysInfoItem> getByTableName(String tableName);
    
    /** 
     * 根据表名获取信息项集合
     * @param tableName 表名
     * @param excludeUnnamed 是否排除未命名的（中文名为空的）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getByTableName(String tableName, boolean excludeUnnamed);

    /** 
     * 根据表名获取信息项集合
     * @param tableName 表名
     * @param type 类型（1为业务字段）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getByTableName(String tableName, Byte type);
    
    /** 
     * 获取最大ID
     * @return 最大ID
     */
    int getMaxId();
    
    /** 
     * 根据信息集标识和列名获取信息项
     * @param infoSetId 信息集标识
     * @param columnName 列名
     * @return 信息项
     */
    SysInfoItem getByColumnName(Integer infoSetId, String columnName);
    
    
    /** 
     * 根据表名和列名获取信息项
     * @param tableName 表名
     * @param columnName 列名
     * @return 信息项
     */
    SysInfoItem getByColumnName(String tableName, String columnName);
    
	/**
	 * 根据sql查询
	 * @param sql
	 * @param params
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> findBySQL(String sql);
	
	/** 
	 * 获取全部的信息项
	 * @return 信息项集
	 */
	Collection<SysInfoItem> getAll();
}
