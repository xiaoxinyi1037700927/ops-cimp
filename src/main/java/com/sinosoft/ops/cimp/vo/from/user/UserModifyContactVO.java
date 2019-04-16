package com.sinosoft.ops.cimp.vo.from.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

/**
 * 修改用户名和用户联系方式接口
 */
@ApiModel(description = "修改用户名和用户联系方式接口")
public class UserModifyContactVO {

    @ApiModelProperty(value = "用户id")
    @NotEmpty(message = "用户id不能为空")
    private String id;
    /**
     * 用户名（账号使用人）
     */
    @ApiModelProperty(value = "用户名（账号使用人）")
    private String name;
    /**
     * 座机
     */
    @ApiModelProperty(value = "座机")
    private String telePhone;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobilePhone;

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

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
