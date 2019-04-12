package com.sinosoft.ops.cimp.vo.from.sys.app.sysApp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel(description = "系统应用删除模型")
public class SysAppDeleteModel {

    @ApiModelProperty(value = "id的集合")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
