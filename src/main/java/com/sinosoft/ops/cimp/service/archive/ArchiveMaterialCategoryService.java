package com.sinosoft.ops.cimp.service.archive;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialCategory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName:  ArchiveMaterialCategoryService
 * @description: 档案分类Service
 */
public interface ArchiveMaterialCategoryService{
	

	/**根据分类ID删除分类*/
	public void deleteById(Serializable id);

	@Transactional(readOnly=true)
	Map<String, ArchiveMaterialCategory> getAll();

	public Serializable create(ArchiveMaterialCategory entity);
	
	public void delete(ArchiveMaterialCategory entity);
	
	public void  update(ArchiveMaterialCategory entity);


	@Transactional
	void deleteById(String id);

	public List<HashMap<String, Object>> getMaterialCategoryAndMaterial4Tree(String code, String empId, String categoryId);

	public List<String> getIdCardsByEmpid(String empId);

	public String getidcardforone(String empId);

	public String findEmpidByUserID(String userid);

	
	
}
