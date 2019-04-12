package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;


@ApiModel(description = "系统应用表集合添加模型")
public class SysAppTableSetAddModel {
    /**
     * 表分组ID
     */
    @ApiModelProperty(value = "表分组ID")
    private String sysAppTableGroupId;
    /**
     * 系统表ID列表
     */
    @ApiModelProperty(value = "系统表ID列表")
    private List<String> sysTableIds;
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

    public String getSysAppTableGroupId() {
        return sysAppTableGroupId;
    }

    public void setSysAppTableGroupId(String sysAppTableGroupId) {
        this.sysAppTableGroupId = sysAppTableGroupId;
    }

    public List<String> getSysTableIds() {
        return sysTableIds;
    }

    public void setSysTableIds(List<String> sysTableIds) {
        this.sysTableIds = sysTableIds;
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
