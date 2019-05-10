package com.sinosoft.ops.cimp.entity.user.subSelects;

import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RHPCOUNT_COUNT")
@Subselect("SELECT ROLE_ID, count(1) as TABLE_COUNT " +
        "FROM ROLE_HOME_PAGE_COUNT " +
        "GROUP BY ROLE_ID")
@Synchronize({"ROLE_HOME_PAGE_COUNT"})
public class RHPCountCount {
    @Id
    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "TABLE_COUNT")
    private Long tableCount;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getTableCount() {
        return tableCount;
    }

    public void setTableCount(Long tableCount) {
        this.tableCount = tableCount;
    }

}
