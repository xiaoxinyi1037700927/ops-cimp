package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

/**
 * @classname:  SheetDesignSection
 * @description: 表格设计区段
 * @author:        Nil
 * @date:            2017年8月13日 下午3:42:34
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_SECTION")
public class SheetDesignSection implements java.io.Serializable {
    private static final long serialVersionUID = 108149174063573179L;
    // Fields
    /*** 标识 */
    private UUID id;
    /*** 设计标识 */
    private UUID designId;
    /*** 名称 */
    private String name;
    /*** 描述 */
    private String description;
    /*** 控制行起始 */
    private Integer ctrlRowStart;
    /*** 控制列起始 */
    private Integer ctrlColumnStart;
    /*** 控制行结束 */
    private Integer ctrlRowEnd;
    /*** 控制列结束 */
    private Integer ctrlColumnEnd;
    /*** 数据填充方式 */
    private Byte dataFillType;
    /*** 起始行号 */
    private Integer startRowNo;
    /*** 起始列号 */
    private Integer startColumnNo;
    /*** 结束行号 */
    private Integer endRowNo;
    /*** 结束列号 */
    private Integer endColumnNo;
    /*** 列扩充间距 */
    private Integer expandColumnSpan;
    /*** 行扩充间距 */
    private Integer expandRowSpan;
    /*** 扩展方向 */
    private Integer expandDirection;
    /*** 是否自动扩展 */ 
    private Boolean autoExpand;
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
    
    /**块颜色*/
    private String color;
    /**列号显示为数字*/
    private Boolean colNum;
    /**可汇总*/
    private Boolean summaryable;
    /**可编辑*/
    private Boolean editable;
    /**父id*/
    private UUID parentId;
    
    private Collection<SheetDesignSection> childrens;
    
    /**是否叶子*/
    private boolean leaf;
    /**数据块编号*/
    private String sectionNo;
    /**编号+名称*/
    private String nameEx;
    
    /**数据绑定*/
    private String bindValue;
    // Constructors

    /** default constructor */
    public SheetDesignSection() {
    }

    /** minimal constructor */
    public SheetDesignSection(UUID id, UUID designId, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignSection(UUID id, UUID designId, String name, String description, Integer ctrlColumnEnd,
            Integer ctrlColumnStart, Integer ctrlRowEnd, Integer ctrlRowStart, Byte dataFillType, Integer endColumnNo,
            Integer endRowNo, Integer startColumnNo, Integer startRowNo, Integer expandColumnSpan,
            Integer expandRowSpan, Integer expandDirection, Boolean autoExpand, Integer ordinal, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.designId = designId;
        this.name = name;
        this.description = description;
        this.ctrlColumnEnd = ctrlColumnEnd;
        this.ctrlColumnStart = ctrlColumnStart;
        this.ctrlRowEnd = ctrlRowEnd;
        this.ctrlRowStart = ctrlRowStart;
        this.dataFillType = dataFillType;
        this.endColumnNo = endColumnNo;
        this.endRowNo = endRowNo;
        this.startColumnNo = startColumnNo;
        this.startRowNo = startRowNo;
        this.expandColumnSpan = expandColumnSpan;
        this.expandRowSpan = expandRowSpan;
        this.expandDirection = expandDirection;
        this.autoExpand = autoExpand;
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

    @Column(name = "DESIGN_ID", nullable = false)
    public UUID getDesignId() {
        return this.designId;
    }
    public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Column(name = "NAME", length = 100)
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "CTRL_COLUMN_END", precision = 8, scale = 0)
    public Integer getCtrlColumnEnd() {
        return this.ctrlColumnEnd;
    }
    public void setCtrlColumnEnd(Integer ctrlColumnEnd) {
        this.ctrlColumnEnd = ctrlColumnEnd;
    }

    @Column(name = "CTRL_COLUMN_START", precision = 8, scale = 0)
    public Integer getCtrlColumnStart() {
        return this.ctrlColumnStart;
    }
    public void setCtrlColumnStart(Integer ctrlColumnStart) {
        this.ctrlColumnStart = ctrlColumnStart;
    }

    @Column(name = "CTRL_ROW_END", precision = 8, scale = 0)
    public Integer getCtrlRowEnd() {
        return this.ctrlRowEnd;
    }
    public void setCtrlRowEnd(Integer ctrlRowEnd) {
        this.ctrlRowEnd = ctrlRowEnd;
    }

    @Column(name = "CTRL_ROW_START", precision = 8, scale = 0)
    public Integer getCtrlRowStart() {
        return this.ctrlRowStart;
    }
    public void setCtrlRowStart(Integer ctrlRowStart) {
        this.ctrlRowStart = ctrlRowStart;
    }

    @Column(name = "DATA_FILL_TYPE", precision = 2, scale = 0)
    public Byte getDataFillType() {
        return this.dataFillType;
    }
    public void setDataFillType(Byte dataFillType) {
        this.dataFillType = dataFillType;
    }

    @Column(name = "END_COLUMN_NO", precision = 8, scale = 0)
    public Integer getEndColumnNo() {
        return this.endColumnNo;
    }
    public void setEndColumnNo(Integer endColumnNo) {
        this.endColumnNo = endColumnNo;
    }

    @Column(name = "END_ROW_NO", precision = 8, scale = 0)
    public Integer getEndRowNo() {
        return this.endRowNo;
    }
    public void setEndRowNo(Integer endRowNo) {
        this.endRowNo = endRowNo;
    }

    @Column(name = "START_COLUMN_NO", precision = 8, scale = 0)
    public Integer getStartColumnNo() {
        return this.startColumnNo;
    }
    public void setStartColumnNo(Integer startColumnNo) {
        this.startColumnNo = startColumnNo;
    }

    @Column(name = "START_ROW_NO", precision = 8, scale = 0)
    public Integer getStartRowNo() {
        return this.startRowNo;
    }
    public void setStartRowNo(Integer startRowNo) {
        this.startRowNo = startRowNo;
    }

    @Column(name = "EXPAND_COLUMN_SPAN", precision = 8, scale = 0)
    public Integer getExpandColumnSpan() {
        return this.expandColumnSpan;
    }
    public void setExpandColumnSpan(Integer expandColumnSpan) {
        this.expandColumnSpan = expandColumnSpan;
    }

    @Column(name = "EXPAND_ROW_SPAN", precision = 8, scale = 0)
    public Integer getExpandRowSpan() {
        return this.expandRowSpan;
    }
    public void setExpandRowSpan(Integer expandRowSpan) {
        this.expandRowSpan = expandRowSpan;
    }

    @Column(name = "EXPAND_DIRECTION", precision = 8, scale = 0)
    public Integer getExpandDirection() {
        return this.expandDirection;
    }
    public void setExpandDirection(Integer expandDirection) {
        this.expandDirection = expandDirection;
    }

    @Column(name = "AUTO_EXPAND", precision = 1, scale = 0)
    public Boolean getAutoExpand() {
        return this.autoExpand==null?false:this.autoExpand;
    }
    public void setAutoExpand(Boolean autoExpand) {
        this.autoExpand = autoExpand;
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

    @Column(name="COLOR")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name="COL_NUM")
	public Boolean getColNum() {
		return colNum;
	}

	public void setColNum(Boolean colNum) {
		this.colNum = colNum;
	}

	@Column(name="SUMMARYABLE")
	public Boolean getSummaryable() {
		return summaryable;
	}

	public void setSummaryable(Boolean summaryable) {
		this.summaryable = summaryable;
	}

	@Column(name="EDITABLE")
	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	@Column(name="PARENT_ID")
	public UUID getParentId() {
		return parentId;
	}

	public void setParentId(UUID parentId) {
		this.parentId = parentId;
	}

	

	@Column(name="SECTION_NO")
	public String getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}
	
	@Transient
	public Collection<SheetDesignSection> getChildrens() {
		return childrens;
	}

	public void setChildrens(Collection<SheetDesignSection> childrens) {
		this.childrens = childrens;
	}

	@Transient
	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	@Transient
	public String getNameEx() {
		return "["+this.sectionNo+"]"+this.name;
	}

	public void setNameEx(String nameEx) {
		this.nameEx = nameEx;
	}

	@Transient
	public String getBindValue() {
		return bindValue;
	}

	public void setBindValue(String bindValue) {
		this.bindValue = bindValue;
	}

}