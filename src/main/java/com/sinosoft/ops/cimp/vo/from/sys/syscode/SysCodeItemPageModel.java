package com.sinosoft.ops.cimp.vo.from.sys.syscode;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(value = "SysCodeItemPageModel",description = "代码项搜索")
public class SysCodeItemPageModel extends RePagination {

    @NotNull(message = "代码集编号不能为空")
    @ApiModelProperty(value = "代码集编号")
    private Integer id;

    @ApiModelProperty(value = "代码项中文名")
    private String nameCn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
