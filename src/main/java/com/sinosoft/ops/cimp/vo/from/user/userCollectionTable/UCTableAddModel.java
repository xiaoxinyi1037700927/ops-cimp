package com.sinosoft.ops.cimp.vo.from.user.userCollectionTable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@ApiModel(description = "用户收藏表 新增类")
public class UCTableAddModel {

    @ApiModelProperty(value = "关联用户id", required = true)
    @NotEmpty(message = "用户id不能为空")
    private String userId;

    /**
     * table Id
     */
    @ApiModelProperty(value = "table Id", required = true)
    @NotEmpty(message = "table Id不能为空")
    private String tableId;
    /**
     * nameCN
     */
    @ApiModelProperty(value = "nameCN")
    private String nameCN;

    @ApiModelProperty(value = "nameEN", required = true)
    @NotEmpty(message = "nameEN名称不能为空")
    private String nameEN;

    @ApiModelProperty(value = "表名")
    private String name;
    /**
     * 排序
     * */
    @ApiModelProperty(value = "排序")
    private Integer sortNumber;

    /**
     * 创建人
     */
    private String createId;
    /**
     * 创建时间
     */
    private Date createTime;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
