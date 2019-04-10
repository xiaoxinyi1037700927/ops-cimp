package com.sinosoft.ops.cimp.exception;

public interface OpsException {
    /**
     * 获取错误编码
     */
    String getErrorCode();

    /**
     * 获取错误详细信息
     */
    String getErrorDtlMsg();

    /**
     * 获取错误描述信息
     */
    String getErrorMsg();
}
