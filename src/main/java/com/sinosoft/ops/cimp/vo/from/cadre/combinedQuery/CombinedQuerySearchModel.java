package com.sinosoft.ops.cimp.vo.from.cadre.combinedQuery;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "组合查询搜索模型")
public class CombinedQuerySearchModel extends RePagination {
    /**
     * 组合查询名
     */
    @ApiModelProperty(value = "组合查询名")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
