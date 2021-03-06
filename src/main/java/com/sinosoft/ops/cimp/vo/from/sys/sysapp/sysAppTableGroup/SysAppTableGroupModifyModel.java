package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "系统应用表分组修改模型")
public class SysAppTableGroupModifyModel {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 表分组名称
     */
    @ApiModelProperty(value = "表分组名称")
    private String name;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

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

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
