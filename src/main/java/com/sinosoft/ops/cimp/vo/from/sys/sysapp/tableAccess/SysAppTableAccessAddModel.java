package com.sinosoft.ops.cimp.vo.from.sys.sysapp.tableAccess;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@ApiModel(description = "角色对表访问权限添加模型")
public class SysAppTableAccessAddModel {
    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", required = true)
    @NotNull(message = "角色ID不能为空")
    private String roleId;
    /**
     * 系统应用ID
     */
    @ApiModelProperty(value = "系统应用ID")
    private String sysAppId;
    /**
     * 系统应用表ID列表
     */
    @ApiModelProperty(value = "系统应用表ID列表")
    private List<String> SysAppTableSetIds;
    /**
     * 是否能读取表中所有字段
     */
    @ApiModelProperty(value = "是否能读取表中所有字段")
    private boolean canReadAll;
    /**
     * 是否能修改表中所有字段
     */
    @ApiModelProperty(value = "是否能修改表中所有字段")
    private boolean canWriteAll;
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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getSysAppId() {
        return sysAppId;
    }

    public void setSysAppId(String sysAppId) {
        this.sysAppId = sysAppId;
    }

    public List<String> getSysAppTableSetIds() {
        return SysAppTableSetIds;
    }

    public void setSysAppTableSetIds(List<String> sysAppTableSetIds) {
        SysAppTableSetIds = sysAppTableSetIds;
    }

    public boolean isCanReadAll() {
        return canReadAll;
    }

    public void setCanReadAll(boolean canReadAll) {
        this.canReadAll = canReadAll;
    }

    public boolean isCanWriteAll() {
        return canWriteAll;
    }

    public void setCanWriteAll(boolean canWriteAll) {
        this.canWriteAll = canWriteAll;
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
