package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.PageableQueryParameter;
import com.sinosoft.ops.cimp.common.model.PageableQueryResult;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDataSource;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.SheetDataSourceService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignDataSourceService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignFieldService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

@Controller
@RequestMapping("sheet/sheetDesignField")
public class SheetDesignFieldController extends BaseEntityController<SheetDesignField> {

	private static final Logger logger = LoggerFactory.getLogger(SheetDesignFieldController.class);
	
	@Autowired
	private SheetDesignFieldService sheetDesignFieldService;
	@Autowired
	private SheetDesignDataSourceService sheetDesignDataSourceService;
	@Autowired
	private SheetDataSourceService sheetDataSourceService;
	@Autowired
	private SysInfoSetService sysInfoSetService;
	
	@ResponseBody
	@RequestMapping(value=MAPPING_PATH_CREATE)
	public ResponseResult create(SheetDesignField entity, HttpServletRequest request) {
		try {
			entity.setId(UUID.randomUUID());
			entity.setJsonData(request.getParameter("dataItems"));
			addTrackData(entity);
			sheetDesignFieldService.setCountData(entity);
			sheetDesignFieldService.create(entity);
			return ResponseResult.success(entity, 1, "保存成功！");
		} catch (Exception e) {
			logger.error("保存失败",e);
			return ResponseResult.failure("保存失败");
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value=MAPPING_PATH_UPDATE)
	public ResponseResult update(SheetDesignField entity,HttpServletRequest request) {
		try {
			addTrackData(entity);
			entity.setJsonData(request.getParameter("dataItems"));
			sheetDesignFieldService.setCountData(entity);
			sheetDesignFieldService.update(entity);
			return ResponseResult.success(entity, 1,"更新成功！");
		} catch (Exception e) {
			logger.error("更新失败",e);
			return ResponseResult.failure("更新失败");
		}
	}

	
	@ResponseBody
	@RequestMapping(value=MAPPING_PATH_DELETE)
	public ResponseResult delete(SheetDesignField entity) {
		try {
			sheetDesignFieldService.delete(entity);
			return ResponseResult.success(entity, 1, "删除成功！");
		} catch (Exception e) {
			logger.error("删除失败",e);
			return ResponseResult.failure("删除失败");
		}
	}

	
	@ResponseBody
	@RequestMapping(value=MAPPING_PATH_DELETE_BY_ID)
	public ResponseResult deleteById(HttpServletRequest request) {
		try {
			sheetDesignFieldService.deleteById(UUID.fromString(request.getParameter("id")));
			return ResponseResult.success("删除成功！");
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure("删除失败");
		}
	}

	
	@ResponseBody
	@RequestMapping(value=MAPPING_PATH_GET_BY_ID)
	public ResponseResult getById(HttpServletRequest request) {
		try {
			SheetDesignField entity = sheetDesignFieldService.getById(UUID.fromString(request.getParameter("id")));
			if(entity.getDataSourceId()!=null)
			{
				SheetDesignDataSource sheetDesignDataSource = sheetDesignDataSourceService.getById(entity.getDataSourceId());
				SheetDataSource sheetDataSource  = sheetDataSourceService.getById(sheetDesignDataSource.getDatasourceId());
				entity.setDataSourceName(sheetDataSource.getName());
			}
			else
			{
				SysInfoSet sysInfoSet= sysInfoSetService.getByTableName(entity.getReferenceTable());
				if(sysInfoSet!=null)
					entity.setDataSourceName(sysInfoSet.getNameCn());
			}

			if(entity != null){
				return ResponseResult.success(entity, 1, "获取成功！");
			}else{
				return ResponseResult.failure("获取失败");
			}
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure("获取失败");
		}
	}

	
	@ResponseBody
	@RequestMapping(value=MAPPING_PATH_FIND_BY_PAGE)
	public ResponseResult findByPage(HttpServletRequest request) {
		try {
			PageableQueryParameter queryParameter = new PageableQueryParameter();//TODO 后续添加完善参数
			PageableQueryResult result = sheetDesignFieldService.findByPage(queryParameter);
			return ResponseResult.success(result.getData(),result.getTotalCount());
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure("获取数据失败");
		}
		
	}
	
	@RequestMapping("/getDataSource")
	@ResponseBody
	public ResponseResult getDataSource(HttpServletRequest request,HttpServletResponse response){
		try {
			
			return ResponseResult.success("EMP_A001", 1, "获取成功");
		} catch (Exception e) {
			logger.error("获取失败",e);
			return ResponseResult.failure("获取失败");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getByDesignId")
	public ResponseResult getByDesignId(HttpServletRequest request, HttpServletResponse response,String designId) {

		try {
			Collection<SheetDesignField> collections = sheetDesignFieldService.getByDesignId(UUID.fromString(designId));
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


	@ResponseBody
	@RequestMapping(value = "/getBingData")
	public ResponseResult getBingData(HttpServletRequest request, HttpServletResponse response,String designId,String sectionNo) {

		try {
			SheetDesignField data = sheetDesignFieldService.getBingData(UUID.fromString(designId),sectionNo);
			return ResponseResult.success(data, 1);
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}
	}
	
	/**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesignField entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignFieldService.moveUp(entity, designId);
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
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesignField entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()));
            boolean success = sheetDesignFieldService.moveDown(entity, designId);
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
	
	private void addTrackData(SheetDesignField entity) {
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
        
        //次序号
        if (entity.getOrdinal()==null){
        	Integer ordinal = sheetDesignFieldService.getMaxOrdinal()==null?0:sheetDesignFieldService.getMaxOrdinal();
        	entity.setOrdinal(ordinal+1);
        }
        
        if(entity.getEndColumnNo()==null){
        	entity.setEndColumnNo(0);
        }
        if(entity.getEndRowNo()==null){
        	entity.setEndRowNo(0);
        }
        if(entity.getStartColumnNo()==null){
        	entity.setStartColumnNo(0);
        }
		if(entity.getStartRowNo()==null){
			entity.setStartRowNo(0);
		}
	}

}
