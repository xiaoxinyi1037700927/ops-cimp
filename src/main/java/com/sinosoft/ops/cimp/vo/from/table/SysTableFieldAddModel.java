package com.sinosoft.ops.cimp.vo.from.table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysTableFieldAddModel",description = "系统表字段添加")
public class SysTableFieldAddModel {
    /**
     * 系统表ID
     */
    @ApiModelProperty(value = "系统表ID")
    private String sysTableId;
    /**
     * 中文字段名
     */
    @ApiModelProperty(value = "中文字段名")
    private String nameCn;
    /**
     * 英文字段名
     */
    @ApiModelProperty(value = "英文字段名")
    private String nameEn;
    /**
     * 字段描述
     */
    @ApiModelProperty(value = "字段描述")
    private String description;

    /**
     * 字段所属表名
     */
    @ApiModelProperty(value = "字段所属表名")
    private String dbTableName;

    /**
     * 数据库中的字段名
     */
    @ApiModelProperty(value = "数据库中的字段名")
    private String dbFieldName;

    /**
     * 是否主键
     */
    @ApiModelProperty(value = "是否主键")
    private String isPK;

    /**
     * 是否外键
     */
    @ApiModelProperty(value = "是否外键")
    private String isFK;

    /**
     * 数据库中的字段类型
     */
    @ApiModelProperty(value = "数据库中的字段类型")
    private String dbFieldDataType;

    /**
     * 是否逻辑删除
     */
    @ApiModelProperty(value = "是否逻辑删除")
    private String logicalDeleteFlag;

    /**
     * 是否级联删除
     */
    @ApiModelProperty(value = "是否级联删除")
    private String deleteCascadeFlag;

    /**
     * 属性值修改是否监听
     */
    @ApiModelProperty(value = "属性值修改是否监听")
    private String attrValueMonitor;

    /**
     * 是否可以搜索结果
     */
    @ApiModelProperty(value = "是否可以搜索结果")
    private String canResultFlag;
    /**
     * 是否可以作为条件字段
     */
    @ApiModelProperty(value = "是否可以作为条件字段")
    private String canConditionFlag;
    /**
     * 是否可以作为排序字段
     */
    @ApiModelProperty(value = "是否可以作为排序字段")
    private String canOrderFlag;

    /**
     * html代码
     */
    @ApiModelProperty(value = "html代码")
    private String defaultHtml;
    /**
     * 默认脚本
     */
    @ApiModelProperty(value = "默认脚本")
    private String defaultScript;

    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
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

    public String getIsPK() {
        return isPK;
    }

    public void setIsPK(String isPK) {
        this.isPK = isPK;
    }

    public String getIsFK() {
        return isFK;
    }

    public void setIsFK(String isFK) {
        this.isFK = isFK;
    }

    public String getDbFieldDataType() {
        return dbFieldDataType;
    }

    public void setDbFieldDataType(String dbFieldDataType) {
        this.dbFieldDataType = dbFieldDataType;
    }

    public String getLogicalDeleteFlag() {
        return logicalDeleteFlag;
    }

    public void setLogicalDeleteFlag(String logicalDeleteFlag) {
        this.logicalDeleteFlag = logicalDeleteFlag;
    }

    public String getDeleteCascadeFlag() {
        return deleteCascadeFlag;
    }

    public void setDeleteCascadeFlag(String deleteCascadeFlag) {
        this.deleteCascadeFlag = deleteCascadeFlag;
    }

    public String getAttrValueMonitor() {
        return attrValueMonitor;
    }

    public void setAttrValueMonitor(String attrValueMonitor) {
        this.attrValueMonitor = attrValueMonitor;
    }

    public String getCanResultFlag() {
        return canResultFlag;
    }

    public void setCanResultFlag(String canResultFlag) {
        this.canResultFlag = canResultFlag;
    }

    public String getCanConditionFlag() {
        return canConditionFlag;
    }

    public void setCanConditionFlag(String canConditionFlag) {
        this.canConditionFlag = canConditionFlag;
    }

    public String getCanOrderFlag() {
        return canOrderFlag;
    }

    public void setCanOrderFlag(String canOrderFlag) {
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
}
