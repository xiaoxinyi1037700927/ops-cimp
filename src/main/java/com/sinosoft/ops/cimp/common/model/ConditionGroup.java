/**
 * @project: IIMP
 * @title: ConditionGroup.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;
import java.util.*;

/**
 * @version 1.0.0
 * @classname: ConditionGroup
 * @description: 条件分组
 * @author: Nil
 * @date: 2017年8月1日 上午10:15:10
 */
@JSONType(orders = {"children", "operator", "type"})
public class ConditionGroup extends AbstractCondition {
    private static final long serialVersionUID = -3551742081099475950L;

    /*** 逻辑运算符 */
    public enum LogicalOperator {
        /**
         * 非
         */
        NOT("NOT", "非"),
        /**
         * 与
         */
        AND("AND", "与"),
        /**
         * 或
         */
        OR("OR", "或");

        private final String value;
        private final String name;

        LogicalOperator(final String value, final String name) {
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

    /*** 逻辑运算符 */
    protected LogicalOperator operator = null;

    public ConditionGroup() {
        //缺省为AND
        this.operator = LogicalOperator.AND;
        this.children = new LinkedList<AbstractCondition>();
    }

    public ConditionGroup(LogicalOperator operator) {
        this.operator = operator;
        this.children = new LinkedList<AbstractCondition>();
    }

    public LogicalOperator getOperator() {
        return operator;
    }

    public void setOperator(LogicalOperator operator) {
        this.operator = operator;
    }

    @Override
    public boolean add(AbstractCondition condition) {
        return this.children.add(condition);
    }

    @Override
    public boolean remove(AbstractCondition condition) {
        return this.children.remove(condition);
    }

    @Override
    public Collection<AbstractCondition> getChildren() {
        return children;
    }

    public void setChildren(Collection<AbstractCondition> children) {
        this.children = children;
    }

    @Override
    public String buildPredicate() {
        boolean first = true;
        StringBuilder sb = new StringBuilder();
        sb.append("( ");
        for (AbstractCondition condition : children) {
            if (condition == null) continue;
            if (first) {
                first = false;
            } else {
                sb.append(" ");
                sb.append(operator);
                sb.append(" ");
            }
            sb.append(condition.toString());
        }
        sb.append(" )\r\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return buildPredicate();
    }

    @Override
    public ConditionType getType() {
        return ConditionType.GROUP;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    /**
     * @param json
     * @return
     */
    public static AbstractCondition fromJson(String json, AbstractCondition parent) {
        /* 根据字符串得到 JSONObject */
        JSONObject jsonObj = JSON.parseObject(json);
        //System.out.println("jsonObj = " + jsonObj);
        ConditionType type = ConditionType.valueOf((String) jsonObj.get("type"));

        /* 如果是 CONDITION_GROUP 类型 则进行递归 */
        if (type == ConditionType.GROUP) {
            ConditionGroup group = new ConditionGroup(LogicalOperator.valueOf(jsonObj.get("operator").toString()));
            if (parent == null) {
                parent = group;
            } else {
                parent.add(group);
            }

            /* 解析孩子 */
            JSONArray jsonArray = jsonObj.getJSONArray("children");
            Iterator<Object> it = jsonArray.iterator();
            while (it.hasNext()) {
                json = JSON.toJSONString((JSONObject) it.next(), SerializerFeature.WriteDateUseDateFormat);

                fromJson(json, group);
            }
            return group;
        } else {
            /* 具体条件 */
            AbstractCondition condition = null;
            if (type == ConditionType.BETWEEN) {
                condition = BetweenCondition.fromJson(json);
            } else if (type == ConditionType.BINARY) {
                condition = BinaryCondition.fromJson(json);
            } else if (type == ConditionType.IN) {
                condition = InCondition.fromJson(json);
            } else if (type == ConditionType.UNARY) {
                condition = UnaryCondition.fromJson(json);
            } else if (type == ConditionType.REFERENCE) {
                condition = ConditionReference.fromJson(json);
            }

            if (parent != null) {
                parent.add(condition);
            }
            return condition;
        }
    }

    @SuppressWarnings("serial")
    public static void main(String args[]) throws Exception {
        ConditionGroup cg = new ConditionGroup();
        System.out.println(JSON.toJSONString(cg, SerializerFeature.WriteDateUseDateFormat));

        Collection<? extends Serializable> values1 = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
        }};
        Collection<? extends Serializable> values2 = new ArrayList<Integer>() {{
            add(1);
            add(3);
            add(5);
        }};

        AbstractCondition root = new ConditionGroup(LogicalOperator.AND);
        //AbstractCondition cg = new BinaryCondition("name",RelationalOperator.EQUAL,"张三");

        ConditionGroup cg1 = new ConditionGroup();
        root.add(cg1);
        cg1.add(new InCondition("name", InCondition.InOperator.IN, values1));
        cg1.add(new BinaryCondition("name", BinaryCondition.BinaryOperator.EQUAL, "张三"));
        cg1.add(new InCondition("price", InCondition.InOperator.NOT_IN, values2));
        cg1.add(new BinaryCondition("name", BinaryCondition.BinaryOperator.LESS_THAN_OR_EQUAL, "李四"));
        cg1.add(new ConditionReference(UUID.randomUUID()));

        ConditionGroup cg2 = new ConditionGroup(LogicalOperator.AND);
        root.add(cg2);
        cg2.add(new BinaryCondition("name", BinaryCondition.BinaryOperator.NOT_EQUAL, "王一"));
        cg2.add(new InCondition("price", InCondition.InOperator.IN, values2));
        cg2.add(new ConditionReference(UUID.randomUUID()));

        System.out.println(root);
        //int features=SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);
        //String json = JSON.toJSONString(root,features,SerializerFeature.WriteDateUseDateFormat);
        String json = JSON.toJSONString(root, SerializerFeature.WriteDateUseDateFormat);
        System.out.println("Json1 = " + json);

        /* 比较fromJson返回的对象与原对象的json是否相等 */
        AbstractCondition newCondition = ConditionGroup.fromJson(json, null);
        System.out.println("newCondition = " + newCondition);
        String json1 = JSON.toJSONString(newCondition, SerializerFeature.WriteDateUseDateFormat);
        System.out.println(json.equals(json1));
    }
}