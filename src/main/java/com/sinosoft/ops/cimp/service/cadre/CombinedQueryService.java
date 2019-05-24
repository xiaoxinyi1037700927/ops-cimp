package com.sinosoft.ops.cimp.service.cadre;

import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery.*;
import com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery.*;

import java.util.List;

public interface CombinedQueryService {

    /**
     * 获取组合查询支持的函数
     *
     * @return
     */
    List<FunctionModel> getFunctions();

    /**
     * 获取组合查询支持的运算符
     *
     * @return
     */
    List<OperatorModel> getOperators();

    /**
     * 获取系统表字段
     *
     * @return
     */
    List<TableModel> getSysTableFields() throws BusinessException;

    /**
     * 获取组合查询信息
     *
     * @param id
     * @return
     * @throws CombinedQueryParseException
     */
    CombinedQueryModel getCombinedQuery(String id) throws CombinedQueryParseException;

    /**
     * 修改表达式
     *
     * @param modifyModel
     * @return
     * @throws CombinedQueryParseException
     */
    CombinedQueryModel modifyExprStr(ExprStrModifyModel modifyModel) throws CombinedQueryParseException;

    /**
     * 获取当前用户所有的组合查询信息
     *
     * @return
     */
    List<CombinedQueryModel> findAll();

    /**
     * 添加表达式
     *
     * @param appendModel
     * @return
     */
    ExprModel appendExpr(ExprAppendModel appendModel);

    /**
     * 删除表达式
     *
     * @param deleteModel
     * @return
     */
    ExprModel deleteExpr(ExprDeleteModel deleteModel);

    /**
     * 添加函数
     *
     * @param appendModel
     * @return
     */
    ExprModel appendFunction(FunctionAppendModel appendModel);

    /**
     * 删除函数
     *
     * @param deleteModel
     * @return
     */
    ExprModel deleteFunction(FunctionDeleteModel deleteModel);

    /**
     * 修改表达式
     *
     * @param modifyModel
     * @return
     */
    ExprModel modifyExpr(ExprModifyModel modifyModel);
}
