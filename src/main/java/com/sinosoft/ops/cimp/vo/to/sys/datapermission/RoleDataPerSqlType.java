package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

/**
 * 角色数据权限sql类型
 */
public enum RoleDataPerSqlType {
    PRE_SQL("1", "前置sql"),
    INJECTION_SQL("2", "注入sql");

    private String type;

    private String name;

    RoleDataPerSqlType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getName(String type) {
        for (RoleDataPerSqlType sqlType : RoleDataPerSqlType.values()) {
            if (sqlType.getType().equals(type)) {
                return sqlType.getName();
            }
        }
        return "";
    }
}
