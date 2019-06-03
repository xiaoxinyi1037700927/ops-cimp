/**
 * @project: IIMP
 * @title: Approvable.java
 * @copyright: ©2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @version 1.0.0
 * @ClassName: Approvable
 * @description: 可审批（数据中包含审批状态、审批时间、审批人等）
 * @author: Nil
 * @date: 2018年4月5日 下午10:57:31
 * @since JDK 1.7
 */
public interface Approvable {
    /**
     * 获取审批状态
     *
     * @return 审批状态
     */
    Byte getApprovalStatus();

    /**
     * 设置审批状态
     *
     * @param approvalStatus 审批状态
     */
    void setApprovalStatus(Byte approvalStatus);

    /**
     * 获取审批时间
     *
     * @return 审批时间
     */
    Timestamp getApprovedTime();

    /**
     * 设置审批时间
     *
     * @param approvedTime 审批时间
     */
    void setApprovedTime(Timestamp approvedTime);

    /**
     * 获取审批人
     *
     * @return 审批人
     */
    UUID getApprovedBy();

    /**
     * 设置审批人
     *
     * @param approvedBy 审批人
     */
    void setApprovedBy(UUID approvedBy);
}