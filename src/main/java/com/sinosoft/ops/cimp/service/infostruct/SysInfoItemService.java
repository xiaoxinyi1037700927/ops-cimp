/**
 * @Project:      IIMP
 * @Title:           SysInfoItemService.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.infostruct;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.infostruct.InputType;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.exception.CloneException;

import java.util.Collection;
import java.util.List;
import java.util.Map;



/**
 * @ClassName:  SysInfoItemService
 * @Description: TODO请描述一下这个类
 * @Author:        wft
 * @Date:            2017年8月18日 下午4:38:18
 * @Version        1.0.0
 */
public interface SysInfoItemService extends BaseEntityService<SysInfoItem> {
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
     * @param setId 信息集标识
     * @param excludeUnnamed 是否排除未命名的（中文名为空的）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getBySetId(int setId, boolean excludeUnnamed);
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
     * @param type 类型（1为业务字段）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getByTableName(String tableName, Byte type);
    /** 
     * 根据表名获取信息项集合
     * @param tableName 表名
     * @param excludeUnnamed 是否排除未命名的（中文名为空的）
     * @return 信息项集合
     */
    Collection<SysInfoItem> getByTableName(String tableName, boolean excludeUnnamed);
    
    /** 
     * 获取最大ID
     * @return 最大ID
     */
    int getMaxId();
    
    /**
     * 根据标识判断是否有效
     * @param id 标识
     * @return 是否有效
     */
    boolean isValid(int id);
    /**
     * 根据名称判断是否有效
     * @param setId 信息集标识
     * @param name 名称
     * @return 是否有效
     */
    boolean isValid(int setId, String name);
    /**
     * 判断是否为有效的字段名
     * @param setId 信息集标识
     * @param columnName 字段名
     * @return 是否有效
     */
    boolean isValidColumn(int setId, String columnName);
    
    /** 
     * 根据信息集标识获取主键信息项集
     * @param setId 信息集标识
     * @return 主键信息项集
     */
    Collection<SysInfoItem> getPrimaryKeysBySetId(int setId);
    
    /** 
     * 根据信息集标识获取非空信息项集
     * @param setId 信息集标识
     * @return 非空信息项集
     */
    Collection<SysInfoItem> getNotNullBySetId(int setId);
    
    /** 
     * 根据信息集标识获取排序信息项集
     * @param setId 信息集标识
     * @return 排序信息项集
     */
    Collection<SysInfoItem> getOrderBySetId(int setId);
    
    /** 
     * 根据信息集标识和列名获取信息项
     * @param infoSetId 信息集标识
     * @param columnName 列名
     * @return 信息项
     */
    SysInfoItem getByColumnName(int infoSetId, String columnName);
    
    /** 
     * 根据表名和列名获取信息项
     * @param tableName 表名
     * @param columnName 列名
     * @return 信息项
     */
    SysInfoItem getByColumnName(String tableName, String columnName); 
    
    /** 
     * 根据信息集标识和名称获取信息项
     * @param infoSetId 信息集标识
     * @param name 名称
     * @return 信息项
     */
    SysInfoItem getByName(int setId, String name);
    /** 
     * 根据表名和名称获取信息项
     * @param tableName 表名
     * @param name 名称
     * @return 信息项
     */
    SysInfoItem getByName(String tableName, String columnName);
    
    /** 
     * 获取列名
     * @param id 标识
     * @return 列名
     * @author Ni
     * @date:    2018年8月3日 下午5:54:10
     * @since JDK 1.7
     */
    String getColumnName(int id);
    
    List<Map<String, Object>> findBySQL(String sql);
    
    /**
     * 
     * 获取全部的标识与代码项映射
     * @return 标识与代码项映射
     */
    Map<Integer,SysInfoItem> getAllMap();
    
    /**
     * 获取系统内置信息项，如ordinal、status、created_time、created_by、last_modified_time、last_modified_by
     * @param setId 信息集标识
     * @return 系统内置信息项集
     * @author Nil
     * @since JDK 1.7
     */
    Collection<SysInfoItem> getBuiltIn(int setId);
    
    /**
     * 根据输入类型获取信息项
     * @param setId 信息集标识
     * @param inputType 输入类型
     * @return 信息项集
     */
    Collection<SysInfoItem> getByInputType(int setId, InputType inputType);
    
    /**
     * 对对象列表进行排序
     * @param list 对象列表
     */
    void sort(List<? extends SysInfoItem> list);
    /**
     * 对对象列表进行排序（按名称）
     * @param list 对象列表
     */
    void sortByName(List<? extends SysInfoItem> list);
    
    /** 
     * 根据标识集获取
     * @param ids 标识集
     * @return 系统权限集
     */
    Collection<SysInfoItem> getByIds(Collection<Integer> ids);
    
    /** 
     * 获取唯一组标识
     * @param setId 信息集标识
     * @return 唯一组标识集
     */
    Collection<Integer> getUniqueGroupIds(int setId);
    
    /** 
     * 获取同唯一组的信息项
     * @param setId 信息集标识
     * @param uniqueGroupId 唯一组标识
     * @return 信息项集
     */
    Collection<SysInfoItem> getByUniqueGroupId(int setId, int uniqueGroupId);
    
    /** 
     * 预初始化
     * @author Ni
     * @date:    2018年7月11日 下午2:02:00
     * @since JDK 1.7
     */
    void preInitialize();
    
    /** 
     * 根据可读标识获取，信息项的可读属性值为真，可写属性值为假
     * @param ids 可读标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:00:08
     * @since JDK 1.7
     */
    Collection<SysInfoItem> getByReadableIds(Collection<Integer> ids) throws CloneException;
    /** 
     * 根据可写标识获取，信息项的可读属性值为真，可写属性值为真
     * @param ids 可写标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:00:08
     * @since JDK 1.7
     */
    Collection<SysInfoItem> getByWritableIds(Collection<Integer> ids) throws CloneException;
    /** 
     * 根据可访问标识获取，信息项的可读、可写属性根据标识来设定并作合并（可写不可读，可读必可写）
     * @param readableIds 可读标识集
     * @param writableIds 可写标识集
     * @return 信息项集
     * @author Ni
     * @throws CloneException 
     * @date:    2019年5月16日 上午9:07:48
     * @since JDK 1.7
     */
    Collection<SysInfoItem> getByAccessibleIds(Collection<Integer> readableIds, Collection<Integer> writableIds) throws CloneException;
}
