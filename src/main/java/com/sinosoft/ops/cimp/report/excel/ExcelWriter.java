package com.sinosoft.ops.cimp.report.excel;


import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain chen on 2017/8/7.
 * 
 * @author rain chen
 * @version 1.0
 * @since 1.0 Copyright (C) 2017. SinSoft All Rights Received
 */
public class ExcelWriter {

	private static Logger log = LoggerFactory.getLogger(ExcelWriter.class);

	// 作业本
	private Workbook workbook = null;

	// 当前Sheet页，默认为第一个（下标为0）
	private Sheet writableSheet = null;

	// Excel对象的行
	private Row row = null;

	// <行号，<列号,类型>>
	private Map<Integer, Map<Integer, ExcelType>> typeMap = new HashMap();

	// 输出流
	private OutputStream stream;

	public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getWritableSheet() {
		return writableSheet;
	}

	public Row getRow() {
		return row;
	}

	public ExcelWriter(File file) {
		try {
			if (file.getName().endsWith(".xls")) {
				this.workbook = new HSSFWorkbook();
			} else if (file.getName().endsWith(".xlsx")) {
				this.workbook = new XSSFWorkbook();
			}
			stream = new FileOutputStream(file);
			assert workbook != null;
			this.writableSheet = workbook.createSheet();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("文件未找到！", e);
		}
	}

	public CellStyle getCellStyle(int rowIndex, int cellIndex) {
		Row row = writableSheet.getRow(rowIndex);
		Cell cell;
		if (row == null) {
			row = writableSheet.createRow(rowIndex);
		}
		cell = row.getCell(cellIndex);
		if (cell == null) {
			cell = row.createCell(cellIndex);
		}
		return cell.getCellStyle();
	}

	public ExcelWriter(File outFile, File templateFile) {
		try {
			if (templateFile.getName().endsWith(".xls")) {
				this.workbook = new HSSFWorkbook(new POIFSFileSystem(
						new FileInputStream(templateFile)));
			} else if (templateFile.getName().endsWith(".xlsx")) {
				this.workbook = new XSSFWorkbook(new FileInputStream(
						templateFile));
			}
			stream = new FileOutputStream(outFile);
			// 默认第一页为当前sheet页
			assert workbook != null;
			this.writableSheet = workbook.getSheetAt(0);
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常！", e);
		}
	}

	/**
	 * 设置Sheet页
	 */
	public void setWritableSheet(Sheet writableSheet) {
		this.writableSheet = writableSheet;
	}

	public void createSheet(String sheetName, int index) {
		writableSheet = workbook.getSheet(sheetName);
		if (writableSheet == null) {
			if (index < workbook.getNumberOfSheets()) {
				workbook.setSheetName(index, sheetName);
				this.writableSheet = workbook.getSheet(sheetName);
			} else {
				writableSheet = workbook.createSheet(sheetName);
			}
		} else {
			log.debug("Sheet页名称【" + sheetName + "】重复，请沿用原有的Sheet页");
		}
	}

	/**
	 * 修改当前写入Sheet页
	 * 
	 * @param sheetName
	 *            sheet页名称
	 */
	public void changeSheet(String sheetName) {
		int sheetIndex = workbook.getSheetIndex(writableSheet);
		if (sheetIndex == -1) {
			throw new RuntimeException("不能指定不存在的Sheet页" + sheetIndex);
		}
		workbook.setSheetName(sheetIndex, sheetName);
	}

	/**
	 * 数据写入Excel中
	 * 
	 * @param rowIndex
	 *            开始写入的行，从0开始
	 * @param colIndex
	 *            开始写入的列，从0开始
	 * @param data
	 *            待写入的数据
	 */
	public void writeData(int rowIndex, int colIndex, Object[] data) {
		writeData(rowIndex, colIndex, data, new HashMap<Integer, ExcelType>());
	}

	/**
	 * 数据写入Excel中
	 * 
	 * @param rowIndex
	 *            开始写入的行，从0开始
	 * @param colIndex
	 *            开始写入的列，从0开始
	 * @param data
	 *            待写入的数据
	 * @param types
	 *            数据格式
	 */
	public void writeData(int rowIndex, int colIndex, Object[] data,
			Map<Integer, ExcelType> types) {
		for (int i = 0; i < data.length; i++) {
			Object obj = data[i];
			writeData(rowIndex, colIndex + i, StringUtil.obj2Str(obj),
					types.get(i));
			// 存储类型
			typeMap.put(rowIndex, types);
		}
	}

