package com.sinosoft.ops.cimp.entity.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_PERMISSION_PAGE_SQL")
public class RolePermissionPageSql {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    //关联角色id
    @Column(length = 50)
    private String roleId;

    //该条sql执行的名称
    @Column(length = 100)
    private String sqlName;

    //执行的sql语句
    @Column(length = 4000)
    private String execListSql;

    //执行统计sql语句
    @Column(length = 4000)
    private String execCountSql;

    //执行sql查询的select字段名称（英文），使用“,”分割
    @Column(length = 4000)
    private String selectListFieldsEn;

    //执行sql查询的select字段名称（中文），使用“,”分割
    @Column(length = 4000)
    private String selectListFieldsCn;

    //执行count列名
    @Column(length = 4000)
    private String selectCountFieldEn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getExecListSql() {
        return execListSql;
    }

    public void setExecListSql(String execListSql) {
        this.execListSql = execListSql;
    }

    public String getSelectListFieldsEn() {
        return selectListFieldsEn;
    }

    public void setSelectListFieldsEn(String selectListFieldsEn) {
        this.selectListFieldsEn = selectListFieldsEn;
    }

    public String getSelectListFieldsCn() {
        return selectListFieldsCn;
    }

    public void setSelectListFieldsCn(String selectListFieldsCn) {
        this.selectListFieldsCn = selectListFieldsCn;
    }

    public String getExecCountSql() {
        return execCountSql;
    }

    public void setExecCountSql(String execCountSql) {
        this.execCountSql = execCountSql;
    }

    public String getSelectCountFieldEn() {
        return selectCountFieldEn;
    }

    public void setSelectCountFieldEn(String selectCountFieldEn) {
        this.selectCountFieldEn = selectCountFieldEn;
    }
}
