/**
 * @Project:      IIMP
 * @Title:          SheetDesignCarrierServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.DefaultTreeNode;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;

import com.sinosoft.ops.cimp.constant.ExpressionType;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignExpression;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignExpressionDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCellService;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignExpressionService;
import com.sinosoft.ops.cimp.util.BeanUtils;
import com.sinosoft.ops.cimp.common.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName:  SheetDesignExpressionServiceImpl
 * @Description: 表格设计表达式服务实现
 * @Author:        Wa
 * @Date:            2018年5月29日 下午6:26:15
 * @Version        1.0.0
 */
@Service("sheetDesignExpressionService")
public class SheetDesignExpressionServiceImpl extends BaseEntityServiceImpl<SheetDesignExpression> implements SheetDesignExpressionService {
	private static final Logger logger = LoggerFactory.getLogger(SheetDesignCellService.class);
	@Autowired
	private SheetDesignExpressionDao sheetDesignExpressionDao;
	
	
	@Override
    @Transactional(readOnly=true)
    public Collection<TreeNode> getByType(Collection<SheetDesignExpression> itemList, String type) {
		//构建树形
		Collection<TreeNode> list = loadChildCodeItemList(itemList, type);
        return list;
    }
	private Collection<TreeNode> loadChildCodeItemList(Collection<SheetDesignExpression> List, String type){
		long i = System.currentTimeMillis();
		Collection<TreeNode> childTree = new ArrayList<TreeNode>();
		for (SheetDesignExpression sheetDesignExpression : List) {	
			System.out.println("service sheetDesignExpression:"+sheetDesignExpression.getType());
			if(type.equals(sheetDesignExpression.getType().toString())){				
				DefaultTreeNode treeModel = conversionTreeOne(sheetDesignExpression);
				//利用递归层级加载子节点
				Collection<TreeNode> childCodeItemList = loadChildCodeItemList(List, treeModel.getCode());
				treeModel.setChildren(childCodeItemList);
				childTree.add(treeModel);		
			}
		}
		long j = System.currentTimeMillis();
		logger.debug("构建树形耗时---"+(j-i)+"-毫秒");
		return childTree;
	}
	
	@Override
	public List<DefaultTreeNode> conversionTree(Collection<SheetDesignExpression> list) {
		List<DefaultTreeNode> treeList = new ArrayList<DefaultTreeNode>();
		for (SheetDesignExpression sheetDesignExpression : list) {
			DefaultTreeNode tree = conversionTreeOne(sheetDesignExpression);
			treeList.add(tree);
		}
		return treeList;
	}
	
	private DefaultTreeNode conversionTreeOne(SheetDesignExpression sheetDesignExpression) {
		DefaultTreeNode treeModel = new DefaultTreeNode();
		treeModel.setChecked(false);
		treeModel.setId(sheetDesignExpression.getId());	
		treeModel.setCode(sheetDesignExpression.getName());	
		treeModel.setText(sheetDesignExpression.getContent());
		String typeName = ExpressionType.getTypeName( sheetDesignExpression.getType() ); //将数字转化为文字
		treeModel.setParentId(typeName);
		treeModel.setExpanded(false);
		return treeModel;
	}
    @Transactional
    @Override
    public int deleteByDesignId(UUID designId) {
        return sheetDesignExpressionDao.deleteByDesignId(designId);
    }

	@Override
	@Transactional
	public void updateByDesignId(SheetDesignExpression sheetDesignExpression) {
		try {
			
			SheetDesignExpression old = sheetDesignExpressionDao.getById(sheetDesignExpression.getId());
			BeanUtils.copyProperties(sheetDesignExpression,old); 
			sheetDesignExpressionDao.updateByDesignId(old);
		} catch (BeansException e) {
			logger.error("sheetDesignExpressionService error:{}", Throwables.getStackTraceAsString(e));
			throw  new RuntimeException("更新操作失败……");
		}
		
	}

	@Override
	@Transactional
	public UUID saveDesignExpression(SheetDesignExpression sheetDesignExpression) {
		
		return sheetDesignExpressionDao.saveDesignExpression(sheetDesignExpression);
	}
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignExpression> getByDesignId(UUID designId) {
		return sheetDesignExpressionDao.getByDesignId(designId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignExpression> getCheckFormulaByDesignId(UUID designId){
		return sheetDesignExpressionDao.getCheckFormulaByDesignId(designId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignExpression> getCaculationFormulaByDesignId(UUID designId){
		return sheetDesignExpressionDao.getCaculationFormulaByDesignId(designId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignExpression> getFillUnitFormulaByDesignId(UUID designId){
		return sheetDesignExpressionDao.getFillUnitFormulaByDesignId(designId);
	}
	
	
	@Override
	@Transactional
	public boolean moveUp(SheetDesignExpression entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignExpression curr = sheetDesignExpressionDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignExpression previous = sheetDesignExpressionDao.findPrevious(id,designId);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDesignExpressionDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignExpressionDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	@Transactional
	public boolean moveDown(SheetDesignExpression entity, UUID designId) {
		UUID id = entity.getId();
		SheetDesignExpression curr = sheetDesignExpressionDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignExpression nextvious = sheetDesignExpressionDao.findNext(id, designId);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDesignExpressionDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignExpressionDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
	

 
    
}