package com.sinosoft.ops.cimp.vo.from.sys.systable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysTableTypeModifyModel",description = "系统表类别修改")
public class SysTableTypeModifyModel extends SysTableTypeAddModel {

    @ApiModelProperty(value = "系统表类别编号")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
