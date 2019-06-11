package com.sinosoft.ops.cimp.service.sheet;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionItem;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SheetConditionService extends BaseEntityService<SheetCondition> {
    List<SheetCondition> findAll();

    void deleteById(String id);

    List<SheetCondition> GetDataByDataSourceID(String DataSourceID);

    SheetCondition GetConditionDataById(String id);

    List<SheetCondition> getConditionByDesignId(String designId);

    public void save(SheetCondition sysconditon);

    void ResolveAndSave(HttpServletRequest request, UUID userId) throws Exception;

    List<Map> getRefSituation(String id);

    PageableQueryResult findByPage(PageableQueryParameter queryParameter);

    void save(SheetCondition sysconditon, List<SheetConditionItem> sysConditionJsonItems);

    void saveOrUpdate(SheetCondition sysconditon, List<SheetConditionItem> sysConditionJsonItems);

    void setSqlTables(SheetCondition entity);

    String GetConditionSql(JSONObject json);

    String GetConditionTableIds(JSONObject json);

    String ResolveToStrSql(String combineQueryParams);

    List<SheetCondition> getConditionByCategoryId(String categoryId);

    boolean moveDown(SheetCondition entity, UUID categoryId);

    boolean moveUp(SheetCondition entity, UUID categoryId);
}
