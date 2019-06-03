/**
 * @Project: IIMP
 * @Title: ApprovalStatus.java
 * @Copyright: © 2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: ApprovalStatus
 * @description: 审批状态
 * @author: Nil
 * @date: 2018年5月17日 下午6:51:59
 */
public enum ApprovalStatus {
    /**
     * 待审批
     */
    TO_APPROVE((byte) 0, "待审批"),
    /**
     * 审批通过
     */
    APPROVED((byte) 1, "审批通过"),
    /**
     * 审批计未通过
     */
    REJECTED((byte) -1, "审批未通过");

    private byte value = 0;
    private String name = "待审批";

    private ApprovalStatus(byte value, String name) {
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

    /**
     * 转为映射表
     *
     * @return 映射表
     */
    public static Map<Byte, String> toMap() {
        Map<Byte, String> map = new HashMap<Byte, String>();
        for (ApprovalStatus e : ApprovalStatus.values()) {
            map.put(e.getValue(), e.getName());
        }
        return map;
    }
}
