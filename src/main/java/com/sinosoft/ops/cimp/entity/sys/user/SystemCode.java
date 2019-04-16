package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 字典表
 */
@Entity
@Table(name="SYSTEM_CODE")
public class SystemCode {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典值
     */
    private String code;
    /**
     * 所属字典类型ID
     */
    private String systemCodeTypeId;
    /**
     * 字典类型code
     */
    private String typeCode;
    /**
     * 字典简称
     */
    private String briefName;
    /**
     * 父节点代码
     */
    private String parentCode;
    /**
     * 父节点Id
     */
    private String parentId;
    /**
     * 字典描述
     */
    private String description;
    /**
     * 排序
     */
    private Integer ordinal;
    /**
     * 状态
     */
    private Integer status;
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

    @Column(length = 32)
    public String getSystemCodeTypeId() {
        return systemCodeTypeId;
    }

    public void setSystemCodeTypeId(String systemCodeTypeId) {
        this.systemCodeTypeId = systemCodeTypeId;
    }

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 50)
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @Column(length = 50)
    public String getBriefName() {
        return briefName;
    }

    public void setBriefName(String briefName) {
        this.briefName = briefName;
    }

    @Column(length = 50)
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    @Column(length = 50)
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length = 8)
    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    @Column(length = 2)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "SystemCode{" +
                "id='" + id + '\'' +
                ", systemCodeTypeId='" + systemCodeTypeId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", typeCode='" + typeCode + '\'' +
                ", briefName='" + briefName + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", description='" + description + '\'' +
                ", ordinal=" + ordinal +
                ", status=" + status +
                '}';
    }
}
