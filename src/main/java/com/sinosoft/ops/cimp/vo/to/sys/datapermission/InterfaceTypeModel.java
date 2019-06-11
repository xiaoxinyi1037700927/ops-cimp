package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "接口类型模型")
public class InterfaceTypeModel {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
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
