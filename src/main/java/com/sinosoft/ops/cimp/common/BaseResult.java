package com.sinosoft.ops.cimp.common;

import java.io.Serializable;
import java.util.List;

public class BaseResult implements Serializable {
    private static final long serialVersionUID = -8435964772335325421L;

    private static final String SUCCESS = "操作成功";
    private static final String FAIL = "操作失败";

    private int code;
    private Object data;
    private boolean isPageData;
    private Page page;
    private String message;
    private List<Error> errors;

    public static BaseResult ok() {
        return createResult(BaseResultHttpStatus.SUCCESS, null, false, null, SUCCESS, null);
    }

    public static BaseResult ok(Object data) {
        return createResult(BaseResultHttpStatus.SUCCESS, data, false, null, SUCCESS, null);
    }

    public static BaseResult ok(Object data, Page page) {
        return createResult(BaseResultHttpStatus.FAIL, data, true, page, SUCCESS, null);
    }

    public static BaseResult ok(Object data, Page page, String message) {
        return createResult(BaseResultHttpStatus.FAIL, data, true, page, message, null);
    }

    public static BaseResult fail() {
        return createResult(BaseResultHttpStatus.FAIL, null, false, null, FAIL, null);
    }

    public static BaseResult fail(String message) {
        return createResult(BaseResultHttpStatus.FAIL, null, false, null, message, null);
    }

    public static BaseResult fail(List<Error> errors) {
        return createResult(BaseResultHttpStatus.FAIL, null, false, null, FAIL, errors);
    }

    public static BaseResult fail(BaseResultHttpStatus status, String message) {
        return createResult(status, null, false, null, message, null);
    }

    public static BaseResult fail(BaseResultHttpStatus status, List<Error> errors) {
        return createResult(status, null, false, null, FAIL, errors);
    }


    private static BaseResult createResult(BaseResultHttpStatus status, Object data, boolean isPageData, Page page, String message, List<Error> errors) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(status.getStatus());
        baseResult.setData(data);
        baseResult.setPageData(isPageData);
        baseResult.setPage(page);
        baseResult.setMessage(message);
        baseResult.setErrors(errors);
        return baseResult;
    }

    public static class Page implements Serializable {
        private static final long serialVersionUID = 7792522516845036266L;
        private int total;
        private int startPage;
        private int pageSize;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getStartPage() {
            return startPage;
        }

        public void setStartPage(int startPage) {
            this.startPage = startPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void of(int total, int startPage, int pageSize) {
            this.total = total;
            this.startPage = startPage;
            this.pageSize = pageSize;
        }
    }

    public static class Error implements Serializable {
        private static final long serialVersionUID = 5481065033612389212L;
        private String field;
        private String message;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isPageData() {
        return isPageData;
    }

    public void setPageData(boolean pageData) {
        isPageData = pageData;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static String getSUCCESS() {
        return SUCCESS;
    }

    public static String getFAIL() {
        return FAIL;
    }
}
