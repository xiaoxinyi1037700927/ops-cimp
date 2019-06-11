package com.sinosoft.ops.cimp.entity.sheet;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 中间表解析类类
 */
public class IntermediateFormAnalyze  {
	
	//private static final Logger logger = LoggerFactory.getLogger(IntermediateFormAnalyze.class);

	/***/
	private static final String SET_ID = "SetID";
	
	private static final String DESCRIPTION = "Description";
	
	private static final String FIELD_NAME = "FieldName";
	
	private static final String FIELD_DEC = "FieldDec";
	
	private String setInfoName;
	
	private String description;
	
	private Map<String, String> itemMap;
	
	private Map<String, String> fieldDecMap;
	
	// Excel工作本
	private Workbook workbook;

	// Excel对应输入流
	private InputStream inputStream;
	
	private Sheet currentSheet;
	
	// 当前行索引
	private int currentRowIndex;

	// 文件类型
	private String fileType;
	
	// 当前Sheet页，默认为0
	private int currentSheetIndex;

//	public IntermediateFormAnalyze() {
//	}
//
//	public IntermediateFormAnalyze(Sheet sheet) {
//		this.sheet = sheet;
//		getInfoSetName();
//	}
	
	public IntermediateFormAnalyze(File file){
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
			initInfoSet();
			initItemList();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("文件未找到异常", e);
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常", e);
		}
	}
	
	public IntermediateFormAnalyze(InputStream inputStream,String fileName){
		try {
			this.inputStream = inputStream;
			if (fileName.lastIndexOf("xlsx") != -1) {
				fileType = "xlsx";
				workbook = new XSSFWorkbook(inputStream);
			} else {
				fileType = "xls";
				workbook = new HSSFWorkbook(inputStream);
			}
			currentSheetIndex = 0;
			currentSheet = workbook.getSheetAt(currentSheetIndex);
			currentRowIndex = 0;
			initInfoSet();
			initItemList();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("文件未找到异常", e);
		} catch (IOException e) {
			throw new RuntimeException("输入输出流异常", e);
		}
	}
	
	public void initInfoSet(){
		boolean flag = false;
		
		Iterator<Row> rowIterator = currentSheet.rowIterator();
		
		while(rowIterator.hasNext()){
			
			Row row = rowIterator.next();
			int rowNum = row.getRowNum();
			
			Iterator<Cell> cellIterator = row.cellIterator();
			boolean flag1 = false,flag2 = false;
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				int ci = cell.getColumnIndex();
				//infoset名称
				if(StringUtils.equalsIgnoreCase(cell.getStringCellValue(), SET_ID)){
					this.setInfoName = currentSheet.getRow(rowNum+1).getCell(ci).getStringCellValue();
					flag1 = true;
					
				}else if(StringUtils.equalsIgnoreCase(cell.getStringCellValue(), DESCRIPTION)){
					this.description = currentSheet.getRow(rowNum+1).getCell(ci).getStringCellValue();
					flag2 = true;
					
				}
				if(flag1 && flag2) {
					flag = true;
					break;
				}
			}
			if(flag) break;
		}
		
	}
	
	public void initItemList(){
		Iterator<Row> rowIterator = currentSheet.rowIterator();
//		itemMap = new HashMap<>();
//		fieldDecMap = new HashMap<>();
		itemMap = new TreeMap<>();
		fieldDecMap = new TreeMap<>();
		while(rowIterator.hasNext()){
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			int first = 0,second = 0,num = 0;
			while(cellIterator.hasNext()){
				Cell cell = cellIterator.next();
				if(StringUtils.equalsIgnoreCase(FIELD_NAME, cell.getStringCellValue())){
					num++;
				}else{
					continue;
				}
				if(num == 1){
					first = cell.getColumnIndex();
				}else if(num == 2){
					second = cell.getColumnIndex();
					break;
				}
			}
			if(num == 2){
				while(rowIterator.hasNext()){
					Row row2 = rowIterator.next();
					if(StringUtils.isNotBlank(row2.getCell(second).getStringCellValue())){
						itemMap.put(row2.getCell(first).getStringCellValue(), 
								row2.getCell(second).getStringCellValue());
						fieldDecMap.put(row2.getCell(first).getStringCellValue(),
								row2.getCell(first+1).getStringCellValue());
					}
					
				}
			}
			
		}
	}
	
	public boolean next(){
		if(currentSheetIndex >= workbook.getNumberOfSheets()){
			return false;
		}
		currentSheetIndex++;
		currentSheet = workbook.getSheetAt(currentSheetIndex);
		initInfoSet();
		initItemList();
		return true;
	}
	
	
	
	public String getSetInfoName() {
		return setInfoName;
	}

	public void setSetInfoName(String setInfoName) {
		this.setInfoName = setInfoName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Map<String, String> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, String> itemMap) {
		this.itemMap = itemMap;
	}
	
	public Map<String, String> getFieldDecMap() {
		return fieldDecMap;
	}

	public void setFieldDecMap(Map<String, String> fieldDecMap) {
		this.fieldDecMap = fieldDecMap;
	}

	public static void main(String[] args){
		File file = new File("C:/Users/sunch/Desktop/中间表/中间表初--20181024.xlsx");
		
		IntermediateFormAnalyze analyze = new IntermediateFormAnalyze(file);
		
		
	}
}
