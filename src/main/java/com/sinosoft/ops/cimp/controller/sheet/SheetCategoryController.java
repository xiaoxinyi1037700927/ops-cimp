/**
 * @Project:      IIMP
 * @Title:          SheetCategoryController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetCategory;
import com.sinosoft.ops.cimp.service.sheet.SheetCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @ClassName: SheetCategoryController
 * @Description: 表格分类控制器
 * @Author: kanglin
 * @Date: 2017年10月13日 下午3:39:56
 * @Version 1.0.0
 */
@Controller("SheetCategoryController")
@RequestMapping("sheet/category")
public class SheetCategoryController extends BaseEntityController<SheetCategory> {
    private static final Logger logger = LoggerFactory.getLogger(SheetCategoryController.class);

    @Resource
    private SheetCategoryService sheetCategoryService = null;
//    @Autowired
//    private SystemUserService systemUserService;
//    
//    @Autowired
//    private SysUserService sysUserService;

//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_CREATE)
//    public ResponseResult create(SheetCategory entity) {
//        try {
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//        	entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//        	entity.setOrdinal(sheetCategoryService.getNextOrdinal());
//    		String treeName = "";
//        	if(1==entity.getReportType()){
//        		treeName = "DepTree";
//        	}else if(2==entity.getReportType()){
//        		treeName = "PartyTree";
//        	}
//        	Collection<String> organizationIds = systemUserService.getTreeOrganizationIds(this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), treeName);
//        	Assert.isTrue(!CollectionUtils.isEmpty(organizationIds),"用户没有授予组织权限");
//        	String orgId = sheetCategoryService.getTopOrgId(organizationIds,entity.getReportType());
//        	entity.setReportOrg(orgId);
//
//            sheetCategoryService.create(entity);
//            return ResponseResult.success(entity, 1, "保存成功！");
//        }catch(IllegalArgumentException e){
//        	return ResponseResult.failure(e.getMessage());
//        } catch (Exception e) {
//            logger.error("创建失败！", e);
//            return ResponseResult.failure("保存失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_UPDATE)
//    public ResponseResult update(SheetCategory entity) {
//        try {
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            sheetCategoryService.updateName(entity);
//            return ResponseResult.success(entity,1,"保存成功！");
//        } catch (Exception e) {
//            logger.error("更新失败！", e);
//            return ResponseResult.failure("保存失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_DELETE)
//    public ResponseResult delete(SheetCategory entity) {
//        try {
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            boolean success = sheetCategoryService.deleteLeaf(entity);
//            if (success) {
//            	return ResponseResult.success(entity,1,"删除成功！");
//            } else {
//                return ResponseResult.failure("删除失败！");
//            }
//        } catch (Exception e) {
//            logger.error("删除失败！", e);
//            return ResponseResult.failure("删除失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
//    public ResponseResult deleteById(HttpServletRequest request) {
//        try {
//            sheetCategoryService.deleteById(getUUIDParam(request, "id", UUID.randomUUID()));
//            return ResponseResult.success("删除成功！");
//        } catch (Exception e) {
//            logger.error("删除失败！", e);
//            return ResponseResult.failure("删除失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
//    public ResponseResult getById(HttpServletRequest request) {
//        try {
//            SheetCategory entity = sheetCategoryService.getById(getUUIDParam(request, "id", UUID.randomUUID()));
//            if (entity != null) {
//                return ResponseResult.success(entity, 1, "获取成功！");
//            } else {
//                return ResponseResult.failure("获取失败！");
//            }
//        } catch (Exception e) {
//            logger.error("获取失败！", e);
//            return ResponseResult.failure("获取失败！");
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/getParentId")
//    public ResponseResult getParentId(HttpServletRequest request, HttpServletResponse response) {
//        try {
//            List<Map> lst = sheetCategoryService.getCategoryMessage(getUUIDParam(request, "parentId", UUID.randomUUID()));
//            return ResponseResult.success(lst, lst.size(), "获取成功！");
//        } catch (Exception e) {
//            logger.error("获取失败！", e);
//            return ResponseResult.failure("获取失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
//    public ResponseResult findByPage(HttpServletRequest request) {
//        try {
//            PageableQueryParameter queryParameter = new PageableQueryParameter();
//            queryParameter.setPageNo(getIntegerParam(request, "page", 1));
//            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));
//            String keyword = request.getParameter("keyword");
//            if (keyword != null) {
//                queryParameter.getParameters().put("keyword", keyword);
//            }
//
//            PageableQueryResult queryResult = sheetCategoryService.findByPage(queryParameter);
//            return ResponseResult.success(queryResult.getData(),  queryResult.getTotalCount());
//        } catch (Exception e) {
//            logger.error("查询数据失败！", e);
//            return ResponseResult.failure("查询数据失败！");
//        }
//    }
//
//    @RequestMapping("/getRoot")
//    public void getRoot(HttpServletRequest request, HttpServletResponse response) {
//        try{
//            Collection<SheetCategory> root = sheetCategoryService.getRoot();
//            writeJson(response,ResponseResult.success(root,root.size(),"查询成功！"));
//        }catch(Exception e){
//            logger.error("查询失败！",e);
//            writeJson(response,ResponseResult.failure("查询失败！"));
//        }
//    }
//
//    @RequestMapping("/getRootWithDescendants")
//    public void getRootWithDescendants(HttpServletRequest request, HttpServletResponse response,String type) {
//        try{
//        	Collection<SheetCategory> root;
//        	Byte param = getByteParam(request, "parameterId",(byte)1);
//        	if(!"sa".equals(this.getCurrentLoggedInUser().getLoginName())){
//        		Collection<String> organizationIds = new ArrayList<>();
//        		if(param==1){
//        			organizationIds = systemUserService.getTreeOrganizationIds(this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()),"DepTree");
//        		}else if(param==2){
//        			organizationIds = systemUserService.getTreeOrganizationIds(this.UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()),"PartyTree");
//        		}
//        		if(CollectionUtils.isEmpty(organizationIds)){
//        			organizationIds.add("NONE");
//        		}
//        		root = sheetCategoryService.getRootWithDescendantsByOrg(type,organizationIds,param);
//        	}else{
//        		root = sheetCategoryService.getRootWithDescendants(type,param.intValue());
//        	}
//
//            writeJson(response,ResponseResult.success(root,root.size(),"查询成功！"));
//        }catch(Exception e){
//            logger.error("查询失败！",e);
//            writeJson(response,ResponseResult.failure("查询失败！"));
//        }
//    }
//
//    @RequestMapping("/getChildren")
//    public void getChildren(HttpServletRequest request, HttpServletResponse response) {
//        try{
//            Collection<SheetCategory> children = sheetCategoryService.getChildren(getUUIDParam(request, "id", UUID.randomUUID()));
//            writeJson(response,ResponseResult.success(children,children.size(),"查询成功！"));
//        }catch(Exception e){
//            logger.error("查询失败！",e);
//            writeJson(response,ResponseResult.failure("查询失败！"));
//        }
//    }
//
//    /**
//     * 根据类型获取全部的表格分类包括业务分类的树型数据
//     * @param request
//     * @param response
//     */
//    @RequestMapping("/getAllWithClassByType")
//    public void getAllWithClassByType(HttpServletRequest request, HttpServletResponse response) {
//        try{
//            Collection<TreeNode> collection = sheetCategoryService.getAllWithClassByType(getByteParam(request, "type", (byte)-1));
//            writeJson(response,ResponseResult.success(collection,collection.size()));
//        }catch(Exception e){
//            logger.error("查询失败！",e);
//            writeJson(response,ResponseResult.failure("查询失败！"));
//        }
//    }
//
//    /**
//     * 上移节点
//     * @param request
//     * @param response
//     */
//    @ResponseBody
//    @RequestMapping("/moveUp")
//    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetCategory entity) {
//        try{
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            boolean success = sheetCategoryService.moveUp(entity);
//            if (success) {
//            	return ResponseResult.success(entity,1,"上移成功！");
//            } else {
//                return ResponseResult.failure("上移失败！");
//            }
//        } catch (Exception e) {
//            logger.error("上移失败！", e);
//            return ResponseResult.failure("上移失败！");
//        }
//    }
//
//    /**
//     * 下移节点
//     * @param request
//     * @param response
//     */
//    @ResponseBody
//    @RequestMapping("/moveDown")
//    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetCategory entity) {
//        try{
//        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
//            boolean success = sheetCategoryService.moveDown(entity);
//            if (success) {
//            	return ResponseResult.success(entity,1,"下移成功！");
//            } else {
//                return ResponseResult.failure("下移失败！");
//            }
//        } catch (Exception e) {
//            logger.error("下移失败！", e);
//            return ResponseResult.failure("下移失败！");
//        }
//    }
//
//    @RequestMapping("/checkAddTaoBiao")
//    @ResponseBody
//    public ResponseResult checkAddTaoBiao(String categoryId,Integer param){
//    	try {
//			if(sysUserService.getAllApprovedEnabledRoleIds(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId())).contains(0)){
//				return ResponseResult.success(1, 1);
//			}else {
//				String treeName;
//				if(1==param){
//	        		treeName = "DepTree";
//	        	}else if(2==param){
//	        		treeName = "PartyTree";
//	        	}else{
//	        		return ResponseResult.failure("非法请求");
//	        	}
//				Collection<String> collection = systemUserService.getTreeOrganizationIds(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()), treeName);
//				if(!CollectionUtils.isEmpty(collection)){
//					Collection<SheetCategory> tree = sheetCategoryService.getWithDescendantsByOrg("0", collection, param.byteValue());
//					if(!CollectionUtils.isEmpty(tree)){
//						for (SheetCategory sheetCategory : tree) {
//							if(sheetCategory.getId().equals(UUID.fromString(categoryId))){
//								return ResponseResult.success(1, 1);
//							}
//						}
//					}
//
//				}else{
//					return ResponseResult.success(0, 1, "您没有权限新增套表");
//				}
//
//				return ResponseResult.success(0, 1, "您没有权限新增套表：只要当上级单位下发统计任务，才能向下级单位下发统计任务");
//			}
//		} catch (Exception e) {
//			logger.error("TODO:异常描述",e);
//			return ResponseResult.failure("检查权限失败");
//		}
//
//    }
//
//
//	@RequestMapping("/checkAdd")
//    @ResponseBody
//    public ResponseResult checkAdd(){
//    	try {
//			if(sysUserService.getAllApprovedEnabledRoleIds(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId())).contains(0)){
//				return ResponseResult.success(1, 1);
//			}else {
//				return ResponseResult.success(0, 1, "您没有权限新增分类");
//			}
//		} catch (Exception e) {
//			logger.error("TODO:异常描述",e);
//			return ResponseResult.failure("检查权限失败");
//		}
//    }
}
