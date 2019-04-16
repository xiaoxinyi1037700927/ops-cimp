package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 修改书记会成员视图
 */
@ApiModel(description = "修改书记会成员视图")
public class SecretaryMemberModifyViewModel extends SecretaryMemberAddViewModel{

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
