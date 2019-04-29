package com.sinosoft.ops.cimp.vo.to.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 查错统计数据
 */
@ApiModel(description = "查错统计数据")
public class SysCheckStatisticsData {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 类型: A(人员)、B(单位)、C(党组织人员)、D(党组织)
     */
    @ApiModelProperty(value = "查错类型")
    private String type;
    /**
     * 统计数据项
     */
    @ApiModelProperty(value = "统计数据项")
    private List<SysCheckStatisticsDataItem> items;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SysCheckStatisticsDataItem> getItems() {
        return items;
    }

    public void setItems(List<SysCheckStatisticsDataItem> items) {
        this.items = items;
    }
}