	public void writeData(int rowIndex, int colIndex, String data,
			ExcelType type) {
		row = writableSheet.getRow(rowIndex);
		if (row == null) {
			row = writableSheet.createRow(rowIndex);
		}
		if (type == null) {
			type = ExcelType.STRING;
		}
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		if (type == ExcelType.NUMBER) {
			cell.setCellValue(NumberUtils.toLong(data));
		} else {
			cell.setCellValue(data);
		}
	}
	//带样式
	public void writeData(int rowIndex, int colIndex, String data,
						  ExcelType type,CellStyle cellStyle,boolean isOrg) {
		row = writableSheet.getRow(rowIndex);
		if (row == null) {
			row = writableSheet.createRow(rowIndex);
		}
		if (isOrg){
			row.setHeightInPoints(30);
		}
		if (type == null) {
			type = ExcelType.STRING;
		}
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		if (type == ExcelType.NUMBER) {
			cell.setCellValue(NumberUtils.toLong(data));
			cell.setCellStyle(cellStyle);
		} else {
			cell.setCellValue(data);
			cell.setCellStyle(cellStyle);
		}
	}
	public void writeData(int rowIndex, int colIndex, String data,
			CellStyle cellStyle, short rowHeight) {
		row = writableSheet.getRow(rowIndex);
		if (row == null) {
			row = writableSheet.createRow(rowIndex);
		}
		row.setHeight(rowHeight);
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		// 设置类型
		cell.setCellStyle(cellStyle);
		cell.setCellValue(data);
	}

	public void writeData(int rowIndex, int colIndex, String data) {
		writeData(rowIndex, colIndex, data, ExcelType.STRING);
	}
	public void writeData(int rowIndex, int colIndex, String data,CellStyle cellStyle,boolean isOrg) {
		writeData(rowIndex, colIndex, data, ExcelType.STRING,cellStyle,isOrg);
	}

	/**
	 * 获取当前Sheet最大行号
	 */
	public int getRowMaxNum() {
		return writableSheet.getLastRowNum();
	}

	/**
	 * 拷贝行数据到指定位置
	 * 
	 * @param copyRow
	 *            需要拷贝的行编号
	 * @param toRow
	 *            需要拷贝到的位置
	 */
	public void copyRow(int copyRow, int toRow) {
		Map<Integer, ExcelType> types = typeMap.get(copyRow);
		if (types == null) {
			types = new HashMap();
		}
		typeMap.put(toRow, types);
		copyRow(copyRow, toRow, typeMap.get(copyRow));
	}

	public void copyRow(int srcRow, int toRow, Map<Integer, ExcelType> types) {
		if (types == null) {
			types = new HashMap();
		}
		int columnCount = 0;
		row = writableSheet.getRow(srcRow);
		if (row == null) {
			log.debug("复制的行不存在");
			throw new RuntimeException("复制的行不存在");
		}
		int cellNum = row.getLastCellNum();
		Row sourceRow = row;
		Row targetRow = writableSheet.getRow(toRow);
		if (targetRow == null) {
			targetRow = writableSheet.createRow(toRow);
		}
		// 保留格式，设置行高
		targetRow.setHeight(sourceRow.getHeight());
		for (int i = 0; i < cellNum; i++) {
			Cell cell = sourceRow.getCell(i);
			Cell cellTo = targetRow.getCell(i);
			if (cell != null) {
				if (cellTo == null) {
					cellTo = targetRow.createCell(i);
				}
				cellTo.setCellStyle(cell.getCellStyle());
			}
		}
		while (this.getCellValue(srcRow, columnCount, types.get(columnCount)) != null) {
			writeData(
					toRow,
					columnCount,
					StringUtil.obj2Str(getCellValue(srcRow, columnCount,
							types.get(columnCount))), types.get(columnCount));
			columnCount++;
		}
	}

