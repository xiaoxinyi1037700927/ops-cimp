package com.sinosoft.ops.cimp.util.combinedQuery.processors.functions;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import org.springframework.stereotype.Component;

@Component
public class DivideProcessor extends FunctionProcessor {

    public DivideProcessor() {
        super(Function.DIVIDE);
    }

}
