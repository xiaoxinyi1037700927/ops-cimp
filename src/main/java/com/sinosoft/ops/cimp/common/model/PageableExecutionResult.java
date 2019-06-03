package com.sinosoft.ops.cimp.common.model;

import java.io.Serializable;

/**
 * 可分页执行结果
 */
public class PageableExecutionResult extends ExecutionResultNoUsed implements Pageable, Serializable {
    private static final long serialVersionUID = -7416860818015261920L;
    /**
     * 当前页号
     */
    protected int pageNo = 1;
    /**
     * 每页记录数
     */
    protected int pageSize = 15;
    /**
     * 总记录数(同一查询的非首页查询附上前次返回的总记录数，以避免不必要查询)
     */
    protected long totalCount = -1;//值未-1表示需重新统计总记录数

    public PageableExecutionResult() {
    }

    public PageableExecutionResult(boolean success) {
        this.success = success;
    }

    public PageableExecutionResult(boolean success, final int count) {
        this.success = success;
        this.count = count;
    }

    public PageableExecutionResult(boolean success, final int count, final String message) {
        this.success = success;
        this.count = count;
        this.message = message;
    }

    public PageableExecutionResult(boolean success, final int count, final String message, final int pageNo, final int pageSize, final long totalCount, final Object data) {
        this.success = success;
        this.count = count;
        this.message = message;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
    }

    /**
     * 生成成功结果
     *
     * @param data 数据
     * @return 成功结果
     */
    public static PageableExecutionResult success(final int pageNo, final int pageSize, final long totalCount, final Object data) {
        return new PageableExecutionResult(true, 0, "", pageNo, pageSize, totalCount, data);
    }

    /**
     * 生成失败对象
     *
     * @param message 消息
     * @return 失败结果
     */
    public static PageableExecutionResult failure(final String message) {
        return new PageableExecutionResult(false, 0, message);
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

    @Override
    public boolean hasPreviousPage() {
        return (pageNo > 1);
    }

    @Override
    public boolean hasNextPage() {
        return (pageNo * pageSize < totalCount);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("IsSuccess=").append(success)
                .append(" Count=").append(count)
                .append(" Message=").append(message)
                .append(" PageNo=").append(pageNo)
                .append(" PageSize=").append(pageSize)
                .append(" TotalCount=").append(totalCount)
                .append(" Data=").append(data)
                .toString();
    }
}
