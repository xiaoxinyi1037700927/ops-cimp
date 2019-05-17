package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import org.springframework.stereotype.Component;

@Component
public class NqProcessor extends OperatorProcessor {

    public NqProcessor() {
        super(Operator.NQ);
    }

}
