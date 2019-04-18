package com.sinosoft.ops.cimp.vo.to.sys.homepagecount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 首页统计项
 */
@ApiModel(description = "首页统计项")
public class HomePageCountVO {
    /**
     * 主键标识ID
     */
    @ApiModelProperty(value = "主键标识ID")
    private String id;
    /**
     * 代码标识code
     * 自定义
     */
    @ApiModelProperty(value = "代码标识code       自定义")
    private String code;
    /**
     * 统计名称
     */
    @ApiModelProperty(value = "统计名称")
    private String name;
    /**
     * 统计描述
     */
    @ApiModelProperty(value = "统计描述")
    private String description;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sortNumber;


    /***********************************/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }
}
