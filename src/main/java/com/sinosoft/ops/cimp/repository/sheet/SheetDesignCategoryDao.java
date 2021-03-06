/**
 * @Project:      IIMP
 * @Title:          SheetDesignCellDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import java.util.Collection;
import java.util.UUID;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;

/**
 * @ClassName:  SheetDesignCategoryDao
 * @description: 表格分类数据访问接口
 * @author:        Ni
 * @date:            2018年5月25日 下午4:06:28
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignCategoryDao extends BaseEntityDao<SheetDesignCategory> {
    /** 
     * 根据标识获取孩子列表
     * @param id 标识
     * @return 孩子列表
     */
    Collection<SheetDesignCategory> getChildren(UUID id);

    /** 
     * 获取根节点
     * @return 根节点列表
     */
    Collection<SheetDesignCategory> getRoot();
    
    /** 
     * 根据表格类型获取全部表格分类列表
     * @param type 表格类型
     * @return 表格分类列表
     */
    Collection<SheetDesignCategory> getByType(Byte type);
	
    /**
     * 
     * 新增记录
     * @param SheetDesignDataSource
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:44:47
     * @since JDK 1.7
     */
    UUID save(SheetDesignCategory sheetDesignCategory);
    
    /**
     * 修改记录名称
     * TODO请描述一下这个方法
     * @param id
     * @param name
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:50:33
     * @since JDK 1.7
     */
    boolean updateName(UUID id, String name, UUID userName);
    
    /**
     * 批量增加序号
     * TODO请描述一下这个方法
     * @param upOrdinal
     * @param parentId
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:58:07
     * @since JDK 1.7
     */
    boolean addOrdinals(int upOrdinal, UUID parentId, UUID userName);
    
    /**
     * 
     * 上一个节点
     * @param id
     * @return 上一个节点
     * @author kanglin
     * @date:    2018年6月13日 上午11:38:38
     * @since JDK 1.7
     */
    SheetDesignCategory findPrevious(UUID id);
    
    /**
     * 
     * 节点换ordinal
     * @param id
     * @param previousOrdinal
     * @return 
     * @author kanglin
     * @date:    2018年6月13日 上午11:41:44
     * @since JDK 1.7
     */
    int updateOrdinal(UUID id, int newOrdinal, UUID userName);
    
    /**
     * 
     * 下一个节点
     * @param id
     * @return 上一个节点
     * @author kanglin
     * @date:    2018年6月13日 上午11:38:38
     * @since JDK 1.7
     */
    SheetDesignCategory findNext(UUID id);
    
    /**
     * 
     * 找到类下面的报表
     * @param categoryId
     * @author kanglin
     * @date:    2018年6月13日 下午3:53:39
     * @since JDK 1.7
     */
    Collection<SheetDesignDesignCategory> findDesignByCategory(UUID categoryId);
    
    /**
     * 
     * 删除节点后，后面的节点ordinal减1
     * @param id
     * @param minusOrdinal
     * @return 
     * @author kanglin
     * @date:    2018年6月13日 上午11:41:44
     * @since JDK 1.7
     */
    int minusOrdinals(UUID id, UUID parentId, UUID userName);
    
    /**
     * 
     * 得到排序的所有节点
     * @return
     * @author Administrator
     * @date:    2018年6月26日 上午10:58:03
     * @since JDK 1.7
     */
    Collection<SheetDesignCategory> findAllByOrdinal();
}
