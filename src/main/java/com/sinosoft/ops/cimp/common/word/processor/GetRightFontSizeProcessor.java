package com.sinosoft.ops.cimp.common.word.processor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rain chen on 2017/10/24.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class GetRightFontSizeProcessor {

    private List<FontSizeAndLines> fontSizeAndLines = new ArrayList<FontSizeAndLines>();

    public double getRightFontSize(List<Integer> linesCount) {

        for (int i = 0; i < fontSizeAndLines.size(); i++) {
            FontSizeAndLines fontSizeAndLine = (FontSizeAndLines) fontSizeAndLines.get(i);
            int lines = 0;
            int maxLine = fontSizeAndLine.getMaxLine();
            double fontSize = fontSizeAndLine.getFontSize();
            for (Integer count : linesCount) {
                int c = Math.round(count / fontSizeAndLine.getWordCountInLine());
                //最少一行
                if (c == 0) {
                    c = 1;
                }
                lines += c;
            }
            if (maxLine > lines) {
                return fontSize;
            }
        }
        //若查找不到字号则使用最小字号
        return 5.0d;
    }

    public GetRightFontSizeProcessor() {
        fontSizeAndLines.add(new FontSizeAndLines(12, 19, 21));
        fontSizeAndLines.add(new FontSizeAndLines(11, 20, 24));
        fontSizeAndLines.add(new FontSizeAndLines(10.5, 22, 25));
        fontSizeAndLines.add(new FontSizeAndLines(10, 24, 26));
        fontSizeAndLines.add(new FontSizeAndLines(9, 30, 27));
        fontSizeAndLines.add(new FontSizeAndLines(8, 34, 31));
        fontSizeAndLines.add(new FontSizeAndLines(7.5, 38, 32));
        fontSizeAndLines.add(new FontSizeAndLines(6.5, 46, 38));
        fontSizeAndLines.add(new FontSizeAndLines(5.5, 58, 48));
        fontSizeAndLines.add(new FontSizeAndLines(5, 61, Integer.MAX_VALUE));
    }
}

class FontSizeAndLines {

    //字号
    private double fontSize;
    //每行字数
    private int wordCountInLine;
    //最大行数
    private int maxLine;

    public FontSizeAndLines(double fontSize, int wordCountInLine, int maxLine) {
        this.fontSize = fontSize;
        this.wordCountInLine = wordCountInLine;
        this.maxLine = maxLine;
    }

    public double getFontSize() {
        return fontSize;
    }

    public int getWordCountInLine() {
        return wordCountInLine;
    }

    public int getMaxLine() {
        return maxLine;
    }
}
