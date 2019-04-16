package com.sinosoft.ops.cimp.vo.from.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApiModel(value = "UserAddViewModel", description = "新增用户参数")
public class UserAddViewModel {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "登录名", required = true)
    @NotEmpty(message = "登录名不能为空")
    private String loginName;

    @ApiModelProperty(value = "登录密码")
    private String loginPassword;

    @ApiModelProperty(value = "关联干部ID", required = true)
    @NotEmpty(message = "关联干部ID")
    private String cadreInfoId;

    @ApiModelProperty(value = "单位编号", required = true)
    @NotEmpty(message = "单位编号不能为空")
    private String organizationId;

    @ApiModelProperty(value = "数据权限单位编号", required = true)
    @NotEmpty(message = "数据权限单位编号不能为空")
    private String dataOrganizationId;

    @ApiModelProperty(value = "干部ID")
    private String reserveCadreId;

    @ApiModelProperty(value = "电话")
    private String telePhone;

    @ApiModelProperty(value = "角色类型")
    private List<String> roleCodeList = new ArrayList<>();

    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

    @ApiModelProperty(value = "描述")
    private String description;


    @ApiModelProperty(value="创建人")
    private String createId;

    @ApiModelProperty(value="创建时间")
    private Date createTime;

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

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCadreInfoId() {
        return cadreInfoId;
    }

    public void setCadreInfoId(String cadreInfoId) {
        this.cadreInfoId = cadreInfoId;
    }

    public String getReserveCadreId() {
        return reserveCadreId;
    }

    public void setReserveCadreId(String reserveCadreId) {
        this.reserveCadreId = reserveCadreId;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public List<String> getRoleCodeList() {
        return roleCodeList;
    }

    public void setRoleCodeList(List<String> roleCodeList) {
        this.roleCodeList = roleCodeList;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public String getDataOrganizationId() {
        return dataOrganizationId;
    }

    public void setDataOrganizationId(String dataOrganizationId) {
        this.dataOrganizationId = dataOrganizationId;
    }
}
