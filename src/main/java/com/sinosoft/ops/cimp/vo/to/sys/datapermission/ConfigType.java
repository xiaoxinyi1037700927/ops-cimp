package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色数据权限sql类型
 */
public enum ConfigType {
    PRE_SQL("前置sql"),
    INJECTION_SQL("注入sql");

    private String name;

    ConfigType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> getConfigTypes() {
        return Arrays.stream(ConfigType.values()).map(ConfigType::getName).collect(Collectors.toList());
    }

}
