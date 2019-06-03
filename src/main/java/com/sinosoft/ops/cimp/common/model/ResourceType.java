/**
 * @project: iimp-gradle
 * @title: ResourceType.java
 * @copyright: ©2018 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName: ResourceType
 * @description: 资源类型
 * @author: Ni
 * @date: 2018年6月6日 下午3:22:41
 * @since JDK 1.7
 */
public enum ResourceType {
    /*** 未知 */
    UNKNOWN((short) 0, "未知"),

    /*** 应用 */
    APP((short) 1, "应用"),
    /*** 数据源 */
    DATA_SOURCE((short) 2, "数据源"),
    /*** 信息集 */
    INFO_SET((short) 3, "信息集"),
    /*** 信息项 */
    INFO_ITEM((short) 4, "信息项"),

    /*** 菜单项 */
    MENU_ITEM((short) 5, "菜单项");

    private final short value;
    private final String name;

    ResourceType(final short value, final String name) {
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
        for (ResourceType e : ResourceType.values()) {
            map.put(Short.valueOf(e.getValue()), e.getName());
        }
        return map;
    }
}
