package com.sinosoft.ops.cimp.esl1;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

//**********************************************//
//ESL公式解析器  yuxingliang 2018-06-09
//
//监听两个事件：
//	1）exitAssignStatement----赋值语句解析计算完成
//	输出：
//	statement:整句赋值语句
//	rule:拆分后的赋值语句（数据表示）
//	data:拆分后的赋值语句（单元表示）
//	value:计算结果
//
//	2）exitLogicStatement-----逻辑语句解析计算完成
//	输出：
//	statement:整句逻辑语句
//	rule:拆分后的逻辑语句（单元表示）
//	data:拆分后的逻辑局域（数据表示）
//	value:计算结果
//
//**********************************************//
public class FormulaEvalueateListener extends ESLBaseListener {

	private static final Logger logger = LogManager.getLogger(FormulaEvalueateListener.class);

	public class Range {
		int rangeType; // 存储类型：0：未定义；1：向量（引用范围）；2：标量（数值）；
		List<Integer> tableList, rowList, colList; // 存放一个计算范围的表号、行号、列号
		int tableCount, rowCount, colCount; // 一个计算范围内的表的个数、行数、列数
		Map<String, Double> dataValues; // 存放一个赋值语句内的计算范围内的所有结果值
		Map<String, Boolean> boolValues; // 存放一个逻辑语句内计算范围内的所有结果值
		Map<String, String> stringValues;
		Map<String, String> ruleDescs; // 存放一个语句分解后的所有计算规则
		Map<String, String> ruleDatas; // 存放一个语句分解后所有计算规则对应的数据
		Map<String, String> ruleTrc;	//存放表行列t-r-c 对应三维集合 的 i-j-k
		String statement; // 存放原始语句
		boolean boolValue;//本次逻辑判断的结果 所有为true才为true，有一个false就为false；

		public Range() {
			tableCount = 0;
			rowCount = 0;
			colCount = 0;
			tableList = new ArrayList<Integer>();
			rowList = new ArrayList<Integer>();
			colList = new ArrayList<Integer>();

			boolValues = new ConcurrentHashMap<String, Boolean>();
			dataValues = new ConcurrentHashMap<String, Double>();
			stringValues = new ConcurrentHashMap<String, String>();
			ruleDescs = new ConcurrentHashMap<String, String>();
			ruleDatas = new ConcurrentHashMap<String, String>();
			ruleTrc = new ConcurrentHashMap<String,String>();
		}
	}

	Stack<Range> rangeStack;// 传递range数据集合
	Stack<Integer> tempStack;// 传递临时range数据，不分行、列、表

	Stack<Range> ifRangeStack;// 传递range数据集合

	Stack<Double> doubleStack;
	Stack<Integer> integerStack;
	Stack<String> stringStack;

	ESLParser parser;

	public FormulaEvalueateListener(ESLParser parser) {
		this.parser = parser;

		rangeStack = new Stack<Range>();
		ifRangeStack = new Stack<Range>();

		tempStack = new Stack<Integer>();

		doubleStack = new Stack<Double>();
		integerStack = new Stack<Integer>();
		stringStack = new Stack<String>();
	}

	@Override
	public void enterAssignStatement(ESLParser.AssignStatementContext ctx) {
		rangeStack.clear();// 开始一次计算

		logger.debug(ctx.ref().toString()+"enterAssignStatement");
	}

	@Override
	public void exitAssignStatement(ESLParser.AssignStatementContext ctx) {
		logger.debug(ctx.getText()+"exitAssignStatement");

		Range ifRange = null;
		// 取当前语句的父节点，如果是if语句，则取if子句的条件值，即ifRangeStack
		int ruleIndex = ctx.getParent().getRuleIndex();
		if (ruleIndex == ESLParser.RULE_ifStatement) {
			ifRange = ifRangeStack.pop();// if语句的else分支，出栈
		} else if (ruleIndex == ESLParser.RULE_thenAssignStatement) {// if语句then分支的下级，不处理，交给父节点处理
			return;
		}

		Range valRange = rangeStack.pop();
		Range refRange = rangeStack.pop();

		refRange.statement = ctx.getText();
		logger.debug("--------------------------");
		logger.debug(refRange.statement);

		for (int i = 0; i < valRange.tableCount; i++) {
			for (int j = 0; j < valRange.rowCount; j++) {
				for (int k = 0; k < valRange.colCount; k++) {

					// if语句的else分支，只有boolValue为false的单元，用本子句求值
					if (ruleIndex == ESLParser.RULE_ifStatement) {
						boolean bValue = ifRange.boolValues.get(i + "-" + j + "-" + k);
						if (bValue)
							continue; // 跳过boolValue为true的单元
					}

					// 规则说明
					String rule = refRange.ruleDescs.get(i + "-" + j + "-" + k);
					rule += " = " + valRange.ruleDescs.get(i + "-" + j + "-" + k);
					logger.debug(rule);

					// 数据说明：赋值语句左侧的数据说明=右侧的数据结果
					String data = valRange.dataValues.get(i + "-" + j + "-" + k).toString();
					if (ruleIndex == ESLParser.RULE_ifStatement) {
						String bsData = ifRange.ruleDatas.get(i + "-" + j + "-" + k);
						data = "因为" + bsData + "为false, 所以 " + data + " = "
								+ valRange.ruleDatas.get(i + "-" + j + "-" + k);
					} else {
						data = data + " = " + valRange.ruleDatas.get(i + "-" + j + "-" + k);
					}
					refRange.ruleDatas.put(i + "-" + j + "-" + k, data);
					logger.debug(data);

					// 赋值操作
					double value = valRange.dataValues.get(i + "-" + j + "-" + k);
					String sLabel = "T[" + refRange.tableList.get(i);
					sLabel += "]R[" + refRange.rowList.get(j);
					sLabel += "]C[" + refRange.colList.get(k) + "]";
					logger.debug(sLabel + " = " + value);

				}
			}

		}

	}

