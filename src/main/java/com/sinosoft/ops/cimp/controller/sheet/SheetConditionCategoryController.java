package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionCategoryService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @description: 条件项组控制类
 */
@Controller("sheetConditionCategoryController")
@RequestMapping("sheet/sheetConditionCategory")
public class SheetConditionCategoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SheetConditionCategoryController.class);

    @Autowired
    private SheetConditionCategoryService sheetConditionCategoryService;

    @RequestMapping("/GetCategoryTree")
    @ResponseBody
    public ResponseEntity GetCategoryTree() throws BusinessException {
        try {
            Collection<Map<String, Object>> list = sheetConditionCategoryService.getCategoryTree();
            
            BaseResult result = BaseResult.ok(list);
            result.setMessage("查询成功！");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("SheetConditionCategoryController getConditionCategoryList error:{}", Throwables.getStackTraceAsString(e));
            return fail("查询失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/findAllChildren")
    public ResponseEntity findAll() throws BusinessException {
        try {
            Collection<SheetConditionCategory> collection = sheetConditionCategoryService.findAllChildren();

            return ok(collection);
        } catch (Exception e) {
            logger.error("查询失败！", e);
            return fail("查询失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
    public ResponseEntity create(SheetConditionCategory entity) throws BusinessException {
        try {
            addTrackData(entity);
            entity.setOrdinal(sheetConditionCategoryService.getNextOrdinal());
            sheetConditionCategoryService.save(entity);
            return ok(entity);
        } catch (Exception e) {
            logger.error("sheetConditionCategoryController create error:{}", Throwables.getStackTraceAsString(e));
            return fail("保存失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseEntity update(SheetConditionCategory entity) throws BusinessException {
        try {
            addTrackData(entity);
            sheetConditionCategoryService.update(entity);
            return ok(entity);
        } catch (Exception e) {
            logger.error("sheetConditionCategoryController update error:{}", Throwables.getStackTraceAsString(e));
            return fail("修改失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_DELETE)
    public ResponseEntity delete(SheetConditionCategory entity, HttpServletRequest request) throws BusinessException {
        try {
            sheetConditionCategoryService.deleteId(UUID.fromString(request.getParameter("Id")).toString());
            return ok(entity);
        } catch (Exception e) {
            logger.error("sheetDesignExpressionController deleteOne error:{}", Throwables.getStackTraceAsString(e));
            return fail("删除失败！");
        }
    }

    /**
     * 上移节点
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseEntity moveUp(SheetConditionCategory entity) throws BusinessException {
        try {
            entity.setLastModifiedBy(SecurityUtils.getSubject().getCurrentUser().getId());
            boolean success = sheetConditionCategoryService.moveUp(entity);
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
     */
    @ResponseBody
    @RequestMapping("/moveDown")
    public ResponseEntity moveDown(SheetConditionCategory entity) throws BusinessException {
        try {
            entity.setLastModifiedBy(SecurityUtils.getSubject().getCurrentUser().getId());
            boolean success = sheetConditionCategoryService.moveDown(entity);
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

    private void addTrackData(SheetConditionCategory entity) {
        UUID id = UUID.randomUUID();
        if (entity.getId() == null) {
            entity.setId(id.toString());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(DataStatus.NORMAL.getValue());
        }
        if (entity.getCreatedTime() == null) {
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        }
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(SecurityUtils.getSubject().getCurrentUser().getId());
        }
        if (entity.getLastModifiedTime() == null) {
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        }
        if (entity.getLastModifiedBy() == null) {
            entity.setLastModifiedBy(id.toString());
        }
    }
}
