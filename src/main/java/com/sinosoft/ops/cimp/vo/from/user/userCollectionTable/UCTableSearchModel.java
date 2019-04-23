package com.sinosoft.ops.cimp.vo.from.user.userCollectionTable;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "用户收藏Table 查询类")
public class UCTableSearchModel extends RePagination {

    @ApiModelProperty(value = "userId")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
