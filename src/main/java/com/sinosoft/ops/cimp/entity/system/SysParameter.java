package com.sinosoft.ops.cimp.entity.system;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * SysParameter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_PARAMETER", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class SysParameter implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = -706978235537817622L;
    private Integer id;
    private String name;
    private String nameCn;
    private Byte type;
    private Byte scope;
    private String defaultValue;
    private Byte valueType;
    private String description;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    private String referenceCodeSet;
    // Constructors

    /** default constructor */
    public SysParameter() {
    }

    /** minimal constructor */
    public SysParameter(Integer id, String name, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SysParameter(Integer id, String name, String nameCn, Byte type, Byte scope, String defaultValue,
            Byte valueType, String description, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy,String referenceCodeSet) {
        this.id = id;
        this.name = name;
        this.nameCn = nameCn;
        this.type = type;
        this.scope = scope;
        this.defaultValue = defaultValue;
        this.valueType = valueType;
        this.description = description;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.referenceCodeSet=referenceCodeSet;
    }

    // Property accessors
    @Id
    @TableGenerator(name="SysParameterIdG", table="SYS_ID_GENERATOR",
    pkColumnName="ID_KEY", pkColumnValue="SYS_PARAMETER_ID", valueColumnName="ID_VALUE",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SysParameterIdG")
    @Column(name = "ID", unique = true, nullable = false, precision = 8, scale = 0)
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME", unique = true, nullable = false, length = 30)

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "NAME_CN")

    public String getNameCn() {
        return this.nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Column(name = "TYPE", precision = 2, scale = 0)

    public Byte getType() {
        return this.type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    @Column(name = "SCOPE", precision = 2, scale = 0)

    public Byte getScope() {
        return this.scope;
    }

    public void setScope(Byte scope) {
        this.scope = scope;
    }

    @Column(name = "DEFAULT_VALUE")

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Column(name = "VALUE_TYPE", precision = 2, scale = 0)

    public Byte getValueType() {
        return this.valueType;
    }

    public void setValueType(Byte valueType) {
        this.valueType = valueType;
    }

    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Column(name = "REFERENCE_CODE_SET")
    public String getReferenceCodeSet() {
        return this.referenceCodeSet;
    }
    public void setReferenceCodeSet(String referenceCodeSet) {
        this.referenceCodeSet = referenceCodeSet;
    }

}