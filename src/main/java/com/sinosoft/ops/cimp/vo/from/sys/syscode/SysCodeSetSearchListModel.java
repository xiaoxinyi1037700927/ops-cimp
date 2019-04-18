package com.sinosoft.ops.cimp.vo.from.sys.syscode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "SysCodeSetSearchListModel ",description = "根据代码集编号集合获取数据")
public class SysCodeSetSearchListModel {

    @ApiModelProperty(value = "代码集编号集合")
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
