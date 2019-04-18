package com.sinosoft.ops.cimp.vo.from.sys.syscode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "SysCodeSetModifyModel", description = "代码集修改")
public class SysCodeSetModifyModel extends SysCodeSetAddModel {

    @NotNull(message = "代码集编号不能为空")
    @ApiModelProperty(value = "代码集编号")
    private Integer id;

    //'创建时间'
    private Date createdTime;

    //'创建人'
    private String createBy;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
