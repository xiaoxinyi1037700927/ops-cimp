package com.sinosoft.ops.cimp.dto.sys.table;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class SysTableInfoDTO implements Serializable {
    private static final long serialVersionUID = -1563513040894889395L;
    //表信息唯一主键
    @ApiModelProperty(value = "表信息唯一主键")
    private String id;
    @ApiModelProperty(value = "信息集英文名")
    private String tableNameEn;
    @ApiModelProperty(value = "信息集中文名")
    private String tableNameCn;
    @ApiModelProperty(value = "是否为主集")
    private boolean isMasterTable;
    @ApiModelProperty(value = "信息集主键字段")
    private String tableNamePK;
    @ApiModelProperty(value = "信息集关联主集字段")
    private String tableNameFK;
    @ApiModelProperty(value = "信息集排序")
    private Integer sort;
    @ApiModelProperty(value = "信息集分组名称")
    private String appTableGroupName;
    @ApiModelProperty(value = "信息集下所有属性")
    private List<SysTableFieldInfoDTO> fields = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableNameEn() {
        return tableNameEn;
    }

    public void setTableNameEn(String tableNameEn) {
        this.tableNameEn = tableNameEn;
    }

    public String getTableNameCn() {
        return tableNameCn;
    }

    public void setTableNameCn(String tableNameCn) {
        this.tableNameCn = tableNameCn;
    }

    public boolean isMasterTable() {
        return isMasterTable;
    }

    public void setMasterTable(boolean masterTable) {
        isMasterTable = masterTable;
    }

    public String getTableNamePK() {
        return tableNamePK;
    }

    public void setTableNamePK(String tableNamePK) {
        this.tableNamePK = tableNamePK;
    }

    public String getTableNameFK() {
        return tableNameFK;
    }

    public void setTableNameFK(String tableNameFK) {
        this.tableNameFK = tableNameFK;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getAppTableGroupName() {
        return appTableGroupName;
    }

    public void setAppTableGroupName(String appTableGroupName) {
        this.appTableGroupName = appTableGroupName;
    }

    public List<SysTableFieldInfoDTO> getFields() {
        return fields;
    }

    public void setFields(List<SysTableFieldInfoDTO> fields) {
        if (fields != null) {
            this.fields.addAll(fields);
        }
    }
}
