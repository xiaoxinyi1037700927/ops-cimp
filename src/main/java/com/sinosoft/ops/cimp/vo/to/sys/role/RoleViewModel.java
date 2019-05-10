package com.sinosoft.ops.cimp.vo.to.sys.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@ApiModel(value = "RoleViewModel", description = "新增角色返回")
public class RoleViewModel {

    @ApiModelProperty(value = "角色ID")
    private String id;

    @ApiModelProperty(value = "角色名称",required = true)
    @NotEmpty(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色值", required = true)
    @NotEmpty(message = "角色code不能为空")
    private String code;

    @ApiModelProperty(value = "父角色ID")
    private String parentId;

    @ApiModelProperty(value = "角色描述")
    private String description;


  //  @ApiModelProperty(value = "菜单集合")
 //   private List<MenuParentViewModel> menuList;

    @ApiModelProperty(value = "功能权限数量")
    private long menuCount;

    @ApiModelProperty(value = "统计项数量")
    private long homePageCountNumber;

    @ApiModelProperty(value = "常规信息集数量")
    private long rPTableCount;

    @ApiModelProperty(value = "角色sql数量")
    private long roleSqlCount;

    @ApiModelProperty(value = "首页类型：1.默认首页，2.无待办首页，3.带统计首页")
    private String pageType;

    @ApiModelProperty(value = "详情url。带统计项时的详情url")
    private String detailUrl;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

 //   public List<MenuParentViewModel> getMenuList() {
 //       return menuList;
 //   }

 //   public void setMenuList(List<MenuParentViewModel> menuList) {
  //      this.menuList = menuList;
  //  }


    public long getMenuCount() {
        return menuCount;
    }

    public void setMenuCount(long menuCount) {
        this.menuCount = menuCount;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public long getHomePageCountNumber() {
        return homePageCountNumber;
    }

    public void setHomePageCountNumber(long homePageCountNumber) {
        this.homePageCountNumber = homePageCountNumber;
    }

    public long getrPTableCount() {
        return rPTableCount;
    }

    public void setrPTableCount(long rPTableCount) {
        this.rPTableCount = rPTableCount;
    }

    public long getRoleSqlCount() {
        return roleSqlCount;
    }

    public void setRoleSqlCount(long roleSqlCount) {
        this.roleSqlCount = roleSqlCount;
    }
}
