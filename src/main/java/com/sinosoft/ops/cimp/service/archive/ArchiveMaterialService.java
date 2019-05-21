package com.sinosoft.ops.cimp.service.archive;

import java.util.List;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;

/**
 * @ClassName:  ArchiveMaterialService
 */
public interface ArchiveMaterialService {

	public ArchiveMaterial getById(String id);
	
	/**
	 * 获取 指定干部ID ArchiveMaterial 实体List
	 */
	public List<ArchiveMaterial> getArchiveListByEmpId(String persionID);
	
	/**
	 * 获取 指定干部ID+档案分类ID ArchiveMaterial ID List
	 */
	public List<String> getArchiveMaterialIDs(String empId, String categoryId);
	
	/**
	 * 获取 指定干部ID+档案分类ID ArchiveMaterial 实体List
	 */
	public List<ArchiveMaterial> getArchiveListByEmpIdAndCaterogyId(String persionID, String categoryId);
}
