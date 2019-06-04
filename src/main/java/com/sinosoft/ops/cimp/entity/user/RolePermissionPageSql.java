package com.sinosoft.ops.cimp.entity.user;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ROLE_PERMISSION_PAGE_SQL")
public class RolePermissionPageSql implements Serializable {

    private static final long serialVersionUID = -5897633557919819807L;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    //关联角色id
    @Column(length = 50)
    private String roleId;

    //该条sql英文名称（0：cadreList，1：departmentList）
    @Column(length = 100)
    private String sqlNameEn;

    //该条sql中文名称（干部列表，单位列表）
    @Column(length = 100)
    private String sqlNameCn;

    //是否包含下级（0：不包含下级，1：包含下级）
    @Column(length = 100)
    private String includeSubNode;

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

    //包含下级时的默认排序
    @Column(length = 1000)
    private String defaultSort;

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

    public String getSqlNameEn() {
        return sqlNameEn;
    }

    public void setSqlNameEn(String sqlNameEn) {
        this.sqlNameEn = sqlNameEn;
    }

    public String getSqlNameCn() {
        return sqlNameCn;
    }

    public void setSqlNameCn(String sqlNameCn) {
        this.sqlNameCn = sqlNameCn;
    }

    public String getIncludeSubNode() {
        return includeSubNode;
    }

    public void setIncludeSubNode(String includeSubNode) {
        this.includeSubNode = includeSubNode;
    }

    public String getExecListSql() {
        return execListSql;
    }

    public void setExecListSql(String execListSql) {
        this.execListSql = execListSql;
    }

    public String getExecCountSql() {
        return execCountSql;
    }

    public void setExecCountSql(String execCountSql) {
        this.execCountSql = execCountSql;
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

    public String getSelectCountFieldEn() {
        return selectCountFieldEn;
    }

    public void setSelectCountFieldEn(String selectCountFieldEn) {
        this.selectCountFieldEn = selectCountFieldEn;
    }

    public String getDefaultSort() {
        return defaultSort;
    }

    public void setDefaultSort(String defaultSort) {
        this.defaultSort = defaultSort;
    }
}
