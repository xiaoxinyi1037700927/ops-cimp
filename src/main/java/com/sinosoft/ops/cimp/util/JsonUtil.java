package com.sinosoft.ops.cimp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public final class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private JsonUtil() {
    }

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(defineDateFormat(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND));
    }

    /**
     * 设置时间转换格式
     */
    private static DateFormat defineDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }

    /**
     * 将对象转换成json字符串
     */
    public static <T> String getJsonString(T data) throws SystemException {
        return getJsonString(data, null);
    }


    public static <T> String getJsonString(T data, String pattern) throws SystemException {
        if (pattern != null && !StringUtils.isEmpty(pattern)) {
            objectMapper.setDateFormat(defineDateFormat(pattern));
        }
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.debug(ExceptionUtils.getStackTrace(e));
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, OpsErrorMessage.CONVERT_ERROR, "JSON");
        }
    }

    /**
     * 把字符串转换成对象
     */
    public static <T> T parseStringToObject(String jsonString, Class<T> clazz) throws SystemException {
        return parseStringToObject(jsonString, clazz, null);
    }

    /**
     * 把字符串转换成对象
     */
    public static <T> T parseStringToObject(String jsonString, Class<T> clazz, String pattern)
            throws SystemException {
        if (pattern != null && !StringUtils.isEmpty(pattern)) {
            objectMapper.setDateFormat(defineDateFormat(pattern));
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            log.debug(ExceptionUtils.getStackTrace(e));
            throw new SystemException(OpsErrorMessage.MODULE_NAME, e, OpsErrorMessage.CONVERT_ERROR, "JSON");
        }
    }

}