package com.sinosoft.ops.cimp.entity.sheet;


import com.sinosoft.ops.cimp.common.model.Trackable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

/**
 * @ClassName:  Sheet
 * @description: 表格
 * @author:        Nil
 * @date:            2017年10月22日 上午9:18:23
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET")
public class Sheet implements java.io.Serializable,Trackable {

    // Fields
    private static final long serialVersionUID = 600767762590856701L;
    /*** 标识 */
    private UUID id;
    /*** 表格设计标识 */
    private UUID designId;
    /*** 生成类型 */
    private Byte generationType;
    /*** 表内校核状态 */
    private Byte innerCheckStatus;
    /*** 表间校核状态 */
    private Byte outerCheckStatus;
    /*** 表内计算状态 */
    private Byte innerCalculationStatus;
    /*** 表间计算状态 */
    private Byte outerCalculationStatus;
    /*** 是否被编辑过 */
    private Boolean beingEdited;
    /*** 是否已归档 */
    private Boolean archived;
    /*** 应用标识 */
    private String appId;
    /*** 次序 */
    private Integer ordinal;
    /*** 标识 */
    private Integer flg;
    /*** 状态 */
    private Byte status;
    /*** 创建时间 */
    private Timestamp createdTime;
    /*** 创建人 */
    private UUID createdBy;
    /*** 最后修改时间 */
    private Timestamp lastModifiedTime;
    /*** 最后修改人 */
    private UUID lastModifiedBy;
    /*** 制表时间 */
    private Timestamp preparedTime;
    /*** 制表人 */
    private UUID preparedBy;
    /*** 审核时间 */
    private Timestamp checkedTime;
    /*** 审核人 */
    private UUID checkedBy;
    /*** 批准时间 */
    private Timestamp approvedTime;
    /*** 批准人 */
    private UUID approvedBy;
    /*** 名称 */
    private String name;
    /*** 说明 */
    private String description;
    /*** 是否汇总 */
    private Boolean summary;
    /*** 审核状态 */
    private Byte auditStatus;
    /*** 批注 */
    private String notation;
    /*** 归档人 */
    private UUID archivedBy;
    /*** 归档机构标识 */
    private String archivedOrganizationId;
    /*** 操作机构标识 */
    private String operatedOrganizationId;
    /*** 参数 */
    protected Collection<SheetParameter> parameters=new LinkedList<SheetParameter>();
    /*** 表格数据 */
    protected Collection<? extends SheetData> datas=new LinkedList<SheetData>();

    // Constructors
    /** default constructor */
    public Sheet() {
    }

    /** minimal constructor */
    public Sheet(UUID id, UUID designId, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public Sheet(UUID id, UUID designId, Byte generationType, Byte innerCheckStatus, Byte outerCheckStatus,
            Byte innerCalculationStatus, Byte outerCalculationStatus, Boolean beingEdited, Boolean archived, String appId,
            Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime,
            UUID lastModifiedBy, String name, String description, Boolean summary, Byte auditStatus) {
        this.id = id;
        this.designId = designId;
        this.generationType = generationType;
        this.innerCheckStatus = innerCheckStatus;
        this.outerCheckStatus = outerCheckStatus;
        this.innerCalculationStatus = innerCalculationStatus;
        this.outerCalculationStatus = outerCalculationStatus;
        this.beingEdited = beingEdited;
        this.archived = archived;
        this.appId = appId;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.name = name;
        this.description = description;
        this.summary = summary;
        this.auditStatus = auditStatus;
    }

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public UUID getId() {
        return this.id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    @Column(name = "DESIGN_ID", nullable = false)
    public UUID getDesignId() {
        return this.designId;
    }
    public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Column(name = "GENERATION_TYPE", precision = 2, scale = 0)
    public Byte getGenerationType() {
        return this.generationType;
    }
    public void setGenerationType(Byte generationType) {
        this.generationType = generationType;
    }

    @Column(name = "INNER_CHECK_STATUS", precision = 2, scale = 0)
    public Byte getInnerCheckStatus() {
        return this.innerCheckStatus;
    }
    public void setInnerCheckStatus(Byte innerCheckStatus) {
        this.innerCheckStatus = innerCheckStatus;
    }

    @Column(name = "OUTER_CHECK_STATUS", precision = 2, scale = 0)
    public Byte getOuterCheckStatus() {
        return this.outerCheckStatus;
    }
    public void setOuterCheckStatus(Byte outerCheckStatus) {
        this.outerCheckStatus = outerCheckStatus;
    }

    @Column(name = "INNER_CALCULATION_STATUS", precision = 2, scale = 0)
    public Byte getInnerCalculationStatus() {
        return this.innerCalculationStatus;
    }
    public void setInnerCalculationStatus(Byte innerCalculationStatus) {
        this.innerCalculationStatus = innerCalculationStatus;
    }

    @Column(name = "OUTER_CALCULATION_STATUS", precision = 2, scale = 0)
    public Byte getOuterCalculationStatus() {
        return this.outerCalculationStatus;
    }
    public void setOuterCalculationStatus(Byte outerCalculationStatus) {
        this.outerCalculationStatus = outerCalculationStatus;
    }

    @Column(name = "BEING_EDITED", precision = 1, scale = 0)
    public Boolean getBeingEdited() {
        return this.beingEdited;
    }
    public void setBeingEdited(Boolean beingEdited) {
        this.beingEdited = beingEdited;
    }

    @Column(name = "ARCHIVED", precision = 1, scale = 0)
    public Boolean getArchived() {
        return this.archived;
    }
    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    @Column(name = "APP_ID", length = 36)
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)
    public Integer getOrdinal() {
        return this.ordinal;
    }
    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "FLG", precision = 1, scale = 0)
    public Integer getFlg() {
        return this.flg;
    }
    public void setFlg(Integer flg) {
        this.flg = flg;
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

    //@Column(name = "PREPARED_TIME", nullable = false, length = 11)
    @Transient
    public Timestamp getPreparedTime() {
        return preparedTime;
    }
    public void setPreparedTime(Timestamp preparedTime) {
        this.preparedTime = preparedTime;
    }

    //@Column(name = "PREPARED_BY")
    @Transient
    public UUID getPreparedBy() {
        return preparedBy;
    }
    public void setPreparedBy(UUID preparedBy) {
        this.preparedBy = preparedBy;
    }

    //@Column(name = "CHECKED_TIME", nullable = false, length = 11)
    @Transient
    public Timestamp getCheckedTime() {
        return checkedTime;
    }
    public void setCheckedTime(Timestamp checkedTime) {
        this.checkedTime = checkedTime;
    }

    //@Column(name = "CHECKED_BY")
    @Transient
    public UUID getCheckedBy() {
        return checkedBy;
    }
    public void setCheckedBy(UUID checkedBy) {
        this.checkedBy = checkedBy;
    }

    //@Column(name = "APPROVED_TIME", nullable = false, length = 11)
    @Transient
    public Timestamp getApprovedTime() {
        return approvedTime;
    }
    public void setApprovedTime(Timestamp approvedTime) {
        this.approvedTime = approvedTime;
    }
    
    //@Column(name = "APPROVED_BY")
    @Transient
    public UUID getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(UUID approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    @Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    @Column(name = "SUMMARY")
	public Boolean getSummary() {
		return summary;
	}
	public void setSummary(Boolean summary) {
		this.summary = summary;
	}

    @Column(name = "AUDIT_STATUS")
	public Byte getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Byte auditStatus) {
		this.auditStatus = auditStatus;
	}
	
    @Column(name = "NOTATION")
    public String getNotation() {
        return notation;
    }
    public void setNotation(String notation) {
        this.notation = notation;
    }

    @Column(name = "ARCHIVED_BY")
	public UUID getArchivedBy() {
        return archivedBy;
    }
    public void setArchivedBy(UUID archivedBy) {
        this.archivedBy = archivedBy;
    }

    @Column(name = "ARCHIVED_ORGANIZATION_ID")
    public String getArchivedOrganizationId() {
        return archivedOrganizationId;
    }
    public void setArchivedOrganizationId(String archivedOrganizationId) {
        this.archivedOrganizationId = archivedOrganizationId;
    }
    
    @Column(name = "OPERATED_ORGANIZATION_ID")
    public String getOperatedOrganizationId() {
        return operatedOrganizationId;
    }
    public void setOperatedOrganizationId(String operatedOrganizationId) {
        this.operatedOrganizationId = operatedOrganizationId;
    }
    
    @Transient
    public Collection<SheetParameter> getParameters() {
        return parameters;
    }
    public void setParameters(Collection<SheetParameter> parameters) {
        this.parameters = parameters;
    }
    
    @Transient
    public Collection<? extends SheetData> getDatas() {
        return datas;
    }
    public void setDatas(Collection<? extends SheetData> datas) {
        this.datas = datas;
    }
}