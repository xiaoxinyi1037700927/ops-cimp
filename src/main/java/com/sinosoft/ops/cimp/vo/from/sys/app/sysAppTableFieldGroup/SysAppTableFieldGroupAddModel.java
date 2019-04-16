package com.sinosoft.ops.cimp.vo.from.sys.app.sysAppTableFieldGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@ApiModel(description = "系统应用表字段分组添加模型")
public class SysAppTableFieldGroupAddModel {
    /**
     * 系统应用表集合ID
     */
    @ApiModelProperty(value = "系统应用表集合ID")
    private String sysAppTableSetId;
    /**
     * 字段分组名称
     */
    @ApiModelProperty(value = "字段分组名称")
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

    public String getSysAppTableSetId() {
        return sysAppTableSetId;
    }

    public void setSysAppTableSetId(String sysAppTableSetId) {
        this.sysAppTableSetId = sysAppTableSetId;
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
