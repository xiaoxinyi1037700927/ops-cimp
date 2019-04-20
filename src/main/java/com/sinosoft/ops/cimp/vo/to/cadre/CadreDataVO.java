package com.sinosoft.ops.cimp.vo.to.cadre;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "CadreDataVO", description = "干部列表数据模型")
public class CadreDataVO implements Serializable {
    private static final long serialVersionUID = -1464101414963330630L;

    //索引页
    private int pageIndex;
    //每页条数
    private int pageSize;
    //记录总数
    private long dataCount;
    //干部记录
    private List<CadreVO> cadres = Lists.newArrayList();

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

    public List<CadreVO> getCadres() {
        return cadres;
    }

    public void setCadres(List<CadreVO> cadres) {
        this.cadres = cadres;
    }
}
