package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * SheetDesignDataSource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHEET_DESIGN_DATA_SOURCE")

public class SheetDesignDataSource implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -1457110480110902042L;
    private UUID id;
    private UUID designId;
    private UUID datasourceId;
    private Integer ordinal;
    private Byte status;
    private Timestamp createdTime;
    private UUID createdBy;
    private Timestamp lastModifiedTime;
    private UUID lastModifiedBy;
    private Integer categoryId;
    private String sectionNo;
    /**数据查询sql*/
    private String sql;
    /**数据范围*/
    private String dataRange;
    
    private String name;
    
    private Integer startRowNo;
    
    private Integer endRowNo;
    
    private Integer startColumnNo;
    
    private Integer endColumnNo;

    // Constructors

    /** default constructor */
    public SheetDesignDataSource() {
    }

    /** minimal constructor */
    public SheetDesignDataSource(UUID id, UUID designId, UUID datasourceId, Byte status, Timestamp createdTime,
            Timestamp lastModifiedTime) {
        this.id = id;
        this.designId = designId;
        this.datasourceId = datasourceId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignDataSource(UUID id, UUID designId, UUID datasourceId, Integer ordinal, Byte status,
			Date createdTime, UUID createdBy, Date lastModifiedTime, UUID lastModifiedBy, String sql,
			String dataRange, String name,Integer categoryId,String sectionNo) {
		super();
		this.id = id;
		this.designId = designId;
		this.datasourceId = datasourceId;
		this.ordinal = ordinal;
		this.status = status;
		this.createdTime = new Timestamp(createdTime.getTime());
		this.createdBy = createdBy;
		this.lastModifiedTime = new Timestamp(lastModifiedTime.getTime());
		this.lastModifiedBy = lastModifiedBy;
		this.sql = sql;
		this.dataRange = dataRange;
		this.name = name;
        this.categoryId = categoryId;
        this.sectionNo=sectionNo;
	}

    public SheetDesignDataSource(UUID id, UUID designId, UUID datasourceId, Integer ordinal, Byte status,
			Date createdTime, UUID createdBy, Date lastModifiedTime, UUID lastModifiedBy, 
			String sql, String dataRange, String name, Integer startRowNo, Integer endRowNo, Integer startColumnNo,
			Integer endColumnNo,Integer categoryId,String sectionNo) {
		super();
		this.id = id;
		this.designId = designId;
		this.datasourceId = datasourceId;
		this.ordinal = ordinal;
		this.status = status;
		this.createdTime = new Timestamp(createdTime.getTime());;
		this.createdBy = createdBy;
		this.lastModifiedTime = new Timestamp(lastModifiedTime.getTime());;
		this.lastModifiedBy = lastModifiedBy;
		this.sql = sql;
		this.dataRange = dataRange;
		this.name = name;
		this.startRowNo = startRowNo;
		this.endRowNo = endRowNo;
		this.startColumnNo = startColumnNo;
		this.endColumnNo = endColumnNo;
		this.categoryId=categoryId;
		this.sectionNo=sectionNo;
	}
    
    

	public SheetDesignDataSource(UUID designId, UUID datasourceId, String dataRange,
			String name, Integer categoryId) {
		super();
		this.designId = designId;
		this.datasourceId = datasourceId;
		this.categoryId = categoryId;
		this.dataRange = dataRange;
		this.name = name;
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

    @Column(name = "DATASOURCE_ID", nullable = false)

    public UUID getDatasourceId() {
        return this.datasourceId;
    }

    public void setDatasourceId(UUID datasourceId) {
        this.datasourceId = datasourceId;
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

    //@Column(name="SQL")
    @Transient
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	//@Column(name="DATA_RANGE")
	@Transient
	public String getDataRange() {
		return dataRange;
	}

	public void setDataRange(String dataRange) {
		this.dataRange = dataRange;
	}

	@Transient
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Transient
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Column(name = "SECTION_NO", length = 10)
    public String getSectionNo() {        return this.sectionNo;    }
    public void setSectionNo(String sectionNo) {        this.sectionNo = sectionNo;    }

    @Column(name="START_ROW_NO")
	public Integer getStartRowNo() {
		return startRowNo;
	}

	public void setStartRowNo(Integer startRowNo) {
		this.startRowNo = startRowNo;
	}

	@Column(name="END_ROW_NO")
	public Integer getEndRowNo() {
		return endRowNo;
	}

	public void setEndRowNo(Integer endRowNo) {
		this.endRowNo = endRowNo;
	}

	@Column(name="START_COLUMN_NO")
	public Integer getStartColumnNo() {
		return startColumnNo;
	}

	public void setStartColumnNo(Integer startColumnNo) {
		this.startColumnNo = startColumnNo;
	}

	@Column(name="END_COLUMN_NO")
	public Integer getEndColumnNo() {
		return endColumnNo;
	}

	public void setEndColumnNo(Integer endColumnNo) {
		this.endColumnNo = endColumnNo;
	}
    
    

}