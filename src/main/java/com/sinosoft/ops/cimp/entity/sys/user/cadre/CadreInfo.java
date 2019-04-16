package com.sinosoft.ops.cimp.entity.sys.user.cadre;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 干部表
 */
@Entity
@Table(name = "CADREINFO")
public class CadreInfo {

    /**
     * 用户唯一表示主键
     */
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 姓名拼音
     */
    private String namePy;
    /**
     * 性别
     */
    private String gender;
    /**
     * 出生日期
     */
    private Date birthDay;
    /**
     * 出生地
     */
    private String birthPlace;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 民族
     */
    private String nation;
    /**
     * 政治面貌
     */
    private String politicalStatus;
    /**
     * 身份证号
     */
    private String identityNumber;
    /**
     * 职务
     */
    private String duty;
    /**
     * 时任职级
     */
    private String rankId;
    /**
     * 入党时间
     */
    private Date joinPartyDate;
    /**
     * 党籍
     */
    private String joinPartyName;
    /**
     * 工作时间
     */
    private Date workDate;
    /**
     * 健康状况
     */
    private String healthStatus;
    /**
     * 干部关联管理状态(1现职, 2离退, 3调出, 4去世, 5其他人员)
     */
    private String manageStatus;
    /**
     * 人员管理类别
     */
    private String manageType;
    /**
     * 专业技术职务
     */
    private String professionPosition;
    /**
     * 专业专长
     */
    private String specialSkills;
    /**
     * 所属单位ID
     */
    private String organizationId;
    /**
     * 是否为部务会委员
     */
    private String isMinistry;
    /**
     * 是否为全务会委员
     */
    private String isNational;
    /**
     * 是否为常委会委员
     */
    private String isStanding;

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
    private Date ModifyTime;

    /**
     * 退休标记：1退休
     */
    private String retireState;

    /**
     * 联系电话
     */
    private String mobilePhone;

    /**
     * 当前学历，代码。全学历去学历表中查
     */
    private String education;

    /**
     * 集体内排序(不包含下级的的时候才能够上移下移，否则输入排序号)
     */
    private Integer sortNumber;

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 50)
    public String getNamePy() {
        return namePy;
    }

    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }

    @Column(length = 100)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column
    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Column(length = 80)
    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Column(length = 80)
    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    @Column(length = 100)
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }


    @Column(length = 100)
    public String getPoliticalStatus() {
        return politicalStatus;
    }

    public void setPoliticalStatus(String politicalStatus) {
        this.politicalStatus = politicalStatus;
    }

    @Column(length = 50)
    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    @Column(length = 500)
    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    @Column(length = 50)
    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    @Column
    public Date getJoinPartyDate() {
        return joinPartyDate;
    }

    public void setJoinPartyDate(Date joinPartyDate) {
        this.joinPartyDate = joinPartyDate;
    }

    @Column(length = 200)
    public String getJoinPartyName() {
        return joinPartyName;
    }

    public void setJoinPartyName(String joinPartyName) {
        this.joinPartyName = joinPartyName;
    }

    @Column
    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    @Column(length = 100)
    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    @Column(length = 100)
    public String getManageStatus() {
        return manageStatus;
    }

    public void setManageStatus(String manageStatus) {
        this.manageStatus = manageStatus;
    }

    @Column(length = 50)
    public String getManageType() {
        return manageType;
    }

    public void setManageType(String manageType) {
        this.manageType = manageType;
    }

    @Column(length = 100)
    public String getProfessionPosition() {
        return professionPosition;
    }

    public void setProfessionPosition(String professionPosition) {
        this.professionPosition = professionPosition;
    }

    @Column(length = 100)
    public String getSpecialSkills() {
        return specialSkills;
    }

    public void setSpecialSkills(String specialSkills) {
        this.specialSkills = specialSkills;
    }

    @Column(length = 100)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Column(length = 50)
    public String getIsMinistry() {
        return isMinistry;
    }

    public void setIsMinistry(String isMinistry) {
        this.isMinistry = isMinistry;
    }

    @Column(length = 50)
    public String getIsNational() {
        return isNational;
    }

    public void setIsNational(String isNational) {
        this.isNational = isNational;
    }

    @Column(length = 50)
    public String getIsStanding() {
        return isStanding;
    }

    public void setIsStanding(String isStanding) {
        this.isStanding = isStanding;
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
        return ModifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        ModifyTime = modifyTime;
    }

    @Column(length = 2)
    public String getRetireState() {
        return retireState;
    }

    public void setRetireState(String retireState) {
        this.retireState = retireState;
    }

    @Column(length = 50)
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Column(length = 50)
    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    @Column(length = 10)
    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    @Override
    public String toString() {
        return "CadreInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDay=" + birthDay +
                ", birthPlace='" + birthPlace + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", nation='" + nation + '\'' +
                ", politicalStatus='" + politicalStatus + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", duty='" + duty + '\'' +
                ", rankId='" + rankId + '\'' +
                ", joinPartyDate=" + joinPartyDate +
                ", joinPartyName='" + joinPartyName + '\'' +
                ", workDate=" + workDate +
                ", healthStatus='" + healthStatus + '\'' +
                ", professionPosition='" + professionPosition + '\'' +
                ", specialSkills='" + specialSkills + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", isMinistry='" + isMinistry + '\'' +
                ", isNational='" + isNational + '\'' +
                ", isStanding='" + isStanding + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", ModifyTime=" + ModifyTime +
                ", retireState='" + retireState + '\'' +
                '}';
    }
}
