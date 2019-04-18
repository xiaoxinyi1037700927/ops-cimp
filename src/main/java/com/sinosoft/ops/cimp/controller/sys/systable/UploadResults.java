package com.sinosoft.ops.cimp.controller.sys.table;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadResults {

    private Map<String, List<FileSaveResult>> resultMap = new ConcurrentHashMap<>();

    /**
     * 分组数量
     */
    public int getGroupSize() {
        return resultMap.size();
    }

    /**
     * 获取分组
     */
    public List<FileSaveResult> getGroup(String groupName) {
        return resultMap.get(groupName);
    }

    /**
     * 放入文件
     */
    public List<FileSaveResult> put(String key, List<FileSaveResult> value) {
        return resultMap.put(key, value);
    }
}