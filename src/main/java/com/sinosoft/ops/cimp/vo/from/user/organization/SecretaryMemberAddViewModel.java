package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 新增书记会成员视图
 */
@ApiModel(description = "新增书记会成员视图")
public class SecretaryMemberAddViewModel {

    /**
     * 单位ID
     */
    @ApiModelProperty(value = "单位ID")
    private String organizationId;
    /**
     * 干部ID
     */
    @ApiModelProperty(value = "干部ID")
    private String cadreInfoId;

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
}
