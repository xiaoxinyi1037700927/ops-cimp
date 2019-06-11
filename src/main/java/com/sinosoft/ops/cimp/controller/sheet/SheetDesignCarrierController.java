/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.Constants;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCarrierService;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @ClassName: SheetDesignCarrierController
 * @Description: 表格设计载体控制器
 * @Author: Nil
 * @Date: 2017年8月18日 下午1:16:08
 * @Version 1.0.0
 */
@Controller("sheetDesignCarrierController")
@RequestMapping("sheet/designCarrier")
public class SheetDesignCarrierController extends BaseEntityController<SheetDesignCarrier> {
    private static final Logger logger = LoggerFactory.getLogger(SheetDesignCarrierController.class);

    @Resource
    private SheetDesignCarrierService sheetDesignCarrierService = null;

//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_CREATE)
//    public ResponseResult create(SheetDesignCarrier entity) {
//        try {
//        	addTrackData(entity);
//        	UUID id = UUID.randomUUID();
//        	entity.setId(id);
//        	byte type = 0;
//        	entity.setType(type);
//            sheetDesignCarrierService.create(entity);
//            return ResponseResult.success(entity,1,"保存成功！");
//        } catch (Exception e) {
//            logger.error("创建失败！", e);
//            return ResponseResult.failure("保存失败！");
//        }
//    }
//
//    @ResponseBody
//
//    @RequestMapping(value = MAPPING_PATH_UPDATE)
//    public ResponseResult update(SheetDesignCarrier entity) {
//        try {
//        	addTrackData(entity);
//        	byte type = 0;
//        	entity.setType(type);
//            sheetDesignCarrierService.update(entity);
//            return ResponseResult.success("保存成功！");
//        } catch (Exception e) {
//            logger.error("更新失败！", e);
//            return ResponseResult.failure("保存失败！");
//        }
//    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_DELETE)
    public ResponseResult delete(SheetDesignCarrier entity) {
        try {
            sheetDesignCarrierService.delete(entity);
            return ResponseResult.success("删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return ResponseResult.failure("删除失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
    public ResponseResult deleteById(HttpServletRequest request) {
        try {
            sheetDesignCarrierService.deleteById(request.getParameter("id"));
            return ResponseResult.success("删除成功！");
        } catch (Exception e) {
            logger.error("删除失败！", e);
            return ResponseResult.failure("删除失败！");
        }
    }

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_GET_BY_ID)
    public ResponseResult getById(HttpServletRequest request) {
        try {
            SheetDesignCarrier entity = sheetDesignCarrierService.getById(request.getParameter("id"));
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

    @ResponseBody
    
    @RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
    public ResponseResult findByPage(HttpServletRequest request) {
        try {
            PageableQueryParameter queryParameter = new PageableQueryParameter();
            queryParameter.setPageNo(getIntegerParam(request, "page", 1));
            queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));
            String keyword = request.getParameter("keyword");
            if (keyword != null) {
                queryParameter.getParameters().put("keyword", keyword);
            }

            PageableQueryResult queryResult = sheetDesignCarrierService.findByPage(queryParameter);
            return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
        } catch (Exception e) {
            logger.error("查询数据失败！", e);
            return ResponseResult.failure("查询数据失败！");
        }
    }
    
    @RequestMapping("getByDesignId")
    public void getByDesignId(HttpServletRequest request, HttpServletResponse response) {
        try {
            SheetDesignCarrier entity = sheetDesignCarrierService.getByDesignId(getUUIDParam(request, "designId", UUID.randomUUID()));
            writeJson(response, ResponseResult.success(entity,1));
        } catch (Exception e) {
            logger.error("根据标识获取表格载体失败！", e);
            writeJson(response, ResponseResult.failure("获取失败！"));
        }
    }

    @RequestMapping("getContentByDesignId")
    public void getContentByDesignId(HttpServletRequest request, HttpServletResponse response) {
        try {
            SheetDesignCarrier entity = sheetDesignCarrierService.getByDesignId(getUUIDParam(request, "designId", UUID.randomUUID()));
            if(entity!=null&&entity.getContent()!=null){
                JSONObject obj = new JSONObject();
                obj.put("id", entity.getId());
                obj.put("content", new String(entity.getContent()));                
            	writeJson(response, ResponseResult.success(obj.toString(), 1));
                //writeJson(response, ResponseResult.success(1, new String(entity.getContent(), "UTF-8")));
            }else{
                writeJson(response, ResponseResult.failure("获取失败！"));
            }
        } catch (Exception e) {
            logger.error("根据标识获取表格载体失败！", e);
            writeJson(response, ResponseResult.failure("获取失败！"));
        }
    }


}
