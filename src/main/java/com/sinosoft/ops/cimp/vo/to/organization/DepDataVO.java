package com.sinosoft.ops.cimp.vo.to.organization;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "DepDataVO", description = "单位列表数据模型")
public class DepDataVO implements Serializable {
    private static final long serialVersionUID = -1464101414963330630L;

    //索引页
    private int pageIndex;
    //每页条数
    private int pageSize;
    //记录总数
    private long dataCount;
    //干部记录
    private List<DepVO> deps = Lists.newArrayList();

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

    public long getDataCount() {
        return dataCount;
    }

    public void setDataCount(long dataCount) {
        this.dataCount = dataCount;
    }

    public List<DepVO> getDeps() {
        return deps;
    }

    public void setDeps(List<DepVO> deps) {
        this.deps = deps;
    }
}
