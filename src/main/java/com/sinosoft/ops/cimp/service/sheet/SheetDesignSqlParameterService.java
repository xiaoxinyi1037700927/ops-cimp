package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.common.service.BaseEntityService;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSqlParameter;

import java.util.UUID;


/**
 * @ClassName:  SheetDesignSqlParameterService
 * @description: 表设计SQL参数访问接口
 * @author:        kanglin
 * @date:            2018年6月6日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignSqlParameterService extends BaseEntityService<SheetDesignSqlParameter> {
	/**
	 * 用id查询表设计SQL参数
	 * @param id
	 * @return 返回一个SheetDesignSqlParameter对象
	 * @author kanglin
	 * @date:    2018年6月6日 下午
	 * @since JDK 1.7
	 */
	SheetDesignSqlParameter getBySqlParameterId(UUID id);
	
	/**
	 * 根据设计SQL参数保存
	 * @param SheetDesignSqlParameter
	 * @return	UUID
	 * @author kanglin
	 * @date:    2018年6月6日 下午
	 * @since JDK 1.7
	 */
	UUID  save(SheetDesignSqlParameter SheetDesignSqlParameter);
	/**
	 * 根据设计SQL参数修改
	 * @param SheetDesignSqlParameter
	 * @author kanglin
	 * @date:    2018年6月6日 下午
	 * @since JDK 1.7
	 */
	void update(SheetDesignSqlParameter SheetDesignSqlParameter, UUID id);
	
	/**
	 * 根据设计SQL参数删除
	 * @param id
	 * @author kanglin
	 * @date:    2018年6月6日 下午
	 * @since JDK 1.7
	 */
	void delete(UUID id);	
}
