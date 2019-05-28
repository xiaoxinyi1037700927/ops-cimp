package com.sinosoft.ops.cimp.util.combinedQuery.processors.post;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FunctionNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.functions.FunctionProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.utils.CombinedQueryUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DateStringProcessor implements PostProcessor {

    private final FunctionProcessor functionProcessor;

    public DateStringProcessor() {
        this.functionProcessor = new FunctionProcessor(Function.TO_DATE);
    }

    /**
     * 处理器是否支持节点
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof ValueNode;
    }

    /**
     * 处理日期字符串,在外层添加to_date函数
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    @Override
    public void postProcessorBeforeGetSql(Node node) throws CombinedQueryParseException {
        ValueNode vNode = (ValueNode) node;

        if (vNode.getReturnType() == Type.DATE.getCode()) {
            List<Node> subNodes = node.getParent().getSubNodes();
            int index = subNodes.indexOf(node);
            subNodes.remove(index);

            FunctionNode fNode = new FunctionNode(functionProcessor);
            fNode.addSubNode(vNode);
            fNode.addSubNode(new ValueNode(Collections.singletonList(CombinedQueryUtil.DEFAULT_DATE_FORMAT), Collections.emptyList(), false, false, Type.STRING.getCode()));

            subNodes.add(index, fNode);
        }

    }
}
