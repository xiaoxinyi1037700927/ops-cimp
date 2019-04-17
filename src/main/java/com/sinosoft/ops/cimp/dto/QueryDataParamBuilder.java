package com.sinosoft.ops.cimp.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel(description = "查询结果集")
public class QueryDataParamBuilder implements Serializable {
    private static final long serialVersionUID = -3631435382070019402L;

    //实体名称
    @ApiModelProperty(value = "模型名称")
    private String tableTypeNameEn;
    //待查询组名称
    @ApiModelProperty(value = "待查询信息集名称")
    private String tableNameEn;
    //项目编号
    @ApiModelProperty(value = "项目编号")
    private String prjCode;
    //待查询组或者实体的主键存储列
    @ApiModelProperty(value = "待查询信息集主键字段")
    private String tableNameEnPK;
    //待查询实体主键属性值
    @ApiModelProperty(value = "待查询信息集主键字段值")
    private Object tableNameEnPKValue;
    @ApiModelProperty(value = "信息集外键字段")
    private String tableNameEnFK;
    @ApiModelProperty(value = "信息集外键字段的值")
    private Object tableNameEnFKValue;
    @ApiModelProperty(value = "待翻译的字段和值")
    private List<TranslateField> translateFields = Lists.newArrayList();
    //是否需要分页
    @ApiModelProperty(value = "是否需要分页")
    private boolean needPager;
    //起始页
    @ApiModelProperty(value = "起始页")
    private Integer startPage;
    //每页显示条数
    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    //需要返回的列名
    @ApiModelProperty(value = "查询结果列名")
    private List<String> resultFields = Lists.newArrayList();
    //其他查询条件
    @ApiModelProperty(value = "查询条件")
    private Map<String, Object> queryConditions = Maps.newHashMap();
    //属性列排序条件
    @ApiModelProperty(value = "排序条件")
    private Map<String, String> orderByAttrs = Maps.newHashMap();
    //返回数据结果类型（1为结果属性和结果集为一对一，2为结果属性和结果集为一对多）
    @ApiModelProperty(value = "返回数据结果类型（1为结果属性和结果集为一对一，2为结果属性和结果集为一对多）")
    private String resultDataType;
    //一对一结果集
    @ApiModelProperty(value = "一对一结果集")
    private Map<String, Object> resultData = Maps.newHashMap();
    //一对多结果集
    @ApiModelProperty(value = "一对多结果集")
    private List<Map<String, Object>> resultMultiData = Lists.newArrayList();
    //修改或更新表单数据
    @ApiModelProperty(value = "修改或更新表单数据")
    private Map<String, Object> saveOrUpdateFormData = Maps.newHashMap();

    public String getPrjCode() {
        return prjCode;
    }

    public QueryDataParamBuilder setPrjCode(String prjCode) {
        this.prjCode = prjCode;
        return this;
    }

    public String getTableTypeNameEn() {
        return tableTypeNameEn;
    }

    public QueryDataParamBuilder setTableTypeNameEn(String tableTypeNameEn) {
        this.tableTypeNameEn = tableTypeNameEn;
        return this;
    }

    public String getTableNameEn() {
        return tableNameEn;
    }

    public QueryDataParamBuilder setTableNameEn(String tableNameEn) {
        this.tableNameEn = tableNameEn;
        return this;
    }

    public String getTableNameEnPK() {
        return tableNameEnPK;
    }

    public QueryDataParamBuilder setTableNameEnPK(String tableNameEnPK) {
        this.tableNameEnPK = tableNameEnPK;
        return this;
    }

    public Object getTableNameEnPKValue() {
        return tableNameEnPKValue;
    }

    public QueryDataParamBuilder setTableNameEnPKValue(Object tableNameEnPKValue) {
        this.tableNameEnPKValue = tableNameEnPKValue;
        return this;
    }

    public String getTableNameEnFK() {
        return tableNameEnFK;
    }

    public QueryDataParamBuilder setTableNameEnFK(String tableNameEnFK) {
        this.tableNameEnFK = tableNameEnFK;
        return this;
    }

    public Object getTableNameEnFKValue() {
        return tableNameEnFKValue;
    }

    public QueryDataParamBuilder setTableNameEnFKValue(Object tableNameEnFKValue) {
        this.tableNameEnFKValue = tableNameEnFKValue;
        return this;
    }

    public List<TranslateField> getTranslateFields() {
        return translateFields;
    }

    public QueryDataParamBuilder setTranslateFields(List<TranslateField> translateFields) {
        if (translateFields != null) {
            this.translateFields.addAll(translateFields);
        }
        return this;
    }

    public QueryDataParamBuilder setTranslateFields(TranslateField translateField) {
        if (translateField != null) {
            this.translateFields.add(translateField);
        }
        return this;
    }

    public boolean isNeedPager() {
        return needPager;
    }

    public QueryDataParamBuilder setNeedPager(boolean needPager) {
        this.needPager = needPager;
        return this;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public QueryDataParamBuilder setStartPage(Integer startPage) {
        this.startPage = startPage;
        return this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public QueryDataParamBuilder setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<String> getResultFields() {
        return resultFields;
    }

    public QueryDataParamBuilder setResultFields(List<String> resultFields) {
        this.resultFields = resultFields;
        return this;
    }

    public Map<String, Object> getQueryConditions() {
        return queryConditions;
    }

    public QueryDataParamBuilder setQueryConditions(Map<String, Object> queryConditions) {
        this.queryConditions = queryConditions;
        return this;
    }

    public Map<String, String> getOrderByAttrs() {
        return orderByAttrs;
    }

    public QueryDataParamBuilder setOrderByAttrs(Map<String, String> orderByAttrs) {
        this.orderByAttrs = orderByAttrs;
        return this;
    }

    public Map<String, Object> getResultData() {
        return resultData;
    }

    public QueryDataParamBuilder setResultData(Map<String, Object> resultData) {
        this.resultData = resultData;
        return this;
    }

    public Map<String, Object> getSaveOrUpdateFormData() {
        return saveOrUpdateFormData;
    }

    public QueryDataParamBuilder setSaveOrUpdateFormData(Map<String, Object> saveOrUpdateFormData) {
        if (saveOrUpdateFormData != null) {
            this.saveOrUpdateFormData = saveOrUpdateFormData;
            return this;
        }
        return this;
    }

    public String getResultDataType() {
        return resultDataType;
    }

    public QueryDataParamBuilder setResultDataType(String resultDataType) {
        this.resultDataType = resultDataType;
        return this;
    }

    public List<Map<String, Object>> getResultMultiData() {
        return resultMultiData;
    }

    public QueryDataParamBuilder setResultMultiData(List<Map<String, Object>> resultMultiData) {
        this.resultMultiData = resultMultiData;
        return this;
    }
}
