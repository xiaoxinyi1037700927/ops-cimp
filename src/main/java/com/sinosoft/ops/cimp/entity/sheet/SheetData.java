package com.sinosoft.ops.cimp.entity.sheet;

import com.sinosoft.ops.cimp.common.model.Trackable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;



/**
 * @classname:  SheetData
 * @description: 表格数据
 * @author:        Nil
 * @date:            2017年10月22日 下午5:04:57
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DATA")
public class SheetData implements java.io.Serializable,Trackable {

    // Fields
    private static final long serialVersionUID = 5020314994893376784L;
    /*** 标识 */
    private UUID id;
    /*** 表格标识 */
    private UUID sheetId;
    /*** 行号 */
    private Integer rowNo;
    /*** 列号 */
    private Integer columnNo;
    /*** 数字型值 */
    private Long numberValue;
    /*** 数字型值的小数位数 */
    private Byte numberScale;
    /*** 字符串型值 */
    private String stringValue;
    /*** 值类型 */
    private Byte valueType;
    /*** 是否已被修改 */
    private Boolean beingEdited;
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
    
    private Integer ctrlRowNo;
    
    private Integer ctrlColumnNo;
    
    private String sheetName;
    
    /**数据块编号*/
    private String sectionNo;
    

    // Constructors

    /** default constructor */
    public SheetData() {
    }

    /** minimal constructor */
    public SheetData(UUID id, UUID sheetId, Integer rowNo, Integer columnNo, Boolean beingEdited, Byte status,
            Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.sheetId = sheetId;
        this.rowNo = rowNo;
        this.columnNo = columnNo;
        this.beingEdited = beingEdited;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetData(UUID id, UUID sheetId, Integer rowNo, Integer columnNo, Long numberValue, Byte numberScale,
            String stringValue, Byte valueType, Boolean beingEdited, Integer ordinal, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy) {
        this.id = id;
        this.sheetId = sheetId;
        this.rowNo = rowNo;
        this.columnNo = columnNo;
        this.numberValue = numberValue;
        this.numberScale = numberScale;
        this.stringValue = stringValue;
        this.valueType = valueType;
        this.beingEdited = beingEdited;
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

    @Column(name = "SHEET_ID", nullable = false)
    public UUID getSheetId() {
        return this.sheetId;
    }
    public void setSheetId(UUID sheetId) {
        this.sheetId = sheetId;
    }

    @Column(name = "ROW_NO", nullable = false, precision = 8, scale = 0)
    public Integer getRowNo() {
        return this.rowNo;
    }
    public void setRowNo(Integer rowNo) {
        this.rowNo = rowNo;
    }

    @Column(name = "COLUMN_NO", nullable = false, precision = 8, scale = 0)
    public Integer getColumnNo() {
        return this.columnNo;
    }
    public void setColumnNo(Integer columnNo) {
        this.columnNo = columnNo;
    }

    @Column(name = "NUMBER_VALUE", precision = 18, scale = 0)
    public Long getNumberValue() {
        return this.numberValue;
    }
    public void setNumberValue(Long numberValue) {
        this.numberValue = numberValue;
    }

    @Column(name = "NUMBER_SCALE", precision = 2, scale = 0)
    public Byte getNumberScale() {
        return this.numberScale;
    }
    public void setNumberScale(Byte numberScale) {
        this.numberScale = numberScale;
    }

    @Column(name = "STRING_VALUE")
    public String getStringValue() {
        return this.stringValue;
    }
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @Column(name = "VALUE_TYPE", precision = 2, scale = 0)
    public Byte getValueType() {
        return this.valueType;
    }
    public void setValueType(Byte valueType) {
        this.valueType = valueType;
    }

    @Column(name = "BEING_EDITED", nullable = false, precision = 1, scale = 0)
    public Boolean getBeingEdited() {
        return this.beingEdited;
    }
    public void setBeingEdited(Boolean beingEdited) {
        this.beingEdited = beingEdited;
    }
    
    @Column(name="SECTION_NO")
    public String getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
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
	public Integer getCtrlRowNo() {
		return ctrlRowNo;
	}

	public void setCtrlRowNo(Integer ctrlRowNo) {
		this.ctrlRowNo = ctrlRowNo;
	}

	@Transient
	public Integer getCtrlColumnNo() {
		return ctrlColumnNo;
	}

	public void setCtrlColumnNo(Integer ctrlColumnNo) {
		this.ctrlColumnNo = ctrlColumnNo;
	}
	
	@Transient
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	@Override
	public String toString() {
		return "SheetData [id=" + id + ", sheetId=" + sheetId + ", rowNo=" + rowNo + ", columnNo=" + columnNo
				+ ", numberValue=" + numberValue + ", numberScale=" + numberScale + ", stringValue=" + stringValue
				+ ", valueType=" + valueType + ", beingEdited=" + beingEdited + ", ordinal=" + ordinal + ", status="
				+ status + ", createdTime=" + createdTime + ", createdBy=" + createdBy + ", lastModifiedTime="
				+ lastModifiedTime + ", lastModifiedBy=" + lastModifiedBy + ", ctrlRowNo=" + ctrlRowNo
				+ ", ctrlColumnNo=" + ctrlColumnNo + ", sheetName=" + sheetName + ", sectionNo=" + sectionNo + "]";
	}

	
    
    
}