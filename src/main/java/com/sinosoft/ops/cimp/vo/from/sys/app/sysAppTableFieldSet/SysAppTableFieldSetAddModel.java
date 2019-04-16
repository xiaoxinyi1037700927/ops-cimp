package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;


@ApiModel(description = "系统应用表字段集合添加模型")
public class SysAppTableFieldSetAddModel {
    /**
     * 字段分组ID
     */
    @ApiModelProperty(value = "字段分组ID")
    private String sysAppTableFieldGroupId;
    /**
     * 系统表字段ID列表
     */
    @ApiModelProperty(value = "系统表字段ID列表")
    private List<String> sysTableFieldIds;
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

    public String getSysAppTableFieldGroupId() {
        return sysAppTableFieldGroupId;
    }

    public void setSysAppTableFieldGroupId(String sysAppTableFieldGroupId) {
        this.sysAppTableFieldGroupId = sysAppTableFieldGroupId;
    }

    public List<String> getSysTableFieldIds() {
        return sysTableFieldIds;
    }

    public void setSysTableFieldIds(List<String> sysTableFieldIds) {
        this.sysTableFieldIds = sysTableFieldIds;
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
