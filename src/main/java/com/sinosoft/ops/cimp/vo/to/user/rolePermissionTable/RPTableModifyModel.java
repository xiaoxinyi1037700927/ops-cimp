package com.sinosoft.ops.cimp.vo.to.user.rolePermissionTable;

import com.sinosoft.ops.cimp.vo.from.user.rolePermissionTable.RPTableAddModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(description = "角色关联Table 修改类")
public class RPTableModifyModel extends RPTableAddModel {

    @ApiModelProperty(value = "主键id", required = true)
    @NotNull(message = "id不能为空！")
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
