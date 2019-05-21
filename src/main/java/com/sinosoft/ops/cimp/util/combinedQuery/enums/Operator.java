package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Operator {
    EQ("等于", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s = %s", "%s 等于 %s"),
    NQ("不等于", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s != %s", "%s 不等于 %s"),
    GT("大于", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s > %s", "%s 大于 %s"),
    LT("小于", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s < %s", "%s 小于 %s"),
    GE("大于等于", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s >= %s", "%s 大于等于 %s"),
    LE("小于等于", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s <= %s", "%s 小于等于 %s"),
    IN("在...之中", "^在(\\[.+?\\])之中$",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s IN (%s)", "%s 在[%s]之中"),
    NOT_IN("不在...之中", "^不在(\\[.+?\\])之中$",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s NOT IN (%s)", "%s 不在[%s]之中"),
    IS_NULL("为空", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode()},
            "%s IS NULL", "%s 为空"),
    NOT_NULL("不为空", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode()},
            "%s IS NOT NULL", "%s 不为空"),
    CONTAINS("包含", "",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s LIKE '%%%s%%'", "%s 包含 %s"),
    BETWEEN_AND("介于..和..之间", "^介于(.+?)和(.+?)之间$",
            new int[]{Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode(), Type.ALL_FIELD_TYPE.getCode()},
            "%s BETWEEN %s AND %s", "%s 介于%s和%s之间");

    /**
     * 操作符名称
     */
    private String name;
    /**
     * 解析操作符的正则表达式(默认用名称匹配，如果无法匹配才用正则)
     */
    private String regex;
    /**
     * 所需的参数类型
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

    Operator(String name, String regex, int[] paramsType, String sqlFormat, String exprFormat) {
        this.name = name;
        this.regex = regex;
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
}
