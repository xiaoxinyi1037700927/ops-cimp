package com.sinosoft.ops.cimp.vo.to.cadre.combinedQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "系统表模型")
public class TableModel {
    /**
     * 表id
     */
    @ApiModelProperty(value = "表id")
    private String id;
    /**
     * 表名
     */
    @ApiModelProperty(value = "表名")
    private String name;
    /**
     * 字段
     */
    @ApiModelProperty(value = "字段")
    private List<FieldModel> fields;

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

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }
}
