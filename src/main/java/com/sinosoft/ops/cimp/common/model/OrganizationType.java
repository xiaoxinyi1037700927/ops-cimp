/**
 * @project: iimp-gradle
 * @title: OrganizationType.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: OrganizationType
 * @description: 机构类型
 * @author: Ni
 * @date: 2018年6月6日 下午3:22:41
 * @since JDK 1.7
 */
public enum OrganizationType {
    /**
     * 单位
     */
    UNIT("UNIT", "单位"),
    /**
     * 单位
     */
    UNIT_UNLIMITED("UNIT_UNLIMITED", "无限制的单位"),
    /**
     * 党组织
     */
    PARTY("PARTY", "党组织"),
    /**
     * 无限制的党组织
     */
    PARTY_UNLIMITED("PARTY_UNLIMITED", "无限制的党组织");

    private final String value;
    private final String name;

    OrganizationType(final String value, final String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
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
    public static Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        for (OrganizationType e : OrganizationType.values()) {
            map.put(e.getValue(), e.getName());
        }
        return map;
    }
}
