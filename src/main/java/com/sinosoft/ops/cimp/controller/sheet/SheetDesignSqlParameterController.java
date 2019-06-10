package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetDesignSqlParameter;
import com.newskysoft.iimp.sheet.service.SheetDesignSqlParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.UUID;


/**
 * @ClassName:  SheetDesignSqlParameterController
 * @description: 表设计SQL参数控制器
 * @author:        kanglin
 * @date:            2018年6月6日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("SheetDesignSqlParameterController")
@RequestMapping("sheet/sheetDesignSqlParameter")
public class SheetDesignSqlParameterController extends BaseEntityController<SheetDesignSqlParameter>{
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignSqlParameterController.class);

    @Autowired
    private SheetDesignSqlParameterService sheetDesignSqlParameterService;

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
    @Override
	public ResponseResult create(SheetDesignSqlParameter entity) {
    	 try{
         	System.out.println("Id --->" + entity.getId());
         	
         	sheetDesignSqlParameterService.save(entity);
             return ResponseResult.success(null,1,"OK");
         }catch(Exception e){
         	e.printStackTrace();
             logger.error("SheetDesignSqlParameterController create error:{}", Throwables.getStackTraceAsString(e)); 
             return ResponseResult.failure("保存失败.");
         }
	}


	@Override
	@ResponseBody
	@RequestMapping(MAPPING_PATH_GET_BY_ID)
	public ResponseResult getById(HttpServletRequest request) {
		try{
			UUID uuid = UUID.fromString(request.getParameter("id"));
			SheetDesignSqlParameter result = sheetDesignSqlParameterService.getById(uuid);
			return ResponseResult.success(result,1,"查询成功");
		}catch (Exception e){
			logger.error("SheetDesignDataSourceController getById error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure("查询失败.");
		}
	}

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignSqlParameter entity, String id) {
    	try {
           // entity.setLastModifiedBy(entity.setCreatedBy(getCurrentUser().getId())); 
    		UUID uuid = UUID.fromString(id);
    		System.out.println(entity+"===================="+uuid);
    		sheetDesignSqlParameterService.update(entity, uuid);
            return ResponseResult.success("修改成功!");
        }catch(Exception e){
            logger.error("SheetDesignSqlParameterController update error:{}", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure("更新失败.");
        }
	}

	@Override
	@ResponseBody
	@RequestMapping(MAPPING_PATH_DELETE_BY_ID)
	public ResponseResult deleteById(HttpServletRequest request) {
		try{
			UUID uuid = UUID.fromString(request.getParameter("id"));
			sheetDesignSqlParameterService.delete(uuid);
			return ResponseResult.success("删除成功!");
		}catch(Exception e){
			logger.error("SheetDesignSqlParameterController deleteOne error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure("删除失败.");
		}
	}


	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult delete(SheetDesignSqlParameter entity) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResponseResult update(SheetDesignSqlParameter entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
