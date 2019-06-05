package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @classname:  SheetDesignPageSetup
 * @description: 表格设计打印设置
 * @author:        Nil
 * @date:            2017年8月13日 下午3:41:05
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN_PAGE_SETUP")
public class SheetDesignPageSetup implements java.io.Serializable {
    private static final long serialVersionUID = 1475349807854852890L;
    // Fields
    /*** 报表设计标识 */
    private UUID designId;
    /*** 名称 */
    private String name;
    /*** 纸张尺寸 */
    private Double paperSize;
    /*** 上边距 */
    private Double topMargin;
    /*** 下边距 */
    private Double bottomMargin;
    /*** 左边距 */
    private Double leftMargin;
    /*** 右边距 */
    private Double rightMargin;
    /*** 打印质量 */
    private Double printQuality;
    /*** 缩放比例 */
    private Double zoom;
    /*** 描述 */
    private String description;
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
    
    //--新加字段
    /**纸张方向----0 横向，1 竖向*/
    private String paperRatation;
    /**页宽度*/
    private Double paperWidth;
    /**页高度*/
    private Double paperHeight;
    /**页眉尺寸*/
    private Double headerSize;
    /**页脚尺寸*/
    private Double footerSize;
    /**页眉*/
    private String header;
    /**页脚*/
    private String footer;
    /**打印区域上行数*/
    private Double printTopRow;
    /**下行数*/
    private Double printBottomRow;
    /**左列数*/
    private Double printLeftColumn;
    /**右列数*/
    private Double printRightColumn;
    /**输出*/
    private String output;
    /**分页顺序*/
    private String pageOrder;
    /**缩放方式*/
    private String zoomType;
    /**居中方式*/
    private String alignType;
    
    

    // Constructors

    /** default constructor */
    public SheetDesignPageSetup() {
    }

