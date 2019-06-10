package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName: SheetConditionCategoryService
 * @description: 条件分类业务层接口
 */
public interface SheetConditionCategoryService extends BaseEntityService<SheetConditionCategory> {

    List<Map<String, Object>> getCategoryTree();

    List<Map<String, Object>> getCategoryTreeByIds(List<UUID> ids);

    Collection<SheetConditionCategory> findAllChildren();

    /**
     * 根据条件分类将数据存入表
     */
    String save(SheetConditionCategory sheetConditionCategory);

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

    boolean moveUp(SheetConditionCategory entity);

    boolean moveDown(SheetConditionCategory entity);

}
