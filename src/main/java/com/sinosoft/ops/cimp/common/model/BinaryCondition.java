/**
 * @project: IIMP
 * @title: BinaryCondition.java
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
 * @classname: BinaryCondition
 * @description: 二元条件项
 * @author: Nil
 * @date: 2017年8月1日 上午10:14:19
 */

@JSONType(orders = {"children", "leftOperand", "operator", "rightOperand", "type"})
public class BinaryCondition extends AbstractCondition {
    private static final long serialVersionUID = -3390853364114348622L;

    /*** 二元操作符 */
    public enum BinaryOperator {
        /*** 等于 */
        EQUAL("=", "等于"),
        /*** 不等于 */
        NOT_EQUAL("<>", "不等于"),
        /*** 大于 */
        GREATER_THAN(">", "大于"),
        /*** 大于等于 */
        GREAT_THAN_OR_EQUAL(">=", "大于等于"),
        /*** 小于 */
        LESS_THAN("<", "小于"),
        /*** 小于等于 */
        LESS_THAN_OR_EQUAL("<=", "小于等于"),
        /*** 匹配 */
        LIKE("LIKE", "匹配"),
        /*** 不匹配 */
        NOT_LIKE("NOT LIKE", "不匹配"),
        /*** 左匹配 */
        LEFT_LIKE("LIKE", "左匹配"),
        /*** 右匹配 */
        RIGHT_LIKE("LIKE", "右匹配");

        private final String value;
        private final String name;

        BinaryOperator(final String value, final String name) {
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

    /*** 左运算数 */
    protected String leftOperand = null;
    /*** 二元运算符 */
    protected BinaryOperator operator = null;
    /*** 右运算数 */
    protected Object rightOperand = null;

    public BinaryCondition() {
    }

    public BinaryCondition(String leftOperand, BinaryOperator operator, Object rightOperand) {
        this.leftOperand = leftOperand;
        this.operator = operator;
        this.rightOperand = rightOperand;
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

    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public void setOperator(BinaryOperator operator) {
        this.operator = operator;
    }

    public BinaryOperator getOperator() {
        return this.operator;
    }

    public Object getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(Object rightOperand) {
        this.rightOperand = rightOperand;
    }

    @Override
    public String buildPredicate() {

        if ("LIKE".equals(operator.toString()) || "NOT_LIKE".equals(operator.toString())) {
            if (rightOperand.toString().startsWith("'")) {
                rightOperand = rightOperand.toString().replace("'", "");
            }
            return new StringBuilder().append(leftOperand).append(" ").append(operator.getValue()).append(" '%").append(rightOperand).append("%'").toString();
        }
        if ("LEFT_LIKE".equals(operator.toString())) {
            if (rightOperand.toString().startsWith("'")) {
                rightOperand = rightOperand.toString().replace("'", "");
            }
            return new StringBuilder().append(leftOperand).append(" ").append(operator.getValue()).append(" '").append(rightOperand).append("%'").toString();
        }
        if ("RIGHT_LIKE".equals(operator.toString())) {
            if (rightOperand.toString().startsWith("'")) {
                rightOperand = rightOperand.toString().replace("'", "");
            }
            return new StringBuilder().append(leftOperand).append(" ").append(operator.getValue()).append(" '%").append(rightOperand).append("'").toString();
        }
        return new StringBuilder().append(leftOperand).append(" ").append(operator.getValue()).append(" ").append(rightOperand).toString();
    }

    @Override
    public String toString() {
        return buildPredicate();
    }

    @Override
    public ConditionType getType() {
        return ConditionType.BINARY;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public static AbstractCondition fromJson(String json) {
        return JSON.parseObject(json, new TypeReference<BinaryCondition>() {
        });
    }

    public static void main(String args[]) throws Exception {
        BinaryCondition bc = new BinaryCondition("name", BinaryOperator.EQUAL, 10);
        System.out.println(JSON.toJSONString(bc, SerializerFeature.WriteDateUseDateFormat));
    }
}
