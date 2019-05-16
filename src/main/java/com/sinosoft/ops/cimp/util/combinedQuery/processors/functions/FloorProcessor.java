package com.sinosoft.ops.cimp.util.combinedQuery.processors.functions;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import org.springframework.stereotype.Component;

@Component
public class FloorProcessor extends FunctionProcessor {

    public FloorProcessor() {
        super(Function.FLOOR);
    }

}
