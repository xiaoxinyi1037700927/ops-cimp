package com.sinosoft.ops.cimp.controller.archive;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.annotation.ArchiveApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyDetail;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyPerson;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.archive.BusArchApplyService;
import com.sinosoft.ops.cimp.service.user.UserRoleService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@ArchiveApiGroup
@Api(description="通用档案业务")
@Controller("busArchApplyController")
@RequestMapping("/busArchApply")
public class BusArchApplyController  extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(BusArchApplyController.class);
	
	@Autowired
	private BusArchApplyService busArchApplyService=null;

	@Autowired
	private UserRoleService userRoleService;

	@ApiOperation("创建申请查看档案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "reason",value = "所要查看档案的理由", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "endTime",value = "结束时间", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "data",value = "数据", dataType = "String", required = true, paramType = "query")
	})
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity create(HttpServletRequest request, HttpServletResponse response, BusArchApply entity) throws BusinessException {
		try {

			String reason ="";
			if(request.getParameter("reason")!=null)reason =request.getParameter("reason").toString();
			User user = SecurityUtils.getSubject().getCurrentUser();

			String applyid = UUID.randomUUID().toString();
			entity= new BusArchApply(applyid, user.getId(), user.getLoginName(),reason,Timestamp.valueOf(request.getParameter("endTime")), new Timestamp(System.currentTimeMillis()), user.getLoginName());
			entity.setVerifyType(1);
			entity.setOrdinal(0);

			JSONArray ListEmp = JSON.parseArray(request.getParameter("data"));
			List<BusArchApplyPerson> baplist = new ArrayList<BusArchApplyPerson>();
			List<BusArchApplyDetail> badlist = new ArrayList<BusArchApplyDetail>();
			int i=1,j=1;
			for(Object ob :ListEmp)
			{
				BusArchApplyPerson bap = new BusArchApplyPerson();
				JSONObject temp = JSONObject.parseObject(ob.toString());
				bap.setApplyId(applyid);
				String personid = UUID.randomUUID().toString();
				bap.setId(personid);
				bap.setEmpid(temp.getString("empid"));
				bap.setName(temp.getString("name"));
				bap.setPost(temp.getString("post"));
				bap.setDepid(temp.getString("depid"));
				bap.setA001003(temp.getString("a001003"));
				bap.setOrdinal(i); i++;
				baplist.add(bap);
				
				JSONArray ArrayDetail = JSON.parseArray(temp.getString("archivesData"));
				for(Object obdetail :ArrayDetail)
				{
					BusArchApplyDetail bad = new BusArchApplyDetail();
					JSONObject tempdetail = JSONObject.parseObject(obdetail.toString());
					String detailid = UUID.randomUUID().toString();
					bad.setId(detailid);
					bad.setApplyid(applyid);
					bad.setPersonid(personid);
					bad.setArchiveMaterialId(tempdetail.getString("archiveMaterialId"));
					bad.setArchiveMaterialText(tempdetail.getString("archiveMaterialText"));
					bad.setCategoryId(tempdetail.getString("categoryId"));
					bad.setOrdinal(i); i++;
					badlist.add(bad);
				}
			}		

			busArchApplyService.create(entity,baplist,badlist);
			return  ok("保存成功！");
		} catch (Exception e) {
			logger.error("创建失败！", e);
			return fail("保存失败！");
		}
	}

	@ApiOperation("查看档案申请和审批")

	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageSize",value = "每页数", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "pageIndex",value = "当前页数", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "resouceId",value = "11为查看申请，其他为审批", dataType = "String", required = true, paramType = "query")
				})
	@RequestMapping(value = "/getApplyByUser",method = RequestMethod.POST)
	public ResponseEntity getApplyByUser(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		String userid =SecurityUtils.getSubject().getCurrentUser().getId();
		String resouceId = request.getParameter("resouceId");
		Integer pageIndex;
		Integer pageSize;
		try{
			pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			pageSize = Integer.parseInt(request.getParameter("pageSize"));
		}catch(NumberFormatException e)
		{
			 pageIndex=1;
			 pageSize=10;
		}


		Map<String, Object> map = busArchApplyService.getApplyByUser(userid, resouceId, pageIndex, pageSize);

		return  ok(map);
	}

	@ApiOperation("根据id 查询申请详情")
	@ApiImplicitParam(name = "id",value = "id", dataType = "String", required = true, paramType = "query")
	@RequestMapping(value = "/getPersonByApplyId",method = RequestMethod.POST)
	public ResponseEntity getPersonByApplyId(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		String id = request.getParameter("id");
		List<HashMap<String, Object>> listbap = busArchApplyService.getPersonByApplyId(id);
		return  ok(listbap);
	}

	@ApiOperation("申请和审批档案树")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "empid",value = "所查人的id", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "applyid",value = "申请时为空", dataType = "String", required = true, paramType = "query")
	})
	@RequestMapping(value = "/getTreeByApplyId",method = RequestMethod.POST)
	public ResponseEntity getTreeByApplyId(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		String id = request.getParameter("applyid");
		String empid = request.getParameter("empid");
		List<HashMap<String, Object>> listbap = busArchApplyService.getTreeByApplyId(id,empid);
		return ok( listbap);
	}

	/*@RequestMapping(value = "/getDetailByPersonId",method = RequestMethod.POST)
	public void getDetailByPersonId(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		String id = request.getParameter("id");
		String empid = request.getParameter("empid");
		List<HashMap<String, Object>> listbad = busArchApplyService.getDetailByPersonId(id,empid);
		writeJson(response,ok(listbad));
	}*/

	@ApiOperation("修改申请查看档案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "reason",value = "所要查看档案的理由", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "endTime",value = "结束时间", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "applyid",value = "申请的id",dataType = "String",required = true,paramType = "query"),
			@ApiImplicitParam(name = "data",value = "数据和创建一样", dataType = "String", required = true, paramType = "query")
	})
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity update(HttpServletRequest request, HttpServletResponse response, BusArchApply entity) throws BusinessException {
		try {

			String reason ="";
			if(request.getParameter("reason")!=null)reason =request.getParameter("reason");
			String userid =  SecurityUtils.getSubject().getCurrentUser().getId();
			String username = SecurityUtils.getSubject().getCurrentUser().getLoginName();
			String applyid = request.getParameter("applyid");
			entity= new BusArchApply(applyid, userid, username,reason,Timestamp.valueOf(request.getParameter("endTime")), new Timestamp(System.currentTimeMillis()), username);
			entity.setVerifyType(1);
			entity.setOrdinal(0);
			
			JSONArray ListEmp = JSON.parseArray(request.getParameter("data"));
			List<BusArchApplyPerson> baplist = new ArrayList<BusArchApplyPerson>();
			List<BusArchApplyDetail> badlist = new ArrayList<BusArchApplyDetail>();
			int i=1,j=1;
			for(Object ob :ListEmp)
			{
				BusArchApplyPerson bap = new BusArchApplyPerson();
				JSONObject temp = JSONObject.parseObject(ob.toString());
				bap.setApplyId(applyid);
				String personid = UUID.randomUUID().toString();
				if(temp.getString("personid")!=null && !temp.getString("personid").equals(""))
				{
					personid = temp.getString("personid");
				}
				bap.setId(personid);
				bap.setEmpid(temp.getString("empid"));
				bap.setName(temp.getString("name"));
				bap.setPost(temp.getString("post"));
				bap.setDepid(temp.getString("depid"));
				bap.setA001003(temp.getString("a001003"));
				bap.setOrdinal(i); i++;
				baplist.add(bap);
				
				JSONArray ArrayDetail = JSON.parseArray(temp.getString("archivesData"));
				for(Object obdetail :ArrayDetail)
				{
					BusArchApplyDetail bad = new BusArchApplyDetail();
					JSONObject tempdetail = JSONObject.parseObject(obdetail.toString());
					String detailid = UUID.randomUUID().toString();
					if(tempdetail.getString("detailid")!=null && !tempdetail.getString("detailid").equals(""))
					{
						detailid = tempdetail.getString("detailid");
					}

					bad.setId(detailid);
					bad.setApplyid(applyid);
					bad.setPersonid(personid);
					bad.setArchiveMaterialId(tempdetail.getString("archiveMaterialId"));
					bad.setArchiveMaterialText(tempdetail.getString("archiveMaterialText"));
					bad.setCategoryId(tempdetail.getString("categoryId"));
					bad.setOrdinal(i); i++;
					badlist.add(bad);
				}
			}			

			busArchApplyService.update(entity,baplist,badlist);
			return ok("保存成功！");
		} catch (Exception e) {
			logger.error("保存失败！", e);
			return fail("保存失败！");
		}
	}

	@ApiOperation("删除申请查看档案")
	@ApiImplicitParam(name = "applyid",value = "申请id", dataType = "String", required = true, paramType = "query")
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity delete(HttpServletRequest request, HttpServletResponse response, BusArchApply entity) throws BusinessException {
		try {
			String applyid = request.getParameter("applyid");
			busArchApplyService.updateflg(applyid,-1);
			return ok("删除成功！");
		} catch (Exception e) {
			logger.error("删除失败！", e);
			return fail("删除失败！");
		}
	}

	@ApiOperation("撤回申请查看档案")
	@ApiImplicitParam(name = "applyid",value = "申请id", dataType = "String", required = true, paramType = "query")
	@RequestMapping(value = "/revoke",method = RequestMethod.POST)
	public ResponseEntity revoke(HttpServletRequest request, HttpServletResponse response, BusArchApply entity) throws BusinessException {
		try {
			String applyid = request.getParameter("applyid");
			busArchApplyService.updateflg(applyid,0);
			return ok("撤回成功！");
		} catch (Exception e) {
			logger.error("撤回失败！", e);
			return fail("撤回失败！");
		}
	}

	@ApiOperation("退回申请查看档案")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "reason",value = "所要查看档案的理由", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "operatortype",value = "操作人员 0为申请退回", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "verifyType",value ="核实类型", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "applyid",value ="申请id", dataType = "String", required = true, paramType = "query"),
			@ApiImplicitParam(name = "endTime",value ="结束时间", dataType = "String", required = true, paramType = "query")
	})
	@RequestMapping(value = "/updateVerifyMessage",method = RequestMethod.POST)
	public ResponseEntity updateVerifyMessage(HttpServletRequest request, HttpServletResponse response) throws BusinessException {
		String message="";
		try {

			String reason ="";
			if(request.getParameter("reason")!=null)reason =request.getParameter("reason");
			String userid = SecurityUtils.getSubject().getCurrentUser().getId();
			String username = SecurityUtils.getSubject().getCurrentUser().getLoginName();

			String applyid =request.getParameter("applyid");
			Integer operatortype = Integer.parseInt(request.getParameter("operatortype"));			
			Integer verifyType = Integer.parseInt(request.getParameter("verifyType"));
			String revokeReason = request.getParameter("revokeReason");
			if(operatortype == 0)
			{
				message="退回";
				verifyType=99;
			}
			else
			{
				List<Role> roles = userRoleService.getRolesByUserId(userid);
				if (roles.size()>0 && roles.stream().filter(temp -> temp.getCode().equals("13")).count() > 0) {
					message="通过";
					verifyType=100;
				}
			}

			BusArchApply entity= new BusArchApply(applyid, userid, username,reason,Timestamp.valueOf(request.getParameter("endTime")), new Timestamp(System.currentTimeMillis()), username);
			entity.setVerifyType(verifyType);
			entity.setVerifyTime(new Timestamp(System.currentTimeMillis()));
			entity.setVerifyBy(username);
			entity.setRevokeReason(revokeReason);
			entity.setOrdinal(0);
			busArchApplyService.update(entity);
			return ok("更新成功！");
		} catch (Exception e) {
			logger.error("更新失败！", e);
			return fail(message+"更新失败！");
		}
	}

}
