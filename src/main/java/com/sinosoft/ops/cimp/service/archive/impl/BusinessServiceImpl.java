package com.sinosoft.ops.cimp.service.archive.impl;


import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;
import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialCategory;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialCategoryRepository;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialRepository;
import com.sinosoft.ops.cimp.repository.archive.busarch.BusArchApplyRepository;
import com.sinosoft.ops.cimp.service.archive.BusinessService;
import com.sinosoft.ops.cimp.service.archive.bean.bean.PersonAndPost;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("businessService")
public class BusinessServiceImpl  implements BusinessService {
	private static final Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);
	

	@Autowired
	private BusArchApplyRepository busArchApplyRepository;
	@Autowired
	private ArchiveMaterialCategoryRepository archiveMaterialCategoryRepository;
	@Autowired
	private ArchiveMaterialRepository archiveMaterialRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<PersonAndPost> getPersonAndPostByDepid(String Depid) {
		return busArchApplyRepository.getPersonAndPostByDepid(Depid);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> getA02ByDepid(String Depid) {
		return busArchApplyRepository.getA02ByDepid(Depid);
	}
	

	
	@Override
	@Transactional
	public int updateA02Data(List<Object> oblist) 
	{
		int i=0;
		for(Object ob:oblist)
		{
			Map map = (Map)ob;
			String subid = map.get("SUB_ID").toString();
			int A02025 = Integer.parseInt(map.get("A02025").toString());
			
			i+=busArchApplyRepository.updateA02Data(subid, A02025);
		}
		return i;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<HashMap<String, Object>> getPersonMaterial(String empId) {
		
		List<ArchiveMaterialCategory> categoryList = archiveMaterialCategoryRepository.findByParentCode();

		List<HashMap<String, Object>> materialCategoryAndMaterialList = new ArrayList<HashMap<String, Object>>();
		
		for(ArchiveMaterialCategory cate:categoryList){
			HashMap<String,Object> map = new  HashMap<String,Object>();
			
			map.put("categoryId",cate.getId());
			map.put("leaf",false);
			map.put("archiveMaterialText", cate.getAssignedSn() + " " + cate.getName());
			map.put("code", cate.getCode());
			if(cate.getParentCode().equals("00"))
			{
				map.put("assignedSn", cate.getSn());
			}
			else
			{
				map.put("assignedSn", cate.getAssignedSn());
			}
			
			map.put("parentCode", cate.getParentCode());
			map.put("archiveMaterialId",cate.getId());
			map.put("expanded", true );
			List<HashMap<String, Object>> nextchild = getChildCate(cate.getCode());
			if(nextchild!=null&& nextchild.size()>0)
			{
				map.put("children", nextchild);
			}
			materialCategoryAndMaterialList.add(map);
		}

		for (HashMap<String, Object> cate0 : materialCategoryAndMaterialList) {
			if (cate0.get("children") != null) {
				List<HashMap<String, Object>> tempmap1 = (List<HashMap<String, Object>>) cate0.get("children");
				if (tempmap1.get(0).get("leaf").toString().equals("false")) {
					for (HashMap<String, Object> cate1 : tempmap1) {
						List<ArchiveMaterial> listArch = archiveMaterialRepository.findAllByEmpIdAndCategoryIdOrderBySeqNum(empId,
								cate1.get("categoryId").toString());
						Integer i=0;
						if (listArch != null && listArch.size() > 0) {
							List<HashMap<String, Object>> tempmap = new  ArrayList<HashMap<String, Object>>();
							for (ArchiveMaterial ArchiveM : listArch) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("categoryId", ArchiveM.getCategoryId());
								if (ArchiveM.getTitle() != null) {
									map.put("archiveMaterialText", cate1.get("assignedSn") + "-" +(++i).toString() + " " + ArchiveM.getTitle() + "(" + ArchiveM.getCreateDate() + ")");
								}
								map.put("code", "");
								map.put("leaf", true);
								map.put("checked", false);
								map.put("count", ArchiveM.getPageCount());
								map.put("archiveMaterialId", ArchiveM.getId());
								tempmap.add(map);
							}
							if(tempmap.size()>0) cate1.put("children", tempmap);
						}
						else
						{
							cate1.put("leaf", true);
						}
					}
				}
			} else {
				List<ArchiveMaterial> listArch = archiveMaterialRepository.findAllByEmpIdAndCategoryIdOrderBySeqNum(empId, cate0.get("categoryId").toString());
				if (listArch != null && listArch.size() > 0) {
					List<HashMap<String, Object>> tempmap = new  ArrayList<HashMap<String, Object>>();
					Integer i=0;
					for (ArchiveMaterial ArchiveM : listArch) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("categoryId", ArchiveM.getCategoryId());
						if (ArchiveM.getTitle() != null) {
							map.put("archiveMaterialText", cate0.get("assignedSn") + "-" +(++i).toString() + " " + ArchiveM.getTitle() + "(" + ArchiveM.getCreateDate() + ")");
						}
						map.put("code", "");
						map.put("leaf", true);
						map.put("checked", false);
						map.put("count", ArchiveM.getPageCount());
						map.put("archiveMaterialId", ArchiveM.getId());
						tempmap.add(map);
					}
					if(tempmap.size()>0) cate0.put("children", tempmap);
				}
				else
				{
					cate0.put("leaf", true);
				}
			}
		}

		return materialCategoryAndMaterialList;
	}
	
	private List<HashMap<String, Object>> getChildCate(String prencode) {
		List<ArchiveMaterialCategory> categoryListnext=null;
		if (StringUtil.isEmptyOrNull(prencode)){
			categoryListnext = archiveMaterialCategoryRepository.findByParentCode();
		}else {
			categoryListnext=archiveMaterialCategoryRepository.findByParentCodeOrderBySn(prencode);
		}

		List<HashMap<String, Object>> tempmap = new  ArrayList<HashMap<String, Object>>();
		if(categoryListnext.size()>0)
		{
			for (ArchiveMaterialCategory cate : categoryListnext) {
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put("categoryId", cate.getId());
				map.put("leaf", false);
				map.put("archiveMaterialText", cate.getAssignedSn() + " " + cate.getName());
				map.put("code", cate.getCode());
				if(cate.getParentCode().equals("00"))
				{
					map.put("assignedSn", cate.getSn());
				}
				else
				{
					map.put("assignedSn", cate.getAssignedSn());
				}
				map.put("expanded", true);
				map.put("parentCode", cate.getParentCode());
				map.put("archiveMaterialId", cate.getId());
				List<HashMap<String, Object>> nextchild = getChildCate(cate.getCode());
				if(nextchild!=null&& nextchild.size()>0)
				{
					map.put("children", nextchild);
				}
				tempmap.add(map);
			}
		}
		return tempmap;
	}

	@Override
	@Transactional
	public List<String> findRoleNameByUserId(String useid) {
		
		return busArchApplyRepository.findRoleNameByUserId(useid);
	}
}
