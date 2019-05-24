package com.sinosoft.ops.cimp.entity.archive;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * BUS_ARCHAPPLY_PERSON entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BUS_ARCHAPPLY_PERSON")
public class BusArchApplyPerson implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -8465178198105129201L;
    private String id;
    private String applyId;
    private String empid;
    private String name;
    private String post;
    private String description;
    private Integer ordinal;
    private String depid;

    @Column(name = "DEP_ID")
    public String getDepid() {
        return depid;
    }

    public void setDepid(String depid) {
        this.depid = depid;
    }


    // Constructors

    /** default constructor */
    public BusArchApplyPerson() {
    }

    /** minimal constructor */
    public BusArchApplyPerson(String id,String applyid, String empid, String name, String post,String depid) {
        this.id = id;
        this.applyId=applyid;
        this.empid = empid;
        this.name = name;
        this.post = post;
        this.depid = depid;
    }

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name = "APPLY_ID", nullable = false)
    public String getApplyId() {
        return this.applyId;
    }

    public void setApplyId(String applyid) {
        this.applyId = applyid;
    }

    @Column(name = "NAME", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "EMP_ID")
    public String getEmpid() {
        return this.empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }   

    @Column(name = "POST")
    public String getPost() {
        return this.post;
    }

    public void setPost(String post) {
        this.post = post;
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