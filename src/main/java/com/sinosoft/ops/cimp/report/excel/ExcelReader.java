package com.sinosoft.ops.cimp.report.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rain chen on 2017/8/7.
 * 
 * @author rain chen
 * @version 1.0
 * @since 1.0 Copyright (C) 2017. SinSoft All Rights Received
 */
public class ExcelReader {

	/**
	 * 日志输出工具
	 */
	private static Logger logger = LoggerFactory.getLogger(ExcelReader.class);

	// Excel工作本
	private Workbook workbook;

	// Excel对应输入流
	private InputStream inputStream;

	// 当前Sheet页，默认为0
	private int currentSheetIndex;

	// 当前Sheet页
	private Sheet currentSheet;

	// Sheet页中的一行
	private Row row;

	// 当前行索引
	private int currentRowIndex;

	// 文件类型
	private String fileType;

	/**
	 * 根据文件创建一个ExcelReader
	 * 
	 * @param file
	 *            Excel文件（扩展名为xlsx或者xls）
	 */
	public ExcelReader(File file) {
		try {
			inputStream = new FileInputStream(file);
			if (file.getName().lastIndexOf("xlsx") != -1) {
				fileType = "xlsx";
				workbook = new XSSFWorkbook(inputStream);
			} else {
				fileType = "xls";
				workbook = new HSSFWorkbook(inputStream);
			}
			currentSheetIndex = 0;
			currentSheet = workbook.getSheetAt(currentSheetIndex);
			currentRowIndex = 0;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("文件未找到异常", e);
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常", e);
		}
	}

	/**
	 * 根据输入流和文件名称创建一个ExcelReader
	 * 
	 * @param inputStream
	 *            Excel文件输入流
	 * @param fileName
	 *            Excel的文件名称（a.xlsx or b.xls）
	 */
	public ExcelReader(InputStream inputStream, String fileName) {
		try {
			if (fileName.endsWith("xlsx")) {
				fileType = "xlsx";
				workbook = new XSSFWorkbook(inputStream);
			} else {
				fileType = "xls";
				workbook = new HSSFWorkbook(inputStream);
			}
			currentSheetIndex = 0;
			currentSheet = workbook.getSheetAt(currentSheetIndex);
			currentRowIndex = 0;
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常", e);
		}
	}

	public ExcelReader(ByteArrayInputStream inputStream, String fileName) {
		try {
			if (fileName.endsWith("xlsx")) {
				fileType = "xlsx";
				workbook = new XSSFWorkbook(inputStream);
			} else {
				fileType = "xls";
				workbook = new HSSFWorkbook(inputStream);
			}
			currentSheetIndex = 0;
			currentSheet = workbook.getSheetAt(currentSheetIndex);
			currentRowIndex = 0;
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常", e);
		}
	}

	/**
	 * 获得工作本的总Sheet页数
	 * 
	 * @return sheet页数
	 */
	public int getNumberOfSheets() {
		return workbook.getNumberOfSheets();
	}

	/**
	 * 根据sheet编号，改变当前读取的sheet页
	 * 
	 * @param index
	 *            sheet编号（大于0小于sheet页数）
	 */
	public void changeSheet(int index) {
		if ((index < 0) || (index > workbook.getNumberOfSheets())) {
			if (logger.isErrorEnabled()) {
				logger.error("number of sheet " + workbook.getNumberOfSheets()
						+ " index is " + index);
			}
			throw new RuntimeException("未找到指定的Sheet页" + index);
		}
		currentSheetIndex = index;
		currentSheet = workbook.getSheetAt(index);
	}

	/**
	 * 根据sheet页名改变当前Sheet页
	 * 
	 * @param sheetName
	 *            sheet页名
	 */
	public void changeSheet(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		changeSheet(index);
	}

	/**
	 * 获取当前Sheet页指定行对应列区间的数据
	 * 
	 * @param rowIndex
	 *            当前Sheet的读取行号，从0开始编号
	 * @param colStartIndex
	 *            要读取的开始列号
	 * @param colEndIndex
	 *            要读取的结束列号
	 * @return rowIndex对应行从colStartIndex单元格到colEndIndex单元格的数据
	 */
	public String[] readData(int rowIndex, int colStartIndex, int colEndIndex) {
		if (rowIndex < 0 || colStartIndex < 0 || colEndIndex < 0
				|| (colEndIndex - colStartIndex) < 0) {
			throw new RuntimeException(
					"行号不能小于0，开始列不能小于0，结束列不能小于0，结束列与开始列的差不能小于0。[rowIndex,colStartIndex,colEndIndex]["
							+ rowIndex
							+ ","
							+ rowIndex
							+ ","
							+ colEndIndex
							+ "]");
		}
		int rowSize = colEndIndex - colStartIndex;
		if (rowSize < 0)
			rowSize = 0;
		String[] result = new String[rowSize + 1];
		Row currentRow = currentSheet.getRow(rowIndex);
		if (currentRow != null) {
			for (int i = 0; i <= rowSize; i++) {
				result[i] = cell2Str(currentRow.getCell(colStartIndex + i));
			}
			return result;
		} else {
			return new String[0];
		}
	}

