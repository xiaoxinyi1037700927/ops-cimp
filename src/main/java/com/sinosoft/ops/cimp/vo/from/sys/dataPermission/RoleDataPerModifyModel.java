package com.sinosoft.ops.cimp.vo.from.sys.dataPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "角色数据权限添加模型")
public class RoleDataPerModifyModel {
    /**
     * 角色数据权限id
     */
    @ApiModelProperty(value = "角色数据权限id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 接口url
     */
    @ApiModelProperty(value = "接口url")
    private String url;
    /**
     * sql类型
     */
    @ApiModelProperty(value = "sql类型")
    private String type;
    /**
     * sql
     */
    @ApiModelProperty(value = "sql")
    private String sql;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
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
