package com.sinosoft.ops.cimp.export.processor;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class FamilyPartyValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {

    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        String attrValue = StringUtil.obj2Str(attrAndValue.values().toArray()[0]);

        StringBuilder sb = new StringBuilder(attrValue);

        for (int i = sb.length(); i > 1; --i) {
            if (i % 2 == 0) {
                sb.insert(i, "\n");
            }
        }
        attrValue = sb.toString();

        Cell cell = super.getAttrIndexCell(document, attrName);
        if (cell != null) {
            insertTextIntoCell(cell, attrValue, true, false, null);
        }
    }

}