	@Override
	public void exitThenAssignStatement(ESLParser.ThenAssignStatementContext ctx) {
		logger.debug(ctx.getText()+"exitThenAssignStatement");

		Range valRange = rangeStack.pop();
		Range refRange = rangeStack.pop();
		Range ifRange = ifRangeStack.peek();// if语句的then分支，只取值，不出栈

		refRange.statement = ctx.getText();
		logger.debug("--------------------------");
		logger.debug(refRange.statement);

		for (int i = 0; i < valRange.tableCount; i++) {
			for (int j = 0; j < valRange.rowCount; j++) {
				for (int k = 0; k < valRange.colCount; k++) {

					// if语句的then分支，只有boolValue为true的单元，用本子句求值
					boolean bValue = ifRange.boolValues.get(i + "-" + j + "-" + k);
					if (!bValue)
						continue; // 跳过boolValue为true的单元

					// 规则说明
					String rule = refRange.ruleDescs.get(i + "-" + j + "-" + k);
					String bsRule = ifRange.ruleDescs.get(i + "-" + j + "-" + k);
					rule = "因为" + bsRule + "为true, 所以 " + rule + " = " + valRange.ruleDescs.get(i + "-" + j + "-" + k);
					refRange.ruleDescs.put(i + "-" + j + "-" + k, rule);
					logger.debug(rule);

					// 数据说明：赋值语句左侧的数据说明=右侧的数据结果
					String data = valRange.dataValues.get(i + "-" + j + "-" + k).toString();

					String bsData = ifRange.ruleDatas.get(i + "-" + j + "-" + k);
					data = "因为" + bsData + "为true, 所以 " + data + " = " + valRange.ruleDatas.get(i + "-" + j + "-" + k);

					refRange.ruleDatas.put(i + "-" + j + "-" + k, data);
					logger.debug(data);

					// 赋值操作
					double value = valRange.dataValues.get(i + "-" + j + "-" + k);
					String sLabel = "T[" + refRange.tableList.get(i);
					sLabel += "]R[" + refRange.rowList.get(j);
					sLabel += "]C[" + refRange.colList.get(k) + "]";
					logger.debug(sLabel + " = " + value);

				}
			}

		}
	}

