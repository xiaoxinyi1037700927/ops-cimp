package com.sinosoft.ops.cimp.vo.to.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 业务部门关联单位列表
 */
@ApiModel(description = "业务部门关联单位列表")
public class BusinessUnitOrgListViewModel {
    /**
     * 单位Id
     */
    @ApiModelProperty(value = "单位Id")
    private String organizationId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String organizationName;

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
}
