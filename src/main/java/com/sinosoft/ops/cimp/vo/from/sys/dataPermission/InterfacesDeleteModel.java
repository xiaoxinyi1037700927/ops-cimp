package com.sinosoft.ops.cimp.vo.from.sys.dataPermission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "接口类型删除模型")
public class InterfacesDeleteModel {
    /**
     * 接口id列表
     */
    @ApiModelProperty(value = "接口id列表")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
