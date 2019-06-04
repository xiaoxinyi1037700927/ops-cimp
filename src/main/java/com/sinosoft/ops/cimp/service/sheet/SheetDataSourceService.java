package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * @ClassName:  SheetDataSourceService
 * @description: 表设计数据源访问接口
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDataSourceService extends BaseEntityService<SheetDataSource>{
	/**
	 * 用id查询表设计数据源
	 * @param id
	 * @return 返回一个SheetDataSource对象
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	SheetDataSource getByDataSourceId(UUID id);
	
	/**
	 * 根据设计数据源保存
	 * @param SheetDataSource
	 * @return	UUID
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	UUID  save(SheetDataSource SheetDataSource);
	/**
	 * 根据设计数据源修改
	 * @param SheetDataSource
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	//void update(SheetDataSource SheetDataSource, UUID id);
	
	/**
	 * 根据设计数据源删除
	 * @param id
	 * @author kanglin
	 * @date:    2018年6月5日 下午
	 * @since JDK 1.7
	 */
	void delete(UUID id);

	/**解析sql表达式，并给数据范围赋值*/
	void analyzeSqlExpress(SheetDataSource entity);

	//根据分类id获取list
	Collection<SheetDataSource> getByCategoryId(int categoryid);

	//取得引用情况
	List<Map> getRefSituation(String id);
}
