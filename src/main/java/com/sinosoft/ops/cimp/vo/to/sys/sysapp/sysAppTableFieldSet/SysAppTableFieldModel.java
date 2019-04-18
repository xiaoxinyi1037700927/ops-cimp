package com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableFieldSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统表字段集合模型")
public class SysAppTableFieldModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 中文字段名
     */
    @ApiModelProperty(value = "中文字段名")
    private String nameCn;
    /**
     * 英文字段名
     */
    @ApiModelProperty(value = "英文字段名")
    private String nameEn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
