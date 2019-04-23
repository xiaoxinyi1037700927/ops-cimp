package com.sinosoft.ops.cimp.vo.from.user.userCollectionTable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "用户收藏Table 删除类")
public class UCTableDeleteModel {
    @ApiModelProperty(value = "要删除的id集合")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
