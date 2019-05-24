package com.sinosoft.ops.cimp.service.archive.impl;

import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyDetail;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyPerson;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyDetailRepository;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyPersonRepository;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyRepository;
import com.sinosoft.ops.cimp.repository.user.UserRoleRepository;
import com.sinosoft.ops.cimp.service.archive.BusArchApplyService;
import com.sinosoft.ops.cimp.service.archive.BusinessService;
import oracle.sql.RAW;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	@Transactional
	public void update(BusArchApply entity,List<BusArchApplyPerson> baplist,List<BusArchApplyDetail> badlist) throws Exception{
		List<BusArchApplyPerson> listbap =  busArchApplyPersonRepository.findAllByApplyId(entity.getId());

		for(BusArchApplyPerson Person:listbap)
		{
			busArchApplyPersonRepository.deleteById(Person.getId());
			List<BusArchApplyDetail> listbad = busArchApplyDetailRepository.findAllByPersonid(Person.getId());
			for(BusArchApplyDetail Detail:listbad)
			{
				busArchApplyDetailRepository.deleteById(Detail.getId());
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


	@Override
	@Transactional
	public List<BusArchApply> getApplyByUser(String userid,String resouceId)
	{
		List<BusArchApply> listBus =new ArrayList<BusArchApply>();
		if(resouceId.equals("11"))
		{
			listBus =  busArchApplyRepository.findAllByUseridAndVerifyType(userid);
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
			return listBus;
		}
		else
		{
//			Collection<UserRole> listsysUserRole = busArchApplyRepository.getByUserId(UUID.fromString(userid));
//			if (listsysUserRole.size()>0 && listsysUserRole.stream().filter(temp -> temp.getRoleId() == 400).count() > 0) {
				listBus = busArchApplyRepository.findAllByVerifyType();
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
				return listBus;
				
//			} else {
//				return listBus;
//			}
		}		
	}
	
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
			map.put("empId",bap.getEmpid());
			map.put("name",bap.getName());
			map.put("position",bap.getPost());
			map.put("depid",bap.getDepid());
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
	
	@Override
	@Transactional
	public List<HashMap<String, Object>> getTreeByApplyId(String applyid,String empid)
	{
		List<HashMap<String, Object>> listpm = businessService.getPersonMaterial(empid);
		List<BusArchApplyPerson> listbap =  busArchApplyPersonRepository.findAllByApplyId(applyid);
		
		String strPersonId=null;
		for(BusArchApplyPerson bap : listbap){
			if(empid.equals(bap.getEmpid()))
			{
				strPersonId = bap.getId().toString();
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



	@Override
	@Transactional
	public void updateflg(String id,Integer type) throws Exception {
		busArchApplyRepository.updArch(type,id);
	}
}
