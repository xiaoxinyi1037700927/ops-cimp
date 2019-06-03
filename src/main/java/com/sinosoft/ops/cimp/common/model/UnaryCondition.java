/**
 * @project: IIMP
 * @title: UnaryCondition.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collection;
import java.util.Collections;

/**
 * @version 1.0.0
 * @classname: UnaryCondition
 * @description: 一元条件项（IS_NOT_NULL、IS_NULL等）
 * @author: Nil
 * @date: 2017年8月1日 上午10:16:43
 */
@JSONType(orders = {"children", "operand", "operator", "type"})
public class UnaryCondition extends AbstractCondition {
    private static final long serialVersionUID = 7822351050974989099L;

    /*** 一元操作符 */
    public enum UnaryOperator {
        /*** 为空值 */
        IS_NULL("IS NULL", "为空值"),
        /*** 非空值 */
        IS_NOT_NULL("IS NOT NULL", "非空值");

        private final String value;
        private final String name;

        UnaryOperator(final String value, final String name) {
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

    /*** 运算数 */
    protected Object operand = null;
    /*** 一元操作符 */
    protected UnaryOperator operator = null;

    public UnaryCondition() {
    }

    public UnaryCondition(String operand, UnaryOperator operator) {
        this.operand = operand;
        this.operator = operator;
    }

    public UnaryOperator getOperator() {
        return operator;
    }

    public void setOperator(UnaryOperator operator) {
        this.operator = operator;
    }

    public Object getOperand() {
        return operand;
    }

    public void setOperand(Object operand) {
        this.operand = operand;
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

    @Override
    public String buildPredicate() {
        return new StringBuilder().append(operand).append(" ").append(operator.getValue()).toString();
    }

    @Override
    public String toString() {
        return buildPredicate();
    }

    @Override
    public ConditionType getType() {
        return ConditionType.UNARY;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public static AbstractCondition fromJson(String json) {
        return JSON.parseObject(json, new TypeReference<UnaryCondition>() {
        });
    }

    public static void main(String args[]) throws Exception {
        UnaryCondition uc = new UnaryCondition("name", UnaryOperator.IS_NULL);
        System.out.println(JSON.toJSONString(uc, SerializerFeature.WriteDateUseDateFormat));
    }
}
