/**
 * @Project:      IIMP
 * @Title:          SheetCategoryServiceImpl.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.service.sheet.impl;


import com.sinosoft.ops.cimp.common.model.TreeNode;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignCategory;
import com.sinosoft.ops.cimp.entity.sheet.SheetDesignDesignCategory;
import com.sinosoft.ops.cimp.exception.CloneException;
import com.sinosoft.ops.cimp.repository.sheet.SheetDesignCategoryDao;
import com.sinosoft.ops.cimp.service.sheet.SheetDesignCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service("sheetDesignCategoryService")
public class SheetDesignCategoryServiceImpl extends BaseEntityServiceImpl<SheetDesignCategory> implements SheetDesignCategoryService {
	@Autowired
	private SheetDesignCategoryDao sheetDesignCategoryDao;

//	@Resource
//    private SysInfoCategoryService sysInfoCategoryService = null;

	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignCategory> getRoot(){
		return sheetDesignCategoryDao.getRoot();
	}
	
    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly=true)
    public Collection<SheetDesignCategory> getRootWithDescendants() {
        return (Collection<SheetDesignCategory>) super.constructTree(sheetDesignCategoryDao.findAllByOrdinal());
    }

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true)
	public Collection<SheetDesignCategory> getAllCategory() {
		return (Collection<SheetDesignCategory>) thisConstructTree(sheetDesignCategoryDao.findAllByOrdinal());
	}

	/**
	 * 形成树结构 全部存在下级
	 */
	public Collection<? extends TreeNode> thisConstructTreeOrg(Collection<? extends TreeNode> collection){
		//根节点
		Collection<TreeNode> root = new LinkedList<TreeNode>();
		//标识和对象映射表
		Map<Object,TreeNode> id2Object = new HashMap<Object,TreeNode>(collection.size());
		//未找到父节点的对象集
		Collection<TreeNode> notFoundParent = new LinkedList<TreeNode>();

		//遍历找父节点
		for(TreeNode o:collection){
			o.setLeaf(false);
			id2Object.put(o.getId(), o);
			Object parentId = o.getParentId();
			if(parentId==null||"".equals(parentId)||"ROOT".equals(parentId)){
				root.add(o);
			}else{
				TreeNode parent = id2Object.get(o.getParentId());
				if(parent!=null){
					parent.getChildren().add(o);
				}else{
					notFoundParent.add(o);
				}
			}
		}
		//处理未找到父节点的
		for(TreeNode o:notFoundParent){
			TreeNode parent = id2Object.get(o.getParentId());
			if(parent!=null){
				parent.getChildren().add(o);
			}else{
				root.add(o);
			}
		}
		//将不必须的属性置空
		for (Map.Entry<Object,TreeNode> entry : id2Object.entrySet()) {
			if(entry.getValue().getChildren()!=null&&entry.getValue().getChildren().isEmpty()){
				entry.getValue().setChildren(null);
			}
			entry.getValue().setParentId(null);
		}
		return root;
	}
    
    //根据集合构造树结构
    private Collection<? extends TreeNode> thisConstructTree(Collection<? extends TreeNode> collection){
    	if (collection == null) {
    		return null;
    	}
        //标识和对象映射表
        Map<Object,TreeNode> id2Object = new HashMap<Object,TreeNode>(collection.size());
        //根节点
        Collection<TreeNode> root = new LinkedList<TreeNode>();
        //准备节点Map
        for (TreeNode o:collection) {
            id2Object.put(o.getId(), o);
        }
        //构造根结点集合
        for (TreeNode o:collection) {
            Object parentId = o.getParentId();
            if (parentId == null || "".equals(parentId) || "ROOT".equals(parentId) || id2Object.get(parentId) == null) {
            	o.setLeaf(false);
            	o.setChecked(false);
                root.add(o);
            }
        }
        root = addChildren(root, root, collection);
        //将不必须的属性置空
        for (Map.Entry<Object,TreeNode> entry : id2Object.entrySet()) {
            if (entry.getValue().getChildren() != null&&entry.getValue().getChildren().isEmpty()) {
                entry.getValue().setChildren(null);
            }
            entry.getValue().setParentId(null);
        }
        return root;
    }
    
    //递归查子节点
    private Collection<TreeNode> addChildren(Collection<TreeNode> root, Collection<TreeNode> thisLayer, Collection<? extends TreeNode> collection) {
    	if (thisLayer != null) {
	        for(TreeNode upItem : thisLayer){
	        	Object upId = upItem.getId();
	        	if (upId != null) {
	                for(TreeNode colItem :collection){
		            	if (upId.equals(colItem.getParentId())) {
		            		colItem.setLeaf(false);
		            		colItem.setChecked(false);
		            		upItem.getChildren().add(colItem);
		            		upItem.setLeaf(false);
		            		upItem.setChecked(false);
		            	}
		        	}
	            }
	        	root = addChildren(root, upItem.getChildren(), collection);
	        }
    	}
        return root;
    }
    
    @Override
    @Transactional(readOnly=true)
    public Collection<SheetDesignCategory> getChildren(UUID id){
        return sheetDesignCategoryDao.getChildren(id);
    }

	@Override
	@Transactional
	public boolean exportToFile(UUID id, String filename) throws FileNotFoundException, IOException {
	    SheetDesignCategory o = sheetDesignCategoryDao.getById(id);
		return writeObjectToFile(o, filename);
	}

	@Override
	@Transactional
	public SheetDesignCategory importFromFile(String filename) throws FileNotFoundException, ClassNotFoundException, IOException {
		SheetDesignCategory o = (SheetDesignCategory)readObjectFromFile(filename);
		sheetDesignCategoryDao.save(o);
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
//        Collection<SheetDesignCategory> l = sheetDesignCategoryDao.getByType(type);
//        for(SheetDesignCategory o:l){
//            o.setNodeType("1");
//            o.setText(o.getName());
//        }
//        for(SheetDesignCategory o:(Collection<SheetDesignCategory>)this.constructTree(l)){
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
	public UUID create(SheetDesignCategory entity) {
		//增加同级节点
		SheetDesignCategory dbEntity = null;
		if (entity.getId() != null) {
			dbEntity = sheetDesignCategoryDao.getById(entity.getId());
		}
		int upOrdinal = 0;
		if (dbEntity != null && dbEntity.getOrdinal() != null && dbEntity.getOrdinal() > 0) {
			upOrdinal = dbEntity.getOrdinal();
		}
		UUID parentId = null;
		if (dbEntity != null) {
			parentId = dbEntity.getParentId();
		}
		if (entity.getId() == null && entity.getParentId() == null) {
			//同层节点和父节点都没传入，在最后增加根节点
			Collection<SheetDesignCategory> coll = sheetDesignCategoryDao.getChildren(null);
			if (coll != null && coll.size() > 0) {
				for (SheetDesignCategory aItem : coll) {
					if (aItem.getOrdinal() != null && aItem.getOrdinal() > upOrdinal) {
						upOrdinal = aItem.getOrdinal();
					}
				}
			}
		}
		entity.setOrdinal(upOrdinal + 1);
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
			Collection<SheetDesignCategory> coll = sheetDesignCategoryDao.getChildren(parentId);
			if (coll != null && coll.size() > 0) {
				for (SheetDesignCategory aItem : coll) {
					if (aItem.getOrdinal() != null && aItem.getOrdinal() > maxOrdinal) {
						maxOrdinal = aItem.getOrdinal();
					}
				}
			}
			entity.setOrdinal(maxOrdinal + 1);
		} else {
			//增加同级节点
			sheetDesignCategoryDao.addOrdinals(upOrdinal, parentId, entity.getLastModifiedBy());
		}
		return sheetDesignCategoryDao.save(entity);
	}

	@Override
	@Transactional
	public boolean updateName(SheetDesignCategory entity) {
		return sheetDesignCategoryDao.updateName(entity.getId(), entity.getName(), entity.getLastModifiedBy());
	}

	@Override
	@Transactional
	public boolean deleteLeaf(SheetDesignCategory entity) {
		Collection<SheetDesignCategory> col = sheetDesignCategoryDao.getChildren(entity.getId());
		if (col.size() <= 0) {
			//不是叶节点
			Collection<SheetDesignDesignCategory> list = sheetDesignCategoryDao.findDesignByCategory(entity.getId());
			if (list.size() <= 0) {
				//节点下没有报表
				SheetDesignCategory dbEntity = sheetDesignCategoryDao.getById(entity.getId());
				//后面的节点ordinal减1
				sheetDesignCategoryDao.minusOrdinals(entity.getId(), dbEntity.getParentId(), entity.getLastModifiedBy());
				//节点删除
				sheetDesignCategoryDao.deleteById(entity.getId());
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveUp(SheetDesignCategory entity) {
		UUID id = entity.getId();
		SheetDesignCategory curr = sheetDesignCategoryDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignCategory previous = sheetDesignCategoryDao.findPrevious(id);
		if (previous != null) {
			UUID preId = previous.getId();
			int preOrdinal = previous.getOrdinal();
			int cnt = sheetDesignCategoryDao.updateOrdinal(preId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignCategoryDao.updateOrdinal(id, preOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean moveDown(SheetDesignCategory entity) {
		UUID id = entity.getId();
		SheetDesignCategory curr = sheetDesignCategoryDao.getById(entity.getId());
		int ordinal = curr.getOrdinal();
		UUID userName = entity.getLastModifiedBy();
		SheetDesignCategory nextvious = sheetDesignCategoryDao.findNext(id);
		if (nextvious != null) {
			UUID nextId = nextvious.getId();
			int nextOrdinal = nextvious.getOrdinal();
			int cnt = sheetDesignCategoryDao.updateOrdinal(nextId, ordinal, userName);
			if (cnt > 0) {
				cnt = sheetDesignCategoryDao.updateOrdinal(id, nextOrdinal, userName);
				if (cnt > 0) {
					return true;
				}
			}
		}
		return false;
	}
}