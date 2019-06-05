package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDataSource;

import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignDataSourceDao
 * @description: 表设计数据源访问接口
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignDataSourceDao extends BaseEntityDao<SheetDesignDataSource>{
	/**
	 * 用id查询表设计数据源
	 * @param id
	 * @return 返回一个SheetDesignDataSource对象
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	SheetDesignDataSource getById(UUID id);

	/**
	 * 根据设计数据源保存
	 * @param SheetDesignDataSource
	 * @return	UUID
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	UUID  save(SheetDesignDataSource SheetDesignDataSource);
	/**
	 * 根据设计数据源修改
	 * @param SheetDesignDataSource
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	void update(SheetDesignDataSource SheetDesignDataSource, UUID id);

	/**
	 * 根据设计数据源删除
	 * @param id
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	void delete(UUID id);

	Integer getMaxOrdinal();

	Collection<SheetDesignDataSource> getByDesignId(UUID designId);

	SheetDesignDataSource getBingData(UUID designId, String sectionNo);

	Collection<SheetDesignDataSource> getByDesignIdDistinct(UUID designId);

	int deleteByDesignIdAndDataSourceId(UUID designId, UUID dataSourceId);

	int deleteByDesignId(UUID designId);
}
