package com.sinosoft.ops.cimp.util;

import java.io.File;

public class FileUtils {

    /**
     * 删除文件
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile() && file.delete()) ;
    }
}
