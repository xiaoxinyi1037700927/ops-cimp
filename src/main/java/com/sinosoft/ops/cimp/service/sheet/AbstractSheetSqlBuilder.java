package com.sinosoft.ops.cimp.service.sheet;

import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;

import java.util.LinkedList;
import java.util.List;



/**
 * 表格SQL构建器抽象类
 * @author Nil
 *
 */
public abstract class AbstractSheetSqlBuilder implements SheetSqlBuilder {
	/*** 表格数据字段 */
	protected List<SheetDesignField> fields = new LinkedList<SheetDesignField>();
	/*** 表格条件 */
	protected List<SheetDesignCondition> conditions = new LinkedList<SheetDesignCondition>();

	@Override
	public SheetSqlBuilder appendField(SheetDesignField field) {
	    fields.add(field);
		return this;
	}

	@Override
	public SheetSqlBuilder appendCondition(SheetDesignCondition condition) {
		this.conditions.add(condition);
		return this;
	}

	@Override
	public abstract String buildSql();
}
