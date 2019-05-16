package com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions;

import org.springframework.stereotype.Component;

@Component
public class EqExpr implements Expression {
    public static final String NAME = "等于";
    public static final int CODE = 1;
    public static final String SQL = " %s = %s ";

    @Override
    public boolean supports(String name) {
        return NAME.equalsIgnoreCase(name);
    }
}