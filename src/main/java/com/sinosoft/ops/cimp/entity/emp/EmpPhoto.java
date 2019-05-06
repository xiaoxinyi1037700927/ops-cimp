package com.sinosoft.ops.cimp.entity.emp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "EMP_PHOTO")
public class EmpPhoto {
    private String empId;
    private Timestamp lastUpdatedStamp;
    private Timestamp lastUpdatedTxStamp;
    private Timestamp createdStamp;
    private Timestamp createdTxStamp;
    private String filePath;
    private long seqid;
    private String photoUuid;
    private String status;
    private String status2;
    private String photoFormat;
    private String subId;
    private byte[] photoFile;

    @Basic
    @Column(name = "EMP_ID")
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    @Basic
    @Column(name = "LAST_UPDATED_STAMP")
    public Timestamp getLastUpdatedStamp() {
        return lastUpdatedStamp;
    }

    public void setLastUpdatedStamp(Timestamp lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }

    @Basic
    @Column(name = "LAST_UPDATED_TX_STAMP")
    public Timestamp getLastUpdatedTxStamp() {
        return lastUpdatedTxStamp;
    }

    public void setLastUpdatedTxStamp(Timestamp lastUpdatedTxStamp) {
        this.lastUpdatedTxStamp = lastUpdatedTxStamp;
    }

    @Basic
    @Column(name = "CREATED_STAMP")
    public Timestamp getCreatedStamp() {
        return createdStamp;
    }

    public void setCreatedStamp(Timestamp createdStamp) {
        this.createdStamp = createdStamp;
    }

    @Basic
    @Column(name = "CREATED_TX_STAMP")
    public Timestamp getCreatedTxStamp() {
        return createdTxStamp;
    }

    public void setCreatedTxStamp(Timestamp createdTxStamp) {
        this.createdTxStamp = createdTxStamp;
    }

    @Basic
    @Column(name = "FILE_PATH")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Basic
    @Column(name = "SEQID")
    public long getSeqid() {
        return seqid;
    }

    public void setSeqid(long seqid) {
        this.seqid = seqid;
    }

    @Basic
    @Column(name = "PHOTO_UUID")
    public String getPhotoUuid() {
        return photoUuid;
    }

    public void setPhotoUuid(String photoUuid) {
        this.photoUuid = photoUuid;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "STATUS2")
    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    @Basic
    @Column(name = "PHOTO_FORMAT")
    public String getPhotoFormat() {
        return photoFormat;
    }

    public void setPhotoFormat(String photoFormat) {
        this.photoFormat = photoFormat;
    }

    @Id
    @Basic
    @Column(name = "SUB_ID")
    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    @Basic
    @Column(name = "PHOTO_FILE")
    public byte[] getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(byte[] photoFile) {
        this.photoFile = photoFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpPhoto that = (EmpPhoto) o;
        return seqid == that.seqid &&
                Objects.equals(empId, that.empId) &&
                Objects.equals(lastUpdatedStamp, that.lastUpdatedStamp) &&
                Objects.equals(lastUpdatedTxStamp, that.lastUpdatedTxStamp) &&
                Objects.equals(createdStamp, that.createdStamp) &&
                Objects.equals(createdTxStamp, that.createdTxStamp) &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(photoUuid, that.photoUuid) &&
                Objects.equals(status, that.status) &&
                Objects.equals(status2, that.status2) &&
                Objects.equals(photoFormat, that.photoFormat) &&
                Objects.equals(subId, that.subId) &&
                Arrays.equals(photoFile, that.photoFile);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(empId, lastUpdatedStamp, lastUpdatedTxStamp, createdStamp, createdTxStamp, filePath, seqid, photoUuid, status, status2, photoFormat, subId);
        result = 31 * result + Arrays.hashCode(photoFile);
        return result;
    }
}
