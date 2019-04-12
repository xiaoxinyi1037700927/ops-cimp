package com.sinosoft.ops.cimp.vo.to.sys.app.sysApp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统应用模型
 */
@ApiModel(description = "系统应用模型")
public class SysAppModel {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 应用编码
     */
    @ApiModelProperty(value = "应用编码")
    private String code;
    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
