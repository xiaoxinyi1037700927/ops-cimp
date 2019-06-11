package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.ResponseResult;
import com.sinosoft.ops.cimp.controller.BaseEntityController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCondition;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDataSource;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignField;
import com.sinosoft.ops.cimp.repository.sheet.impl.SheetDesignBind;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignBindService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignConditionService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignDataSourceService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignFieldService;
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
import java.util.*;

/**
 * 
 * @ClassName:  SheetDesignBindController
 * @description: 表数据绑定控制器
 * @author:        kanglin
 * @date:            2018年6月12日 下午2:58:56
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("SheetDesignBindController")
@RequestMapping("sheet/sheetDesignBind")
public class SheetDesignBindController extends BaseEntityController<SheetDesignBind> {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignBindController.class);
	
    @Autowired
    private SheetDesignBindService sheetDesignBindService;
	@Autowired
	private SheetDesignDataSourceService sheetDesignDataSourceService;
	@Autowired
	private SheetDesignConditionService sheetDesignConditionService;
	@Autowired
	private SheetDesignFieldService sheetDesignFieldService;
	
    @ResponseBody
    @RequestMapping("/update")
	public ResponseResult upate(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SheetDesignBind> sheetDesignBinds) {
    	try{
    		String userId = UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString().replace("-", "").toUpperCase();
    		int cnt = 0;
    		for(SheetDesignBind sheetDesignBind :sheetDesignBinds) {
				cnt+=sheetDesignBindService.updateRowCol(sheetDesignBind, userId);
			}
    		if (cnt > 0) { 
    			return ResponseResult.success(null, cnt, "数据绑定成功！");
    		} else {
                return ResponseResult.failure("表数据绑定失败.");
    		}
        } catch(Exception e) {
        	e.printStackTrace();
            logger.error("表数据绑定失败!", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure("表数据绑定失败.");
        }
	}
    
    @ResponseBody
    @RequestMapping("/delete")
	public ResponseResult delete(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SheetDesignBind> sheetDesignBinds) {
    	try{
    		String userId = UUID.fromString(SecurityUtils.getSubject().getCurrentUser().getId()).toString().replace("-", "").toUpperCase();
    		int cnt = 0;
    		for(SheetDesignBind sheetDesignBind :sheetDesignBinds) {
				cnt+=sheetDesignBindService.deleteRowCol(sheetDesignBind, userId);
			}
    		if (cnt > 0) { 
    			return ResponseResult.success(null, cnt, "数据解绑成功！");
    		} else {
                return ResponseResult.failure("表数据解绑失败.");
    		}
        } catch(Exception e) {
        	e.printStackTrace();
            logger.error("表数据绑定失败!", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure("表数据绑定失败.");
        }
	}

    @ResponseBody
    @RequestMapping("/query")
	public ResponseResult query(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SheetDesignBind> sheetDesignBinds) {
    	try{
    		List<Map<String, Object>> list = null;
    		for(SheetDesignBind sheetDesignBind :sheetDesignBinds) {
    			list = sheetDesignBindService.queryExcel(sheetDesignBind);
			}
    		return ResponseResult.success(list, list.size(), "数据查询成功！");
        } catch(Exception e) {
        	e.printStackTrace();
            logger.error("表数据查询失败!", Throwables.getStackTraceAsString(e)); 
            return ResponseResult.failure("表数据查询失败.");
        }
	}

    @ResponseBody
	@RequestMapping(value = "/getBingData")
	public ResponseResult getBingData(HttpServletRequest request, HttpServletResponse response,String designId,String sectionNo) {

		try {
			SheetDesignCondition sheetDesignCondition = sheetDesignConditionService.getBingData(UUID.fromString(designId),sectionNo);
			SheetDesignField sheetDesignField = sheetDesignFieldService.getBingData(UUID.fromString(designId),sectionNo);
			SheetDesignDataSource sheetDesignDataSource = sheetDesignDataSourceService.getBingData(UUID.fromString(designId),sectionNo);
            List<Map> lst= new ArrayList<Map>();

            if(sheetDesignField!=null)
			{
				Map mapField= new HashMap();
				mapField.put("type","2");
				mapField.put("id",sheetDesignField.getId().toString());
				lst.add(mapField);
			}

			if(sheetDesignDataSource!=null) {
				Map mapDataSource = new HashMap();
				mapDataSource.put("type", "3");
				mapDataSource.put("id", sheetDesignDataSource.getId().toString());
				lst.add(mapDataSource);
			}

			if(sheetDesignCondition!=null) {
				Map mapCondition = new HashMap();
				mapCondition.put("type", "4");
				mapCondition.put("id", sheetDesignCondition.getId().toString());
				lst.add(mapCondition);
			}

			return ResponseResult.success(lst, lst.size());
		} catch (Exception e) {
			logger.error("查询数据失败！", e);
			return ResponseResult.failure("查询数据失败！");
		}
	}

	
	public ResponseResult create(SheetDesignBind entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult update(SheetDesignBind entity) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public ResponseResult delete(SheetDesignBind entity) {
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

}
