package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 修改业务部门
 */
@ApiModel(description = "修改业务部门")
public class BusinessUnitModifyViewModel extends BusinessUnitAddViewModel {

    /**
     * 业务部门Id
     */
    @ApiModelProperty(value = "业务部门Id", required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
