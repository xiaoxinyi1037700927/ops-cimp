package com.sinosoft.ops.cimp.entity.sys.user;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Jay
 * date : 2018/9/5
 * des :
 */
@Entity
@Table(name = "PROJECT_POSITION_COUNT_VIEW")
@Subselect("SELECT PJ.PROJECT_ID,CAP.POSITION_ID,COUNT(CAP.POSITION_ID) as POSITION_NUM " +
        "from PROJECT_CADREINFO PJ LEFT JOIN CADRE_ASSIGN_POSITION CAP ON PJ.ID = " +
        "CAP.PROJECT_CADRE_INFO_ID " +
        "GROUP BY PJ.PROJECT_ID,CAP.POSITION_ID")
@Synchronize({"PROJECT_CADRE_INFO","CADRE_ASSIGN_POSITION"})
public class ProjectPositionCountView {
    @Id
    @Column(name = "PROJECT_ID")
    private String projectId;

    @Column(name = "POSITION_ID")
    private String positionId;

    @Column(name = "POSITION_NUM")
    private String positionNum;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionNum() {
        return positionNum;
    }

    public void setPositionNum(String positionNum) {
        this.positionNum = positionNum;
    }
}
