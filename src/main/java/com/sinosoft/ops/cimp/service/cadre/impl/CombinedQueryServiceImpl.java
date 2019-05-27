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
import com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery.*;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreDataVO;
import com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                    return null;
                }
            }
        }

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
                    .map(text -> {
                        Param param = new Param(IdUtil.uuid(), text);
                        processParam(param);
                        return param;
                    }).collect(Collectors.toList()));
        }
        newExpr.setText(parser.parseExprStr(Collections.singletonList(newExpr), false));


        //添加表达式
        position.add(newExpr);
        //更新缓存
        putCache(userId, combinedQueryId, exprs);

        ExprModel result = new ExprModel();
        result.setCombinedQueryId(combinedQueryId);
        result.setExpr(newExpr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCompilePass(parser.compile(newExpr.getText()));

        return result;
    }

    /**
     * 参数处理
     *
     * @param param
     */
    private void processParam(Param param) {
        String text = param.getText();
        try {
            if (fieldNodeProcessor.support(text)) {
                //为字段值添加id和返回类型
                FieldNode node = (FieldNode) fieldNodeProcessor.parse(text);
                param.setTableId(node.getTableId());
                param.setFieldId(node.getFieldId());
                param.setReturnType(node.getReturnType());
            } else if (valueNodeProcessor.isCode(text)) {
                //码值
                param.setReturnType(Type.CODE.getCode());
            } else {
                try {
                    //判断输入值是否可转为数字
                    new BigDecimal(text.substring(text.indexOf("'") + 1, text.lastIndexOf("'")));
                    param.setReturnType(Type.STRING.getCode() | Type.NUMBER.getCode());
                } catch (Exception e) {
                    param.setReturnType(Type.STRING.getCode());
                }
            }
        } catch (CombinedQueryParseException e) {
            param.setReturnType(Type.UNKNOWN.getCode());
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
        result.setCompilePass(parser.compile(result.getExprstr()));

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

        putCache(userId, combinedQueryId, exprs);


        ExprModel result = new ExprModel();
        result.setExpr(expr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCombinedQueryId(combinedQueryId);
        result.setCompilePass(parser.compile(expr.getText()));

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

        putCache(userId, combinedQueryId, exprs);


        ExprModel result = new ExprModel();
        result.setExpr(expr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCombinedQueryId(combinedQueryId);
        result.setCompilePass(parser.compile(expr.getText()));

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

        putCache(userId, combinedQueryId, exprs);


        ExprModel result = new ExprModel();
        result.setExpr(expr);
        result.setExprstr(parser.parseExprStr(exprs, true));
        result.setCombinedQueryId(combinedQueryId);
        result.setCompilePass(parser.compile(expr.getText()));

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
                    processParam(param);
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

    @Override
    public String getsql(String combinedQueryId) {
        String userId = SecurityUtils.getSubject().getCurrentUser().getId();
        try {

            return parser.parseSql(parser.parseExprStr(getCache(userId, combinedQueryId), false));
        } catch (CombinedQueryParseException e) {
            e.printStackTrace();
            return e.getMessage();
        }
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
     * 获取干部列表
     *
     * @param searchModel
     * @return
     * @throws BusinessException
     */
    @Override
    public CadreDataVO listCadre(CadreSearchModel searchModel) throws BusinessException {
        boolean includeSubNode = "1".equals(searchModel.getIncludeSubNode());
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 1;
        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 10;
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = pageIndex * pageSize;

        Optional<CombinedQuery> optional = combinedQueryRepository.findById(searchModel.getCombinedQueryId());
        String combinedQuerySql = "";
        if (optional.isPresent()) {
            try {
                combinedQuerySql = parser.parseSql(optional.get().getExpression());
            } catch (CombinedQueryParseException e) {
                throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "组合查询解析失败，请核对组合查询信息!");
            }
        }

        String sql = "SELECT\n" +
                "  EMP_ID                                      AS \"EMP_ID\",\n" +
                "  A01001                                      AS \"a01001\",\n" +
                "  A01004                                      AS \"a01004\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 18 AND code = A01004)  AS \"a01004value\",\n" +
                "  to_char(A01007, 'yyyy-mm-dd')               AS \"a01007\",\n" +
                "  A01017                                      AS \"a01017\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 23 AND code = A01017)  AS \"a01017value\",\n" +
                "  A01011_A                                    AS \"a01011A\",\n" +
                "  A01014_B                                    AS \"a01014B\",\n" +
                "  A01011_B                                    AS \"a01014B\",\n" +
                "  to_char(A01034, 'yyyy-mm-dd')               AS \"a01034\",\n" +
                "  A01028                                      AS \"a01028\",\n" +
                "  A001003                                     AS \"A001003\",\n" +
                "  A01060                                      AS \"A01060\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 161 AND code = A01060) AS \"A01060value\",\n" +
                "  A01057_A                                    AS \"a01057A\",\n" +
                "  A01065                                      AS \"a01065\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 162 AND code = A01065) AS \"a01065value\",\n" +
                "  A01057                                      AS \"a01057\",\n" +
                "  A01111                                      AS \"a01111\",\n" +
                "  A01062                                      AS \"a01062\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 9 AND code = A01062)   AS \"a01062value\",\n" +
                "  A01081                                      AS \"a01081\",\n" +
                "  A01063                                      AS \"a01063\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 101 AND code = A01063) AS \"a01063value\",\n" +
                "  A01031                                      AS \"a01031\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 24 AND code = A01031)  AS \"a01031value\",\n" +
                "  A01057_B                                    AS \"a01057B\",\n" +
                "  A01027                                      AS \"a01027\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 20 AND code = A01027)  AS \"a01027value\",\n" +
                "  A01002_A                                    AS \"a01002A\",\n" +
                "  A01015                                      AS \"a01015\",\n" +
                "  A01002_B                                    AS \"a01002B\",\n" +
                "  to_char(A01040, 'yyyy-mm-dd')               AS \"a01040\",\n" +
                "  A01051                                      AS \"a01051\",\n" +
                "  to_char(A01094, 'yyyy-mm-dd')               AS \"a01094\",\n" +
                "  A01088                                      AS \"a01088\",\n" +
                "  A01061                                      AS \"a01061\",\n" +
                "  A01087                                      AS \"a01087\",\n" +
                "  A01097                                      AS \"a01097\",\n" +
                "  A01014_Z                                    AS \"a01014Z\",\n" +
                "  A01110                                      AS \"a01110\",\n" +
                "  A001004_A                                   AS \"a001004a\",\n" +
                "  A01014_A                                    AS \"a01014A\",\n" +
                "  A01067                                      AS \"a01067\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 166 AND code = A01067) AS \"a01067value\",\n" +
                "  A01058                                      AS \"a01058\",\n" +
                "  A01071                                      AS \"a01071\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 86 AND code = A01071)  AS \"a01071value\",\n" +
                "  A01074                                      AS \"a01074\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 1 AND code = A01074)   AS \"a01074value\",\n" +
                "  A01103                                      AS \"a01103\",\n" +
                "  A01107                                      AS \"a01107\",\n" +
                "  A01073                                      AS \"a01073\",\n" +
                "  A01090                                      AS \"a01090\",\n" +
                "  to_char(A01054, 'yyyy-mm-dd')               AS \"a01054\",\n" +
                "  A01052                                      AS \"a01052\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 106 AND code = A01052) AS \"a01052value\",\n" +
                "  A01048                                      AS \"a01048\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 19 AND code = A01048)  AS \"a01048value\",\n" +
                "  A01108                                      AS \"a01108\",\n" +
                "  A01086                                      AS \"a01086\",\n" +
                "  A01037                                      AS \"a01037\",\n" +
                "  A01044                                      AS \"a01044\",\n" +
                "  A01084                                      AS \"a01084\",\n" +
                "  A01083                                      AS \"a01083\"\n" +
                "FROM (SELECT\n" +
                "        ROWNUM AS rn,\n" +
                "        t.*\n" +
                "      FROM (SELECT\n" +
                "              a001.*,\n" +
                "              nvl(tmp.codeLen, '99999') as codeLen,\n" +
                "              nvl(tmp.code, '99999') as code,\n" +
                "              nvl(tmp.sortNum, '99999') as sortNum,\n" +
                "              nvl(tmp.orgSortNum, '99999') as orgSortNum\n" +
                "            FROM EMP_A001 a001 LEFT JOIN (SELECT\n" +
                "                                             a02.EMP_ID,\n" +
                "                                             min(length(nvl(org.code, '99999')))         AS codeLen,\n" +
                "                                             nvl(min(nvl(org.code, '99999')), '99999')   AS code,\n" +
                "                                             nvl(min(nvl(a02.A02025, '99999')), '99999') AS orgSortNum,\n" +
                "                                             nvl(min(nvl(a02.A02023, '99999')), '99999') AS sortNum\n" +
                "                                           FROM EMP_A02 a02 INNER JOIN ORGANIZATION org ON a02.A02001_B = org.ID\n" +
                "                                           WHERE a02.STATUS = '0' AND a02.A02055 = '2' AND org.CODE LIKE (SELECT code " + (includeSubNode ? "|| '%'" : "") + " FROM ORGANIZATION WHERE ID = '" + searchModel.getDeptId() + "')\n" +
                "                                           GROUP BY a02.EMP_ID) tmp ON a001.EMP_ID = tmp.EMP_ID\n" +
                "            WHERE a001.A01063 = '1' AND a001.STATUS = '0' " + combinedQuerySql +
                (includeSubNode ? "  ORDER BY codeLen, code, sortNum, a001.ORDINAL" : "ORDER BY orgSortNum ") +
                "    ) t  WHERE ROWNUM <= '" + endIndex + "') t\n" +
                "WHERE rn > '" + startIndex + "'";


        return null;
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
        }
    }
}
