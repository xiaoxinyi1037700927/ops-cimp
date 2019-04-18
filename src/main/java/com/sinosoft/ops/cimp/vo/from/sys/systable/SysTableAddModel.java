package com.sinosoft.ops.cimp.vo.from.sys.systable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysTableAddModel", description = "添加系统表")
public class SysTableAddModel {


    @ApiModelProperty(value = "类别编号")
    private String sysTableTypeId;

    /**
     * 中文表名
     */
    @ApiModelProperty(value = "中文表名")
    private String nameCn;
    /**
     * 英文表名
     */
    @ApiModelProperty(value = "英文表名")
    private String nameEn;
    /**
     * 表描述
     */
    @ApiModelProperty(value = "表描述")
    private String description;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String status;
    /**
     * 数据库中的表名
     */
    @ApiModelProperty(value = "数据库中的表名")
    private String dbTableName;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**
     * 是否主集表
     */
    @ApiModelProperty(value = "是否主集表")
    private String isMasterTable;

    public String getSysTableTypeId() {
        return sysTableTypeId;
    }

    public void setSysTableTypeId(String sysTableTypeId) {
        this.sysTableTypeId = sysTableTypeId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIsMasterTable() {
        return isMasterTable;
    }

    public void setIsMasterTable(String isMasterTable) {
        this.isMasterTable = isMasterTable;
    }
}
