/**
 * @Project:      IIMP
 * @Title:           SysInfoSetDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.infostruct;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;

import java.util.Collection;



/**
 * @ClassName:  SysInfoSetDao
 * @Description: TODO请描述一下这个类
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:40:34
 * @Version        1.0.0
 */
public interface SysInfoSetDao extends BaseEntityDao<SysInfoSet> {
    /** 
     * 获取当前数据库所有的表信息
     * @param maxSetId 最大信息集标识
     * @param maxItemId 最大信息项标识
     * @return 信息集集合
     */
	Collection<SysInfoSet> getAllFromDBTables(int maxSetId, int maxItemId);
	
	/**
	 * 获取全部信息集并按中文名排序
	 * @return 信息集集合
	 */
	Collection<SysInfoSet> getAllOrderByNameCn();
	
	   /** 
     * 根据名称获取信息集标识
     * @param name 名称
     * @return 信息集标识
     */
    Integer getIdByName(String name);
    /** 
     * 根据表名获取信息集标识
     * @param tableName 表名
     * @return 信息集标识
     */
    Integer getIdByTableName(String tableName);
	
	/** 
	 * 根据名称获取信息集
	 * @param name 名称
	 * @return 信息集
	 */
	SysInfoSet getByName(String name);
	   /** 
     * 根据表名获取信息集
     * @param tableName 表名
     * @return 信息集
     */
	SysInfoSet getByTableName(String tableName);
	
    /** 
     * 获取最大ID
     * @return 最大ID
     */
    int getMaxId();
    
    /** 
     * 根据类型获取信息集
     * @param type 信息集类型
     * @return 信息集集合
     */
    Collection<SysInfoSet> getByType(byte type);
    
    /** 
     * 根据组标识获取信息集
     * @param groupId 组标识
     * @return 信息集集合
     */
    Collection<SysInfoSet> getByGroupId(Integer groupId);
    
    /** 
     * 获取组主信息集
     * @param groupId 组标识
     * @return 组主信息集
     */
    SysInfoSet getGroupMainSet(Integer groupId);
    
    /** 
     * 获取组子信息集
     * @param groupId 组标识
     * @return 组子信息集集合
     */
    Collection<SysInfoSet> getGroupSubSet(Integer groupId);
    /** 
     * 获取主集名称 交给页面master的title
     * 
     * 
     */
    
	SysInfoItem findInfoItemby(Integer id);
	
    /** 
     * 获取全部信息集标识
     * @return 信息集标识集
     * @author Ni
     * @date:    2018年7月12日 下午2:52:35
     * @since JDK 1.7
     */
    Collection<Integer> getAllIds();
    
    /** 
     * 获取全部信息集
     * @return 信息集
     * @author Ni
     * @date:    2018年7月12日 下午2:52:35
     * @since JDK 1.7
     */
    Collection<SysInfoSet> getAll();
    
    /** 
     * 根据组标识获取信息集标识集
     * @param groupId 组标识
     * @return 信息集标识集
     */
    Collection<Integer> getIdsByGroupId(Integer groupId);
}
