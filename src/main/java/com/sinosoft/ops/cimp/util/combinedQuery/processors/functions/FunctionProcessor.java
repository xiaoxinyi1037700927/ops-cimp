package com.sinosoft.ops.cimp.util.combinedQuery.processors.functions;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.ExprStream;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FunctionProcessor {
    private Function function;
    private Pattern pattern;

    public FunctionProcessor(Function function) {
        this.function = function;
        pattern = Pattern.compile(function.getRegex());
    }

    /**
     * 判断表达式是否是该函数
     *
     * @param expr
     * @return
     */
    public boolean support(String expr) {
        Matcher matcher = pattern.matcher(expr);
        return matcher.matches();
    }

    public void parse(Node node, String expr) throws CombinedQueryParseException {
        Matcher matcher = pattern.matcher(expr);
        if (!matcher.matches()) {
            throw new CombinedQueryParseException("非法的函数表达式：" + expr);
        }

        List<String> params = getParams(matcher.group(1));

        if (function.getParamsNum() != params.size()) {
            throw new CombinedQueryParseException("参数数量不匹配：" + expr);
        }

        node.addSubNodeExpr(params);
    }


    /**
     * 获取表达式对应的sql文本
     *
     * @param subNodes
     * @return
     */
    public String getSql(List<Node> subNodes) {
        Object[] subSql = new Object[function.getParamsNum()];
        for (int i = 0; i < subNodes.size(); ++i) {
            subSql[i] = subNodes.get(i).getSql();
        }
        return String.format(function.getSqlFormat(), subSql);
    }


    /**
     * 获取表达式对应的表达式
     *
     * @param subNodes
     * @return
     */
    public String getExpr(List<Node> subNodes) {
        Object[] subExpr = new Object[function.getParamsNum()];
        for (int i = 0; i < subNodes.size(); ++i) {
            subExpr[i] = subNodes.get(i).getExpr();
        }
        return String.format(function.getExprFormat(), subExpr);
    }

    /**
     * 解析表达式，获取函数的参数列表
     *
     * @param expr
     * @return
     */
    protected List<String> getParams(String expr) throws CombinedQueryParseException {
        List<String> params = new ArrayList<>();
        if (StringUtils.isBlank(expr)) {
            return params;
        }

        ExprStream stream = new ExprStream(expr);
        StringBuilder sb = new StringBuilder();
        char c;
        while (stream.hasNext()) {
            c = stream.next();

            if (c == ',') {
                String param = sb.toString().trim();
                if (StringUtils.isEmpty(param)) {
                    //逗号前没有参数
                    throw new CombinedQueryParseException("非法的函数表达式 ：" + expr);
                }
                params.add(param);
                sb.delete(0, sb.length());
                continue;
            }
            sb.append(c);

            if (c == '(') {
                //如果遇到左括号，则直接取到对应的由括号
                sb.append(stream.getUtilRightBrackets('(', ')'));
            } else if (c == '\'') {
                //如果遇到单引号，则直接取到下一个单引号
                while (true) {
                    if (!stream.hasNext()) {
                        //如果没找到下一个单引号，抛出异常
                        throw new CombinedQueryParseException("非法的函数表达式 ：" + expr);
                    }

                    c = stream.next();
                    sb.append(c);
                    if (c == '\'') {
                        break;
                    }
                }
            }
        }

        String param = sb.toString().trim();
        if (StringUtils.isEmpty(param) && params.size() > 0) {
            //参数以逗号结尾
            throw new CombinedQueryParseException("非法的函数表达式 ：" + expr);
        } else if (StringUtils.isNotEmpty(param)) {
            params.add(param);
        }

        return params;
    }

    public Function getFunction() {
        return function;
    }
}
