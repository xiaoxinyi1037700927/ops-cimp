package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignFieldBinding;

import java.util.Collection;
import java.util.UUID;

public interface SheetDesignFieldBindingDao extends BaseEntityDao<SheetDesignFieldBinding>{

	/**
	 * 根据数据项id获取绑定信息集合
	 * @param fieldId
	 * @return
	 * @author sunch
	 * @date:    2018年8月4日 下午9:41:06
	 * @since JDK 1.7
	 */
	Collection<SheetDesignFieldBinding> getByFieldId(UUID fieldId);

	void deleteByDesignId(UUID designId);
}
