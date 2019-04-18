package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "系统应用表集合删除模型")
public class SysAppTableSetDeleteModel {

    @ApiModelProperty(value = "id的集合")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
