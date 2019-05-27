package com.sinosoft.ops.cimp.service.archive.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterial;
import com.sinosoft.ops.cimp.entity.archive.ArchiveMaterialCategory;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialCategoryRepository;
import com.sinosoft.ops.cimp.repository.archive.ArchiveMaterialRepository;
import com.sinosoft.ops.cimp.service.archive.ArchiveMaterialCategoryService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @ClassName: ArchiveMaterialCategoryServiceImpl
 */
@Service("archiveMaterialCategoryService")
public class ArchiveMaterialCategoryServiceImpl implements ArchiveMaterialCategoryService {
    /***MaterialCategory标识->MaterialCategory映射*/
    private static Map<String, ArchiveMaterialCategory> id2IMaterial = new ConcurrentHashMap<String, ArchiveMaterialCategory>();

    @Autowired
    private ArchiveMaterialCategoryRepository archiveMaterialCategoryRepository;
    @Autowired
    private ArchiveMaterialRepository archiveMaterialRepository;

    /*** 初始化缓存 */
    private void initializeCache() {
        if (id2IMaterial.isEmpty()) {
            synchronized (id2IMaterial) {
                if (id2IMaterial.isEmpty()) {
                    String prevInfoSetId = "";
                    String ID = "";
                    for (ArchiveMaterialCategory o : archiveMaterialCategoryRepository.findAll()) {
                        ID = o.getId();
                        if (!prevInfoSetId.equals(ID)) {
                            id2IMaterial.put(ID, o);
                            prevInfoSetId = o.getId();
                        }
                    }
                }
            }
        }
    }

    /*** 重置缓存 */
    private void resetCache() {
        id2IMaterial.clear();
    }


    @Override
    public void deleteById(Serializable id) {

    }

    @Override
    public Map<String, ArchiveMaterialCategory> getAll() {
        return null;
    }

    @Override
    @Transactional
    public Serializable create(ArchiveMaterialCategory entity) {
        Serializable primaryKey = archiveMaterialCategoryRepository.save(entity);
        resetCache();
        return primaryKey;
    }

    @Override
    @Transactional
    public void delete(ArchiveMaterialCategory entity) {
        archiveMaterialCategoryRepository.delete(entity);
        resetCache();
    }

    @Override
    public void update(ArchiveMaterialCategory entity) {

    }

    @Override
    @Transactional
    public void deleteById(String id) {
        archiveMaterialCategoryRepository.deleteById(id);
        resetCache();
    }


    @Override
    @Transactional(readOnly = true)
    public List<HashMap<String, Object>> getMaterialCategoryAndMaterial4Tree(String code, String empId, String categoryId) {
        List<ArchiveMaterialCategory> categoryList = null;
        if (StringUtil.isEmptyOrNull(code)) {
            categoryList = archiveMaterialCategoryRepository.findByParentCode();
        } else {
            categoryList = archiveMaterialCategoryRepository.findByParentCodeOrderBySn(code);
        }

        // System.out.println("empid==="+empId+"code=="+code+"categoryId="+categoryId);

        List<HashMap<String, Object>> materialCategoryAndMaterialList = new ArrayList<HashMap<String, Object>>();

        if (empId == null || empId == "") {
            if (categoryList != null && categoryList.size() > 0) {
                materialCategoryAndMaterialList = buildArchiveMaterialCategory(categoryList);

            }
        } else {
            if (categoryId != null) {
                List<ArchiveMaterialCategory> ArchiveMaterialCategorys = archiveMaterialCategoryRepository
                        .findArchiveMaterialCategorysByCategoryId(categoryId);
                if (ArchiveMaterialCategorys.size() > 0) {
                    materialCategoryAndMaterialList = buildArchiveMaterialCategory(ArchiveMaterialCategorys);

                }

                List<ArchiveMaterial> ds = archiveMaterialRepository.findAllByEmpIdAndCategoryIdOrderBySeqNum(empId, categoryId);

                System.out.println("dsds==" + ds.size());
                if (ds != null && ds.size() > 0) {
                    for (ArchiveMaterial ArchiveM : ds) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        System.out.println("ArchiveM.getArchiveId()=" + ArchiveM.getArchiveId() + "=="
                                + ArchiveM.getTitle() + "==" + ArchiveM.getCreateDate());
                        if (ArchiveM.getArchiveId() != null) {
                            map.put("id", ArchiveM.getId());
                        }
                        if (ArchiveM.getTitle() != null) {
                            map.put("text", ArchiveM.getTitle() + "(" + ArchiveM.getCreateDate() + ")");
                        }
                        map.put("code", "");
                        map.put("leaf", true);
                        map.put("qtip", ArchiveM.getTitle() + "(" + ArchiveM.getCreateDate() + ")");
                        map.put("count", ArchiveM.getPageCount());
                        // map1.put("parentCode", o.getCode());
                        map.put("archiveMaterialCategoryId", ArchiveM.getId());
                        materialCategoryAndMaterialList.add(map);
                        // map.put("assignedSn", o.getAssignedSn());
                    }
                }
            }
        }

        return materialCategoryAndMaterialList;
    }

    private List<HashMap<String, Object>> buildArchiveMaterialCategory(List<ArchiveMaterialCategory> categoryList) {
        List<HashMap<String, Object>> materialCategoryAndMaterialList = new ArrayList<HashMap<String, Object>>();

        for (ArchiveMaterialCategory o : categoryList) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            //map=new HashMap<String, Object>();
            //map.put("empId", "");
            map.put("id", o.getId());
            //boolean leaf=archiveMaterialCategoryDao.findleafyorn(o.getId());
            map.put("leaf", false);
            map.put("text", o.getName());
            map.put("code", o.getCode());
            map.put("qtip", o.getName());
            //map.put("children", map1);
            map.put("parentCode", o.getParentCode());
            map.put("archiveMaterialCategoryId", o.getId());
            map.put("assignedSn", o.getAssignedSn());

            materialCategoryAndMaterialList.add(map);
        }
        return materialCategoryAndMaterialList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getIdCardsByEmpid(String empId) {

        return archiveMaterialRepository.findByemp(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getidcardforone(String empId) {
        // TODO Auto-generated method stub
        return archiveMaterialRepository.findByemp001(empId);
    }

    @Override
    @Transactional(readOnly = true)
    public String findEmpidByUserID(String userid) {
        return archiveMaterialRepository.findEmpidByUserID(userid);
    }


}




