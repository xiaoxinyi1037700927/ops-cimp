package com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;


@ApiModel(description = "角色关联Table 返回类")
public class RPTableViewModel {

    @ApiModelProperty(value = "角色关联Tableid", required = true)
    @NotNull(message = "id不能为空！")
    private String id;

    @ApiModelProperty(value = "roleId", required = true)
    @NotNull(message = "roleId不能为空！")
    private String roleId;

    @ApiModelProperty(value = "table Id", required = true)
    @NotNull(message = "table Id不能为空！")
    private String tableId;

    @ApiModelProperty(value = "nameEN", required = true)
    @NotNull(message = "nameEN不能为空！")
    private String nameEN;

    @ApiModelProperty(value = "表名")
    private String name;

    /**
     * 修改人
     */
    private String modifyId;
    /**
     * 修改时间
     */
    private Date modifyTime;


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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
