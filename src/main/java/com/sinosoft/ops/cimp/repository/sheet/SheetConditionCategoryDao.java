package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


public interface SheetConditionCategoryDao extends BaseEntityDao<SheetConditionCategory> {

    /**
     * 根据DesignId查所有分组
     */
    Collection<SheetConditionCategory> getCategory();

    Collection<SheetConditionCategory> findByParentId(UUID uuid);

    Collection<SheetConditionCategory> findByParentIdAndIds(UUID parentid, List<UUID> ids);

    Collection<SheetConditionCategory> findAllChildren();

    Collection<SheetConditionCategory> getRootData();

    Collection<SheetConditionCategory> getRootDataByIds(List<UUID> ids);

    /**
     * 根据条件分类将数据存入表
     */
    String save(SheetConditionCategory sheetConditionCategory);

    //取得第一个ID
    UUID getFisrtId();

    /**
     * 根据条件分类修改
     */
    void update(SheetConditionCategory sheetConditionCategory);

    /**
     * 根据Id查询一条分类对象
     */
    SheetConditionCategory getById(UUID id);

    /**
     * 根据分组id删除分组
     */
    void deleteId(UUID Id);

    SheetConditionCategory findNext(UUID id);

    int updateOrdinal(UUID nextId, int ordinal, UUID userName);

    SheetConditionCategory findPrevious(UUID id);
}
