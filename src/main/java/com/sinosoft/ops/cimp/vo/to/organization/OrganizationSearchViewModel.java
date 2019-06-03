package com.sinosoft.ops.cimp.vo.to.organization;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "OrganizationSearchViewModel", description = "查询单位树")
public class OrganizationSearchViewModel extends RePagination {

    @ApiModelProperty(value = "单位名称")
    private String name;

    @ApiModelProperty(value = "单位ID")
    private String organizationId;

    @ApiModelProperty(value = "业务部门Id")
    private String businessUnitId;

    @ApiModelProperty(value = "查询类型：1全树，2.单位及其下级")
    private String queryType;

    @ApiModelProperty(value = "树权限，true带权限，false不带权限。默认带权限")
    private boolean noPermission = true;

    @ApiModelProperty(value = "数据权限id")
    private String dataOrganizationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public boolean isNoPermission() {
        return noPermission;
    }

    public void setNoPermission(boolean noPermission) {
        this.noPermission = noPermission;
    }

    public String getDataOrganizationId() {
        return dataOrganizationId;
    }

    public void setDataOrganizationId(String dataOrganizationId) {
        this.dataOrganizationId = dataOrganizationId;
    }
}
