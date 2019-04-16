package com.sinosoft.ops.cimp.exception.handler;

import com.sinosoft.ops.cimp.common.BaseResult;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.exception.SystemException;
import com.vip.vjtools.vjkit.base.ExceptionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice(basePackages = "com.sinosoft.ops.cimp.controller")
@SuppressWarnings("unchecked")
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 通用处理自定义的业务异常和系统异常
     */
    @ExceptionHandler({BusinessException.class, SystemException.class})
    public BaseResult businessExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String errorMsg = null;
        logger.error("【统一异常拦截】：异常类为：" + e.getClass().getName() + "类");
        logger.error(ExceptionUtil.stackTraceText(e));
        if (e instanceof BusinessException) {
            BusinessException exception = (BusinessException) e;
            errorMsg = exception.getErrorDtlMsg();
        }
        if (e instanceof SystemException) {
            SystemException exception = (SystemException) e;
            errorMsg = exception.getErrorDtlMsg();
        }
        if (StringUtils.isEmpty(errorMsg)) {
            errorMsg = "系统异常";
        }
        return BaseResult.fail(errorMsg);
    }

    /**
     * 处理常见的异常，例如RuntimeException，如果RuntimeException的message的第一个字符为汉字则将错误信息返回
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResult runtimeExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RuntimeException exception = (RuntimeException) e;
        String message = exception.getMessage();
        logger.error("【统一异常拦截】：异常类为：" + e.getClass().getName() + "类");
        logger.error(ExceptionUtil.stackTraceText(e));
        if (message != null
                && StringUtils.isNotEmpty(message)
                && message.charAt(0) > '\u4e00') {
            return BaseResult.fail(message);
        } else {
            return BaseResult.fail();
        }
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException) {
            return new ResponseEntity(BaseResult.fail("方法参数无效"), status);
        }
        logger.error("【统一异常拦截】：异常类为：" + ex.getClass().getName() + "类");
        logger.error(ExceptionUtil.stackTraceText(ex));
        if (ex instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException exception = (MethodArgumentTypeMismatchException) ex;
            logger.error("参数转换失败，方法：" + exception.getParameter().getMethod().getName() + "，参数：" + exception.getName() + ",信息：" + exception.getLocalizedMessage());

            return new ResponseEntity(BaseResult.fail("参数转换失败"), status);
        }
        return new ResponseEntity(BaseResult.fail("系统异常"), status);
    }
}
