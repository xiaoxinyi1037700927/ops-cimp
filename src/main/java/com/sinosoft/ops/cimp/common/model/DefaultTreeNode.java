/**
 * @project: IIMP
 * @title: DefaultTreeNode.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @version 1.0.0
 * @ClassName: DefaultTreeNode
 * @description: 缺省树节点
 * @author: Nil
 * @date: 2017年11月11日 下午9:19:17
 */
public class DefaultTreeNode implements TreeNode {
    /*** 标识 */
    protected Object id;
    /*** 文本 */
    protected String text;
    /*** 父标识 */
    protected Object parentId = null;
    /*** 是否叶子 */
    protected Boolean leaf = false;
    /*** 是否可选择 */
    protected Boolean selectable = null;
    /*** 是否展开 */
    protected Boolean expanded = null;
    /*** 是否选中 */
    protected Boolean checked = null;
    /*** 节点类型 */
    protected String nodeType = null;
    /*** 数据 */
    protected Object data = null;
    /*** code000.006.004 */
    protected String code;
    /*** 孩子列表 */
    protected Collection<TreeNode> children = new LinkedList<TreeNode>();

    public DefaultTreeNode() {
    }

    public DefaultTreeNode(Object id, String text) {
        this.id = id;
        this.text = text;
    }

    public DefaultTreeNode(Object id, String text, Boolean leaf) {
        this.id = id;
        this.text = text;
        this.leaf = leaf;
    }

    public DefaultTreeNode(Object id, String text, Boolean leaf, Object parentId) {
        this.id = id;
        this.text = text;
        this.leaf = leaf;
        this.parentId = parentId;
    }

    public DefaultTreeNode(Object id, String text, Boolean leaf, Object parentId, Collection<TreeNode> children) {
        this.id = id;
        this.text = text;
        this.leaf = leaf;
        this.parentId = parentId;
        this.children = children;
    }

    public DefaultTreeNode(String code, Object id, String text, Boolean leaf, Object parentId,
                           Collection<TreeNode> children) {
        this.code = code;
        this.id = id;
        this.text = text;
        this.leaf = leaf;
        this.parentId = parentId;
        this.children = children;
    }

    public DefaultTreeNode(Object id, String text, Boolean leaf, Object parentId, Collection<TreeNode> children,
                           Object data) {
        this.id = id;
        this.text = text;
        this.leaf = leaf;
        this.parentId = parentId;
        this.children = children;
        this.data = data;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Collection<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(Collection<TreeNode> children) {
        this.children = children;
    }
}
