package com.sinosoft.ops.cimp.entity.sys.sysapp;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统应用表集合
 */
@Entity
@Table(name = "SYS_APP_TABLE_SET")
public class SysAppTableSet implements Serializable {
    private static final long serialVersionUID = -8191624682378471385L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 表分组ID
     */
    @Column(name = "SYS_APP_TABLE_GROUP_ID", length = 36)
    private String sysAppTableGroupId;
    /**
     * 系统表ID
     */
    @Column(name = "SYS_TABLE_ID", length = 36)
    private String sysTableId;
    /**
     * 表名称
     */
    @Column(name = "NAME", length = 120)
    private String name;
    /**
     * 英文表名称
     */
    @Column(name = "NAME_EN", length = 120)
    private String nameEn;
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

    public String getSysAppTableGroupId() {
        return sysAppTableGroupId;
    }

    public void setSysAppTableGroupId(String sysAppTableGroupId) {
        this.sysAppTableGroupId = sysAppTableGroupId;
    }

    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
