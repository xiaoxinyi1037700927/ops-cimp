package com.sinosoft.ops.cimp.vo.to.cadre;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CadreSearchVO implements Serializable {
    private static final long serialVersionUID = 4081432285017945403L;

    //单位id
    private String deptId;
    //搜索人员标签
    private List<String> cadreTagIds = Lists.newArrayList();
    //信息集添加的条件
    private Map<String, Object> tableConditions = Maps.newHashMap();
    //分页开始索引
    private String startIndex;
    //分页结束索引
    private String endIndex;
    //项目编号
    private String appCode;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public List<String> getCadreTagIds() {
        return cadreTagIds;
    }

    public void setCadreTagIds(List<String> cadreTagIds) {
        this.cadreTagIds = cadreTagIds;
    }

    public Map<String, Object> getTableConditions() {
        return tableConditions;
    }

    public void setTableConditions(Map<String, Object> tableConditions) {
        this.tableConditions = tableConditions;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }
}
