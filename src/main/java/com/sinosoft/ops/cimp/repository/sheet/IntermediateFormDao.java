package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseDao;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;

import java.util.Collection;
import java.util.Map;



public interface IntermediateFormDao extends BaseDao {

	Collection<?> queryListBySql(String sql, Object... args);

	Collection<SysInfoItem> getItemByName(String name);

	Map<String, Object> getIdByIdKey(String itemKey);

}
