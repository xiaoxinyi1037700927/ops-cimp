/**
 * @Project: IIMP
 * @Title: AbstractCondition.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * @ClassName: AbstractCondition
 * @Description: 抽象条件类
 * @Author: Nil
 * @Date: 2017年8月1日 上午10:12:03
 * @Version 1.0.0
 */
public abstract class AbstractCondition implements Serializable {
    private static final long serialVersionUID = -1759943797514612955L;

    /*** 条件类型 */
    public enum ConditionType {
        /*** 一元 */
        UNARY,
        /*** 二元 */
        BINARY,
        /*** 在..间 */
        BETWEEN,
        /*** 在...中 */
        IN,
        /***条件引用*/
        REFERENCE,
        /*** 条件组 */
        GROUP
    }

    /*** 条件类型 */
    protected ConditionType type;
    /***子条件*/
    protected Collection<AbstractCondition> children = null;

    /**
     * 获取类型
     *
     * @return 类型
     */
    public abstract ConditionType getType();

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public abstract void setType(ConditionType type);

    /**
     * 添加条件
     *
     * @param condition 要添加的条件
     * @return 是否添加成功
     */
    public abstract boolean add(AbstractCondition condition);

    /**
     * 移除条件
     *
     * @param condition 要移除的条件
     * @return 是否移除成功
     */
    public abstract boolean remove(AbstractCondition condition);

    /**
     * 获取子条件集
     *
     * @return 子条件集
     */
    public abstract Collection<AbstractCondition> getChildren();

    /**
     * 生成条件谓词表达式
     *
     * @return 谓词表达式
     */
    public abstract String buildPredicate();
}
