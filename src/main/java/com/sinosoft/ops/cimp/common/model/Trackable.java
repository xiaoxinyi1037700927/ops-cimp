/**
 * @project: IIMP
 * @title: Trackable.java
 * @copyright: ©2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @version 1.0.0
 * @ClassName: Trackable
 * @description: 可追踪（数据中包含创建人、创建时间、最后修改人、修改时间等信息）
 * @author: Nil
 * @date: 2017年11月25日 下午10:57:31
 * @since JDK 1.7
 */
public interface Trackable {
    /**
     * 获取创建时间
     *
     * @return 创建时间
     * @author Nil
     * @since JDK 1.7
     */
    Timestamp getCreatedTime();

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     * @author Nil
     * @since JDK 1.7
     */
    void setCreatedTime(Timestamp createdTime);

    /**
     * 获取创建人
     *
     * @return 创建人
     * @author Nil
     * @since JDK 1.7
     */
    UUID getCreatedBy();

    /**
     * 设置创建人
     *
     * @param createdBy 创建人
     * @author Nil
     * @since JDK 1.7
     */
    void setCreatedBy(UUID createdBy);

    /**
     * 获取最后修改时间
     *
     * @return 最后修改时间
     * @author Nil
     * @since JDK 1.7
     */
    Timestamp getLastModifiedTime();

    /**
     * 设置最后修改时间
     *
     * @param lastModifiedTime 最后修改时间
     * @author Nil
     * @since JDK 1.7
     */
    void setLastModifiedTime(Timestamp lastModifiedTime);

    /**
     * 获取最后修改人
     *
     * @return 最后修改人
     * @author Nil
     * @since JDK 1.7
     */
    UUID getLastModifiedBy();

    /**
     * 设置最后修改人
     *
     * @param lastModifiedBy 最后修改人
     * @author Nil
     * @since JDK 1.7
     */
    void setLastModifiedBy(UUID lastModifiedBy);

    /**
     * 获取数据状态
     *
     * @return 数据状态
     * @author Nil
     * @since JDK 1.7
     */
    Byte getStatus();

    /**
     * 设置数据状态
     *
     * @param status 数据状态
     * @author Nil
     * @since JDK 1.7
     */
    void setStatus(Byte status);
}