	/**
	 * 复制单元格样式
	 * 
	 * @param fromStyle
	 *            源格式
	 * @param toStyle
	 *            目标格式
	 */
	private CellStyle copyCellStyle(CellStyle fromStyle, CellStyle toStyle) {
		if (toStyle != null && fromStyle != null) {
			toStyle.setAlignment(fromStyle.getAlignment());
			// 边框和边框颜色
			toStyle.setBorderBottom(fromStyle.getBorderBottom());
			toStyle.setBorderLeft(fromStyle.getBorderLeft());
			toStyle.setBorderRight(fromStyle.getBorderRight());
			toStyle.setBorderTop(fromStyle.getBorderTop());
			toStyle.setTopBorderColor(fromStyle.getTopBorderColor());
			toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());
			toStyle.setRightBorderColor(fromStyle.getRightBorderColor());
			toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());
			toStyle.setDataFormat(fromStyle.getDataFormat());
			toStyle.setFillPattern(fromStyle.getFillPattern());
			toStyle.setHidden(fromStyle.getHidden());
			toStyle.setIndention(fromStyle.getIndention());// 首行缩进
			toStyle.setLocked(fromStyle.getLocked());
			toStyle.setRotation(fromStyle.getRotation());// 旋转
			toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
			toStyle.setWrapText(fromStyle.getWrapText());
		}
		return toStyle;
	}

	/**
	 * 插入行的位置，执行后该下标对应的行为空行
	 * 
	 * @param insertRowIndex
	 *            插入行的索引
	 */
	public void insertRow(int insertRowIndex) {
		writableSheet.createRow(insertRowIndex);
	}

	/**
	 * 文件关闭并刷新流到文件
	 */
	public void closeBook() {
		try {
			if (workbook != null) {
				workbook.write(stream);
				if (stream != null) {
					stream.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("关闭流发生异常！", e);
		}
	}

	private ExcelType getType(int rowNum, int cellNum) {
		Map<Integer, ExcelType> excelTypeMap = typeMap.get(rowNum);
		if (excelTypeMap == null) {
			return ExcelType.STRING;
		} else {
			ExcelType excelType = excelTypeMap.get(cellNum);
			if (excelType == null) {
				return ExcelType.STRING;
			} else {
				return excelType;
			}
		}
	}

	/**
	 * 同一行相邻单元格值相同则进行合并
	 */
	public void merger() {
		int rowMaxNum = this.getRowMaxNum();
		int cellMaxNum = writableSheet.getRow(writableSheet.getFirstRowNum())
				.getLastCellNum();
		for (int i = 0; i < cellMaxNum; i++) {
			mergerCell(i, rowMaxNum);
		}
	}

	public void mergerCell(int cellNum, int rowMaxNum) {
		mergerCell(cellNum, rowMaxNum, false);
	}

	private void mergerCell(int cellNum, int rowMaxNum, boolean mergerNullFlag) {
		for (int i = 0; i < rowMaxNum; i++) {
			ExcelType type = getType(i, cellNum);
			// 单元格的值
			Object cellValue = getCellValue(i, cellNum, type);
			if (!mergerNullFlag) {
				if (StringUtil.isNotEmptyOrNull(cellValue)) {
					i = mergerCell(i, cellNum, cellValue);
				}
			} else {
				i = mergerCell(i, cellNum, cellValue);
			}
		}
	}

	private int mergerCell(int startRow, int cellNum, Object cellValue) {
		int endRowNum = startRow + 1;
		ExcelType type = getType(endRowNum, cellNum);
		while (ObjectUtils.equals(cellValue,
				getCellValue(endRowNum, cellNum, type))) {
			endRowNum++;
			type = getType(endRowNum, cellNum);
		}
		endRowNum = endRowNum - 1;
		// 开始行号与结束行号不同，进行合并处理
		if (startRow != endRowNum) {
			mergedRegion(startRow, endRowNum, cellNum, cellNum);
		}
		return endRowNum;
	}

	/**
	 * 合并单元格操作
	 * 
	 * @param startRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param startCellNum
	 *            开始列
	 * @param endCellNum
	 *            结束列
	 */
	public void mergedRegion(int startRow, int endRow, int startCellNum,
			int endCellNum) {
		CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow,
				endRow, startCellNum, endCellNum);
		writableSheet.addMergedRegion(cellRangeAddress);
	}

	private Object getCellValue(int rowNum, int cellNum, ExcelType excelType) {
		Object cellValue = null;
		if (excelType == null) {
			excelType = ExcelType.STRING;
		}
		int type = excelType.getType();
		Row row = writableSheet.getRow(rowNum);
		if (row != null) {
			Cell cell = writableSheet.getRow(rowNum).getCell(cellNum);
			if (cell != null) {
				cell.setCellType(type);
				if (type == Cell.CELL_TYPE_NUMERIC) {
					cellValue = cell.getNumericCellValue();
				} else if (type == Cell.CELL_TYPE_STRING) {
					cellValue = cell.getStringCellValue();
				}
			}
		}
		return cellValue;
	}
}
