package com.sinosoft.ops.cimp.vo.from.user.organization;


import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModelProperty;

@io.swagger.annotations.ApiModel(description = "SecretaryMemberSearchViewModel")

public class SecretaryMemberSearchViewModel extends RePagination {

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String organizationName;
    /**
     * 干部姓名
     */
    @ApiModelProperty(value = "干部姓名")
    private String cadreInfoName;

    /**
     * 机构ID
     */
    @ApiModelProperty(value = "机构ID")
    private String organizationId;

    /**
     * 包含下级
     */
    @ApiModelProperty(value = "包含下级")
    private String contain;



    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCadreInfoName() {
        return cadreInfoName;
    }

    public void setCadreInfoName(String cadreInfoName) {
        this.cadreInfoName = cadreInfoName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getContain() {
        return contain;
    }

    public void setContain(String contain) {
        this.contain = contain;
    }
}
