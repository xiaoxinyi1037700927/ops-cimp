package com.sinosoft.ops.cimp.entity.infostruct;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * SysInfoSetCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_INFO_SET_CATEGORY")
public class SysInfoSetCategory implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1474635542326961841L;
    private Integer id;
    private String name;
    private String description;
    private Integer parentId;
    private Boolean invalid;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    
    /**是否是子节点*/
    private boolean leaf;
    /** 文本 */
    private String text;
    /**子节点*/
    private Collection<Object> children = new ArrayList<Object>();
    // Constructors

    /** default constructor */
    public SysInfoSetCategory() {
    }

    /** minimal constructor */
    public SysInfoSetCategory(Integer id, Boolean invalid, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.invalid = invalid;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SysInfoSetCategory(Integer id, String name, String description, Integer parentId,
            Boolean invalid, Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy,
            Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.invalid = invalid;
        this.ordinal = ordinal;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
    }

    // Property accessors
    @Id
    @TableGenerator(name="SysInfoSetCategoryIdG", table="SYS_ID_GENERATOR",
    pkColumnName="ID_KEY", pkColumnValue="SYS_INFO_SET_CATEGORY_ID", valueColumnName="ID_VALUE",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="SysInfoSetCategoryIdG") 
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
        this.setText(name);
    }

    @Column(name = "DESCRIPTION")

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "PARENT_ID", precision = 8, scale = 0)

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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
	public Collection<Object> getChildren() {
		return children;
	}
	public void setChildren(Collection<Object> children) {
		this.children = children;
	}
}