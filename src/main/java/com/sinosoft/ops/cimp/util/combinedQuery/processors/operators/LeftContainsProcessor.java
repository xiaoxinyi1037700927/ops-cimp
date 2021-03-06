package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeftContainsProcessor extends OperatorProcessor {
    public LeftContainsProcessor() {
        super(Operator.LEFT_CONTAINS);
    }


    @Override
    public String getSql(List<Node> subNodes) {
        String value = subNodes.get(1).getSql();


        //如果值节点返回类型是string，去掉sql中默认包含的''
        if (Type.NUMBER.getCode() != subNodes.get(1).getReturnType()) {
            value = value.substring(value.indexOf("'") + 1, value.lastIndexOf("'"));
        }

        return String.format(Operator.LEFT_CONTAINS.getSqlFormat(), subNodes.get(0).getSql(), value);
    }
}
