package com.sinosoft.ops.cimp.export.processor;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

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
public class TechnicPositionAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {

    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        String attrValue = StringUtil.obj2Str(attrAndValue.values().toArray()[0]);
        Cell cell = super.getAttrIndexCell(document, attrName);

        if (cell != null) {
            int length = attrValue.length();
            if (StringUtils.isEmpty(attrValue)) {
                insertTextIntoCell(cell, "", true, false, null);
            }
            if (length <= 6) {
                insertTextIntoCell(cell, attrValue, true, false, null);
            } else if (length > 6 && length <= 8) {
                Map<String, Object> config = new HashMap<String, Object>(1);
                config.put("fontPos", "LEFT");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else if (length > 8 && length <= 16) {
                String firstLine = attrValue.substring(0, 8);
                String secondLine = attrValue.substring(8, length);
                attrValue = firstLine + "\n" + secondLine;
                Map<String, Object> config = new HashMap<String, Object>(1);
                config.put("fontPos", "LEFT");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else if (length > 16 && length <= 24) {
                String firstLine = attrValue.substring(0, 12);
                String secondLine = attrValue.substring(12, length);
                attrValue = firstLine + "\n" + secondLine;
                Map<String, Object> config = new HashMap<String, Object>(2);
                config.put("fontSize", 10.5);
                config.put("fontPos", "LEFT");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else if (length > 24 && length <= 30) {
                String firstLine = attrValue.substring(0, 17);
                String secondLine = attrValue.substring(17, length);
                attrValue = firstLine + "\n" + secondLine;
                Map<String, Object> config = new HashMap<String, Object>(2);
                config.put("fontSize", 7.5);
                config.put("fontPos", "LEFT");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else {
                throw new RuntimeException("专长属性最多为30个汉字，输入的汉字长度为：" + length);
            }
        }
    }
}
