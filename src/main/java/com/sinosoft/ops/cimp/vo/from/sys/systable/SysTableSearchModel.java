package com.sinosoft.ops.cimp.vo.from.sys.systable;


import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysTableSearchModel", description = "系统表分页查询")
public class SysTableSearchModel extends RePagination {

    @ApiModelProperty(value = "系统表中文")
    private String nameCn;

    @ApiModelProperty(value = "系统表类别编号")
    private String sysTableTypeId;

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getSysTableTypeId() {
        return sysTableTypeId;
    }

    public void setSysTableTypeId(String sysTableTypeId) {
        this.sysTableTypeId = sysTableTypeId;
    }
}
