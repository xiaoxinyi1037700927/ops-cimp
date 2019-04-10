package com.sinosoft.ops.cimp.exception;


import com.vip.vjtools.vjkit.base.ExceptionUtil;

import java.util.List;

public class BusinessException extends Exception implements OpsException {

    private static final long serialVersionUID = -7422865553361251643L;
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

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorDtlMsg() {
        return this.errorMsg;
    }

    @Override
    public String getErrorMsg() {
        return this.getMessage();
    }

    public void seterrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * 构建业务异常
     *
     * @param moduleName     模块代码
     * @param t              异常信息
     * @param errorCode      错误代码
     * @param errorMsgParams 错误信息参数
     */
    public BusinessException(String moduleName, Throwable t, String errorCode, String... errorMsgParams) {
        this(moduleName, t, true, errorCode, errorMsgParams);
    }

    /**
     * 构建业务异常
     *
     * @param moduleName     模块代码
     * @param t              异常信息
     * @param cacheMessage   缓存错误信息标志
     * @param errorCode      错误代码
     * @param errorMsgParams 错误信息参数
     */
    public BusinessException(String moduleName, Throwable t, boolean cacheMessage, String errorCode, String... errorMsgParams) {
        super(t);
        String errorMessage = ExceptionConfig.mm(moduleName, cacheMessage, errorCode, errorMsgParams);
        this.errorCode = errorCode;
        this.errorMsg = errorMessage;
        this.moduleName = moduleName;
    }

    public BusinessException(String moduleName, List<BusinessException> businessExceptions) {
        this(moduleName, null, businessExceptions);
    }

    public BusinessException(String moduleName, Throwable t, List<BusinessException> businessExceptions) {
        super(t);
        StringBuilder errorMessage = new StringBuilder();
        if (businessExceptions != null && businessExceptions.size() > 0) {
            for (BusinessException businessException : businessExceptions) {
                errorMessage.append(businessException.getMessage()).append(";");
            }
        }
        this.errorMsg = errorMessage.toString();
        this.moduleName = moduleName;
    }

    /**
     * 业务异常构造函数
     *
     * @param moduleName 模块代码
     * @param errorCode  错误信息
     */
    public BusinessException(String moduleName, String errorCode) {
        this(moduleName, null, errorCode, (String[]) null);
    }


    /**
     * 业务异常构造函数
     *
     * @param moduleName 模块代码
     * @param t          异常源信息
     * @param errorCode  错误编码
     */
    public BusinessException(String moduleName, Throwable t, String errorCode) {
        this(moduleName, t, errorCode, (String[]) null);
    }

    /**
     * 业务异常构造函数
     *
     * @param moduleName     模块代码
     * @param errorCode      错误编码
     * @param errorMsgParams 错误信息参数
     */
    public BusinessException(String moduleName, String errorCode, String... errorMsgParams) {
        this(moduleName, null, errorCode, errorMsgParams);
    }

    @Override
    public String getMessage() {
        return moduleName + "-" + errorCode + ":" + errorMsg;
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

}
