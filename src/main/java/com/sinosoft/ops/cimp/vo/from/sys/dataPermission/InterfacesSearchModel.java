package com.sinosoft.ops.cimp.vo.from.sys.dataPermission;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "接口查询模型")
public class InterfacesSearchModel extends RePagination {
    /**
     * 接口类型id
     */
    @ApiModelProperty(value = "接口类型id")
    private String interfaceTypeId;
    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String name;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private String roleId;

    public String getInterfaceTypeId() {
        return interfaceTypeId;
    }

    public void setInterfaceTypeId(String interfaceTypeId) {
        this.interfaceTypeId = interfaceTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
