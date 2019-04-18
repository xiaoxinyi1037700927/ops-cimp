package com.sinosoft.ops.cimp.util.CachePackage;


import com.sinosoft.ops.cimp.entity.oraganization.Organization;
import com.sinosoft.ops.cimp.repository.oraganization.OrganizationRepository;
import com.sinosoft.ops.cimp.util.Cache;
import com.sinosoft.ops.cimp.util.CacheManager;
import com.sinosoft.ops.cimp.util.SpringContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganizationCacheManager {
    private static OrganizationCacheManager organizationCacheManager;

    private final String ORGANIZTION_KEY = "ORGANIZTION_KEY";

    /**
     * 获取到缓存单例实例
     *
     * @return
     */
    public static OrganizationCacheManager getSubject() {
        if (organizationCacheManager == null) {
            organizationCacheManager = new OrganizationCacheManager();
        }
        return organizationCacheManager;
    }

    /**
     * 根据机构ID获取机构信息
     *
     * @param Id 机构ID
     * @return
     */
    public Organization getOrganizationById(String Id) {
        if (Id == null || "".equals(Id)) {
            return null;
        }

        List<Organization> list = getAllList();
        List<Organization> list2 = list.stream()
                .filter(x -> Id.equals(x.getId())).collect(Collectors.toList());

        if (list2.size() == 0) {
            return null;
        }
        return list2.get(0);
    }

    /**
     * 根据机构CODE获取机构信息
     *
     * @param Code 机构Code
     * @return
     */
    public Organization getOrganizationByCode(String Code) {
        if (Code == null || "".equals(Code)) {
            return null;
        }

        List<Organization> list = getAllList();
        List<Organization> list2 = list.stream()
                .filter(x -> Code.equals(x.getCode())).collect(Collectors.toList());

        if (list2.size() == 0) {
            return null;
        }
        return list2.get(0);
    }

    /**
     * 获取机构全部信息
     * @return
     */
    public List<Organization> getAllList() {
        Cache cache = CacheManager.getCacheInfo(ORGANIZTION_KEY);
        if (cache == null) {
            List<Organization> list = getAllListByDB();
            return list;
        }

        Object obj = cache.getValue();
        if (obj instanceof List) {
            return (List<Organization>) obj;
        } else {
            return new ArrayList<Organization>();
        }
    }


    /**
     * 数据库内获取机构列表
     *
     * @return
     */
    public synchronized List<Organization> getAllListByDB() {
        OrganizationRepository repository = SpringContextUtils.getBean(OrganizationRepository.class);
        List<Organization> list = repository.findAll();
        Cache cache = new Cache();
        cache.setKey("organization");
        cache.setValue(list);
        cache.setTimeOut(1000 * 60 * 60 * 3 + System.currentTimeMillis());
        CacheManager.putCache(ORGANIZTION_KEY, cache);
        return list;
    }

    /**
     *清空机构缓存
     *
     * @return
     */
    public synchronized void  clear(){
        CacheManager.clearOnly(ORGANIZTION_KEY);
    }
}
