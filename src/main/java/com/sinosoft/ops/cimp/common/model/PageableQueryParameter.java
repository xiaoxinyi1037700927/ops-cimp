package com.sinosoft.ops.cimp.common.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 可分页的SQL查询参数
 */
public class PageableQueryParameter implements Pageable, QueryParameter {
    /***当前页号*/
    protected int pageNo = 1;
    /***每页记录数*/
    protected int pageSize = 20;
    /**
     * 总记录数(同一查询的非首页查询附上前次返回的总记录数，以避免不必要查询)
     */
    protected long totalCount = -1;//值为-1表示需重新统计总记录数
    /**
     * 查询条件
     */
    protected AbstractCondition condition = new ConditionGroup();
    /***查询参数*/
    protected Map<String, Object> parameters = new HashMap<String, Object>();
    /***排序（字段名，ASC/DESC）*/
    protected Map<String, String> orderBys = new HashMap<String, String>();
    /***分组字段*/
    protected Collection<String> groupBys = new ArrayList<String>();

    public PageableQueryParameter() {
    }

    public PageableQueryParameter(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageableQueryParameter(int pageNo, int pageSize, Map<String, Object> parameters) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.parameters.putAll(parameters);
    }

    public PageableQueryParameter(int pageNo, int pageSize, Map<String, Object> parameters, Map<String, String> orderBys) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.parameters.putAll(parameters);
        this.orderBys.putAll(orderBys);
    }

    public PageableQueryParameter(int pageNo, int pageSize, long totalCount, Map<String, Object> parameters, Map<String, String> orderBys, Collection<String> groupBys) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.parameters.putAll(parameters);
        this.orderBys.putAll(orderBys);
        this.groupBys.addAll(groupBys);
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
    public AbstractCondition getCondition() {
        return condition;
    }

    public void setCondition(AbstractCondition condition) {
        this.condition = condition;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Map<String, String> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(Map<String, String> orderBys) {
        this.orderBys = orderBys;
    }

    @Override
    public Collection<String> getGroupBys() {
        return groupBys;
    }

    public void setGroupBys(Collection<String> groupBys) {
        this.groupBys = groupBys;
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
