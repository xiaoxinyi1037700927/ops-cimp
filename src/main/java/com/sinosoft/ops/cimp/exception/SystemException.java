package com.sinosoft.ops.cimp.exception;


import com.vip.vjtools.vjkit.base.ExceptionUtil;

public class SystemException extends RuntimeException implements OpsException {
    private static final long serialVersionUID = -7178656989706344962L;
    /**
     * 错误编码
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 错误模块
     */
    private String moduleName;

    /**
     * 系统异常构建
     *
     * @param moduleName     模块编码
     * @param t              异常源信息
     * @param errorCode      错误代码
     * @param errorMsgParams 错误消息参数
     */
    public SystemException(String moduleName, Throwable t, String errorCode, String... errorMsgParams) {
        this(moduleName, t, true, errorCode, errorMsgParams);
    }


    /**
     * 系统异常构建
     *
     * @param moduleName     模块编码
     * @param t              异常源信息
     * @param cacheMessage   缓存错误信息标志
     * @param errorCode      错误代码
     * @param errorMsgParams 错误消息参数
     */
    public SystemException(String moduleName, Throwable t, boolean cacheMessage, String errorCode, String... errorMsgParams) {
        super(t);
        String errorMessage = ExceptionConfig.mm(moduleName, cacheMessage, errorCode, errorMsgParams);
        this.errorCode = errorCode;
        this.errorMsg = errorMessage;
        this.moduleName = moduleName;
    }

    /**
     * 系统异常构造函数
     *
     * @param moduleName 模块代码
     * @param errorCode  错误信息
     */
    public SystemException(String moduleName, String errorCode) {
        this(moduleName, null, errorCode, (String[]) null);
    }


    /**
     * 系统异常构造函数
     *
     * @param moduleName 模块代码
     * @param errorCode  错误信息
     * @param t          异常源
     */
    public SystemException(String moduleName, Throwable t, String errorCode) {
        this(moduleName, t, errorCode, null);
    }

    /**
     * 系统异常构造函数
     *
     * @param moduleName     错误模块
     * @param errorCode      错误信息
     * @param errorMsgParams 异常源
     */
    public SystemException(String moduleName, String errorCode, String... errorMsgParams) {
        this(moduleName, null, errorCode, errorMsgParams);
    }

    /**
     * 输出异常堆栈信息
     *
     * @param e 异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable e) {
        return ExceptionUtil.stackTraceText(e);
    }

    @Override
    public String getMessage() {
        return moduleName + "-" + errorCode + ":" + errorMsg;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorDtlMsg() {
        return this.getMessage();
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
