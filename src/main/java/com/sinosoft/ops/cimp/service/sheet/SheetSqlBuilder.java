package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;

/**
 * 表格SQL构建器
 * @author Nil
 *
 */
public interface SheetSqlBuilder {
	/**
	 * 添加表格数据项
	 * @param sheetDataItem 表格数据项
	 * @return this
	 */
	public SheetSqlBuilder appendField(SheetDesignField fieldBind);
	
	/**
	 * 添加表格条件
	 * @param condition 表格条件
	 * @return this
	 */
	public SheetSqlBuilder appendCondition(SheetDesignCondition condition);
	
	/**
	 * 构建SQL
	 * @return SQL
	 */
	public String buildSql();
}
