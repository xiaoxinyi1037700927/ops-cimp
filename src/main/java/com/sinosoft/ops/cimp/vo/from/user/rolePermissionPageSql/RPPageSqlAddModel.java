package com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@ApiModel(description = "角色权限页面sql 新增类")
public class RPPageSqlAddModel {

    //关联角色id
    @ApiModelProperty(value = "关联角色id", required = true)
    @NotEmpty(message = "角色id不能为空")
    private String roleId;

    //该条sql英文名称（0：cadreList，1：departmentList）
    @ApiModelProperty(value = "该条sql英文名称（0：cadreList，1：departmentList）", required = true)
    @NotEmpty(message = "sql英文不能为空")
    private String sqlNameEn;

    //该条sql中文名称（干部列表，单位列表）
    @ApiModelProperty(value = "该条sql中文名称（干部列表，单位列表）", required = true)
    @NotEmpty(message = "ql中文不能为空")
    private String sqlNameCn;

    //是否包含下级（0：不包含下级，1：包含下级）
    @ApiModelProperty(value = "是否包含下级（0：不包含下级，1：包含下级）", required = true)
    @NotEmpty(message = "是否包含下级不能为空")
    private String includeSubNode;

    //执行的sql语句
    @ApiModelProperty(value = "执行的sql语句", required = true)
    @NotEmpty(message = "执行的sql语句不能为空")
    private String execListSql;

    //执行统计sql语句
    @ApiModelProperty(value = "执行统计sql语句", required = true)
    @NotEmpty(message = "执行统计sql语句不能为空")
    private String execCountSql;

    //执行sql查询的select字段名称（英文），使用“,”分割
    @ApiModelProperty(value = "执行sql查询的select字段名称（英文），使用“,”分割", required = true)
    @NotEmpty(message = "执行sql查询的select字段名称（英文）不能为空")
    private String selectListFieldsEn;

    //执行sql查询的select字段名称（中文），使用“,”分割
    @ApiModelProperty(value = "执行sql查询的select字段名称（中文），使用“,”分割", required = true)
    @NotEmpty(message = "执行sql查询的select字段名称（中文）不能为空")
    private String selectListFieldsCn;

    //执行count列名
    @ApiModelProperty(value = "执行count列名", required = true)
    @NotEmpty(message = "执行count列名不能为空")
    private String selectCountFieldEn;


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
}
