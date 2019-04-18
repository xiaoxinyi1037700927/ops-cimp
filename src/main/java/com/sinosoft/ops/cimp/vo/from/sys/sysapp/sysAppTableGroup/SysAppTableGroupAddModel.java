package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;


@ApiModel(description = "系统应用表分组添加模型")
public class SysAppTableGroupAddModel {
    /**
     * 系统应用ID
     */
    @ApiModelProperty(value = "系统应用ID")
    private String sysAppId;
    /**
     * 表分组名称
     */
    @ApiModelProperty(value = "表分组名称")
    private String name;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public String getSysAppId() {
        return sysAppId;
    }

    public void setSysAppId(String sysAppId) {
        this.sysAppId = sysAppId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
