package com.sinosoft.ops.cimp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


@ApiModel(value = "RePagination", description = "翻页信息")
public class RePagination {
    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页码", required = true)
    @NotNull(message = "页码不能为空")
    int pageIndex;
    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页数量", required = true)
    @NotNull(message = "每页数量不能为空")
    int pageSize;

    @ApiModelProperty(value = "排序字段")
    String orderBy;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
