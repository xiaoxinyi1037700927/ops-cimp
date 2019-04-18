package com.sinosoft.ops.cimp.vo.from.sys.homepagecount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 角色首页统计配置项MODEL
 */
@ApiModel(description = "角色首页统计配置项MODEL")
public class RoleHomePageCountModel {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private String id;
    /**
     * 角色标识id
     */
    @ApiModelProperty(value = "角色标识id")
    private String roleId;

    /**
     * 统计名称
     */
    @ApiModelProperty(value = "统计名称")
    private String countName;

    /**
     * 统计语句
     */
    @ApiModelProperty(value = "统计语句")
    private String countSql;




    /*************************************************/
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountName() {
        return countName;
    }

    public void setCountName(String countName) {
        this.countName = countName;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }
}
