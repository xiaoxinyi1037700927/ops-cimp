package com.sinosoft.ops.cimp.util;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile() && file.delete()) ;
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static File createFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            createDir(path.substring(0, path.lastIndexOf("/")));
            file.createNewFile();
        }
        return file;
    }
}
