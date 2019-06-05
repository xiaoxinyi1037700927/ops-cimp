package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;

import java.util.Collection;
import java.util.UUID;

/**
 * 
 * @ClassName:  SheetDesignFieldService
 * @description: SheetDesignField 服务接口类
 * @author:        sunch
 * @date:            2018年6月5日 上午11:14:38
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignFieldService extends BaseEntityService<SheetDesignField> {

	/**
	 * 获取最大排序号
	 */
	public Integer getMaxOrdinal();

	Collection<SheetDesignField> getByDesignId(UUID designId);

	void setCountData(SheetDesignField sheetDesignField);

	SheetDesignField getBingData(UUID designId, String sectionNo);

	public boolean moveUp(SheetDesignField entity, UUID designId);

	public boolean moveDown(SheetDesignField entity, UUID designId);

	public void deleteByDesignId(UUID designId);

}
