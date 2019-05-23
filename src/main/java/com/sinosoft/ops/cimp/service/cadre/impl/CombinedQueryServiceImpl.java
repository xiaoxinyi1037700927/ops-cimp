package com.sinosoft.ops.cimp.service.cadre.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.constant.Constants;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableFieldInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.entity.combinedQuery.CombinedQuery;
import com.sinosoft.ops.cimp.entity.combinedQuery.QCombinedQuery;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.combinedQuery.CombinedQueryRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableFieldRepository;
import com.sinosoft.ops.cimp.repository.sys.systable.SysTableRepository;
import com.sinosoft.ops.cimp.service.cadre.CombinedQueryService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.combinedQuery.CombinedQueryParser;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Param;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CombinedQueryServiceImpl implements CombinedQueryService {

    private final CombinedQueryParser parser;
    private final CombinedQueryRepository combinedQueryRepository;
    private final SysTableRepository tableRepository;
    private final SysTableFieldRepository fieldRepository;
    private final SysTableInfoDao sysTableInfoDao;


    public CombinedQueryServiceImpl(CombinedQueryParser parser, CombinedQueryRepository combinedQueryRepository, SysTableRepository tableRepository, SysTableFieldRepository fieldRepository, SysTableInfoDao sysTableInfoDao) {
        this.parser = parser;
        this.combinedQueryRepository = combinedQueryRepository;
        this.tableRepository = tableRepository;
        this.fieldRepository = fieldRepository;
        this.sysTableInfoDao = sysTableInfoDao;
    }

    private void putCache(String userId, String combinedQueryId, List<Expr> expr) {
        CacheManager.getInstance().put(Constants.COMBINED_QUERY_CACHE, userId + "/" + combinedQueryId, expr);
    }

    @SuppressWarnings("unchecked")
    private List<Expr> getCache(String userId, String combinedQueryId) {
        Object o = CacheManager.getInstance().get(Constants.COMBINED_QUERY_CACHE, userId + "/" + combinedQueryId);
        return o != null ? (List<Expr>) o : new ArrayList<>();
    }

    /**
     * 获取组合查询支持的函数
     *
     * @return
     */
    @Override
    public List<FunctionModel> getFunctions() {
        List<FunctionModel> result = new ArrayList<>();

        for (Function function : Function.values()) {
            FunctionModel model = new FunctionModel();
            model.setName(function.getName());

            int returnType = function.getReturnType();
            model.setReturnType(new TypeModel(Type.getNameByCode(returnType), returnType));
            model.setParamsType(Arrays.stream(function.getParamsType()).mapToObj(paramType -> new TypeModel(Type.getNameByCode(paramType), paramType)).collect(Collectors.toList()));
            model.setParamsNum(function.getParamsType().length);

            result.add(model);
        }

        return result;
    }

    /**
     * 获取组合查询支持的运算符
     *
     * @return
     */
    @Override
    public List<OperatorModel> getOperators() {
        List<OperatorModel> result = new ArrayList<>();

        for (Operator operator : Operator.values()) {
            OperatorModel model = new OperatorModel();
            model.setName(operator.getName());
            model.setParamsType(operator.getParamsType());
            model.setParamsNum(operator.getParamsType().length);

            result.add(model);
        }

        return result;
    }

    /**
     * 获取系统表字段
     *
     * @return
     */
    @Override
    public List<TableModel> getSysTableFields() throws BusinessException {
        SysTableModelInfo cadreInfo = sysTableInfoDao.getTableInfo("cadreInfo");

        List<TableModel> tables = new ArrayList<>();
        for (SysTableInfo sysTableInfo : cadreInfo.getTables()) {
            TableModel table = new TableModel();
            table.setId(sysTableInfo.getId());
            table.setName(sysTableInfo.getNameCn());

            List<FieldModel> fields = new ArrayList<>();
            for (SysTableFieldInfo sysTableFieldInfo : sysTableInfo.getTableFields()) {
                if (StringUtils.isEmpty(sysTableFieldInfo.getNameCn())) {
                    continue;
                }
                FieldModel field = new FieldModel();
                field.setId(sysTableFieldInfo.getId());
                field.setName(sysTableFieldInfo.getNameCn());
                field.setCodeSetName(sysTableFieldInfo.getCodeSetName());

                fields.add(field);
            }
            table.setFields(fields);
            tables.add(table);
        }

        return tables;
    }

    /**
     * 获取组合查询信息
     *
     * @param id
     * @return
     * @throws CombinedQueryParseException
     */
    @Override
    public CombinedQueryModel getCombinedQuery(String id) throws CombinedQueryParseException {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();

        //获取组合查询信息
        CombinedQuery combinedQuery = combinedQueryRepository.getOne(id);
        //将表达式解析为表达式树
        List<Expr> expr = parser.parseExprTree(combinedQuery.getExpression());

        //将表达式树存入缓存
        putCache(userId, combinedQuery.getId(), expr);

        CombinedQueryModel model = new CombinedQueryModel();
        model.setCombinedQueryId(id);
        model.setExprstr(combinedQuery.getExpression());
        model.setExpr(expr);

        return model;
    }

    /**
     * 修改表达式
     *
     * @param modifyModel
     * @return
     * @throws CombinedQueryParseException
     */
    @Override
    public CombinedQueryModel modifyExprStr(ExprStrModifyModel modifyModel) throws CombinedQueryParseException {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        String combinedQueryId = modifyModel.getCombinedQueryId();
        String exprStr = modifyModel.getExprStr();

        //如果id为空，创建
        if (StringUtils.isEmpty(combinedQueryId)) {
            combinedQueryId = IdUtil.uuid();
        }

        List<Expr> expr = parser.parseExprTree(exprStr);
        exprStr = parser.parseExprStr(expr);

        //将表达式树存入缓存
        putCache(userId, combinedQueryId, expr);

        CombinedQueryModel model = new CombinedQueryModel();
        model.setExprstr(exprStr);
        model.setExpr(expr);

        return model;
    }

    /**
     * 获取当前用户所有的组合查询信息
     *
     * @return
     */
    @Override
    public List<CombinedQueryModel> findAll() {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        List<String> roleIds = SecurityUtils.getSubject().getCurrentUserRole().stream().map(UserRole::getRoleId).collect(Collectors.toList());

        QCombinedQuery qCombinedQuery = QCombinedQuery.combinedQuery;
        Iterable<CombinedQuery> iterable = combinedQueryRepository.findAll(qCombinedQuery.roleId.in(roleIds).and(qCombinedQuery.userId.isNull().or(qCombinedQuery.userId.eq(userId))));

        return Lists.newArrayList(iterable).stream().map(combinedQuery -> {
            CombinedQueryModel model = new CombinedQueryModel();
            model.setCombinedQueryId(combinedQuery.getId());
            model.setName(combinedQuery.getName());
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 添加表达式
     *
     * @param appendModel
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public ExprModel appendExpr(ExprAppendModel appendModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        String combinedQueryId = appendModel.getCombinedQueryId();
        if (StringUtils.isEmpty(combinedQueryId)) {
            combinedQueryId = IdUtil.uuid();
        }

        //获取组合查询缓存
        List<Expr> expr = getCache(userId, combinedQueryId);

        //定位添加表达式的位置,默认添加在最外层最后
        List<Expr> position = expr;
        if (StringUtils.isNotEmpty(appendModel.getExprId())) {
            Expr e = getExpr(expr, appendModel.getExprId());
            if (e != null && e.isBracketsNode()) {
                position = e.getSubExprs();
            }
        }

        //创建表达式对象
        Expr newExpr = new Expr();
        newExpr.setId(IdUtil.uuid());
        newExpr.setLogicalOperator(appendModel.getLogicalOperator());
        if (appendModel.IsBrackets()) {
            newExpr.setBracketsNode(true);
            newExpr.setSubExprs(new ArrayList<>());
        } else {
            newExpr.setOperator(appendModel.getOperator());
            newExpr.setParams(appendModel.getParams().stream().map(Param::new).collect(Collectors.toList()));
        }

        //添加表达式
        position.add(newExpr);
        //更新缓存
        putCache(userId, combinedQueryId, expr);

        ExprModel result = new ExprModel();
        result.setCombinedQueryId(combinedQueryId);
        result.setExpr(newExpr);
        result.setExprstr(parser.parseExprStr(expr));
        result.setComplilePass(parser.compile(parser.parseExprStr(newExpr)));

        return result;
    }

    /**
     * 删除表达式
     *
     * @param deleteModel
     * @return
     */
    @Override
    public ExprModel deleteExpr(ExprDeleteModel deleteModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        String combinedQueryId = deleteModel.getCombinedQueryId();
        List<Expr> exprs = getCache(userId, combinedQueryId);

        deleteExpr(exprs, deleteModel.getExprId());

        putCache(userId, combinedQueryId, exprs);

        ExprModel result = new ExprModel();
        result.setCombinedQueryId(combinedQueryId);
        result.setExprstr(parser.parseExprStr(exprs));
        result.setComplilePass(parser.compile(result.getExprstr()));

        return result;
    }

    /**
     * 根据id获取表达式
     *
     * @param exprs
     * @param id
     * @return
     */
    private Expr getExpr(List<Expr> exprs, String id) {
        if (exprs == null || exprs.size() == 0) {
            return null;
        }

        for (Expr expr : exprs) {
            if (expr.getId().equals(id)) {
                return expr;
            }

            //处理子节点
            if (expr.isBracketsNode()) {
                Expr e = getExpr(expr.getSubExprs(), id);
                if (e != null) {
                    return e;
                }
            }
        }
        return null;
    }

    /**
     * 删除表达式
     *
     * @param exprs
     * @param id
     * @return
     */
    private boolean deleteExpr(List<Expr> exprs, String id) {
        if (exprs == null || exprs.size() == 0) {
            return true;
        }

        for (Expr expr : exprs) {
            if (expr.getId().equals(id)) {
                return exprs.remove(expr);
            }

            //处理子节点
            if (expr.isBracketsNode()) {
                if (deleteExpr(expr.getSubExprs(), id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
