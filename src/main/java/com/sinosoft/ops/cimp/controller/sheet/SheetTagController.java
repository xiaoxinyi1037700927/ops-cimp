package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.Sheet;
import com.sinosoft.ops.cimp.entity.sheet.SheetTag;
import com.sinosoft.ops.cimp.service.sheet.SheetComService;
import com.sinosoft.ops.cimp.service.sheet.SheetService;
import com.sinosoft.ops.cimp.service.sheet.SheetTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;



@Controller
@RequestMapping("sheet/sheetTag")
public class SheetTagController extends BaseEntityController<SheetTag> {

	private static final Logger logger = LoggerFactory.getLogger(SheetTagController.class);
	
	@Autowired
	private SheetTagService sheetTagService;
	@Autowired
	private SheetComService sheetComService;
	@Autowired
	private SheetService sheetService;
	
	@RequestMapping("/getBySheetId")
	@ResponseBody
	public ResponseResult getBySheetId(HttpServletRequest request, HttpServletResponse response, String sheetId){
		try {
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			Collection<SheetTag> collection = sheetTagService.getBySheetId(UUID.fromString(sheetId));
			for (SheetTag sheetTag : collection) {
				Map<String, Integer> position = sheetComService.getCtrlPositionByRelativePosition(sheet.getDesignId(), sheetTag.getRowNo(), sheetTag.getColumnNo());
				sheetTag.setCtrlRowNo(position.get("rowNo"));
				sheetTag.setCtrlColumnNo(position.get("columnNo"));
			}
			return ResponseResult.success(collection, collection.size(), "根据报表id获取批注成功");
		} catch (Exception e) {
			logger.error("根据报表id获取批注失败",e);
			return ResponseResult.failure("获取批注失败");
		}
	}
	
	@Override
	public ResponseResult create(SheetTag entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult update(SheetTag entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseResult delete(SheetTag entity) {
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

}