    /** minimal constructor */
    public SheetDesignPageSetup(UUID designId, Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.designId = designId;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesignPageSetup(UUID designId, String name, Double paperSize, Double topMargin, Double bottomMargin,
			Double leftMargin, Double rightMargin, Double printQuality, Double zoom, String description,
			Integer ordinal, Byte status, Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime,
			UUID lastModifiedBy, String paperRatation, Double paperWidth, Double paperHeight, Double headerSize,
			Double footerSize, String header, String footer, Double printTopRow, Double printBottomRow,
			Double printLeftColumn, Double printRightColumn, String output, String pageOrder) {
		super();
		this.designId = designId;
		this.name = name;
		this.paperSize = paperSize;
		this.topMargin = topMargin;
		this.bottomMargin = bottomMargin;
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
		this.printQuality = printQuality;
		this.zoom = zoom;
		this.description = description;
		this.ordinal = ordinal;
		this.status = status;
		this.createdTime = createdTime;
		this.createdBy = createdBy;
		this.lastModifiedTime = lastModifiedTime;
		this.lastModifiedBy = lastModifiedBy;
		this.paperRatation = paperRatation;
		this.paperWidth = paperWidth;
		this.paperHeight = paperHeight;
		this.headerSize = headerSize;
		this.footerSize = footerSize;
		this.header = header;
		this.footer = footer;
		this.printTopRow = printTopRow;
		this.printBottomRow = printBottomRow;
		this.printLeftColumn = printLeftColumn;
		this.printRightColumn = printRightColumn;
		this.output = output;
		this.pageOrder = pageOrder;
	}

    // Property accessors
    @Id
    @Column(name = "DESIGN_ID", unique = true, nullable = false)
    public UUID getDesignId() {
        return this.designId;
    }
	public void setDesignId(UUID designId) {
        this.designId = designId;
    }

    @Transient
    public UUID getId() {
        return this.designId;
    }
    public void setId(UUID designId) {
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

    @Column(name = "PAPER_SIZE", precision = 8)
    public Double getPaperSize() {
        return this.paperSize;
    }
    public void setPaperSize(Double paperSize) {
        this.paperSize = paperSize;
    }

    @Column(name = "TOP_MARGIN", precision = 8)
    public Double getTopMargin() {
        return this.topMargin;
    }
    public void setTopMargin(Double topMargin) {
        this.topMargin = topMargin;
    }

    @Column(name = "BOTTOM_MARGIN", precision = 8)
    public Double getBottomMargin() {
        return this.bottomMargin;
    }
    public void setBottomMargin(Double bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    @Column(name = "LEFT_MARGIN", precision = 8)
    public Double getLeftMargin() {
        return this.leftMargin;
    }
    public void setLeftMargin(Double leftMargin) {
        this.leftMargin = leftMargin;
    }

    @Column(name = "RIGHT_MARGIN", precision = 8)
    public Double getRightMargin() {
        return this.rightMargin;
    }
    public void setRightMargin(Double rightMargin) {
        this.rightMargin = rightMargin;
    }

    @Column(name = "PRINT_QUALITY", precision = 8)
    public Double getPrintQuality() {
        return this.printQuality;
    }
    public void setPrintQuality(Double printQuality) {
        this.printQuality = printQuality;
    }

    @Column(name = "ZOOM", precision = 8)
    public Double getZoom() {
        return this.zoom;
    }
    public void setZoom(Double zoom) {
        this.zoom = zoom;
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
    
    
    @Column(name="PAPER_RATATION")
	public String getPaperRatation() {
		return paperRatation;
	}

	public void setPaperRatation(String paperRatation) {
		this.paperRatation = paperRatation;
	}

	@Column(name="PAPER_WIDTH")
	public Double getPaperWidth() {
		return paperWidth;
	}

	public void setPaperWidth(Double paperWidth) {
		this.paperWidth = paperWidth;
	}

	@Column(name="PAPER_HEIGHT")
	public Double getPaperHeight() {
		return paperHeight;
	}

	public void setPaperHeight(Double paperHeight) {
		this.paperHeight = paperHeight;
	}

	@Column(name="HEADER_SIZE")
	public Double getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(Double headerSize) {
		this.headerSize = headerSize;
	}

	@Column(name="FOOTER_SIZE")
	public Double getFooterSize() {
		return footerSize;
	}

	public void setFooterSize(Double footerSize) {
		this.footerSize = footerSize;
	}

	@Column(name="HEADER")
	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Column(name="FOOTER")
	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	@Column(name="PRINT_TOP_ROW")
	public Double getPrintTopRow() {
		return printTopRow;
	}

	public void setPrintTopRow(Double printTopRow) {
		this.printTopRow = printTopRow;
	}

	@Column(name="PRINT_BOTTOM_ROW")
	public Double getPrintBottomRow() {
		return printBottomRow;
	}

	public void setPrintBottomRow(Double printBottomRow) {
		this.printBottomRow = printBottomRow;
	}

	@Column(name="PRINT_LEFT_COLUMN")
	public Double getPrintLeftColumn() {
		return printLeftColumn;
	}

	public void setPrintLeftColumn(Double printLeftColumn) {
		this.printLeftColumn = printLeftColumn;
	}

	@Column(name="PRINT_RIGHT_COLUMN")
	public Double getPrintRightColumn() {
		return printRightColumn;
	}

	public void setPrintRightColumn(Double printRightColumn) {
		this.printRightColumn = printRightColumn;
	}

	@Column(name="OUTPUT")
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Column(name="PAPER_ORDER")
	public String getPageOrder() {
		return pageOrder;
	}

	public void setPageOrder(String pageOrder) {
		this.pageOrder = pageOrder;
	}
	
	
	@Column(name="ZOOM_TYPE")
	public String getZoomType() {
		return zoomType;
	}

	public void setZoomType(String zoomType) {
		this.zoomType = zoomType;
	}

	@Column(name="ALIGN_TYPE")
	public String getAlignType() {
		return alignType;
	}

	public void setAlignType(String alignType) {
		this.alignType = alignType;
	}

	@Override
	public String toString() {
		return "SheetDesignPageSetup [designId=" + designId + ", name=" + name + ", paperSize=" + paperSize
				+ ", topMargin=" + topMargin + ", bottomMargin=" + bottomMargin + ", leftMargin=" + leftMargin
				+ ", rightMargin=" + rightMargin + ", printQuality=" + printQuality + ", zoom=" + zoom
				+ ", description=" + description + ", ordinal=" + ordinal + ", status=" + status + ", createdTime="
				+ createdTime + ", createdBy=" + createdBy + ", lastModifiedTime=" + lastModifiedTime
				+ ", lastModifiedBy=" + lastModifiedBy + ", paperRatation=" + paperRatation + ", paperWidth="
				+ paperWidth + ", paperHeight=" + paperHeight + ", headerSize=" + headerSize + ", footerSize="
				+ footerSize + ", header=" + header + ", footer=" + footer + ", printTopRow=" + printTopRow
				+ ", printBottomRow=" + printBottomRow + ", printLeftColumn=" + printLeftColumn + ", printRightColumn="
				+ printRightColumn + ", output=" + output + ", pageOrder=" + pageOrder + "]";
	}

	
    
}