package com.sinosoft.ops.cimp.vo.from.sys.syscode;


import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SysCodeSetSearchModel", description = "代码集搜索条件")
public class SysCodeSetSearchModel extends RePagination {

    @ApiModelProperty(value = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
