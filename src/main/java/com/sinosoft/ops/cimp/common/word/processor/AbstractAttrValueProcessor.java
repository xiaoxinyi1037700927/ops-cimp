package com.sinosoft.ops.cimp.common.word.processor;

import com.aspose.words.*;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
public abstract class AbstractAttrValueProcessor {

    /**
     * 获取域的Cell
     *
     * @param document 模板文档对象
     * @param attrName 域对应的属性名
     * @return Document对象的Cell
     */
    public Cell getAttrIndexCell(Document document, String attrName) {
        //寻找域的pattern(两次校验防止field的值就为域本身)
        String pattern = "MERGEFIELD  " + attrName;
        String pattern1 = "«" + attrName + "»";

        //该域所在的坐标
        TableCollection tables = document.getFirstSection().getBody().getTables();
        int iTable = 0, jRow = 0, kCell = 0;
        for (int i = 0; i < tables.getCount(); i++) {
            Table table = tables.get(i);
            RowCollection rows = table.getRows();
            for (int j = 0; j < rows.getCount(); j++) {
                Row row = rows.get(j);
                CellCollection cells = row.getCells();
                for (int k = 0; k < cells.getCount(); k++) {
                    Cell cell = cells.get(k);
                    String text = cell.getText();
                    if (text.contains(pattern) && text.contains(pattern1)) {
                        iTable = i;
                        jRow = j;
                        kCell = k;
                        break;
                    }
                }
            }
        }
        //获取第i个table的第j行第k列的Cell
        Cell cell = document.getFirstSection().getBody().getTables().get(iTable).getRows().get(jRow).getCells().get(kCell);
        if (cell == null) {
            throw new RuntimeException("未设置域或者域填写错误，错误的域为：" + attrName);
        }
        if (iTable == 0 && jRow == 0 && kCell == 0) {
            return null;
        }
        return cell;
    }

    public static void insertTextIntoCell(Cell cell, String content, Boolean isRemoveOldValue, Boolean isNewLine, Map<String, Object> config) throws Exception {
        //如果在这个cell中没有Paragraph
        //添加一个新行
        if (cell.getParagraphs().getCount() == 0) {
            isNewLine = true;
            isRemoveOldValue = false;
        }
        if (isRemoveOldValue) {
            isNewLine = false;
            Paragraph paragraph = getEmptyParagraph(cell, config == null ? new HashMap<String, Object>() : config);
            cell.removeAllChildren();
            cell.appendChild(paragraph);
        }
        if (isNewLine) {
            cell.appendChild(getEmptyParagraph(cell, config == null ? new HashMap<String, Object>() : config));
        }
        Run run = cell.getLastParagraph().getRuns().get(cell.getLastParagraph().getRuns().getCount() - 1);
        run.setText(content);
    }

    public static Paragraph getEmptyParagraph(Cell cell, Map<String, Object> config) throws Exception {
        Paragraph paragraph;
        if (cell.getParagraphs().getCount() > 0) {
            paragraph = (Paragraph) cell.getLastParagraph().deepClone(false);
            Run run = cell.getLastParagraph().getRuns().get(0);
            if (run != null) {
                run = (Run) run.deepClone(true);
            } else {
                run = new Run(cell.getDocument());
                run.getFont().setName("宋体");
                if (config.get("fontSize") != null) {
                    run.getFont().setSize(NumberUtils.toInt(StringUtil.obj2Str(config.get("fontSize"))));
                } else {
                    run.getFont().setSize(14);
                }
            }
            //对齐方式
            ParagraphFormat paragraphFormat = paragraph.getParagraphFormat();
            if (config.get("fontPos") != null) {
                if (StringUtils.equals(StringUtil.obj2Str(config.get("fontPos")), "LEFT")) {
                    paragraphFormat.setAlignment(ParagraphAlignment.LEFT);
                } else if (StringUtils.equals(StringUtil.obj2Str(config.get("fontPos")), "CENTER")) {
                    paragraphFormat.setAlignment(ParagraphAlignment.CENTER);
                } else if (StringUtils.equals(StringUtil.obj2Str(config.get("fontPos")), "RIGHT")) {
                    paragraphFormat.setAlignment(ParagraphAlignment.RIGHT);
                }
            } else {
                paragraphFormat.setAlignment(ParagraphAlignment.CENTER);
            }
            paragraph.appendChild(run);
        } else {
            paragraph = new Paragraph(cell.getDocument());
            Run run = new Run(cell.getDocument());
            paragraph.appendChild(run);
        }
        return paragraph;
    }
}
