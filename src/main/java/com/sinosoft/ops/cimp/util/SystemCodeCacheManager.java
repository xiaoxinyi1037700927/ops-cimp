package com.sinosoft.ops.cimp.util;

import com.sinosoft.ops.cimp.entity.sys.user.SystemCode;
import com.sinosoft.ops.cimp.repository.user.SystemCodeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SystemCodeCacheManager {
    private static SystemCodeCacheManager systemCodeCacheManager;

    private final String SYSTEMCODE_KEY = "SYSTEMCODE_KEY";

    /**
     * 获取到字典缓存实例
     *
     * @return
     */
    public static SystemCodeCacheManager getSubject() {
        if (systemCodeCacheManager == null) {
            systemCodeCacheManager = new SystemCodeCacheManager();
        }
        return systemCodeCacheManager;
    }

    /**
     * 根据字典ID获取机构信息
     *
     * @param Id 字典ID
     * @return
     */
    public SystemCode getSystemCodeById(String Id) {
        if (Id == null || "".equals(Id)) {
            return null;
        }

        List<SystemCode> list = getAllList();
        List<SystemCode> list2 = list.stream()
                .filter(x -> Id.equals(x.getId())).collect(Collectors.toList());

        if (list2.size() == 0) {
            return null;
        }
        return list2.get(0);
    }

    /**
     * 根据字典ID获取对应名称
     *
     * @param Id 字典ID
     * @return
     */
    public String getSystemCodeNameById(String Id) {
        if (Id == null || "".equals(Id)) {
            return null;
        }

        List<SystemCode> list = getAllList();
        List<SystemCode> list2 = list.stream()
                .filter(x -> Id.equals(x.getId())).collect(Collectors.toList());

        if (list2.size() == 0) {
            return null;
        }
        return list2.get(0).getName();
    }

    /**
     * 根据字典typeCODE获取字典列表
     *
     * @param typeCode 字典typecode
     * @return
     */
    public List<SystemCode> getSystemCodeListByTypeCode(String typeCode) {
        if (typeCode == null || "".equals(typeCode)) {
            return new ArrayList<SystemCode>();
        }

        List<SystemCode> list = getAllList();
        List<SystemCode> list2 = list.stream()
                .filter(x -> typeCode.equals(x.getTypeCode())).collect(Collectors.toList());

        return list2;
    }

    /**
     * 获取机构全部信息
     *
     * @return
     */
    public List<SystemCode> getAllList() {
        Cache cache = CacheManager.getCacheInfo(SYSTEMCODE_KEY);
        if (cache == null) {
            List<SystemCode> list = getAllListByDB();
            return list;
        }

        Object obj = cache.getValue();
        if (obj instanceof List) {
            return (List<SystemCode>) obj;
        } else {
            return new ArrayList<SystemCode>();
        }
    }


    /**
     * 数据库内获取机构列表
     *
     * @return
     */
    public synchronized List<SystemCode> getAllListByDB() {
        SystemCodeRepository repository = SpringContextUtils.getBean(SystemCodeRepository.class);
        List<SystemCode> list = repository.findAll();
        Cache cache = new Cache();
        cache.setKey("systemCode");
        cache.setValue(list);
        cache.setTimeOut(1000 * 60 * 60 * 3 + System.currentTimeMillis());
        CacheManager.putCache(SYSTEMCODE_KEY, cache);
        return list;
    }

    /**
     * 清空字典缓存
     *
     * @return
     */
    public synchronized void clear() {
        CacheManager.clearOnly(SYSTEMCODE_KEY);
    }

}
