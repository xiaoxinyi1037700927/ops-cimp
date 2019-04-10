package com.sinosoft.ops.cimp.cache.file;


import com.vip.vjtools.vjkit.io.FileUtil;

import java.io.*;

/**
 * 文件序列化和反序列化
 */
public class FileSerializer {

    /**
     * 将对象序列化转换为字节数组
     *
     * @param filePath 文件路径
     * @param obj      需要序列化的对象
     */
    public static boolean serialize(String filePath, Object obj) {
        try {
            File file = new File(filePath);
            FileUtil.deleteFile(file);
            OutputStream outputStream = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            out.writeObject(obj);
            out.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("对象文件序列化保存错误", e);
        }
        return false;
    }

    /**
     * 将字节数组反序列化为对象
     *
     * @param filePath 字节数组
     * @return 数据对象
     */
    public static Object deSerialize(String filePath) {
        Object result = null;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
                result = objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("对象文件反序列化错误", e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("对象文件反序列化对应的类不存在", e);
            }
        }
        return result;
    }
}
