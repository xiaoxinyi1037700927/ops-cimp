package com.sinosoft.ops.cimp.dao.domain;

import java.io.Serializable;

public class Conditions implements Serializable {
    private static final long serialVersionUID = 7801729397709986916L;

    public enum ConditionsEnum {
        /**
         * 等于 =
         */
        EQUAL("="),
        /**
         * 小于 <
         */
        LESS("<"),
        /**
         * 大于 >
         */
        GREATER(">"),
        /**
         * 不等于 <>
         */
        UNEQUAL("<>"),
        /**
         * like
         */
        LIKE("like"),
        /**
         * in
         */
        IN("in"),
        /**
         * 不包含
         */
        NOT_IN("NOT IN"),
        /**
         * is
         */
        IS("IS");
        private String condition;

        private ConditionsEnum(String condition) {
            this.condition = condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getCondition() {
            return condition;
        }
    }

    /**
     * 条件类别信息
     */
    public enum Type {
        GROUP(0), //分组条件
        OR(1);//or 连接
        private int type;

        Type(int type) {
            this.type = type;
        }

        public String getStrType() {
            if (0 == this.getType()) {
                return "GROUP BY";
            } else if (1 == this.getType()) {
                return "OR";
            }
            return "";
        }

        public int getType() {
            return type;
        }

        public Type initType(int type) {
            this.type = type;
            return this;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    /**
     * 条件名字，vo.attr
     */
    private String conditionName;
    /**
     * 条件， =  <  >
     */
    private String condition;
    /**
     * 条件的值
     */
    private Object conditionValue;
    /**
     * 条件类别
     */
    private Type type;


    //    @Deprecated
    public Conditions(String conditionName, String condition, String conditionValue) {
        this.conditionName = conditionName;
        this.condition = condition;
        this.conditionValue = conditionValue;
    }

    public Conditions(String conditionName, ConditionsEnum condition, Object conditionValue, Type type) {
        this.conditionName = conditionName;
        if (condition != null) {
            this.condition = condition.getCondition();
        }
        this.conditionValue = conditionValue;
        this.type = type;
    }

    public Conditions(String conditionName, ConditionsEnum condition, String conditionValue) {
        this.conditionName = conditionName;
        this.condition = condition.getCondition();
        this.conditionValue = conditionValue;
    }

    public Conditions() {
    }

    @Override
    public Conditions clone() {
        Conditions newConditions = new Conditions();
        newConditions.setConditionName(this.conditionName);
        newConditions.setCondition(this.condition);
        newConditions.setConditionValue(this.conditionValue);
        return newConditions;
    }

    @Override
    public String toString() {
        return conditionName + ":" + condition + ":" + conditionValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getConditionName() {
        return conditionName;
    }

    public String getCondition() {
        return condition;
    }

    public void setConditionValue(Object conditionValue) {
        this.conditionValue = conditionValue;
    }

    public Object getConditionValue() {
        return conditionValue;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setCondition(ConditionsEnum condition) {
        this.condition = condition.getCondition();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conditions that = (Conditions) o;
        if (!conditionName.equals(that.conditionName)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return conditionName.hashCode();
    }
}
