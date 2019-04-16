package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Jay
 * date : 2018/9/4
 * des :
 */
@Entity
@Table(name = "PROJECT_POSITION")
public class ProjectPosition {
    /**
     * 主键标识ID
     */
    private String id;
    /**
     * 方案ID
     */
    private String projectId;
    /**
     * 任职单位
     */
    private String organizationId;
    /**
     * 任职单位名称
     */
    private String organizationName;
    /**
     * 任职职位
     */
    private String organizationPositionId;
    /**
     * 任职职位名称
     */
    private String organizationPositionName;
    /**
     * 所属分组
     */
    private String groupId;

    /**
     * 分组名称
     * */
    private String groupName;
    /**
     * 人选范围
     */
    private String selectRange;
    /**
     * 人选方式
     */
    private String selectType;
    /**
     * 人选程序
     */
    private String selectProgram;
    /**
     * 人选条件
     */
    private String selectCondition;
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
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    @Column(length = 50)
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    @Column(length = 120)
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    @Column(length = 50)
    public String getOrganizationPositionId() {
        return organizationPositionId;
    }

    public void setOrganizationPositionId(String organizationPositionId) {
        this.organizationPositionId = organizationPositionId;
    }

    @Column(length = 200)
    public String getOrganizationPositionName() {
        return organizationPositionName;
    }

    public void setOrganizationPositionName(String organizationPositionName) {
        this.organizationPositionName = organizationPositionName;
    }

    @Column(length = 50)
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Column(length = 500)
    public String getSelectRange() {
        return selectRange;
    }

    public void setSelectRange(String selectRange) {
        this.selectRange = selectRange;
    }
    @Column(length = 50)
    public String getSelectType() {
        return selectType;
    }

    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }
    @Column(length = 50)
    public String getSelectProgram() {
        return selectProgram;
    }

    public void setSelectProgram(String selectProgram) {
        this.selectProgram = selectProgram;
    }
    @Column(length = 50)
    public String getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(String selectCondition) {
        this.selectCondition = selectCondition;
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
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return "ProjectPosition{" +
                "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationPositionId='" + organizationPositionId + '\'' +
                ", organizationPositionName='" + organizationPositionName + '\'' +
                ", selectRange='" + selectRange + '\'' +
                ", selectType='" + selectType + '\'' +
                ", selectProgram='" + selectProgram + '\'' +
                ", selectCondition='" + selectCondition + '\'' +
                '}';
    }
}
