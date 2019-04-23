package com.sinosoft.ops.cimp.vo.to.sys.sysapp.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * app中角色对表字段的访问权限模型
 */
@ApiModel(description = "app中角色对表字段的访问权限模型")
public class SysAppFieldAccessModel {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 表字段中文名称
     */
    @ApiModelProperty(value = "表字段中文名称")
    private String nameCn;
    /**
     * 表字段英文名称
     */
    @ApiModelProperty(value = "表字段英文名称")
    private String nameEn;
    /**
     * 是否对字段有读权限
     */
    @ApiModelProperty(value = "是否对字段有读权限")
    private boolean canReadAll;
    /**
     * 是否对字段有写权限
     */
    @ApiModelProperty(value = "是否对字段有写权限")
    private boolean canWriteAll;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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
}
