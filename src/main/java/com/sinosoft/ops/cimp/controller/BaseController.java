package com.sinosoft.ops.cimp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.common.BaseResultHttpStatus;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@SuppressWarnings("unchecked")
public abstract class BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

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
            LOGGER.error("写入JSON数据失败！" + data, e);
        }
    }
}
