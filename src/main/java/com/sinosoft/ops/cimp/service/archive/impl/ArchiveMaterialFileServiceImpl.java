package com.sinosoft.ops.cimp.service.archive.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialFile;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialFileRepository;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @ClassName:  ArchiveMaterialFileServiceImpl
 */
@Service("archiveMaterialFileService")
public class ArchiveMaterialFileServiceImpl  implements ArchiveMaterialFileService {
	
	@Autowired
	private ArchiveMaterialFileRepository archiveMaterialFileRepository;
	
	@Override
	@Transactional
	public ArchiveMaterialFile findbypageNo(String archiveMaterial, Integer pageNo, String type){
		return archiveMaterialFileRepository.findAllByArchiveMaterialIdAndPageNumberAndAndFileType(archiveMaterial, pageNo,type);
	}

	@Override
	public ArchiveMaterialFile getById(String id) {
		return archiveMaterialFileRepository.findById(id).get();
	}

	@Override
	@Transactional
	public ArrayList<ArchiveMaterialFile> getArchiveMaterialFilebyAchiveMaterialID(String  archiveMaterialId){
		return (ArrayList<ArchiveMaterialFile>) archiveMaterialFileRepository.findByArchiveMaterialIdOrderByaAndArchiveMaterialIdAndIdAndPageNumber(archiveMaterialId);
	};
	
	@Override
	@Transactional
	public List<ArchiveMaterialFile> getArchiveMaterialFilebyAchiveMaterialIDs(List<String> idList){
		return (List<ArchiveMaterialFile>) archiveMaterialFileRepository.findAllByArchiveMaterialIdOrderByArchiveMaterialIdAndIdAndPageNumber(idList);
	}

	@Override
	@Transactional
	public Map<String,  List<ArchiveMaterialFile>> getMapByAchiveMaterialIDs(List<String> idList) {
		Map<String, List<ArchiveMaterialFile>> map = new HashMap<String, List<ArchiveMaterialFile>>();
		List<ArchiveMaterialFile> list = new ArrayList<ArchiveMaterialFile>();
		for (String materialId : idList) {
			list = ( List<ArchiveMaterialFile>) archiveMaterialFileRepository.findByArchiveMaterialIdOrderByaAndArchiveMaterialIdAndIdAndPageNumber(materialId);
			map.put(materialId, list);
		}
		return map;
	}

	
}
