package com.sinosoft.ops.cimp.vo.to.user;

import com.sinosoft.ops.cimp.vo.user.organization.DisciplineUnitOrgViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 上级纪委信息
 */
@ApiModel(description = "上级纪委信息")
public class DisciplineUnitViewModel {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 上级纪委名称
     */
    @ApiModelProperty(value = "上级纪委名称")
    private String name;
    /**
     * 纪委关联的单位
     */
    @ApiModelProperty(value = "纪委关联的单位")
    private List<DisciplineUnitOrgViewModel> disciplineUnitOrgList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DisciplineUnitOrgViewModel> getDisciplineUnitOrgList() {
        return disciplineUnitOrgList;
    }

    public void setDisciplineUnitOrgList(List<DisciplineUnitOrgViewModel> disciplineUnitOrgList) {
        this.disciplineUnitOrgList = disciplineUnitOrgList;
    }
}
