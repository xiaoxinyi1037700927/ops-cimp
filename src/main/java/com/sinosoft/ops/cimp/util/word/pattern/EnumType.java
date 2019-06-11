package com.sinosoft.ops.cimp.util.word.pattern;

/**
 * Created by SML
 * date : 2017/11/8
 * des :
 */
public enum EnumType {

	//毕节使用的标准
    SCHOOL_UNIT_TYPE_71("中央党校", "71", "中央党校"),
    SCHOOL_UNIT_TYPE_72("省委党校", "72", "省委党校"),
    SCHOOL_UNIT_TYPE_73("市委党校", "73", "地、市委党校"),

    //新疆使用的标准
    SCHOOL_UNIT_TYPE_101("中央党校", "101", "中央党校"),
    SCHOOL_UNIT_TYPE_102("省委党校", "102", "省委党校"),
    SCHOOL_UNIT_TYPE_103("市委党校", "103", "地、市委党校"),


    ;
    public final String name;
    public final String value;
    public final String des;

    private EnumType(String name, String value, String des) {
        this.name = name;
        this.value = value;
        this.des = des;
    }
}
