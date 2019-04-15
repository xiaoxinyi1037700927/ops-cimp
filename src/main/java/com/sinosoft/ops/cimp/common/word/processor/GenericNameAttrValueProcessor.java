package com.sinosoft.ops.cimp.common.word.processor;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/16.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class GenericNameAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {

    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        //该方法为遵循一般name属性值处理的处理器
        //当属性值小于等于4个汉字时，上下左右居中
        //当属性值大于4个汉字小于等于8个汉字时，居左 分为两行 每行4个字 上下居中
        //当属性值大于8个汉字小于等于10个汉字时，居左 分为两行 每行5个字 上下居中
        //当属性值大于10个字小于等于15个字时，居左 分为三行 每行5个字 上下居中
        //超过15个汉字直接输出
        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        String attrValue = StringUtil.obj2Str(attrAndValue.values().toArray()[0]);
        Cell cell = super.getAttrIndexCell(document, attrName);
        if (cell != null) {


            int length = attrValue.length();
            if (length <= 4) {
                insertTextIntoCell(cell, attrValue, true, false, null);
            } else if (length > 4 && length <= 8) {
                String firstLine = attrValue.substring(0, 4);
                String secondLine = attrValue.substring(4, length);
                attrValue = firstLine + "\n" + secondLine;
                Map<String, Object> config = new HashMap<String, Object>(1);
                config.put("fontSize", 10.5);
                config.put("fontPos", "CENTER");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else if (length > 8 && length <= 10) {
                String firstLine = attrValue.substring(0, 5);
                String secondLine = attrValue.substring(5, length);
                attrValue = firstLine + "\n" + secondLine;
                Map<String, Object> config = new HashMap<String, Object>(2);
                config.put("fontSize", 10.5);
                config.put("fontPos", "CENTER");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else if (length > 10 && length <= 15) {
                String firstLine = attrValue.substring(0, 5);
                String secondLine = attrValue.substring(5, 10);
                String thirdLine = attrValue.substring(10, length);
                attrValue = firstLine + "\n" + secondLine + "\n" + thirdLine;
                Map<String, Object> config = new HashMap<String, Object>(2);
                config.put("fontSize", 7.5);
                config.put("fontPos", "CENTER");
                insertTextIntoCell(cell, attrValue, true, false, config);
            } else {
                String firstLine = attrValue.substring(0, 6);
                String secondLine = attrValue.substring(6, 12);
                String thirdLine = attrValue.substring(12, length);
                attrValue = firstLine + "\n" + secondLine + "\n" + thirdLine;
                Map<String, Object> config = new HashMap<String, Object>(2);
                config.put("fontSize", 7.5);
                config.put("fontPos", "CENTER");
                insertTextIntoCell(cell, attrValue, true, false, config);
            }
        }
    }
}
