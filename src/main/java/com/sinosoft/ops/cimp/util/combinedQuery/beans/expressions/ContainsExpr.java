package com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions;

import org.springframework.stereotype.Component;


@Component
public class ContainsExpr implements Expression {
    public static final String NAME = "包含";
    public static final int CODE = 1;
    public static final String SQL = " %s LIKE '%s' ";

    @Override
    public boolean supports(String name) {
        return NAME.equalsIgnoreCase(name);
    }

}
