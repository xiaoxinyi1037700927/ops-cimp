package com.sinosoft.ops.cimp.common.excel;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SExcelWriter {

	private static Logger logger = LoggerFactory.getLogger(SExcelWriter.class);

	private SXSSFWorkbook workbook = null;

	private SXSSFSheet writableSheet = null;

	private Row row = null;

	private OutputStream stream;
	private FileOutputStream os = null;
	private List<Row> headerRows = null;

	public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getWritableSheet() {
		return writableSheet;
	}

	public Row getRow() {
		return row;
	}

	public List<Row> getHeaderRows() {
		return headerRows;
	}

	public SExcelWriter(File outFile, File templateFile, int headerRowCnt) {
		FileInputStream is = null;
		BufferedInputStream bis = null;
		try {
			if (templateFile.getName().endsWith(".xls")) {
			} else if (templateFile.getName().endsWith(".xlsx")) {
				is = new FileInputStream(templateFile);
				bis = new BufferedInputStream(is);
				XSSFWorkbook wb = new XSSFWorkbook(bis);
				if (headerRowCnt > 0) {
					List<Row> rows = new ArrayList<Row>();
					XSSFSheet sheet = wb.getSheetAt(0);
					for (int i = 0; i < headerRowCnt; i++) {
						rows.add(sheet.getRow(i));
					}
					headerRows = rows;
				}
				this.workbook = new SXSSFWorkbook(wb, 100);
				this.workbook.setCompressTempFiles(false);
			}
			os = new FileOutputStream(outFile);
			stream = new BufferedOutputStream(os);
			assert workbook != null;
			workbook.setSheetName(0, "领导职数");
			this.writableSheet = (SXSSFSheet) workbook.getSheetAt(0);
			this.writableSheet.setRandomAccessWindowSize(100);
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常！", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
					logger.error("关闭流发生异常！", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					logger.error("关闭流发生异常！", e);
				}
			}
		}
	}

	public void writeColorData(int rowIndex, int colIndex, String data, Map<String, Object> styleMap, ExcelType type,
                               CellStyle cellStyle, IndexedColors color, boolean wrapText, boolean bold, short align) {
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
		if (wrapText || color != null || type == ExcelType.NUMBER || bold || align != HSSFCellStyle.ALIGN_LEFT) {
			String styleKey = String.valueOf(wrapText) + (color == null ? "null" : String.valueOf(color.getIndex()))
					+ String.valueOf(type.getType()) + String.valueOf(bold) + String.valueOf(align);
			if (styleMap.containsKey(styleKey)) {
				cellStyle = (CellStyle) (styleMap.get(styleKey));
			} else {
				cellStyle = workbook.createCellStyle();
				cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
				cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
				cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				if (wrapText) {
					cellStyle.setWrapText(true);
				}
				if (color != null) {
					cellStyle.setFillForegroundColor(color.getIndex());
					cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				}
				if (bold) {
					Font font = workbook.createFont();
					font.setFontHeightInPoints((short) 10);
					font.setFontName("宋体");
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					cellStyle.setFont(font);
				}
				if (type == ExcelType.NUMBER) {
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
				}
				if (align != HSSFCellStyle.ALIGN_LEFT) {
					cellStyle.setAlignment(align);
				}
				styleMap.put(styleKey, cellStyle);
			}
		}
		if (type == ExcelType.NUMBER && !"".equals(data) && !" ".equals(data)) {
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(NumberUtils.toLong(data));
			cell.setCellStyle(cellStyle);
		} else {
			cell.setCellValue(data);
			cell.setCellStyle(cellStyle);
		}
	}

	public void writeRowHeight(int rowIndex, int colIndex, int fontSize, Map<String, Object> styleMap) {
		row = writableSheet.getRow(rowIndex);
		if (row == null) {
			row = writableSheet.createRow(rowIndex);
		}
		Cell cell = row.getCell(colIndex);
		if (cell == null) {
			cell = row.createCell(colIndex);
		}
		String styleKey = "ROW_HEIGHT";
		CellStyle cellStyle = null;
		if (styleMap.containsKey(styleKey)) {
			cellStyle = (CellStyle) (styleMap.get(styleKey));
		} else {
			cellStyle = workbook.createCellStyle();
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_NONE);
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_NONE);
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_NONE);
			cellStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle.setWrapText(true);
			Font font = workbook.createFont();
			font.setFontHeightInPoints((short) fontSize);
			font.setFontName("宋体");
			cellStyle.setFont(font);
			styleMap.put(styleKey, cellStyle);
		}
		cell.setCellValue("　\n　");
		cell.setCellStyle(cellStyle);
	}

	public void merge(int startrow, int endrow, int startcol, int endcol) {
		writableSheet.addMergedRegion(new CellRangeAddress(startrow, endrow, startcol, endcol));
	}

	public void flushRows() {
		try {
			writableSheet.flushRows();
		} catch (Exception e) {
			throw new RuntimeException("flushRows发生异常！", e);
		}
	}

	public void close() {
		try {
			if (workbook != null) {
				workbook.write(stream);
				if (stream != null) {
					stream.flush();
					stream.close();
				}
				workbook.dispose();
			}
		} catch (Exception e) {
			throw new RuntimeException("关闭流发生异常！", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					logger.error("关闭流发生异常！", e);
				}
			}
		}
	}

	public void newPage() {
		this.flushRows();
		int sheets = workbook.getNumberOfSheets();
		String name = "领导职数";
		SXSSFSheet newSheet = (SXSSFSheet) workbook.createSheet(name + "-" + sheets);
		newSheet.setRandomAccessWindowSize(100);
		if (headerRows != null && headerRows.size() > 0) {
			PoiUtils.copySheet(workbook, (SXSSFSheet) workbook.getSheetAt(0), newSheet, headerRows, true);
		}
		this.writableSheet = newSheet;
	}

	public SExcelWriter reOpen(File outFile, File srcFile) {
		FileInputStream is = null;
		BufferedInputStream bis = null;
		try {
			this.flushRows();
			if (this.workbook != null) {
				this.workbook.write(stream);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error("sleep失败！", e);
				}
				if (this.stream != null) {
					this.stream.flush();
					this.stream.close();
				}
				this.workbook.dispose();
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
					logger.error("关闭流发生异常！", e);
				}
			}
			if (srcFile.getName().endsWith(".xlsx")) {
				is = new FileInputStream(srcFile);
				bis = new BufferedInputStream(is);
				XSSFWorkbook wb = new XSSFWorkbook(is);
				this.workbook = new SXSSFWorkbook(wb, 100);
				this.workbook.setCompressTempFiles(false);
			}
			os = new FileOutputStream(outFile);
			stream = new BufferedOutputStream(os);
			assert this.workbook != null;
			int sheets = this.workbook.getNumberOfSheets();
			String name = "领导职数";
			SXSSFSheet newSheet = (SXSSFSheet) this.workbook.createSheet(name + "-" + sheets);
			newSheet.setRandomAccessWindowSize(100);
			if (headerRows != null && headerRows.size() > 0) {
				PoiUtils.copySheet(workbook, (SXSSFSheet) workbook.getSheetAt(0), newSheet, headerRows, true);
			}
			this.writableSheet = newSheet;
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常！", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
					logger.error("关闭流发生异常！", e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					logger.error("关闭流发生异常！", e);
				}
			}
		}
		return this;
	}

	public void writeData(int rowIndex, int colIndex, String data, ExcelType type, CellStyle cellStyle, boolean isOrg) {
		row = writableSheet.getRow(rowIndex);
		if (row == null) {
			row = writableSheet.createRow(rowIndex);
		}
		if (isOrg) {
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
}
