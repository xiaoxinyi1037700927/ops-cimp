package com.sinosoft.ops.cimp.util.combinedQuery.beans.codeSet;

public enum Education {
    CODE1("1", "研究生..."),
    CODE11("11", "博士研究生"),
    CODE12("12", "硕士研究生"),
    CODE13("13", "研究生"),
    CODE19("19", "研究生肄业"),
    CODE2("2", "大学本科..."),
    CODE21("21", "大学"),
    CODE22("22", "第二学士学位"),
    CODE29("29", "大学肄业"),
    CODE3("3", "大专和专科学校..."),
    CODE31("31", "大学专科"),
    CODE32("32", "大学普通班"),
    CODE39("39", "专科肄业"),
    CODE4("4", "中专和中技..."),
    CODE41("41", "中专"),
    CODE42("42", "中技"),
    CODE49("49", "中专/中技肄业"),
    CODE5("5", "技工学校..."),
    CODE51("51", "技校"),
    CODE59("59", "技校肄业"),
    CODE6("6", "高中..."),
    CODE61("61", "高中"),
    CODE62("62", "职高"),
    CODE63("63", "农业高中"),
    CODE69("69", "高中肄业"),
    CODE7("7", "初中..."),
    CODE71("71", "初中"),
    CODE72("72", "职业初中"),
    CODE73("73", "农业初中"),
    CODE79("79", "初中肄业"),
    CODE8("8", "小学..."),
    CODE81("81", "小学"),
    CODE89("89", "小学肄业"),
    CODE9("9", "文盲或半文盲"),
    CODE10("10", "学龄前儿童");

    private String code;
    private String name;

    Education(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
