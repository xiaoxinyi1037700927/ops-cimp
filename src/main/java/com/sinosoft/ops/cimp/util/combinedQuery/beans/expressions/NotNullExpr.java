package com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions;

import org.springframework.stereotype.Component;

@Component
public class NotNullExpr implements Expression {
    public static final String NAME = "不为空";
    public static final int CODE = 1;
    public static final String SQL = " %s IS NOT NULL ";


    @Override
    public boolean supports(String name) {
        return NAME.equalsIgnoreCase(name);
    }
}
