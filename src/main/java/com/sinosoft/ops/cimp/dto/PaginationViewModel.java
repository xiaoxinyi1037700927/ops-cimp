package com.sinosoft.ops.cimp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(value = "PaginationViewModel", description = "数据集")
public class PaginationViewModel<T> {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页", required = true)
    int pageIndex;
    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", required = true)
    int pageSize;
    /**
     * 总数据条数
     */
    @ApiModelProperty(value = "总数据条数", required = true)
    long totalCount;

    List<T> data;

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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static class Builder<T> {
        private PaginationViewModel<T> model = new PaginationViewModel<>();

        public Builder<T> pageIndex(int pageIndex) {
            model.setPageIndex(pageIndex);
            return this;
        }

        public Builder<T> pageSize(int pageSize) {
            model.setPageSize(pageSize);
            return this;
        }

        public Builder<T> totalCount(long totalCount) {
            model.setTotalCount(totalCount);
            return this;
        }

        public Builder<T> data(List<T> data) {
            model.setData(data);
            return this;
        }

        public PaginationViewModel<T> build() {
            return model;
        }
    }

}
