package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.annotation.RequiresAuthentication;
import com.sinosoft.ops.cimp.annotation.SheetApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSourceCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sheet.SheetDataSourceCategoryService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 报表分类控制器
 */
@SheetApiGroup
@Api(description = "报表分类控制器")
@Controller("sheetDataSourceCategoryController")
@RequestMapping("sheet/sheetDataSourceCategory")
public class SheetDataSourceCategoryController extends BaseController {
    @Autowired
    private SheetDataSourceCategoryService sheetDataSourceCategoryService;

    SecurityUtils securityUtils = new SecurityUtils();

    @ApiOperation(value = "新增数据源分类")
    @PostMapping("/create")
    @RequiresAuthentication
    public ResponseEntity<String> create(SheetDataSourceCategory entity) throws BusinessException{
        try{
            Integer id = Integer.parseInt(sheetDataSourceCategoryService.getMaxId());
            entity.setId(id);
            entity.setStatus((byte)0);
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            entity.setOrdinal(sheetDataSourceCategoryService.getNextOrdinal());
            sheetDataSourceCategoryService.create(entity);
            return ok("操作成功");
        }catch(Exception e){
            return fail("创建失败!");
        }
    }

    @ApiOperation(value = "修改数据源分类")
    @PostMapping("/update")
    @RequiresAuthentication
    public ResponseEntity<String> update(SheetDataSourceCategory entity) throws BusinessException{
        try{
            SheetDataSourceCategory updateentity = sheetDataSourceCategoryService.getById(entity.getId());

            updateentity.setName(entity.getName());
            updateentity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDataSourceCategoryService.update(updateentity);
            return ok("操作成功");
        }catch(Exception e){
            return fail("修改失败!");
        }
    }

    @ApiOperation(value = "删除数据源分类")
    @PostMapping("/delete")
    @RequiresAuthentication
    public ResponseEntity<String> delete(SheetDataSourceCategory entity) throws BusinessException{
        try{
            sheetDataSourceCategoryService.update(entity);
            return ok("操作成功");
        }catch(Exception e){
            return fail("删除失败!");
        }
    }

    @ApiOperation(value = "删除数据源分类")
    @PostMapping("/deleteById")
    @RequiresAuthentication
    public ResponseEntity<String> deleteById(@RequestParam("id") Integer id) throws BusinessException{
        try{
            sheetDataSourceCategoryService.deleteById(id);
            return ok("操作成功");
        }catch(Exception e){
            return fail("删除失败!");
        }
    }

    @ApiOperation(value = "获取数据源分类")
    @PostMapping("/getById")
    @RequiresAuthentication
    public ResponseEntity<String> getById(@RequestParam("id") Integer id) throws BusinessException{
        try{
            SheetDataSourceCategory sheetDataSourceCategory = sheetDataSourceCategoryService.getById(id);
            return ok(sheetDataSourceCategory);
        }catch(Exception e){
            return fail("获取数据失败!");
        }
    }

    @ApiOperation(value = "获取数据源分类树形结构")
    @PostMapping("/getCategoryTree")
    @RequiresAuthentication
    public ResponseEntity<String> getCategoryTree(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            List<HashMap<String, Object>> root = sheetDataSourceCategoryService.GetCategoryTree();
            return ok(root);
        }catch(Exception e){
            return fail("查询失败!");
        }
    }

    @ApiOperation(value = "获取数据源分类子节点")
    @PostMapping("/findAllChildren")
    @RequiresAuthentication
    public ResponseEntity<String> findAll(HttpServletRequest request, HttpServletResponse response) throws BusinessException{
        try{
            Collection<SheetDataSourceCategory> collection = sheetDataSourceCategoryService.findAllChildren();
            return ok(collection);
        }catch(Exception e){
            return fail("查询失败!");
        }
    }

    @ApiOperation(value = "上移节点")
    @PostMapping("/moveUp")
    @RequiresAuthentication
    public ResponseEntity<String> moveUp(HttpServletRequest request, HttpServletResponse response, SheetDataSourceCategory entity) throws BusinessException{
        try{
            String userid = securityUtils.getCurrentUser().getId();
            entity.setLastModifiedBy(UUID.fromString(userid));
            boolean success = sheetDataSourceCategoryService.moveUp(entity);
            if (success) {
                return ok("上移成功!");
            }else{
                return fail("上移失败!");
            }
        }catch(Exception e){
            return fail("上移失败!");
        }
    }

    @ApiOperation(value = "下移节点")
    @PostMapping("/moveDown")
    @RequiresAuthentication
    public ResponseEntity<String> moveDown(HttpServletRequest request, HttpServletResponse response, SheetDataSourceCategory entity) throws BusinessException{
        try{
            String userid = securityUtils.getCurrentUser().getId();
            entity.setLastModifiedBy(UUID.fromString(userid));
            boolean success = sheetDataSourceCategoryService.moveDown(entity);
            if (success) {
                return ok("下移成功!");
            }else{
                return fail("下移失败!");
            }
        }catch(Exception e){
            return fail("下移失败!");
        }
    }
}
