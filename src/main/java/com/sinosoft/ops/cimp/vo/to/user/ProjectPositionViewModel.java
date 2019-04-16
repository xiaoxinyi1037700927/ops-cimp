package com.sinosoft.ops.cimp.vo.to.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Jay
 * date : 2018/9/4
 * des :
 */
@ApiModel(value = "ProjectPositionViewModel", description = "方案职位返回")
public class ProjectPositionViewModel {
    @ApiModelProperty(value = "方案职位代码")
    private String id;

    @ApiModelProperty(value = "方案代码")
    private String projectId;

    @ApiModelProperty(value = "任职单位代码")
    private String organizationId;

    @ApiModelProperty(value = "任职单位名称")
    private String organizationName;

    @ApiModelProperty(value = "任职职位名称")
    private String organizationPositionId;

    @ApiModelProperty(value = "任职职位名称")
    private String organizationPositionName;

    @ApiModelProperty(value = "人选范围")
    private String selectRange;

    @ApiModelProperty(value = "人选方式")
    private String selectType;

    @ApiModelProperty(value = "人选程序")
    private String selectProgram;

    @ApiModelProperty(value = "人选条件")
    private String selectCondition;

    @ApiModelProperty(value = "职位涉及人数", required = true)
    private String positionCadreCount;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getOrganizationPositionId() {
        return organizationPositionId;
    }

    public void setOrganizationPositionId(String organizationPositionId) {
        this.organizationPositionId = organizationPositionId;
    }

    public String getOrganizationPositionName() {
        return organizationPositionName;
    }

    public void setOrganizationPositionName(String organizationPositionName) {
        this.organizationPositionName = organizationPositionName;
    }

    public String getSelectRange() {
        return selectRange;
    }

    public void setSelectRange(String selectRange) {
        this.selectRange = selectRange;
    }

    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSelectProgram() {
        return selectProgram;
    }

    public void setSelectProgram(String selectProgram) {
        this.selectProgram = selectProgram;
    }

    public String getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(String selectCondition) {
        this.selectCondition = selectCondition;
    }

    public String getPositionCadreCount() {
        return positionCadreCount;
    }

    public void setPositionCadreCount(String positionCadreCount) {
        this.positionCadreCount = positionCadreCount;
    }
}
