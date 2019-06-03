package com.sinosoft.ops.cimp.service.archive.impl;

import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyDetail;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyPerson;
import com.sinosoft.ops.cimp.entity.user.Role;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyDetailRepository;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyPersonRepository;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyRepository;
import com.sinosoft.ops.cimp.service.archive.BusArchApplyService;
import com.sinosoft.ops.cimp.service.archive.BusinessService;
import com.sinosoft.ops.cimp.service.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Repository("busArchApplyService")
public class BusArchApplyServiceImpl implements BusArchApplyService {

	@Autowired
	private BusArchApplyRepository busArchApplyRepository;
	
	@Autowired
	private BusArchApplyPersonRepository busArchApplyPersonRepository;
	
	@Autowired
	private BusArchApplyDetailRepository busArchApplyDetailRepository;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UserRoleService userRoleService;


	/**
	 * 创建申请
	 * @param entity
	 * @param baplist
	 * @param badlist
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void create(BusArchApply entity,List<BusArchApplyPerson> baplist,List<BusArchApplyDetail> badlist) throws Exception{
		busArchApplyRepository.save(entity);
		for(BusArchApplyPerson bap:baplist)
		{
			busArchApplyPersonRepository.save(bap);
		}
		for(BusArchApplyDetail bad:badlist)
		{
			busArchApplyDetailRepository.save(bad);
		}
	}

	@Override
	@Transactional
	public void update(BusArchApply entity){
		busArchApplyRepository.save(entity);
	}

	/**
	 * 修改申请查看档案
	 * @param entity
	 * @param baplist
	 * @param badlist
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void update(BusArchApply entity,List<BusArchApplyPerson> baplist,List<BusArchApplyDetail> badlist) throws Exception{
		List<BusArchApplyPerson> listbap =  busArchApplyPersonRepository.findAllByApplyId(entity.getId());

		for(BusArchApplyPerson Person:listbap)
		{
			List<BusArchApplyDetail> listbad = busArchApplyDetailRepository.findAllByPersonid(Person.getId());
			boolean b1 = busArchApplyPersonRepository.existsById(Person.getId());
			if (b1) {
				busArchApplyPersonRepository.deleteById(Person.getId());
			}

			for(BusArchApplyDetail Detail:listbad)
			{
				boolean b = busArchApplyDetailRepository.existsById(Detail.getId());
				if (b) {
					busArchApplyDetailRepository.deleteById(Detail.getId());
				}
			}
		}

		busArchApplyRepository.save(entity);
		for(BusArchApplyPerson bap:baplist)
		{
			busArchApplyPersonRepository.save(bap);
		}
		for(BusArchApplyDetail bad:badlist)
		{
			busArchApplyDetailRepository.save(bad);
		}
	}

	/**
	 * 分页查看申请或审批
	 * @param userid
	 * @param resouceId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	@Transactional
	public Map<String, Object> getApplyByUser(String userid,String resouceId,Integer pageIndex,Integer pageSize)
	{
		Map<String, Object> map=new HashMap<String, Object>();
		List<BusArchApply> listBus =new ArrayList<BusArchApply>();
		if(resouceId.equals("11"))
		{
			Page<BusArchApply> pagelist = busArchApplyRepository.findAllByUseridAndVerifyType(userid, PageRequest.of((pageIndex-1), pageSize));
			listBus=pagelist.getContent();
			map.put("total",pagelist.getTotalElements());
			map.put("pages",pagelist.getTotalPages());
			map.put("pageIndex",pageIndex);
			map.put("pageSize",pageSize);
			for(BusArchApply busArchApply:listBus)
			{
				List<BusArchApplyPerson> ListBusArchApplyPerson =busArchApplyPersonRepository.findAllByApplyId(busArchApply.getId());
				String strPerson ="";
				for(BusArchApplyPerson busArchApplyPerson : ListBusArchApplyPerson)
				{
					strPerson+= busArchApplyPerson.getName() + ",";
				}	
				strPerson=strPerson.substring(0, strPerson.length()-1);
				busArchApply.setPersonName(strPerson);
			}
			map.put("data",listBus);
			return map;
		}
		else
		{
			List<Role> roles =  userRoleService.getRolesByUserId(userid);
			if (roles.size()>0 && roles.stream().filter(temp -> temp.getCode().equals("13")).count() > 0) {
				Page<BusArchApply> pagelist = busArchApplyRepository.findAllByVerifyType(PageRequest.of((pageIndex-1),pageSize));
				listBus=pagelist.getContent();
				map.put("total",pagelist.getTotalElements());
				map.put("pages",pagelist.getTotalPages());
				map.put("pageIndex",pageIndex+1);
				map.put("pageSize",pageSize);
				for(BusArchApply busArchApply:listBus)
				{
					List<BusArchApplyPerson> ListBusArchApplyPerson =busArchApplyPersonRepository.findAllByApplyId(busArchApply.getId());
					System.out.println(ListBusArchApplyPerson);
					String strPerson ="";
					for(BusArchApplyPerson busArchApplyPerson : ListBusArchApplyPerson)
					{
						strPerson+= busArchApplyPerson.getName() + ",";
					}	
					strPerson=strPerson.substring(0, strPerson.length()-1);
					busArchApply.setPersonName(strPerson);
				}
				map.put("data",listBus);
				return map;
				
			} else {
				return map;
			}
		}		
	}

	/**
	 * 根据id查看申请详情
	 * @param applyid
	 * @return
	 */
	@Override
	@Transactional
	public List<HashMap<String, Object>> getPersonByApplyId(String applyid)
	{
		BusArchApply busArchApplies=busArchApplyRepository.findByIdAnd(applyid);
		List<BusArchApplyPerson> listbap =  busArchApplyPersonRepository.findAllByApplyId(applyid);
		List<HashMap<String, Object>> listpm = new ArrayList<HashMap<String, Object>>();
		HashMap<String,Object> apply = new  HashMap<String,Object>();
		apply.put("endtime",busArchApplies.getEndTime());
		apply.put("reason",busArchApplies.getReason());
		listpm.add(apply);
		for(BusArchApplyPerson bap : listbap){
			HashMap<String,Object> map = new  HashMap<String,Object>();
			map.put("personid",bap.getId());
			map.put("empid",bap.getEmpid());
			map.put("name",bap.getName());
			map.put("post",bap.getPost());
			map.put("depid",bap.getDepid());
			map.put("a001003",bap.getA001003());
			List<HashMap<String, Object>> nextchild = getDetailArray(bap.getId());
			if(nextchild!=null&& nextchild.size()>0)
			{
				map.put("archivesData", nextchild);
			}
			listpm.add(map);
		}
		
		return listpm;
	}
	
