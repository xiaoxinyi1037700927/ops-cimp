package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSql;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSqlService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
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
 * 
 * @ClassName:  SheetDesignSqlController
 * @description: 表格设计SQL 控制器
 * @author:        sunch
 * @date:            2018年6月6日 下午1:55:16
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller
@RequestMapping("sheetDesignSqlController")
public class SheetDesignSqlController extends BaseEntityController<SheetDesignSql> {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignSqlController.class);
	
	@Autowired
	private SheetDesignSqlService sheetDesignSqlService;

	
	@RequestMapping(value=MAPPING_PATH_CREATE)
	@ResponseBody
	public ResponseResult create(SheetDesignSql entity) {
		try {
			addTrackData(entity);
			sheetDesignSqlService.create(entity);
			return ResponseResult.success(entity, 1, "创建成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建失败",e);
			return ResponseResult.failure("创建失败");
		}
	}

	
	@RequestMapping(value=MAPPING_PATH_UPDATE)
	@ResponseBody
	public ResponseResult update(SheetDesignSql entity) {
		try {
			addTrackData(entity);
			sheetDesignSqlService.update(entity);
			return ResponseResult.success(entity, 1, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新失败",e);
			return ResponseResult.failure("更新失败");
		}
	}

	
	@RequestMapping(value=MAPPING_PATH_DELETE)
	@ResponseBody
	public ResponseResult delete(SheetDesignSql entity) {
		try {
			sheetDesignSqlService.delete(entity);
			return ResponseResult.success(entity, 1, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败",e);
			return ResponseResult.failure("删除失败");
		}
	}

	
	@RequestMapping(value=MAPPING_PATH_DELETE_BY_ID)
	@ResponseBody
	public ResponseResult deleteById(HttpServletRequest request) {
		try {
			sheetDesignSqlService.deleteById(UUID.fromString(request.getParameter("id")));
			return ResponseResult.success("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除失败",e);
			return ResponseResult.failure("删除失败");
		}
	}

	
	@RequestMapping(value=MAPPING_PATH_GET_BY_ID)
	@ResponseBody
	public ResponseResult getById(HttpServletRequest request) {
		try {
			SheetDesignSql entity = sheetDesignSqlService.getById(UUID.fromString(request.getParameter("id")));
			return ResponseResult.success(entity, 1, "获取成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取失败",e);
			return ResponseResult.failure("获取失败");
		}
	}

	
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO 根据业务后续完善
		return null;
	}

	private void addTrackData(SheetDesignSql entity) {
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
        	Integer ordinal = sheetDesignSqlService.getMaxOrdinal()==null?1:sheetDesignSqlService.getMaxOrdinal();
        	entity.setOrdinal(ordinal+1);
        }
	}
}
