/**
 * @project: IIMP
 * @title: InCondition.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @version 1.0.0
 * @classname: InCondition
 * @description: IN条件
 * @author: Nil
 * @date: 2017年8月1日 上午10:15:41
 */

public class InCondition extends AbstractCondition {
    private static final long serialVersionUID = -1153009941971086781L;

    /*** IN操作符 */
    public enum InOperator {
        /*** 在...中 */
        IN("IN", "在...中"),
        /*** 不介于之间 */
        NOT_IN("NOT IN", "不在...中");

        private final String value;
        private final String name;

        InOperator(final String value, final String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    /***运算数*/
    protected String operand = null;
    /*** IN操作符 */
    protected InOperator operator = null;
    /***值集*/
    protected Collection<? extends Serializable> values = new ArrayList<>();

    public InCondition() {
        this.operator = InOperator.IN;
    }

    public InCondition(String operand, Collection<? extends Serializable> values) {
        this.operator = InOperator.IN;
        this.operand = operand;
        this.values = values;
    }

    public InCondition(String operand, InOperator operator, Collection<? extends Serializable> values) {
        this.operand = operand;
        this.operator = operator;
        this.values = values;
    }

    @Override
    public boolean add(AbstractCondition condition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(AbstractCondition condition) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<AbstractCondition> getChildren() {
        return Collections.emptyList();//条件项的子条件为空集
    }

    public Collection<? extends Serializable> getValues() {
        return values;
    }

    public void setValues(Collection<? extends Serializable> values) {
        this.values = values;
    }

    /**
     * @return
     * @see com.newskysoft.iimp.query.AbstractCondition#buildPredicate()
     */
    @Override
    public String buildPredicate() {
        StringBuilder sb = new StringBuilder().append(operand).append(" ").append(operator.getValue()).append(" ( ");
        String comma = "";
        for (Object value : values) {
            sb.append(comma);
            sb.append(value);
            if ("".equals(comma)) {
                comma = ",";
            }
        }
        return sb.append(" )").toString();
    }

    @Override
    public String toString() {
        return buildPredicate();
    }

    @Override
    public ConditionType getType() {
        return ConditionType.IN;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public static AbstractCondition fromJson(String json) {
        return JSON.parseObject(json, new TypeReference<InCondition>() {
        });
    }

    public void setOperator(InOperator operator) {
        this.operator = operator;
    }

    public InOperator getOperator() {
        return this.operator;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public static void main(String args[]) throws Exception {
        Collection<String> collection = new ArrayList<String>();
        for (int i = 1; i < 5; i++) {
            collection.add(String.valueOf(i));
        }
        InCondition bc = new InCondition("name", collection);
        System.out.println(JSON.toJSONString(bc, SerializerFeature.WriteDateUseDateFormat));
    }
}
