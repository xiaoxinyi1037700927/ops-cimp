package com.sinosoft.ops.cimp.vo.from.sys.syscode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "SysCodeItemAddModel",description = "代码项添加Model")
public class SysCodeItemAddModel {

    @NotNull(message = "代码集标识不能为空")
    @ApiModelProperty(value = "代码集标识")
    private Integer codeSetId;

    @NotBlank(message = "代码不能为空")
    @ApiModelProperty(value = "代码")
    private String code;

    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简称")
    private String briefName;

    @ApiModelProperty(value = "上级代码")
    private String parentCode;

    @ApiModelProperty(value = "名称简拼")
    private String spell;

    @ApiModelProperty(value = "是否可选择")
    private Integer selectAble;

    @ApiModelProperty(value = "是否叶子")
    private Integer leaf;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "是否已失效")
    private Integer invalid;

    @ApiModelProperty(value = "次序")
    private Integer ordinal;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "是否只读")
    private Integer readonly;

    public Integer getCodeSetId() {
        return codeSetId;
    }

    public void setCodeSetId(Integer codeSetId) {
        this.codeSetId = codeSetId;
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

    public String getBriefName() {
        return briefName;
    }

    public void setBriefName(String briefName) {
        this.briefName = briefName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public Integer getSelectAble() {
        return selectAble;
    }

    public void setSelectAble(Integer selectAble) {
        this.selectAble = selectAble;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReadonly() {
        return readonly;
    }

    public void setReadonly(Integer readonly) {
        this.readonly = readonly;
    }

}
