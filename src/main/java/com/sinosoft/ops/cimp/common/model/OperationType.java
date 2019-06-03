/**
 * @project: iimp-gradle
 * @title: OperationType.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: OperationType
 * @description: 操作类型
 * @author: Ni
 * @date: 2018年6月6日 下午3:22:41
 * @since JDK 1.7
 */
public enum OperationType {
    /*** 空 */
    NONE((short) 0, "空"),

    /*** 插入 */
    INSERT((short) 1, "插入"),
    /*** 删除 */
    DELETE((short) 2, "删除"),
    /*** 更新 */
    UPDATE((short) 3, "更新"),
    /*** 查询 */
    SELECT((short) 4, "查询"),

    /*** 读 */
    READ((short) 5, "读"),
    /*** 写 */
    WRITE((short) 6, "写"),
    /*** 执行 */
    EXECUTE((short) 7, "执行"),

    /*** 审批 */
    APPROVE((short) 51, "审批"),
    /*** 审计 */
    AUDIT((byte) 61, "审计");

    private final short value;
    private final String name;

    OperationType(final short value, final String name) {
        this.value = value;
        this.name = name;
    }

    public short getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    /**
     * 转为映射表
     *
     * @return 映射表
     */
    public static Map<Short, String> toMap() {
        Map<Short, String> map = new HashMap<Short, String>();
        for (OperationType e : OperationType.values()) {
            map.put(Short.valueOf(e.getValue()), e.getName());
        }
        return map;
    }
}
