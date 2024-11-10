package com.bestbigkk.web.exception.handler;

import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.web.response.RWrapper;
import com.bestbigkk.common.web.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.*;

/**
* @author: 开
* @date: 2020-03-23 19:57:15
* @describe: 全局异常处理
*/
@Slf4j
public abstract class BaseGlobalExceptionHandler {

    /**
     * 违反约束异常
     */
    protected RWrapper handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.info("handleConstraintViolationException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return RWrapper.failure(ResultCode.BAD_REQUEST, "参数校验不通过", wrapperError(e));
    }

    /**
     * 转换参数校验校验异常信息
     * @return
     */
    private List<Map<String, String>> wrapperError(ConstraintViolationException e) {
        LinkedList<Map<String, String>> res = new LinkedList<>();
        final Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        constraintViolations.forEach(constraintViolation -> {
            final String errorMsg = constraintViolation.getMessageTemplate();
            final Path paramName = constraintViolation.getPropertyPath();
            res.add(new HashMap<String, String>(2) {{
                put("msg", errorMsg);
                put("key", paramName.toString());
            }});
        });
        return res;
    }

    /**
     * 处理验证参数封装错误时异常
     */
    protected RWrapper handleConstraintViolationException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.info("handleConstraintViolationException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return RWrapper.failure(ResultCode.BAD_REQUEST, e.getMessage());
    }

    /**
     * 处理参数绑定时异常（反400错误码）
     */
    protected RWrapper handleBindException(BindException e, HttpServletRequest request) {
        log.info("handleBindException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return RWrapper.failure(ResultCode.BAD_REQUEST, e.getFieldErrors());
    }

    /**
     * 处理使用@Validated注解时，参数验证错误异常（反400错误码）
     */
    protected RWrapper handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.info("handleBindException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return RWrapper.failure(ResultCode.BAD_REQUEST, e.getBindingResult());
    }

    /**
     * 处理通用自定义业务异常
     */
    protected RWrapper handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.info("handleBusinessException start, uri:{}, exception:{}, caused by: {}", request.getRequestURI(), e.getClass(), e.getMessage());
        return RWrapper.failure(e.getResultCode(), e.getMessage());
    }

    /**
     * 处理运行时系统异常（反500错误码）
     */
    protected RWrapper handleRuntimeException(Throwable e, HttpServletRequest request) {
        log.error("handleRuntimeException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return RWrapper.failure(ResultCode.SERVER_ERROR, e.getMessage());
    }

}
