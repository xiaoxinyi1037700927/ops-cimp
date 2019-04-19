package com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

@ApiModel(description = "角色权限页面sql 新增类")
public class RPPageSqlAddModel {

    //关联角色id
    @ApiModelProperty(value = "关联角色id", required = true)
    @NotEmpty(message = "角色id不能为空")
    private String roleId;

    //该条sql执行的名称
    @ApiModelProperty(value = "该条sql执行的名称", required = true)
    @NotEmpty(message = "该条sql执行的名称不能为空")
    private String sqlName;

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
