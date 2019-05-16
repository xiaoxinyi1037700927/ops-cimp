package com.sinosoft.ops.cimp.service.archive;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;

/**
 * @ClassName:  ArchiveMaterialFileService
 * @author:        zhaizf
 * @date:            2017年12月24日 
 */
public interface ArchiveMaterialFileService{

	public    ArchiveMaterialFile   getById(String id);
	/**
	 *  获取 单个Achive_Material_ID 对应的ArchiveMaterialFile Collection
	 */
	public Collection<ArchiveMaterialFile> getArchiveMaterialFilebyAchiveMaterialID(String archiveMaterialId);
	
	/**
	 * 获取 多个Achive_Material_ID 对应的ArchiveMaterialFile Collection
	 */
	public Collection<ArchiveMaterialFile> getArchiveMaterialFilebyAchiveMaterialIDs(List<String> archiveMaterialIds);
	
	/**
	 * 根据archiveMaterialId 和pageNo获取 ArchiveMaterialFile
	 * */
	public ArchiveMaterialFile findbypageNo(String archiveMaterialId, String pageNo, String type);
	
	/**
	 * 获取 多个Achive_Material_ID 对应的ArchiveMaterialFile Map <Material_ID,List<ArchiveMaterialFile>>
	 */
	public Map<String,List<ArchiveMaterialFile>> getMapByAchiveMaterialIDs(List<String> archiveMaterialIds);
	
	
}
