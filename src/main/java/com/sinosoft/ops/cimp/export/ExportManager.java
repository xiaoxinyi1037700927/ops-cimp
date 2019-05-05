package com.sinosoft.ops.cimp.export;

import com.sinosoft.ops.cimp.export.handlers.ExportHandler;
import com.sinosoft.ops.cimp.util.FileUtils;

import java.util.HashMap;
import java.util.Map;

public class ExportManager {
    private static Map<String, Long> map = new HashMap<>();

    public static String generate(ExportHandler handler) throws Exception {
        String filePath = handler.getFilePath();

        //如果目标文件正在生成，直接返回，否则存入map中
        if (!put(filePath)) {
            return null;
        }

        try {
            //如果目标文件不存在,生成目标文件
            if (!handler.isReuse() || !FileUtils.fileExists(filePath)) {
                if (!handler.generate()) {
                    return null;
                }
            }
        } finally {
            remove(filePath);
        }

        return filePath;
    }

    private synchronized static boolean put(String key) {
        if (map.containsKey(key)) {
            return false;
        } else {
            map.put(key, System.currentTimeMillis());
            return true;
        }
    }

    private synchronized static void remove(String key) {
        map.remove(key);
    }

}
