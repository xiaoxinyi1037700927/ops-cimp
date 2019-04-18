package com.sinosoft.ops.cimp.vo.from.sys.sysapp.sysAppTableFieldSet;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "系统应用表字段集合删除模型")
public class SysAppTableFieldSetDeleteModel {

    @ApiModelProperty(value = "id的集合")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
