package com.sinosoft.ops.cimp.vo.from.user.organization;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 上级纪委查询
 */
@ApiModel(description = "上级纪委查询")
public class DisciplineUnitSearchViewModel extends RePagination {

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