	private List<HashMap<String, Object>> getDetailArray(String personid)
	{
		List<BusArchApplyDetail> listbad = busArchApplyDetailRepository.findAllByPersonid(personid);
		List<HashMap<String, Object>> lstmapbad = new ArrayList<HashMap<String, Object>>();
		for(BusArchApplyDetail bad : listbad){
			HashMap<String,Object> map = new  HashMap<String,Object>();
			map.put("detailid", bad.getId());
			map.put("archiveMaterialId", bad.getArchiveMaterialId());
			map.put("archiveMaterialText", bad.getArchiveMaterialText());
			map.put("categoryId", bad.getCategoryId());
			lstmapbad.add(map);
		}
		return lstmapbad;
	}

	/**
	 * 申请和审批档案树
	 * @param applyid
	 * @param empid
	 * @return
	 */
	@Override
	@Transactional
	public List<HashMap<String, Object>> getTreeByApplyId(String applyid,String empid)
	{
		List<HashMap<String, Object>> listpm = businessService.getPersonMaterial(empid);
		System.out.println(listpm);
		List<BusArchApplyPerson> listbap =  busArchApplyPersonRepository.findAllByApplyId(applyid);
		
		String strPersonId=null;
		for(BusArchApplyPerson bap : listbap){
			if(empid.equals(bap.getEmpid()))
			{
				strPersonId = bap.getId();
			}
		}
		List<BusArchApplyDetail> listbad = busArchApplyDetailRepository.findAllByPersonid(strPersonId);
		for(BusArchApplyDetail bad : listbad)
		{
			listpm=getChildCate(listpm,bad.getArchiveMaterialId());
		}
		return listpm;
	}
	
	@Override
	@Transactional
	public List<HashMap<String, Object>> getDetailByPersonId(String personid,String empid)
	{
		List<HashMap<String, Object>> listpm = businessService.getPersonMaterial(empid);
//		List<BusArchApplyDetail> listbad = busArchApplyDetailDao.getDetailByPersonId(personid);
//		for(BusArchApplyDetail bad : listbad)
//		{
//			listpm=getChildCate(listpm,bad.getArchiveMaterialId());
//		}

		return listpm;
	}



	private List<HashMap<String, Object>> getChildCate(List<HashMap<String, Object>> listpm, String archiveMaterialId) {
		for (HashMap<String, Object> pm : listpm) {
			if (pm.get("children") != null) {
				List<HashMap<String, Object>> listtemp = (List<HashMap<String, Object>>) pm.get("children");
				getChildCate(listtemp, archiveMaterialId);
			} else {
				if (pm.get("archiveMaterialId").equals(archiveMaterialId)) {
					pm.put("checked", true);
					return listpm;
				}
			}
		}
		return listpm;
	}


	/**
	 * 根据id删除查看申请
	 * @param id
	 * @param type
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateflg(String id,Integer type) throws Exception {
		busArchApplyRepository.updArch(type,id);
	}
}
