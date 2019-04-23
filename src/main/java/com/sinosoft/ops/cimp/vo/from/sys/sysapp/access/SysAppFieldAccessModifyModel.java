package com.sinosoft.ops.cimp.vo.from.sys.sysapp.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;


@ApiModel(description = "角色对表字段访问权限修改模型")
public class SysAppFieldAccessModifyModel {
    /**
     * 主键id列表
     */
    @ApiModelProperty(value = "主键id列表")
    private List<String> ids;
    /**
     * 是否能读取字段
     */
    @ApiModelProperty(value = "是否能读取字段")
    private boolean canRead;
    /**
     * 是否能修改字段
     */
    @ApiModelProperty(value = "是否能修改字段")
    private boolean canWrite;
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

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
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
