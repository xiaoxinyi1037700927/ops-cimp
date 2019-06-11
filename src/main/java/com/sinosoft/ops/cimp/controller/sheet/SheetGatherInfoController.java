package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetGatherInfo;
import com.sinosoft.ops.cimp.service.sheet.SheetGatherInfoService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
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
import java.util.List;
import java.util.UUID;



@Controller
@RequestMapping("sheetGatherInfo")
public class SheetGatherInfoController extends BaseEntityController<SheetGatherInfo> {

	private static final Logger logger = LoggerFactory.getLogger(SheetGatherInfoController.class);
	
	@Autowired
	private SheetGatherInfoService sheetGatherInfoService;
	
	
	public ResponseResult create(SheetGatherInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult update(SheetGatherInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult delete(SheetGatherInfo entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult deleteById(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult getById(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@ResponseBody
    @RequestMapping("/multipleCreate")
    public ResponseResult multipleCreate(HttpServletRequest request, HttpServletResponse response,@RequestBody List<SheetGatherInfo> infos){
		try {
			for (SheetGatherInfo sheetGatherInfo : infos) {
				sheetGatherInfo.setId(UUID.randomUUID());
				addTrackData(sheetGatherInfo);
				sheetGatherInfoService.create(sheetGatherInfo);
			}
			return ResponseResult.success(infos,infos.size(),"批量添加汇总信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量添加汇总信息失败",e);
			return ResponseResult.failure("批量添加汇总信息失败");
		}
	}
	
	@ResponseBody
    @RequestMapping("/multipleUpdate")
    public ResponseResult multipleUpdate(HttpServletRequest request, HttpServletResponse response,@RequestBody List<SheetGatherInfo> infos){
		try {
			for (SheetGatherInfo sheetGatherInfo : infos) {
				addTrackData(sheetGatherInfo);
				sheetGatherInfoService.update(sheetGatherInfo);
			}
			return ResponseResult.success(infos,infos.size(),"批量添加汇总信息成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量添加汇总信息失败",e);
			return ResponseResult.failure("批量添加汇总信息失败");
		}
	}
	
	private void addTrackData(SheetGatherInfo entity) {
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
      
	}

}
