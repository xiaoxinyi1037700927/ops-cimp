package com.sinosoft.ops.cimp.vo.to.sys.homepagecount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * 图表数据模型
 */
@ApiModel(description = "图表数据模型")
public class ChartsDataModel {
    /**
     * 图表名称
     */
    @ApiModelProperty(value = "图表名称")
    private String name;
    /**
     * 图表数据
     */
    @ApiModelProperty(value = "图表数据")
    private List<Map<String, Object>> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
