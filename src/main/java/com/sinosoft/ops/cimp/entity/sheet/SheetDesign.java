package com.sinosoft.ops.cimp.entity.sheet;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

/**
 * @classname:  SheetDesign
 * @description: 表格设计
 * @author:        Nil
 * @date:            2017年10月22日 下午5:14:56
 * @version        1.0.0
 */
@Entity
@Table(name = "SHEET_DESIGN")
public class SheetDesign implements java.io.Serializable {
    private static final long serialVersionUID = 8515617099301343196L;
    /*** 表格设计的初始版本 */
    public static final int INITIAL_VERSION = 1;
    // Fields
    /*** 标识 */
    protected UUID id;
    /*** 名称 */
    protected String name;
    /*** 描述 */
    protected String description;
    /*** 表号 */
    protected String sheetNo;
    /*** 版本号 */
    protected Integer version;
    /*** 数据填入方式 */
    protected Byte dataFillType;
    /*** 填报说明 */
    protected String instructions;
    /*** 表格类型 */
    protected Byte type;
    /*** 顶部位置 */
    protected Integer top;
    /*** 左部位置 */
    protected Integer left;
    /*** 右部位置 */
    protected Integer right;
    /*** 底部位置 */
    protected Integer bottom;
    /*** 能否编辑 */
    protected Boolean editable;
    /*** 次序 */
    protected Integer ordinal;
    /*** 应用标识 */
    protected String appId;
    /*** 锁定状态 */
    protected Boolean locked;
    /*** 是否已归档 */
    protected Boolean archived;
    /*** 状态 */
    protected Byte status;
    /*** 创建时间 */
    protected Timestamp createdTime;
    /*** 创建人 */
    protected UUID createdBy;
    /*** 最后修改时间 */
    protected Timestamp lastModifiedTime;
    /*** 最后修改人 */
    protected UUID lastModifiedBy;
    /*** 发布时间 */
    protected Timestamp releaseTime;
    /*** 发布单位ID */
    protected String releaseComId;
    /*** 发布单位名称 */
    protected String releaseComName;
    /*** 有效期限 */
    protected Timestamp expiredTime;
    /*** 发布范围单位ID,多单位用逗号分隔 */
    protected String releaseComIds;
    /*** 发布范围单位名称,多单位用逗号分隔 */
    protected String releaseComNames;
    /*** 锁定密码 */
    protected String lockPassword;
    /*** 是否发布 */
    protected boolean isReleased;
        
    /*** 表格页面设置 */
    protected SheetDesignPageSetup pageSetup = null;
    /*** 表格载体 */
    protected Collection<SheetDesignCarrier> carriers = new LinkedList<SheetDesignCarrier>();
    /*** 表格单元格 */
    protected Collection<SheetDesignCell> cells = new LinkedList<SheetDesignCell>();
    /*** 表格条件 */
    protected Collection<SheetDesignCondition> conditions = new LinkedList<SheetDesignCondition>();
    /*** 表格数据区 */
    protected Collection<SheetDesignSection> sections = new LinkedList<SheetDesignSection>();
    /*** 表格表达式 */
    protected Collection<SheetDesignExpression> expressions = new LinkedList<SheetDesignExpression>();
    /*** 表格参数 */
    protected Collection<SheetDesignParameter> parameters = new LinkedList<SheetDesignParameter>();
    /*** 表格SQL */
    protected Collection<SheetDesignSql> sqls = new LinkedList<SheetDesignSql>();
    /*** 表格标签 */
    protected Collection<SheetDesignTag> tags = new LinkedList<SheetDesignTag>();
    /*** 表格变量 */
    protected Collection<SheetDesignVariable> variables = new LinkedList<SheetDesignVariable>();
    /*** 表格数据源 */
    protected Collection<SheetDesignDataSource> dataSources = new LinkedList<SheetDesignDataSource>();
    /*** 表格字段 */
    protected Collection<SheetDesignField> fields = new LinkedList<SheetDesignField>();

    protected Integer refNum;
    // Constructors

    /** default constructor */
    public SheetDesign() {
    }

    /** minimal constructor */
    public SheetDesign(UUID id, String name, String sheetNo, Integer version, Boolean locked, Boolean archived,
            Byte status, Timestamp createdTime, Timestamp lastModifiedTime) {
        this.id = id;
        this.name = name;
        this.sheetNo = sheetNo;
        this.version = version;
        this.locked = locked;
        this.archived = archived;
        this.status = status;
        this.createdTime = createdTime;
        this.lastModifiedTime = lastModifiedTime;
    }

