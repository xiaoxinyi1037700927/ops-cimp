package com.sinosoft.ops.cimp.vo.from;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "DEP_B001", catalog = "")
public class DepB001 {
    private Long b01042;
    private String b01044B;
    private String b01054A;
    private String b01004;
    private String b01007;
    private String b01023;
    private String b01027;
    private String b01031;
    private String b01051A;
    private String b01087A;
    private String b01029;
    private String b01071A;
    private String b01017;
    private Long b01040;
    private String b01071B;
    private String b01014;
    private String b01054B;
    private String b01061;
    private String b01084;
    private String b01087B;
    private String b01001;
    private String b01057B;
    private String b01094;
    private String b01028;
    private Long b01043;
    private String b01044A;
    private String b01057A;
    private String b01260;
    private String b01025;
    private String b01034;
    private Long b01045;
    private String b01051B;
    private String b01067;
    private String status2;
    private Long seqid;
    private String depId;
    private String categoryId;
    private Timestamp lastUpdatedStamp;
    private Timestamp lastUpdatedTxStamp;
    private Timestamp createdStamp;
    private Timestamp createdTxStamp;
    private String code;
    private String codeType;
    private String cptr;
    private String description;
    private String pptr;
    private String spell;
    private String treeLevelCode;
    private String icon;
    private Time b01064;
    private Time b01081;
    private Time b01068;
    private String b01001A;
    private String b01001B;
    private String b01014Z;
    private String b01097;
    private String b01037;
    private Long ordinal;
    private long status;
    private Timestamp createdTime;
    private byte[] createdBy;
    private Timestamp lastModifiedTime;
    private byte[] lastModifiedBy;
    private String b001001B;
    private String b001001A;

    @Basic
    @Column(name = "B01042", nullable = true)
    public Long getB01042() {
        return b01042;
    }

    public void setB01042(Long b01042) {
        this.b01042 = b01042;
    }

    @Basic
    @Column(name = "B01044_B", nullable = true)
    public String getB01044B() {
        return b01044B;
    }

    public void setB01044B(String b01044B) {
        this.b01044B = b01044B;
    }

    @Basic
    @Column(name = "B01054_A", nullable = true)
    public String getB01054A() {
        return b01054A;
    }

    public void setB01054A(String b01054A) {
        this.b01054A = b01054A;
    }

    @Basic
    @Column(name = "B01004", nullable = true)
    public String getB01004() {
        return b01004;
    }

    public void setB01004(String b01004) {
        this.b01004 = b01004;
    }

    @Basic
    @Column(name = "B01007", nullable = true)
    public String getB01007() {
        return b01007;
    }

    public void setB01007(String b01007) {
        this.b01007 = b01007;
    }

    @Basic
    @Column(name = "B01023", nullable = true)
    public String getB01023() {
        return b01023;
    }

    public void setB01023(String b01023) {
        this.b01023 = b01023;
    }

    @Basic
    @Column(name = "B01027", nullable = true)
    public String getB01027() {
        return b01027;
    }

    public void setB01027(String b01027) {
        this.b01027 = b01027;
    }

    @Basic
    @Column(name = "B01031", nullable = true)
    public String getB01031() {
        return b01031;
    }

    public void setB01031(String b01031) {
        this.b01031 = b01031;
    }

    @Basic
    @Column(name = "B01051_A", nullable = true)
    public String getB01051A() {
        return b01051A;
    }

    public void setB01051A(String b01051A) {
        this.b01051A = b01051A;
    }

    @Basic
    @Column(name = "B01087_A", nullable = true)
    public String getB01087A() {
        return b01087A;
    }

    public void setB01087A(String b01087A) {
        this.b01087A = b01087A;
    }

    @Basic
    @Column(name = "B01029", nullable = true)
    public String getB01029() {
        return b01029;
    }

    public void setB01029(String b01029) {
        this.b01029 = b01029;
    }

    @Basic
    @Column(name = "B01071_A", nullable = true)
    public String getB01071A() {
        return b01071A;
    }

    public void setB01071A(String b01071A) {
        this.b01071A = b01071A;
    }

    @Basic
    @Column(name = "B01017", nullable = true)
    public String getB01017() {
        return b01017;
    }

