package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import org.springframework.stereotype.Component;

@Component
public class LeProcessor extends OperatorProcessor {

    public LeProcessor() {
        super(Operator.LE);
    }
}
