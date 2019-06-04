package com.sinosoft.ops.cimp.repository.sheet;


import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;

import java.util.List;
import java.util.Map;


public interface SheetConditionDao extends BaseEntityDao<SheetCondition> {

    String getName(long id);

    List<SheetCondition> findAll();

    List<SheetCondition> GetDataByDataSourceID(String DataSourceID);

    SheetCondition GetConditionDataById(String id);

    List<SheetCondition> getConditionByCategoryId(String categoryId);

    List<SheetCondition> getConditionByDesignId(String designId);

    SheetCondition findNext(String id, String categoryId);

    int updateOrdinal(String nextId, int ordinal, String userName);

    SheetCondition findPrevious(String id, String categoryId);

    PageableQueryResult findByPage(PageableQueryParameter queryParameter);

    List<Map> getRefSituation(String id);
}
