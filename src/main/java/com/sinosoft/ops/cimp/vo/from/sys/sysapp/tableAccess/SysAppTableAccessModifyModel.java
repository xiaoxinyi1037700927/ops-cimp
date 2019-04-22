package com.sinosoft.ops.cimp.vo.from.sys.sysapp.tableAccess;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@ApiModel(description = "角色对表访问权限修改模型")
public class SysAppTableAccessModifyModel {
    /**
     * 主键id列表
     */
    @ApiModelProperty(value = "主键id列表")
    private List<String> ids;
    /**
     * 是否能读取表中所有字段
     */
    @ApiModelProperty(value = "是否能读取表中所有字段")
    private boolean canReadAll;
    /**
     * 是否能修改表中所有字段
     */
    @ApiModelProperty(value = "是否能修改表中所有字段")
    private boolean canWriteAll;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean isCanReadAll() {
        return canReadAll;
    }

    public void setCanReadAll(boolean canReadAll) {
        this.canReadAll = canReadAll;
    }

    public boolean isCanWriteAll() {
        return canWriteAll;
    }

    public void setCanWriteAll(boolean canWriteAll) {
        this.canWriteAll = canWriteAll;
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
