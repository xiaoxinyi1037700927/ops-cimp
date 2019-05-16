package com.sinosoft.ops.cimp.entity.archive;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ARCHIVE")
public class Archive implements java.io.Serializable {

  private static final long serialVersionUID = -2809747338662623465L;
  private String id;
  private String personId;
  private String archiveTitle;
  private String archiveSn;
  private String archiveCode;
  private String location;
  private String archiveType;
  private Date establishDate;
  private String fondsNumber;
  private String dossierNumber;
  private String securityClassification;
  private BigDecimal expirationYears;
  private Timestamp createdStamp;
  private Timestamp createdTxStamp;
  private Timestamp lastUpdatedStamp;
  private Timestamp lastUpdatedTxStamp;

  public Archive() {}


  public Archive(String id, String personId, String archiveTitle) {
    this.id = id;
    this.personId = personId;
    this.archiveTitle = archiveTitle;
  }

  public Archive(String id, String personId, String archiveTitle, String archiveSn,
      String archiveCode, String location, String archiveType, Date establishDate,
      String fondsNumber, String dossierNumber, String securityClassification,
      BigDecimal expirationYears, Timestamp createdStamp, Timestamp createdTxStamp,
      Timestamp lastUpdatedStamp, Timestamp lastUpdatedTxStamp) {
    this.id = id;
    this.personId = personId;
    this.archiveTitle = archiveTitle;
    this.archiveSn = archiveSn;
    this.archiveCode = archiveCode;
    this.location = location;
    this.archiveType = archiveType;
    this.establishDate = establishDate;
    this.fondsNumber = fondsNumber;
    this.dossierNumber = dossierNumber;
    this.securityClassification = securityClassification;
    this.expirationYears = expirationYears;
    this.createdStamp = createdStamp;
    this.createdTxStamp = createdTxStamp;
    this.lastUpdatedStamp = lastUpdatedStamp;
    this.lastUpdatedTxStamp = lastUpdatedTxStamp;
  }

  @Id


  @Column(name = "ID", unique = true, nullable = false, length = 50)
  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }


  @Column(name = "PERSON_ID", nullable = false, length = 50)
  public String getPersonId() {
    return this.personId;
  }

  public void setPersonId(String personId) {
    this.personId = personId;
  }


  @Column(name = "ARCHIVE_TITLE", nullable = false, length = 100)
  public String getArchiveTitle() {
    return this.archiveTitle;
  }

  public void setArchiveTitle(String archiveTitle) {
    this.archiveTitle = archiveTitle;
  }


  @Column(name = "ARCHIVE_SN", length = 100)
  public String getArchiveSn() {
    return this.archiveSn;
  }

  public void setArchiveSn(String archiveSn) {
    this.archiveSn = archiveSn;
  }


  @Column(name = "ARCHIVE_CODE", length = 200)
  public String getArchiveCode() {
    return this.archiveCode;
  }

  public void setArchiveCode(String archiveCode) {
    this.archiveCode = archiveCode;
  }


  @Column(name = "LOCATION", length = 200)
  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }


  @Column(name = "ARCHIVE_TYPE", length = 10)
  public String getArchiveType() {
    return this.archiveType;
  }

  public void setArchiveType(String archiveType) {
    this.archiveType = archiveType;
  }

  @Temporal(TemporalType.DATE)
  @Column(name = "ESTABLISH_DATE", length = 7)
  public Date getEstablishDate() {
    return this.establishDate;
  }

  public void setEstablishDate(Date establishDate) {
    this.establishDate = establishDate;
  }


  @Column(name = "FONDS_NUMBER", length = 50)
  public String getFondsNumber() {
    return this.fondsNumber;
  }

  public void setFondsNumber(String fondsNumber) {
    this.fondsNumber = fondsNumber;
  }


  @Column(name = "DOSSIER_NUMBER", length = 50)
  public String getDossierNumber() {
    return this.dossierNumber;
  }

  public void setDossierNumber(String dossierNumber) {
    this.dossierNumber = dossierNumber;
  }


  @Column(name = "SECURITY_CLASSIFICATION", length = 50)
  public String getSecurityClassification() {
    return this.securityClassification;
  }

  public void setSecurityClassification(String securityClassification) {
    this.securityClassification = securityClassification;
  }


  @Column(name = "EXPIRATION_YEARS", precision = 3)
  public BigDecimal getExpirationYears() {
    return this.expirationYears;
  }

  public void setExpirationYears(BigDecimal expirationYears) {
    this.expirationYears = expirationYears;
  }


  @Column(name = "CREATED_STAMP", length = 11)
  public Timestamp getCreatedStamp() {
    return this.createdStamp;
  }

  public void setCreatedStamp(Timestamp createdStamp) {
    this.createdStamp = createdStamp;
  }


  @Column(name = "CREATED_TX_STAMP", length = 11)
  public Timestamp getCreatedTxStamp() {
    return this.createdTxStamp;
  }

  public void setCreatedTxStamp(Timestamp createdTxStamp) {
    this.createdTxStamp = createdTxStamp;
  }


  @Column(name = "LAST_UPDATED_STAMP", length = 11)
  public Timestamp getLastUpdatedStamp() {
    return this.lastUpdatedStamp;
  }

  public void setLastUpdatedStamp(Timestamp lastUpdatedStamp) {
    this.lastUpdatedStamp = lastUpdatedStamp;
  }


  @Column(name = "LAST_UPDATED_TX_STAMP", length = 11)
  public Timestamp getLastUpdatedTxStamp() {
    return this.lastUpdatedTxStamp;
  }

  public void setLastUpdatedTxStamp(Timestamp lastUpdatedTxStamp) {
    this.lastUpdatedTxStamp = lastUpdatedTxStamp;
  }



}


