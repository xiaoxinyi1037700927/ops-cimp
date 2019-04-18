package com.sinosoft.ops.cimp.vo.from.user;


import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UserSearchViewModel", description = "用户搜索")
public class UserSearchViewModel extends RePagination {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "登录名")
    private String loginName;

    @ApiModelProperty(value = "角色类型")
    private String roleType;

    @ApiModelProperty(value = "机构代码")
    private String organizationCode;


    @ApiModelProperty(value = "是否包含下级 1包含 0不包含")
    private String contain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }


    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }
}