    public void setB01017(String b01017) {
        this.b01017 = b01017;
    }

    @Basic
    @Column(name = "B01040", nullable = true)
    public Long getB01040() {
        return b01040;
    }

    public void setB01040(Long b01040) {
        this.b01040 = b01040;
    }

    @Basic
    @Column(name = "B01071_B", nullable = true)
    public String getB01071B() {
        return b01071B;
    }

    public void setB01071B(String b01071B) {
        this.b01071B = b01071B;
    }

    @Basic
    @Column(name = "B01014", nullable = true)
    public String getB01014() {
        return b01014;
    }

    public void setB01014(String b01014) {
        this.b01014 = b01014;
    }

    @Basic
    @Column(name = "B01054_B", nullable = true)
    public String getB01054B() {
        return b01054B;
    }

    public void setB01054B(String b01054B) {
        this.b01054B = b01054B;
    }

    @Basic
    @Column(name = "B01061", nullable = true)
    public String getB01061() {
        return b01061;
    }

    public void setB01061(String b01061) {
        this.b01061 = b01061;
    }

    @Basic
    @Column(name = "B01084", nullable = true)
    public String getB01084() {
        return b01084;
    }

    public void setB01084(String b01084) {
        this.b01084 = b01084;
    }

    @Basic
    @Column(name = "B01087_B", nullable = true)
    public String getB01087B() {
        return b01087B;
    }

    public void setB01087B(String b01087B) {
        this.b01087B = b01087B;
    }

    @Basic
    @Column(name = "B01001", nullable = true)
    public String getB01001() {
        return b01001;
    }

    public void setB01001(String b01001) {
        this.b01001 = b01001;
    }

    @Basic
    @Column(name = "B01057_B", nullable = true)
    public String getB01057B() {
        return b01057B;
    }

    public void setB01057B(String b01057B) {
        this.b01057B = b01057B;
    }

    @Basic
    @Column(name = "B01094", nullable = true)
    public String getB01094() {
        return b01094;
    }

    public void setB01094(String b01094) {
        this.b01094 = b01094;
    }

    @Basic
    @Column(name = "B01028", nullable = true)
    public String getB01028() {
        return b01028;
    }

    public void setB01028(String b01028) {
        this.b01028 = b01028;
    }

    @Basic
    @Column(name = "B01043", nullable = true)
    public Long getB01043() {
        return b01043;
    }

    public void setB01043(Long b01043) {
        this.b01043 = b01043;
    }

    @Basic
    @Column(name = "B01044_A", nullable = true)
    public String getB01044A() {
        return b01044A;
    }

    public void setB01044A(String b01044A) {
        this.b01044A = b01044A;
    }

    @Basic
    @Column(name = "B01057_A", nullable = true)
    public String getB01057A() {
        return b01057A;
    }

    public void setB01057A(String b01057A) {
        this.b01057A = b01057A;
    }

    @Basic
    @Column(name = "B01260", nullable = true)
    public String getB01260() {
        return b01260;
    }

    public void setB01260(String b01260) {
        this.b01260 = b01260;
    }

    @Basic
    @Column(name = "B01025", nullable = true)
    public String getB01025() {
        return b01025;
    }

    public void setB01025(String b01025) {
        this.b01025 = b01025;
    }

    @Basic
    @Column(name = "B01034", nullable = true)
    public String getB01034() {
        return b01034;
    }

    public void setB01034(String b01034) {
        this.b01034 = b01034;
    }

    @Basic
    @Column(name = "B01045", nullable = true)
    public Long getB01045() {
        return b01045;
    }

    public void setB01045(Long b01045) {
        this.b01045 = b01045;
    }

    @Basic
    @Column(name = "B01051_B", nullable = true)
    public String getB01051B() {
        return b01051B;
    }

    public void setB01051B(String b01051B) {
        this.b01051B = b01051B;
    }

    @Basic
    @Column(name = "B01067", nullable = true)
    public String getB01067() {
        return b01067;
    }

    public void setB01067(String b01067) {
        this.b01067 = b01067;
    }

    @Basic
    @Column(name = "STATUS2", nullable = true)
    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    @Basic
    @Column(name = "SEQID", nullable = true)
    public Long getSeqid() {
        return seqid;
    }

