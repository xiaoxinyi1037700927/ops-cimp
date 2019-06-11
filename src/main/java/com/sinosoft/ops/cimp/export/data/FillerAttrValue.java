package com.sinosoft.ops.cimp.export.data;

import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FillerAttrValue implements AttrValue {

    public static final int ORDER = 30;

    public static final String KEY = "filler";

    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId) throws Exception {
        return "";
    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
