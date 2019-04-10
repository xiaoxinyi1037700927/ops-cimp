package com.sinosoft.ops.cimp.common;

import com.sinosoft.ops.cimp.exception.SystemException;
import com.sinosoft.ops.cimp.util.JsonUtil;

public final class ResponseHelper {

    private ResponseHelper() {
    }

    /**
     * 向前端响应JSON数据
     */
    public static String printJsonString(Object obj) throws SystemException {
        return JsonUtil.getJsonString(obj);
    }

}
