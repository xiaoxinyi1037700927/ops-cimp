/**
 * @project: IIMP
 * @title: ConditionReference.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;


/**
 * @version 1.0.0
 * @classname: ConditionReference
 * @description: 条件引用
 * @author: Nil
 * @date: 2017年8月1日 上午10:14:19
 */

public class ConditionReference extends AbstractCondition {
    private static final long serialVersionUID = -2433777850767812787L;

    /*** 引用条件标识 */
    protected UUID conditionId = null;

    public ConditionReference() {
    }

    public ConditionReference(UUID conditionId) {
        this.conditionId = conditionId;
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
        return new StringBuilder().append(" ").append("${").append(conditionId).append("} ").toString();
    }

    @Override
    public String toString() {
        return buildPredicate();
    }

    @Override
    public ConditionType getType() {
        return ConditionType.REFERENCE;
    }

    @Override
    public void setType(ConditionType type) {
        this.type = type;
    }

    public UUID getConditionId() {
        return conditionId;
    }

    public void setConditionId(UUID conditionId) {
        this.conditionId = conditionId;
    }

    public static AbstractCondition fromJson(String json) {
        return JSON.parseObject(json, new TypeReference<ConditionReference>() {
        });
    }

    public static void main(String args[]) throws Exception {
        ConditionReference bc = new ConditionReference(UUID.randomUUID());
        System.out.println(JSON.toJSONString(bc, SerializerFeature.WriteDateUseDateFormat));
    }
}
