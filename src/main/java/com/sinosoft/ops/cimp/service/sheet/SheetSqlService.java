package com.sinosoft.ops.cimp.service.sheet;


import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSql;

/**
 * 表格SQL服务
 * @author Nil
 *
 */
public interface SheetSqlService {
	/**
	 * 按表格设计标识获取该设计的全部设计SQL
	 * @param designId 表格设计标识
	 * @return 设计SQL
	 */
	public SheetDesignSql[] getByDesignId(String designId);
	
	/**
	 * 按表格表号和版本获取设计的全部设计SQL
	 * @param sheetNo 表格表号
	 * @param version 版本号
	 * @return 设计SQL
	 */
	public SheetDesignSql[] getBySheetNo(int sheetNo, int version);
	
	/**
	 * 按表格表号获取设计的全部设计SQL（初始版本）
	 * @param sheetNo 表格表号
	 * @return 设计SQL
	 */
	public SheetDesignSql[] getBySheetNo(int sheetNo);
}
