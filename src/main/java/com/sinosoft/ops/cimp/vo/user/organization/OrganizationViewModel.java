package com.sinosoft.ops.cimp.vo.user.organization;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "OrganizationViewModel", description="返回单位数")
public class OrganizationViewModel {

    @ApiModelProperty(value = "单位ID")
    private String id;

    @ApiModelProperty(value = "单位名称")
    private String name;

    @ApiModelProperty(value = "单位代码")
    private String code;

    @ApiModelProperty(value = "单位简称")
    private String briefName;

    @ApiModelProperty(value = "单位全称")
    private String fullName;

    @ApiModelProperty(value = "父单位代码")
    private String parentCode;

    @ApiModelProperty(value = "父单位ID")
    private String parentId;

    @ApiModelProperty(value = "单位类型")
    private String orgType;

    @ApiModelProperty(value = "单位级别")
    private String orgLevel;

    @ApiModelProperty(value = "是否有子节点")
    private String hasChildren;

    @ApiModelProperty(value = "是否选中")
    private String isCheck;

    @ApiModelProperty(value = "是否填报")
    private String submit;

    @ApiModelProperty(value = "长度")
    private int count;

    @ApiModelProperty(value = "子节点")
    private List<OrganizationViewModel> subTreeNode = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBriefName() {
        return briefName;
    }

    public void setBriefName(String briefName) {
        this.briefName = briefName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public List<OrganizationViewModel> getSubTreeNode() {
        return subTreeNode;
    }

    public void setSubTreeNode(List<OrganizationViewModel> subTreeNode) {
        this.subTreeNode = subTreeNode;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
