package com.sinosoft.ops.cimp.export.common;

/**
 * Created by SML
 * date : 2017/11/8
 * des :
 */
public enum EnumType {

    SCHOOL_UNIT_TYPE_71("中央党校", "71", "中央党校"),
    SCHOOL_UNIT_TYPE_72("省委党校", "72", "省委党校"),
    SCHOOL_UNIT_TYPE_73("市委党校", "73", "地、市委党校"),

    UNIT_JOB_SPLIT("unit_job_split","unit_job_split","unit_job_split"),




    ;
    public final String name;
    public final String value;
    public final String des;

    private EnumType(String name, String value, String des) {
        this.name = name;
        this.value = value;
        this.des = des;
    }


    public enum positionEnum {
        四级职员("厅局级副职"),五级职员("县处级正职"),六级职员("县处级副职"),七级职员("乡科级正职"),八级职员("乡科级副职");
        private final String value;

        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        positionEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    public enum educationEnum {
        high("高中"),juniorMiddle("初中"),primary("小学"),illiteracy("文盲");
        private final String value;

        //构造器默认也只能是private, 从而保证构造函数只能在内部使用
        educationEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
