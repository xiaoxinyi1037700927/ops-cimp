/**
 * @Project:      IIMP
 * @Title:          SheetCategoryServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetSheetCategory;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.repository.sheet.SheetCategoryDao;
import com.sinosoft.ops.cimp.service.sheet.SheetCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName:  SheetCategoryServiceImpl
 * @Description: 表格分类服务实现
 * @Author:        Nil
 * @Date:            2017年8月18日 下午1:26:15
 * @Version        1.0.0
 */
@Service("sheetCategoryService")
public class SheetCategoryServiceImpl extends BaseEntityServiceImpl<SheetCategory> implements SheetCategoryService {
	
	private static final Logger logger = LoggerFactory.getLogger(SheetCategoryServiceImpl.class);
	
	@Autowired
	private SheetCategoryDao sheetCategoryDao;
	
//	@Resource
//    private SysInfoCategoryService sysInfoCategoryService = null;

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetCategory> getRoot(){
		return sheetCategoryDao.getRoot();
	}
	
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
    public Collection<SheetCategory> getRootWithDescendants(String type,Integer param) {
        return (Collection<SheetCategory>) this.constructTree(sheetCategoryDao.getByType(type,param));
    }
    
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetCategory> getRootWithDescendantsByOrg(String type, Collection<String> organizationIds,Byte param) {
		
		Collection<SheetCategory> tree = (Collection<SheetCategory>)this.constructTree(sheetCategoryDao.getByTypeAndOrg(type,organizationIds,param));
		if("0".equals(type)){
			Map<UUID, SheetCategory> map = new HashMap<>();
			Collection<SheetCategory> root = sheetCategoryDao.getRootByTypeAndParam(type,param);
			for (SheetCategory sheetCategory : root) {
				for (SheetCategory category : tree) {
					if(category.getParentId() != null ){
						if(category.getParentId().equals(sheetCategory.getId())){
							if(sheetCategory.getChildren() == null){
								List<TreeNode> children = new ArrayList<>();
								children.add(category);
								sheetCategory.setChildren(children);
							}else{
								sheetCategory.getChildren().add(category);
							}
							
						}else{
							graduallyLoadParent1(sheetCategory, category, category,map);
						}
						
					}
				}
			}
			return root;
		}
		return tree;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetCategory> getWithDescendantsByOrg(String type, Collection<String> organizationIds,Byte param) {
		
		return (Collection<SheetCategory>) sheetCategoryDao.getByTypeAndOrg(type,organizationIds,param);
		
	}
	
	@Transactional(readOnly=true)
	public void graduallyLoadParent(SheetCategory root,SheetCategory tree,SheetCategory parent){
		
		SheetCategory parent1 = sheetCategoryDao.getById(parent.getParentId());
		if(parent1.getParentId() != null){
			if (parent1.getParentId().equals(root.getId())) {
				if(root.getChildren() == null){
					List<TreeNode> children = new ArrayList<>();
					children.add(tree);
					root.setChildren(children);
				}else{
					root.getChildren().add(tree);
				}
				
			}else{
				graduallyLoadParent(root,tree,parent1);
			}
		}else{
			if(parent1.getId().equals(root.getId())){
				List<TreeNode> children = new ArrayList<>();
				children.add(tree);
				root.setChildren(children);
			}
		}
	}
	
	@Transactional(readOnly=true)
	public void graduallyLoadParent1(SheetCategory root,SheetCategory tree,SheetCategory parent, Map<UUID, SheetCategory> map){
		SheetCategory parent1;
		if(map.containsKey(parent.getParentId())){
			parent1 = map.get(parent.getParentId());
		}else{
			parent1 = sheetCategoryDao.getById(parent.getParentId());
			if(parent1==null){
				logger.info("报表分类数据问题 id："+parent.getId().toString()+"parentId："+parent.getParentId().toString());
			}
			map.put(parent1.getId(), parent1);
		}
		
		if(parent1.getParentId() != null){
			if(parent1.getChildren() == null){
				List<TreeNode> c = new ArrayList<>();
				c.add(tree);
				parent1.setChildren(c);
			}else{
				if(!parent1.getChildren().contains(tree)){
					parent1.getChildren().add(tree);
				}
				
			}
			if (parent1.getParentId().equals(root.getId())) {
				
				if(root.getChildren() == null){
					List<TreeNode> children = new ArrayList<>();
					children.add(parent1);
					root.setChildren(children);
				}else{
					if(!root.getChildren().contains(parent1)){
						root.getChildren().add(parent1);
					}
					
				}
				
			}else{
				graduallyLoadParent1(root,parent1,parent1,map);
			}
		}else{
			if(parent1.getId().equals(root.getId())){
				if(parent1.getChildren() == null){
					List<TreeNode> c = new ArrayList<>();
					c.add(tree);
					parent1.setChildren(c);
				}else{
					if(!parent1.getChildren().contains(tree)){
						parent1.getChildren().add(tree);
					}
					
				}
				List<TreeNode> children = new ArrayList<>();
				children.add(parent1);
				root.setChildren(children);
			}
		}
	}
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SheetCategory> getChildren(UUID id){
        return sheetCategoryDao.getChildren(id);
    }

	@Override
	@Transactional(readOnly=true)
	public List<Map> getCategoryMessage(UUID parentId){
		return sheetCategoryDao.getCategoryMessage(parentId);
	}

	@Override
	@Transactional
	public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException {
	    SheetCategory o = sheetCategoryDao.getById(id);
		return writeObjectToFile(o, filename);
	}

	@Override
	@Transactional
	public SheetCategory importFromFile(String filename) throws FileNotFoundException, ClassNotFoundException, IOException {
		SheetCategory o = (SheetCategory)readObjectFromFile(filename);
		sheetCategoryDao.save(o);
		return o;
	}
	
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
    public Collection<TreeNode> getAllWithClassByType(Byte type) throws CloneException {
        Collection<TreeNode> collection = new LinkedList<TreeNode>();

//        Map<String,SysInfoCategory> map = new HashMap<String,SysInfoCategory>();
////        Collection<SysInfoCategory> categorys = sysInfoCategoryService.getAll();
////        for(SysInfoCategory c:categorys){
////            c.setNodeType("0");
////            map.put(c.getId(), c);
////        }
////        collection.addAll((Collection<? extends TreeModel>) sysInfoCategoryService.constructTree(categorys));
////
//        Collection<SheetCategory> l = sheetCategoryDao.getByType(type);
//        for(SheetCategory o:l){
//            o.setNodeType("1");
//            o.setText(o.getName());
//        }
//        for(SheetCategory o:(Collection<SheetCategory>)this.constructTree(l)){
//            if(o.getInfoCategoryId()==null||"".equals(o.getInfoCategoryId())){
//                collection.add(o);
//            }else{
//                SysInfoCategory c = map.get(o.getInfoCategoryId());
//                if(c!=null){
//                    c.getChildren().add(o);
//                }else{
//                    collection.add(o);
//                }
//            }
//        }
        return collection;
    }

	@Override
	@Transactional
	public UUID create(SheetCategory entity) {
		//增加同级节点
		SheetCategory dbEntity = null;
		if (entity.getId() != null) {
			dbEntity = sheetCategoryDao.getById(entity.getId());
		}
		int upOrdinal = 0;
		if (dbEntity != null && dbEntity.getOrdinal() != null && dbEntity.getOrdinal() > 0) {
			upOrdinal = dbEntity.getOrdinal();
		}
		UUID parentId = null;
		if (dbEntity != null) {
			parentId = dbEntity.getParentId();
		}
		entity.setOrdinal(getNextOrdinal());
		entity.setStatus((byte)0);
		if (entity.getType() == null) {
			entity.setType((byte)0);
		}
		if (entity.getCreatedTime() == null) {
			entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		}
		if (entity.getLastModifiedTime() == null) {
			entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
		}
    	UUID id = UUID.randomUUID();
    	entity.setId(id);
		if (entity.getParentId() != null) {
			//增加子节点
			parentId = entity.getParentId();
			int maxOrdinal = 0;
			Collection<SheetCategory> coll = sheetCategoryDao.getChildren(parentId);
			if (coll != null && coll.size() > 0) {
				for (SheetCategory aItem : coll) {
					if (aItem.getOrdinal() != null && aItem.getOrdinal() > maxOrdinal) {
						maxOrdinal = aItem.getOrdinal();
					}
				}
			}
			entity.setOrdinal(maxOrdinal + 1);
		}
		return sheetCategoryDao.save(entity);
	}

	@Override
	@Transactional
	public boolean updateName(SheetCategory entity) {
		return sheetCategoryDao.updateName(entity.getId(), entity.getName(), entity.getLastModifiedBy());
	}

	@Override
	@Transactional
	public boolean deleteLeaf(SheetCategory entity) {
		Collection<SheetCategory> col = sheetCategoryDao.getChildren(entity.getId());
		Assert.isTrue(CollectionUtils.isEmpty(col),"分类下有子节点不能删除");
		if (col.size() <= 0) {
			//不是叶节点
			Collection<SheetSheetCategory> list = sheetCategoryDao.findSheetByCategory(entity.getId());
			Assert.isTrue(CollectionUtils.isEmpty(list),"该分类有报表不能删除");
			if (list.size() <= 0) {
				//节点下没有报表
				SheetCategory dbEntity = sheetCategoryDao.getById(entity.getId());
				//后面的节点ordinal减1
				sheetCategoryDao.minusOrdinals(entity.getId(), dbEntity.getParentId(), entity.getLastModifiedBy());
				//节点删除
				sheetCategoryDao.deleteById(entity.getId());
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveUp(SheetCategory entity) {
		UUID id = entity.getId();
		SheetCategory curr = sheetCategoryDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetCategory previous = sheetCategoryDao.findPrevious(id);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetCategoryDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetCategoryDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveDown(SheetCategory entity) {
		UUID id = entity.getId();
		SheetCategory curr = sheetCategoryDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetCategory nextvious = sheetCategoryDao.findNext(id);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetCategoryDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetCategoryDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly=true)
	public String getTopOrgId(Collection<String> organizationIds,Integer reportType) {
		return sheetCategoryDao.getTopOrgId(organizationIds,reportType);
	}

}