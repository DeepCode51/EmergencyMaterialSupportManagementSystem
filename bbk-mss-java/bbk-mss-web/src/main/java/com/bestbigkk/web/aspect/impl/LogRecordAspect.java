package com.bestbigkk.web.aspect.impl;

import com.alibaba.fastjson.JSONObject;
import com.bestbigkk.common.utils.HttpUtils;
import com.bestbigkk.common.utils.RequestContextHolderUtils;
import com.bestbigkk.persistence.entity.OperationLogPO;
import com.bestbigkk.common.aspect.AbstractAspect;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.web.validator.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 记录日志切面
 * @author liugh
 * @since on 2018/5/10.
 */
@Slf4j
@Component
public class LogRecordAspect extends AbstractAspect {

    private final LogRecordPersistence logRecordPersistence;

    public LogRecordAspect(LogRecordPersistence logRecordPersistence) {
        this.logRecordPersistence = logRecordPersistence;
    }


    @Override
    public Object doHandlerAspect(JoinPoint pjp, Method method, Throwable throwable) throws Throwable{
        if (Objects.isNull(throwable) || throwable instanceof BusinessException) {
            return null;
        }
        return execute(pjp,method, throwable);
    }

    @Override
    public Object execute(JoinPoint pjp, Method method, Throwable throwable) throws Throwable{
        LogRecord logRecord  = method.getAnnotation(LogRecord.class );

        final String modelName = logRecord.modelName();
        final String description = logRecord.description();

        final StringBuilder stringBuilder = new StringBuilder();
        final StackTraceElement[] trace = throwable.getStackTrace();
        Stream.of(trace).forEach(stackTraceElement -> stringBuilder.append(stackTraceElement).append("\n"));

        LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        final String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);

        final Object[] args = pjp.getArgs();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            String paramName = Objects.requireNonNull(parameterNames)[i];
            try {
                res.append("【").append(paramName).append("=").append(JSONObject.toJSONString(args[i].toString())).append("】");
            } catch (Exception e) {
                res.append("【").append(paramName).append("=").append("无法转换该参数类型保存").append("】");
            }
        }

        final OperationLogPO build = OperationLogPO.builder()
                .createTime(LocalDateTime.now())
                .logDescription(description)
                .modelName(modelName)
                .errorMsg(throwable.getMessage())
                .stackInfo(stringBuilder.toString())
                .logLocation(pjp.getTarget().getClass().getName() + ":" + method.getName())
                .ip(HttpUtils.getIpAddress(RequestContextHolderUtils.getRequest()))
                .actionArgs(res.toString())
                .build();

        if (!logRecordPersistence.add(build)) {
            logRecordPersistence.persistence(build);
        }

        return null;
    }



}


