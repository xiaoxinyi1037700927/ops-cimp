package com.sinosoft.ops.cimp.entity.sys.table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统表字段
 */
@Entity
@Table(name = "SYS_TABLE_FIELD")
public class SysTableField implements Serializable {
    private static final long serialVersionUID = -131153731245506393L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 系统表ID
     */
    @Column(name = "SYS_TABLE_ID", length = 36)
    private String sysTableId;
    /**
     * 中文字段名
     */
    @Column(name = "NAME_CN", length = 120)
    private String nameCn;
    /**
     * 英文字段名
     */
    @Column(name = "NAME_EN", length = 120)
    private String nameEn;
    /**
     * 字段描述
     */
    @Column(name = "DESCRIPTION", length = 300)
    private String description;
    /**
     * 字段所属表名
     */
    @Column(name = "TABLE_NAME", length = 50)
    private String tableName;
    /**
     * 数据库中的字段名
     */
    @Column(name = "DB_FIELD_NAME", length = 50)
    private String dbFieldName;
    /**
     * 数据库中的字段类型
     */
    @Column(name = "DB_FIELD_DATA_TYPE", length = 50)
    private String dbFieldDataType;
    /**
     * 是否为一对多标识
     */
    @Column(name = "MULTI_ATTR_FLAG", length = 50)
    private String multiAttrFlag;
    /**
     * 是否逻辑删除
     */
    @Column(name = "LOGICAL_DELETE_FLAG", length = 50)
    private String logicalDeleteFlag;
    /**
     * 是否级联删除
     */
    @Column(name = "DELETE_CASCADE_FLAG", length = 50)
    private String deleteCascadeFlag;
    /**
     * 是否可以为变量
     */
    @Column(name = "ATTR_CAN_VARIABLE", length = 50)
    private String attrCanVariable;
    /**
     * 属性值修改是否监听
     */
    @Column(name = "ATTR_VALUE_MONITOR", length = 50)
    private String attrValueMonitor;
    /**
     * 属性值修改执行规则
     */
    @Column(name = "ATTR_MODIFY_RULE", length = 50)
    private String attrModifyRule;
    /**
     * 实体属性关联实体
     */
    @Column(name = "ATTR_REL_ENTITY", length = 50)
    private String attrRelEntity;
    /**
     * 实体属性关联的字段
     */
    @Column(name = "ATTR_REL_ENTITY_FIELD", length = 50)
    private String attrRelEntityField;
    /**
     * 是否可以搜索结果
     */
    @Column(name = "CAN_RESULT_FLAG", length = 50)
    private String canResultFlag;
    /**
     * 是否可以作为条件字段
     */
    @Column(name = "CAN_CONDITION_FLAG", length = 50)
    private String canConditionFlag;
    /**
     * 是否可以作为排序字段
     */
    @Column(name = "CAN_ORDER_FLAG", length = 50)
    private String canOrderFlag;
    /**
     * 是否支持函数
     */
    @Column(name = "IS_SUPPORT_FUNC", length = 50)
    private String isSupportFunc;
    /**
     * html代码
     */
    @Column(name = "DEFAULT_HTML", length = 50)
    private String defaultHtml;
    /**
     * 默认脚本
     */
    @Column(name = "DEFAULT_SCRIPT", length = 50)
    private String defaultScript;
    /**
     * 创建人
     */
    @Column(name = "CREATE_ID", length = 36)
    private String createId;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 修改人
     */
    @Column(name = "MODIFY_ID", length = 36)
    private String modifyId;
    /**
     * 修改时间
     */
    @Column(name = "MODIFY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public String getMultiAttrFlag() {
        return multiAttrFlag;
    }

    public void setMultiAttrFlag(String multiAttrFlag) {
        this.multiAttrFlag = multiAttrFlag;
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

    public String getAttrCanVariable() {
        return attrCanVariable;
    }

    public void setAttrCanVariable(String attrCanVariable) {
        this.attrCanVariable = attrCanVariable;
    }

    public String getAttrValueMonitor() {
        return attrValueMonitor;
    }

    public void setAttrValueMonitor(String attrValueMonitor) {
        this.attrValueMonitor = attrValueMonitor;
    }

    public String getAttrModifyRule() {
        return attrModifyRule;
    }

    public void setAttrModifyRule(String attrModifyRule) {
        this.attrModifyRule = attrModifyRule;
    }

    public String getAttrRelEntity() {
        return attrRelEntity;
    }

    public void setAttrRelEntity(String attrRelEntity) {
        this.attrRelEntity = attrRelEntity;
    }

    public String getAttrRelEntityField() {
        return attrRelEntityField;
    }

    public void setAttrRelEntityField(String attrRelEntityField) {
        this.attrRelEntityField = attrRelEntityField;
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

    public String getIsSupportFunc() {
        return isSupportFunc;
    }

    public void setIsSupportFunc(String isSupportFunc) {
        this.isSupportFunc = isSupportFunc;
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
