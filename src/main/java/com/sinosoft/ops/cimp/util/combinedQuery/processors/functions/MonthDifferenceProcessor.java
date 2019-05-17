package com.sinosoft.ops.cimp.util.combinedQuery.processors.functions;

import com.sinosoft.ops.cimp.util.combinedQuery.enums.Function;
import org.springframework.stereotype.Component;

@Component
public class MonthDifferenceProcessor extends FunctionProcessor {

    public MonthDifferenceProcessor() {
        super(Function.MONTH_DIFFERENCE);
    }

}
