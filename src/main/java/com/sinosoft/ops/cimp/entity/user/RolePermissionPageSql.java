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
    private String execSql;

    //执行sql查询的select字段名称（英文），使用“,”分割
    @Column(length = 4000)
    private String selectFieldsEn;

    //执行sql查询的select字段名称（中文），使用“,”分割
    @Column(length = 4000)
    private String selectFieldsCn;

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

    public String getExecSql() {
        return execSql;
    }

    public void setExecSql(String execSql) {
        this.execSql = execSql;
    }

    public String getSelectFieldsEn() {
        return selectFieldsEn;
    }

    public void setSelectFieldsEn(String selectFieldsEn) {
        this.selectFieldsEn = selectFieldsEn;
    }

    public String getSelectFieldsCn() {
        return selectFieldsCn;
    }

    public void setSelectFieldsCn(String selectFieldsCn) {
        this.selectFieldsCn = selectFieldsCn;
    }
}
