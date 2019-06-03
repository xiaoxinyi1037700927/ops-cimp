package com.sinosoft.ops.cimp.common.model;

import java.io.Serializable;

/**
 * 执行结果
 */
public class ExecutionResultNoUsed implements Serializable {
    private static final long serialVersionUID = -4905739825567400423L;
    /*** 是否成功 */
    protected boolean success = false;
    /*** 数据记录数 */
    protected int count = 0;
    /*** 消息 */
    protected String message = null;
    /*** 数据 */
    protected Object data = null;

    /***其它选项*/
    //protected Map<String,Object> options = new HashMap<String,Object>();
    public ExecutionResultNoUsed() {
    }

    public ExecutionResultNoUsed(boolean successful) {
        this.success = successful;
    }

    public ExecutionResultNoUsed(boolean successful, final int count) {
        this.success = successful;
        this.count = count;
    }

    public ExecutionResultNoUsed(boolean successful, final int count, final String message) {
        this.success = successful;
        this.count = count;
        this.message = message;
    }

    public ExecutionResultNoUsed(boolean successful, final int count, final String message, final Object data) {
        this.success = successful;
        this.count = count;
        this.message = message;
        this.data = data;
    }

    /**
     * 生成成功结果
     *
     * @return 成功结果
     */
    public static ExecutionResultNoUsed success() {
        return new ExecutionResultNoUsed(true, 0);
    }

    /**
     * 生成成功结果
     *
     * @param datas 数据
     * @return 成功结果
     */
    public static ExecutionResultNoUsed success(Object data) {
        return new ExecutionResultNoUsed(true, 0, "", data);
    }

    /**
     * 生成成功结果
     *
     * @param message 消息
     * @return 成功结果
     */
    public static ExecutionResultNoUsed success(final String message) {
        return new ExecutionResultNoUsed(true, 0, message);
    }

    /**
     * 生成失败对象
     *
     * @return 失败对象
     */
    public ExecutionResultNoUsed failure() {
        return new ExecutionResultNoUsed();
    }

    public static ExecutionResultNoUsed failure(final int errorCode) {
        return new ExecutionResultNoUsed(false, errorCode);
    }

    public static ExecutionResultNoUsed failure(final int errorCode, final String message) {
        return new ExecutionResultNoUsed(false, errorCode, message);
    }

    /**
     * 生成失败对象
     *
     * @param message 消息
     * @return 失败结果
     */
    public static ExecutionResultNoUsed failure(final String message) {
        return new ExecutionResultNoUsed(false, 0, message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setDatas(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("IsSuccess=").append(success)
                .append(" Count=").append(count)
                .append(" Message=").append(message)
                .append(" Data=").append(data)
                .toString();
    }
}
