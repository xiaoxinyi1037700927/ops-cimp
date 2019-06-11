/**
 * @Project:      IIMP
 * @Title:          SheetCategoryController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.*;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName: SheetCategoryController
 * @Description: 表格分类控制器
 * @Author: Nil
 * @Date: 2017年10月13日 下午3:39:56
 * @Version 1.0.0
 */
@Controller("SheetDesignCategoryController")
@RequestMapping("sheet/designCategory")
public class SheetDesignCategoryController extends BaseEntityController<SheetDesignCategory> {
    private static final Logger logger = LoggerFactory.getLogger(SheetDesignCategoryController.class);

    @Resource
    private SheetDesignCategoryService sheetDesignCategoryService = null;

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)
    public ResponseEntity create(SheetDesignCategory entity) throws BusinessException {
        try {
//        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
//        	entity.setCreatedBy(getCurrentLoggedInUser().getId());
            entity.setLastModifiedBy(UUID.randomUUID());
            entity.setCreatedBy(UUID.randomUUID());
            sheetDesignCategoryService.create(entity);
            return ok(entity);
        } catch (Exception e) {
            logger.error("创建失败！", e);
            return fail("保存失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseEntity update(SheetDesignCategory entity) throws BusinessException{
        try {
//        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
            entity.setLastModifiedBy(UUID.randomUUID());
            sheetDesignCategoryService.updateName(entity);
            return ok(entity);
        } catch (Exception e) {
            logger.error("更新失败！", e);
            return fail("保存失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_DELETE)
    public ResponseEntity delete(SheetDesignCategory entity) throws BusinessException{
        try {
//        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
            entity.setLastModifiedBy(UUID.randomUUID());
            boolean success = sheetDesignCategoryService.deleteLeaf(entity);
            if (success) {
                return ok(entity);
            } else {
                return fail("删除失败！");
            }
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return fail("删除失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
    public ResponseEntity deleteById(HttpServletRequest request) throws BusinessException{
        try {
            sheetDesignCategoryService.deleteById(getUUIDParam(request, "id", UUID.randomUUID()));
            return ok("删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return fail("删除失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
    public ResponseEntity getById(HttpServletRequest request) throws BusinessException{
        try {
            SheetDesignCategory entity = sheetDesignCategoryService.getById(getUUIDParam(request, "id", UUID.randomUUID()));
            if (entity != null) {
                return ok(entity);
            } else {
                return fail("获取失败！");
            }
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return fail("获取失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
    public ResponseEntity findByPage(HttpServletRequest request) throws BusinessException{
        try {
            PageableQueryParameter queryParameter = new PageableQueryParameter();
            queryParameter.setPageNo(getIntegerParam(request, "page", 1));
            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                queryParameter.getParameters().put("keyword", keyword);
            }

            PageableQueryResult queryResult = sheetDesignCategoryService.findByPage(queryParameter);
            return ok(queryResult.getData());
        } catch (Exception e) {
            logger.error("查询数据失败！", e);
            return fail("查询数据失败！");
        }
    }

    @RequestMapping("/getRoot")
    public void getRoot(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            Collection<SheetDesignCategory> root = sheetDesignCategoryService.getRoot();
            writeJson(response,ok(root));
        }catch(Exception e){
            logger.error("查询失败！",e);
            writeJson(response,fail("查询失败！"));
        }
    }

    @RequestMapping("/getRootWithDescendants")
    public void getRootWithDescendants(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            Collection<SheetDesignCategory> root = sheetDesignCategoryService.getRootWithDescendants();
            writeJson(response,ok(root));
        }catch(Exception e){
            logger.error("查询失败！",e);
            writeJson(response,fail("查询失败！"));
        }
    }

    @RequestMapping("/getAllCategory")
    public void getAllCategory(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            Collection<SheetDesignCategory> root = sheetDesignCategoryService.getAllCategory();
            writeJson(response,ok(root));
        }catch(Exception e){
            logger.error("查询失败！",e);
            writeJson(response,fail("查询失败！"));
        }
    }

    @RequestMapping("/getChildren")
    public void getChildren(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            Collection<SheetDesignCategory> children = sheetDesignCategoryService.getChildren(getUUIDParam(request, "id", UUID.randomUUID()));
            writeJson(response,ok(children));
        }catch(Exception e){
            logger.error("查询失败！",e);
            writeJson(response,fail("查询失败！"));
        }
    }

    /**
     * 根据类型获取全部的表格分类包括业务分类的树型数据
     * @param request
     * @param response
     */
    @RequestMapping("/getAllWithClassByType")
    public void getAllWithClassByType(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            Collection<TreeNode> collection = sheetDesignCategoryService.getAllWithClassByType(getByteParam(request, "type", (byte)-1));
            writeJson(response,ok(collection));
        }catch(Exception e){
            logger.error("查询失败！",e);
            writeJson(response,fail("查询失败！"));
        }
    }

    /**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseEntity moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesignCategory entity) throws BusinessException{
        try{
//        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
            entity.setLastModifiedBy(UUID.randomUUID());
            boolean success = sheetDesignCategoryService.moveUp(entity);
            if (success) {
                return ok(entity);
            } else {
                return fail("上移失败！");
            }
        } catch (Exception e) {
            logger.error("上移失败！", e);
            return fail("上移失败！");
        }
    }

    /**
     * 下移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveDown")
    public ResponseEntity moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesignCategory entity) throws BusinessException{
        try{
//        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
            entity.setLastModifiedBy(UUID.randomUUID());
            boolean success = sheetDesignCategoryService.moveDown(entity);
            if (success) {
                return ok(entity);
            } else {
                return fail("下移失败！");
            }
        } catch (Exception e) {
            logger.error("下移失败！", e);
            return fail("下移失败！");
        }
    }
}
