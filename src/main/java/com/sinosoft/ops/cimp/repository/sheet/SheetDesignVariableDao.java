package com.sinosoft.ops.cimp.repository.sheet;

import com.sinosoft.ops.cimp.common.dao.BaseEntityDao;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignVariable;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignVariableDao
 * @description: 表设计变量访问接口
 * @author:        lixianfu
 * @date:            2018年6月4日 下午12:42:25
 * @version        1.0.0
 * @since            JDK 1.7
 */
public interface SheetDesignVariableDao extends BaseEntityDao<SheetDesignVariable>{
	/**
	 * 用id查询表设计变量
	 * @param id
	 * @return 返回一个SheetDesignVariable对象
	 * @author lixianfu
	 * @date:    2018年6月4日 下午12:59:33
	 * @since JDK 1.7
	 */
	SheetDesignVariable getById(UUID id);

	/**
	 * 根据设计变量保存
	 * @param SheetDesignVariable
	 * @return	UUID
	 * @author lixianfu
	 * @date:    2018年6月4日 下午1:10:45
	 * @since JDK 1.7
	 */
	UUID  saveVariable(SheetDesignVariable SheetDesignVariable);
	/**
	 * 根据设计变量修改
	 * @param SheetDesignVariable
	 * @author lixianfu
	 * @date:    2018年6月4日 下午1:14:07
	 * @since JDK 1.7
	 */
	void updateVariable(SheetDesignVariable SheetDesignVariable, UUID id);

	/**
	 * 根据设计变量删除
	 * @param id
	 * @author lixianfu
	 * @date:    2018年6月4日 下午1:15:23
	 * @since JDK 1.7
	 */
	void deleteVariable(UUID id);

	List<SheetDesignVariable> getByDesignId(UUID designId);
}
