package com.sinosoft.ops.cimp.vo.to.table;

import com.sinosoft.ops.cimp.vo.from.table.SysTableModifyModel;
import com.sinosoft.ops.cimp.vo.from.table.SysTableTypeModifyModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "SysTableModel", description = "系统表查询模型")
public class SysTableModel extends SysTableModifyModel {

    @ApiModelProperty(value = "系统表类型查询信息")
    private List<SysTableTypeModifyModel> sysTableTypeModifyModels;

    public List<SysTableTypeModifyModel> getSysTableTypeModifyModels() {
        return sysTableTypeModifyModels;
    }

    public void setSysTableTypeModifyModels(List<SysTableTypeModifyModel> sysTableTypeModifyModels) {
        this.sysTableTypeModifyModels = sysTableTypeModifyModels;
    }
}
