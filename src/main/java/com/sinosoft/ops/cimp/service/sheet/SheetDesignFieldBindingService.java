package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignFieldBinding;

import java.util.Collection;
import java.util.UUID;



public interface SheetDesignFieldBindingService extends BaseEntityService<SheetDesignFieldBinding> {

	/**
	 * 
	 * 根据数据项id获取绑定信息集
	 * @param uuid
	 * @return
	 * @author sunch
	 * @date:    2018年8月4日 下午9:55:50
	 * @since JDK 1.7
	 */
	Collection<SheetDesignFieldBinding> getByFieldId(UUID fieldId);
	
	/**
	 * 根据模板id删除绑定信息
	 * @return 
	 */
	void deleteByDesignId(UUID designId);
}
