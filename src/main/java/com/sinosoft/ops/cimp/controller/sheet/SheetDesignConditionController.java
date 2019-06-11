package com.sinosoft.ops.cimp.controller.sheet;


import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignConditionService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

/**
 * 
 * @ClassName:  SheetDesignConditionController
 * @author:        xieJY
 * @date:            2018年5月29日 下午9:10:25
 * @version        1.0.0
 */
@Controller("sheetDesignConditionController")
@RequestMapping("sheet/sheetDesignCondition")
public class SheetDesignConditionController extends BaseEntityController<SheetDesignCondition> {

	private static final Logger logger = LoggerFactory.getLogger(SheetDesignConditionController.class);

	@Resource
	private SheetDesignConditionService sheetDesignConditionService = null;

	@Resource
	private SheetConditionService sheetConditionService = null;

	@ResponseBody
	@RequestMapping("/addList")
	public ResponseResult addList(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SheetDesignCondition> sheetDesignConditions)
	{
		try{
			for(SheetDesignCondition entity : sheetDesignConditions)
			{
				entity.setConditionId(entity.getId());
				entity.setStatus(DataStatus.NORMAL.getValue());
				entity.setOrdinal(sheetDesignConditionService.getNextOrdinal());
				entity.setId(UUID.randomUUID());
				entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
				entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));

				sheetDesignConditionService.create(entity);
			}
			return ResponseResult.success(null, sheetDesignConditions.size(), "保存成功！");
		} catch (Exception e) {
			logger.error("addList error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "保存失败！");
		}
	}

	
	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_CREATE)
	public ResponseResult create(SheetDesignCondition entity) {
		try {
			UUID id = UUID.randomUUID();
			entity.setId(id);
			System.out.println(entity);
			sheetDesignConditionService.create(entity);
			return ResponseResult.success(id, 1, "保存成功！");
		} catch (Exception e) {
			logger.error("创建失败！", e);
			return ResponseResult.failure("保存失败！");
		}
	}

	
	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignCondition entity) {
		try {
			sheetDesignConditionService.update(entity);
			 return ResponseResult.success(entity,1,"保存成功！");
		} catch (Exception e) {
			logger.error("更新失败！", e);
			 return ResponseResult.failure("保存失败！");
		}
	}

	
	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_DELETE)
	public ResponseResult delete(SheetDesignCondition entity) {
		try {
			SheetCondition sheetCondition = sheetConditionService.getById(entity.getConditionId());
			sheetDesignConditionService.delete(entity);
			if(sheetCondition.getDesignId() == entity.getDesignId())
			{
				sheetConditionService.delete(sheetCondition);
			}
			return ResponseResult.success(entity,1,"删除成功！");
		} catch (Exception e) {
			logger.error("删除失败！", e);
			return ResponseResult.failure("删除失败！");
		}
	}


	@ResponseBody
	@RequestMapping("deleteByIds")
	public ResponseResult deleteByIds(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] array = request.getParameterValues("ids");
			for(String id : array)
			{
				sheetDesignConditionService.deleteById(UUID.fromString(id));
			}
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
			sheetDesignConditionService.deleteById(getUUIDParam(request, "id", UUID.randomUUID()));
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
			SheetDesignCondition entity = sheetDesignConditionService
					.getById(getUUIDParam(request, "id", UUID.randomUUID()));
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
	@RequestMapping(value = "/getByConditionId")
	public ResponseResult getByConditionId(HttpServletRequest request, HttpServletResponse response) {
		try {
			Collection<SheetDesignCondition> entity = sheetDesignConditionService
					.getByConditionId(getUUIDParam(request, "conditionId", UUID.randomUUID()));
			System.out.println("----> " + entity);
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
	@RequestMapping(value = "/getByDesignId")
	public ResponseResult getByDesignId(HttpServletRequest request, HttpServletResponse response) {
		try {
			String designId = request.getParameter("designId");
			List<SheetCondition> entitys =  sheetConditionService.getConditionByDesignId(designId);

			List<Map<String, Object>> lstmap = new ArrayList<>();
			for(SheetCondition sheetCondition:entitys)
			{
				Collection<SheetDesignCondition> sheetDesignConditions = sheetDesignConditionService.getByConditionId(sheetCondition.getId());

				if(sheetDesignConditions.size()>0)
				{
					SheetDesignCondition sheetDesignCondition =sheetDesignConditions.iterator().next();
					Map<String, Object> map = new HashMap<>();
					map.put("id",sheetDesignCondition.getId());
					map.put("name",sheetCondition.getConditionName());
					map.put("conditionId",sheetDesignCondition.getConditionId());
					lstmap.add(map);
				}
			}

			return ResponseResult.success(lstmap, lstmap.size(), "获取成功！");
		} catch (Exception e) {
			logger.error("获取失败！", e);
			return ResponseResult.failure("获取失败！");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getTreeByDesignId")
	public ResponseResult getTreeByDesignId(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Map<String, Object>> lstmap = sheetDesignConditionService
					.getTreeByDesignId(getUUIDParam(request, "designId", UUID.randomUUID()));
			return ResponseResult.success(lstmap, lstmap.size(), "获取成功！");
		} catch (Exception e) {
			logger.error("获取失败！", e);
			return ResponseResult.failure("获取失败！");
		}
	}
	
	@RequestMapping("/getByDesignIdDistinct")
	@ResponseBody
	public ResponseResult getByDesignIdDistinct(String designId){
		try {
			Collection<SheetDesignCondition> collection = sheetDesignConditionService.getByDesignIdDistinct(UUID.fromString(designId));
			return ResponseResult.success(collection, collection.size(), "根据模板id获取去重数据成功");
		} catch (Exception e) {
			logger.error("根据模板id获取去重数据失败",e);
			return ResponseResult.failure("根据模板id获取去重数据失败");
		}
	}
	
	@RequestMapping("/deleteByDesignIdAndConditionId")
	@ResponseBody
	public ResponseResult deleteByDesignIdAndConditionId(String designId,String conditionId){
		try {
			sheetDesignConditionService.deleteByDesignIdAndConditionId(UUID.fromString(designId),UUID.fromString(conditionId));
			return ResponseResult.success("删除成功");
		} catch (Exception e) {
			logger.error("删除失败",e);
			return ResponseResult.failure("删除失败");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getBingData")
	public ResponseResult getBingData(HttpServletRequest request, HttpServletResponse response,String designId,String sectionNo) {

		try {
			SheetDesignCondition data = sheetDesignConditionService.getBingData(UUID.fromString(designId),sectionNo);
			return ResponseResult.success(data, 1);
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}
	}
 
	
	@RequestMapping(value = MAPPING_PATH_FIND_BY_PAGE)
	public ResponseResult findByPage(HttpServletRequest request) {
		return null;
		
	}
	
	@RequestMapping(value = "/getAll")
	public void getBeans(HttpServletRequest request, HttpServletResponse response) {
		Collection<SheetDesignCondition> collection = sheetDesignConditionService.findAll();
		for (SheetDesignCondition sheetDesignCondition : collection) {
			System.out.println("---> " + sheetDesignCondition);
		}
	}

 
	/**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesignCondition entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignConditionService.moveUp(entity, designId);
            if (success) {
            	return ResponseResult.success(entity,1,"上移成功！");
            } else {
                return ResponseResult.failure("上移失败！");
            }
        } catch (Exception e) {
            logger.error("上移失败！", e);
            return ResponseResult.failure("上移失败！");
        }
    }
    
    /**
     * 下移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveDown")
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesignCondition entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignConditionService.moveDown(entity, designId);
            if (success) {
            	return ResponseResult.success(entity,1,"下移成功！");
            } else {
                return ResponseResult.failure("下移失败！");
            }
        } catch (Exception e) {
            logger.error("下移失败！", e);
            return ResponseResult.failure("下移失败！");
        }
    }

}
