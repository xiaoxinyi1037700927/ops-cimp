package com.sinosoft.ops.cimp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.common.BaseResultHttpStatus;
import com.sinosoft.ops.cimp.common.model.Constants;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.UUID;

@SuppressWarnings("unchecked")
public abstract class BaseController {

    public static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 映射路径--创建
     */
    public static final String MAPPING_PATH_CREATE = "/create";
    /**
     * 映射路径--修改
     */
    public static final String MAPPING_PATH_UPDATE = "/update";
    /**
     * 映射路径--删除
     */
    public static final String MAPPING_PATH_DELETE = "/delete";
    /**
     * 映射路径--根据Id修改
     */
    public static final String MAPPING_PATH_DELETE_BY_ID = "/deleteById";
    /**
     * 映射路径--根据Id获取
     */
    public static final String MAPPING_PATH_GET_BY_ID = "/getById";
    /**
     * 映射路径--分页查询
     */
    public static final String MAPPING_PATH_FIND_BY_PAGE = "/findByPage";

    public ResponseEntity ok(Object data, BaseResult.Page page) throws BusinessException, SystemException {
        return ResponseEntity.ok(BaseResult.ok(data, page));
    }

    public ResponseEntity ok(Object data) throws BusinessException, SystemException {
        return ResponseEntity.ok(BaseResult.ok(data));
    }

    public ResponseEntity fail(String message) throws BusinessException, SystemException {
        return new ResponseEntity(BaseResult.fail(message), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity fail(String message, BaseResultHttpStatus status) throws BusinessException, SystemException {
        if (status.getStatus() == BaseResultHttpStatus.FAIL.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.BAD_REQUEST);
            }
        }
        if (status.getStatus() == BaseResultHttpStatus.NOT_FOUND.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.NOT_FOUND);
            }
        }
        if (status.getStatus() == BaseResultHttpStatus.UNAUTHORIZED.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.UNAUTHORIZED);
            }
        }
        if (status.getStatus() == BaseResultHttpStatus.INTERNAL_SERVER_ERROR.getStatus()) {
            if (message != null && StringUtils.isNotEmpty(message)) {
                return new ResponseEntity(BaseResult.fail(status, message), HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity(BaseResult.fail(status, BaseResultHttpStatus.FAIL.getDesc()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity(BaseResult.fail(message), HttpStatus.BAD_REQUEST);
    }

    /**
     * 写入JSON格式数据（返回给客户端）
     *
     * @param response HTTPResponse对象
     * @param data     数据
     */
    protected void writeJson(HttpServletResponse response, Object data) {
        try (PrintWriter writer = response.getWriter()) {
            String json = JSON.toJSONString(data, SerializerFeature.WriteDateUseDateFormat);
            System.out.println("json=" + json);
            // 设置信息类型、编码格式和禁用缓存
            response.setContentType("application/json;charset=utf-8");
            response.setHeader("cache-Control", "no-cache");
            response.setCharacterEncoding("utf-8");
            writer.print(json);
            writer.flush();
        } catch (Exception e) {
            logger.error("写入JSON数据失败！" + data, e);
        }
    }

    /**
     * 获取Boolean类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public Boolean getBooleanParam(HttpServletRequest request, String paramName, Boolean nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                return Boolean.valueOf(value);
            } catch (Exception e) {
                // logger.error("从request中获取Boolean类型参数失败！"+paramName+"="+value,e);
            }
        }
        return nullValue;
    }

    /**
     * 获取Boolean类型的请求参数值，参数不存在返回false
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @return 参数值
     */
    public Boolean getBooleanParam(HttpServletRequest request, String paramName) {
        return getBooleanParam(request, paramName, false);
    }

    /**
     * 获取Byte类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public Byte getByteParam(HttpServletRequest request, String paramName, Byte nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                return Byte.valueOf(value);
            } catch (Exception e) {
                logger.error("从request中获取Byte类型参数失败！"+paramName+"="+value,e);
            }
        }
        return nullValue;
    }

    /**
     * 获取Byte类型的请求参数值，参数值为空时返回-1
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public Byte getByteParam(HttpServletRequest request, String paramName) {
        return getByteParam(request, paramName, (byte) -1);
    }

    /**
     * 获取Float类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public Float getFloatParam(HttpServletRequest request, String paramName, Float nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                return Float.valueOf(value);
            } catch (Exception e) {
                logger.error("从request中获取Float类型参数失败！"+paramName+"="+value,e);
            }
        }
        return nullValue;
    }
    /**
     * 获取Float类型的请求参数值，参数值为空时返回-1
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @return 参数值
     */
    public Float getFloatParam(HttpServletRequest request, String paramName) {
        return getFloatParam(request, paramName, 0f);
    }

    /**
     * 获取Integer类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public Integer getIntegerParam(HttpServletRequest request, String paramName, Integer nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                return Integer.valueOf(value);
            } catch (Exception e) {
                logger.error("从request中获取Integer类型参数失败！"+paramName+"="+value,e);
            }
        }
        return nullValue;
    }

    /**
     * 获取Integer类型的请求参数值，参数值为空时返回-1
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @return 参数值
     */
    public Integer getIntegerParam(HttpServletRequest request, String paramName) {
        return getIntegerParam(request, paramName, -1);
    }

    /**
     * 获取Long类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public Long getLongParam(HttpServletRequest request, String paramName, Long nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                return Long.valueOf(value);
            } catch (Exception e) {
                logger.error("从request中获取Long类型参数失败！"+paramName+"="+value,e);
            }
        }
        return nullValue;
    }

    /**
     * 获取Long类型的请求参数值，参数值为空时返回-1
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @return 参数值
     */
    public Long getLongParam(HttpServletRequest request, String paramName) {
        return getLongParam(request, paramName, (long) -1);
    }

    /**
     * 获取UUID类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public UUID getUUIDParam(HttpServletRequest request, String paramName, UUID nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            try {
                return UUID.fromString(value);
            } catch (Exception e) {
                logger.error("从request中获取UUID类型参数失败！"+paramName+"="+value,e);
            }
        }
        return nullValue;
    }

    /**
     * 获取UUID类型的请求参数值，如果值不存在或为空，返回零值UUID，即“000000000000-0000-0000-0000-00000000”
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @return 参数值
     */
    public UUID getUUIDParam(HttpServletRequest request, String paramName) {
        return getUUIDParam(request, paramName, Constants.UUID_ZERO);
    }

    /**
     * 获取String类型的请求参数值
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @param nullValue
     *            参数值为空时的返回值
     * @return 参数值
     */
    public String getStringParam(HttpServletRequest request, String paramName, String nullValue) {
        String value = request.getParameter(paramName);
        if (value != null && !"".equals(value)) {
            return value;
        }
        return nullValue;
    }

    /**
     * 获取String类型的请求参数值，如果值不存在或为空，返回空字符串""
     *
     * @param request
     *            HTTP请求
     * @param paramName
     *            参数名
     * @return 参数值
     */
    public String getStringParam(HttpServletRequest request, String paramName) {
        return getStringParam(request, paramName, "");
    }
}
