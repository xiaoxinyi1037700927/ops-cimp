package com.sinosoft.ops.cimp.vo.to.sys.homepagecount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 首页统计项-查询结果集
 */
@ApiModel(description = "首页统计项-查询结果集")
public class HomePageCountQueryVO {
    /**
     * 主键标识ID
     */
    @ApiModelProperty(value = "主键标识ID")
    private String id;

    /**
     * 统计名称
     */
    @ApiModelProperty(value = "统计名称")
    private String countName;


    /***********************************/
    /**
     * 查询汇总值
     */
    @ApiModelProperty(value = "查询汇总值")
    private long countNumber;

    /***********************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(long countNumber) {
        this.countNumber = countNumber;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }


}
