package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.*;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCarrierService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSectionService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Controller("sheetDesignSectionController")
@RequestMapping("sheet/designSection")
public class SheetDesignSectionController extends BaseEntityController<SheetDesignSection> {

	private static final Logger logger = LoggerFactory.getLogger(SheetDesignSectionController.class);

	@Resource
	private SheetDesignSectionService SheetDesignSectionService =null;
	
	@Autowired
	private SheetDesignCarrierService sheetDesignCarrierService;

	@ResponseBody
	
	@RequestMapping(value = MAPPING_PATH_CREATE)
	public ResponseResult create(SheetDesignSection entity) {
		try {
			UUID id = UUID.randomUUID();
			entity.setId(id);
			addTrackData(entity);
			if(!SheetDesignSectionService.checkSectionSame(entity))
			{
				return ResponseResult.failure("数据块行列位置重复！");
			}
			SheetDesignSectionService.create(entity);
			return ResponseResult.success(entity,1,"保存成功！");
		} catch (Exception e) {
			logger.error("创建失败！", e);
			return ResponseResult.failure("保存失败！");
		}

	}

	@ResponseBody
	
	@RequestMapping(value = MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignSection entity) {
		try {
			SheetDesignSectionService.update(entity);
			return ResponseResult.success(entity,1,"保存成功！");
		} catch (Exception e) {
			logger.error("更新失败！", e);
			return ResponseResult.failure("保存失败！");
		}
	}

	
	@RequestMapping(value = MAPPING_PATH_DELETE)
	@ResponseBody
	public ResponseResult delete(SheetDesignSection entity) {
		try {
			SheetDesignSectionService.delete(entity);
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
			UUID id = getUUIDParam(request, "id", UUID.randomUUID());
			SheetDesignSectionService.deleteById(id);
			return ResponseResult.success(id, 1, "删除成功！");
		} catch (Exception e) {
			logger.error("删除失败！", e);
			return ResponseResult.failure("删除失败！");
		}
	}

	@ResponseBody
	
	@RequestMapping(value = MAPPING_PATH_GET_BY_ID)
	public ResponseResult getById(HttpServletRequest request) {
		try {
			SheetDesignSection entity = SheetDesignSectionService.getById(getUUIDParam(request, "id", null));
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
			//queryParameter.getParameters().put("categoryId",this.getUUIDParam(request, "categoryId", UUID.randomUUID()));
			//queryParameter.getParameters().put("type",this.getByteParam(request, "type", (byte)-1));

			PageableQueryResult queryResult = SheetDesignSectionService.findByPage(queryParameter);
			return ResponseResult.success(queryResult.getData(), queryResult.getTotalCount());
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/getByDesignId")
	public ResponseResult getByDesignId(HttpServletRequest request, HttpServletResponse response,String designId) {

		try {
			Collection<SheetDesignSection> collections = SheetDesignSectionService.getByDesignId(UUID.fromString(designId));
			if (collections != null) {
				return ResponseResult.success(collections, collections.size());
			} else {
				return ResponseResult.failure("查询数据失败！");
			}
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}

	}
	
	/**
	 * 移动数据块
	 */
	@RequestMapping("/moveSection")
	@ResponseBody
	public ResponseResult moveSection(String id,String toId){
		try {
			SheetDesignSectionService.moveSection(id,toId);
			return ResponseResult.success("移动成功");
		} catch (Exception e) {
			logger.error("移动数据块失败",e);
			return ResponseResult.failure("移动数据块成功");
			
		}
	}
	
	/**
	 * 根据designId获取数据块树
	 */
	@RequestMapping("/getSectionTreeByDesignId")
	@ResponseBody
	public ResponseResult getSectionTreeByDesignId(HttpServletRequest request, HttpServletResponse response,String designId){
		try {
			Collection<SheetDesignSection> tree = SheetDesignSectionService.getSectionTreeByDesignId(UUID.fromString(designId));
			return ResponseResult.success(tree, tree.size(), "根据designId获取数据块树成功");
		} catch (Exception e) {
			logger.error("根据designId获取数据块树失败",e);
			return ResponseResult.failure("根据designId获取数据块树失败");
		}
	}

	@RequestMapping("/exportWordBySection")
	public void exportWordBySection(HttpServletRequest request, HttpServletResponse response,String designId){
		try {
			Collection<SheetDesignSection> collection = SheetDesignSectionService.getByDesignId(UUID.fromString(designId),"ordinal",false);
			//TODO 预留数据绑定接口
			SheetDesignSectionService.dataBinding(collection);
			SheetDesignCarrier designCarrier = sheetDesignCarrierService.getByDesignId(UUID.fromString(designId));
			//SheetDesignSectionService.fillData2Templement(collection,designCarrier.getContent(),response);
			writeJson(response, ResponseResult.success("下载成功！"));
		} catch (Exception e) {
			logger.error("导出word失败",e);
			writeJson(response, ResponseResult.success("下载失败！"));
		}
		
	}
	
	/**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesignSection entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = SheetDesignSectionService.moveUp(entity, designId);
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
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesignSection entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = SheetDesignSectionService.moveDown(entity, designId);
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

	
	private void addTrackData(SheetDesignSection entity) {
		// 创建人
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
        }
        // 创建时间
        if (entity.getCreatedTime() == null) {
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        }
        // 最后修改人
        entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
        // 最后修改时间
        entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));

        // 数据状态
        if (entity.getStatus() == null) {
            entity.setStatus(DataStatus.NORMAL.getValue());
        }
        
        //次序
        if(entity.getOrdinal() == null){
        	Integer ordinal = SheetDesignSectionService.getMaxOrdinal()==null?1:SheetDesignSectionService.getMaxOrdinal();
        	entity.setOrdinal(ordinal+1);
        }
        
        //编号
        if(entity.getSectionNo()==null){
        	String max = SheetDesignSectionService.getMaxSectionNoByDesignId(entity.getDesignId());
        	int i = Integer.parseInt(max)+1;
        	entity.setSectionNo(i+"");
        }
	}

}
