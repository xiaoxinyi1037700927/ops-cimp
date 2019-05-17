package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContainsProcessor extends OperatorProcessor {
    public ContainsProcessor() {
        super(Operator.CONTAINS);
    }


    @Override
    public String getSql(List<Node> subNodes) {
        //去掉值节点sql中默认包含的''
        String value = subNodes.get(1).getSql();
        value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"));

        return String.format(Operator.CONTAINS.getSql(), subNodes.get(0).getSql(), value);
    }
}