    /** full constructor */
    public SheetDesign(UUID id, String name, String description, String sheetNo, Integer version,
            Byte dataFillType, String instructions, Byte type, Integer top, Integer left, Integer right, Integer bottom,
            Boolean editable, Integer ordinal, String appId, Boolean locked, Boolean archived, Byte status,
            Timestamp createdTime, UUID createdBy, Timestamp lastModifiedTime, UUID lastModifiedBy,
            Timestamp releaseTime, String releaseComId, String releaseComName, Timestamp expiredTime,
            String releaseComIds, String releaseComNames, String lockPassword) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sheetNo = sheetNo;
        this.version = version;
        this.dataFillType = dataFillType;
        this.instructions = instructions;
        this.type = type;
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
        this.editable = editable;
        this.ordinal = ordinal;
        this.appId = appId;
        this.locked = locked;
        this.archived = archived;
        this.status = status;
        this.createdTime = createdTime;
        this.createdBy = createdBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.releaseTime = releaseTime;
        this.releaseComId = releaseComId;
        this.releaseComName = releaseComName;
        this.expiredTime = expiredTime;
        this.releaseComIds = releaseComIds;
        this.releaseComNames = releaseComNames;
        this.lockPassword = lockPassword;
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
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "SHEET_NO", nullable = false, length = 20)
    public String getSheetNo() {
        return this.sheetNo;
    }
    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    @Column(name = "VERSION", nullable = false, precision = 8, scale = 0)
    public Integer getVersion() {
        return this.version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(name = "DATA_FILL_TYPE", precision = 2, scale = 0)
    public Byte getDataFillType() {
        return this.dataFillType;
    }
    public void setDataFillType(Byte dataFillType) {
        this.dataFillType = dataFillType;
    }

    @Column(name = "INSTRUCTIONS")
    public String getInstructions() {
        return this.instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Column(name = "TYPE", precision = 2, scale = 0)
    public Byte getType() {
        return this.type;
    }
    public void setType(Byte type) {
        this.type = type;
    }

    @Column(name = "TOP", precision = 8, scale = 0)
    public Integer getTop() {
        return this.top;
    }
    public void setTop(Integer top) {
        this.top = top;
    }

    @Column(name = "LEFT", precision = 8, scale = 0)
    public Integer getLeft() {
        return this.left;
    }
    public void setLeft(Integer left) {
        this.left = left;
    }

    @Column(name = "RIGHT", precision = 8, scale = 0)
    public Integer getRight() {
        return this.right;
    }
    public void setRight(Integer right) {
        this.right = right;
    }

    @Column(name = "BOTTOM", precision = 8, scale = 0)
    public Integer getBottom() {
        return this.bottom;
    }
    public void setBottom(Integer bottom) {
        this.bottom = bottom;
    }

    @Column(name = "EDITABLE", precision = 1, scale = 0)
    public Boolean getEditable() {
        return this.editable;
    }
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)
    public Integer getOrdinal() {
        return this.ordinal;
    }
    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(name = "APP_ID", length = 36)
    public String getAppId() {
        return this.appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "LOCKED", nullable = false, precision = 1, scale = 0)
    public Boolean getLocked() {
        return this.locked;
    }
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Column(name = "ARCHIVED", nullable = false, precision = 1, scale = 0)
    public Boolean getArchived() {
        return this.archived;
    }
    public void setArchived(Boolean archived) {
        this.archived = archived;
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

    @Column(name = "RELEASE_TIME")
    public Timestamp getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
		this.isReleased = (this.releaseTime != null ? true : false);
	}

    @Column(name = "RELEASE_COM_ID")
	public String getReleaseComId() {
		return releaseComId;
	}
	public void setReleaseComId(String releaseComId) {
		this.releaseComId = releaseComId;
	}

    @Column(name = "RELEASE_COM_NAME")
	public String getReleaseComName() {
		return releaseComName;
	}
	public void setReleaseComName(String releaseComName) {
		this.releaseComName = releaseComName;
	}

    @Column(name = "EXPIRED_TIME")
	public Timestamp getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Timestamp expiredTime) {
		this.expiredTime = expiredTime;
	}

    @Column(name = "RELEASE_COM_IDS")
	public String getReleaseComIds() {
		return releaseComIds;
	}

	public void setReleaseComIds(String releaseComIds) {
		this.releaseComIds = releaseComIds;
	}

    @Column(name = "RELEASE_COM_NAMES")
	public String getReleaseComNames() {
		return releaseComNames;
	}

	public void setReleaseComNames(String releaseComNames) {
		this.releaseComNames = releaseComNames;
	}

    @Column(name = "LOCK_PASSWORD")
	public String getLockPassword() {
		return lockPassword;
	}

	public void setLockPassword(String lockPassword) {
		this.lockPassword = lockPassword;
	}

	@Transient
	public boolean getIsReleased() {
		return isReleased;
	}

	public void setIsReleased(boolean isReleased) {
		this.isReleased = isReleased;
	}

	@Transient
    public Collection<SheetDesignCell> getCells() {
        return cells;
    }
    public void setCells(Collection<SheetDesignCell> cells) {
        this.cells = cells;
    }

    @Transient
    public SheetDesignPageSetup getPageSetup() {
        return pageSetup;
    }
    public void setPageSetup(SheetDesignPageSetup pageSetup) {
        this.pageSetup = pageSetup;
    }

    @Transient
    public Collection<SheetDesignVariable> getVariables() {
        return this.variables;
    }
    public void setVariables(Collection<SheetDesignVariable> variables) {
        this.variables = variables;
    }
    public void addVariable(SheetDesignVariable variable) {
        this.variables.add(variable);
    }
    public void removeVariable(SheetDesignVariable variable) {
        this.variables.remove(variable);
    }

    @Transient
    public Collection<SheetDesignParameter> getParameters() {
        return this.parameters;
    }
    public void setParameters(Collection<SheetDesignParameter> parameters) {
        this.parameters = parameters;
    }
    public void addParameter(SheetDesignParameter parameter) {
        this.parameters.add(parameter);
    }
    public void removeParameter(SheetDesignParameter parameter) {
        this.parameters.remove(parameter);
    }

    @Transient
    public Collection<SheetDesignTag> getTags() {
        return this.tags;
    }
    public void setTags(Collection<SheetDesignTag> tags) {
        this.tags = tags;
    }
    public void addTag(SheetDesignTag tag) {
        this.tags.add(tag);
    }
    public void removeTag(SheetDesignTag tag) {
        this.tags.remove(tag);
    }

    @Transient
    public Collection<SheetDesignSql> getSqls() {
        return this.sqls;
    }
    public void setSqls(Collection<SheetDesignSql> sqls) {
        this.sqls = sqls;
    }
    public void addSql(SheetDesignSql sql) {
        this.sqls.add(sql);
    }
    public void removeSql(SheetDesignSql sql) {
        this.sqls.remove(sql);
    }

    @Transient
    public Collection<SheetDesignExpression> getExpressions() {
        return this.expressions;
    }
    public void setExpressions(Collection<SheetDesignExpression> expressions) {
        this.expressions = expressions;
    }
    public void addExpression(SheetDesignExpression expression) {
        this.expressions.add(expression);
    }
    public void removeExpression(SheetDesignExpression expression) {
        this.expressions.remove(expression);
    }

    @Transient
    public Collection<SheetDesignSection> getSections() {
        return this.sections;
    }
    public void setSections(Collection<SheetDesignSection> sections) {
        this.sections = sections;
    }
    public void addSection(SheetDesignSection section) {
        this.sections.add(section);
    }
    public void removeSection(SheetDesignSection section) {
        this.sections.remove(section);
    }

    @Transient
    public Collection<SheetDesignCondition> getConditions() {
        return conditions;
    }
    public void setConditions(Collection<SheetDesignCondition> conditions) {
        this.conditions = conditions;
    }
    public void addRule(SheetDesignCondition condition) {
        this.conditions.add(condition);
    }
    public void removeRule(SheetDesignCondition condition) {
        this.conditions.remove(condition);
    }

    @Transient
    public Collection<SheetDesignCarrier> getCarriers() {
        return this.carriers;
    }
    public void setCarriers(Collection<SheetDesignCarrier> carriers) {
        this.carriers = carriers;
    }
    public void addCarrier(SheetDesignCarrier carrier) {
        this.carriers.add(carrier);
    }
    public void removeCarrier(SheetDesignCarrier carrier) {
        this.carriers.remove(carrier);
    }
    
    @Transient
    public Collection<SheetDesignDataSource> getDataSources() {
        return dataSources;
    }
    public void setDataSources(Collection<SheetDesignDataSource> dataSources) {
        this.dataSources = dataSources;
    }
    public void addDataSource(SheetDesignDataSource dataSource) {
        this.dataSources.add(dataSource);
    }
    public void removeDataSource(SheetDesignDataSource dataSource) {
        this.dataSources.remove(dataSource);
    }

    @Transient
    public Collection<SheetDesignField> getFields() {
        return this.fields;
    }
    public void setFields(Collection<SheetDesignField> fields) {
        this.fields = fields;
    }
    public void addField(SheetDesignField field) {
        this.fields.add(field);
    }
    public void removeField(SheetDesignField field) {
        this.fields.remove(field);
    }

    @Transient
    public Integer getRefNum() {
        return refNum;
    }

    public void setRefNum(Integer refNum) {
        this.refNum = refNum;
    }
}