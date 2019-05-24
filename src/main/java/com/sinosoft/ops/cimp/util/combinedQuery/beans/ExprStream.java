package com.sinosoft.ops.cimp.util.combinedQuery.beans;

public class ExprStream {
    private String expression;

    private char[] exprStream;

    private int total;

    private int index;

    public ExprStream(String expression) {
        this.expression = expression.replaceAll("\n", "");
        this.exprStream = this.expression.toCharArray();
        this.total = exprStream.length;
        this.index = 0;
    }

    public String getExpression() {
        return expression;
    }

    public char next() {
        return exprStream[index++];
    }

    public boolean hasNext() {
        return index < total;
    }

    public void back() {
        index--;
    }

    public String remaining() {
        return expression.substring(index, total);
    }

    /**
     * 获取到左括号对应的右括号为止的字符串
     *
     * @param begin (/[/{
     * @param end   )/]/}
     * @return
     * @throws CombinedQueryParseException
     */
    public String getUtilRightBrackets(char begin, char end) throws CombinedQueryParseException {
        StringBuilder sb = new StringBuilder();

        char c;
        int n = 0;
        while (true) {
            if (!hasNext()) {
                //如果没找到对应的右括号，抛出异常
                throw new CombinedQueryParseException("缺失的 " + end + " ：" + sb.toString());
            }

            c = next();
            sb.append(c);
            if (c == begin) {
                //内嵌的括号
                n++;
            } else if (c == end && n-- == 0) {
                //最外层括号
                break;
            }
        }
        return sb.toString();
    }

}
