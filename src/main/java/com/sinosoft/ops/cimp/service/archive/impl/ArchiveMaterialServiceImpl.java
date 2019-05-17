package com.sinosoft.ops.cimp.service.archive.impl;

import java.util.List;


import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialRepository;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName:  ArchiveMaterialServiceImpl
 * @author:        zhaizf
 * @date:            2017年12月24日 
 */
@Service("archiveMaterialService")
public class ArchiveMaterialServiceImpl implements ArchiveMaterialService {
	@Autowired
	private ArchiveMaterialRepository archiveMaterialRepository;


	@Override
	public ArchiveMaterial getById(String id) {
		return archiveMaterialRepository.findById(id).get();
	}

	@Override
	@Transactional
	public List<ArchiveMaterial> getArchiveListByEmpId(String empId){
		return archiveMaterialRepository.findAllByEmpIdOrderByCategoryIdSeqNum(empId);
	}
	
	@Override
	@Transactional
	public List<ArchiveMaterial> getArchiveListByEmpIdAndCaterogyId(String empId,String categoryId){
		return archiveMaterialRepository.findAllByEmpIdAndCategoryIdOrderBySeqNum(empId,categoryId);
	}
	@Transactional
	public List<String> getArchiveMaterialIDs(String empId,String categoryId){
		return  archiveMaterialRepository.findByEmpIdAndCategoryId(empId, categoryId);
	}
}
