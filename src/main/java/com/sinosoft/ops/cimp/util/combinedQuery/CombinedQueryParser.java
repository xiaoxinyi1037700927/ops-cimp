package com.sinosoft.ops.cimp.util.combinedQuery;

import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Expr;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.ExprStream;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.Param;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.*;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.LogicalOperator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.NodeProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.OperatorNodeProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.post.PostProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CombinedQueryParser {

    private final NodeProcessor[] nodeProcessors;
    private final OperatorNodeProcessor operatorNodeProcessor;
    private final PostProcessor[] postProcessors;

    @Autowired
    public CombinedQueryParser(NodeProcessor[] nodeProcessors, OperatorNodeProcessor operatorNodeProcessor, PostProcessor[] postProcessors) {
        this.nodeProcessors = nodeProcessors;
        this.operatorNodeProcessor = operatorNodeProcessor;
        this.postProcessors = postProcessors;
    }

    /**
     * 将表达式树解析为表达式
     *
     * @param exprs
     * @param format
     * @return
     */
    public String parseExprStr(List<Expr> exprs, boolean format) {
        StringBuilder sb = new StringBuilder();
        for (Expr expr : exprs) {
            if (expr.isBracketsNode()) {
                sb.append(" ").append(expr.getLogicalOperator())
                        .append(" (").append(format ? "\n" : "")
                        .append(parseExprStr(expr.getSubExprs(), format))
                        .append(")").append(format ? "\n" : "");
            } else {
                refreshText(expr);
                sb.append(expr.getText()).append(format ? "\n" : "");
            }
        }

        return sb.toString();
    }

    /**
     * 刷新非括号表达式节点下各级的text
     *
     * @param expr
     */
    public void refreshText(Expr expr) {
        //刷新参数的text
        refreshParams(expr.getParams());

        List<Param> params = expr.getParams();
        String opStr = " " + expr.getLogicalOperator() + " ";

        Operator op = null;
        try {
            op = Operator.getByName(expr.getOperator());
            //如果是已定义的运算符，使用运算符定义的格式
            expr.setText(opStr + op.getExpr(params.stream().map(Param::getText).collect(Collectors.toList())));
        } catch (CombinedQueryParseException e) {
            //未定义运算符,使用默认格式
            StringBuilder sb = new StringBuilder(opStr);
            sb.append(params.get(0).getText());
            sb.append(" ").append(expr.getOperator()).append(" ");
            for (int i = 1; i < params.size(); i++) {
                sb.append(params.get(i).getText());
            }
            expr.setText(sb.toString());
        }

    }

    /**
     * 刷新参数节点下各级的text
     *
     * @param params
     * @return
     */
    private void refreshParams(List<Param> params) {
        for (Param param : params) {
            if (param.getIsFunction() == 0) {
                continue;
            }

            refreshParams(param.getParams());

            param.setText(param.getFunctionName() + "(" +
                    param.getParams().stream().map(Param::getText).collect(Collectors.joining(",")) + ")");
        }
    }

    /**
     * 将表达式解析为表达式树
     *
     * @param exprStr
     * @return
     * @throws CombinedQueryParseException
     */
    public List<Expr> parseExprs(String exprStr) throws CombinedQueryParseException {
        Node root = parseGramTree(exprStr, null);

        List<Expr> exprs = new ArrayList<>();

        GramTreeToExprs(root, exprs, null);

        return exprs;
    }

    /**
     * 将语法树解析为表达式树
     *
     * @param root
     * @param exprs
     * @param logicalOperator
     */
    private void GramTreeToExprs(Node root, List<Expr> exprs, String logicalOperator) {
        if (root.isDefault()) {
            //忽略默认节点1=1
            return;
        }
        if (logicalOperator == null) {
            logicalOperator = LogicalOperator.AND.getName();
        }

        if (root instanceof LogicalOperatorNode) {
            //逻辑表达式节点，递归处理子节点
            List<Node> subNodes = root.getSubNodes();
            GramTreeToExprs(subNodes.get(0), exprs, logicalOperator);
            GramTreeToExprs(subNodes.get(1), exprs, ((LogicalOperatorNode) root).getLogicalOperator().getName());
        } else if (root instanceof BracketsNode) {
            //添加括号表达式
            Expr expr = new Expr();
            List<Expr> subExprs = new ArrayList<>();
            expr.setId(IdUtil.uuid());
            expr.setBracketsNode(true);
            expr.setSubExprs(subExprs);
            expr.setLogicalOperator(logicalOperator);
            exprs.add(expr);

            //递归处理子节点
            GramTreeToExprs(root.getSubNodes().get(0), subExprs, null);
        } else if (root instanceof OperatorNode) {
            Expr expr = new Expr();
            expr.setText(root.getExpr());
            expr.setId(IdUtil.uuid());

            List<Param> params = new ArrayList<>();
            for (Node subNode : root.getSubNodes()) {
                params.add(nodeToParam(subNode));
            }
            expr.setParams(params);
            expr.setLogicalOperator(logicalOperator);
            expr.setOperator(((OperatorNode) root).getProcessor().getOperator().getName());

            exprs.add(expr);
        }
    }

    /**
     * 运算符的参数节点解析为表达式树的节点参数
     *
     * @param node
     * @return
     */
    private Param nodeToParam(Node node) {
        Param param = new Param(IdUtil.uuid(), node.getExpr());
        param.setReturnType(node.getReturnType());

        if (node instanceof FunctionNode) {
            param.setType(Param.Type.FUNCTION.getName());
            param.setIsFunction(1);
            param.setFunctionName(((FunctionNode) node).getProcessor().getFunction().getName());

            List<Param> params = new ArrayList<>();
            for (Node subNode : node.getSubNodes()) {
                params.add(nodeToParam(subNode));
            }
            param.setParams(params);
        } else if (node instanceof FieldNode) {
            param.setType(Param.Type.FIELD.getName());
            param.setTableId(((FieldNode) node).getTableId());
            param.setFieldId(((FieldNode) node).getFieldId());
        } else if (node instanceof ValueNode) {
            if (node.getReturnType() == Type.CODE.getCode()) {
                param.setType(Param.Type.CODE.getName());
                param.setCodeSetName(((ValueNode) node).getCodeSetName());
                param.setFieldId(String.join(",", ((ValueNode) node).getValues()));
            } else {
                param.setType(Param.Type.VALUE.getName());
            }

            //判断值节点是否是多选的
            Node parent = node.getParent();
            if (parent instanceof OperatorNode) {
                param.setMultiselect(((OperatorNode) parent).getProcessor().getOperator().isArray());
            }

        }

        return param;
    }

    /**
     * 将表达式解析为sql
     *
     * @return result[0]:查询的where条件，result[1]:查询涉及的表名
     */
    public Object[] parseSql(String exprStr) throws CombinedQueryParseException {
        Node root = parseGramTree(exprStr, null);

        //调用码值处理器
        invokePostProcessorBoforeGetSql(root);

        //获取表达式所需的表名，并排序
        Set<String> tableNames = new HashSet<>();
        getTableNames(root, tableNames);

        Object[] result = new Object[2];
        result[0] = root.getSql();
        result[1] = tableNames;

        return result;
    }

    /**
     * invokePostProcessorBoforeGetSql
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    private void invokePostProcessorBoforeGetSql(Node node) throws CombinedQueryParseException {
        for (PostProcessor postProcessor : postProcessors) {
            if (postProcessor.support(node)) {
                postProcessor.postProcessorBeforeGetSql(node);
            }
        }

        for (Node subNode : node.getSubNodes()) {
            invokePostProcessorBoforeGetSql(subNode);
        }
    }

    /**
     * 获取表达式所需的所有表名
     *
     * @param node
     * @param tables
     */
    private void getTableNames(Node node, Set<String> tables) {
        if (node instanceof FieldNode) {
            tables.add(((FieldNode) node).getTableName());
        }

        for (Node subNode : node.getSubNodes()) {
            getTableNames(subNode, tables);
        }

    }


    /**
     * 编译表达式
     *
     * @param exprStr
     * @return
     */
    public String compile(String exprStr) {
        try {
            parseSql(exprStr);
        } catch (CombinedQueryParseException e) {
            return e.getMessage();
        }
        return null;
    }


    /**
     * 表达式解析为语法树
     *
     * @param exprStr
     * @param parent  父节点为null是根节点
     * @return
     * @throws CombinedQueryParseException
     */
    private Node parseGramTree(String exprStr, Node parent) throws CombinedQueryParseException {
        if (StringUtils.isEmpty(exprStr) && parent == null) {
            //如果表达式为空，返回默认的1=1
            return operatorNodeProcessor.getDefaultNode();
        }

        Deque<Node> stack = new LinkedList<>();
        ExprStream stream = new ExprStream(exprStr);

        String expr;
        Node node;
        NodeProcessor processor;
        while (stream.hasNext()) {
            expr = getExpr(stream);
            if (expr == null) {
                break;
            }

            //获取节点处理器
            processor = getNodeProcessor(expr);
            //将表达式解析为节点
            node = processor.parse(expr);

            //节点入栈
            //先入栈后处理子节点是为了保证子节点的添加顺序
            pushNode(stack, node);

            //处理子节点
            if (node.getSubNodeExpr().size() > 0) {
                for (String subNodeExpr : node.getSubNodeExpr()) {
                    node.addSubNode(parseGramTree(subNodeExpr, node));
                }
                if (stack.peek().equals(node)) {
                    node = stack.pop();
                    pushNode(stack, node);
                }
            }
        }

        if (parent == null && stack.size() == 0) {
            return operatorNodeProcessor.getDefaultNode();
        }


        if (stack.size() > 1) {
            //如果处理结束后堆栈中的节点数大于1，说明解析出错
            throw new CombinedQueryParseException("非法表达式!");
        }


        node = stack.pop();
        node.setParent(parent);
        if (parent == null) {
            if (!(node instanceof BracketsNode || node instanceof LogicalOperatorNode || node instanceof OperatorNode)) {
                //根节点必须是括号节点、逻辑运算符节点、运算符节点之一
                throw new CombinedQueryParseException("非法表达式!");
            }

            //语法树节点校验
            checkNode(node);
        }

        return node;
    }

    private void pushNode(Deque<Node> stack, Node node) throws CombinedQueryParseException {
        while (node != null) {
            NodeProcessor processor = getNodeProcessor(node);
            node = processor.pushNode(stack, node);
        }
    }

    /**
     * 节点检验
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    private void checkNode(Node node) throws CombinedQueryParseException {
        NodeProcessor processor = getNodeProcessor(node);
        processor.checkNode(node);

        //校验子节点
        for (Node subNode : node.getSubNodes()) {
            checkNode(subNode);
        }
    }

    /**
     * 获取节点处理器
     */
    private NodeProcessor getNodeProcessor(String expr) throws CombinedQueryParseException {
        for (NodeProcessor nodeProcessor : nodeProcessors) {
            if (nodeProcessor.support(expr)) {
                return nodeProcessor;
            }
        }
        throw new CombinedQueryParseException("非法表达式: " + expr);
    }

    /**
     * 获取节点处理器
     */
    private NodeProcessor getNodeProcessor(Node node) throws CombinedQueryParseException {
        for (NodeProcessor nodeProcessor : nodeProcessors) {
            if (nodeProcessor.support(node)) {
                return nodeProcessor;
            }
        }
        throw new CombinedQueryParseException("no nodeProcessor for node: " + node.getClass());
    }

    /**
     * 获取节点对应的表达式
     */
    private String getExpr(ExprStream stream) throws CombinedQueryParseException {
        //获取第一个非空格字符
        char first = stream.next();
        while (first == ' ') {
            if (stream.hasNext()) {
                first = stream.next();
            } else {
                return null;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(first);

        switch (first) {
            case '(':
            case ')':
                break;
            case '\'':
                //value
                sb.append(stream.getUtilNextSingleQuotes());
                break;
            case '[':
                //数组格式的value
                sb.append(stream.getUtilRightBrackets('[', ']'));
                break;
            case '介':
                //处理介于..和..之间
                while (stream.hasNext()) {
                    char c = stream.next();
                    if (c == ' ') {
                        //取到空格为止
                        break;
                    }
                    sb.append(c);

                    if (c == '\'') {
                        //处理值
                        sb.append(stream.getUtilNextSingleQuotes());
                    }

                    if (c == '(') {
                        //处理函数
                        sb.append(stream.getUtilRightBrackets('(', ')'));
                    }
                }
                break;
            default:
                while (stream.hasNext()) {
                    char c = stream.next();
                    if (c == ' ') {
                        //默认取到空格为止
                        break;
                    }
                    sb.append(c);

                    if (c == '[') {
                        //处理运算符在[...]之中，取到与之对应的右括号为止
                        sb.append(stream.getUtilRightBrackets('[', ']'));
                    }

                    if (c == '(') {
                        //函数格式，取到与之对应的右括号为止
                        sb.append(stream.getUtilRightBrackets('(', ')'));
                        break;
                    }
                }
        }

        return sb.toString();
    }

}
