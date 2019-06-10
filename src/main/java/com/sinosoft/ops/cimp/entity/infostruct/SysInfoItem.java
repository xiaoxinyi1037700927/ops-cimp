package com.sinosoft.ops.cimp.entity.infostruct;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * IimpInfoItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_INFO_ITEM")
public class SysInfoItem implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -8407478591933861927L;
    private Integer id;
    private Integer infoSetId;
    private String name;
    private String columnName;
    private Boolean required;
    private String alias;
    private String nameCn;
    private String shortNameCn;
    private Byte type;
    private String description;
    private String jdbcType;
    private String dbType;
    private String inputType;
    private Integer inputWidth;
    private Integer displayWidth;
    private Byte displayAlign=(byte)3;//默认值居中
    private Integer length;
    private Integer precision;
    private Integer scale;
    private Boolean primaryKey;
    private String primaryKeyGenerator;
    private Boolean nullable;
    private Boolean searchable;
    private String defaultValue;
    private Boolean autoIncrement;
    private String referenceTable;
    private String referenceColumn;
    private Integer referenceItemId;
    private String referenceCodeSet;
    private Short sortOrder;
    private Boolean sortAscending;
    private Boolean invalid;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    /*** 扩展的名字--列名+中文名*/
    private String nameEx;
    /***验证正则式*/
    private String validationRegex;
    /***验证提示信息*/
    private String validationTip;
    private String secretLevel;
    private Integer nameValuePair;
    private Boolean readable;
    private Boolean writable;
    private Byte approvalStatus;
    private Timestamp approvedTime;
    private UUID approvedBy;
    private Integer uniqueGroupId;
    private String validationType;
    private String tip;
    private Date declassifiedDate;
    // Constructors

    /** default constructor */
    public SysInfoItem() {
    }

    /** minimal constructor */
    public SysInfoItem(Integer id, Integer infoSetId, String name,String columnName, Byte type, String dbType, String inputType, Boolean primaryKey,
            Boolean nullable, Boolean invalid, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.infoSetId = infoSetId;
        this.name = name;
        this.columnName = columnName;
        this.type = type;
        this.dbType = dbType;
        this.inputType = inputType;
        this.primaryKey = primaryKey;
        this.nullable = nullable;
        this.invalid = invalid;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SysInfoItem(Integer id, Integer infoSetId, String name,String columnName, String alias, String nameCn, String shortNameCn,
            Byte type,String description, String jdbcType, String dbType, String inputType, Integer length, Integer precision,
            Integer scale, Boolean primaryKey, String primaryKeyGenerator,Boolean nullable,Boolean searchable, String defaultValue, Boolean autoIncrement,
            String referenceTable, String referenceColumn, String referenceCodeSet, Boolean invalid, Integer ordinal,
            Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy,
            Byte approvalStatus, Timestamp approvedTime, UUID approvedBy) {
        this.id = id;
        this.infoSetId = infoSetId;
        this.columnName = columnName;
        this.name = name;
        this.alias = alias;
        this.nameCn = nameCn;
        this.shortNameCn = shortNameCn;
        this.type = type;
        this.description = description;
        this.jdbcType = jdbcType;
        this.dbType = dbType;
        this.inputType = inputType;
        this.length = length;
        this.precision = precision;
        this.scale = scale;
        this.primaryKey = primaryKey;
        this.primaryKeyGenerator = primaryKeyGenerator;
        this.nullable = nullable;
        this.searchable = searchable;
        this.defaultValue = defaultValue;
        this.autoIncrement = autoIncrement;
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
        this.referenceCodeSet = referenceCodeSet;
        this.invalid = invalid;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.approvalStatus = approvalStatus;
        this.approvedTime = approvedTime;
        this.approvedBy = approvedBy;        
    }

    // Property accessors
    @Id
    @TableGenerator(name="SysInfoItemIdG", table="SYS_ID_GENERATOR",
    pkColumnName="ID_KEY", pkColumnValue="SYS_INFO_ITEM_ID", valueColumnName="ID_VALUE",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SysInfoItemIdG")       
    @Column(name = "ID", unique = true, nullable = false, precision = 8, scale = 0)
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "INFO_SET_ID", nullable = false, precision = 8, scale = 0)
    public Integer getInfoSetId() {
        return this.infoSetId;
    }
    public void setInfoSetId(Integer infoSetId) {
        this.infoSetId = infoSetId;
    }

    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "COLUMN_NAME", nullable = false, length = 50)
    public String getColumnName() {
        return this.columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
        this.setNameEx((this.nameCn==null)?"["+this.columnName+"]":"["+this.columnName+"]"+this.nameCn);
    }
    
    @Column(name = "REQUIRED", nullable = false, precision = 1, scale = 0)
    public Boolean getRequired() {
        return required;
    }
    public void setRequired(Boolean required) {
        this.required = required;
    }

    @Column(name = "ALIAS")
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "NAME_CN")
    public String getNameCn() {
        return this.nameCn;
    }
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
        if(nameCn!=null){
            this.setNameEx((this.columnName==null)?this.nameCn:"["+this.columnName+"]"+this.nameCn);
        }
    }

    @Column(name = "SHORT_NAME_CN")
    public String getShortNameCn() {
        return this.shortNameCn;
    }
    public void setShortNameCn(String shortNameCn) {
        this.shortNameCn = shortNameCn;
    }
    
    @Column(name = "TYPE", nullable = false, precision = 2, scale = 0)
    public Byte getType() {
        return this.type;
    }
    public void setType(Byte type) {
        this.type = type;
    }
    
    @Column(name = "DISPLAY_ALIGN", precision = 2, scale = 0)
    public Byte getDisplayAlign() {
        return displayAlign;
    }
    public void setDisplayAlign(Byte displayAlign) {
        this.displayAlign = displayAlign;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "JDBC_TYPE", length = 50)
    public String getJdbcType() {
        return this.jdbcType;
    }
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    @Column(name = "DB_TYPE", nullable = false, length = 50)
    public String getDbType() {
        return this.dbType;
    }
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    @Column(name = "INPUT_TYPE", nullable = false, length = 50)
    public String getInputType() {
        return this.inputType;
    }
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    @Column(name = "INPUT_WIDTH", precision = 8, scale = 0)
    public Integer getInputWidth() {
        return inputWidth;
    }
    public void setInputWidth(Integer inputWidth) {
        this.inputWidth = inputWidth;
    }
    
    @Column(name = "DISPLAY_WIDTH", precision = 8, scale = 0)
    public Integer getDisplayWidth() {
        return displayWidth;
    }
    public void setDisplayWidth(Integer displayWidth) {
        this.displayWidth = displayWidth;
    }

    @Column(name = "LENGTH", precision = 8, scale = 0)
    public Integer getLength() {
        return this.length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }

    @Column(name = "PRECISION", precision = 8, scale = 0)
    public Integer getPrecision() {
        return this.precision;
    }
    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    @Column(name = "SCALE", precision = 8, scale = 0)
    public Integer getScale() {
        return this.scale;
    }
    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @Column(name = "PRIMARY_KEY", nullable = false, precision = 1, scale = 0)
    public Boolean getPrimaryKey() {
        return this.primaryKey;
    }
    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
    
    @Column(name = "PRIMARY_KEY_GENERATOR", length = 50)
    public String getPrimaryKeyGenerator() {
        return this.primaryKeyGenerator;
    }
    public void setPrimaryKeyGenerator(String primaryKeyGenerator) {
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Column(name = "NULLABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getNullable() {
        return this.nullable;
    }
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }
    
    @Column(name = "SEARCHABLE", precision = 1, scale = 0)
    public Boolean getSearchable() {
        return this.searchable;
    }
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    @Column(name = "DEFAULT_VALUE")
    public String getDefaultValue() {
        return this.defaultValue;
    }
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "AUTO_INCREMENT", precision = 1, scale = 0)
    public Boolean getAutoIncrement() {
        return this.autoIncrement;
    }
    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    @Column(name = "REFERENCE_TABLE", length = 50)
    public String getReferenceTable() {
        return this.referenceTable;
    }
    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    @Column(name = "REFERENCE_COLUMN", length = 50)
    public String getReferenceColumn() {
        return this.referenceColumn;
    }
    public void setReferenceColumn(String referenceColumn) {
        this.referenceColumn = referenceColumn;
    }
    
    @Column(name = "REFERENCE_ITEM_ID", precision = 8, scale = 0)
    public Integer getReferenceItemId() {
        return referenceItemId;
    }
    public void setReferenceItemId(Integer referenceItemId) {
        this.referenceItemId = referenceItemId;
    }

    @Column(name = "REFERENCE_CODE_SET", length = 50)
    public String getReferenceCodeSet() {
        return this.referenceCodeSet;
    }
    public void setReferenceCodeSet(String referenceCodeSet) {
        this.referenceCodeSet = referenceCodeSet;
    }
    
    @Column(name = "SORT_ORDER", precision = 4, scale = 0)
    public Short getSortOrder() {
        return sortOrder;
    }
    public void setSortOrder(Short sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Column(name = "SORT_ASCENDING", precision = 1, scale = 0)
    public Boolean getSortAscending() {
        return sortAscending;
    }
    public void setSortAscending(Boolean sortAscending) {
        this.sortAscending = sortAscending;
    }

    @Column(name = "INVALID", nullable = false, precision = 1, scale = 0)
    public Boolean getInvalid() {
        return this.invalid;
    }
    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)
    public Integer getOrdinal() {
        return this.ordinal;
    }
    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
    public Byte getStatus() {
        return this.status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "CREATED_TIME", nullable = false, length = 11)
    public Timestamp getCreatedTime() {
        return this.createdTime;
    }
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Column(name = "CREATED_BY")
    public UUID getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "LAST_MODIFIED_TIME", nullable = false, length = 11)
    public Timestamp getLastModifiedTime() {
        return this.lastModifiedTime;
    }
    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Column(name = "LAST_MODIFIED_BY")
    public UUID getLastModifiedBy() {
        return this.lastModifiedBy;
    }
    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    
    @Transient
    public String getNameEx() {
        return nameEx;
    }
    public void setNameEx(String nameEx) {
        this.nameEx = (nameEx==null)?"":nameEx;
    }

    @Column(name = "VALIDATION_REGEX")
    public String getValidationRegex() {
        return validationRegex;
    }
    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }

    @Column(name = "VALIDATION_TIP")
    public String getValidationTip() {
        return validationTip;
    }
    public void setValidationTip(String validationTip) {
        this.validationTip = validationTip;
    }
    
    @Column(name = "SECRET_LEVEL", length = 8)
    public String getSecretLevel() {
        return this.secretLevel;
    }
    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    @Column(name = "NAME_VALUE_PAIR", precision = 8, scale = 0)
    public Integer getNameValuePair() {
        return this.nameValuePair;
    }
    public void setNameValuePair(Integer nameValuePair) {
        this.nameValuePair = nameValuePair;
    }

    @Column(name = "READABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getReadable() {
        return this.readable;
    }
    public void setReadable(Boolean readable) {
        this.readable = readable;
    }

    @Column(name = "WRITABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getWritable() {
        return this.writable;
    }
    public void setWritable(Boolean writable) {
        this.writable = writable;
    }
    
    @Column(name = "APPROVAL_STATUS", precision = 2, scale = 0)
    public Byte getApprovalStatus() {
        return this.approvalStatus;
    }
    public void setApprovalStatus(Byte approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    @Column(name = "APPROVED_TIME", length = 11)
    public Timestamp getApprovedTime() {
        return this.approvedTime;
    }
    public void setApprovedTime(Timestamp approvedTime) {
        this.approvedTime = approvedTime;
    }

    @Column(name = "APPROVED_BY")
    public UUID getApprovedBy() {
        return this.approvedBy;
    }
    public void setApprovedBy(UUID approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    @Column(name = "UNIQUE_GROUP_ID", precision = 8, scale = 0)
    public Integer getUniqueGroupId() {
        return uniqueGroupId;
    }
    public void setUniqueGroupId(Integer uniqueGroupId) {
        this.uniqueGroupId = uniqueGroupId;
    }

    @Column(name = "VALIDATION_TYPE")
    public String getValidationType() {
        return validationType;
    }
    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }
    
    @Column(name = "TIP", length = 100)
    public String getTip() {
        return tip;
    }
    public void setTip(String tip) {
        this.tip = tip;
    }
    
    @Column(name = "DECLASSIFIED_DATE", length = 7)
    public Date getDeclassifiedDate() {
        return declassifiedDate;
    }
    public void setDeclassifiedDate(Date declassifiedDate) {
        this.declassifiedDate = declassifiedDate;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SysInfoItem)) {
            return false;
        }
        SysInfoItem other = (SysInfoItem) obj;
        return id.equals(other.id);
    }    

    @Override
	public String toString() {
		return "InfoItem [id=" + id + ", infoSetId=" + infoSetId + ", name=" + name + ", alias=" + alias + ", nameCn=" + nameCn
				+ ", shortNameCn=" + shortNameCn + ", description=" + description + ", jdbcType=" + jdbcType
				+ ", dbType=" + dbType + ", inputType=" + inputType + ", length=" + length + ", precision=" + precision
				+ ", scale=" + scale + ", primaryKey=" + primaryKey + ", nullable=" + nullable + ", defaultValue="
				+ defaultValue + ", autoIncrement=" + autoIncrement + ", referenceTable=" + referenceTable
				+ ", referenceColumn=" + referenceColumn + ", referenceCodeSet=" + referenceCodeSet + ", invalid="
				+ invalid + ", ordinal=" + ordinal + ", status=" + status + ", createdTime=" + createdTime
				+ ", createdBy=" + createdBy + ", lastModifiedTime=" + lastModifiedTime + ", lastModifiedBy="
				+ lastModifiedBy + "]";
	}
    
}