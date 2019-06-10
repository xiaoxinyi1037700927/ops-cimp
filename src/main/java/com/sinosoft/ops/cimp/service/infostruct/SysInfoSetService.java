/**
 * @Project:      IIMP
 * @Title:           SysInfoSetService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.infostruct;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.infostruct.InfoSetType;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;
import com.sinosoft.ops.cimp.exception.CloneException;

import java.util.Collection;
import java.util.List;


/**
 * @ClassName:  SysInfoSetService
 * @Description: 信息集服务接口
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:37:36
 * @Version        1.0.0
 */
public interface SysInfoSetService extends BaseEntityService<SysInfoSet> {
    /** 
     * 根据标识集获取
     * @param ids 标识集
     * @return 信息集集
     */
    Collection<SysInfoSet> getByIds(Collection<Integer> ids);
    
    /** 
     * 根据名称获取信息集标识
     * @param name 信息集名称
     * @return 信息集标识
     * @author Ni
     * @date:    2018年7月12日 上午10:15:34
     * @since JDK 1.7
     */
    Integer getIdByName(String name);
    /** 
     * 根据表名获取信息集标识
     * @param tableName 表名
     * @return 信息集标识
     * @author Ni
     * @date:    2018年7月12日 上午10:16:19
     * @since JDK 1.7
     */
    Integer getIdByTableName(String tableName);
    
    /** 
     * 获取信息集名称
     * @param id 标识
     * @return 信息集名称
     * @author Ni
     * @date:    2018年7月12日 上午10:14:14
     * @since JDK 1.7
     */
    String getName(int id);
    /** 
     * 获取信息集中文名称
     * @param id 标识
     * @return 信息集名称
     * @author Ni
     * @date:    2018年7月12日 上午10:14:14
     * @since JDK 1.7
     */
    String getNameCn(int id);    
	/**
	 * 获取信息集表名
	 */
	String getTableName(int id);
    /**
     * 获取信息集描述
     */
    String getDescription(int id);
	
    /**
     * 根据信息集名称获取信息集
     * @param name 信息集名称
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
     * 根据名称集获取信息集集合
     * @param ids 名称集
     * @return 信息集集合
     */
    Collection<SysInfoSet> getByNames(Collection<String> names);
    /** 
     * 根据表名集获取信息集集合
     * @param ids 表名集
     * @return 信息集集合
     */
    Collection<SysInfoSet> getByTableNames(Collection<String> tableNames);    
    
    /**
     * 根据标识判断是否有效
     * @param id 标识
     * @return 是否有效
     */
    boolean isValid(int id);
    /**
     * 根据标识判断是否有效
     * @param ids 标识数组
     * @return 是否有效
     */
    boolean isValid(Collection<Integer> ids);    
    
    /**
     * 根据名称判断是否有效
     * @param name 名称
     * @return 是否有效
     */
    boolean isValidName(String name);
    /**
     * 根据名称判断是否有效
     * @param names 名称数组
     * @return 是否有效
     */
    boolean isValidName(Collection<String> names);    
    
    /**
     * 判断是否为有效的表名
     * @param tableName 表名
     * @return 是否有效
     */
    boolean isValidTableName(String tableName);
    /**
     * 判断是否为有效的表名
     * @param tableNames 表名数组
     * @return 是否有效
     */
    boolean isValidTableName(Collection<String> tableNames);

    /** 
     * 获取最大ID
     * @return 最大ID
     */
    int getMaxId();
    
    /** 
     * 获取全部的信息集
     * @return 信息集集合
     */
    Collection<SysInfoSet> getAllOrderByNameCn();
    
    /** 
     * 根据类型获取信息集
     * @param type 信息集类型
     * @return 信息集集合
     */
    Collection<SysInfoSet> getByType(InfoSetType type);
    /** 
     * 根据类型获取树形信息集
     * @param type 信息集类型
     * @return 信息集集合
     */
    Collection<SysInfoSetCategory> getInfoSetTreeByType(InfoSetType type);
    
    /** 
     * 根据组标识获取信息集标识集
     * @param groupId 组标识
     * @return 信息集标识集
     */
    Collection<Integer> getIdsByGroupId(Integer groupId);
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

	String findbysetid(int parseInt);

	SysInfoItem findInfoItemby(Integer id);
	
	Collection<SysInfoSetCategory> getInfoSetTreeColByType(InfoSetType type) ;
	
    /**
     * 对对象列表进行排序
     * @param list 对象列表
     */
    void sort(List<? extends SysInfoSet> list);
    /**
     * 对对象列表进行排序（按名称）
     * @param list 对象列表
     */
    void sortByName(List<? extends SysInfoSet> list);    
    
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
     * 预初始化
     * @author Ni
     * @date:    2018年7月11日 下午2:02:00
     * @since JDK 1.7
     */
    void preInitialize();
    
    /** 
     * 根据可插入标识获取信息集，
     * 其可插入属性值为真，
     * 其可删除属性值为假，
     * 其可更新属性值为假，
     * 其可查询属性值为真
     * @param ids 可读标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:00:08
     * @since JDK 1.7
     */
    Collection<SysInfoSet> getByInsertableIds(Collection<Integer> ids) throws CloneException;
    /** 
     * 根据可删除标识获取信息集，
     * 其可插入属性值为假，
     * 其可删除属性值为真，
     * 其可更新属性值为假，
     * 其可查询属性值为真
     * @param ids 可读标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:00:08
     * @since JDK 1.7
     */
    Collection<SysInfoSet> getByDeletableIds(Collection<Integer> ids) throws CloneException;
    /** 
     * 根据可更新标识获取信息集，
     * 其可插入属性值为假，
     * 其可删除属性值为假，
     * 其可更新属性值为真，
     * 其可查询属性值为真
     * @param ids 可读标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:00:08
     * @since JDK 1.7
     */
    Collection<SysInfoSet> getByUpdatableIds(Collection<Integer> ids) throws CloneException;
    /** 
     * 根据可查询标识获取信息集，
     * 其可插入属性值为假，
     * 其可删除属性值为假，
     * 其可更新属性值为假，
     * 其可查询属性值为真
     * @param ids 可读标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:00:08
     * @since JDK 1.7
     */
    Collection<SysInfoSet> getBySelectableIds(Collection<Integer> ids) throws CloneException;    
    /** 
     * 根据可访问标识获取，信息项的可读、可写属性根据标识来设定并作合并（可写不可读，可读必可写）
     * @param insertableIds 可插入标识集
     * @param deletableIds 可删除标识集
     * @param updatableIds 可更新标识集
     * @param selectableIds 可查询标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:07:48
     * @since JDK 1.7
     */
    Collection<SysInfoSet> getByAccessibleIds(Collection<Integer> insertableIds, Collection<Integer> deletableIds, Collection<Integer> updatableIds, Collection<Integer> selectableIds) throws CloneException;
}
