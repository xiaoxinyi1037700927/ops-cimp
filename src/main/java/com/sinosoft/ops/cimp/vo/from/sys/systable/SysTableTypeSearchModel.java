package com.sinosoft.ops.cimp.vo.from.sys.systable;

import com.sinosoft.ops.cimp.dto.RePagination;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;


@ApiModel(value = "SysTableTypeSearchModel", description = "分页查询系统表类型信息")
public class SysTableTypeSearchModel extends RePagination {

    @ApiModelProperty(value = "类别编码")
    @NotBlank(message = "类别编码不能为空")
    private String code;

    @ApiModelProperty(value = "中文类别名称")
    private String nameCn;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }
}
