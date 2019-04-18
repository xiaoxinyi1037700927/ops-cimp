package com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统表类型集合模型")
public class SysAppTableTypeModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 类别名称
     */
    @ApiModelProperty(value = "类别名称")
    private String nameCn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
