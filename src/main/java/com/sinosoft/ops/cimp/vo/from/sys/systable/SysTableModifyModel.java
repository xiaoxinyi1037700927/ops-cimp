package com.sinosoft.ops.cimp.vo.from.sys.systable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;


@ApiModel(value = "SysEntityDefModifyModel", description = "系统表修改")
public class SysTableModifyModel extends SysTableAddModel {

    @NotBlank(message = "编号不能为空")
    @ApiModelProperty("实体编号")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
