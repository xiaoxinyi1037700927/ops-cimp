package com.sinosoft.ops.cimp.entity.sys.table;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统表
 */
@Entity
@Table(name = "SYS_TABLE")
public class SysTable implements Serializable {
    private static final long serialVersionUID = 9016588170580187273L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 类别ID
     */
    @Column(name = "SYS_TABLE_TYPE_ID", length = 36)
    private String sysTableTypeId;
    /**
     * 编号
     */
    @Column(name = "CODE", length = 10)
    private String code;
    /**
     * 中文表名
     */
    @Column(name = "NAME_CN", length = 120)
    private String nameCn;
    /**
     * 英文表名
     */
    @Column(name = "NAME_EN", length = 120)
    private String nameEn;
    /**
     * 表描述
     */
    @Column(name = "DESCRIPTION", length = 300)
    private String description;
    /**
     * 状态
     */
    @Column(name = "STATUS", length = 10)
    private String status;
    /**
     * 数据库中的表名
     */
    @Column(name = "DB_TABLE_NAME", length = 50)
    private String dbTableName;
    /**
     * 父级表名
     */
    @Column(name = "PARENT_DB_TABLE_NAME", length = 50)
    private String parentDbTableName;
    /**
     * 排序
     */
    @Column(name = "SORT", length = 10)
    private Integer sort;
    /**
     * 创建人
     */
    @Column(name = "CREATE_ID", length = 36)
    private String createId;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 修改人
     */
    @Column(name = "MODIFY_ID", length = 36)
    private String modifyId;
    /**
     * 修改时间
     */
    @Column(name = "MODIFY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysTableTypeId() {
        return sysTableTypeId;
    }

    public void setSysTableTypeId(String sysTableTypeId) {
        this.sysTableTypeId = sysTableTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDbTableName() {
        return dbTableName;
    }

    public void setDbTableName(String dbTableName) {
        this.dbTableName = dbTableName;
    }

    public String getParentDbTableName() {
        return parentDbTableName;
    }

    public void setParentDbTableName(String parentDbTableName) {
        this.parentDbTableName = parentDbTableName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
