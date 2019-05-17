package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Function {
    LENGTH("求长度", "^求长度\\((.*?)\\)$", 1, 1, " length(%s) "),
    SYSDATE("系统日期", "^系统日期\\((.*?)\\)$", 0, 1 << 1, " SYSDATE "),
    MONTH_DIFFERENCE("月份差", "^月份差\\((.*?)\\)$", 2, 1 << 2, " MONTHS_BETWEEN(%s,%s) "),
    TO_DATE("转日期", "^转日期\\((.*?)\\)$", 2, 1 << 3, " TO_DATE(%s,%s) "),
    ABS("绝对值", "^绝对值\\((.*?)\\)$", 1, 1 << 4, " ABS(%s) "),
    FLOOR("向下取整", "^向下取整\\((.*?)\\)$", 1, 1 << 5, " FLOOR(%s) "),
    CEIL("向上取整", "^向上取整\\((.*?)\\)$", 1, 1 << 6, " CEIL(%s) "),
    SUB_STRING("字符串截取", "^字符串截取\\((.*?)\\)$", 3, 1 << 7, " SUBSTR(%s,%s,%s) "),
    DIVIDE("求商", "^求商\\((.*?)\\)$", 2, 1 << 8, " %s / %s "),
    MULTIPLY("求积", "^求积\\((.*?)\\)$", 2, 1 << 9, " %s * %s "),
    PLUS("求和", "^求和\\((.*?)\\)$", 2, 1 << 10, " %s + %s "),
    MINUS("求差", "^求差\\((.*?)\\)$", 2, 1 << 11, " %s - %s ");

    private String name;
    private String regex;
    private int paramsNum;
    private int code;
    private String sql;

    Function(String name, String regex, int paramsNum, int code, String sql) {
        this.name = name;
        this.regex = regex;
        this.paramsNum = paramsNum;
        this.code = code;
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public int getParamsNum() {
        return paramsNum;
    }

    public int getCode() {
        return code;
    }

    public String getSql() {
        return sql;
    }
}

