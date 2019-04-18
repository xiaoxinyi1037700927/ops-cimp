package com.sinosoft.ops.cimp.constant;

public class RoleEnum {
    //角色登录对应的首页展示
    public enum ROLE_pageType {
        defaultPage("1", "默认首页"),
        noToDoPage("2", "无待办首页"),
        haveCountPage("3", "带统计首页");
        public String code;
        public String value;

        ROLE_pageType(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
