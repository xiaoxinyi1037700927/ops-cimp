package com.sinosoft.ops.cimp.controller;

import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.common.BaseResultHttpStatus;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("unchecked")
public abstract class BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

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
}
