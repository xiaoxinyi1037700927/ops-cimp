package com.sinosoft.ops.cimp.vo.from.sys.app.sysApp;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "系统应用查询模型")
public class SysAppSearchModel extends RePagination {
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
