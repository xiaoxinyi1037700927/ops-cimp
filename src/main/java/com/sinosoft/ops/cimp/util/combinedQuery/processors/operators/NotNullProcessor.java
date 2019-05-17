package com.sinosoft.ops.cimp.util.combinedQuery.processors.operators;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Operator;
import org.springframework.stereotype.Component;

@Component
public class NotNullProcessor extends OperatorProcessor {

    public NotNullProcessor() {
        super(Operator.NOT_NULL);
    }
}
