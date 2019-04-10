package com.sinosoft.ops.cimp.vo.from.table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysTableFieldAddModel",description = "系统表字段修改")
public class SysTableFieldModifyModel extends SysTableFieldAddModel {

    @ApiModelProperty(value = "主键编号")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
