package com.sinosoft.ops.cimp.vo.from.sys.sysapp.access;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


@ApiModel(description = "角色对表字段访问权限查询模型")
public class SysAppFieldAccessSearchModel extends RePagination {
    /**
     * 系统应用中角色对表的访问权限id
     */
    @ApiModelProperty(value = "系统应用中角色对表的访问权限id", required = true)
    @NotNull(message = "系统应用中角色对表的访问权限id不能为空")
    private String sysAppRoleTableAccessId;
    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名")
    private String name;

    public String getSysAppRoleTableAccessId() {
        return sysAppRoleTableAccessId;
    }

    public void setSysAppRoleTableAccessId(String sysAppRoleTableAccessId) {
        this.sysAppRoleTableAccessId = sysAppRoleTableAccessId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
