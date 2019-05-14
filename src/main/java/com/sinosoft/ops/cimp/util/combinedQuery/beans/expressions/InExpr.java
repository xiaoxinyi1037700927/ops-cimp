package com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InExpr implements Expression {
    public static final String NAME = "在...之中";
    public static final int CODE = 1;
    public static final String SQL = " %s IN (%s) ";
    private static final Pattern pattern = Pattern.compile("在(.+?)之中");


    @Override
    public boolean supports(String name) {
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
