package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "字段模型")
public class FieldModel {
    /**
     * 字段id
     */
    @ApiModelProperty(value = "字段id")
    private String id;
    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名")
    private String name;
    /**
     * 代码集名称
     */
    @ApiModelProperty(value = "代码集名称")
    private String codeSetName;
    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeSetName() {
        return codeSetName;
    }

    public void setCodeSetName(String codeSetName) {
        this.codeSetName = codeSetName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
