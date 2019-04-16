package com.sinosoft.ops.cimp.vo.user;

public class RoleEnum {
    //角色信息
    public enum ROLE_INFO {
        干部科室("10", "干部科室"),
        考察组("20", "考察组"),
        档案室("30", "档案室"),
        干部监督科("40", "干部监督科"),
        纪委("50","纪委");
        public String code;
        public String value;

        ROLE_INFO(String code, String value) {
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
