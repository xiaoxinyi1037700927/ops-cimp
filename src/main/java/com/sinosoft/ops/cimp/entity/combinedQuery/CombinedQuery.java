package com.sinosoft.ops.cimp.entity.combinedQuery;

import javax.persistence.*;
import java.util.Date;

/**
 * 组合查询
 */
@Entity
@Table(name = "COMBINED_QUERY")
public class CombinedQuery implements java.io.Serializable {
    private static final long serialVersionUID = -4148203667739310936L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "ID", length = 36)
    private String id;
    /**
     * 用户id
     */
    @Column(name = "USER_ID", length = 36)
    private String userId;
    /**
     * 角色id
     */
    @Column(name = "ROLE_ID", length = 36)
    private String roleId;
    /**
     * 组合查询名称
     */
    @Column(name = "NAME", length = 200)
    private String name;
    /**
     * 组合查询表达式
     */
    @Column(name = "EXPRESSION", length = 2000)
    private String expression;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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
