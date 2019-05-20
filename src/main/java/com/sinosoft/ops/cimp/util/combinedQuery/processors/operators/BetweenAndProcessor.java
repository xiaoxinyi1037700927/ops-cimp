package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BetweenAndProcessor extends OperatorProcessor {
    private static final Pattern pattern = Pattern.compile(Operator.BETWEEN_AND.getRegex());

    public BetweenAndProcessor() {
        super(Operator.BETWEEN_AND);
    }


    @Override
    public boolean support(String expr) {
        Matcher matcher = pattern.matcher(expr);
        return matcher.matches();
    }

    @Override
    public void parse(Node node, String expr) {
        Matcher matcher = pattern.matcher(expr);
        if (matcher.matches()) {
            node.addSubNodeExpr(matcher.group(1));
            node.addSubNodeExpr(matcher.group(2));
        }
    }

}
