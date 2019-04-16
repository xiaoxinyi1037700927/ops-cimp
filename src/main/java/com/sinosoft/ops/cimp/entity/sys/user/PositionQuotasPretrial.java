package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 职数预审方案表
 */
@Entity
@Table(name = "POSITION_QUOTAS_PRETRIAL")
public class PositionQuotasPretrial {

    /**
     * 编号标识ID
     */
    private String id;
    /**
     * 预审方案名称
     */
    private String name;
    /**
     * 编制部门审核情况
     */
    private String quotasOrgVerifyResult;
    /**
     * 编制部门审核发现问题
     */
    private String quotasOrgVerifyQuestion;
    /**
     * 上级监督部门审核情况
     */
    private String supervisionVerifyResult;
    /**
     * 所处阶段(0 运行中, 1 已结束)
     */
    private String stage;
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

    @Column(length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 1)
    public String getQuotasOrgVerifyResult() {
        return quotasOrgVerifyResult;
    }

    public void setQuotasOrgVerifyResult(String quotasOrgVerifyResult) {
        this.quotasOrgVerifyResult = quotasOrgVerifyResult;
    }

    @Column(length = 2000)
    public String getQuotasOrgVerifyQuestion() {
        return quotasOrgVerifyQuestion;
    }

    public void setQuotasOrgVerifyQuestion(String quotasOrgVerifyQuestion) {
        this.quotasOrgVerifyQuestion = quotasOrgVerifyQuestion;
    }

    @Column(length = 1)
    public String getSupervisionVerifyResult() {
        return supervisionVerifyResult;
    }

    public void setSupervisionVerifyResult(String supervisionVerifyResult) {
        this.supervisionVerifyResult = supervisionVerifyResult;
    }

    @Column(length = 1)
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
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
}
