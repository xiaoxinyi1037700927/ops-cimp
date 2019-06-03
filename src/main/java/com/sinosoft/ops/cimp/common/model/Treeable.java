package com.sinosoft.ops.cimp.common.model;

import java.util.Collection;

/**
 * @version 1.0.0
 * @classname: Treeable
 * @description: 可树型化
 * @author: Nil
 * @date: 2017年11月11日 下午9:16:57
 */
public interface Treeable {
    /**
     * 获取标识
     *
     * @return 标识
     */
    Object getId();

    /**
     * 设置标识
     *
     * @param id 标识
     */
    void setId(Object id);

    /**
     * 获取文本
     *
     * @return 文本
     */
    String getText();

    /**
     * 设置文本
     *
     * @param text 文本
     */
    void setText(String text);

    /**
     * 获取父标识
     *
     * @return 父标识
     */
    Object getParentId();

    /***
     * 设置父标识
     * @param parentId 父标识
     */
    void setParentId(Object parentId);

    /**
     * 获取是否叶子
     *
     * @return 是否叶子
     */
    Boolean getLeaf();

    /**
     * 设置可选择
     */
    void setSelectable(Boolean selectable);

    /**
     * 获取可选择节点
     *
     * @return
     */
    Boolean getSelectable();

    /**
     * 设置是否叶子
     */
    void setLeaf(Boolean leaf);

    /**
     * 获取是否展开
     *
     * @return 是否展开
     */
    Boolean getExpanded();

    /**
     * 设置是否展开
     */
    void setExpanded(Boolean expanded);

    /**
     * 获取是否选中
     *
     * @return 是否选中
     */
    Boolean getChecked();

    /**
     * 设置是否选中
     */
    void setChecked(Boolean checked);

    /**
     * 获取孩子集合
     *
     * @return 孩子集合
     */
    Collection<Treeable> getChildren();

    /**
     * 设置孩子列表
     *
     * @param children 孩子列表
     */
    void setChildren(Collection<Treeable> children);
}
