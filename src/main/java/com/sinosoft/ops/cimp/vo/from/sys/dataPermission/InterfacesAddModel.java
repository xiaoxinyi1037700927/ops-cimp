package com.sinosoft.ops.cimp.vo.from.sys.dataPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "接口添加模型")
public class InterfacesAddModel {
    /**
     * 接口类型id
     */
    @ApiModelProperty(value = "接口类型id")
    private String interfaceTypeId;
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
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public String getInterfaceTypeId() {
        return interfaceTypeId;
    }

    public void setInterfaceTypeId(String interfaceTypeId) {
        this.interfaceTypeId = interfaceTypeId;
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
