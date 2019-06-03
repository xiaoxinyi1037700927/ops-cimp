package com.sinosoft.ops.cimp.common.model;

/**
 * 数据状态
 *
 * @author Nil
 * @version 1.0
 */
public enum DataStatus {
    /**
     * 无效
     */
    INVALID((byte) -1, "无效"),
    /**
     * 正常
     */
    NORMAL((byte) 0, "正常"),
    /**
     * 暂存
     */
    TEMPORAL((byte) 6, "暂存"),
    /**
     * 逻辑删除
     */
    DELETED((byte) 9, "逻辑删除"),
    /**
     * 待审批
     */
    TO_APPROVE((byte) 11, "待审批"),
    /**
     * 已批准
     */
    APPROVED_PASSED((byte) 12, "已批准"),
    /**
     * 未获批准
     */
    APPROVED_NOT_APPROVED((byte) 13, "未获批准"),
    /**
     * 待审核
     */
    TO_AUDIT((byte) 21, "待审核"),
    /**
     * 审核通过
     */
    AUDIT_PASSED((byte) 22, "审核通过"),
    /**
     * 审核未通过
     */
    AUDIT_NOT_PASSED((byte) 23, "审核未通过");

    private byte value = 0;
    private String name = "正常";

    private DataStatus(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 获取value的值
     *
     * @return value
     */
    public byte getValue() {
        return value;
    }

    /**
     * 获取name的值
     *
     * @return name
     */
    public String getName() {
        return name;
    }
}
