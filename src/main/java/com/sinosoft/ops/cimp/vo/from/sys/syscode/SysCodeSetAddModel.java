package com.sinosoft.ops.cimp.vo.from.sys.syscode;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "SysCodeSetAddModel",description = "代码集添加")
public class SysCodeSetAddModel {

    @ApiModelProperty(value = "名称")
    private String name;

    @NotBlank(message = "中文名称不能为空")
    @ApiModelProperty(value = "中文名称")
    private String nameCn;

    @ApiModelProperty(value = "代码最大长度")
    private Integer maxCodeLength;

    @ApiModelProperty(value = "类型（国家标准、行业标准、企业标准等）")
    private Integer type;

    @ApiModelProperty(value = "是否层次结构代码(树形)")
    private Integer hierarchical;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "次序")
    private Integer ordinal;

    @ApiModelProperty(value = "应用标识")
    private String appId;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "叶")
    private Integer leaf;

    @ApiModelProperty(value = "父级编号")
    private Integer parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public Integer getMaxCodeLength() {
        return maxCodeLength;
    }

    public void setMaxCodeLength(Integer maxCodeLength) {
        this.maxCodeLength = maxCodeLength;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getHierarchical() {
        return hierarchical;
    }

    public void setHierarchical(Integer hierarchical) {
        this.hierarchical = hierarchical;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
