package com.sinosoft.ops.cimp.vo.to.sys.code;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "SysCodeSetModel", description = "用于系统表字段选择所属代码集")
public class SysCodeSetModel {

    @ApiModelProperty(value = "代码集编号")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "中文名称")
    private String nameCn;

    @ApiModelProperty(value = "次序")
    private Integer ordinal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
}
