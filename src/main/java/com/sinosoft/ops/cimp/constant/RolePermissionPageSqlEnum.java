package com.sinosoft.ops.cimp.constant;

public class RolePermissionPageSqlEnum {

    public enum NAME_EN{
        干部集合(0,"cadreList"),
        单位集合(1,"departmentList");

        public Integer code;
        public String value;

        NAME_EN(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }



}
