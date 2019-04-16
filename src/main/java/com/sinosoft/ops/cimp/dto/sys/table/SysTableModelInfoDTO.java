package com.sinosoft.ops.cimp.dto.sys.table;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(description = "系统表模型")
public class SysTableModelInfoDTO implements Serializable {
    private static final long serialVersionUID = -2658459401157397392L;
    //项目编号
    @ApiModelProperty(value = "项目编号")
    private String prjCode;
    //系统表类型英文名
    @ApiModelProperty(value = "系统表类型英文名")
    private String tableTypeNameEn;
    //系统表类型中文名
    @ApiModelProperty(value = "系统表类型中文名")
    private String tableTypeNameCn;
    //信息项子集和主集的关联字段，也是主集的主键
    @ApiModelProperty(value = "信息项子集和主集的关联字段，也是主集的主键")
    private String primaryField;
    //系统表信息
    @ApiModelProperty(value = "系统表信息")
    private List<SysTableInfoDTO> tables = Lists.newArrayList();

    public String getPrjCode() {
        return prjCode;
    }

    public void setPrjCode(String prjCode) {
        this.prjCode = prjCode;
    }

    public String getTableTypeNameEn() {
        return tableTypeNameEn;
    }

    public void setTableTypeNameEn(String tableTypeNameEn) {
        this.tableTypeNameEn = tableTypeNameEn;
    }

    public String getTableTypeNameCn() {
        return tableTypeNameCn;
    }

    public void setTableTypeNameCn(String tableTypeNameCn) {
        this.tableTypeNameCn = tableTypeNameCn;
    }

    public String getPrimaryField() {
        return primaryField;
    }

    public void setPrimaryField(String primaryField) {
        this.primaryField = primaryField;
    }

    public List<SysTableInfoDTO> getTables() {
        return tables;
    }

    public void setTables(List<SysTableInfoDTO> tables) {
        this.tables = tables;
    }
}
