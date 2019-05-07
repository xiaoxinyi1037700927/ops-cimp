package com.sinosoft.ops.cimp.vo.from.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统查错查询模型")
public class SysCheckSearchModel {

    /**
     * true:错误，false：全部
     */
    @ApiModelProperty(value = "是否仅显示含错误项")
    private boolean wrongOnly;

    public boolean isWrongOnly() {
        return wrongOnly;
    }

    public void setWrongOnly(boolean wrongOnly) {
        this.wrongOnly = wrongOnly;
    }
}
