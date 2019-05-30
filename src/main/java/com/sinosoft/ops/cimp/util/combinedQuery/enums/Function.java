package com.sinosoft.ops.cimp.util.combinedQuery.enums;

import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;

public enum Function {
    LENGTH("求长度", "^求长度\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.STRING.getCode() | Type.NUMBER.getCode()},
            "length(%s)", "求长度(%s)"),
    SYSDATE("系统日期", "^系统日期\\((.*?)\\)$",
            Type.DATE.getCode(), new int[]{},
            "SYSDATE", "系统日期()"),
    MONTH_DIFFERENCE("月份差", "^月份差\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.DATE.getCode(), Type.DATE.getCode()},
            "MONTHS_BETWEEN(%s,%s)", "月份差(%s,%s)"),
    TO_DATE("转日期", "^转日期\\((.*?)\\)$",
            Type.DATE.getCode(), new int[]{Type.STRING.getCode(), Type.STRING.getCode()},
            "TO_DATE(%s,%s)", "转日期(%s,%s)"),
    ABS("绝对值", "^绝对值\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode()},
            "ABS(%s)", "绝对值(%s)"),
    FLOOR("向下取整", "^向下取整\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode()},
            "FLOOR(%s)", "向下取整(%s)"),
    CEIL("向上取整", "^向上取整\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode()},
            "CEIL(%s)", "向上取整(%s)"),
    SUB_STRING("字符串截取", "^字符串截取\\((.*?)\\)$",
            Type.STRING.getCode(), new int[]{Type.STRING.getCode(), Type.NUMBER.getCode(), Type.NUMBER.getCode()},
            "SUBSTR(%s,%s,%s)", "字符串截取(%s,%s,%s)"),
    DIVIDE("求商", "^求商\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode(), Type.NUMBER.getCode()},
            "%s / %s", "求商(%s,%s)"),
    MULTIPLY("求积", "^求积\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode(), Type.NUMBER.getCode()},
            "%s * %s", "求积(%s,%s)"),
    PLUS("求和", "^求和\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode(), Type.NUMBER.getCode()},
            "%s + %s", "求和(%s,%s)"),
    MINUS("求差", "^求差\\((.*?)\\)$",
            Type.NUMBER.getCode(), new int[]{Type.NUMBER.getCode(), Type.NUMBER.getCode()},
            "%s - %s", "求差(%s,%s)");

    /**
     * 函数名
     */
    private String name;
    /**
     * 解析函数的正则表达式
     */
    private String regex;
    /**
     * 返回值类型
     */
    private int returnType;
    /**
     * 所需参数类型
     */
    private int[] paramsType;
    /**
     * sql的格式
     */
    private String sqlFormat;
    /**
     * 表达式的格式
     */
    private String exprFormat;

    Function(String name, String regex, int returnType, int[] paramsType, String sqlFormat, String exprFormat) {
        this.name = name;
        this.regex = regex;
        this.returnType = returnType;
        this.paramsType = paramsType;
        this.sqlFormat = sqlFormat;
        this.exprFormat = exprFormat;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public int getReturnType() {
        return returnType;
    }

    public int[] getParamsType() {
        return paramsType;
    }

    public int getParamsNum() {
        return paramsType.length;
    }

    public String getSqlFormat() {
        return sqlFormat;
    }

    public String getExprFormat() {
        return exprFormat;
    }

    public static Function getByName(String name) throws CombinedQueryParseException {
        for (Function function : Function.values()) {
            if (function.getName().equals(name)) {
                return function;
            }
        }
        throw new CombinedQueryParseException("未定义的函数：" + name);
    }
}

