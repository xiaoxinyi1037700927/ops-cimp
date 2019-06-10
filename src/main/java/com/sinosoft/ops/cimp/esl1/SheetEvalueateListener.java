package com.sinosoft.ops.cimp.esl1;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SheetEvalueateListener extends FormulaEvalueateListener{
	
	private static final Logger logger = LoggerFactory.getLogger(SheetEvalueateListener.class);

	private int startRow;
	
	private int startCol;
	
	private int endRow;
	
	private int endCol;
	
	private Map<String, Map<String,Object>> dataMap;
	
	private Map<String,String> calculationFormula = new ConcurrentHashMap<String, String>();
	
	private Map<String, String> calculationResult = new ConcurrentHashMap<String, String>();
	
	private List<Map<String, Object>> logicResult;
	
	private Byte formulaType;
	
	private Map<String, Object> logicRule;
	
	private Map<String, Object> logicData;
	
	public SheetEvalueateListener(ESLParser parser,int startRow,int startCol,int endRow,int endCol,Map<String, Map<String,Object>> dataMap) {
		super(parser);
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
		this.dataMap = dataMap;
		this.logicResult = new ArrayList<>();
	}
	
	public SheetEvalueateListener(ESLParser parser,int startRow,int startCol,int endRow,int endCol,Map<String, Map<String,Object>> dataMap,Byte formulaType) {
		super(parser);
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
		this.dataMap = dataMap;
		this.formulaType = formulaType;
		this.logicResult = new ArrayList<>();
	}
	
	@Override
	public void exitAssignStatement(ESLParser.AssignStatementContext ctx) {
		logger.debug(ctx.getText());

		Range valRange = rangeStack.pop();
		Range refRange = rangeStack.pop();

		refRange.statement = ctx.getText();
		logger.info("--------------------------");
		logger.info(refRange.statement);

		for (int i = 0; i < valRange.tableCount; i++) {
			for (int j = 0; j < valRange.rowCount; j++) {
				for (int k = 0; k < valRange.colCount; k++) {
					// 规则说明
					String rule = refRange.ruleDescs.get(i + "-" + j + "-" + k);
					rule += " = " + valRange.ruleDescs.get(i + "-" + j + "-" + k);
					logger.info(rule);
					
					//暂存规则  key: 表-行-列
					calculationFormula.put(refRange.tableList.get(i)+"-"+refRange.rowList.get(j)+"-"+refRange.colList.get(k), rule);

					// 数据说明：赋值语句左侧的数据说明=右侧的数据结果
					String data = valRange.dataValues.get(i + "-" + j + "-" + k).toString();
					refRange.ruleDatas.put(i + "-" + j + "-" + k, data);

					data = data + " = " + valRange.ruleDatas.get(i + "-" + j + "-" + k);
					logger.info(data);
					
					//暂存数据 key：表-行-列
					calculationResult.put(refRange.tableList.get(i)+"-"+refRange.rowList.get(j)+"-"+refRange.colList.get(k), data);

					// 赋值操作
					double value = valRange.dataValues.get(i + "-" + j + "-" + k);
					String sLabel = "T[" + refRange.tableList.get(i);
					sLabel += "]R[" + refRange.rowList.get(j);
					sLabel += "]C[" + refRange.colList.get(k) + "]";
					logger.info(sLabel + " = " + value);
					
					Map<String,Object> sheetData = dataMap.get(refRange.tableList.get(i)+"-"+refRange.rowList.get(j)+"-"+refRange.colList.get(k));
					if(sheetData != null){
						sheetData.put("stringValue", value+"");
					}
					
					
				}
			}
		}

	}
	
	@Override
	public void exitLogicStatement(ESLParser.LogicStatementContext ctx) {
		logger.debug(ctx.getText());

//		Range range = rangeStack.pop();
//		range.statement = ctx.getText();
//		logger.info("--------------------------");
//		logger.info(range.statement);
//		List<Map<String,Object>> childs = new ArrayList<>();
//		boolean flag = true;
//		for (int i = 0; i < range.tableCount; i++) {
//			for (int j = 0; j < range.rowCount; j++) {
//				for (int k = 0; k < range.colCount; k++) {
//					// 赋值操作
//					boolean value = range.boolValues.get(i + "-" + j + "-" + k);
//					logger.info("Result = " + value);
//					
//
//					String rule = range.ruleDescs.get(i + "-" + j + "-" + k);
//					logger.info(rule);
//
//					String data = range.ruleDatas.get(i + "-" + j + "-" + k);
//					logger.info(data);
//					
//					Map<String, Object> child = new HashMap<>();
//					child.put("value", value);
//					child.put("rule", rule);
//					child.put("data", data);
//					child.put("type", formulaType);
//					childs.add(child);
//					//只要有一个为false则该公式为未通过
//					if(!value){
//						flag = false;
//					}
//				}
//			}
//		}
//		Map<String,Object> result = new HashMap<String, Object>();
//		result.put("statement", range.statement);
//		result.put("value", flag);
//		result.put("type", formulaType);
//		result.put("childs", childs);
//		logicResult.add(result);
		// 取当前语句的父节点，如果是then语句，当前语句为then分支，不弹栈交由父节点处理
		int ruleIndex = ctx.getParent().getRuleIndex();
		if(ruleIndex == ESLParser.RULE_thenLogicStatement){
			return;
		}

		Range range = rangeStack.pop();
		range.statement = ctx.getText();
		logger.debug("--------------------------");
		logger.debug(range.statement);
		
		List<Map<String,Object>> childs = new ArrayList<>();
		boolean flag = true;
		for (int i = 0; i < range.tableCount; i++) {
			for (int j = 0; j < range.rowCount; j++) {
				for (int k = 0; k < range.colCount; k++) {
					// 赋值操作
					boolean value = range.boolValues.get(i + "-" + j + "-" + k);
					logger.debug("Result = " + value);

					String rule = range.ruleDescs.get(i + "-" + j + "-" + k);
					logger.debug(rule);

					String data = range.ruleDatas.get(i + "-" + j + "-" + k);
					logger.debug(data);
					
					Map<String, Object> child = new HashMap<>();
					child.put("value", value);
					child.put("rule", rule);
					child.put("data", data);
					child.put("type", formulaType);
					childs.add(child);
					//只要有一个为false则该公式为未通过
					if(!value){
						flag = false;
						range.boolValue = false;
					}
				}
			}
		}
		if (ruleIndex == ESLParser.RULE_ifStatement) {
			ifRangeStack.push(range);// 将range对象转移到ifRangeStack，供赋值语句判断
		}else{
			//如果不是if语句块，保留校核结果
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("statement", range.statement);
			result.put("value", flag);
			result.put("type", formulaType);
			result.put("childs", childs);
			result.put("status", "1");
			logicResult.add(result);
		}
	}
	
	@Override
	public void exitThenLogicStatement(ESLParser.ThenLogicStatementContext ctx) {
		logger.debug(ctx.getText()+"exitThenLogicStatement");
		Range ifRange = ifRangeStack.pop();
		Range range = rangeStack.pop();
		range.statement = ctx.parent.getText();
		logger.debug("--------------------------");
		logger.debug(range.statement);
		if(ifRange.boolValue){
			List<Map<String,Object>> childs = new ArrayList<>();
			boolean flag = true;
			for (int i = 0; i < range.tableCount; i++) {
				for (int j = 0; j < range.rowCount; j++) {
					for (int k = 0; k < range.colCount; k++) {
						Boolean booleanValue = ifRange.boolValues.get(i + "-" + j + "-" + k);
						
						String string = ifRange.ruleDatas.get(i + "-" + j + "-" + k);
						String string2 = ifRange.ruleDescs.get(i + "-" + j + "-" + k);
						
						// 赋值操作
						boolean value = range.boolValues.get(i + "-" + j + "-" + k);
						logger.debug("Result = " + value);
						
						String rule = range.ruleDescs.get(i + "-" + j + "-" + k);
						logger.debug(rule);

						String data = range.ruleDatas.get(i + "-" + j + "-" + k);
						logger.debug(data);
						
						String info = "因为校核："+string2+"数据："+string+"结果为： "+booleanValue+"所以："+rule+"参与校核，校核数据为："+data+"校核结果为："+value;
						logger.debug(info);
						
						Map<String, Object> child = new HashMap<>();
						child.put("value", value);
						child.put("rule", rule);
						child.put("data", data);
						child.put("type", formulaType);
						childs.add(child);
						//只要有一个为false则该公式为未通过
						if(!value){
							flag = false;
							range.boolValue = false;
						}
					}
				}
			}
			//状态为1 正常校核
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("statement", range.statement);
			result.put("value", flag);
			result.put("type", formulaType);
			result.put("childs", childs);
			result.put("status", "1");
			logicResult.add(result);
		}else{
			//状态为0 if语句 条件不符合 then不参与校核
			Map<String,Object> result = new HashMap<String, Object>();
			result.put("statement", range.statement);
			result.put("type", formulaType);
			result.put("status", "0");
			logicResult.add(result);
		}

		
		
	}
	
	// 根据根据行所在的块，确定最大行数
	@Override
	public int getMaxRow(int table, int row) {
		return this.endRow;
	}

	// 根据模列所在的块，确定最大列数
	@Override
	public int getMaxCol(int table, int col) {
		return this.endCol;
	}

	// 根据根据行所在的块，确定最大行数
	@Override
	public int getMinRow(int table, int row) {
		return this.startRow;
	}

	// 根据模列所在的块，确定最大列数
	@Override
	public int getMinCol(int table, int col) {
		return this.startCol;
	}

	// 根据行号确定所有列
	@Override
	public List<Integer> getRowList(int table, int col) {
		List<Integer> rowList = new ArrayList<Integer>();
		for (int i = 0; i < this.endRow; i++) {
			rowList.add(i + 1);
		}
		return rowList;
	}

	// 根据列号确定所有行
	@Override
	public List<Integer> getColList(int table, int row) {
		List<Integer> colList = new ArrayList<Integer>();
		for (int i = 0; i < this.endCol; i++) {
			colList.add(i + 1);
		}
		return colList;
	}
	
	// 为指定范围的单元格取数
	@Override
	public Range getRangeData(Range range) {
		int ti = 0;
		for (int i = 0; i < range.tableCount; i++) {
			ti=range.tableList.get(i);
			int rj = 0;
			for (int j = 0; j < range.rowCount; j++) {
				rj = range.rowList.get(j);
				int ck = 0;
				for (int k = 0; k < range.colCount; k++) {
					ck = range.colList.get(k);
					// 静态数组不能满足要求，换成map或者List类，三维存储，可能map更合适
					// range.dataValues[i][j][k] = 1;
//					int t = range.tableList.get(i);
//					int r = range.rowList.get(j);
//					int c = range.colList.get(k);
					Double value = 0d;
					Map<String,Object> sheetData = dataMap.get(ti + "-" + rj + "-" + ck);
					if(sheetData != null && sheetData.get("stringValue")!=null && StringUtils.isNotBlank(sheetData.get("stringValue").toString())){
						try {
							value = Double.parseDouble(sheetData.get("stringValue").toString());
						} catch (NumberFormatException e) {
						}
					}
					range.dataValues.put(i + "-" + j + "-" + k, value);
					range.ruleTrc.put(ti + "-" + rj + "-" + ck, i + "-" + j + "-" + k);
					
				}
				
			}
			
		}
		return range;
	}

	public Map<String, String> getCalculationFormula() {
		return calculationFormula;
	}

	public void setCalculationFormula(Map<String, String> calculationFormula) {
		this.calculationFormula = calculationFormula;
	}

	public Map<String, String> getCalculationResult() {
		return calculationResult;
	}

	public void setCalculationResult(Map<String, String> calculationResult) {
		this.calculationResult = calculationResult;
	}

	public List<Map<String, Object>> getLogicResult() {
		return logicResult;
	}

	public void setLogicResult(List<Map<String, Object>> logicResult) {
		this.logicResult = logicResult;
	}

	
}
