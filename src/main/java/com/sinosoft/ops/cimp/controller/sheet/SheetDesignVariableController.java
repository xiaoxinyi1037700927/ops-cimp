package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.DataStatus;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetDesignVariable;
import com.newskysoft.iimp.sheet.service.SheetDesignVariableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


/**
 * @ClassName:  SheetDesignVariableController
 * @description: 表设计变量控制器
 * @author:        lixianfu
 * @date:            2018年6月4日 下午3:23:12
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("SheetDesignVariableController")
@RequestMapping("sheet/SheetDesignVariable")
public class SheetDesignVariableController extends BaseEntityController<SheetDesignVariable>{
	private static final Logger logger = LoggerFactory.getLogger(SheetController.class);

    @Autowired
    private SheetDesignVariableService sheetDesignVariableService;

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
    @Override
	public ResponseResult create(SheetDesignVariable entity) {
    	 try{
			 entity.setStatus(DataStatus.NORMAL.getValue());
			 entity.setOrdinal(sheetDesignVariableService.getNextOrdinal());
			 entity.setId(UUID.randomUUID());
			 entity.setCreatedBy(getCurrentLoggedInUser().getId());
			 entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
			 entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
         	sheetDesignVariableService.saveVariable(entity);
             return ResponseResult.success(entity, 1, "保存成功！");
         }catch(Exception e){
         	e.printStackTrace();
             logger.error("SheetDesignVariableController create error:{}", Throwables.getStackTraceAsString(e)); 
             return ResponseResult.failure("保存失败.");
         }
	}

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignVariable entity, String id) {
    	try {
           // entity.setLastModifiedBy(entity.setCreatedBy(getCurrentUser().getId())); 
    		UUID uuid = UUID.fromString(id);
            sheetDesignVariableService.updateVariable(entity, uuid);
            return ResponseResult.success(entity, 1, "修改成功！");
        }catch(Exception e){
            logger.error("sheetDesignVariableController update error:{}", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure("更新失败.");
        }
	}

	@ResponseBody
    @RequestMapping(value = MAPPING_PATH_DELETE)
	public ResponseResult delete(String id) {
		 try{
			 UUID uuid = UUID.fromString(id);
			 sheetDesignVariableService.deleteVariable(uuid); 
	            return ResponseResult.success("删除成功!");
	        }catch(Exception e){
	            logger.error("SheetDesignVariableController deleteOne error:{}", Throwables.getStackTraceAsString(e)); 
	            return ResponseResult.failure("删除失败.");
	        }
	}

	@Override
	@RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
	public ResponseResult deleteById(HttpServletRequest request) {
		try{
			sheetDesignVariableService.deleteById(UUID.fromString(request.getParameter("id")));
			return ResponseResult.success("删除成功!");
		}catch(Exception e){
			logger.error("SheetDesignVariableController deleteOne error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure("删除失败.");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getByDesignId")
	public ResponseResult getByDesignId(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<SheetDesignVariable> entitys = sheetDesignVariableService
					.getByDesignId(getUUIDParam(request, "designId", UUID.randomUUID()));
			return ResponseResult.success(entitys, entitys.size(), "获取成功！");
		} catch (Exception e) {
			logger.error("获取失败！", e);
			return ResponseResult.failure("获取失败！");
		}
	}

	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResponseResult getById(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResponseResult delete(SheetDesignVariable entity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResponseResult update(SheetDesignVariable entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
