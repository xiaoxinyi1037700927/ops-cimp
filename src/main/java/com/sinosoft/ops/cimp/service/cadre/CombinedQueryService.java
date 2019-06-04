package com.sinosoft.ops.cimp.service.cadre;

import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery.*;
import com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery.*;

import java.util.List;

public interface CombinedQueryService {

    /**
     * 获取组合查询支持的函数
     *
     * @param searchModel
     * @return
     */
    List<FunctionModel> getFunctions(FunctionSearchModel searchModel);

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
     * @throws BusinessException
     */
    List<TableModel> getSysTableFields() throws BusinessException;

    /**
     * 获取组合查询信息
     *
     * @param id
     * @return
     * @throws BusinessException
     */
    CombinedQueryModel getCombinedQuery(String id) throws BusinessException;

    /**
     * 获取当前用户所有的组合查询信息
     *
     * @param searchModel
     * @return
     */
    PaginationViewModel<CombinedQueryModel> findAll(CombinedQuerySearchModel searchModel);

    /**
     * 添加表达式
     *
     * @param appendModel
     * @return
     * @throws BusinessException
     */
    ExprModel appendExpr(ExprAppendModel appendModel) throws BusinessException;

    /**
     * 删除表达式
     *
     * @param deleteModel
     * @return
     * @throws BusinessException
     */
    ExprModel deleteExpr(ExprDeleteModel deleteModel) throws BusinessException;

    /**
     * 添加函数
     *
     * @param appendModel
     * @return
     * @throws BusinessException
     */
    ExprModel appendFunction(FunctionAppendModel appendModel) throws BusinessException;

    /**
     * 删除函数
     *
     * @param deleteModel
     * @return
     * @throws BusinessException
     */
    ExprModel deleteFunction(FunctionDeleteModel deleteModel) throws BusinessException;

    /**
     * 修改表达式
     *
     * @param modifyModel
     * @return
     * @throws BusinessException
     */
    ExprModel modifyExpr(ExprModifyModel modifyModel) throws BusinessException;

    /**
     * 编译表达式
     *
     * @param combinedQueryId
     * @param exprStr
     * @return
     * @throws BusinessException
     */
    CompileResultModel compileExprStr(String combinedQueryId, String exprStr) throws BusinessException;

    /**
     * 统计表达式数量
     *
     * @param combinedQueryId
     * @return
     * @throws BusinessException
     */
    ExprStatisticsModel statisticsExpr(String combinedQueryId) throws BusinessException;


    /**
     * 保存组合查询
     *
     * @param saveModel
     * @throws BusinessException
     */
    void saveCombinedQuery(ExprSaveModel saveModel) throws BusinessException;

    /**
     * 删除组合查询
     *
     * @param combinedQueryId
     */
    void deleteCombinedQuery(String combinedQueryId);
}
