package com.sinosoft.ops.cimp.service.sheet.impl;

import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.service.BaseEntityServiceImpl;
import com.sinosoft.ops.cimp.entity.sheet.SheetConditionCategory;
import com.sinosoft.ops.cimp.repository.sheet.SheetConditionCategoryDao;
import com.sinosoft.ops.cimp.service.sheet.SheetConditionCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @description: 条件分类业务层实现
 */
@Service("sheetConditionCategoryService")
public class SheetConditionCategoryServiceImpl extends BaseEntityServiceImpl<SheetConditionCategory> implements SheetConditionCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(SheetConditionCategoryServiceImpl.class);

    @Autowired
    private SheetConditionCategoryDao sheetConditionCategoryDao;

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoryTree() {
        Collection<SheetConditionCategory> collection = sheetConditionCategoryDao.getRootData();
        List<Map<String, Object>> root = new ArrayList<Map<String, Object>>();
        //遍历root集合将RecursionCategory(sdsc)返回的集合放到root里面
        for (SheetConditionCategory sheetConditionCategory : collection) {
            root.add(RecursionCategory(sheetConditionCategory));
        }
        return root;
    }

    public Map<String, Object> RecursionCategory(SheetConditionCategory sheetConditionCategory) {

        Map map = new HashMap<String, Object>();
        map.put("id", sheetConditionCategory.getId());
        map.put("name", sheetConditionCategory.getName());
        //利用分组id查询子集
        Collection<SheetConditionCategory> childs = sheetConditionCategoryDao.findByParentId(sheetConditionCategory.getId());
        if (childs.size() > 0) {
            List<Map<String, Object>> childmap = new ArrayList<Map<String, Object>>();
            map.put("leaf", false);
            //遍历子集
            for (SheetConditionCategory child : childs) {
                childmap.add(RecursionCategory(child));//递归调用子集
            }
            map.put("child", childmap);
        } else {
            map.put("leaf", true);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoryTreeByIds(List<String> ids) {
        Collection<SheetConditionCategory> collection = sheetConditionCategoryDao.getRootDataByIds(ids);
        List<Map<String, Object>> root = new ArrayList<Map<String, Object>>();
        //遍历root集合将RecursionCategory(sdsc)返回的集合放到root里面
        for (SheetConditionCategory sheetConditionCategory : collection) {
            root.add(RecursionCategory(sheetConditionCategory, ids));
        }
        return root;
    }

    public Map<String, Object> RecursionCategory(SheetConditionCategory sheetConditionCategory, List<String> ids) {

        Map map = new HashMap<String, Object>();
        map.put("id", sheetConditionCategory.getId());
        map.put("name", sheetConditionCategory.getName());
        map.put("type", "Category");
        //利用分组id查询子集
        Collection<SheetConditionCategory> childs = sheetConditionCategoryDao.findByParentIdAndIds(sheetConditionCategory.getId(), ids);
        if (childs.size() > 0) {
            List<Map<String, Object>> childmap = new ArrayList<Map<String, Object>>();
            map.put("leaf", false);
            //遍历子集
            for (SheetConditionCategory child : childs) {
                childmap.add(RecursionCategory(child, ids));//递归调用子集
            }
            map.put("child", childmap);
        } else {
            map.put("leaf", false);
        }
        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<SheetConditionCategory> findAllChildren() {
        Collection<SheetConditionCategory> sheetConditionCategoryCollection = sheetConditionCategoryDao.findAllChildren();
        Collection<SheetConditionCategory> collection = new LinkedList<SheetConditionCategory>();
        Map<Integer, SheetConditionCategory> map = new HashMap<Integer, SheetConditionCategory>();
        for (SheetConditionCategory sheetConditionCategory : sheetConditionCategoryCollection) {
            sheetConditionCategory.setLeaf(false);
            map.put(Integer.parseInt(sheetConditionCategory.getId()), sheetConditionCategory);
        }
        for (Integer key : map.keySet()) {
            collection.add(map.get(key));
        }
        return collection;
    }

    @Override
    @Transactional
    public String save(SheetConditionCategory sheetConditionCategory) {

        return sheetConditionCategoryDao.save(sheetConditionCategory);
    }

    @Override
    @Transactional
    public String getFisrtId() {

        return sheetConditionCategoryDao.getFisrtId();
    }

    @Override
    @Transactional
    public void update(SheetConditionCategory sheetConditionCategory) {
        try {
            SheetConditionCategory old = sheetConditionCategoryDao.getById(sheetConditionCategory.getId());
            BeanUtils.copyProperties(sheetConditionCategory, old);
            sheetConditionCategoryDao.update(old);
        } catch (BeansException e) {
            logger.error("SheetConditionCategoryServiceImpl error:{}", Throwables.getStackTraceAsString(e));
            throw new RuntimeException("更新操作失败……");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SheetConditionCategory getById(String id) {

        return sheetConditionCategoryDao.getById(id);
    }

    @Override
    @Transactional
    public void deleteId(String Id) {
        sheetConditionCategoryDao.deleteId(Id);

    }

    @Override
    @Transactional
    public boolean moveUp(SheetConditionCategory entity) {
        String id = entity.getId();
        SheetConditionCategory curr = sheetConditionCategoryDao.getById(entity.getId());
        int ordinal = curr.getOrdinal();
        String userName = entity.getLastModifiedBy();
        SheetConditionCategory previous = sheetConditionCategoryDao.findPrevious(id);
        if (previous != null) {
            String preId = previous.getId();
            int preOrdinal = previous.getOrdinal();
            int cnt = sheetConditionCategoryDao.updateOrdinal(preId, ordinal, userName);
            if (cnt > 0) {
                cnt = sheetConditionCategoryDao.updateOrdinal(id, preOrdinal, userName);
                if (cnt > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean moveDown(SheetConditionCategory entity) {
        String id = entity.getId();
        SheetConditionCategory curr = sheetConditionCategoryDao.getById(entity.getId());
        int ordinal = curr.getOrdinal();
        String userName = entity.getLastModifiedBy();
        SheetConditionCategory nextvious = sheetConditionCategoryDao.findNext(id);
        if (nextvious != null) {
            String nextId = nextvious.getId();
            int nextOrdinal = nextvious.getOrdinal();
            int cnt = sheetConditionCategoryDao.updateOrdinal(nextId, ordinal, userName);
            if (cnt > 0) {
                cnt = sheetConditionCategoryDao.updateOrdinal(id, nextOrdinal, userName);
                if (cnt > 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
