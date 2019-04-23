package com.sinosoft.ops.cimp.vo.from.sys.sysapp.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@ApiModel(description = "角色对表字段访问权限添加模型")
public class SysAppFieldAccessAddModel {
    /**
     * 系统应用中角色对表的访问权限id
     */
    @ApiModelProperty(value = "系统应用中角色对表的访问权限id", required = true)
    @NotNull(message = "系统应用中角色对表的访问权限id不能为空")
    private String sysAppRoleTableAccessId;
    /**
     * 系统应用表字段ID列表
     */
    @ApiModelProperty(value = "系统应用表字段ID列表", required = true)
    @NotNull(message = "系统应用表字段ID列表不能为空")
    private List<String> sysAppTableFieldSetIds;
    /**
     * 是否能读取字段
     */
    @ApiModelProperty(value = "是否能读字段")
    private boolean canRead;
    /**
     * 是否能修改字段
     */
    @ApiModelProperty(value = "是否能修改字段")
    private boolean canWrite;
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

    public String getSysAppRoleTableAccessId() {
        return sysAppRoleTableAccessId;
    }

    public void setSysAppRoleTableAccessId(String sysAppRoleTableAccessId) {
        this.sysAppRoleTableAccessId = sysAppRoleTableAccessId;
    }

    public List<String> getSysAppTableFieldSetIds() {
        return sysAppTableFieldSetIds;
    }

    public void setSysAppTableFieldSetIds(List<String> sysAppTableFieldSetIds) {
        this.sysAppTableFieldSetIds = sysAppTableFieldSetIds;
    }

    public boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
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
