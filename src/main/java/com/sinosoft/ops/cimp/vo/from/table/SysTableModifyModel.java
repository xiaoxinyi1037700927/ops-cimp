package com.sinosoft.ops.cimp.vo.from.table;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


@ApiModel(value = "SysEntityDefModifyModel", description = "系统表修改")
public class SysTableModifyModel extends SysTableAddModel {

    @NotNull(message = "编号不能为空")
    @ApiModelProperty("实体编号")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
