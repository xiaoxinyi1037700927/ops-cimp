package com.sinosoft.ops.cimp.repository.sheet;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface SheetConditionDao extends BaseEntityDao<SheetCondition> {

    String getName(long id);

    List<SheetCondition> findAll();

    List<SheetCondition> GetDataByDataSourceID(String DataSourceID);

    SheetCondition GetConditionDataById(String id);

    List<SheetCondition> getConditionByCategoryId(String categoryId);

    List<SheetCondition> getConditionByDesignId(String designId);

    SheetCondition findNext(UUID id, UUID categoryId);

    int updateOrdinal(UUID nextId, int ordinal, UUID userName);

    SheetCondition findPrevious(UUID id, UUID categoryId);

    PageableQueryResult findByPage(PageableQueryParameter queryParameter);

    List<Map> getRefSituation(String id);
}
