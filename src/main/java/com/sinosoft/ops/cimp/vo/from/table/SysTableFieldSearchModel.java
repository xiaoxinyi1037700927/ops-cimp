package com.sinosoft.ops.cimp.vo.from.table;


import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysTableTypeSearchModel", description = "分页查询系统表字段信息")
public class SysTableFieldSearchModel extends RePagination {

    @ApiModelProperty(value = "系统表编号")
    private String sysTableId;

    @ApiModelProperty(value = "中文字段名")
    private String nameCn;


    public String getSysTableId() {
        return sysTableId;
    }

    public void setSysTableId(String sysTableId) {
        this.sysTableId = sysTableId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
