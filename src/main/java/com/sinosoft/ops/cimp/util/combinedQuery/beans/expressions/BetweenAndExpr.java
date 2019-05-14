package com.sinosoft.ops.cimp.util.combinedQuery.beans.expressions;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BetweenAndExpr implements Expression {
    public static final String NAME = "介于...和...之间";
    public static final int CODE = 1;
    public static final String SQL = " %s BETWEEN %s AND %s ";
    private static final Pattern pattern = Pattern.compile("介于(.+?)和(.+?)之间");


    @Override
    public boolean supports(String name) {
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}
