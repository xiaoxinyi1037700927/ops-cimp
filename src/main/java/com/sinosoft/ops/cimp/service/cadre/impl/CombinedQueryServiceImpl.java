package com.sinosoft.ops.cimp.service.cadre.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.constant.Constants;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableFieldInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableInfo;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.entity.combinedQuery.CombinedQuery;
import com.sinosoft.ops.cimp.entity.combinedQuery.QCombinedQuery;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.combinedQuery.CombinedQueryRepository;
import com.sinosoft.ops.cimp.service.cadre.CombinedQueryService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.combinedQuery.CombinedQueryParser;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Param;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.FieldNodeProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.ValueNodeProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.utils.CombinedQueryUtil;
import com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery.*;
import com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CombinedQueryServiceImpl implements CombinedQueryService {

    private final CombinedQueryParser parser;
    private final CombinedQueryRepository combinedQueryRepository;
    private final SysTableInfoDao sysTableInfoDao;
    private final FieldNodeProcessor fieldNodeProcessor;
    private final ValueNodeProcessor valueNodeProcessor;


    public CombinedQueryServiceImpl(CombinedQueryParser parser, CombinedQueryRepository combinedQueryRepository, SysTableInfoDao sysTableInfoDao, FieldNodeProcessor fieldNodeProcessor, ValueNodeProcessor valueNodeProcessor) {
        this.parser = parser;
        this.combinedQueryRepository = combinedQueryRepository;
        this.sysTableInfoDao = sysTableInfoDao;
        this.fieldNodeProcessor = fieldNodeProcessor;
        this.valueNodeProcessor = valueNodeProcessor;
    }

    private void putCache(String userId, String combinedQueryId, List<Expr> expr) {
        CacheManager.getInstance().put(Constants.COMBINED_QUERY_CACHE, userId + "/" + combinedQueryId, expr);
    }

    @SuppressWarnings("unchecked")
    private List<Expr> getCache(String userId, String combinedQueryId) {
        Object o = CacheManager.getInstance().get(Constants.COMBINED_QUERY_CACHE, userId + "/" + combinedQueryId);

        if (o == null) {
            //如果缓存中没有，尝试去数据库中获取
            Optional<CombinedQuery> optional = combinedQueryRepository.findById(combinedQueryId);
            if (optional.isPresent()) {
                try {
                    List<Expr> exprs = parser.parseExprs(optional.get().getExpression());
                    putCache(userId, combinedQueryId, exprs);
                    o = exprs;
                } catch (CombinedQueryParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return o != null ? (List<Expr>) o : new ArrayList<>();
    }

    /**
     * 获取组合查询支持的函数
     *
     * @param searchModel
     * @return
     */
    @Override
    public List<FunctionModel> getFunctions(FunctionSearchModel searchModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        //获取全部函数
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

        //过滤函数
        List<Expr> exprs = getCache(userId, searchModel.getCombinedQueryId());
        Expr expr = getExpr(exprs, searchModel.getExprId());

        if (expr != null) {
            filterFunction(result, expr.getParams(), null, searchModel);
        }

        return result;
    }

    /**
     * 过滤函数
     *
     * @param result
     * @param params
     * @param parent
     * @param searchModel
     * @return
     */
    private boolean filterFunction(List<FunctionModel> result, List<Param> params, Param parent, FunctionSearchModel searchModel) {
        for (int i = 0; i < params.size(); i++) {
            Param param = params.get(i);
            if (param.getId().equals(searchModel.getParamId())) {
                Iterator<FunctionModel> iterator = result.iterator();
                while (iterator.hasNext()) {
                    FunctionModel function = iterator.next();
                    if (searchModel.getAddOrUpdate() == 0) {
                        //新增函数

                        //参数为空字符串时，可以加任意函数，不为空时，根据参数类型过滤
                        if (StringUtils.isNotEmpty(param.getText()) && (function.getParamsNum() == 0 || (function.getParamsType().get(0).getCode() & param.getReturnType()) == 0)) {
                            iterator.remove();
                            continue;
                        }
                    } else {
                        //编辑函数

                        //编辑时过滤原函数
                        if (param.getFunctionName().equalsIgnoreCase(function.getName())) {
                            iterator.remove();
                            continue;
                        }

                        //判断参数个数是否和原来一样
                        if (param.getParams().size() != function.getParamsType().size()) {
                            iterator.remove();
                            continue;
                        }

                        //判断各个参数类型是否匹配
                        boolean isMatch = true;
                        for (int j = 0; j < function.getParamsType().size(); j++) {
                            if ((function.getParamsType().get(j).getCode() & param.getParams().get(j).getReturnType()) == 0) {
                                isMatch = false;
                                break;
                            }
                        }
                        if (!isMatch) {
                            iterator.remove();
                            continue;
                        }
                    }

                    //如果有外层函数，判断返回类型是否一样
                    if (parent != null) {
                        //获取上级函数
                        Function func = Function.getByName(parent.getFunctionName());

                        if ((func.getParamsType()[i] & function.getReturnType().getCode()) == 0) {
                            iterator.remove();
                        }
                    }
                }
            }

            //递归处理下一级
            if (param.getIsFunction() == 1 && filterFunction(result, param.getParams(), param, searchModel)) {
                return true;
            }
        }

        return false;
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
            model.setMultiselect(operator.isArray());

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
                field.setType(fieldNodeProcessor.getReturnType(sysTableFieldInfo).getCode());

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
     * @throws BusinessException
     */
    @Override
    public CombinedQueryModel getCombinedQuery(String id) throws BusinessException {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();

        //获取组合查询信息
        CombinedQuery combinedQuery = combinedQueryRepository.getOne(id);
        List<Expr> exprs = null;
        try {
            //解析表达式字符串
            exprs = parser.parseExprs(combinedQuery.getExpression());
        } catch (CombinedQueryParseException e) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, e.getMessage());
        }

        //将表达式信息存入缓存
        putCache(userId, combinedQuery.getId(), exprs);

        CombinedQueryModel model = new CombinedQueryModel();
        model.setCombinedQueryId(id);
        model.setExprstr(parser.parseExprStr(exprs, true));
        model.setExpr(exprs);

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
        List<Expr> exprs = getCache(userId, combinedQueryId);

        //定位添加表达式的位置,默认添加在最外层最后
        List<Expr> position = exprs;
        if (StringUtils.isNotEmpty(appendModel.getExprId())) {
            Expr e = getExpr(exprs, appendModel.getExprId());
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
            newExpr.setParams(appendModel.getParams().stream()
                    .map(text -> new Param(IdUtil.uuid(), text)).collect(Collectors.toList()));
            processParams(newExpr.getParams(), appendModel.getOperator());
        }
        newExpr.setText(parser.parseExprStr(Collections.singletonList(newExpr), false));

        //编译表达式
        newExpr.setWrongMessage(parser.compile(newExpr.getText()));
        newExpr.setCompilePass(newExpr.getWrongMessage() == null);


        //添加表达式
        position.add(newExpr);
        //更新缓存
        putCache(userId, combinedQueryId, exprs);

        ExprModel result = new ExprModel();
        result.setCombinedQueryId(combinedQueryId);
        result.setExpr(newExpr);
        result.setExprstr(parser.parseExprStr(exprs, true));

        return result;
    }

    /**
     * 参数处理
     *
     * @param params
     * @param operator
     */
    private void processParams(List<Param> params, String operator) {
        String codeSetName = null;
        String codes;
        boolean multiselect = operator != null && Operator.getByName(operator).isArray();

        for (Param param : params) {
            if (param.getIsFunction() == 1) {
                continue;
            }
            String text = param.getText();
            try {
                if (fieldNodeProcessor.support(text)) {
                    //为字段值添加id和返回类型
                    FieldNode node = (FieldNode) fieldNodeProcessor.parse(text);
                    param.setTableId(node.getTableId());
                    param.setFieldId(node.getFieldId());
                    param.setReturnType(node.getReturnType());
                    param.setType(Param.Type.FIELD.getName());
                    codeSetName = node.getCodeSetName();
                } else if ((codes = valueNodeProcessor.isCode(text)) != null) {
                    //码值
                    param.setReturnType(Type.CODE.getCode());
                    param.setType(Param.Type.CODE.getName());
                    param.setCodeSetName(codeSetName);
                    param.setFieldId(codes);
                    param.setMultiselect(multiselect);
                } else {
                    param.setType(Param.Type.VALUE.getName());
                    param.setMultiselect(multiselect);

                    int type = Type.STRING.getCode();
                    String value = text.substring(text.indexOf("'") + 1, text.lastIndexOf("'"));
                    if (CombinedQueryUtil.isNumber(value)) {
                        type |= Type.NUMBER.getCode();
                    } else if (CombinedQueryUtil.isDate(value)) {
                        type |= Type.DATE.getCode();
                    }

                    param.setReturnType(type);
                }
            } catch (Exception e) {
                param.setReturnType(Type.UNKNOWN.getCode());
            }
        }
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
        result.setExprstr(parser.parseExprStr(exprs, true));

        return result;
    }

    /**
     * 添加函数
     *
     * @param appendModel
     * @return
     */
    @Override
    public ExprModel appendFunction(FunctionAppendModel appendModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        String combinedQueryId = appendModel.getCombinedQueryId();

        List<Expr> exprs = getCache(userId, combinedQueryId);

        Expr expr = getExpr(exprs, appendModel.getExprId());

        //添加函数
        doAppendFunction(expr.getParams(), appendModel);

        //刷新表达式下所有text
        parser.refreshText(expr);
        //编译表达式
        expr.setWrongMessage(parser.compile(expr.getText()));
        expr.setCompilePass(expr.getWrongMessage() == null);

        putCache(userId, combinedQueryId, exprs);


        ExprModel result = new ExprModel();
        result.setExpr(expr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCombinedQueryId(combinedQueryId);

        return result;
    }

    /**
     * 添加函数
     *
     * @param params
     * @param appendModel
     */
    private boolean doAppendFunction(List<Param> params, FunctionAppendModel appendModel) {
        Param target = null;
        for (Param param : params) {
            if (param.getId().equals(appendModel.getParamId())) {
                target = param;
                break;
            }

            if (param.getIsFunction() == 1 && doAppendFunction(param.getParams(), appendModel)) {
                return true;
            }
        }

        if (target != null) {
            int index = params.indexOf(target);
            params.remove(target);

            Param newFunction = new Param();
            newFunction.setId(IdUtil.uuid());
            newFunction.setIsFunction(1);
            newFunction.setFunctionName(appendModel.getFunctionName());
            newFunction.setType(Param.Type.FUNCTION.getName());


            Function func = Function.getByName(appendModel.getFunctionName());
            int paramNum = func != null ? func.getParamsNum() : 0;

            List<Param> newParams = new ArrayList<>();
            newFunction.setParams(newParams);
            newFunction.setReturnType(func.getReturnType());

            if (paramNum > 0) {
                newParams.add(target);
                for (int i = 1; i < paramNum; i++) {
                    newParams.add(new Param(IdUtil.uuid(), "''"));
                }
            }

            params.add(index, newFunction);
            return true;
        }

        return false;
    }


    /**
     * 删除函数
     *
     * @param deleteModel
     * @return
     */
    @Override
    public ExprModel deleteFunction(FunctionDeleteModel deleteModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        String combinedQueryId = deleteModel.getCombinedQueryId();

        List<Expr> exprs = getCache(userId, combinedQueryId);

        Expr expr = getExpr(exprs, deleteModel.getExprId());

        //删除函数
        doDeleteFunction(expr.getParams(), deleteModel);

        //刷新表达式下所有text
        parser.refreshText(expr);

        //编译表达式
        expr.setWrongMessage(parser.compile(expr.getText()));
        expr.setCompilePass(expr.getWrongMessage() == null);

        putCache(userId, combinedQueryId, exprs);


        ExprModel result = new ExprModel();
        result.setExpr(expr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCombinedQueryId(combinedQueryId);

        return result;
    }

    /**
     * 删除函数
     *
     * @param params
     * @param deleteModel
     */
    private boolean doDeleteFunction(List<Param> params, FunctionDeleteModel deleteModel) {
        Param target = null;
        for (Param param : params) {
            if (param.getId().equals(deleteModel.getParamId())) {
                target = param;
                break;
            }

            if (param.getIsFunction() == 1 && doDeleteFunction(param.getParams(), deleteModel)) {
                return true;
            }
        }

        if (target != null) {
            int index = params.indexOf(target);
            params.remove(target);

            params.add(index, target.getParams().size() > 0 ? target.getParams().get(0) : new Param(IdUtil.uuid(), "''"));
            return true;
        }

        return false;
    }


    /**
     * 修改表达式
     *
     * @param modifyModel
     * @return
     */
    @Override
    public ExprModel modifyExpr(ExprModifyModel modifyModel) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        String combinedQueryId = modifyModel.getCombinedQueryId();

        List<Expr> exprs = getCache(userId, combinedQueryId);

        Expr expr = getExpr(exprs, modifyModel.getExprId());

        if (StringUtils.isEmpty(modifyModel.getParamId())) {
            if (StringUtils.isNotEmpty(modifyModel.getLogicalOperator())) {
                //修改逻辑操作符
                expr.setLogicalOperator(modifyModel.getLogicalOperator());
            } else {
                //修改运算符
                doModifyOperator(expr, modifyModel);
            }
        } else {
            //修改参数
            doModifyParam(expr.getParams(), modifyModel);
        }


        //刷新表达式下所有text
        parser.refreshText(expr);

        //编译表达式
        expr.setWrongMessage(parser.compile(expr.getText()));
        expr.setCompilePass(expr.getWrongMessage() == null);

        putCache(userId, combinedQueryId, exprs);


        ExprModel result = new ExprModel();
        result.setExpr(expr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCombinedQueryId(combinedQueryId);

        return result;
    }

    /**
     * 修改运算符
     *
     * @param expr
     * @param modifyModel
     */
    private void doModifyOperator(Expr expr, ExprModifyModel modifyModel) {
        expr.setOperator(modifyModel.getOperator());

        Operator op = Operator.getByName(expr.getOperator());
        int paramsNum = op != null ? op.getParamsNum() : 1;

        List<Param> oldParams = expr.getParams();
        if (oldParams.size() > paramsNum) {
            while (oldParams.size() > paramsNum) {
                oldParams.remove(paramsNum);
            }
        } else if (oldParams.size() < paramsNum) {
            while (oldParams.size() < paramsNum) {
                oldParams.add(new Param(IdUtil.uuid(), "''"));
            }
        }

        processParams(oldParams, modifyModel.getOperator());
    }

    /**
     * 修改参数
     *
     * @param params
     * @param modifyModel
     */
    private boolean doModifyParam(List<Param> params, ExprModifyModel modifyModel) {
        for (Param param : params) {
            if (param.getId().equals(modifyModel.getParamId())) {
                if (param.getIsFunction() == 0) {
                    //值
                    param.setText(modifyModel.getValue());
                } else {
                    //函数
                    param.setFunctionName(modifyModel.getFunctionName());

                    //修正函数参数数量
                    Function func = Function.getByName(modifyModel.getFunctionName());
                    int paramsNum = func != null ? func.getParamsNum() : 0;

                    param.setReturnType(func.getReturnType());
                    List<Param> oldParams = param.getParams();
                    if (oldParams.size() > paramsNum) {
                        while (oldParams.size() > paramsNum) {
                            oldParams.remove(paramsNum);
                        }
                    } else if (oldParams.size() < paramsNum) {
                        while (oldParams.size() < paramsNum) {
                            oldParams.add(new Param(IdUtil.uuid(), "''"));
                        }
                    }
                }

                processParams(params, null);
                return true;
            }

            if (param.getIsFunction() == 1 && doModifyParam(param.getParams(), modifyModel)) {
                return true;
            }
        }

        return false;
    }


    /**
     * 根据id获取表达式
     *
     * @param exprs
     * @param id
     * @return
     */
    private Expr getExpr(List<Expr> exprs, String id) {
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
            return false;
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


    /**
     * 编译表达式
     *
     * @param combinedQueryId
     * @param exprStr
     * @return
     */
    @Override
    public CompileResultModel compileExprStr(String combinedQueryId, String exprStr) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        CompileResultModel result = new CompileResultModel();
        try {
            if (StringUtils.isBlank(combinedQueryId)) {
                combinedQueryId = IdUtil.uuid();
            }

            result.setCombinedQueryId(combinedQueryId);
            result.setExprs(parser.parseExprs(exprStr));
            result.setExprstr(parser.parseExprStr(result.getExprs(), true));
            result.setCompilePass(true);

            putCache(userId, combinedQueryId, result.getExprs());
            System.out.println(parser.parseSql(result.getExprstr()));
        } catch (CombinedQueryParseException e) {
            e.printStackTrace();
            result.setCompilePass(false);
            result.setWrongMessage(e.getMessage());
        }

        return result;
    }

    /**
     * 统计表达式数量
     *
     * @param combinedQueryId
     * @return
     */
    @Override
    public ExprStatisticsModel statisticsExpr(String combinedQueryId) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();

        ExprStatisticsModel result = new ExprStatisticsModel();

        List<Expr> exprs = getCache(userId, combinedQueryId);

        statictics(result, exprs);

        return result;
    }

    /**
     * 保存组合查询
     *
     * @param saveModel
     * @throws BusinessException
     */
    @Override
    public void saveCombinedQuery(ExprSaveModel saveModel) throws BusinessException {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        Optional<CombinedQuery> optional = combinedQueryRepository.findById(saveModel.getCombinedQueryId());

        CombinedQuery combinedQuery = null;
        if (optional.isPresent()) {
            combinedQuery = optional.get();
        } else {
            combinedQuery = new CombinedQuery();
            combinedQuery.setId(saveModel.getCombinedQueryId());
            combinedQuery.setUserId(userId);

            combinedQuery.setCreateId(userId);
            combinedQuery.setCreateTime(new Date());
        }

        List<Expr> exprs;
        try {
            exprs = parser.parseExprs(saveModel.getExprStr());
        } catch (CombinedQueryParseException e) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, e.getMessage());
        }

        combinedQuery.setExpression(parser.parseExprStr(exprs, true));
        combinedQuery.setName(saveModel.getName());
        combinedQuery.setModifyId(userId);
        combinedQuery.setModifyTime(new Date());

        combinedQueryRepository.save(combinedQuery);
    }

    /**
     * 统计表达式数量
     *
     * @param result
     * @param exprs
     */
    private void statictics(ExprStatisticsModel result, List<Expr> exprs) {
        for (Expr expr : exprs) {
            if (expr.isBracketsNode()) {
                result.incrNestedExprNum();
                statictics(result, expr.getSubExprs());
            } else {
                result.incrGeneralExprNum();
            }

            if (!expr.isCompilePass()) {
                result.incrWrongExprNum();
            }
        }
    }
}
