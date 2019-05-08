package com.sinosoft.ops.cimp.vo.from.cadre;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description = "修改干部状态模型")
public class CadreStatusModifyModel {
    /**
     * 干部id列表
     */
    @ApiModelProperty(value = "干部id列表")
    private List<String> empIds;
    /**
     * 干部状态
     */
    @ApiModelProperty(value = "干部状态(去世：'QS',其他：'QT')")
    private String status;

    public List<String> getEmpIds() {
        return empIds;
    }

    public void setEmpIds(List<String> empIds) {
        this.empIds = empIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
