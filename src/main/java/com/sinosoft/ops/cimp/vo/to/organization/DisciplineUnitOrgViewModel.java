package com.sinosoft.ops.cimp.vo.to.organization;

import io.swagger.annotations.ApiModelProperty;

@io.swagger.annotations.ApiModel(description = "DisciplineUnitOrgViewModel")

public class DisciplineUnitOrgViewModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 纪委部门Id
     */
    @ApiModelProperty(value = "纪委部门Id")
    private String disciplineUnitId;
    /**
     * 业务部门名称
     */
    @ApiModelProperty(value = "业务部门名称")
    private String disciplineUnitName;
    /**
     * 单位Id
     */
    @ApiModelProperty(value = "单位Id")
    private String organizationId;
    /**
     * 关联单位名称
     */
    @ApiModelProperty(value = "关联单位名称")
    private String organizationName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisciplineUnitId() {
        return disciplineUnitId;
    }

    public void setDisciplineUnitId(String disciplineUnitId) {
        this.disciplineUnitId = disciplineUnitId;
    }

    public String getDisciplineUnitName() {
        return disciplineUnitName;
    }

    public void setDisciplineUnitName(String disciplineUnitName) {
        this.disciplineUnitName = disciplineUnitName;
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
}
