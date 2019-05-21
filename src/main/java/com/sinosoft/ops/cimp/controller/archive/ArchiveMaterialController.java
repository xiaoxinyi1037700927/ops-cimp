package com.sinosoft.ops.cimp.controller.archive;


import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ArchiveMaterial控制器
 */
@Api("ArchiveMaterial控制器")
@Controller("archiveMaterialController")
@RequestMapping("/archiveMaterial")
public class ArchiveMaterialController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ArchiveMaterialController.class);
	@Resource 
	private ArchiveMaterialService archiveMaterialService;

	
	/**
	 * 根据人员ID +档案分类ID  获取ArchiveMaterial集合
	 * @param request->empId 人员ID;  categoryId分类ID
	 * @return 指定人员 特定分类 ArchiveMaterial集合
	 */
	@ApiOperation("根据人员ID +档案分类ID  获取ArchiveMaterial集合")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "empId", value = "empId", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "categoryId", value = "categoryId", dataType = "String", required = true, paramType = "query")
	})
	@RequestMapping(value = "/getAMTLbyEmpIDAndCategoryId",method = RequestMethod.GET)
	public void getArchiveMaterialListByEmpIdAndCategoryId(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String empId = request.getParameter("empId");
			String categoryId = request.getParameter("categoryId");
			List<ArchiveMaterial> archiveMaterialList = archiveMaterialService.getArchiveListByEmpIdAndCaterogyId(empId,categoryId);
			if(archiveMaterialList!=null&&archiveMaterialList.size()>0){
				writeJson(response,ok(archiveMaterialList));
			}else{
				writeJson(response, fail("不存在empId="+empId+"&categoryId="+categoryId+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	
	/**
	 * 根据人员ID查询  获取ArchiveMaterial集合
	 * @param request->empId 指定人员ID
	 * @return 指定人员ID的 ArchiveMaterial 集合
	 */
	@ApiOperation("根据人员ID查询  获取ArchiveMaterial集合")
	@ApiImplicitParam(name = "empId",
					value = "empId",
					dataType = "String",
					required = true,
					paramType = "query")
	@RequestMapping(value = "/getArchiveMateriaListbyEmpID",method = RequestMethod.GET)
	public void getArchiveMaterialListByEmpId(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String empId = request.getParameter("empId");
			List<ArchiveMaterial>  archiveMateriaList = archiveMaterialService.getArchiveListByEmpId(empId);
			if(archiveMateriaList!=null&&archiveMateriaList.size()!=0){
				writeJson(response,ok(archiveMateriaList));
			}else{
				writeJson(response, fail("不存在empId="+empId+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	
	
	/**
	 * 根据档案ID查询 获取ArchiveMaterial
	 * @param  request->materialID   档案ID
	 * @return 指定ID ArchiveMaterial
	 */
	@ApiOperation("根据档案ID查询 获取ArchiveMaterial")
	@ApiImplicitParam(name = "materialID",
			value = "materialID",
			dataType = "String",
			required = true,
			paramType = "query")
	@RequestMapping(value = "/getArchiveMaterialByID",method = RequestMethod.GET)
	public void getArchiveMaterialByID(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String archiveMaterialID = request.getParameter( "materialID");
			ArchiveMaterial  archiveMaterial = archiveMaterialService.getById(archiveMaterialID);
			if(archiveMaterial!=null){
				writeJson(response,ok(archiveMaterial));
			}else{
				writeJson(response, fail("不存在empId="+archiveMaterialID+"的记录"));
			}
		} catch (Exception e) {
			logger.error("查询失败！", e);
			writeJson(response, fail("查询失败"));
		}
	}

	
	
}
