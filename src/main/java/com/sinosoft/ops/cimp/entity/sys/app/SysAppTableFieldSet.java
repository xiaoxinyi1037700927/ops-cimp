package com.sinosoft.ops.cimp.entity.sys.app;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统应用字段集合
 */
@Entity
@Table(name = "SYS_APP_TABLE_FIELD_SET")
public class SysAppTableFieldSet implements Serializable {
    private static final long serialVersionUID = -2873316984633790491L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 字段分组ID
     */
    @Column(name = "SYS_APP_TABLE_FIELD_GROUP_ID", length = 36)
    private String sysAppTableFieldGroupId;
    /**
     * 系统表字段ID
     */
    @Column(name = "SYS_TABLE_FIELD_ID", length = 36)
    private String sysTableFieldId;
    /**
     * 字段名称
     */
    @Column(name = "NAME", length = 120)
    private String name;
    /**
     * 排序
     */
    @Column(name = "SORT", length = 10)
    private Integer sort;
    /**
     * 代码模板
     */
    @Column(name = "HTML", length = 300)
    private String html;
    /**
     * 脚本
     */
    @Column(name = "SCRIPT", length = 300)
    private String script;
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

    public String getSysAppTableFieldGroupId() {
        return sysAppTableFieldGroupId;
    }

    public void setSysAppTableFieldGroupId(String sysAppTableFieldGroupId) {
        this.sysAppTableFieldGroupId = sysAppTableFieldGroupId;
    }

    public String getSysTableFieldId() {
        return sysTableFieldId;
    }

    public void setSysTableFieldId(String sysTableFieldId) {
        this.sysTableFieldId = sysTableFieldId;
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

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
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
