package com.sinosoft.ops.cimp.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PoiUtils {

	public static CellStyle copyCellStyle(Workbook wb, CellStyle fromStyle, CellStyle toStyle) {
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

		// 背景和前景
		toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());
		toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());

		toStyle.setDataFormat(fromStyle.getDataFormat());
		toStyle.setFillPattern(fromStyle.getFillPattern());
		toStyle.setFont(wb.getFontAt(fromStyle.getFontIndex()));
		toStyle.setHidden(fromStyle.getHidden());
		toStyle.setIndention(fromStyle.getIndention());// 首行缩进
		toStyle.setLocked(fromStyle.getLocked());
		toStyle.setRotation(fromStyle.getRotation());// 旋转
		toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());
		toStyle.setWrapText(fromStyle.getWrapText());

		return toStyle;
	}

	public static void copySheet(Workbook wb, Sheet fromSheet, Sheet toSheet, boolean copyValueFlag) {
		// 合并区域处理
		mergerRegion(fromSheet, toSheet);
		Map<Integer, CellStyle> styleMap = new HashMap<Integer, CellStyle>();
		for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
			Row tmpRow = (Row) rowIt.next();
			Row newRow = toSheet.createRow(tmpRow.getRowNum());
			// 行复制
			copyRow(wb, tmpRow, newRow, copyValueFlag, styleMap);
		}
	}
	
	public static void copySheet(Workbook wb, Sheet fromSheet, Sheet toSheet, List<Row> copyRows, boolean copyValueFlag) {
		if (copyRows == null || copyRows.size() == 0) {
			return;
		}
		// 合并区域处理
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
			if (mergedRegionAt.getFirstRow() >= copyRows.size()) {
				break;
			}
			toSheet.addMergedRegion(mergedRegionAt);
		}
		Map<Integer, CellStyle> styleMap = new HashMap<Integer, CellStyle>();
		for (Row tmpRow : copyRows) {
			Row newRow = toSheet.createRow(tmpRow.getRowNum());
			//设置行高
			float height = tmpRow.getHeightInPoints();
			newRow.setHeightInPoints((int)height);
			// 行复制
			copyRow(wb, tmpRow, newRow, copyValueFlag, styleMap);
		}
		toSheet.createFreezePane(0, copyRows.size(), 0, copyRows.size());
		setColumnWidth(fromSheet, toSheet, copyRows);
	}

	public static Map<Integer, CellStyle> copyRow(Workbook wb, Row fromRow, Row toRow, boolean copyValueFlag, Map<Integer, CellStyle> styleMap) {
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			Cell tmpCell = (Cell) cellIt.next();
			Cell newCell = toRow.createCell(tmpCell.getColumnIndex());
			styleMap = copyCell(wb, tmpCell, newCell, copyValueFlag, styleMap);
		}
		return styleMap;
	}

	public static void mergerRegion(Sheet fromSheet, Sheet toSheet) {
		int sheetMergerCount = fromSheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			CellRangeAddress mergedRegionAt = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(mergedRegionAt);
		}
	}

	public static Map<Integer, CellStyle> copyCell(Workbook wb, Cell srcCell, Cell distCell, boolean copyValueFlag, Map<Integer, CellStyle> styleMap) {
		CellStyle newstyle = null;
		CellStyle srcCellStyle = srcCell.getCellStyle();
		int fontHeight = wb.getFontAt(srcCellStyle.getFontIndex()).getFontHeightInPoints();
		if (styleMap.containsKey(fontHeight)) {
			newstyle = styleMap.get(fontHeight);
		} else {
			newstyle = wb.createCellStyle();
			newstyle = copyCellStyle(wb, srcCellStyle, newstyle);
			styleMap.put(fontHeight, newstyle);
		}
		//distCell.setEncoding(srcCell.getEncoding());
		// 样式
		distCell.setCellStyle(newstyle);
		// 评论
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		// 不同数据类型处理
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);
		if (copyValueFlag) {
			if (srcCellType == Cell.CELL_TYPE_NUMERIC) {
				if (DateUtil.isCellDateFormatted(srcCell)) {
					distCell.setCellValue(srcCell.getDateCellValue());
				} else {
					distCell.setCellValue(srcCell.getNumericCellValue());
				}
			} else if (srcCellType == Cell.CELL_TYPE_STRING) {
				distCell.setCellValue(srcCell.getRichStringCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_BLANK) {
				// nothing21
			} else if (srcCellType == Cell.CELL_TYPE_BOOLEAN) {
				distCell.setCellValue(srcCell.getBooleanCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_ERROR) {
				distCell.setCellErrorValue(srcCell.getErrorCellValue());
			} else if (srcCellType == Cell.CELL_TYPE_FORMULA) {
				distCell.setCellFormula(srcCell.getCellFormula());
			} else { // nothing29
			}
		}
		return styleMap;
	}
	
	public static void setColumnWidth(Sheet fromSheet, Sheet toSheet, List<Row> copyRows) {
		if (copyRows.size() > 0) {
			int cols = copyRows.get(0).getLastCellNum();
			for (int i = 0; i < cols; i++) {
				int width = fromSheet.getColumnWidth(i);
				toSheet.setColumnWidth(i, width);
			}
		}
	}
}
