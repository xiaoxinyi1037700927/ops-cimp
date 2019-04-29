package com.sinosoft.ops.cimp.vo.to.sys.check;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 统计数据项
 */
@ApiModel(description = "统计数据项")
public class SysCheckStatisticsDataItem {
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private String orgId;
    /**
     * 是否含有下级
     */
    @ApiModelProperty(value = "是否含有下级")
    private boolean hasChildren;
    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private String total;
    /**
     * 查错数
     */
    @ApiModelProperty(value = "查错数")
    private String num;
    /**
     * 单位名
     */
    @ApiModelProperty(value = "单位名")
    private String name;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
