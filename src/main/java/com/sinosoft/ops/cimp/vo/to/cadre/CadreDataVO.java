package com.sinosoft.ops.cimp.vo.to.cadre;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    //干部列表表头
    private Map tableFields = Maps.newHashMap();

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

    public Map getTableFields() {
        return tableFields;
    }

    public void setTableFields(Map tableFields) {
        this.tableFields = tableFields;
    }
}
