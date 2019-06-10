package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetDesignPageSetup;
import com.newskysoft.iimp.sheet.service.SheetDesignPageSetupService;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.UUID;

/**
 * @ClassName:  SheetDesignPageSetupController
 * @description: 表设计打印变量控制层
 * @author:        lixianfu
 * @date:            2018年6月5日 下午5:27:27
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("sheetDesignPageSetupController")
@RequestMapping("sheet/sheetDesignPageSetup")
public class SheetDesignPageSetupController extends BaseEntityController<SheetDesignPageSetup> {

    private static final Logger logger = LoggerFactory.getLogger(SheetController.class);// 生成日志实体,记录日志

    @Autowired
    private SheetDesignPageSetupService sheetDesignPageSetupService;

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
    public ResponseResult create(SheetDesignPageSetup entity,HttpServletRequest request) {
        try{
        	if(ArrayUtils.isNotEmpty(request.getParameterValues("outputArr"))){
        	    entity.setOutput(String.join(",", request.getParameterValues("outputArr")));
        	}
            if(ArrayUtils.isNotEmpty(request.getParameterValues("alignTypeArr"))){
                entity.setAlignType(String.join(",", request.getParameterValues("alignTypeArr")));
            }
            sheetDesignPageSetupService.save(entity);
            return ResponseResult.success(entity, 1, "保存成功！");
        }catch(Exception e){
            logger.error("sheetDesignPageSetupController create error:{}", e); //将错误信息记录到日志,便于上线后查问题
            return ResponseResult.failure("保存失败.");
        }

    }

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
    public ResponseResult update(SheetDesignPageSetup entity,HttpServletRequest request) {
        try{
        	if(ArrayUtils.isNotEmpty(request.getParameterValues("outputArr"))){
        		entity.setOutput(String.join(",", request.getParameterValues("outputArr")));
        	}
            if(ArrayUtils.isNotEmpty(request.getParameterValues("alignTypeArr"))){
            	entity.setAlignType(String.join(",", request.getParameterValues("alignTypeArr")));
            }
            
            sheetDesignPageSetupService.update(entity);
            return ResponseResult.success(entity, 1, "更新成功！");
        }catch(Exception e){
            logger.error("sheetDesignPageSetupController update error:{}", Throwables.getStackTraceAsString(e)); //将错误信息记录到日志,便于上线后查问题
            return ResponseResult.failure("更新失败.");
        }

    }

    @Override
    @ResponseBody
    @RequestMapping(value=MAPPING_PATH_GET_BY_ID)
    public ResponseResult getById(HttpServletRequest request) {
        try{
            UUID uuid = UUID.fromString(request.getParameter("designId"));
            SheetDesignPageSetup result = sheetDesignPageSetupService.getByDesignId(uuid);
            return ResponseResult.success(result,1,"查询成功");
        }catch (Exception e){
            logger.error("sheetDesignPageSetupController getById error:{}", Throwables.getStackTraceAsString(e)); //将错误信息记录到日志,便于上线后查问题
            return ResponseResult.failure("查询失败.");
        }
    }


    @Override
    @ResponseBody
    @RequestMapping(value=MAPPING_PATH_DELETE_BY_ID)
    public ResponseResult deleteById(HttpServletRequest request) {
        try{
            UUID uuid = UUID.fromString(request.getParameter("designId"));
            sheetDesignPageSetupService.deleteByDesignId(uuid); // UUID.fromString(uuid) 将string 类型参数转为uuid
            return ResponseResult.success(null, 1, "删除成功！");
        }catch(Exception e){
            logger.error("sheetDesignPageSetupController deleteOne error:{}", Throwables.getStackTraceAsString(e)); //将错误信息记录到日志,便于上线后查问题
            return ResponseResult.failure("删除失败.");
        }
    }

    @Override
    public Class<SheetDesignPageSetup> getClazz() {
        return super.getClazz();
    }

    @Override
    public void setClazz(Class<SheetDesignPageSetup> clazz) {
        super.setClazz(clazz);
    }

    public SheetDesignPageSetupController() {
        super();
    }
}
