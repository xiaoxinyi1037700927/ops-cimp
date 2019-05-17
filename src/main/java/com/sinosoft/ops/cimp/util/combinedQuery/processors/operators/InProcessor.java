package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InProcessor extends OperatorProcessor {
    public static final String IDENTIFIER = "$IN_OPERATOR_VALUE";
    private static final Pattern pattern = Pattern.compile(Operator.IN.getName());


    public InProcessor() {
        super(Operator.IN);
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
            //添加IN操作标识符,在valueNodeProcessor中处理
            node.addSubNodeExpr("'" + IDENTIFIER + matcher.group(1).replaceAll("'", "\"") + "'");
        }
    }
}
