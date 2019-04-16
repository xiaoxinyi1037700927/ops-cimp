package com.sinosoft.ops.cimp.vo.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 书记会成员返回视图
 */
@ApiModel(description = "书记会成员返回视图")
public class SecretaryMemberViewModel {

    /**
     * 单位书记会干部ID
     */
    @ApiModelProperty(value = "单位书记会干部ID")
    private String id;

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private String organizationId;
    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String organizationName;
    /**
     * 干部ID
     */
    @ApiModelProperty(value = "干部ID")
    private String cadreInfoId;
    /**
     * 干部名称
     */
    @ApiModelProperty(value = "干部名称")
    private String cadreInfoName;

    /**
     * 干部职务
     */
    @ApiModelProperty(value = "干部职务")
    private String duty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCadreInfoId() {
        return cadreInfoId;
    }

    public void setCadreInfoId(String cadreInfoId) {
        this.cadreInfoId = cadreInfoId;
    }

    public String getCadreInfoName() {
        return cadreInfoName;
    }

    public void setCadreInfoName(String cadreInfoName) {
        this.cadreInfoName = cadreInfoName;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
