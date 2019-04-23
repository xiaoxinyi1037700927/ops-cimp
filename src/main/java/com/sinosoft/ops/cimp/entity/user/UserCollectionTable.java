package com.sinosoft.ops.cimp.entity.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏的表
 */
@Entity
@Table(name = "USER_COLLECTION_TABLE")
public class UserCollectionTable implements Serializable {
    private static final long serialVersionUID = -719038901366414093L;
    /**
     * 标识ID
     * */
    private String id;
    /**
     * 用户ID
     * */
    private String userId;
    /**
     * table Id
     */
    private String tableId;
    /**
     * nameCN
     */
    private String nameCN;
    /**
     * nameEN
     */
    private String nameEN;
    /**
     * 名称
     */
    private String name;
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
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(length = 50)
    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    @Column(length = 10)
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

    @Column(length = 100)
    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    @Column(length = 100)
    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    @Column(length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserCollectionTable{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", tableId='" + tableId + '\'' +
                ", nameCN='" + nameCN + '\'' +
                ", nameEN='" + nameEN + '\'' +
                ", name='" + name + '\'' +
                ", sortNumber=" + sortNumber +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
