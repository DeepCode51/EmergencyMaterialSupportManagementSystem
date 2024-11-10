package com.bestbigkk.web.exception.handler;

import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.web.response.RWrapper;
import com.bestbigkk.common.web.ResultCode;
import com.bestbigkk.web.response.Tips;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author: 开
 * @date: 2020-03-23 19:58:05
 * @describe: 全局异常处理
 */
@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public RWrapper handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        return super.handleConstraintViolationException(e, request);
    }


    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RWrapper handleConstraintViolationException(HttpMessageNotReadableException e, HttpServletRequest request) {
        return super.handleConstraintViolationException(e, request);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public RWrapper handleBindException(BindException e, HttpServletRequest request) {
        final List<ObjectError> allErrors = e.getAllErrors();
        LinkedList<Map<String, String>> res = new LinkedList<>();
        allErrors.forEach(error->{
            final Object[] arguments = error.getArguments();
            DefaultMessageSourceResolvable defaultMessageSourceResolvable = (DefaultMessageSourceResolvable) arguments[0];
            final String paramName = defaultMessageSourceResolvable.getDefaultMessage();
            final String defaultMessage = error.getDefaultMessage();
            res.add(Collections.singletonMap(paramName, defaultMessage));
        });

        return RWrapper.failure(ResultCode.BAD_REQUEST, res);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RWrapper handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return super.handleMethodArgumentNotValidException(e, request);
    }

    @Override
    @ExceptionHandler(BusinessException.class)
    public RWrapper handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("Business Exception!");
        return super.handleBusinessException(e, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public RWrapper handlerUnAuthorization(UnauthorizedException e) {
        final String s = Tips.getAndRemove();
        if (Objects.isNull(s)) {
            return RWrapper.failure(ResultCode.UN_AUTHORIZATION);
        }
        return RWrapper.failure(ResultCode.UN_AUTHORIZATION, s);
    }

    @Override
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public RWrapper handleRuntimeException(Throwable e, HttpServletRequest request) {
        log.error("Internal Error!");
        //TODO 可通过邮件、微信公众号等方式发送信息至开发人员、记录存档等操作

        //404
        if (e instanceof NoHandlerFoundException) {
            return RWrapper.failure(ResultCode.NOT_FOUND, e.getMessage());
        }

        // Interval Error
        return super.handleRuntimeException(e, request);
    }

}
