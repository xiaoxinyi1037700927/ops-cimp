package com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable;

import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableAddModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@ApiModel(description = "角色关联Table 返回类")
public class RPTableViewModel extends RPTableAddModel {

    @ApiModelProperty(value = "角色关联Tableid", required = true)
    @NotEmpty(message = "主键id不能为空")
    private String id;

    /**
     * 修改人
     */
    private String modifyId;
    /**
     * 修改时间
     */
    private Date modifyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModifyId() {
        return modifyId;
    }

    public void setModifyId(String modifyId) {
        this.modifyId = modifyId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
