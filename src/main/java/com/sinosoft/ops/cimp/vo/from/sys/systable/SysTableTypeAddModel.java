package com.sinosoft.ops.cimp.vo.from.sys.systable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "SysTableTypeAddModel", description = "系统表类别添加")
public class SysTableTypeAddModel {

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "类别中文名称")
    @NotBlank(message = "类别中文名称不能为空")
    private String nameCn;

    @ApiModelProperty(value = "类别英文名称")
    @NotBlank(message = "类别英文名称不能为空")
    private String nameEn;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
