/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetRule;
import com.newskysoft.iimp.sheet.service.SheetRuleService;

/**
 * @ClassName: SheetRuleController
 * @Description: 表格规则控制器
 * @Version 1.0.0
 */
@Controller("sheetRuleController")
@RequestMapping("sheet/rule")
public class SheetRuleController extends BaseEntityController<SheetRule> {
    private static final Logger logger = LoggerFactory.getLogger(SheetRuleController.class);

    @Resource
    private SheetRuleService sheetRuleService = null;

    @ResponseBody
    @Override
    @RequestMapping(value = MAPPING_PATH_CREATE)
    public ResponseResult create(SheetRule entity) {
        try {
            sheetRuleService.create(entity);
            return ResponseResult.success(entity,1,"保存成功！");
        } catch (Exception e) {
            logger.error("创建失败！", e);
            return ResponseResult.failure("保存失败！");
        }
    }

    @ResponseBody
    @Override
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseResult update(SheetRule entity) {
        try {
            sheetRuleService.update(entity);
            return ResponseResult.success(null,1,"更新成功！");
        } catch (Exception e) {
            logger.error("更新失败！", e);
            return ResponseResult.failure("更新失败！");
        }
    }


    @ResponseBody
    @Override
    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
    public ResponseResult deleteById(HttpServletRequest request) {
        try {
            sheetRuleService.deleteById(UUID.fromString(request.getParameter("id")));
            return ResponseResult.success(null,1,"删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return ResponseResult.failure("删除失败！");
        }
    }

    @ResponseBody
    @Override
    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
    public ResponseResult getById(HttpServletRequest request) {
        try {
            SheetRule entity = sheetRuleService.getById(UUID.fromString(request.getParameter("id")));
            if (entity != null) {
                return ResponseResult.success(entity, 1, "获取成功！");
            } else {
                return ResponseResult.failure("获取失败！");
            }
        } catch (Exception e) {
            logger.error("获取失败！", e);
            return ResponseResult.failure("获取失败！");
        }
    }

	@Override
	public ResponseResult delete(SheetRule entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

 
}
