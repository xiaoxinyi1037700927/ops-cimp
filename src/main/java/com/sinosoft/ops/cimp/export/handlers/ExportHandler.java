package com.sinosoft.ops.cimp.export.handlers;

public interface ExportHandler {
    /**
     * 生成文件
     */
    boolean generate() throws Exception;

    /**
     * 获取生成的文件路径
     */
    String getFilePath() throws Exception;

    /**
     * 生成的文件是否重复使用
     */
    default boolean isReuse() {
        return true;
    }
}