    public void setSeqid(Long seqid) {
        this.seqid = seqid;
    }

    @Id
    @Column(name = "DEP_ID", nullable = true)
    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    @Basic
    @Column(name = "CATEGORY_ID", nullable = true)
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "LAST_UPDATED_STAMP", nullable = true)
    public Timestamp getLastUpdatedStamp() {
        return lastUpdatedStamp;
    }

    public void setLastUpdatedStamp(Timestamp lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }

    @Basic
    @Column(name = "LAST_UPDATED_TX_STAMP", nullable = true)
    public Timestamp getLastUpdatedTxStamp() {
        return lastUpdatedTxStamp;
    }

    public void setLastUpdatedTxStamp(Timestamp lastUpdatedTxStamp) {
        this.lastUpdatedTxStamp = lastUpdatedTxStamp;
    }

    @Basic
    @Column(name = "CREATED_STAMP", nullable = true)
    public Timestamp getCreatedStamp() {
        return createdStamp;
    }

    public void setCreatedStamp(Timestamp createdStamp) {
        this.createdStamp = createdStamp;
    }

    @Basic
    @Column(name = "CREATED_TX_STAMP", nullable = true)
    public Timestamp getCreatedTxStamp() {
        return createdTxStamp;
    }

    public void setCreatedTxStamp(Timestamp createdTxStamp) {
        this.createdTxStamp = createdTxStamp;
    }

    @Basic
    @Column(name = "CODE", nullable = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "CODE_TYPE", nullable = true)
    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    @Basic
    @Column(name = "CPTR", nullable = true)
    public String getCptr() {
        return cptr;
    }

    public void setCptr(String cptr) {
        this.cptr = cptr;
    }

    @Basic
    @Column(name = "DESCRIPTION", nullable = true)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "PPTR", nullable = true)
    public String getPptr() {
        return pptr;
    }

    public void setPptr(String pptr) {
        this.pptr = pptr;
    }

    @Basic
    @Column(name = "SPELL", nullable = true)
    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    @Basic
    @Column(name = "TREE_LEVEL_CODE", nullable = true)
    public String getTreeLevelCode() {
        return treeLevelCode;
    }

    public void setTreeLevelCode(String treeLevelCode) {
        this.treeLevelCode = treeLevelCode;
    }

    @Basic
    @Column(name = "ICON", nullable = true)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "B01064", nullable = true)
    public Time getB01064() {
        return b01064;
    }

    public void setB01064(Time b01064) {
        this.b01064 = b01064;
    }

    @Basic
    @Column(name = "B01081", nullable = true)
    public Time getB01081() {
        return b01081;
    }

    public void setB01081(Time b01081) {
        this.b01081 = b01081;
    }

    @Basic
    @Column(name = "B01068", nullable = true)
    public Time getB01068() {
        return b01068;
    }

    public void setB01068(Time b01068) {
        this.b01068 = b01068;
    }

    @Basic
    @Column(name = "B01001_A", nullable = true)
    public String getB01001A() {
        return b01001A;
    }

    public void setB01001A(String b01001A) {
        this.b01001A = b01001A;
    }

    @Basic
    @Column(name = "B01001_B", nullable = true)
    public String getB01001B() {
        return b01001B;
    }

    public void setB01001B(String b01001B) {
        this.b01001B = b01001B;
    }

    @Basic
    @Column(name = "B01014_Z", nullable = true)
    public String getB01014Z() {
        return b01014Z;
    }

    public void setB01014Z(String b01014Z) {
        this.b01014Z = b01014Z;
    }

    @Basic
    @Column(name = "B01097", nullable = true)
    public String getB01097() {
        return b01097;
    }

    public void setB01097(String b01097) {
        this.b01097 = b01097;
    }

    @Basic
    @Column(name = "B01037", nullable = true)
    public String getB01037() {
        return b01037;
    }

    public void setB01037(String b01037) {
        this.b01037 = b01037;
    }

    @Basic
    @Column(name = "ORDINAL", nullable = true)
    public Long getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Long ordinal) {
        this.ordinal = ordinal;
    }

    @Basic
    @Column(name = "STATUS", nullable = false)
    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    @Basic
    @Column(name = "CREATED_TIME", nullable = false)
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    @Basic
    @Column(name = "CREATED_BY", nullable = true)
    public byte[] getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(byte[] createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "LAST_MODIFIED_TIME", nullable = false)
    public Timestamp getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Timestamp lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Basic
    @Column(name = "LAST_MODIFIED_BY", nullable = true)
    public byte[] getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(byte[] lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Basic
    @Column(name = "B001001_B", nullable = true)
    public String getB001001B() {
        return b001001B;
    }

    public void setB001001B(String b001001B) {
        this.b001001B = b001001B;
    }

    @Basic
    @Column(name = "B001001_A", nullable = true)
    public String getB001001A() {
        return b001001A;
    }

    public void setB001001A(String b001001A) {
        this.b001001A = b001001A;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepB001 depB001 = (DepB001) o;

        if (status != depB001.status) return false;
        if (b01042 != null ? !b01042.equals(depB001.b01042) : depB001.b01042 != null) return false;
        if (b01044B != null ? !b01044B.equals(depB001.b01044B) : depB001.b01044B != null) return false;
        if (b01054A != null ? !b01054A.equals(depB001.b01054A) : depB001.b01054A != null) return false;
        if (b01004 != null ? !b01004.equals(depB001.b01004) : depB001.b01004 != null) return false;
        if (b01007 != null ? !b01007.equals(depB001.b01007) : depB001.b01007 != null) return false;
        if (b01023 != null ? !b01023.equals(depB001.b01023) : depB001.b01023 != null) return false;
        if (b01027 != null ? !b01027.equals(depB001.b01027) : depB001.b01027 != null) return false;
        if (b01031 != null ? !b01031.equals(depB001.b01031) : depB001.b01031 != null) return false;
        if (b01051A != null ? !b01051A.equals(depB001.b01051A) : depB001.b01051A != null) return false;
        if (b01087A != null ? !b01087A.equals(depB001.b01087A) : depB001.b01087A != null) return false;
        if (b01029 != null ? !b01029.equals(depB001.b01029) : depB001.b01029 != null) return false;
        if (b01071A != null ? !b01071A.equals(depB001.b01071A) : depB001.b01071A != null) return false;
        if (b01017 != null ? !b01017.equals(depB001.b01017) : depB001.b01017 != null) return false;
        if (b01040 != null ? !b01040.equals(depB001.b01040) : depB001.b01040 != null) return false;
        if (b01071B != null ? !b01071B.equals(depB001.b01071B) : depB001.b01071B != null) return false;
        if (b01014 != null ? !b01014.equals(depB001.b01014) : depB001.b01014 != null) return false;
        if (b01054B != null ? !b01054B.equals(depB001.b01054B) : depB001.b01054B != null) return false;
        if (b01061 != null ? !b01061.equals(depB001.b01061) : depB001.b01061 != null) return false;
        if (b01084 != null ? !b01084.equals(depB001.b01084) : depB001.b01084 != null) return false;
        if (b01087B != null ? !b01087B.equals(depB001.b01087B) : depB001.b01087B != null) return false;
        if (b01001 != null ? !b01001.equals(depB001.b01001) : depB001.b01001 != null) return false;
        if (b01057B != null ? !b01057B.equals(depB001.b01057B) : depB001.b01057B != null) return false;
        if (b01094 != null ? !b01094.equals(depB001.b01094) : depB001.b01094 != null) return false;
        if (b01028 != null ? !b01028.equals(depB001.b01028) : depB001.b01028 != null) return false;
        if (b01043 != null ? !b01043.equals(depB001.b01043) : depB001.b01043 != null) return false;
        if (b01044A != null ? !b01044A.equals(depB001.b01044A) : depB001.b01044A != null) return false;
        if (b01057A != null ? !b01057A.equals(depB001.b01057A) : depB001.b01057A != null) return false;
        if (b01260 != null ? !b01260.equals(depB001.b01260) : depB001.b01260 != null) return false;
        if (b01025 != null ? !b01025.equals(depB001.b01025) : depB001.b01025 != null) return false;
        if (b01034 != null ? !b01034.equals(depB001.b01034) : depB001.b01034 != null) return false;
        if (b01045 != null ? !b01045.equals(depB001.b01045) : depB001.b01045 != null) return false;
        if (b01051B != null ? !b01051B.equals(depB001.b01051B) : depB001.b01051B != null) return false;
        if (b01067 != null ? !b01067.equals(depB001.b01067) : depB001.b01067 != null) return false;
        if (status2 != null ? !status2.equals(depB001.status2) : depB001.status2 != null) return false;
        if (seqid != null ? !seqid.equals(depB001.seqid) : depB001.seqid != null) return false;
        if (depId != null ? !depId.equals(depB001.depId) : depB001.depId != null) return false;
        if (categoryId != null ? !categoryId.equals(depB001.categoryId) : depB001.categoryId != null) return false;
        if (lastUpdatedStamp != null ? !lastUpdatedStamp.equals(depB001.lastUpdatedStamp) : depB001.lastUpdatedStamp != null)
            return false;
        if (lastUpdatedTxStamp != null ? !lastUpdatedTxStamp.equals(depB001.lastUpdatedTxStamp) : depB001.lastUpdatedTxStamp != null)
            return false;
        if (createdStamp != null ? !createdStamp.equals(depB001.createdStamp) : depB001.createdStamp != null)
            return false;
        if (createdTxStamp != null ? !createdTxStamp.equals(depB001.createdTxStamp) : depB001.createdTxStamp != null)
            return false;
        if (code != null ? !code.equals(depB001.code) : depB001.code != null) return false;
        if (codeType != null ? !codeType.equals(depB001.codeType) : depB001.codeType != null) return false;
        if (cptr != null ? !cptr.equals(depB001.cptr) : depB001.cptr != null) return false;
        if (description != null ? !description.equals(depB001.description) : depB001.description != null) return false;
        if (pptr != null ? !pptr.equals(depB001.pptr) : depB001.pptr != null) return false;
        if (spell != null ? !spell.equals(depB001.spell) : depB001.spell != null) return false;
        if (treeLevelCode != null ? !treeLevelCode.equals(depB001.treeLevelCode) : depB001.treeLevelCode != null)
            return false;
        if (icon != null ? !icon.equals(depB001.icon) : depB001.icon != null) return false;
        if (b01064 != null ? !b01064.equals(depB001.b01064) : depB001.b01064 != null) return false;
        if (b01081 != null ? !b01081.equals(depB001.b01081) : depB001.b01081 != null) return false;
        if (b01068 != null ? !b01068.equals(depB001.b01068) : depB001.b01068 != null) return false;
        if (b01001A != null ? !b01001A.equals(depB001.b01001A) : depB001.b01001A != null) return false;
        if (b01001B != null ? !b01001B.equals(depB001.b01001B) : depB001.b01001B != null) return false;
        if (b01014Z != null ? !b01014Z.equals(depB001.b01014Z) : depB001.b01014Z != null) return false;
        if (b01097 != null ? !b01097.equals(depB001.b01097) : depB001.b01097 != null) return false;
        if (b01037 != null ? !b01037.equals(depB001.b01037) : depB001.b01037 != null) return false;
        if (ordinal != null ? !ordinal.equals(depB001.ordinal) : depB001.ordinal != null) return false;
        if (createdTime != null ? !createdTime.equals(depB001.createdTime) : depB001.createdTime != null) return false;
        if (!Arrays.equals(createdBy, depB001.createdBy)) return false;
        if (lastModifiedTime != null ? !lastModifiedTime.equals(depB001.lastModifiedTime) : depB001.lastModifiedTime != null)
            return false;
        if (!Arrays.equals(lastModifiedBy, depB001.lastModifiedBy)) return false;
        if (b001001B != null ? !b001001B.equals(depB001.b001001B) : depB001.b001001B != null) return false;
        if (b001001A != null ? !b001001A.equals(depB001.b001001A) : depB001.b001001A != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = b01042 != null ? b01042.hashCode() : 0;
        result = 31 * result + (b01044B != null ? b01044B.hashCode() : 0);
        result = 31 * result + (b01054A != null ? b01054A.hashCode() : 0);
        result = 31 * result + (b01004 != null ? b01004.hashCode() : 0);
        result = 31 * result + (b01007 != null ? b01007.hashCode() : 0);
        result = 31 * result + (b01023 != null ? b01023.hashCode() : 0);
        result = 31 * result + (b01027 != null ? b01027.hashCode() : 0);
        result = 31 * result + (b01031 != null ? b01031.hashCode() : 0);
        result = 31 * result + (b01051A != null ? b01051A.hashCode() : 0);
        result = 31 * result + (b01087A != null ? b01087A.hashCode() : 0);
        result = 31 * result + (b01029 != null ? b01029.hashCode() : 0);
        result = 31 * result + (b01071A != null ? b01071A.hashCode() : 0);
        result = 31 * result + (b01017 != null ? b01017.hashCode() : 0);
        result = 31 * result + (b01040 != null ? b01040.hashCode() : 0);
        result = 31 * result + (b01071B != null ? b01071B.hashCode() : 0);
        result = 31 * result + (b01014 != null ? b01014.hashCode() : 0);
        result = 31 * result + (b01054B != null ? b01054B.hashCode() : 0);
        result = 31 * result + (b01061 != null ? b01061.hashCode() : 0);
        result = 31 * result + (b01084 != null ? b01084.hashCode() : 0);
        result = 31 * result + (b01087B != null ? b01087B.hashCode() : 0);
        result = 31 * result + (b01001 != null ? b01001.hashCode() : 0);
        result = 31 * result + (b01057B != null ? b01057B.hashCode() : 0);
        result = 31 * result + (b01094 != null ? b01094.hashCode() : 0);
        result = 31 * result + (b01028 != null ? b01028.hashCode() : 0);
        result = 31 * result + (b01043 != null ? b01043.hashCode() : 0);
        result = 31 * result + (b01044A != null ? b01044A.hashCode() : 0);
        result = 31 * result + (b01057A != null ? b01057A.hashCode() : 0);
        result = 31 * result + (b01260 != null ? b01260.hashCode() : 0);
        result = 31 * result + (b01025 != null ? b01025.hashCode() : 0);
        result = 31 * result + (b01034 != null ? b01034.hashCode() : 0);
        result = 31 * result + (b01045 != null ? b01045.hashCode() : 0);
        result = 31 * result + (b01051B != null ? b01051B.hashCode() : 0);
        result = 31 * result + (b01067 != null ? b01067.hashCode() : 0);
        result = 31 * result + (status2 != null ? status2.hashCode() : 0);
        result = 31 * result + (seqid != null ? seqid.hashCode() : 0);
        result = 31 * result + (depId != null ? depId.hashCode() : 0);
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (lastUpdatedStamp != null ? lastUpdatedStamp.hashCode() : 0);
        result = 31 * result + (lastUpdatedTxStamp != null ? lastUpdatedTxStamp.hashCode() : 0);
        result = 31 * result + (createdStamp != null ? createdStamp.hashCode() : 0);
        result = 31 * result + (createdTxStamp != null ? createdTxStamp.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (codeType != null ? codeType.hashCode() : 0);
        result = 31 * result + (cptr != null ? cptr.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (pptr != null ? pptr.hashCode() : 0);
        result = 31 * result + (spell != null ? spell.hashCode() : 0);
        result = 31 * result + (treeLevelCode != null ? treeLevelCode.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (b01064 != null ? b01064.hashCode() : 0);
        result = 31 * result + (b01081 != null ? b01081.hashCode() : 0);
        result = 31 * result + (b01068 != null ? b01068.hashCode() : 0);
        result = 31 * result + (b01001A != null ? b01001A.hashCode() : 0);
        result = 31 * result + (b01001B != null ? b01001B.hashCode() : 0);
        result = 31 * result + (b01014Z != null ? b01014Z.hashCode() : 0);
        result = 31 * result + (b01097 != null ? b01097.hashCode() : 0);
        result = 31 * result + (b01037 != null ? b01037.hashCode() : 0);
        result = 31 * result + (ordinal != null ? ordinal.hashCode() : 0);
        result = 31 * result + (int) (status ^ (status >>> 32));
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(createdBy);
        result = 31 * result + (lastModifiedTime != null ? lastModifiedTime.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(lastModifiedBy);
        result = 31 * result + (b001001B != null ? b001001B.hashCode() : 0);
        result = 31 * result + (b001001A != null ? b001001A.hashCode() : 0);
        return result;
    }
}
