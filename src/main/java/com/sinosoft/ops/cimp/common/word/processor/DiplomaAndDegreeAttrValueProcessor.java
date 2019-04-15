package com.sinosoft.ops.cimp.common.word.processor;

import com.aspose.words.Cell;
import com.aspose.words.Document;

import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/17.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class DiplomaAndDegreeAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {
    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        List attrValue = (List) attrAndValue.values().toArray()[0];
        Cell cell = super.getAttrIndexCell(document, attrName);

        if (cell != null) {
            if (attrValue != null) {
                if (attrValue.size() == 1) {
                    String value = StringUtil.obj2Str(attrValue.get(0));
                    insertTextIntoCell(cell, value, true, false, null);
                } else if (attrValue.size() > 1) {
                    String school = StringUtil.obj2Str(attrValue.get(0));
                    String degree = StringUtil.obj2Str(attrValue.get(1));
                    if (StringUtils.isNotEmpty(school)) {
                        insertTextIntoCell(cell, school, true, false, null);
                    }
                    if (StringUtils.isNotEmpty(degree)) {
                        insertTextIntoCell(cell, degree, false, true, null);
                    }
                }
            }
        }
    }
}
