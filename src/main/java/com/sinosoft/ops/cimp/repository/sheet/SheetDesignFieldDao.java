package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;

import java.util.Collection;
import java.util.UUID;

/**
 * 
 * @ClassName:  SheetDesignFieldDao
 * @description: SheetDesignField 数据访问接口
 * @author:        sunch
 * @date:            2018年6月5日 上午11:09:34
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignFieldDao extends BaseEntityDao<SheetDesignField>{

	/**获取最大排序号*/
	public Integer getMaxOrdinal();

	Collection<SheetDesignField> getByDesignId(UUID designId);

	SheetDesignField getBingData(UUID designId, String sectionNo);

	public SheetDesignField findPrevious(UUID id, UUID designId);

	public int updateOrdinal(UUID preId, int ordinal, UUID userName);

	public SheetDesignField findNext(UUID id, UUID designId);

	public void deleteByDesignId(UUID designId);

	public void deleteFieldBindingByDesignId(UUID designId);

	void deleteFieldBindingByFieldId(UUID fieldId);
}
