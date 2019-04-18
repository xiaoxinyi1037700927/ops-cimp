package com.sinosoft.ops.cimp.entity.sys.permission;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PERMISSION_PAGE_OPERATION")
public class PermissionPageOperation {

    /**
     * 标识ID
     * */
    private String id;

    /**
     * 页面ID
     * */
    private String permissionPageId;

    /**
     * 操作名称
     * */
    private String name;

    /**
     * 描述
     * */
    private String description;

    /**
     * 操作ID
     * */
    private String operationId;

    /**
     * 排序
     * */
    private Integer sortNumber;

    /**
     * 创建人
     */
    private String createId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改人
     */
    private String modifyId;
    /**
     * 修改时间
     */
    private Date modifyTime;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(length = 50)
    public String getPermissionPageId() {
        return permissionPageId;
    }

    public void setPermissionPageId(String permissionPageId) {
        this.permissionPageId = permissionPageId;
    }

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50)
    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Column
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Column(length = 50)
    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    @Column
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(length = 50)
    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    @Column
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(length = 1000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
