package com.sinosoft.ops.cimp.entity.sys.check;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 查错条件项
 */
@Entity
@Table(name = "SYS_CHECK_ITEM")
public class SysCheckItem implements java.io.Serializable {

    private static final long serialVersionUID = 9023757743610786891L;
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 查错条件id
     */
    @Column(name = "SYS_CHECK_CONDITION_ID", length = 36)
    private String sysCheckConditionId;
    /**
     * 查错条件类型
     */
    @Column(name = "TYPE", length = 2)
    private String type;
    /**
     * 查询条件部分
     */
    @Column(name = "WHERE_PART", length = 1000)
    private String wherePart;
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

    public String getSysCheckConditionId() {
        return sysCheckConditionId;
    }

    public void setSysCheckConditionId(String sysCheckConditionId) {
        this.sysCheckConditionId = sysCheckConditionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWherePart() {
        return wherePart;
    }

    public void setWherePart(String wherePart) {
        this.wherePart = wherePart;
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
