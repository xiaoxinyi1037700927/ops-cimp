package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetDesignCell;
import com.newskysoft.iimp.sheet.service.SheetDesignCellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
/**
 * @ClassName:  SheetDesignCellController
 * @description: 表格设计单元格控制器
 * @author:        lixianfu
 * @date:            2018年6月5日 下午2:40:24
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("SheetDesignCellController")
@RequestMapping("sheet/SheetDesignCell")
public class SheetDesignCellController extends BaseEntityController<SheetDesignCell>{
	
	private static final Logger logger = LoggerFactory.getLogger(SheetController.class);
	
	@Autowired
	private SheetDesignCellService sheetDesignCellService;
	  
	  
	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
	@Override
	public ResponseResult create(SheetDesignCell entity) {
		try {
			entity.setId(UUID.randomUUID());
			addTrackData(entity);
			sheetDesignCellService.create(entity);
			return ResponseResult.success(entity, 1, "保存成功！");
		} catch (Exception e) {
			logger.error("sheetDesignCellController create error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "保存失败！");
		}
	}
	
	@ResponseBody
    @RequestMapping("/getById")
	public ResponseResult getById(String id) {
		 try{
    		 UUID uuid = UUID.fromString(id);
    		 System.out.println("id -------> " + uuid);
    		 SheetDesignCell result = sheetDesignCellService.getById(uuid);
    		 System.out.println(result);
             return ResponseResult.success(result,1,"查询成功");
         }catch (Exception e){
             logger.error("sheetDesignCellController getById error:{}", Throwables.getStackTraceAsString(e));
             return ResponseResult.failure(0, "保存失败！");
         }
	}
	
	@ResponseBody
    @RequestMapping("/deleteOne")
	public ResponseResult deleteOne(String id) {
		 try{
			 System.out.println(id);
			 UUID uuid = UUID.fromString(id);
			
			 sheetDesignCellService.deleteById(uuid); 
	            return ResponseResult.success("删除成功!");
	        }catch(Exception e){
	            logger.error("sheetDesignCellController deleteOne error:{}", Throwables.getStackTraceAsString(e)); 
	            return ResponseResult.failure(0, "保存失败！");
	        }
	}
	
	@Override
	@ResponseBody
    @RequestMapping(value = MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignCell entity) {
		try {
			addTrackData(entity);
			sheetDesignCellService.update(entity);
            return ResponseResult.success(entity, 1, "修改成功！");
        }catch(Exception e){
            logger.error("sheetDesignCellController update error:{}", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure(0, "保存失败！");
        }
	}

	@Override
	public ResponseResult delete(SheetDesignCell entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult deleteById(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult getById(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping("/getByPosition")
	@ResponseBody
	public ResponseResult getByPosition(HttpServletRequest request, HttpServletResponse response
			,String designId,Integer rowNo,Integer columnNo){
		try {
			SheetDesignCell cell = sheetDesignCellService.getByPosition(UUID.fromString(designId),rowNo,columnNo);
			return ResponseResult.success(cell, 1, "根据位置获取单元格成功");
		}catch(IllegalArgumentException e){
			logger.error(e.getMessage(), e);
			return ResponseResult.failure(e.getMessage());
		} catch (Exception e) {
			logger.error("根据位置获取单元格失败",e);
			return ResponseResult.failure("根据位置获取单元格失败");
		}
	}
	
}
