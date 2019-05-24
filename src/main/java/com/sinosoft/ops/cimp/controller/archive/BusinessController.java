package com.sinosoft.ops.cimp.controller.archive;


import com.sinosoft.ops.cimp.annotation.ArchiveApiGroup;
import com.sinosoft.ops.cimp.constant.UserRoleConstants;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.archive.BusinessService;
import com.sinosoft.ops.cimp.service.archive.bean.bean.PersonAndPost;
import com.sinosoft.ops.cimp.service.user.UserRoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ArchiveApiGroup
@Api(description="档案业务")
@Controller("businessController")
@RequestMapping("/business")
public class BusinessController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UserRoleService userRoleService;

	@ApiOperation("根据depid查申请人信息和岗位")
	@ApiImplicitParam(name = "depid",value = "depid", dataType = "String", required = true, paramType = "query")
	@RequestMapping(value = "/getPersonAndPostByDepid",method = RequestMethod.POST)
	public ResponseEntity getPersonAndPostByDepid(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();
		String Depid=request.getParameter("depid");
		List<PersonAndPost>	personAndPost = businessService.getPersonAndPostByDepid(Depid);
		return ok(personAndPost);
	}

	/*@ApiOperation("")
	@ApiImplicitParam(name = "empid",value = "所要查看人的id", dataType = "String", required = true, paramType = "query")
	@RequestMapping(value = "/getA02ByDepid",method = RequestMethod.POST)
	public ResponseEntity getA02ByDepid(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String Depid=request.getParameter("depId");
		List<Map<String, Object>>	A02ByDep = businessService.getA02ByDepid(Depid);
		return ok(A02ByDep);
	}*/
	
/*	@RequestMapping("/updateA02Data")
	public void updateA02Data(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try
		{
			List<Object> oblist = JSONObject.parseArray(request.getParameter("json"));
			int count = businessService.updateA02Data(oblist);
			writeJson(response, ok(count, DateTime.now().toString("yyyy-MM-dd HH:mm:ss")));
			
		} catch (Exception e) {
			logger.error("更新机体内排序异常:", e);
			writeJson(response, fail("保存失败！"));
		}
	}*/

	@ApiOperation("树结构")
	@ApiImplicitParam(name = "empid",value = "所要查看人的id", dataType = "String", required = true, paramType = "query")
	@RequestMapping(value = "/getPersonMaterial",method = RequestMethod.POST)
	public ResponseEntity getPersonMaterial(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String userid=SecurityUtils.getSubject().getCurrentUser().getId();
		boolean flag=false;

		List<Role> roles = userRoleService.getRolesByUserId(userid);
		if (roles.size()>0 && roles.stream().filter(temp -> temp.getCode().equals("90")).count() > 0) {
			flag=true;
		}
		if(flag){
			String empid=request.getParameter("empid");
			List<HashMap<String, Object>> prsonMaterial = businessService.getPersonMaterial(empid);
			return ok(prsonMaterial);
		}else{
			return fail("抱歉您无查看档案权限！");
		}
	
	}
	/*@ApiOperation("获取系统时间")
	@RequestMapping(value = "/getSystemDate",method = RequestMethod.POST)
	public ResponseEntity revoke(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		try {
			return ok(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			logger.error("取得系统时间失败！", e);
			return fail("取得系统时间失败!");
		}
	}*/
}
