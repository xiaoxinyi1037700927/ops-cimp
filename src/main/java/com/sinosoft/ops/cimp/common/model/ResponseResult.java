/**
 * @project: IIMP
 * @title: ResponseResult.java
 * @copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.common.model;

/**
 * @version 1.0.1.5
 * @ClassName: ResponseResult
 * @description: 响应结果
 * @author: Administrator
 * @date: 2017年8月2日 下午6:09:50
 */
public class ResponseResult {
    /*** 空执行结果 */
    public static final ResponseResult EMPTY = new ResponseResult(null, 0, "");

    /*** 操作成功 */
    public static final int ERROR_SUCCESS = 0;
    /*** 操作失败 */
    public static final int ERROR_FAILED = -1;
    /*** 验证未通过 */
    public static final int ERROR_VALIDATION_FAILED = -5;
    /*** 无效用户 */
    public static final int ERROR_INVALID_USER = -11;
    /*** 用户重复登录 */
    public static final int ERROR_REPEAT_LOGON = -12;
    /*** 用户已禁用 */
    public static final int ERROR_USER_DISABLED = -13;
    /*** 用户已锁定 */
    public static final int ERROR_USER_LOCKED = -15;
    /*** 无效的登录名或密码 */
    public static final int ERROR_INVALID_LOGINNAME_OR_PASSWORD = -17;
    /*** 无效验证码 */
    public static final int ERROR_INVALID_VALIDATE_CODE = -18;
    /*** 必须修改密码 */
    public static final int ERROR_MUST_CHANGE_PASSWORD = -19;

    /*** 无效会话 */
    public static final int ERROR_INVALID_SESSION = -21;
    /*** 审批失败 */
    public static final int ERROR_APPROVAL_FAILED = -41;

    /*** 未授权 */
    public static final int ERROR_UNAUTHORIZED = -401;
    /*** 内部服务器错误 */
    public static final int ERROR_INTERNAL_SERVER_ERROR = -500;

    /*** 是否成功 */
    private boolean success = true;
    /*** 错误代码 */
    private int errorCode = ERROR_SUCCESS;
    /*** 数据 */
    private Object data = null;
    /*** 数据数目 */
    private long count = 0;
    /*** 消息--格式为形如：“0:成功！”，错误代码和消息以冒号分隔 */
    private String message = "执行成功！";
    /*** 额外数据 */
    private Object extra;

    public ResponseResult() {
    }

    public ResponseResult(boolean success, int errorCode, Object data, long count, String message, Object extra) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
        this.count = count;
        this.message = message;
        this.extra = extra;
    }

    public ResponseResult(boolean success, int errorCode, Object data, long count, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.data = data;
        this.count = count;
        this.message = message;
    }

    public ResponseResult(boolean success, Object data, long count, String message) {
        this.success = success;
        this.data = data;
        this.count = count;
        this.message = message;
    }

    public ResponseResult(Object data, long count, String message) {
        this.data = data;
        this.count = count;
        this.message = message;
    }

    public ResponseResult(Object data, long count) {
        this.data = data;
        this.count = count;
    }

    /**
     * 生成成功的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult success() {
        return new ResponseResult(null, 0, "0:执行成功！");
    }

    /**
     * 生成成功的响应结果对象
     *
     * @param message 消息
     * @return 响应结果对象
     */
    public static ResponseResult success(final String message) {
        return new ResponseResult(null, 0, message);
    }

    /**
     * 生成成功的响应结果对象
     *
     * @param data  数据
     * @param count 数据数目
     * @return 响应结果对象
     */
    public static ResponseResult success(final Object data, final long count) {
        return new ResponseResult(data, count, "0:执行成功！");
    }


    /**
     * 生成成功的响应结果对象
     *
     * @param data    数据
     * @param count   数据数据
     * @param message 消息
     * @return 响应结果对象
     */
    public static ResponseResult success(final Object data, final long count, final String message) {
        return new ResponseResult(data, count, message);
    }

    /**
     * 生成未认证的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult unauthorized() {
        return new ResponseResult(false, ERROR_UNAUTHORIZED, null, 0, buildMessage(ERROR_UNAUTHORIZED, "操作失败！未被授权允许此项操作！"));
    }

    /**
     * 生成无效会话的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult invalidSession() {
        return new ResponseResult(false, ERROR_INVALID_SESSION, null, 0, buildMessage(ERROR_INVALID_SESSION, "无效会话！请重新登录！"));
    }

    /**
     * 生成会话过期的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult sessionExpired() {
        return new ResponseResult(false, ERROR_INVALID_SESSION, null, 0, buildMessage(ERROR_INVALID_SESSION, "会话失效！请重新登录！"));
    }

    /**
     * 生成内部服务器错误的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult internalServerError() {
        return new ResponseResult(false, ERROR_INTERNAL_SERVER_ERROR, null, 0, buildMessage(ERROR_INTERNAL_SERVER_ERROR, "请求失败！内部服务器错误！"));
    }

    /**
     * 生成未认证的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult unauthorize() {
        return new ResponseResult(false, 0, "1;请先登录！");
    }

    /**
     * 被踢出
     *
     * @return
     * @author Nil
     * @since JDK 1.7
     */
    public static ResponseResult kickout() {
        return new ResponseResult(false, 0, "1;用户已登录！");
    }

    /**
     * 生成无效的登录名响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult invalidLoginName() {
        return new ResponseResult(false, ERROR_INVALID_LOGINNAME_OR_PASSWORD, null, 0, buildMessage(ERROR_INVALID_LOGINNAME_OR_PASSWORD, "登录名或密码无效！"));
    }

    /**
     * 生成必须修改密码的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult mustChangePassword(String message) {
        return new ResponseResult(false, ERROR_MUST_CHANGE_PASSWORD, null, 0, buildMessage(ERROR_MUST_CHANGE_PASSWORD, message));
    }

    /**
     * 生成失败的响应结果对象
     *
     * @return 响应结果对象
     */
    public static ResponseResult failure() {
        return new ResponseResult(false, ERROR_FAILED, null, 0, buildMessage(ERROR_FAILED, "执行失败！"));
    }

    /**
     * 生成失败的响应结果对象
     *
     * @param message 消息
     * @return 响应结果对象
     */
    public static ResponseResult failure(final String message) {
        return new ResponseResult(false, ERROR_FAILED, null, 0, message);
    }

    /**
     * 生成失败的响应结果对象
     *
     * @param message 消息
     * @return 响应结果对象
     */
    public static ResponseResult failure(final int errorCode, final String message) {
        return new ResponseResult(false, errorCode, null, 0, buildMessage(errorCode, message));
    }

    /**
     * 根据错误代码和消息构建消息
     *
     * @param errorCode 错误代码
     * @param message   错误消息
     * @return
     */
    public static String buildMessage(final int errorCode, final String message) {
        return new StringBuilder().append(errorCode).append(":").append(message).toString();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
