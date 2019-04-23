package com.sinosoft.ops.cimp.vo.from.sys.sysapp.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(description = "角色对表访问权限删除模型")
public class SysAppTableAccessDeleteModel {
    /**
     * 主键id列表
     */
    @ApiModelProperty(value = "主键id列表")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
