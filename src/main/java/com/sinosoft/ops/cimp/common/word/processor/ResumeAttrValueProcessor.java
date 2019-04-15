package com.sinosoft.ops.cimp.common.word.processor;

import com.aspose.words.*;
import com.sinosoft.ops.cimp.common.word.base.MapMailMergeDataSource;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/9/28.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class ResumeAttrValueProcessor extends AbstractAttrValueProcessor implements AttrValueProcessor {

    @Override
    @SuppressWarnings("unchecked")
    public void processAttr(Document document, Map<String, Object> attrAndValue) throws Exception {
        double fontSize = (Double) attrAndValue.get("fontSize");
        List<Map<String, Object>> resumeContent = (List<Map<String, Object>>) attrAndValue.get("resumeContent");

        if (resumeContent.size() > 0) {
            document.getMailMerge().executeWithRegions(new MapMailMergeDataSource(resumeContent, "Resumes"));
        }

        TableCollection tables = document.getFirstSection().getBody().getTables();
        Cell cell = tables.get(0).getRows().get(9).getCells().get(1);

        if (cell != null) {
            ParagraphCollection paragraphs = cell.getParagraphs();
            for (Paragraph paragraph : paragraphs) {
                RunCollection runs = paragraph.getRuns();
                for (Run run : runs) {
                    run.getFont().setSize(fontSize);
                }
            }
        }
        //设置生成的表格的字体和字号
        //这里需要根据简历的多少决定
        //1.字号。这里可以根据结果的行数和总体的数量两个指标最终决定缩小的尺度
        //2.表格的宽度。主要是表格的第一列的宽度，字体缩小的尺度同样决定着第一列的宽度
        //3.表格的行距这里统一设置为0
    }
}
