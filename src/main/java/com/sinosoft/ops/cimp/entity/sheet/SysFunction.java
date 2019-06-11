package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "SYS_FUNCTION")
public class SysFunction implements java.io.Serializable {

    private static final long serialVersionUID = -8465178328105159201L;
    private UUID id;
    private String name;
    private String namecn;
    private String jdbctype;
    private Integer parametercount;
    private String param1name;
    private String param1type;
    private String param2name;
    private String param2type;
    private String param3name;
    private String param3type;
    private String description;
    private Integer ordinal;
    private Integer status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "NAME_CN", nullable = false)
    public String getNameCn() {
        return this.namecn;
    }

    public void setNameCn(String namecn) {
        this.namecn = namecn;
    }

    @Column(name = "JDBC_TYPE", nullable = false)
    public String getJdbcType() {
        return this.jdbctype;
    }

    public void setJdbcType(String jdbctype) {
        this.jdbctype = jdbctype;
    }

    @Column(name = "PARAMETER_COUNT", nullable = false)
    public Integer getParameterCount() {
        return this.parametercount;
    }

    public void setParameterCount(Integer parametercount) {
        this.parametercount = parametercount;
    }

    @Column(name = "PARAM1_NAME", nullable = false)
    public String getParam1Name() {
        return this.param1name;
    }

    public void setParam1Name(String param1name) {
        this.param1name = param1name;
    }

    @Column(name = "PARAM1_TYPE", nullable = false)
    public String getParam1Type() {
        return this.param1type;
    }

    public void setParam1Type(String param1type) {
        this.param1type = param1type;
    }

    @Column(name = "PARAM2_NAME", nullable = false)
    public String getParam2Name() {
        return this.param2name;
    }

    public void setParam2Name(String param2name) {
        this.param2name = param2name;
    }

    @Column(name = "PARAM2_TYPE", nullable = false)
    public String getParam2Type() {
        return this.param2type;
    }

    public void setParam2Type(String param2type) {
        this.param2type = param2type;
    }


    @Column(name = "PARAM3_NAME", nullable = false)
    public String getParam3Name() {
        return this.param3name;
    }

    public void setParam3Name(String param3name) {
        this.param3name = param3name;
    }

    @Column(name = "PARAM3_TYPE", nullable = false)
    public String getParam3Type() {
        return this.param3type;
    }

    public void setParam3Type(String param3type) {
        this.param3type = param3type;
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

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
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
}
