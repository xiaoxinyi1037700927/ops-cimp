package com.sinosoft.ops.cimp.dao.domain.sys.table;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class SysTableInfo implements Serializable {
    private static final long serialVersionUID = 7109252290305845978L;
    //系统表模型唯一主键
    private String id;
    //存储表名称
    private String dbTableName;
    //存储表描述
    private String description;
    //是否为主要信息集
    private boolean isMasterTable;
    //信息集中文名
    private String nameCn;
    //信息集英文名
    private String nameEn;
    //信息集排序
    private Integer sort = 0;
    //信息集状态（0：不可用，1：可用）
    private String status;
    //信息集下的属性（字段）信息
    private List<SysTableFieldInfo> tableFields = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMasterTable() {
        return isMasterTable;
    }

    public void setMasterTable(boolean masterTable) {
        isMasterTable = masterTable;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SysTableFieldInfo> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<SysTableFieldInfo> tableFields) {
        this.tableFields = tableFields;
    }
}
