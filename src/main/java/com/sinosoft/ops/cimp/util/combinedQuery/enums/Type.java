package com.sinosoft.ops.cimp.util.combinedQuery.enums;

public enum Type {
    NONE("无", 0),
    BRACKETS("括号", 1),
    LOGICAL_OPERATOR("逻辑操作符", 1 << 1),
    OPERATOR("运算符", 1 << 2),
    STRING("字符", 1 << 3),
    NUMBER("数字", 1 << 4),
    DATE("日期", 1 << 5),
    LOB("大字段", 1 << 6),
    CODE("码值", 1 << 7),
    UNKNOWN("未知类型", 1 << 8),
    ALL_FIELD_TYPE("所有字段类型", STRING.code | NUMBER.code | DATE.code | LOB.code | CODE.code),
    ALL_FIELD_TYPE_WITHOUT_CODE("除了码值的字段类型", STRING.code | NUMBER.code | DATE.code | LOB.code),
    ALL("所有类型", (1 << 30) - 1);

    private String name;
    private int code;

    Type(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static String getNameByCode(int code) {
        for (Type type : Type.values()) {
            if (type.getCode() == code) {
                return type.getName();
            }
        }
        return "未定义类型";
    }
}
