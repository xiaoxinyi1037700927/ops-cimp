package com.sinosoft.ops.cimp.repository.sheet;

import java.util.UUID;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSqlParameter;

/**
 * @ClassName:  SheetDesignDataSourceDao
 * @description: 表设计SQL参数访问接口
 * @author:        kanglin
 * @date:            2018年6月6日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignSqlParameterDao extends BaseEntityDao<SheetDesignSqlParameter>{
			/**
			 * 用id查询表设计SQL参数
			 * @param id
			 * @return 返回一个SheetDesignSqlParameter对象
			 * @author kanglin
			 * @date:    2018年6月6日 下午
			 * @since JDK 1.7
			 */
			SheetDesignSqlParameter getById(UUID id);
			
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
			 * @param SheetDesignDataSource
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
