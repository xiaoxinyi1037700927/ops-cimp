package com.sinosoft.ops.cimp.entity.archive;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BUS_ARCHAPPLY_Detail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BUS_ARCHAPPLY_Detail")
public class BusArchApplyDetail implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -8445178198105159201L;
    private String id;
    private String applyid;
    private String personid;
    private String empid;
    private String archiveMaterialId;
    private String categoryId;
    private String archiveMaterialText;
    private String description;
    private Integer ordinal;

    // Constructors

    /** default constructor */
    public BusArchApplyDetail() {
    }

    /** minimal constructor */
    public BusArchApplyDetail(String id,String applyid, String empid,String archiveMaterialId, String categoryId, String archiveMaterialText) {
        this.id = id;
        this.applyid = applyid;
        this.empid = empid;
        this.archiveMaterialId = archiveMaterialId;
        this.categoryId = categoryId;
        this.archiveMaterialText = archiveMaterialText;
    }

    // Property accessors
    @Id
    @Column(name = "ID", nullable = false)

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "APPLY_ID", nullable = false)
    public String getApplyid() {
        return this.applyid;
    }
    public void setApplyid(String applyid) {
        this.applyid = applyid;
    }
    
    @Column(name = "PERSON_ID", nullable = false)
    public String getPersonid() {
        return this.personid;
    }
    public void setPersonid(String personid) {
        this.personid = personid;
    }

    @Column(name = "EMP_ID")
    public String getEmpid() {
        return this.empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    @Column(name = "ARCHIVEMATERIAL_ID")
    public String getArchiveMaterialId() {
        return this.archiveMaterialId;
    }
    public void setArchiveMaterialId(String archiveMaterialId) {
        this.archiveMaterialId = archiveMaterialId;
    }
    
    @Column(name = "CATEGORY_ID")
    public String getCategoryId() {
        return this.categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    @Column(name = "ARCHIVEMATERIALTEXT")
    public String getArchiveMaterialText() {
        return this.archiveMaterialText;
    }

    public void setArchiveMaterialText(String archiveMaterialText) {
        this.archiveMaterialText = archiveMaterialText;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ORDINAL", precision = 8, scale = 0)
    public Integer getOrdinal() {
        return this.ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }
}