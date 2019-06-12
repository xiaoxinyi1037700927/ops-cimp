package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色数据权限sql类型
 */
public enum ConfigType {
    PRE_SQL("preSql", "前置sql"),
    INJECTION_SQL("injectionSql", "注入sql");

    private String code;
    private String name;

    ConfigType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName(String code) {
        for (ConfigType configType : ConfigType.values()) {
            if (configType.getCode().equals(code)) {
                return configType.getName();
            }
        }
        return "";
    }

    public static List<Map<String, Object>> getConfigTypes() {
        return Arrays.stream(ConfigType.values()).map(type -> {
            Map<String, Object> map = new HashMap<>();
            map.put("code", type.getCode());
            map.put("name", type.getName());
            return map;
        }).collect(Collectors.toList());
    }

}
