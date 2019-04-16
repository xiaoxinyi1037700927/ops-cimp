package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 修改上级纪委部门
 */
@ApiModel(description = "修改上级纪委部门")
public class DisciplineUnitModifyViewModel extends DisciplineUnitAddViewModel {

    /**
     * 修改纪委部门Id
     */
    @ApiModelProperty(value = "修改纪委部门Id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
