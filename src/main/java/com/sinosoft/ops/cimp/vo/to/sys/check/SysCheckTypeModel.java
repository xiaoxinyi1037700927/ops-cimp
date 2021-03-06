package com.sinosoft.ops.cimp.vo.to.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统查错条件类型模型
 */
@ApiModel(description = "系统查错条件类型模型")
public class SysCheckTypeModel {
    /**
     * id
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
