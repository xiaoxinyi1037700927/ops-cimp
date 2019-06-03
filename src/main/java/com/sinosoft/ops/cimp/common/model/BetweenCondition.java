/**
 * @project: IIMP
 * @title: BetweenCondition.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @version 1.0.0
 * @classname: BetweenCondition
 * @description: 介于两个值之间条件
 * @author: Nil
 * @date: 2017年8月1日 上午10:13:51
 */
@JSONType(orders = {"children", "maxValue", "minValue", "operand", "operator", "type"})
public class BetweenCondition extends AbstractCondition {
    private static final long serialVersionUID = -8935242258542461571L;

    /*** 介于操作符 */
    public enum BetweenOperator {
        /*** 介于之间 */
        BETWEEN("BETWEEN", "介于之间"),
        /*** 不介于之间 */
        NOT_BETWEEN("NOT BETWEEN", "不介于之间");

        private final String value;
        private final String name;

        BetweenOperator(final String value, final String name) {
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
    /*** 操作符 */
    protected BetweenOperator operator = null;
    /*** 最小值 */
    protected Object minValue = null;
    /*** 最大值 */
    protected Object maxValue = null;

    public BetweenCondition() {
        this.operator = BetweenOperator.BETWEEN;
    }

    public BetweenCondition(String operand, Object minValue, Object maxValue) {
        this.operator = BetweenOperator.BETWEEN;
        this.operand = operand;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public BetweenCondition(String operand, BetweenOperator operator, List<Object> values) {
        this.operand = operand;
        this.operator = operator;
        this.minValue = values.get(0);
        this.maxValue = values.get(1);
    }

    public BetweenCondition(String operand, BetweenOperator operator, Object minValue, Object maxValue) {
        this.operand = operand;
        this.operator = operator;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
        return Collections.emptyList();// 条件项的子条件为空集
    }

    public Object getMinValue() {
        return minValue;
    }

    public void setMinValue(Object minValue) {
        this.minValue = minValue;
    }

    public Object getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Object maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return
     */
    @Override
    public String buildPredicate() {
        return new StringBuilder().append(operand).append(" ").append(operator.getValue()).append(" ").append(minValue).append(" AND ").append(maxValue)
                .toString();
    }

    @Override
    public String toString() {
        return buildPredicate();
    }

    @Override
    public ConditionType getType() {
        return ConditionType.BETWEEN;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public BetweenOperator getOperator() {
        return this.operator;
    }

    public void setOperator(BetweenOperator operator) {
        this.operator = operator;
    }

    public static AbstractCondition fromJson(String json) {
        return JSON.parseObject(json, new TypeReference<BetweenCondition>() {
        });
    }

    public static void main(String args[]) throws Exception {
        BetweenCondition bc = new BetweenCondition("name", 2, 10);
        System.out.println(JSON.toJSONString(bc, SerializerFeature.WriteDateUseDateFormat));

        System.out.println(ConditionType.BETWEEN.toString());
        System.out.println(ConditionType.valueOf("BETWEEN"));

        System.out.println(BinaryCondition.BinaryOperator.GREAT_THAN_OR_EQUAL);
        System.out.println(BinaryCondition.BinaryOperator.valueOf("EQUAL1"));
    }
}
