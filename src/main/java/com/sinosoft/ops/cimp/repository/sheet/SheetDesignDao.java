/**
 * @Project:      IIMP
 * @Title:          SheetDesignDao.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesign;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/** 
 * @ClassName:  SheetDesignDao
 * @description: 表格设计数据访问接口
 * @author:        Ni
 * @date:            2018年5月25日 下午2:10:56
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignDao extends BaseEntityDao<SheetDesign> {
    /** 
     * 根据分类标识获取
     * @param categoryId 分类标识
     * @return 表格设计集
     * @author Ni
     * @since JDK 1.7
     */
	Collection<SheetDesign> getByCategoryId(UUID categoryId);
    Collection<UUID> getSheetCategoryId(UUID designId);
    /**
     * 批量增加序号
     * TODO请描述一下这个方法
     * @param upOrdinal
     * @param categorytId
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:58:07
     * @since JDK 1.7
     */
    boolean addOrdinals(int upOrdinal, UUID categoryId, UUID userName);

    //判断模板号是否重复
    boolean checkSheetNo(String sheetNo);
    boolean checkSheetNo(String sheetNo, UUID id);

    /**
     * 
     * 得到目录下所有节点的最大ordinal
     * @param categoryId
     * @return
     * @author kanglin
     * @date:    2018年6月15日 上午9:42:23
     * @since JDK 1.7
     */
    int getMaxOrdinal(UUID categoryId);
	
    /**
     * 删除节点后，后面的节点ordinal减1
     * @param upOrdinal
     * @param categorytId
     * @return
     * @author kanglin
     * @date:    2018年6月13日 上午10:58:07
     * @since JDK 1.7
     */
    int minusOrdinals(int thisOrdinal, UUID categoryId, UUID userName);
    
    /**
     * 
     * 上一个节点
     * @param id
     * @return 上一个节点
     * @author kanglin
     * @date:    2018年6月13日 上午11:38:38
     * @since JDK 1.7
     */
    SheetDesign findPrevious(UUID id, UUID categoryId);
    
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
    SheetDesign findNext(UUID id, UUID categoryId);

    //取得引用情况
    List<Map> getRefSituation(String id);

	Collection<String> getAllOrganizationByParent(Collection<String> organizationIds);
    //取得绑定的条件
    Collection<String> getSecCondition(String designId, String sectionNo);
    //取得绑定的数据项
    Collection<String> getSecField(String designId, String sectionNo);
}
