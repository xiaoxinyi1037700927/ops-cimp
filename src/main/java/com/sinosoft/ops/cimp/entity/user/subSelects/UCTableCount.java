package com.sinosoft.ops.cimp.entity.user.subSelects;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UCTABLE_COUNT")
@Subselect("SELECT USER_ID, count(1) as TABLE_COUNT " +
        "FROM USER_COLLECTION_TABLE " +
        "GROUP BY USER_ID")
@Synchronize({"USER_COLLECTION_TABLE"})
public class UCTableCount {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TABLE_COUNT")
    private Long tableCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTableCount() {
        return tableCount;
    }

    public void setTableCount(Long tableCount) {
        this.tableCount = tableCount;
    }
}
