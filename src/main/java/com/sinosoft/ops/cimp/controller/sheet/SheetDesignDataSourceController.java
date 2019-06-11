package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.DataStatus;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.sheet.model.SheetDataSource;
import com.newskysoft.iimp.sheet.model.SheetDesignDataSource;
import com.newskysoft.iimp.sheet.service.SheetDataSourceService;
import com.newskysoft.iimp.sheet.service.SheetDesignDataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;


/**
 * @ClassName:  SheetDesignDataSourceController
 * @description: 表设计数据源控制器
 * @author:        kanglin
 * @date:            2018年6月5日 下午
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("sheetDesignDataSourceController")
@RequestMapping("sheet/sheetDesignDataSource")
public class SheetDesignDataSourceController extends BaseEntityController<SheetDesignDataSource>{
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignDataSourceController.class);

    @Autowired
    private SheetDesignDataSourceService sheetDesignDataSourceService;
    
    @Autowired
    private SheetDataSourceService sheetDataSourceService;

    @ResponseBody
    @RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
    @Override
	public ResponseResult create(SheetDesignDataSource entity) {
    	 try{
         	entity.setId(UUID.randomUUID());
         	addTrackData(entity);
         	SheetDataSource dataSource = sheetDataSourceService.getById(entity.getDatasourceId());
         	 //解析sql语句把表名赋给数据范围
            sheetDesignDataSourceService.analyzeSqlExpress(entity,dataSource.getSql());
            
         	sheetDesignDataSourceService.save(entity);
            return ResponseResult.success(entity,1,"OK");
         }catch(Exception e){
         	e.printStackTrace();
             logger.error("SheetDesignDataSourceController create error:{}", Throwables.getStackTraceAsString(e)); 
             return ResponseResult.failure("保存失败.");
         }
	}
    
    @ResponseBody
    @RequestMapping("multipleCreate")
    public ResponseResult multipleCreate(HttpServletRequest request, HttpServletResponse response,@RequestBody List<SheetDesignDataSource> sources){
		try {
			for (SheetDesignDataSource sheetDesignDataSource : sources) {
				sheetDesignDataSource.setId(UUID.randomUUID());
				addTrackData(sheetDesignDataSource);
				SheetDataSource dataSource = sheetDataSourceService.getById(sheetDesignDataSource.getDatasourceId());
				sheetDesignDataSourceService.analyzeSqlExpress(sheetDesignDataSource,dataSource.getSql());
				sheetDesignDataSourceService.save(sheetDesignDataSource);
			}
			return ResponseResult.success(sources, sources.size(), "多条添加数据源成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("多条添加数据源失败",e);
			return ResponseResult.failure("多条添加数据源失败");
		}
    	
    } 
    

	@Override
	@RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
	@ResponseBody
	public ResponseResult deleteById(HttpServletRequest request) {
		try {
			sheetDesignDataSourceService.deleteById(UUID.fromString(request.getParameter("id")));
			return ResponseResult.success("根据id删除成功");
		} catch (Exception e) {
			logger.error("根据id删除失败",e);
			return ResponseResult.failure("根据id删除失败");
		}
	}


	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@RequestMapping(value=MAPPING_PATH_GET_BY_ID)
	@ResponseBody
	public ResponseResult getById(HttpServletRequest request) {
		try{
   		 	UUID uuid = UUID.fromString(request.getParameter("id"));
   		 	SheetDesignDataSource result = sheetDesignDataSourceService.getById(uuid);
            return ResponseResult.success(result,1,"查询成功");
        }catch (Exception e){
            logger.error("SheetDesignDataSourceController getById error:{}", Throwables.getStackTraceAsString(e));
            return ResponseResult.failure("查询失败.");
        }
	}
	@Override
	@RequestMapping(value=MAPPING_PATH_DELETE)
	@ResponseBody
	public ResponseResult delete(SheetDesignDataSource entity) {
		try {
			sheetDesignDataSourceService.delete(entity);
			return ResponseResult.success("删除成功");
		} catch (Exception e) {
			logger.error("删除失败",e);
			return ResponseResult.failure("删除失败");
		}
	}
	
	@Override
	@RequestMapping(value=MAPPING_PATH_UPDATE)
	@ResponseBody
	public ResponseResult update(SheetDesignDataSource entity) {
		try {
			addTrackData(entity);
			 //解析sql语句把表名赋给数据范围
			SheetDataSource dataSource = sheetDataSourceService.getById(entity.getDatasourceId());
            sheetDesignDataSourceService.analyzeSqlExpress(entity,dataSource.getSql());
			sheetDesignDataSourceService.update(entity);
			return ResponseResult.success(entity, 1,"更新数据成功");
		} catch (Exception e) {
			logger.error("更新数据失败",e);
			return ResponseResult.failure("更新数据失败");
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getByDesignId")
	public ResponseResult getByDesignId(HttpServletRequest request, HttpServletResponse response,String designId) {

		try {
			Collection<SheetDesignDataSource> collections = sheetDesignDataSourceService.getByDesignId(UUID.fromString(designId));
			if (collections != null) {
				Collection<SheetDesignDataSource> lstReturn= new ArrayList<>();
				Map<String,SheetDesignDataSource> map = new HashMap<>();
				for(SheetDesignDataSource sheetDesignDataSource:collections)
				{
					map.put(sheetDesignDataSource.getDatasourceId().toString(),sheetDesignDataSource);
				}

				for(String key :map.keySet())
				{
					lstReturn.add(map.get(key));
				}
				return ResponseResult.success(lstReturn, lstReturn.size());
			} else {
				return ResponseResult.failure("查询数据失败！");
			}
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}

	}

	@ResponseBody
	@RequestMapping(value = "/getBingData")
	public ResponseResult getBingData(HttpServletRequest request, HttpServletResponse response,String designId,String sectionNo) {

		try {
			SheetDesignDataSource data = sheetDesignDataSourceService.getBingData(UUID.fromString(designId),sectionNo);
			return ResponseResult.success(data, 1);
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}
	}

	@ResponseBody
	@RequestMapping("/GetTree")
	public ResponseResult GetTree(HttpServletRequest request, HttpServletResponse response,String designId){
		try
		{
			List<Map<String, Object>> root = sheetDesignDataSourceService.GetTree(UUID.fromString(designId));

			return ResponseResult.success(root,root.size());
		}catch(Exception e){
			logger.error("查询失败！",e);
			return ResponseResult.failure("查询失败");
		}
	}
	
	@RequestMapping("/getByDesignIdDistinct")
	@ResponseBody
	public ResponseResult getByDesignIdDistinct(String designId){
		try {
			Collection<SheetDesignDataSource> collection = sheetDesignDataSourceService.getByDesignIdDistinct(UUID.fromString(designId));
			return ResponseResult.success(collection, collection.size(), "根据模板id获取去重数据成功");
		} catch (Exception e) {
			logger.error("根据模板id获取去重数据失败",e);
			return ResponseResult.failure("根据模板id获取去重数据失败");
		}
	}
	
	@RequestMapping("/deleteByDesignIdAndDataSourceId")
	@ResponseBody
	public ResponseResult deleteByDesignIdAndDataSourceId(String designId,String dataSourceId){
		try {
			sheetDesignDataSourceService.deleteByDesignIdAndDataSourceId(UUID.fromString(designId),UUID.fromString(dataSourceId));
			return ResponseResult.success("根据数据源id删除数据成功");
		} catch (Exception e) {
			logger.error("根据数据源id删除数据失败",e);
			return ResponseResult.failure("根据数据源id删除数据失败");
		}
	}
	
	private void addTrackData(SheetDesignDataSource entity) {
		// 创建人
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(getCurrentLoggedInUser().getId());
        }
        // 创建时间
        if (entity.getCreatedTime() == null) {
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        }
        // 最后修改人
        entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
        // 最后修改时间
        entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));

        // 数据状态
        if (entity.getStatus() == null) {
            entity.setStatus(DataStatus.NORMAL.getValue());
        }
        
        //次序
        if(entity.getOrdinal() == null){
        	Integer ordinal = sheetDesignDataSourceService.getMaxOrdinal()==null?0:sheetDesignDataSourceService.getMaxOrdinal();
        	entity.setOrdinal(ordinal+1);
        }
        
       
	}

}
