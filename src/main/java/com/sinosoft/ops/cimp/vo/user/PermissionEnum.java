package com.sinosoft.ops.cimp.vo.user;

/**
 * Created by Jay on 2018/12/26.
 *
 * @author Jay
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinoSoft All Rights Received
 */
public class PermissionEnum {

    public enum SYSTEM_TYPE{
        任免系统("1","任免系统"),
        监督系统("2","监督系统");

        public String code;
        public String value;

        SYSTEM_TYPE(String code, String value) {
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
