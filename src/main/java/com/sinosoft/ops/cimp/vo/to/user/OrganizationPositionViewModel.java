package com.sinosoft.ops.cimp.vo.to.user;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "OrganizationPositionViewModel", description = "单位职务信息")
public class OrganizationPositionViewModel {

    @ApiModelProperty(value = "班子职务ID")
    private String id;

    @ApiModelProperty(value = "方案职务ID")
    private String projectPositionId;

    @ApiModelProperty(value = "机构ID")
    private String organizationId;

    @ApiModelProperty(value = "职务名称")
    private String name;

    @ApiModelProperty(value = "职务代码")
    private String code;

    @ApiModelProperty(value = "职数")
    private Integer positionQuota;

    @ApiModelProperty(value = "职级ID")
    private String rankId;

    @ApiModelProperty(value = "职级名称")
    private String rankName;

    @ApiModelProperty(value = "职级代码")
    private String rankCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectPositionId() {
        return projectPositionId;
    }

    public void setProjectPositionId(String projectPositionId) {
        this.projectPositionId = projectPositionId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPositionQuota() {
        return positionQuota;
    }

    public void setPositionQuota(Integer positionQuota) {
        this.positionQuota = positionQuota;
    }

    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }
}
