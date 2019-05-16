package com.sinosoft.ops.cimp.controller.archive;


import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.common.BaseResultHttpStatus;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialCategoryService;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.StringUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 档案分类控制器
 */
@Controller("archiveMaterialCategoryController")
@RequestMapping("/materialCategory")
public class ArchiveMaterialCategoryController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ArchiveMaterialCategoryController.class);
	@Resource
	private ArchiveMaterialCategoryService archiveMaterialCategoryService;
	@Resource
	private ArchiveMaterialService archiveMaterialService;
	
	/**
	 * 档案分类 和 档案材料 树结构
	 * ArchiveMaterialCategory 和 ArchiveMaterial树结构
	 * @param request->id  档案分类的code
	 * @param request->empId 人员ID
	 * @param request->categoryId 档案分类ID
	 * @author zhaizf
	 * @date 2017年12月26日
	 */
	@RequestMapping("/getTree")
	public void getMaterialCategoryAndMaterial4Tree(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			String code=request.getParameter("id");//id档案分类的code
			String empId=request.getParameter("empId");
			String userid="";
			
			//this.getCurrentUser().getEMP_ID里的数据不对 所以获取userid 到sys_user中重新获取emp_id
			if(SecurityUtils.getSubject().getCurrentUser().getId().toString().contains("-")){
				userid=SecurityUtils.getSubject().getCurrentUser().getId().toString().replace("-", "");
			}else{
				userid=SecurityUtils.getSubject().getCurrentUser().getId().toString();
			}
			System.out.println("useriduseriduserid==="+userid);
			//判断是不是档案管理员
			//boolean flag=archiveMaterialCategoryService.testglYorN(userid.toUpperCase());
			
		//	String user_empid=archiveMaterialCategoryService.findEmpidByUserID(userid.toUpperCase());
			String categoryId = request.getParameter("archiveMaterialCategoryId");//档案分类ID
			List<HashMap<String,Object>> materialCategoryAndMaterialList=
					archiveMaterialCategoryService.getMaterialCategoryAndMaterial4Tree(code,empId,categoryId);
			        HashMap<String, Object> resultMap = new HashMap<String,Object>();
			      //  List<HashMap<String,String>> materialCategorytexts=archiveMaterialCategoryService.findbyempower(empId,this.getCurrentUser().getId().toString());    


				if(materialCategoryAndMaterialList.size()>0){
					if(StringUtil.isEmptyOrNull(code)){
						resultMap.put("empId", empId);
						resultMap.put("text","档案");
						resultMap.put("code","VirtualRoot");
						resultMap.put("pptr","VirtualRoot");
						resultMap.put("expanded", true);
						resultMap.put("tip", "档案");
						resultMap.put("children",materialCategoryAndMaterialList);
						resultMap.put("root", resultMap);


						writeJson(response, ok(resultMap));
					}else{
						writeJson(response, ok(materialCategoryAndMaterialList));
					}
				}
			
				
				
			//}
			
			
		} catch (Exception e) {
            logger.error("查询失败！", e);
			writeJson(response, fail("查询失败", BaseResultHttpStatus.FAIL));
		}
	}
	@RequestMapping("/testrelated")
	public void testrelated(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		String userid="";
		String empId=request.getParameter("empId");
//		if(this.getCurrentUser().getName().equals("sa")){
//			writeJson(response, ResponseResult.success(0,"查询成功",null));
//		}else{
//			
//		}
		//this.getCurrentUser().getEMP_ID里的数据不对 所以获取userid 到sys_user中重新获取emp_id
		if(SecurityUtils.getSubject().getCurrentUser().getId().toString().contains("-")){
			userid=SecurityUtils.getSubject().getCurrentUser().getId().toString().replace("-", "");
		}else{
			userid=SecurityUtils.getSubject().getCurrentUser().getId().toString();
		}
		
		String user_empid=archiveMaterialCategoryService.findEmpidByUserID(userid.toUpperCase());
		List<String> idcards=null;
		//获取登录人员家庭成员身份证
		if(user_empid!=null){
			idcards=archiveMaterialCategoryService.getIdCardsByEmpid(user_empid);
		}
	
	    //登录人查看的人员的idcard
		String idcard="";
		if(empId!=null&&empId!=""){
			idcard=archiveMaterialCategoryService.getidcardforone(empId);
		}
		System.out.println("useriduseriduserid==="+userid);
		
		if(user_empid==null){
		String ms="系统提示:请在<用户管理>中为此账户关联人员!";
		writeJson(response, fail(ms, BaseResultHttpStatus.FAIL));
		}
		else if(user_empid!=null&&(empId.toUpperCase().contains("-")?empId.toUpperCase().replace("-", ""):empId.toUpperCase()).equals(user_empid.toUpperCase())){
			String ms="系统提示:不能查询本人档案!";
			writeJson(response, fail(ms, BaseResultHttpStatus.FAIL));
		}else if(idcards!=null&&idcard!=null&&idcard!=""&&idcards.contains(idcard)){
			String ms="系统提示:不允许访问本人家庭成员及社会关系档案!";
			writeJson(response, fail(ms, BaseResultHttpStatus.FAIL));
		}else{
			writeJson(response, ok("查询成功"));
		}
		writeJson(response, ok("查询成功"));
	}


}
