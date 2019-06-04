package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;

import java.util.Collection;
import java.util.List;


public interface SheetConditionCategoryDao extends BaseEntityDao<SheetConditionCategory> {

    /**
     * 根据DesignId查所有分组
     */
    Collection<SheetConditionCategory> getCategory();

    Collection<SheetConditionCategory> findByParentId(String uuid);

    Collection<SheetConditionCategory> findByParentIdAndIds(String parentid, List<String> ids);

    Collection<SheetConditionCategory> findAllChildren();

    Collection<SheetConditionCategory> getRootData();

    Collection<SheetConditionCategory> getRootDataByIds(List<String> ids);

    /**
     * 根据条件分类将数据存入表
     */
    String save(SheetConditionCategory sheetConditionCategory);

    //取得第一个ID
    String getFisrtId();

    /**
     * 根据条件分类修改
     */
    void update(SheetConditionCategory sheetConditionCategory);

    /**
     * 根据Id查询一条分类对象
     */
    SheetConditionCategory getById(String id);

    /**
     * 根据分组id删除分组
     */
    void deleteId(String Id);

    SheetConditionCategory findNext(String id);

    int updateOrdinal(String nextId, int ordinal, String userName);

    SheetConditionCategory findPrevious(String id);
}
