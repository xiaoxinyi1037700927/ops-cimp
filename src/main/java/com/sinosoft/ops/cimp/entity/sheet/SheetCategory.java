package com.sinosoft.ops.cimp.entity.sheet;


import com.sinosoft.ops.cimp.common.model.DefaultTreeNode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @classname:  SheetCategory
 * @description: 表格分类
 * @author:        Nil
 * @date:            2017年10月22日 上午9:18:38
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_CATEGORY")
public class SheetCategory extends DefaultTreeNode implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -1855384331365459418L;
    /*** 标识 */
    private UUID id;
    /*** 名称 */
    private String name;
    /*** 描述 */
    private String description;
    /*** 应用范围 */
    private Byte scope;
    /*** 父标识 */
    private UUID parentId;
    /*** 应用标识 */
    private Integer appId;
    /*** 信息分类标识 */
    private String infoCategoryId;
    /*** 是否系统定义 */
    private Boolean systemDefined;
    /*** 次序 */
    private Integer ordinal;
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
    /***是否叶子节点*/
    private Boolean leaf=false;
    /*** 表格类型 */
    private Byte type;

    private UUID designCategoryId;
    private String reportOrg;
    private Integer reportType;


    // Constructors

    /** default constructor */
    public SheetCategory() {
    }

    /** minimal constructor */
    public SheetCategory(UUID id, String name, Byte scope,Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.scope = scope;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetCategory(UUID id, String name, Byte scope,String description, UUID parentId, Integer appId,
            Boolean systemDefined, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.scope = scope;
        this.description = description;
        this.parentId = parentId;
        this.appId = appId;
        this.systemDefined = systemDefined;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
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

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
        this.setText(name);
    }
    
    @Column(name = "SCOPE", precision = 2, scale = 0)
    public Byte getScope() {
        return this.scope;
    }
    public void setScope(Byte scope) {
        this.scope = scope;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PARENT_ID")
    public UUID getParentId() {
        return this.parentId;
    }
    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    @Column(name = "APP_ID", precision = 8, scale = 0)
    public Integer getAppId() {
        return this.appId;
    }
    public void setAppId(Integer appId) {
        this.appId = appId;
    }
    
    @Column(name = "INFO_CATEGORY_ID", length = 20)
    public String getInfoCategoryId() {
        return infoCategoryId;
    }
    public void setInfoCategoryId(String infoCategoryId) {
        this.infoCategoryId = infoCategoryId;
    }

    @Column(name = "SYSTEM_DEFINED", precision = 1, scale = 0)
    public Boolean getSystemDefined() {
        return this.systemDefined;
    }
    public void setSystemDefined(Boolean systemDefined) {
        this.systemDefined = systemDefined;
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

    @Column(name = "TYPE")
    public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

    @Column(name = "DESIGN_CATEGORY_ID")
    public UUID getDesignCategoryId() {
        return designCategoryId;
    }

    public void setDesignCategoryId(UUID designCategoryId) {
        this.designCategoryId = designCategoryId;
    }

    @Column(name = "REPORT_ORG")
    public String getReportOrg() {
        return reportOrg;
    }

    public void setReportOrg(String reportOrg) {
        this.reportOrg = reportOrg;
    }

    @Column(name = "REPORT_TYPE")
    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

	@Transient
    public Boolean getLeaf() {
        return leaf;
    }
    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }
}