package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.context.ExecuteContext;

import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class ProposedPostAttrValue implements AttrValue {

    public static final int ORDER = 29;

    public static final String KEY = "proposedPost";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        try {
            Object var = ExecuteContext.getVariable(KEY);
            return var != null ? var : "";
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
