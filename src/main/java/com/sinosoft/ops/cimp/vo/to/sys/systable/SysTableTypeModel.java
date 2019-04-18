package com.sinosoft.ops.cimp.vo.to.sys.systable;


import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableModifyModel;
import com.sinosoft.ops.cimp.vo.from.sys.systable.SysTableTypeModifyModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(value = "SysAppTableModel", description = "系统表查询模型")
public class SysTableTypeModel extends SysTableTypeModifyModel{

    @ApiModelProperty(value = "系统表查询信息")
    List<SysTableModifyModel> sysTableModifyModels;

    public List<SysTableModifyModel> getSysTableModifyModels() {
        return sysTableModifyModels;
    }

    public void setSysTableModifyModels(List<SysTableModifyModel> sysTableModifyModels) {
        this.sysTableModifyModels = sysTableModifyModels;
    }
}
