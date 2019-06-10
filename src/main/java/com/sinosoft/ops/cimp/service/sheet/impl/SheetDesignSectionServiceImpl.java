package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignSectionService;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSectionDao;
import com.sinosoft.ops.cimp.util.word.FileUtils;
import com.sinosoft.ops.cimp.util.word.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;

@Service("sheetDesignSectionService")
public class SheetDesignSectionServiceImpl extends BaseEntityServiceImpl<SheetDesignSection> implements SheetDesignSectionService {

	@Autowired
	private SheetDesignSectionDao SheetDesignSectionDao;

	@Override
	@Transactional
	public Collection<SheetDesignSection> getByDesignId(UUID designId) {
		return SheetDesignSectionDao.getByDesignId(designId);
	}
	
	@Override
	@Transactional
	public Collection<SheetDesignSection> getByDesignId(UUID designId,String orderField,boolean desc) {
		return SheetDesignSectionDao.getByDesignId(designId, orderField, desc);
	}

	@Override
	@Transactional
	public Integer getMaxOrdinal() {
		return SheetDesignSectionDao.getMaxOrdinal();
	}

	/**
	 * 移动数据块
	 */
	@Override
	@Transactional
	public void moveSection(String idStr, String toIdStr) {
		
		UUID id = UUID.fromString(idStr);
		UUID toId = UUID.fromString(toIdStr);
		
		SheetDesignSection centry = getById(id);
		Integer cen = centry.getOrdinal();
		
		SheetDesignSection adjacent = getById(toId);
		Integer adj = adjacent.getOrdinal();
		centry.setOrdinal(adj);
		adjacent.setOrdinal(cen);
		update(centry);
		update(adjacent);
	}

	@Override
	@Transactional
	public Boolean checkSectionSame(SheetDesignSection entity)
	{
		Collection<SheetDesignSection> sheetDesignSections= SheetDesignSectionDao.getByDesignId(entity.getDesignId());
		for(SheetDesignSection sheetDesignSection :sheetDesignSections)
		{
			if(entity.getStartRowNo()>=sheetDesignSection.getStartRowNo() && entity.getStartRowNo()<=sheetDesignSection.getEndRowNo() && entity.getStartColumnNo()>=sheetDesignSection.getStartColumnNo() && entity.getStartColumnNo()<=sheetDesignSection.getEndColumnNo())
			{
				return false;
			}
			if(entity.getStartRowNo()>=sheetDesignSection.getStartRowNo() && entity.getStartRowNo()<=sheetDesignSection.getEndRowNo() && entity.getEndColumnNo()>=sheetDesignSection.getStartColumnNo() && entity.getEndColumnNo()<=sheetDesignSection.getEndColumnNo())
			{
				return false;
			}
			if(entity.getEndRowNo()>=sheetDesignSection.getStartRowNo() && entity.getEndRowNo()<=sheetDesignSection.getEndRowNo() && entity.getEndColumnNo()>=sheetDesignSection.getStartColumnNo() && entity.getEndColumnNo()<=sheetDesignSection.getEndColumnNo())
			{
				return false;
			}
			if(entity.getEndRowNo()>=sheetDesignSection.getStartRowNo() && entity.getEndRowNo()<=sheetDesignSection.getEndRowNo() && entity.getStartColumnNo()>=sheetDesignSection.getStartColumnNo() && entity.getStartColumnNo()<=sheetDesignSection.getEndColumnNo())
			{
				return false;
			}


			if(entity.getCtrlRowStart()>=sheetDesignSection.getCtrlRowStart() && entity.getCtrlRowStart()<=sheetDesignSection.getCtrlRowEnd() && entity.getCtrlColumnStart()>=sheetDesignSection.getCtrlColumnStart() && entity.getCtrlColumnStart()<=sheetDesignSection.getCtrlColumnEnd())
			{
				return false;
			}
			if(entity.getCtrlRowStart()>=sheetDesignSection.getCtrlRowStart() && entity.getCtrlRowStart()<=sheetDesignSection.getCtrlRowEnd() && entity.getCtrlColumnEnd()>=sheetDesignSection.getCtrlColumnStart() && entity.getCtrlColumnEnd()<=sheetDesignSection.getCtrlColumnEnd())
			{
				return false;
			}
			if(entity.getCtrlRowEnd()>=sheetDesignSection.getCtrlRowStart() && entity.getCtrlRowEnd()<=sheetDesignSection.getCtrlRowEnd() && entity.getCtrlColumnEnd()>=sheetDesignSection.getCtrlColumnStart() && entity.getCtrlColumnEnd()<=sheetDesignSection.getCtrlColumnEnd())
			{
				return false;
			}
			if(entity.getCtrlRowEnd()>=sheetDesignSection.getCtrlRowStart() && entity.getCtrlRowEnd()<=sheetDesignSection.getCtrlRowEnd() && entity.getCtrlColumnStart()>=sheetDesignSection.getCtrlColumnStart() && entity.getCtrlColumnStart()<=sheetDesignSection.getCtrlColumnEnd())
			{
				return false;
			}
		}
		return true;
	}

