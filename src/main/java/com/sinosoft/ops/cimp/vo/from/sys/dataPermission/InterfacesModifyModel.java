package com.sinosoft.ops.cimp.vo.from.sys.dataPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = "接口修改模型")
public class InterfacesModifyModel {
    /**
     * 接口id
     */
    @ApiModelProperty(value = "接口id")
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

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
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
