package com.sinosoft.ops.cimp.vo.to.sys.code;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "SysCodeSetObtainModel", description = "用于级联获取的model")
public class SysCodeSetObtainModel extends SysCodeSetModel {

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "对应代码项")
    private List<SysCodeItemModel> sysCodeItemModels;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<SysCodeItemModel> getSysCodeItemModels() {
        return sysCodeItemModels;
    }

    public void setSysCodeItemModels(List<SysCodeItemModel> sysCodeItemModels) {
        this.sysCodeItemModels = sysCodeItemModels;
    }
}
