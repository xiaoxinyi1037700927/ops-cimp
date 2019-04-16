package com.sinosoft.ops.cimp.vo.user;

import com.sinosoft.ops.cimp.entity.sys.user.Role;
import com.sinosoft.ops.cimp.vo.user.role.MenuParentViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel(value = "UserViewModel", description = "用户登录后返回")
public class UserViewModel {
    /**
     * 主键标识 ID
     */
    @ApiModelProperty(value = "主键标识 ID")
    private String id;

    /**
     * 干部ID
     */
    @ApiModelProperty(value = "干部ID")
    private String cadreInfoId;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名")
    private String loginName;

    /**
     * 所属机构标识ID
     */
    @ApiModelProperty(value = "所属机构标识ID")
    private String organizationId;

    @ApiModelProperty(value = "所属机构code标识")
    private String organizationCode;


    @ApiModelProperty(value = "用户单位名称")
    private String organizationName;

    /**
     * 数据权限机构标识ID
     */
    @ApiModelProperty(value = "数据权限机构标识ID")
    private String dataOrganizationId;

    @ApiModelProperty(value = "数据权限单位名称")
    private String dataOrganizationName;

    @ApiModelProperty(value = "账号所属单位ID")
    private String ownerOrganizationId;

    @ApiModelProperty(value = "账号所属单位code")
    private String ownerOrganizationCode;

    @ApiModelProperty(value = "账号所属单位名称")
    private String ownerOrganizationName;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String telePhone;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;
    /**
     * 最后登录IP
     */
    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIP;
    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;
    /**
     * 是否被锁定(1 否, 0 是)
     */
    @ApiModelProperty(value = "是否被锁定(1 否, 0 是)")
    private Boolean locked;
    /**
     * 锁定时间
     */
    @ApiModelProperty(value = "锁定时间")
    private Date lockTime;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 用户状态
     */
    @ApiModelProperty(value = "用户状态")
    private Integer status;

    @ApiModelProperty(value = "登陆后的token")
    private String accessToken;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "角色")
    private List<Role> roleList = new ArrayList<>();

    @ApiModelProperty(value = "用户拥有的角色")
    private List<RoleViewModel> roleViewModelList = new ArrayList<>();

    @ApiModelProperty(value = "菜单集合")
    private List<MenuParentViewModel> menuList;

    @ApiModelProperty(value = "是否拥有首页统计项")
    private boolean haveCount;

    @ApiModelProperty(value = "是否拥有下级单位")
    private boolean haveChildrenOrg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCadreInfoId() {
        return cadreInfoId;
    }

    public void setCadreInfoId(String cadreInfoId) {
        this.cadreInfoId = cadreInfoId;
    }

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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<RoleViewModel> getRoleViewModelList() {
        return roleViewModelList;
    }

    public void setRoleViewModelList(List<RoleViewModel> roleViewModelList) {
        this.roleViewModelList = roleViewModelList;
    }

    public List<MenuParentViewModel> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuParentViewModel> menuList) {
        this.menuList = menuList;
    }

    public boolean isHaveCount() {
        return haveCount;
    }

    public void setHaveCount(boolean haveCount) {
        this.haveCount = haveCount;
    }

    public String getDataOrganizationId() {
        return dataOrganizationId;
    }

    public void setDataOrganizationId(String dataOrganizationId) {
        this.dataOrganizationId = dataOrganizationId;
    }

    public String getDataOrganizationName() {
        return dataOrganizationName;
    }

    public void setDataOrganizationName(String dataOrganizationName) {
        this.dataOrganizationName = dataOrganizationName;
    }

    public boolean isHaveChildrenOrg() {
        return haveChildrenOrg;
    }

    public void setHaveChildrenOrg(boolean haveChildrenOrg) {
        this.haveChildrenOrg = haveChildrenOrg;
    }

    public String getOwnerOrganizationId() {
        return ownerOrganizationId;
    }

    public void setOwnerOrganizationId(String ownerOrganizationId) {
        this.ownerOrganizationId = ownerOrganizationId;
    }

    public String getOwnerOrganizationCode() {
        return ownerOrganizationCode;
    }

    public void setOwnerOrganizationCode(String ownerOrganizationCode) {
        this.ownerOrganizationCode = ownerOrganizationCode;
    }

    public String getOwnerOrganizationName() {
        return ownerOrganizationName;
    }

    public void setOwnerOrganizationName(String ownerOrganizationName) {
        this.ownerOrganizationName = ownerOrganizationName;
    }
}