	@Override
	public void exitLogicStatement(ESLParser.LogicStatementContext ctx) {
		logger.debug(ctx.getText()+"exitLogicStatement");

		// 取当前语句的父节点，如果是if语句，当前语句为if分支，其逻辑值从rangeStack移到ifStack
		int ruleIndex = ctx.getParent().getRuleIndex();
		if(ruleIndex == ESLParser.RULE_thenLogicStatement){
			return;
		}

		Range range = rangeStack.pop();
		range.statement = ctx.getText();
		logger.debug("--------------------------");
		logger.debug(range.statement);
		
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
				}
			}
		}
		if (ruleIndex == ESLParser.RULE_ifStatement) {
			ifRangeStack.push(range);// 将range对象转移到ifRangeStack，供赋值语句判断
		}
	}
	
	@Override
	public void exitThenLogicStatement(ESLParser.ThenLogicStatementContext ctx) {
		logger.debug(ctx.getText()+"exitThenLogicStatement");
		Range ifRange = ifRangeStack.pop();
		Range range = rangeStack.pop();
		range.statement = ctx.getText();
		logger.debug("--------------------------");
		logger.debug(range.statement);
		

		for (int i = 0; i < range.tableCount; i++) {
			for (int j = 0; j < range.rowCount; j++) {
				for (int k = 0; k < range.colCount; k++) {
					Boolean booleanValue = ifRange.boolValues.get(i + "-" + j + "-" + k);
					if(!booleanValue){
						continue;
					}
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
				}
			}
		}
	};

	/*
	 * @Override // 不对。因为算数运算和逻辑运算，是在统一的数据map上运算，而不是在堆栈上运算 public void
	 * exitIfStatement(ESLParser.IfStatementContext ctx) { Range elseRange =
	 * rangeStack.pop(); Range thenRange = rangeStack.pop(); Range ifRange =
	 * rangeStack.pop(); for (int i = 0; i < ifRange.tableCount; i++) { for (int
	 * j = 0; j < ifRange.rowCount; j++) { for (int k = 0; k < ifRange.colCount;
	 * k++) { // 值 boolean bResult = ifRange.boolValues.get(i + "-" + j + "-" +
	 * k); if (bResult) { rangeStack.push(thenRange); } else {
	 * rangeStack.push(elseRange); } } } } }
	 */

	@Override
	public void exitLogicAndExpression(ESLParser.LogicAndExpressionContext ctx) {
		logger.debug(ctx.getText()+"exitLogicAndExpression");

		int iOperaCount = ctx.logicNotExpression().size();// 表达式中，子表达式的个数
		Range result = rangeStack.pop();
		while (--iOperaCount > 0) {
			Range element = rangeStack.pop();
			for (int i = 0; i < element.tableCount; i++) {
				for (int j = 0; j < element.rowCount; j++) {
					for (int k = 0; k < element.colCount; k++) {
						// 值
						boolean bResult = result.boolValues.get(i + "-" + j + "-" + k);
						boolean bElement = element.boolValues.get(i + "-" + j + "-" + k);
						bResult = bResult && bElement;
						result.boolValues.put(i + "-" + j + "-" + k, bResult);
						// 规则说明：栈底在左侧，栈顶在右侧
						String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
						rule += " AND " + result.ruleDescs.get(i + "-" + j + "-" + k);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule);
						// 规则数据
						String data = element.ruleDatas.get(i + "-" + j + "-" + k);
						data += " AND " + result.ruleDatas.get(i + "-" + j + "-" + k);
						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}
		rangeStack.push(result);
	}

	@Override
	public void exitLogicOrExpression(ESLParser.LogicOrExpressionContext ctx) {
		logger.debug(ctx.getText()+"exitLogicOrExpression");

		int iOperaCount = ctx.logicAndExpression().size();// 表达式中，子表达式的个数
		Range result = rangeStack.pop();
		while (--iOperaCount > 0) {
			Range element = rangeStack.pop();
			for (int i = 0; i < element.tableCount; i++) {
				for (int j = 0; j < element.rowCount; j++) {
					for (int k = 0; k < element.colCount; k++) {
						boolean bResult = result.boolValues.get(i + "-" + j + "-" + k);
						boolean bElement = element.boolValues.get(i + "-" + j + "-" + k);
						bResult = bResult || bElement;
						result.boolValues.put(i + "-" + j + "-" + k, bResult);

						String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
						rule += " OR " + result.ruleDescs.get(i + "-" + j + "-" + k);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule);

						String data = element.ruleDatas.get(i + "-" + j + "-" + k);
						data += " OR " + result.ruleDatas.get(i + "-" + j + "-" + k);
						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}
		rangeStack.push(result);
	}

	@Override
	public void exitLogicNotExpression(ESLParser.LogicNotExpressionContext ctx) {
		logger.debug(ctx.getText()+"exitLogicNotExpression");

		if (ctx.NOT() != null) {
			Range result = rangeStack.pop();
			for (int i = 0; i < result.tableCount; i++) {
				for (int j = 0; j < result.rowCount; j++) {
					for (int k = 0; k < result.colCount; k++) {
						boolean bResult = !(result.boolValues.get(i + "-" + j + "-" + k));
						String rule = " NOT " + result.ruleDescs.get(i + "-" + j + "-" + k);
						String data = " NOT " + result.ruleDatas.get(i + "-" + j + "-" + k);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule);
						result.boolValues.put(i + "-" + j + "-" + k, bResult);
						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
			rangeStack.push(result);
		}
	}

	@Override
	public void enterRowRange(ESLParser.RowRangeContext ctx) {
	}

	@Override
	public void exitRowRange(ESLParser.RowRangeContext ctx) {
		logger.debug(ctx.getText()+"exitRowRange");

		Range range = rangeStack.pop();

		int rowType = tempStack.pop();

		switch (rowType) {
		case 1:// 单个
			range.rowCount++;
			range.rowList.add(tempStack.pop());
			break;
		case 2:// 定两端
			int iBegin = tempStack.pop();
			int iEnd = tempStack.pop();

			for (int i = iBegin; i <= iEnd; i++) {
				range.rowCount++;
				range.rowList.add(i);
			}
			break;
		case 3:// 定左端
			iBegin = tempStack.pop();
			iEnd = getMaxRow(range.tableList.get(0), iBegin);
			for (int i = iBegin; i <= iEnd; i++) {
				range.rowCount++;
				range.rowList.add(i);
			}
			break;
		case 4:// 定右端
			iEnd = tempStack.pop();
			iBegin = getMinRow(range.tableList.get(0), iEnd);
			for (int i = iBegin; i <= iEnd; i++) {
				range.rowCount++;
				range.rowList.add(i);
			}
			break;
		case 5:// 枚举
			do {
				int i = tempStack.pop();
				range.rowCount++;
				range.rowList.add(0, i);
			} while (!tempStack.isEmpty());
			break;
		}
		rangeStack.push(range);
	}

	@Override
	public void exitColRange(ESLParser.ColRangeContext ctx) {
		logger.debug(ctx.getText()+"exitColRange");

		Range range = rangeStack.pop();

		int colType = tempStack.pop();

		switch (colType) {
		case 1:// 单个
			range.colCount++;
			range.colList.add(tempStack.pop());
			break;
		case 2:// 定两端
			int iBegin = tempStack.pop();
			int iEnd = tempStack.pop();

			for (int i = iBegin; i <= iEnd; i++) {
				range.colCount++;
				range.colList.add(i);
			}
			break;
		case 3:// 定左端
			iBegin = tempStack.pop();
			iEnd = getMaxRow(range.tableList.get(0), iBegin);
			for (int i = iBegin; i <= iEnd; i++) {
				range.colCount++;
				range.colList.add(i);
			}
			break;
		case 4:// 定右端
			iEnd = tempStack.pop();
			iBegin = getMinRow(range.tableList.get(0), iEnd);
			for (int i = iBegin; i <= iEnd; i++) {
				range.colCount++;
				range.colList.add(i);
			}
			break;
		case 5:// 枚举
			do {
				int i = tempStack.pop();
				range.colCount++;
				range.colList.add(0, i);
			} while (!tempStack.isEmpty());
			break;
		}
		rangeStack.push(range);
	}

	@Override
	public void exitSingleRange(ESLParser.SingleRangeContext ctx) {
		logger.debug(ctx.getText()+"exitSingleRange");

		int type = 1;// 第一种：只有一个数字
		int token1;
		token1 = Integer.parseInt(ctx.Integer().getText());
		// token1 = integerStack.pop();
		tempStack.push(token1);
		tempStack.push(type);
	}

	@Override
	public void exitCloseRange(ESLParser.CloseRangeContext ctx) {
		logger.debug(ctx.getText()+"exitCloseRange");

		int type = 2;// 第二种：1）起始位置；2）结束位置
		int iBegin, iEnd;
		iBegin = Integer.parseInt(ctx.Integer(0).getText());
		iEnd = Integer.parseInt(ctx.Integer(1).getText());
		// iBegin = integerStack.pop();
		// iEnd = integerStack.pop();
		tempStack.push(iEnd);
		tempStack.push(iBegin);
		tempStack.push(type);
	}

	@Override
	public void exitLeftRange(ESLParser.LeftRangeContext ctx) {
		logger.debug(ctx.getText()+"exitLeftRange");

		int type = 3;// 第三种：1）起始位置：整数；2）结束位置：-1
		int token1;
		token1 = Integer.parseInt(ctx.Integer().getText());
		// token1 = integerStack.pop();
		tempStack.push(token1);
		tempStack.push(type);
	}

	@Override
	public void exitRightRange(ESLParser.RightRangeContext ctx) {
		logger.debug(ctx.getText()+"exitRightRange");

		int type = 4;// 第四种：1）起始位置 -1；2）结束位置：整数
		int token2;
		token2 = Integer.parseInt(ctx.Integer().getText());
		// token2 = integerStack.pop();
		tempStack.push(token2);
		tempStack.push(type);
	}

	@Override
	public void exitEnumerateRange(ESLParser.EnumerateRangeContext ctx) {
		logger.debug(ctx.getText()+"exitEnumerateRange");

		int type = 5;// 第五种：枚举所有位置
		int token;
		int tokenCount = ctx.Integer().size();
		for (int i = 0; i < tokenCount; i++) {
			token = Integer.parseInt(ctx.Integer(i).getText());
			// token = integerStack.pop();
			tempStack.push(token);
		}
		tempStack.push(type);
	}

	@Override
	public void exitRectRang(ESLParser.RectRangContext ctx) {
		logger.debug(ctx.getText()+"exitRectRang");

		Range range = rangeStack.pop();
		if (range.tableCount == 0) {
			range.tableCount++;
			range.tableList.add(0);
		}
		// 填充缺省的行
		if (range.rowCount == 0) {
			List<Integer> rows = this.getRowList(range.tableList.get(0), range.colList.get(0));
			for (int i = 0; i < rows.size(); i++) {
				range.rowCount++;
				range.rowList.add(rows.get(i));
			}
		}
		// 填充缺省的列
		if (range.colCount == 0) {
			List<Integer> cols = this.getColList(range.tableList.get(0), range.rowList.get(0));
			for (int i = 0; i < cols.size(); i++) {
				range.colCount++;
				range.colList.add(cols.get(i));
			}
		}
		rangeStack.push(range);
	}

	@Override
	public void enterSheetRangeWithNumber(ESLParser.SheetRangeWithNumberContext ctx) {

	}

	@Override
	public void exitSheetRangeWithNumber(ESLParser.SheetRangeWithNumberContext ctx) {
		logger.debug(ctx.getText()+"exitSheetRangeWithNumber");

		Range range = rangeStack.pop();
		int tableType = tempStack.pop();

		switch (tableType) {
		case 1:// 单个
			range.tableCount++;
			range.tableList.add(tempStack.pop());
			break;
		case 2:// 定两端
			int iBegin = tempStack.pop();
			int iEnd = tempStack.pop();
			for (int i = iBegin; i <= iEnd; i++) {
				range.tableCount++;
				range.tableList.add(i);
			}
			break;
		case 5:// 枚举
			int i = tempStack.pop();
			while (!tempStack.isEmpty()) {
				range.tableCount++;
				range.tableList.add(i);
			}
			break;
		}
		rangeStack.push(range);
	}

	@Override
	public void enterRef(ESLParser.RefContext ctx) {
		logger.debug(ctx.getText()+"enterRef");

		// 开始解析一个单元引用：每个ref使用一个独立的range对象
		Range range = new Range();
		rangeStack.push(range);
	}

	@Override
	public void exitRef(ESLParser.RefContext ctx) {
		logger.debug(ctx.getText()+"exitRef");

		Range range = rangeStack.pop();
		range = getRangeData(range);

		for (int i = 0; i < range.tableCount; i++) {
			for (int j = 0; j < range.rowCount; j++) {
				for (int k = 0; k < range.colCount; k++) {
					// 规则说明
					String rule = "T[" + range.tableList.get(i);
					rule += "]R[" + range.rowList.get(j);
					rule += "]C[" + range.colList.get(k) + "]";
					range.ruleDescs.put(i + "-" + j + "-" + k, rule);
					// 赋值语句左侧没有数值
					if (!range.dataValues.isEmpty()) {
						// 规则数据
						String data = range.dataValues.get(i + "-" + j + "-" + k).toString();
						range.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}

		rangeStack.push(range);
	}

	@Override
	public void exitRangeAdd(ESLParser.RangeAddContext ctx) {
		logger.debug(ctx.getText()+"exitRangeAdd");

		int iOperaCount = ctx.ref().size();// 连加表达式中，子表达式的个数
		Range result = rangeStack.pop();
		while (--iOperaCount > 0) {
			Range element = rangeStack.pop();

			int tBegin = Math.min(result.tableList.get(0), element.tableList.get(0));
			int tEnd = Math.max(result.tableList.get(result.tableList.size()-1), element.tableList.get(element.tableList.size()-1));
			int rBegin = Math.min(result.rowList.get(0), element.rowList.get(0));
			int rEnd = Math.max(result.rowList.get(result.rowList.size()-1), element.rowList.get(element.rowList.size()-1));
			int cBegin = Math.min(result.colList.get(0), element.colList.get(0));
			int cEnd = Math.max(result.colList.get(result.colList.size()-1), element.colList.get(element.colList.size()-1));
			Range temp = getRangeData(element,result);
			for (int i = 0; i < result.tableCount; i++) {
				for (int j = 0; j < result.rowCount; j++) {
					for (int k = 0; k < result.colCount; k++) {
						// 数据
						double dResult = 0;
						String rule = "+";
						String data = "+";
						
						
						for(int t = result.tableCount>1?i+1:tBegin;t<= (result.tableCount>1?i+1:tEnd);t++)
						{
							for(int r = result.rowCount>1?j+1:rBegin;r<=(result.rowCount>1?j+1:rEnd);r++)
							{
								for(int c = result.colCount>1?k+1:cBegin;c<=(result.colCount>1?k+1:cEnd);c++)
								{
									dResult += temp.dataValues.get(temp.ruleTrc.get(t+"-"+r+"-"+c));
									// 规则说明
									rule += " + " + String.format("T[%s]R[%s]C[%s]",t,r,c);
									// 规则数据
									data += " + " + temp.dataValues.get(temp.ruleTrc.get(t+"-"+r+"-"+c));
								}
							}
						}
						result.dataValues.put(i + "-" + j + "-" + k, dResult);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule.replace("+ + ",""));
						result.ruleDatas.put(i + "-" + j + "-" + k, data.replace("+ + ",""));

//						double dElement = result.dataValues.get(i + "-" + j + "-" + k);
//						dResult += dElement;
//						result.dataValues.put(i + "-" + j + "-" + k, dResult);
//						// 规则说明
//						String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
//						rule += " + " + result.ruleDescs.get(i + "-" + j + "-" + k);
//						result.ruleDescs.put(i + "-" + j + "-" + k, rule);
//						// 规则数据
//						String data = element.ruleDatas.get(i + "-" + j + "-" + k);
//						data += " + " + result.ruleDatas.get(i + "-" + j + "-" + k);
//						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}
		rangeStack.push(result);
	}

	@Override
	public void exitCellReference(ESLParser.CellReferenceContext ctx) {
		logger.debug(ctx.getText()+"exitCellReference");

		// 所有引用单元需要代换为数值
		Range range = rangeStack.pop();

		// 对于表内公式，其tableList缺省为0
		if (range.tableCount == 0) {
			range.tableCount++;
			range.tableList.add(0);
		}
		range = getRangeData(range);

		// for (int i = 0; i < range.tableCount; i++) {
		// for (int j = 0; j < range.rowCount; j++) {
		// for (int k = 0; k < range.colCount; k++) {
		// String rule = "T[" + range.tableList.get(i);
		// rule += "]R[" + range.rowList.get(j);
		// rule += "]C[" + range.colList.get(k) + "]";
		// range.ruleDescs.put(i + "-" + j + "-" + k, rule);
		//
		// String data = range.dataValues.get(i + "-" + j + "-" + k).toString();
		// range.ruleDatas.put(i + "-" + j + "-" + k, data);
		// }
		// }
		// }

		rangeStack.push(range);
	}

	@Override
	public void exitAddExp(ESLParser.AddExpContext ctx) {
		logger.debug(ctx.getText()+"exitAddExp");

		int iOperaCount = ctx.subExp().size();// 除法表达式中，子表达式的个数
		Range result = rangeStack.pop();
		while (--iOperaCount > 0) {
			Range element = rangeStack.pop();

			processSingleValue(result, element);
			int tCount = result.tableCount;
			int rCount = result.rowCount;
			int cCount = result.colCount;

			for (int i = 0; i < tCount; i++) {
				for (int j = 0; j < rCount; j++) {
					for (int k = 0; k < cCount; k++) {
						// 数据
						double dResult = result.dataValues.get(i + "-" + j + "-" + k);
						double dElement = element.dataValues.get(i + "-" + j + "-" + k);
						dResult=new BigDecimal(dResult+"").add(new BigDecimal(dElement+"")).doubleValue();
						//dResult += dElement;
						result.dataValues.put(i + "-" + j + "-" + k, dResult);
						// 规则说明
						String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
						rule += " + " + result.ruleDescs.get(i + "-" + j + "-" + k);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule);
						// 规则数据
						String data = element.ruleDatas.get(i + "-" + j + "-" + k);
						data += " + " + result.ruleDatas.get(i + "-" + j + "-" + k);
						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}
		rangeStack.push(result);
	}

	@Override
	public void exitSubExp(ESLParser.SubExpContext ctx) {
		logger.debug(ctx.getText()+"exitSubExp");

		int iOperaCount = ctx.multExp().size();// 除法表达式中，子表达式的个数
		Range result = rangeStack.pop();
		while (--iOperaCount > 0) {
			Range element = rangeStack.pop();

			processSingleValue(result, element);
			int tCount = result.tableCount;
			int rCount = result.rowCount;
			int cCount = result.colCount;

			for (int i = 0; i < tCount; i++) {
				for (int j = 0; j < rCount; j++) {
					for (int k = 0; k < cCount; k++) {
						double dResult = result.dataValues.get(i + "-" + j + "-" + k);
						double dElement = element.dataValues.get(i + "-" + j + "-" + k);
						dResult = dElement - dResult;
						result.dataValues.put(i + "-" + j + "-" + k, dResult);

						String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
						rule += " - " + result.ruleDescs.get(i + "-" + j + "-" + k);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule);

						String data = element.ruleDatas.get(i + "-" + j + "-" + k);
						data += " - " + result.ruleDatas.get(i + "-" + j + "-" + k);
						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}
		rangeStack.push(result);
	}

	@Override
	public void exitMultExp(ESLParser.MultExpContext ctx) {
		logger.debug(ctx.getText()+"exitMultExp");

		int iOperaCount = ctx.divExp().size();// 除法表达式中，子表达式的个数
		Range result = rangeStack.pop();
		while (--iOperaCount > 0) {
			Range element = rangeStack.pop();

			processSingleValue(result, element);
			int tCount = result.tableCount;
			int rCount = result.rowCount;
			int cCount = result.colCount;

			for (int i = 0; i < tCount; i++) {
				for (int j = 0; j < rCount; j++) {
					for (int k = 0; k < cCount; k++) {
						// 数据
						double dResult = result.dataValues.get(i + "-" + j + "-" + k);
						double dElement = element.dataValues.get(i + "-" + j + "-" + k);
						dResult *= dElement;
						// 规则说明
						String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
						rule += " * " + result.ruleDescs.get(i + "-" + j + "-" + k);
						result.ruleDescs.put(i + "-" + j + "-" + k, rule);
						result.dataValues.put(i + "-" + j + "-" + k, dResult);
						// 规则数据
						String data = element.ruleDatas.get(i + "-" + j + "-" + k);
						data += " * " + result.ruleDatas.get(i + "-" + j + "-" + k);
						result.ruleDatas.put(i + "-" + j + "-" + k, data);
					}
				}
			}
		}
		rangeStack.push(result);
	}

	@Override
	public void exitDivExp(ESLParser.DivExpContext ctx) {
		logger.debug(ctx.getText()+"exitDivExp");

		if (rangeStack.size() > 1) {
			int iOperaCount = ctx.primaryExp().size();// 除法表达式中，子表达式的个数
			Range result = rangeStack.pop();

			while (--iOperaCount > 0) {
				Range element = rangeStack.pop();

				processSingleValue(result, element);
				int tCount = result.tableCount;
				int rCount = result.rowCount;
				int cCount = result.colCount;

				for (int i = 0; i < tCount; i++) {
					for (int j = 0; j < rCount; j++) {
						for (int k = 0; k < cCount; k++) {
							// 数据
							double dResult = result.dataValues.get(i + "-" + j + "-" + k);
							double dElement = element.dataValues.get(i + "-" + j + "-" + k);
							if(dResult != 0){
								dResult = new BigDecimal(Double.toString(dElement)).
										divide(new BigDecimal(Double.toString(dResult)), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
							}
							
							//dResult = dElement / dResult;
							result.dataValues.put(i + "-" + j + "-" + k, dResult);
							// 规则说明
							String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
							rule += " / " + result.ruleDescs.get(i + "-" + j + "-" + k);
							result.ruleDescs.put(i + "-" + j + "-" + k, rule);
							// 规则数据
							String data = element.ruleDatas.get(i + "-" + j + "-" + k);
							data += " / " + result.ruleDatas.get(i + "-" + j + "-" + k);
							result.ruleDatas.put(i + "-" + j + "-" + k, data);
						}
					}
				}
			}

			rangeStack.push(result);
		}
	}

	@Override
	public void exitRelationalExp(ESLParser.RelationalExpContext ctx) {
		logger.debug(ctx.getText()+"exitRelationalExp");

		Range right = rangeStack.pop();
		Range left = rangeStack.pop();
		Range result = new Range();

		for (int i = 0; i < left.tableCount; i++) {
			for (int j = 0; j < left.rowCount; j++) {
				for (int k = 0; k < left.colCount; k++) {

					double dLeft = left.dataValues.get(i + "-" + j + "-" + k);
					double dRight = right.dataValues.get(i + "-" + j + "-" + k);
					String sRuleLeft = left.ruleDescs.get(i + "-" + j + "-" + k);
					String sRuleRight = right.ruleDescs.get(i + "-" + j + "-" + k);
					String sDataLeft = left.ruleDatas.get(i + "-" + j + "-" + k);
					String sDataRight = right.ruleDatas.get(i + "-" + j + "-" + k);

					Boolean bResult = null;
					String sRule = null;
					String sData = null;

					switch (ctx.RelationOp().getText()) {
					case ">":
						bResult = (dLeft > dRight);
						sRule = sRuleLeft + " > " + sRuleRight;
						sData = sDataLeft + " > " + sDataRight;
						break;
					case "<":
						bResult = (dLeft < dRight);
						sRule = sRuleLeft + " < " + sRuleRight;
						sData = sDataLeft + " < " + sDataRight;
						break;
					case ">=":
						bResult = (dLeft >= dRight);
						sRule = sRuleLeft + " >= " + sRuleRight;
						sData = sDataLeft + " >= " + sDataRight;
						break;
					case "<=":
						bResult = (dLeft <= dRight);
						sRule = sRuleLeft + " <= " + sRuleRight;
						sData = sDataLeft + " <= " + sDataRight;
						break;
					case "==":
						bResult = (dLeft == dRight);
						sRule = sRuleLeft + " == " + sRuleRight;
						sData = sDataLeft + " == " + sDataRight;
						break;
					case "!=":
						bResult = (dLeft != dRight);
						sRule = sRuleLeft + " != " + sRuleRight;
						sData = sDataLeft + " != " + sDataRight;
						break;
					}
					result.boolValues.put(i + "-" + j + "-" + k, bResult);
					result.ruleDescs.put(i + "-" + j + "-" + k, sRule);
					result.ruleDatas.put(i + "-" + j + "-" + k, sData);
				}
			}
		}
		result.tableCount = left.tableCount;
		result.colCount = left.colCount;
		result.rowCount = left.rowCount;

		rangeStack.push(result);
	}

	@Override
	public void exitNegativeExpression(ESLParser.NegativeExpressionContext ctx) {
		logger.debug(ctx.getText()+"exitNegativeExpression");

		Range element = rangeStack.pop();
		for (int i = 0; i < element.tableCount; i++) {
			for (int j = 0; j < element.rowCount; j++) {
				for (int k = 0; k < element.colCount; k++) {
					// 数据
					double dElement = element.dataValues.get(i + "-" + j + "-" + k);
					dElement = -dElement;
					element.dataValues.put(i + "-" + j + "-" + k, dElement);
					// 规则说明
					String rule = element.ruleDescs.get(i + "-" + j + "-" + k);
					rule = " - " + rule;
					element.ruleDescs.put(i + "-" + j + "-" + k, rule);
					// 规则数据
					String data = element.ruleDatas.get(i + "-" + j + "-" + k);
					data = " - " + data;
					element.ruleDatas.put(i + "-" + j + "-" + k, data);
				}
			}
		}
		rangeStack.push(element);
	}

	@Override
	public void exitIntegerValue(ESLParser.IntegerValueContext ctx) {
		// integerStack.push(Integer.parseInt(ctx.Integer().getText()));

		Range element = new Range();
		element.rangeType = 2;

		int dElement = Integer.parseInt(ctx.Integer().getText());
		element.dataValues.put(0 + "-" + 0 + "-" + 0, new Double(dElement));
		// 规则说明
		String rule = "数值";
		element.ruleDescs.put(0 + "-" + 0 + "-" + 0, Integer.toString(dElement));
		// 规则数据
		String dataDesc = Integer.toString(dElement);
		element.ruleDatas.put(0 + "-" + 0 + "-" + 0, dataDesc);

		element.tableCount = 1;
		element.rowCount = 1;
		element.colCount = 1;

		rangeStack.push(element);
	}

	@Override
	public void exitRealValue(ESLParser.RealValueContext ctx) {
		// doubleStack.push(Double.parseDouble(ctx.REAL().getText()));
		Range element = new Range();
		element.rangeType = 2;

		double dElement = Double.parseDouble(ctx.REAL().getText());
		element.dataValues.put(0 + "-" + 0 + "-" + 0, dElement);
		// 规则说明
		String rule = "数值";
		element.ruleDescs.put(0 + "-" + 0 + "-" + 0, dElement+"");
		// 规则数据
		String dataDesc = Double.toString(dElement);
		element.ruleDatas.put(0 + "-" + 0 + "-" + 0, dataDesc);

		element.tableCount = 1;
		element.rowCount = 1;
		element.colCount = 1;

		rangeStack.push(element);
	}

	@Override
	public void exitStringValue(ESLParser.StringValueContext ctx) {
		// stringStack.push(ctx.STRING().getText());
	}

	// 处理标量：缺省情况下，公式均按照向量来处理，但当primaryExp规则的成员是标量时，需要根据上层规则来解释，只要上层规则中包含向量，则标量要按照向量维度进行扩展

	@Override
	public void enterExpression(ESLParser.ExpressionContext ctx) {
		logger.debug(ctx.getText());
	}

	@Override
	public void enterAddExp(ESLParser.AddExpContext ctx) {
		logger.debug(ctx.getText());
	}

	@Override
	public void enterSubExp(ESLParser.SubExpContext ctx) {
		logger.debug(ctx.getText());
	}

	@Override
	public void enterMultExp(ESLParser.MultExpContext ctx) {
		logger.debug(ctx.getText());
	}

	@Override
	public void enterDivExp(ESLParser.DivExpContext ctx) {
		logger.debug(ctx.getText());
	}

	// -----------------------------------------------------------------
	// 以下皆为模拟，需要修改实现
	// 根据根据行所在的块，确定最大行数
	public int getMaxRow(int table, int row) {
		return 10;
	}

	// 根据模列所在的块，确定最大列数
	public int getMaxCol(int table, int col) {
		return 10;
	}

	// 根据根据行所在的块，确定最大行数
	public int getMinRow(int table, int row) {
		return 1;
	}

	// 根据模列所在的块，确定最大列数
	public int getMinCol(int table, int col) {
		return 1;
	}

	// 根据行号确定所有列
	public List<Integer> getRowList(int table, int col) {
		List<Integer> rowList = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			rowList.add(i + 1);
		}
		return rowList;
	}

	// 根据列号确定所有行
	public List<Integer> getColList(int table, int row) {
		List<Integer> colList = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			colList.add(i + 1);
		}
		return colList;
	}
	
	// 为指定范围的单元格取数
		public Range getRangeData(Range rangeStart,Range rangeEnd) {
			// 分析两个加法运算符之间的三维距离，确定循环的三个起止点
			int tBegin = Math.min(rangeEnd.tableList.get(0), rangeStart.tableList.get(0));
			int tEnd = Math.max(rangeEnd.tableList.get(rangeEnd.tableList.size()-1), rangeStart.tableList.get(rangeStart.tableList.size()-1));
			int rBegin = Math.min(rangeEnd.rowList.get(0), rangeStart.rowList.get(0));
			int rEnd = Math.max(rangeEnd.rowList.get(rangeEnd.rowList.size()-1), rangeStart.rowList.get(rangeStart.rowList.size()-1));
			int cBegin = Math.min(rangeEnd.colList.get(0), rangeStart.colList.get(0));
			int cEnd = Math.max(rangeEnd.colList.get(rangeEnd.colList.size()-1), rangeStart.colList.get(rangeStart.colList.size()-1));

			rangeStart.tableCount=tEnd-tBegin+1;
			rangeStart.rowCount=rEnd-rBegin+1;
			rangeStart.colCount=cEnd-cBegin+1;
			rangeStart.tableCount=tEnd-tBegin+1;
			rangeStart.tableList.clear();
			// +..+ 补充中间缺省的值
			for(int t = tBegin; t <= tEnd; t++){
				rangeStart.tableList.add(t);
			}
			rangeStart.rowCount=rEnd-rBegin+1;
			rangeStart.rowList.clear();
			for(int r = rBegin; r <= rEnd; r++){
				rangeStart.rowList.add(r);
			}
			rangeStart.colCount=cEnd-cBegin+1;
			rangeStart.colList.clear();
			for(int c = cBegin; c <= cEnd; c++){
				rangeStart.colList.add(c);
			}
			rangeStart=getRangeData(rangeStart);

			return rangeStart;
		}

	// 为指定范围的单元格取数
	public Range getRangeData(Range range) {
		for (int i = 0; i < range.tableCount; i++) {
			for (int j = 0; j < range.rowCount; j++) {
				for (int k = 0; k < range.colCount; k++) {

					// 静态数组不能满足要求，换成map或者List类，三维存储，可能map更合适
					// range.dataValues[i][j][k] = 1;
					range.dataValues.put(i + "-" + j + "-" + k, 1.0);
				}
			}
		}
		return range;
	}

	boolean processSingleValue(Range result, Range element) {
		boolean rtn = true;

		int tCount = 0;
		int rCount = 0;
		int cCount = 0;
		// 处理标量：按照向量维度扩展标量
		if (result.rangeType == 2 && element.rangeType == 2) {

		} else if (result.rangeType != 2 && element.rangeType == 2) {
			tCount = result.tableCount;
			rCount = result.rowCount;
			cCount = result.colCount;
			double dValue = element.dataValues.get(0 + "-" + 0 + "-" + 0);
			for (int i = 0; i < tCount; i++) {
				for (int j = 0; j < rCount; j++) {
					for (int k = 0; k < cCount; k++) {
						element.dataValues.put(i + "-" + j + "-" + k, dValue);
						element.ruleDescs.put(i + "-" + j + "-" + k, dValue+"");
						element.ruleDatas.put(i + "-" + j + "-" + k, Double.toString(dValue));
					}
				}
			}
			element.tableCount = tCount;
			for (int i = 0; i < tCount; i++) {
				element.tableList.add(result.tableList.get(i));
			}
			element.rowCount = rCount;
			for (int i = 0; i < rCount; i++) {
				element.rowList.add(result.rowList.get(i));
			}
			element.colCount = cCount;
			for (int i = 0; i < cCount; i++) {
				element.colList.add(result.colList.get(i));
			}
		} else if (result.rangeType == 2 && element.rangeType != 2) {
			tCount = element.tableCount;
			rCount = element.rowCount;
			cCount = element.colCount;
			double dValue = result.dataValues.get(0 + "-" + 0 + "-" + 0);
			for (int i = 0; i < tCount; i++) {
				for (int j = 0; j < rCount; j++) {
					for (int k = 0; k < cCount; k++) {
						result.dataValues.put(i + "-" + j + "-" + k, dValue);
						result.ruleDescs.put(i + "-" + j + "-" + k, dValue+"");
						result.ruleDatas.put(i + "-" + j + "-" + k, Double.toString(dValue));
					}
				}
			}
			result.tableCount = tCount;
			for (int i = 0; i < tCount; i++) {
				result.tableList.add(element.tableList.get(i));
			}
			result.rowCount = rCount;
			for (int i = 0; i < rCount; i++) {
				result.rowList.add(element.rowList.get(i));
			}
			result.colCount = cCount;
			for (int i = 0; i < cCount; i++) {
				result.colList.add(element.colList.get(i));
			}
		} else {
			rtn = false;
		}
		return rtn;
	}
	// 以上模拟方法，为解析器所调用，为解析器提供数据，实现时应实现为表格模板组件的Service
	// -------------------------------------------------------------
}
