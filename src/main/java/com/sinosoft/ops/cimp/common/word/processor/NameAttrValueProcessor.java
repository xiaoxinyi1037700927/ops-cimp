package com.sinosoft.ops.cimp.common.word.processor;

import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.sinosoft.ops.cimp.util.StringUtil;

import java.util.*;

/**
 * Created by rain chen on 2017/9/27.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class NameAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {

    @Override
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {

        String attrName = attrAndValue.keySet().toArray(new String[]{})[0];
        String attrValue = StringUtil.obj2Str(attrAndValue.values().toArray()[0]);
        Cell cell = super.getAttrIndexCell(document, attrName);

        if (cell != null) {
            attrValue = attrValue.replaceAll(" ", "");
            int nameLength = attrValue.length();
            boolean containsDot = attrValue.contains("•");  //名称中是否有“•”若存在单独处理
            if (!containsDot) {     //不包含点
                if (nameLength == 2) {
                    //单名的中间空一个汉字的位置
                    //单名的数据库中存储的为中间两个空格，所以长度也为4，必须去掉空格再计算
                    String attr_value = StringUtil.obj2Str(attrAndValue.values().toArray()[0]);
                    insertTextIntoCell(cell, attr_value, true, false, null);
                } else if (nameLength > 2 && nameLength <= 4) {
                    insertTextIntoCell(cell, attrValue, true, false, null);
                } else if (nameLength > 4 && nameLength <= 8) {
                    String firstLine = attrValue.substring(0, 4);
                    String secondLine = attrValue.substring(4, nameLength);
                    attrValue = firstLine + "\n" + secondLine;
                    Map<String, Object> config = new HashMap<String, Object>(1);
                    config.put("fontPos", "LEFT");
                    insertTextIntoCell(cell, attrValue, true, false, config);
                } else if (nameLength > 8 && nameLength <= 10) {
                    String firstLine = attrValue.substring(0, 5);
                    String secondLine = attrValue.substring(5, nameLength);
                    attrValue = firstLine + "\n" + secondLine;
                    Map<String, Object> config = new HashMap<String, Object>(2);
                    config.put("fontSize", 10.5);
                    config.put("fontPos", "LEFT");
                    insertTextIntoCell(cell, attrValue, true, false, config);
                } else if (nameLength > 10 && nameLength <= 15) {
                    String firstLine = attrValue.substring(0, 5);
                    String secondLine = attrValue.substring(5, 10);
                    String thirdLine = attrValue.substring(10, nameLength);
                    attrValue = firstLine + "\n" + secondLine + "\n" + thirdLine;
                    Map<String, Object> config = new HashMap<String, Object>(2);
                    config.put("fontSize", 7.5);
                    config.put("fontPos", "LEFT");
                    insertTextIntoCell(cell, attrValue, true, false, config);
                } else if (nameLength > 18) {
                    throw new RuntimeException("姓名不能超过18个汉字");
                }
            } else {       //包含“•”
                //查找出点的所有位置，然后排序
                char[] attrValueChars = attrValue.toCharArray();
                List<Integer> dotIndex = new ArrayList<Integer>();
                for (int i = 0; i < attrValueChars.length; i++) {
                    if (attrValueChars[i] == '•') {
                        dotIndex.add(i);
                    }
                }
                Collections.sort(dotIndex);
                if (nameLength <= 4) {
                    insertTextIntoCell(cell, attrValue, true, false, null);
                } else if (nameLength > 4 && nameLength <= 8) {
                    //例如 帕巴拉格•列朗杰 点在索引4上
                    if (dotIndex.contains(4)) {
                        String firstLine = attrValue.substring(0, 3);
                        String secondLine = attrValue.substring(3, nameLength);
                        attrValue = firstLine + "\n" + secondLine;
                        Map<String, Object> config = new HashMap<String, Object>(1);
                        config.put("fontPos", "LEFT");
                        insertTextIntoCell(cell, attrValue, true, false, config);
                    } else {
                        String firstLine = attrValue.substring(0, 4);
                        String secondLine = attrValue.substring(4, nameLength);
                        attrValue = firstLine + "\n" + secondLine;
                        Map<String, Object> config = new HashMap<String, Object>(1);
                        config.put("fontPos", "LEFT");
                        insertTextIntoCell(cell, attrValue, true, false, config);
                    }
                } else if (nameLength > 8 && nameLength <= 10) {
                    if (dotIndex.contains(5)) {
                        String firstLine = attrValue.substring(0, 4);
                        String secondLine = attrValue.substring(4, nameLength);
                        attrValue = firstLine + "\n" + secondLine;
                        Map<String, Object> config = new HashMap<String, Object>(2);
                        config.put("fontSize", 10.5);
                        config.put("fontPos", "LEFT");
                        insertTextIntoCell(cell, attrValue, true, false, config);
                    } else {
                        String firstLine = attrValue.substring(0, 5);
                        String secondLine = attrValue.substring(5, nameLength);
                        attrValue = firstLine + "\n" + secondLine;
                        Map<String, Object> config = new HashMap<String, Object>(2);
                        config.put("fontSize", 10.5);
                        config.put("fontPos", "LEFT");
                        insertTextIntoCell(cell, attrValue, true, false, config);
                    }
                } else if (nameLength > 10 && nameLength <= 15) {
                    if (dotIndex.contains(5)) {
                        String firstLine = attrValue.substring(0, 4);
                        String secondLine;
                        if (dotIndex.contains(9)) {
                            secondLine = attrValue.substring(4, 8);
                            String thirdLine = attrValue.substring(8, nameLength);
                            attrValue = firstLine + "\n" + secondLine + "\n" + thirdLine;
                            Map<String, Object> config = new HashMap<String, Object>(2);
                            config.put("fontSize", 7.5);
                            config.put("fontPos", "LEFT");
                            insertTextIntoCell(cell, attrValue, true, false, config);
                        } else {
                            secondLine = attrValue.substring(4, 9);
                            String thirdLine = attrValue.substring(9, nameLength);
                            attrValue = firstLine + "\n" + secondLine + "\n" + thirdLine;
                            Map<String, Object> config = new HashMap<String, Object>(2);
                            config.put("fontSize", 7.5);
                            config.put("fontPos", "LEFT");
                            insertTextIntoCell(cell, attrValue, true, false, config);
                        }
                    }
                } else if (nameLength > 18) {
                    throw new RuntimeException("姓名不能超过18个汉字");
                }
            }
        }
    }
}
