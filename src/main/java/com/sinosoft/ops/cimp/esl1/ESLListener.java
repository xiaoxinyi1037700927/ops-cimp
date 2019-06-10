package com.sinosoft.ops.cimp.esl1;
// Generated from ESL.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ESLParser}.
 */
public interface ESLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ESLParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ESLParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ESLParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(ESLParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(ESLParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(ESLParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(ESLParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void enterAssignStatement(ESLParser.AssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#assignStatement}.
	 * @param ctx the parse tree
	 */
	void exitAssignStatement(ESLParser.AssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#logicStatement}.
	 * @param ctx the parse tree
	 */
	void enterLogicStatement(ESLParser.LogicStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#logicStatement}.
	 * @param ctx the parse tree
	 */
	void exitLogicStatement(ESLParser.LogicStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#logicOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicOrExpression(ESLParser.LogicOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#logicOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicOrExpression(ESLParser.LogicOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(ESLParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(ESLParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#thenAssignStatement}.
	 * @param ctx the parse tree
	 */
	void enterThenAssignStatement(ESLParser.ThenAssignStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#thenAssignStatement}.
	 * @param ctx the parse tree
	 */
	void exitThenAssignStatement(ESLParser.ThenAssignStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#thenLogicStatement}.
	 * @param ctx the parse tree
	 */
	void enterThenLogicStatement(ESLParser.ThenLogicStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#thenLogicStatement}.
	 * @param ctx the parse tree
	 */
	void exitThenLogicStatement(ESLParser.ThenLogicStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#logicAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicAndExpression(ESLParser.LogicAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#logicAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicAndExpression(ESLParser.LogicAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#logicNotExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicNotExpression(ESLParser.LogicNotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#logicNotExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicNotExpression(ESLParser.LogicNotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code relationalExp}
	 * labeled alternative in {@link ESLParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExp(ESLParser.RelationalExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code relationalExp}
	 * labeled alternative in {@link ESLParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExp(ESLParser.RelationalExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthLogicExp}
	 * labeled alternative in {@link ESLParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthLogicExp(ESLParser.ParenthLogicExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthLogicExp}
	 * labeled alternative in {@link ESLParser#relationExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthLogicExp(ESLParser.ParenthLogicExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#ref}.
	 * @param ctx the parse tree
	 */
	void enterRef(ESLParser.RefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#ref}.
	 * @param ctx the parse tree
	 */
	void exitRef(ESLParser.RefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sheetRangeWithNumber}
	 * labeled alternative in {@link ESLParser#sheetRange}.
	 * @param ctx the parse tree
	 */
	void enterSheetRangeWithNumber(ESLParser.SheetRangeWithNumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sheetRangeWithNumber}
	 * labeled alternative in {@link ESLParser#sheetRange}.
	 * @param ctx the parse tree
	 */
	void exitSheetRangeWithNumber(ESLParser.SheetRangeWithNumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code sheetRangeWithName}
	 * labeled alternative in {@link ESLParser#sheetRange}.
	 * @param ctx the parse tree
	 */
	void enterSheetRangeWithName(ESLParser.SheetRangeWithNameContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sheetRangeWithName}
	 * labeled alternative in {@link ESLParser#sheetRange}.
	 * @param ctx the parse tree
	 */
	void exitSheetRangeWithName(ESLParser.SheetRangeWithNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#rectRang}.
	 * @param ctx the parse tree
	 */
	void enterRectRang(ESLParser.RectRangContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#rectRang}.
	 * @param ctx the parse tree
	 */
	void exitRectRang(ESLParser.RectRangContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void enterRowRange(ESLParser.RowRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#rowRange}.
	 * @param ctx the parse tree
	 */
	void exitRowRange(ESLParser.RowRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#colRange}.
	 * @param ctx the parse tree
	 */
	void enterColRange(ESLParser.ColRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#colRange}.
	 * @param ctx the parse tree
	 */
	void exitColRange(ESLParser.ColRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(ESLParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(ESLParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#closeRange}.
	 * @param ctx the parse tree
	 */
	void enterCloseRange(ESLParser.CloseRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#closeRange}.
	 * @param ctx the parse tree
	 */
	void exitCloseRange(ESLParser.CloseRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#leftRange}.
	 * @param ctx the parse tree
	 */
	void enterLeftRange(ESLParser.LeftRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#leftRange}.
	 * @param ctx the parse tree
	 */
	void exitLeftRange(ESLParser.LeftRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#rightRange}.
	 * @param ctx the parse tree
	 */
	void enterRightRange(ESLParser.RightRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#rightRange}.
	 * @param ctx the parse tree
	 */
	void exitRightRange(ESLParser.RightRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#enumerateRange}.
	 * @param ctx the parse tree
	 */
	void enterEnumerateRange(ESLParser.EnumerateRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#enumerateRange}.
	 * @param ctx the parse tree
	 */
	void exitEnumerateRange(ESLParser.EnumerateRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#singleRange}.
	 * @param ctx the parse tree
	 */
	void enterSingleRange(ESLParser.SingleRangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#singleRange}.
	 * @param ctx the parse tree
	 */
	void exitSingleRange(ESLParser.SingleRangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ESLParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ESLParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#addExp}.
	 * @param ctx the parse tree
	 */
	void enterAddExp(ESLParser.AddExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#addExp}.
	 * @param ctx the parse tree
	 */
	void exitAddExp(ESLParser.AddExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#subExp}.
	 * @param ctx the parse tree
	 */
	void enterSubExp(ESLParser.SubExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#subExp}.
	 * @param ctx the parse tree
	 */
	void exitSubExp(ESLParser.SubExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#multExp}.
	 * @param ctx the parse tree
	 */
	void enterMultExp(ESLParser.MultExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#multExp}.
	 * @param ctx the parse tree
	 */
	void exitMultExp(ESLParser.MultExpContext ctx);
	/**
	 * Enter a parse tree produced by {@link ESLParser#divExp}.
	 * @param ctx the parse tree
	 */
	void enterDivExp(ESLParser.DivExpContext ctx);
	/**
	 * Exit a parse tree produced by {@link ESLParser#divExp}.
	 * @param ctx the parse tree
	 */
	void exitDivExp(ESLParser.DivExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code cellReference}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterCellReference(ESLParser.CellReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code cellReference}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitCellReference(ESLParser.CellReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code rangeAdd}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterRangeAdd(ESLParser.RangeAddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code rangeAdd}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitRangeAdd(ESLParser.RangeAddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code expressionWithParentheses}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterExpressionWithParentheses(ESLParser.ExpressionWithParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code expressionWithParentheses}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitExpressionWithParentheses(ESLParser.ExpressionWithParenthesesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code negativeExpression}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterNegativeExpression(ESLParser.NegativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code negativeExpression}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitNegativeExpression(ESLParser.NegativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code integerValue}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterIntegerValue(ESLParser.IntegerValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code integerValue}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitIntegerValue(ESLParser.IntegerValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code realValue}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterRealValue(ESLParser.RealValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code realValue}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitRealValue(ESLParser.RealValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(ESLParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringValue}
	 * labeled alternative in {@link ESLParser#primaryExp}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(ESLParser.StringValueContext ctx);
}