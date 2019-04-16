package com.sinosoft.ops.cimp.vo.from.user.organization;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 新增上级纪委
 */
public class DisciplineUnitAddViewModel {
    /**
     * 上级纪委名称
     */
    @ApiModelProperty(value = "上级纪委名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 单位ID的集合
     */
    @ApiModelProperty(value = "单位Id的集合", required = true)
    private List<String> organizationIds;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<String> organizationIds) {
        this.organizationIds = organizationIds;
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
