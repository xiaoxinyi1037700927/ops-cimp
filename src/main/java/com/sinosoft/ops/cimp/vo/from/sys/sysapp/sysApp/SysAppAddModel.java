package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysApp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;


@ApiModel(description = "系统应用添加模型")
public class SysAppAddModel {
    /**
     * 应用编码
     */
    @ApiModelProperty(value = "应用编码", required = true)
    @NotNull(message = "应用编码不能为空")
    private Integer code;
    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称")
    private String name;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
