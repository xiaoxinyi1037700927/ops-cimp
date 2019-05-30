package com.sinosoft.ops.cimp.util.combinedQuery.processors.post;

import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.codeSet.CodeSet;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.FieldNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.Node;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.OperatorNode;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.nodes.ValueNode;
import com.sinosoft.ops.cimp.util.combinedQuery.enums.Type;
import com.sinosoft.ops.cimp.util.combinedQuery.processors.operators.InProcessor;
import com.sinosoft.ops.cimp.util.combinedQuery.utils.CodeUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CodeProcessor implements PostProcessor {

    private final InProcessor inProcessor;
    private final CodeUtil codeUtil;


    public CodeProcessor(InProcessor inProcessor, CodeUtil codeUtil) {
        this.inProcessor = inProcessor;
        this.codeUtil = codeUtil;
    }

    /**
     * 处理器是否支持节点
     *
     * @param node
     * @return
     */
    @Override
    public boolean support(Node node) {
        return node instanceof OperatorNode;
    }

    /**
     * 码值转换，获取sql之前调用
     * 例如：等于 '[2]大学本科' -> 在['2','21','22','29']之中
     *
     * @param node
     * @throws CombinedQueryParseException
     */
    @Override
    public void postProcessorBeforeGetSql(Node node) throws CombinedQueryParseException {
        OperatorNode oNode = (OperatorNode) node;
        List<Node> subNodes = node.getSubNodes();

        Node first = subNodes.get(0);
        if (subNodes.size() == 1 || !(first instanceof FieldNode) || first.getReturnType() != Type.CODE.getCode()) {
            return;
        }

        //初始化代码集数据
        String codeSetName = ((FieldNode) first).getCodeSetName();
        List<SysCodeItem> sysCodeItem = codeUtil.getSysCodeItems(((FieldNode) first).getCodeSetId());
        CodeSet codeSet = new CodeSet(codeSetName, sysCodeItem);

        List<String> codes = new ArrayList<>();
        String code = ((ValueNode) subNodes.get(1)).getValues().get(0);
        try {
            switch (oNode.getProcessor().getOperator()) {
                case EQ:
                    codeSet.getCodesByEq(code, codeSet.getItems(), codes);
                    break;
                case NQ:
                    codeSet.getCodesByNq(code, codes);
                    break;
                case GT:
                    codeSet.getCodesByGt(code, codeSet.getItems(), codes);
                    break;
                case GE:
                    codeSet.getCodesByGe(code, codes);
                    break;
                case LT:
                    codeSet.getCodesByLt(code, codes);
                    break;
                case LE:
                    codeSet.getCodesByLe(code, codes);
                    break;
                case BETWEEN_AND:
                    codeSet.getCodesByBetweenAnd(code, ((ValueNode) subNodes.get(2)).getValues().get(0), codes);
                    break;
                case IN:
                    codeSet.getCodesByIn((((ValueNode) subNodes.get(1)).getValues()), codes);
                    break;
                case NOT_IN:
                    codeSet.getCodesByNotIn((((ValueNode) subNodes.get(1)).getValues()), codes);
                    break;
                default:
                    return;
            }

            if (codes.size() == 0) {
                codes.add("-1");
            }

            //将操作符替换为in
            oNode.setProcessor(inProcessor);
            oNode.getSubNodes().clear();
            oNode.getSubNodes().add(first);
            ValueNode valueNode = new ValueNode(codes, null, true, true, Type.CODE.getCode());
            oNode.getSubNodes().add(valueNode);

        } catch (Exception e) {
            throw new CombinedQueryParseException("码值转换失败！");
        }
    }
}
