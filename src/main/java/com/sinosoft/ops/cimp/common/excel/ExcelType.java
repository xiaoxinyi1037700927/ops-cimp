package com.sinosoft.ops.cimp.common.excel;

/**
 * Created by rain chen on 2017/8/7.
 *
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public enum ExcelType {
    NUMBER(0), STRING(1);
    private int type;

    ExcelType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
