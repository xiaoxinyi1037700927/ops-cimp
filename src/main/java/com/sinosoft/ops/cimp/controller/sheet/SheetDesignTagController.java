package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.DataStatus;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetDesignTag;
import com.newskysoft.iimp.sheet.service.SheetDesignTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.util.UUID;


/**
 * @ClassName:  SheetDesignTagController
 * @description: 表格设计标签控制器
 * @author:        lixianfu
 * @date:            2018年6月6日 下午3:42:14
 * @version        1.0.0
 * @since            JDK 1.7
 */

@Controller("SheetDesignTagController")
@RequestMapping("sheet/SheetDesignTag")
public class SheetDesignTagController extends BaseEntityController<SheetDesignTag>{
	private static final Logger logger = LoggerFactory.getLogger(SheetController.class);

    @Autowired
    private SheetDesignTagService sheetDesignTagService;
    
    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)
	@Override
	public ResponseResult create(SheetDesignTag entity) {
		try {
//			addTrackData(entity);
			System.out.println(entity+"==================================");
			sheetDesignTagService.saveDesignTag(entity);
			return ResponseResult.success(entity, 1, "添加成功！");
		} catch (Exception e) {
			logger.error("SheetDesignTagController create error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "添加失败！");
		}
	}

	@Override
	@ResponseBody
	@RequestMapping(MAPPING_PATH_GET_BY_ID)
	public ResponseResult getById(HttpServletRequest request) {
		try{
			SheetDesignTag result = sheetDesignTagService.getById(toUUID(request.getParameter("id")));
			return ResponseResult.success(result,1,"查询成功");
		}catch (Exception e){
			logger.error("SheetDesignTagController getById error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0,"查询失败.");
		}
	}

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignTag entity,HttpServletRequest request) {
    	try {
           // entity.setLastModifiedBy(entity.setCreatedBy(getCurrentUser().getId())); 
    		addTrackData(entity);
    		sheetDesignTagService.updateDesignTag(entity);
            return ResponseResult.success(entity, 1, "修改成功！");
        }catch(Exception e){
            logger.error("SheetDesignTagController update error:{}", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure(0,"更新失败.");
        }
	}

	@Override
	@ResponseBody
	@RequestMapping(MAPPING_PATH_DELETE_BY_ID)
	public ResponseResult deleteById(HttpServletRequest request) {
		try{
			sheetDesignTagService.deleteDesignTag(toUUID(request.getParameter("id")));
			return ResponseResult.success("删除成功!");
		}catch(Exception e){
			logger.error("SheetDesignTagController deleteOne error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0,"删除失败.");
		}
	}

    private UUID toUUID(String id) {
    	UUID uuid = UUID.fromString(id);
		return uuid; 
       	}
    private void addTrackData(SheetDesignTag entity) {
         	if (entity.getStatus() == null) {
         		entity.setStatus(DataStatus.NORMAL.getValue());
         	}
         	if(entity.getCreatedTime() == null){
         		entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));;
	        }	    	
	        if(entity.getCreatedBy() == null){
	        	entity.setCreatedBy(getCurrentLoggedInUser().getId());
	        }
	        if(entity.getLastModifiedTime() == null){
	        	entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
	        }
	        if(entity.getLastModifiedBy() == null){
	        	entity.setLastModifiedBy(UUID.randomUUID());
	        }
	}

	@Override
	public ResponseResult update(SheetDesignTag entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult delete(SheetDesignTag entity) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

    
}
