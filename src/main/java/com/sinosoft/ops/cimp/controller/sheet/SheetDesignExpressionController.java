package com.sinosoft.ops.cimp.controller.sheet;

import com.google.common.base.Throwables;
import com.newskysoft.iimp.common.DataStatus;
import com.newskysoft.iimp.common.ResponseResult;
import com.newskysoft.iimp.common.TreeNode;
import com.newskysoft.iimp.common.controller.BaseEntityController;
import com.newskysoft.iimp.common.model.DefaultTreeNode;
import com.newskysoft.iimp.sheet.model.SheetDesignExpression;
import com.newskysoft.iimp.sheet.model.SheetDesignParameter;
import com.newskysoft.iimp.sheet.service.SheetDesignExpressionService;
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
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignExpressionController
 * @description: 表格设计表达式控制层
 * @author:        lixianfu
 * @date:            2018年6月11日 下午2:17:13
 * @version        1.0.0
 * @since            JDK 1.7
 */
@Controller("SheetDesignExpressionController")
@RequestMapping("sheet/sheetDesignExpression")
public class SheetDesignExpressionController extends BaseEntityController<SheetDesignExpression>{
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignExpressionController.class);
	@Autowired
	private SheetDesignExpressionService sheetDesignExpressionService;

	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_CREATE)//SpringMVC这个类的前段请求路径
	public ResponseResult create(SheetDesignExpression entity) {
		try {
			addTrackData(entity);
			entity.setOrdinal(sheetDesignExpressionService.getNextOrdinal());
			sheetDesignExpressionService.saveDesignExpression(entity);
			return ResponseResult.success(entity, 1,"保存成功！");
		} catch (Exception e) {
			logger.error("sheetDesignExpressionController create error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "保存失败！");
		}

	}

	@RequestMapping("/getDesignExpressionTreeAll")
	@ResponseBody
	public ResponseResult getDesignExpressionAll(HttpServletRequest request,
												 HttpServletResponse response){

		try {
			String type = request.getParameter("type");
			UUID designId = UUID.fromString(request.getParameter("designId"));

			Collection<SheetDesignExpression> List = sheetDesignExpressionService.getByDesignId(designId);

			Collection<TreeNode> tree  = sheetDesignExpressionService.getByType(List, type);

			return ResponseResult.success(tree, tree.size(),"构建树形结构成功");
		} catch (Exception e) {
			logger.error("构建树形失败",e);
			return ResponseResult.failure("构建树形失败");
		}
	}

	@RequestMapping("/getChild")
	@ResponseBody
	public ResponseResult getChild(HttpServletRequest request,
								   HttpServletResponse response){

		try {

			UUID designId = UUID.fromString(request.getParameter("designId"));

			Collection<SheetDesignExpression> List = sheetDesignExpressionService.getByDesignId(designId);

			List<DefaultTreeNode> treeList = sheetDesignExpressionService.conversionTree(List);

			return ResponseResult.success(treeList, treeList.size(),"获取子节点成功");
		} catch (Exception e) {
			logger.error("获取子节点失败",e);
			return ResponseResult.failure("获取子节点失败");
		}
	}


	@RequestMapping("/getByDesignId")
	@ResponseBody
	public ResponseResult getByDesignId(HttpServletRequest request,
								   HttpServletResponse response){
		try {
			UUID designId = UUID.fromString(request.getParameter("designId"));
			Collection<SheetDesignExpression> SheetDesignExpressions = sheetDesignExpressionService.getByDesignId(designId);
			return ResponseResult.success(SheetDesignExpressions, SheetDesignExpressions.size(),"获取表达式成功");
		} catch (Exception e) {
			logger.error("获取表达式失败",e);
			return ResponseResult.failure("获取表达式失败");
		}
	}
	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_GET_BY_ID)
	@Override
	public ResponseResult getById(HttpServletRequest request) {
		try{
			SheetDesignExpression entity = sheetDesignExpressionService.getById(toUUID(request.getParameter("id")));
			return ResponseResult.success(entity,1,"执行成功!");
		}catch(Exception e){
			logger.error("sheetDesignExpressionController getById error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "执行失败！");
		}
	}

	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_UPDATE)
	@Override
	public ResponseResult update(SheetDesignExpression entity) {
		try {
			SheetDesignExpression expression = sheetDesignExpressionService.getById(entity.getId());
			addTrackData(entity);
			entity.setOrdinal(expression.getOrdinal());
			sheetDesignExpressionService.update(entity);
			return ResponseResult.success(entity, 1, "修改成功！");
		} catch (Exception e) {
			logger.error("sheetDesignExpressionController update error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "修改失败！");
		}

	}


	@ResponseBody
	@RequestMapping(value = MAPPING_PATH_DELETE_BY_ID)
	@Override
	public ResponseResult deleteById(HttpServletRequest request) {
		try{
			sheetDesignExpressionService.deleteById(toUUID(request.getParameter("id")));
			return ResponseResult.success(null,1, "删除成功!");
		}catch(Exception e){
			logger.error("sheetDesignExpressionController deleteOne error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "删除失败！");
		}
	}
	
	
	/**
     * 上移节点
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/moveUp")
    public ResponseResult moveUp(HttpServletRequest request, HttpServletResponse response, SheetDesignExpression entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
            boolean success = sheetDesignExpressionService.moveUp(entity, designId);
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
    public ResponseResult moveDown(HttpServletRequest request, HttpServletResponse response, SheetDesignExpression entity) {
        try{
        	UUID designId = UUID.fromString(request.getParameter("designId"));
        	entity.setLastModifiedBy(getCurrentLoggedInUser().getId());
            boolean success = sheetDesignExpressionService.moveDown(entity, designId);
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
	
	private UUID toUUID(String id) {
		UUID uuid = UUID.fromString(id);
		return uuid;
	}
	private void addTrackData(SheetDesignExpression entity) {
		if(entity.getId() == null) {
			entity.setId(UUID.randomUUID());
		}
		if (entity.getStatus() == null) {
			entity.setStatus(DataStatus.NORMAL.getValue());
		}
		if (entity.getType() == null) {
			entity.setType(DataStatus.NORMAL.getValue());
		}
		if(entity.getCreatedTime() == null){
			entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));;
		}
		if(entity.getCreatedBy() == null){
			entity.setCreatedBy(getCurrentLoggedInUser().getId());
		}
		if(entity.getLastModifiedTime() == null){
			entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
		}
		if(entity.getLastModifiedBy() == null){
			entity.setLastModifiedBy(UUID.randomUUID());
		}
		

	}


	@Override
	public ResponseResult findByPage(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResponseResult delete(SheetDesignExpression entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
