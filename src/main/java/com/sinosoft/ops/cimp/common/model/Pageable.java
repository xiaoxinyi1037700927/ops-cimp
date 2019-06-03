package com.sinosoft.ops.cimp.common.model;

/**
 * 可分页
 */
public interface Pageable {
    /**
     * 获取当前页页号
     *
     * @return 当前页页号
     */
    public int getPageNo();

    /**
     * 获取每页记录数
     *
     * @return 每页记录数
     */
    public int getPageSize();

    /**
     * 获取总记录数
     * （值为-1表示需重新统计总记录数）
     *
     * @return 总记录数
     */
    public long getTotalCount();

    /**
     * 获取是否还有上一页
     *
     * @return 是否还有上一页
     */
    public boolean hasPreviousPage();

    /**
     * 获取是否还有下一页
     *
     * @return 是否还有下一页
     */
    public boolean hasNextPage();
}
