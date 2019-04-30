package com.sinosoft.ops.cimp.vo.to.sys.tag;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "SysTagVO", description = "查询标签")
public class SysTagVO implements Serializable {
    private static final long serialVersionUID = 562259383989713362L;

    @ApiModelProperty(value = "标签id")
    private String tagId;
    @ApiModelProperty(value = "标签名称")
    private String tagName;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
