package com.sinosoft.ops.cimp.util.CachePackage;

import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.dto.sys.table.SysTableModelInfoDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.Map;

public class SysTableModelInfoManager {

    private SysTableModelInfoManager() {
    }

    private static SysTableModelInfoManager sysTableModelInfoManager = new SysTableModelInfoManager();

    private static final Map<String, SysTableModelInfo> SYS_TABLE_MODEL_INFO = Maps.newConcurrentMap();
    private static final Map<String, SysTableModelInfoDTO> SYS_TABLE_MODEL_INFO_DTO = Maps.newConcurrentMap();

    public static SysTableModelInfoManager getInstance() {
        return sysTableModelInfoManager;
    }


    public SysTableModelInfo getSysTableModelInfo(String tableTypeName) {
        return SYS_TABLE_MODEL_INFO.get(tableTypeName);
    }

    public synchronized void setSysTableModeInfo(String tableTypeName, SysTableModelInfo sysTableModeInfo) {
        SYS_TABLE_MODEL_INFO.put(tableTypeName, sysTableModeInfo);
    }

    public synchronized void removeSysTableModelInfoCache(String tableTypeName) {
        Iterator<Map.Entry<String, SysTableModelInfo>> iterator = SYS_TABLE_MODEL_INFO.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SysTableModelInfo> next = iterator.next();
            String key = next.getKey();
            if (StringUtils.equals(tableTypeName, key)) {
                iterator.remove();
            }
        }
    }

    public SysTableModelInfoDTO getSysTableModelInfoDTO(String tableTypeName, String prjCode) {
        String cacheKey = tableTypeName + "_" + prjCode;
        return SYS_TABLE_MODEL_INFO_DTO.get(cacheKey);
    }

    public void setSysTableModelInfoDTO(String tableTypeName, String prjCode, SysTableModelInfoDTO sysTableModelInfoDTO) {
        String cacheKey = tableTypeName + "_" + prjCode;
        SYS_TABLE_MODEL_INFO_DTO.put(cacheKey, sysTableModelInfoDTO);
    }

    public synchronized void removeSysTableModelInfoDTO(String tableTypeName, String prjCode) {
        String cacheKey = tableTypeName + "_" + prjCode;
        Iterator<Map.Entry<String, SysTableModelInfoDTO>> iterator = SYS_TABLE_MODEL_INFO_DTO.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, SysTableModelInfoDTO> next = iterator.next();
            String key = next.getKey();
            if (StringUtils.equals(cacheKey, key)) {
                iterator.remove();
            }
        }
    }

    public static void removeAllCache() {
        SYS_TABLE_MODEL_INFO.entrySet().removeIf((e) -> true);
        SYS_TABLE_MODEL_INFO_DTO.entrySet().removeIf((e) -> true);
    }
}
