package com.sinosoft.ops.cimp.export.processor;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/17.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class ApprovalOptionsAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {

    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        String attrValue = StringUtil.obj2Str(attrAndValue.values().toArray()[0]);
        Cell cell = super.getAttrIndexCell(document, attrName);
        if (cell != null) {
            int len = attrValue.length();
            Map<String, Object> config = new HashMap<>();
            config.put("fontPos", "LEFT");
            insertTextIntoCell(cell, attrValue, true, false, config);
        }
    }
}