	@Override
	@Transactional
	public Collection<SheetDesignSection> getSectionTreeByDesignId(UUID designId) {
		Collection<SheetDesignSection> collection = SheetDesignSectionDao.getByDesignId(designId, "ordinal", false);
		List<SheetDesignSection> tree = generateSectionTree(collection,designId);
		return tree;
	}

	@Override
	@Transactional
	public List<SheetDesignSection> generateSectionTree(Collection<SheetDesignSection> collection, UUID designId) {
		List<SheetDesignSection> list = new ArrayList<>();
		for (SheetDesignSection sheetDesignSection : collection) {
			if(designId.equals(sheetDesignSection.getParentId())){
				
				List<SheetDesignSection> childrens = generateSectionTree(collection,sheetDesignSection.getId());
				sheetDesignSection.setChildrens(childrens);
				if(CollectionUtils.isEmpty(childrens)){
					sheetDesignSection.setLeaf(true);
				}else{
					sheetDesignSection.setLeaf(false);
				}
				list.add(sheetDesignSection);
			}
		}
		return list;
	}

	@Override
	@Transactional
	public String getMaxSectionNoByDesignId(UUID designId) {
		return SheetDesignSectionDao.getMaxSectionNoByDesignId(designId);
	}

	@Override
	@Transactional
	public void dataBinding(Collection<SheetDesignSection> collection) {
		
		int i = 1;
		for (SheetDesignSection sheetDesignSection : collection) {
			sheetDesignSection.setBindValue("测试-"+i);
			i++;
		}
		
	}

	@Override
	@Transactional
	public void fillData2Templement(Collection<Map<String, Object>> collection, byte[] content,
			HttpServletResponse response) throws Exception {
		String filePath = WordUtil.fillData2Templement(content, collection);
		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("测试.docx", "UTF-8"));  
		response.setHeader("Connection", "close");  
		response.setHeader("Content-Type", "application/x-download"); 
		//String outPath = "C:\\software\\eclipseWorkSpace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\iimp-gradle\\resources\\temp\\123.docx";
		FileUtils.writeFile(filePath, response);
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}

	@Override
	@Transactional
	public void deleteByDesignId(UUID designId) {
		SheetDesignSectionDao.deleteByDesignId(designId);
	}

	@Override
	@Transactional
	public boolean moveUp(SheetDesignSection entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignSection curr = SheetDesignSectionDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignSection previous = SheetDesignSectionDao.findPrevious(id,designId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = SheetDesignSectionDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = SheetDesignSectionDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveDown(SheetDesignSection entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignSection curr = SheetDesignSectionDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignSection nextvious = SheetDesignSectionDao.findNext(id, designId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = SheetDesignSectionDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = SheetDesignSectionDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String,Integer> getRangeByDesignId(UUID designId) {
		return SheetDesignSectionDao.getRangeByDesignId(designId);
		
	}

	
	
	
	
	
}
