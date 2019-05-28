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
import com.sinosoft.ops.cimp.util.combinedQuery.processors.code.CodeProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.NodeProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.nodes.OperatorNodeProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CombinedQueryParser {

    private final NodeProcessor[] nodeProcessors;
    private final CodeProcessor codeProcessor;
    private final OperatorNodeProcessor operatorNodeProcessor;

    @Autowired
    public CombinedQueryParser(NodeProcessor[] nodeProcessors, CodeProcessor codeProcessor, OperatorNodeProcessor operatorNodeProcessor) {
        this.nodeProcessors = nodeProcessors;
        this.codeProcessor = codeProcessor;
        this.operatorNodeProcessor = operatorNodeProcessor;
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

        Operator op = Operator.getByName(expr.getOperator());
        if (op != null) {
            //如果是已定义的运算符，使用运算符定义的格式
            expr.setText(opStr + op.getExpr(params.stream().map(Param::getText).collect(Collectors.toList())));
        } else {
            //使用默认格式
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
        } else if (node instanceof ValueNode && node.getReturnType() == Type.CODE.getCode()) {
            param.setType(Param.Type.CODE.getName());
            param.setCodeSetName(((ValueNode) node).getCodeSetName());
        } else {
            param.setType(Param.Type.VALUE.getName());
        }

        return param;
    }

    /**
     * 将表达式解析为sql
     */
    public String parseSql(String exprStr) throws CombinedQueryParseException {
        Node root = parseGramTree(exprStr, null);

        //调用码值处理器
        invokeCodeProcessors(root);

        //获取表达式所需的表名，并排序
        Set<String> set = new HashSet<>();
        getTableNames(root, set);
        List<String> tables = new ArrayList<>(set);
        tables.sort(String::compareTo);

        if (tables.size() == 0) {
            return "AND" + root.getSql();
        }


        String tmp = tables.get(0);
        StringBuilder sql = new StringBuilder();
        sql.append(" AND A001.EMP_ID IN(SELECT DISTINCT ")
                .append(tmp)
                .append(".EMP_ID FROM ")
                .append(tmp);
        for (int i = 1; i < tables.size(); i++) {
            String tmp2 = tables.get(i);
            sql.append(" INNER JOIN ").append(tmp2).append(" ON ")
                    .append(tmp).append(".EMP_ID = ").append(tmp2).append(".EMP_ID ");
        }
        sql.append(" WHERE ").append(root.getSql()).append(" )");

        return sql.toString();
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
     * 调用码值处理器,生成sql前调用
     *
     * @param root
     */
    private void invokeCodeProcessors(Node root) throws CombinedQueryParseException {
        if (root instanceof OperatorNode) {
            codeProcessor.processCode((OperatorNode) root);
            return;
        }

        for (Node subNode : root.getSubNodes()) {
            invokeCodeProcessors(subNode);
        }
    }

    /**
     * 编译表达式
     *
     * @param exprStr
     * @return
     */
    public boolean compile(String exprStr) {
        try {
            parseGramTree(exprStr, null);
            return true;
        } catch (CombinedQueryParseException e) {
//            e.printStackTrace();
        }
        return false;
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
            Node next = node;
            do {
                next = processor.pushNode(stack, next);
                if (next != null) {
                    processor = getNodeProcessor(next);
                }
            } while (next != null);


            //处理子节点
            if (node.getSubNodeExpr().size() > 0) {
                for (String subNodeExpr : node.getSubNodeExpr()) {
                    node.addSubNode(parseGramTree(subNodeExpr, node));
                }
                if (stack.peek().equals(node)) {
                    node = stack.pop();
                    getNodeProcessor(node).pushNode(stack, node);
                }
            }
        }

        if (stack.size() != 1) {
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
                while (stream.hasNext()) {
                    char c = stream.next();
                    sb.append(c);
                    if (c == '\'') {
                        break;
                    }
                }
                break;
            case '[':
                //数组格式的value
                sb.append(stream.getUtilRightBrackets('[', ']'));
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
