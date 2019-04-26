package com.sinosoft.ops.cimp.dao.domain.sys.table;

import java.io.Serializable;

public class SysTableFieldInfo implements Serializable {
    private static final long serialVersionUID = -7893452490324786179L;

    //信息集下属性唯一标识
    private String id;
    //属性中文名
    private String nameCn;
    //属性英文名
    private String nameEn;
    //属性描述
    private String description;
    //存储表名称
    private String dbTableName;
    //存储列名称
    private String dbFieldName;
    //存储列类型
    private String dbFieldDataType;
    //是否为主键
    private boolean isPK;
    //是否为外键
    private boolean isFK;
    //是否为逻辑删除字段
    private boolean logicalDeleteFlag;
    //是否为级联删除字段
    private boolean deleteCascadeFlag;
    //属性值修改简单规则
    private String attrValueMonitor;
    //是否可以作为搜索结果
    private boolean canResultFlag;
    //是否可以作为条件字段
    private boolean canConditionFlag;
    //是否可以作为排序字段
    private boolean canOrderFlag;
    //默认的html片段
    private String defaultHtml;
    //默认的js片段
    private String defaultScript;
    //排序
    private Integer sort;
    //代码集名称
    private String codeSetName;
    //代码集类型
    private String codeSetType;
    //在列表中的排序
    private Integer sortInList;
    //在列表中是否显示
    private boolean isShowInList;
    //是否必填
    private boolean isNecessary;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public String getDbFieldDataType() {
        return dbFieldDataType;
    }

    public void setDbFieldDataType(String dbFieldDataType) {
        this.dbFieldDataType = dbFieldDataType;
    }

    public boolean isPK() {
        return isPK;
    }

    public void setPK(boolean PK) {
        isPK = PK;
    }

    public boolean isFK() {
        return isFK;
    }

    public void setFK(boolean FK) {
        isFK = FK;
    }

    public boolean isLogicalDeleteFlag() {
        return logicalDeleteFlag;
    }

    public void setLogicalDeleteFlag(boolean logicalDeleteFlag) {
        this.logicalDeleteFlag = logicalDeleteFlag;
    }

    public boolean isDeleteCascadeFlag() {
        return deleteCascadeFlag;
    }

    public void setDeleteCascadeFlag(boolean deleteCascadeFlag) {
        this.deleteCascadeFlag = deleteCascadeFlag;
    }

    public String getAttrValueMonitor() {
        return attrValueMonitor;
    }

    public void setAttrValueMonitor(String attrValueMonitor) {
        this.attrValueMonitor = attrValueMonitor;
    }

    public boolean isCanResultFlag() {
        return canResultFlag;
    }

    public void setCanResultFlag(boolean canResultFlag) {
        this.canResultFlag = canResultFlag;
    }

    public boolean isCanConditionFlag() {
        return canConditionFlag;
    }

    public void setCanConditionFlag(boolean canConditionFlag) {
        this.canConditionFlag = canConditionFlag;
    }

    public boolean isCanOrderFlag() {
        return canOrderFlag;
    }

    public void setCanOrderFlag(boolean canOrderFlag) {
        this.canOrderFlag = canOrderFlag;
    }

    public String getDefaultHtml() {
        return defaultHtml;
    }

    public void setDefaultHtml(String defaultHtml) {
        this.defaultHtml = defaultHtml;
    }

    public String getDefaultScript() {
        return defaultScript;
    }

    public void setDefaultScript(String defaultScript) {
        this.defaultScript = defaultScript;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCodeSetName() {
        return codeSetName;
    }

    public void setCodeSetName(String codeSetName) {
        this.codeSetName = codeSetName;
    }

    public String getCodeSetType() {
        return codeSetType;
    }

    public void setCodeSetType(String codeSetType) {
        this.codeSetType = codeSetType;
    }

    public Integer getSortInList() {
        return sortInList;
    }

    public void setSortInList(Integer sortInList) {
        this.sortInList = sortInList;
    }

    public boolean isShowInList() {
        return isShowInList;
    }

    public void setShowInList(boolean showInList) {
        isShowInList = showInList;
    }

    public boolean isNecessary() {
        return isNecessary;
    }

    public void setNecessary(boolean necessary) {
        isNecessary = necessary;
    }
}
