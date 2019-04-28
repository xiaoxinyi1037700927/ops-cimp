package com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysApp;

import com.sinosoft.ops.cimp.vo.to.sys.sysapp.sysAppTableGroup.SysAppTableGroupModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 系统应用表分组树模型
 */
@ApiModel(description = "系统应用表分组树模型")
public class SysAppTreeModel {
    /**
     * 系统应用id
     */
    @ApiModelProperty(value = "系统应用id")
    private String id;
    /**
     * 系统应用名称
     */
    private String name;
    /**
     * 系统应用表分组信息
     */
    private List<SysAppTableGroupModel> sysAppTableGroups;

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

    public List<SysAppTableGroupModel> getSysAppTableGroups() {
        return sysAppTableGroups;
    }

    public void setSysAppTableGroups(List<SysAppTableGroupModel> sysAppTableGroups) {
        this.sysAppTableGroups = sysAppTableGroups;
    }
}
