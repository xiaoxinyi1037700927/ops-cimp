package com.sinosoft.ops.cimp.util.combinedQuery.beans;

public class ExprStream {
    private String expression;

    private char[] exprStream;

    private int total;

    private int index;

    public ExprStream(String expression) {
        this.expression = expression;
        this.exprStream = expression.toCharArray();
        this.total = exprStream.length;
        this.index = 0;
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

}
