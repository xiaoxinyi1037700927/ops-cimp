package com.sinosoft.ops.cimp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "需要翻译的属性")
public class TranslateField implements Serializable {

    private static final long serialVersionUID = 5734763126700887559L;
    @ApiModelProperty(value = "需要翻译的属性")
    private String fieldName;
    @ApiModelProperty(value = "待翻译属性原值")
    private Object fieldValue;
    @ApiModelProperty(value = "待翻译属性翻译后的值")
    private Object fieldTranslateValue;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Object getFieldTranslateValue() {
        return fieldTranslateValue;
    }

    public void setFieldTranslateValue(Object fieldTranslateValue) {
        this.fieldTranslateValue = fieldTranslateValue;
    }
}
