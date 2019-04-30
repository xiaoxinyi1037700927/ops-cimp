package com.sinosoft.ops.cimp.vo.to.sys.tag;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "SysTagModel", description = "系统查询标签模型")
public class SysTagModel implements Serializable {
    private static final long serialVersionUID = 562259383989713362L;

    @ApiModelProperty(value = "标签分类id")
    private String tagCategoryId;
    @ApiModelProperty(value = "标签分类名称")
    private String tagCategoryName;
    @ApiModelProperty(value = "标签分类模型")
    private String tagModel;
    @ApiModelProperty(value = "所有标签")
    private List<SysTagVO> sysTags = Lists.newArrayList();

    public String getTagCategoryId() {
        return tagCategoryId;
    }

    public void setTagCategoryId(String tagCategoryId) {
        this.tagCategoryId = tagCategoryId;
    }

    public String getTagCategoryName() {
        return tagCategoryName;
    }

    public void setTagCategoryName(String tagCategoryName) {
        this.tagCategoryName = tagCategoryName;
    }

    public String getTagModel() {
        return tagModel;
    }

    public void setTagModel(String tagModel) {
        this.tagModel = tagModel;
    }

    public List<SysTagVO> getSysTags() {
        return sysTags;
    }

    public void setSysTags(List<SysTagVO> sysTags) {
        this.sysTags = sysTags;
    }
}