	/**
	 * 读取当前Sheet页指定行所有列的数据
	 * 
	 * @param rowIndex
	 *            当前Sheet需要读取的行号，从0开始
	 * @return rowIndex对应的所有数据
	 */
	public String[] readData(int rowIndex) {
		Row currentRow = currentSheet.getRow(rowIndex);
		if (currentRow != null) {
			return readData(rowIndex, 0, currentRow.getLastCellNum() - 1);
		} else {
			return new String[0];
		}
	}

	/**
	 * 获取Sheet页名
	 * 
	 * @param sheetIndex
	 *            sheet页编号，从0开始
	 * @return sheet页名称
	 */
	public String getSheetName(int sheetIndex) {
		return workbook.getSheetName(sheetIndex);
	}

	/**
	 * 获取当前Sheet页总记录数
	 * 
	 * @return 当前Sheet总记录数
	 */
	public int getRowNum() {
		return currentSheet.getLastRowNum() + 1;
	}

	/**
	 * 获取行的总列数
	 * 
	 * @param rowIndex
	 *            行号，从0开始
	 * @return rowIndex行的总列数，若当前列不存在则返回0
	 */
	public int getRowColNum(int rowIndex) {
		Row currentRow = currentSheet.getRow(rowIndex);
		if (currentRow != null) {
			return currentRow.getLastCellNum();
		} else {
			return 0;
		}
	}

	/**
	 * 读取Excel单元格数据转换为字符串
	 * 
	 * @param cell
	 *            Excel单元格对象
	 * @return 字符串数据
	 */
	private String cell2Str(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			cellValue = "";
		} else {
			int cellType = cell.getCellType();
			if (cellType == 1) {
				cellValue = cell.getRichStringCellValue().getString();
			} else if (cellType == 0) {
				// 此处存在一个poi的一个bug，必须这样处理
				// 1.先获取原单元格的原数据格式数据（数字类型）
				// 2.设置单元格类型为字符串
				// 3.获取该单元格字符串数据
				// 4.再次设置单元格的值为步骤1获取到的值
				// 5.再次设置单元格格式为数字型
				double numericCellValue = cell.getNumericCellValue();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cellValue = cell.getRichStringCellValue().getString();
				cell.setCellValue(numericCellValue);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			} else if (cellType == 2) {
				cellValue = cell.getCellFormula();
			} else if (cellType == 3) {
				cellValue = "";
			} else if (cellType == 4) {
				cellValue = String.valueOf(cell.getBooleanCellValue());
			} else if (cellType == 5) {
				cellValue = "";
			}
		}
		return cellValue;
	}

	/**
	 * 判断是否存在下一个Sheet页
	 * 
	 * @return 存在返回true否则返回false
	 */
	public boolean nextSheet() {
		if (currentSheetIndex >= workbook.getNumberOfSheets()) {
			return false;
		}
		currentSheet = workbook.getSheetAt(currentSheetIndex);
		currentSheetIndex++;
		currentRowIndex = 0;
		return true;
	}

	/**
	 * 返回当前行
	 * 
	 * @return 当前行
	 */
	public Row row() {
		return getRow(currentRowIndex);
	}

	/**
	 * 获取指定行
	 * 
	 * @param currentRowIndex
	 *            行索引
	 * @return 指定行对象
	 */
	public Row getRow(int currentRowIndex) {
		if (currentRowIndex >= currentSheet.getPhysicalNumberOfRows()) {
			return null;
		}
		return currentSheet.getRow(currentRowIndex);
	}

	/**
	 * 设置当前行号
	 * 
	 * @param currentRowIndex
	 *            行号
	 */
	public void setCurrentRow(int currentRowIndex) {
		this.currentRowIndex = currentRowIndex;
	}

	/**
	 * 判断是否为最后一行
	 * 
	 * @return 最后一行返回true否则返回false
	 */
	public boolean nextRow() {
		if (currentRowIndex > currentSheet.getPhysicalNumberOfRows()) {
			return false;
		}
		row = currentSheet.getRow(currentRowIndex);
		currentRowIndex++;
		return true;
	}

	/**
	 * 读取row中所有值，返回字符串数组
	 * 
	 * @param hRow
	 *            Excel的Row对象
	 * @return 该行的所有值
	 */
	public String[] row2Str(Row hRow) {
		if (hRow == null) {
			throw new RuntimeException("该行不存在");
		}
		List<String> result = new ArrayList();
		int j = 0;
		boolean isNull = true;
		DecimalFormat format = new DecimalFormat("#");
		String value;
		double tmp;
		for (int i = 0; i < hRow.getLastCellNum() + j; i++) {
			if (hRow.getCell(i) == null) {
				j++;
				result.add("");
			} else if ("".equals(hRow.getCell(i).toString().trim())) {
				result.add("");
			} else {
				isNull = false;
				value = hRow.getCell(i).toString();
				// 判断是否包含科学计数法
				if (value.contains(".") && value.contains("E")) {
					tmp = Double.parseDouble(value);
					result.add(format.format(tmp));
				} else {
					result.add(value);
				}
			}
		}
		if (isNull) {
			return null;
		}// StringUtils.toStringArray(result);
		return result.toArray(new String[result.size()]);
	}

	/**
	 * 返回文档类型
	 * 
	 * @return xlsx 或 xls
	 */
	public String getFileType() {
		return this.fileType;
	}

	public Row getRow() {
		return row;
	}
}
