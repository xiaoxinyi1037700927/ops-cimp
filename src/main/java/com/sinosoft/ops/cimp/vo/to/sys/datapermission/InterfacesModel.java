package com.sinosoft.ops.cimp.vo.to.sys.datapermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "接口模型")
public class InterfacesModel {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String name;
    /**
     * 接口url
     */
    @ApiModelProperty(value = "接口url")
    private String url;
    /**
     * 配置类型
     */
    @ApiModelProperty(value = "配置类型")
    private String configType;
    /**
     * sql表达式
     */
    @ApiModelProperty(value = "sql表达式")
    private String sql;

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

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
