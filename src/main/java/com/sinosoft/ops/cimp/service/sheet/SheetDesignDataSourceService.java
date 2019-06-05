package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDataSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignDataSourceService
 * @description: 表设计数据源访问接口
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignDataSourceService extends BaseEntityService<SheetDesignDataSource> {
	/**
	 * 用id查询表设计数据源
	 * @param id
	 * @return 返回一个SheetDesignDataSource对象
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	SheetDesignDataSource getByDataSourceId(UUID id);
	
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
	//void update(SheetDesignDataSource SheetDesignDataSource, UUID id);
	
	/**
	 * 根据设计数据源删除
	 * @param id
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	void delete(UUID id);

	/**获取最大排序号*/
	Integer getMaxOrdinal();

	/**解析sql表达式，并给数据范围赋值
	 * @param sql
	 **/
	void analyzeSqlExpress(SheetDesignDataSource entity, String sql) throws Exception;

	Collection<SheetDesignDataSource> getByDesignId(UUID fromString);

	List<Map<String, Object>> GetTree(UUID designId);

	SheetDesignDataSource getBingData(UUID designId, String sectionNo);

	Collection<SheetDesignDataSource> getByDesignIdDistinct(UUID designId);


	int deleteByDesignIdAndDataSourceId(UUID fromString, UUID fromString2);

	void deleteByDesignId(UUID designId);
}
