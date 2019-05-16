package com.sinosoft.ops.cimp.util.combinedQuery.processors.functions;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import org.springframework.stereotype.Component;

@Component
public class ToDateProcessor extends FunctionProcessor {

    public ToDateProcessor() {
        super(Function.TO_DATE);
    }

}
