package com.sinosoft.ops.cimp.entity.infostruct;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.UUID;

/**
 * IimpInfoSet entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_INFO_SET")
public class SysInfoSet implements java.io.Serializable {
    // Fields
    private static final long serialVersionUID = 7855648675878081820L;
    private Integer id;
    private String name;
    private String tableName;
    private String nameCn;
    private Integer groupId;
    private Boolean groupMain;
    private String shortNameCn;
    private Byte type;
    private String alias;
    private String description;
    private Integer categoryId;
    private Boolean invalid;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    private String secretLevel;
    private Boolean insertable;
    private Boolean deletable;
    private Boolean updatable;
    private Boolean selectable;
    private Byte approvalStatus;
    private Timestamp approvedTime;
    private UUID approvedBy;
    /**解密时间*/
    private Date declassifiedDate;
    /**是否是子节点*/
    private boolean leaf;
    /**文本*/
    private String text;
    /**子节点*/
    private Collection<? extends Object> children = new LinkedHashSet<Object>();
    /***信息项集合*/
    private Collection<SysInfoItem> infoItems = new LinkedHashSet<SysInfoItem>();
    
    /*** 扩展的名字--表名+中文名*/
    private String nameEx;
    
    /**扩展名称--名称大写+中文名*/
    private String nameEx2;
    // Constructors

    /** default constructor */
    public SysInfoSet() {
    }

    /** minimal constructor */
    public SysInfoSet(Integer id, String name,String tableName,Integer groupId,Boolean groupMain, Byte type, Integer categoryId, Boolean invalid, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.tableName = tableName;
        this.groupId =  groupId;
        this.groupMain = groupMain;
        this.type = type;
        this.categoryId = categoryId;
        this.invalid = invalid;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SysInfoSet(Integer id, String name,String tableName, Integer groupId,Boolean groupMain,String nameCn, String shortNameCn, Byte type, String alias, String description,
            Integer categoryId, Boolean invalid, Integer ordinal, Byte status, Timestamp createdTime,
            UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy,
            Byte approvalStatus, Timestamp approvedTime, UUID approvedBy) {
        this.id = id;
        this.name = name;
        this.tableName = tableName;
        this.groupId =  groupId;
        this.groupMain = groupMain;
        this.nameCn = nameCn;
        this.shortNameCn = shortNameCn;
        this.type = type;
        this.alias = alias;
        this.description = description;
        this.categoryId = categoryId;
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
    @TableGenerator(name="SysInfoSetIdG", table="SYS_ID_GENERATOR",
    pkColumnName="ID_KEY", pkColumnValue="SYS_INFO_SET_ID", valueColumnName="ID_VALUE",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SysInfoSetIdG")     
    @Column(name = "ID", unique = true, nullable = false, precision = 8, scale = 0)
    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
        this.setNameEx((this.name==null)?this.name.toUpperCase():"["+this.name.toUpperCase()+"]"+this.nameCn);
    }
    
    
    @Column(name = "GROUP_ID", nullable = false, precision = 8, scale = 0)
    public Integer getGroupId() {
        return groupId;
    }
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Column(name = "GROUP_MAIN", nullable = false, precision = 1, scale = 0)
    public Boolean getGroupMain() {
        return groupMain;
    }
    public void setGroupMain(Boolean groupMain) {
        this.groupMain = groupMain;
    }

    @Column(name = "TABLE_NAME", nullable = false, length = 50)
    public String getTableName() {
        return this.tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Column(name = "NAME_CN")
    public String getNameCn() {
        return this.nameCn;
    }
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
        if(nameCn!=null){
            this.setNameEx((this.name==null)?this.nameCn:"["+this.name.toUpperCase()+"]"+this.nameCn);
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

    @Column(name = "ALIAS")
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CATEGORY_ID", precision = 8, scale = 0)
    public Integer getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
    
    @Column(name = "SECRET_LEVEL", length = 8)
    public String getSecretLevel() {
        return this.secretLevel;
    }
    public void setSecretLevel(String secretLevel) {
        this.secretLevel = secretLevel;
    }

    @Column(name = "INSERTABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getInsertable() {
        return this.insertable;
    }
    public void setInsertable(Boolean insertable) {
        this.insertable = insertable;
    }

    @Column(name = "DELETABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getDeletable() {
        return this.deletable;
    }
    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    @Column(name = "UPDATABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getUpdatable() {
        return this.updatable;
    }
    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    @Column(name = "SELECTABLE", nullable = false, precision = 1, scale = 0)
    public Boolean getSelectable() {
        return this.selectable;
    }
    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
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
    
    @Column(name = "DECLASSIFIED_DATE", length = 7)
    public Date getDeclassifiedDate() {
        return declassifiedDate;
    }
    public void setDeclassifiedDate(Date declassifiedDate) {
        this.declassifiedDate = declassifiedDate;
    }

	@Transient
    public Collection<SysInfoItem> getInfoItems() {
        return infoItems;
    }
    
	public void setInfoItems(Collection<SysInfoItem> infoItems) {
        this.infoItems = infoItems;
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
        if (!(obj instanceof SysInfoSet)) {
            return false;
        }
        SysInfoSet other = (SysInfoSet) obj;
        return id.equals(other.id);
    }
    @Transient
    public boolean isLeaf() {
        return leaf;
    }
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
    @Transient
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

	@Transient
	public Collection<? extends Object> getChildren() {
		return children;
	}
    public void setChildren(Collection<? extends Object> children) {
		this.children = children;
	}

    @Transient
    public String getNameEx() {
        return nameEx;
    }
    public void setNameEx(String nameEx) {
        this.nameEx = (nameEx==null)?"":nameEx;
        this.setText(this.nameEx);
    }

    @Transient
	public String getNameEx2() {
		return "["+this.name.toUpperCase()+"]"+this.nameCn;
	}

	public void setNameEx2(String nameEx2) {
		this.nameEx2 = nameEx2;
	}
    
    
    
}