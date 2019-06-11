package com.sinosoft.ops.cimp.controller.sheet;


import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.entity.sheet.SheetData;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;
import com.sinosoft.ops.cimp.service.sheet.SheetComService;
import com.sinosoft.ops.cimp.service.sheet.SheetDataService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSectionService;
import com.sinosoft.ops.cimp.service.sheet.SheetService;
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
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * 
 * @ClassName:  SheetDataController
 * @description: 表格数据控制器
 * @author:        sunch
 * @date:            2018年7月4日 下午7:18:40
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller
@RequestMapping("/sheet/sheetData")
public class SheetDataController extends BaseEntityController<SheetData> {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetDataController.class);
	
	@Autowired
	private SheetDataService sheetDataService;
	@Autowired
	private SheetService sheetService;
	@Autowired
	private SheetComService sheetComService;
	@Autowired
	private SheetDesignSectionService sheetDesignSectionService;

	
	public ResponseResult create(SheetData entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult update(SheetData entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult delete(SheetData entity) {
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
    public ResponseResult multipleCreate(HttpServletRequest request, HttpServletResponse response,@RequestBody List<SheetData> datas){
		try {
			long i = System.currentTimeMillis();
			for (SheetData data : datas) {
				data.setId(UUID.randomUUID());
				addTrackData(data);
				sheetDataService.create(data);
			}
			long j = System.currentTimeMillis();
			System.out.println("批量添加耗时--"+(j-i));
			return ResponseResult.success(datas, datas.size(), "多条添加表格数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("多条添加表格数据失败",e);
			return ResponseResult.failure("多条添加表格数据失败");
		}
    	
    }
	
	@RequestMapping("/getBySheetId")
	@ResponseBody
	public ResponseResult getBySheetId(String sheetId){
		try {
			Collection<SheetData> collection = sheetDataService.getBySheetId(sheetId);
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			Collection<SheetDesignSection> sections = sheetDesignSectionService.getByDesignId(sheet.getDesignId());
			for (SheetData sheetData : collection) {
				SheetDesignSection section = sheetComService.getUniqueSection(sections, sheetData.getRowNo(), sheetData.getColumnNo());
				if(section != null){
					int betweenRow = section.getCtrlRowStart()-section.getStartRowNo()-1;
					int betweenColumn = section.getCtrlColumnStart()-section.getStartColumnNo()-1;
					
					sheetData.setCtrlRowNo(sheetData.getRowNo()+betweenRow);
					sheetData.setCtrlColumnNo(sheetData.getColumnNo()+betweenColumn);
				}
				
			}
			return ResponseResult.success(collection, collection.size(), "根据报表id获取报表数据成功");
		} catch(IllegalArgumentException e){
			logger.error(e.getMessage(),e);
			return ResponseResult.failure(e.getMessage());
		}catch (Exception e) {
			logger.error("根据报表id获取报表数据失败",e);
			return ResponseResult.failure("根据报表id获取报表数据失败");
		}
	}
	
	private void addTrackData(SheetData entity) {
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
		if (entity.getOrdinal() == null) {
			entity.setOrdinal(sheetDataService.getNextOrdinal());
		}
      
	}
}
