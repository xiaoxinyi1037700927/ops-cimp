/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCarrier;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignSection;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCarrierDao;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignSectionDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCarrierService;
import com.sinosoft.ops.cimp.util.word.WordUtil;
import com.sinosoft.ops.cimp.util.word.analyzeWordFieldService.AnalyzeWordFieldTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignCarrierServiceImpl
 * @Description: 表格设计载体服务实现
 * @Author:        Nil
 * @Date:            2017年8月18日 下午1:26:15
 * @Version        1.0.0
 */
@Service("sheetDesignCarrierService")
public class SheetDesignCarrierServiceImpl extends BaseEntityServiceImpl<SheetDesignCarrier> implements SheetDesignCarrierService {
	@Autowired
	private SheetDesignCarrierDao sheetDesignCarrierDao;
	
	@Autowired
	private SheetDesignSectionDao sheetDesignSectionDao;
	
	public SheetDesignCarrierServiceImpl(){

	}
	
    @Override
    @Transactional(readOnly=true)
    public SheetDesignCarrier getByDesignId(UUID id) {
        // TODO Auto-generated method stub
        return sheetDesignCarrierDao.getByDesignId(id);
    }

	@Transactional
    @Override
    public int deleteByDesignId(UUID designId) {
        return sheetDesignCarrierDao.deleteByDesignId(designId);
    }

	@Override
	@Transactional
	public void analyzeWord2Tree(byte[] content, UUID designId) throws Exception {
		Collection<TreeNode> collection = WordUtil.analyzeWord2Tree(content, designId);
		saveTreeNode(collection,designId);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer word2html(UUID designId,String templateFilePath,String tempPath) throws Exception {
		SheetDesignCarrier oldItem = sheetDesignCarrierDao.getByDesignId(designId);
		if (oldItem != null) {
			byte[] data = oldItem.getContent();
			// 破解Aspose代码
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream license = loader.getResourceAsStream("license.xml");// 凭证文件
			License aposeLic = new License();
			aposeLic.setLicense(license);

			license.close();
			Document document = new Document(new ByteArrayInputStream(data));
			document.save(templateFilePath);
			return 1;
		}
		return 0;
	}

	@Transactional
	private void saveTreeNode(Collection<TreeNode> collection,UUID designId) {
		for (TreeNode tree : collection) {
			AnalyzeWordFieldTree analyzeWordFieldTree = (AnalyzeWordFieldTree)tree;
			SheetDesignSection section = new SheetDesignSection();
			section.setId((UUID)analyzeWordFieldTree.getId());
			section.setName(analyzeWordFieldTree.getText());
			section.setDesignId(designId);
			section.setSectionNo(analyzeWordFieldTree.getNo());
			section.setOrdinal(analyzeWordFieldTree.getOrdinal());
			section.setParentId((UUID)analyzeWordFieldTree.getParentId());
			section.setStatus(DataStatus.NORMAL.getValue());
//			section.setCreatedBy(((SysUser) SecurityUtils.getSubject().getPrincipal()).getId());
			section.setCreatedBy(UUID.randomUUID());
			section.setCreatedTime(new Timestamp(System.currentTimeMillis()));
//			section.setLastModifiedBy(((SysUser) SecurityUtils.getSubject().getPrincipal()).getId());
			section.setLastModifiedBy(UUID.randomUUID());
			section.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));

			if(CollectionUtils.isEmpty(analyzeWordFieldTree.getChildren())){
				sheetDesignSectionDao.save(section);
			}else{
				sheetDesignSectionDao.save(section);
				saveTreeNode(analyzeWordFieldTree.getChildren(), designId);

			}
		}
	}

    @Override
    @Transactional(readOnly=true)
    public Collection<UUID> getAllIds() {
        return sheetDesignCarrierDao.getAllIds();
    }
}