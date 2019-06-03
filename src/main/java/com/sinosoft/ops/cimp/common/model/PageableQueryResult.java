package com.sinosoft.ops.cimp.common.model;

import java.io.Serializable;

/**
 * 可分页的SQL查询结果
 */
public class PageableQueryResult implements Pageable, Serializable {
    private static final long serialVersionUID = -575469706275108567L;
    /***当前页号*/
    protected int pageNo = 1;
    /***每页记录数*/
    protected int pageSize = 20;
    /**
     * 总记录数(同一查询的非首页查询附上前次返回的总记录数，以避免不必要查询)
     */
    protected long totalCount = -1;//值为-1表示需重新统计总记录数
    /*** 数据集 */
    protected Object data = null;

    public PageableQueryResult() {
    }

    public PageableQueryResult(final int pageNo, final int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageableQueryResult(final int pageNo, final int pageSize, final long totalCount, Object data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
    }

    @Override
    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean hasPreviousPage() {
        return (pageNo > 1);
    }

    @Override
    public boolean hasNextPage() {
        return (pageNo * pageSize < totalCount);
    }
}
