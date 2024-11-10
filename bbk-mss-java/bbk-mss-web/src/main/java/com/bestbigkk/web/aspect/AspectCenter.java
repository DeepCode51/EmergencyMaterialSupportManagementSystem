package com.bestbigkk.web.aspect;

import com.bestbigkk.web.aspect.impl.AccessLimitAspect;
import com.bestbigkk.web.aspect.impl.LogRecordAspect;
import com.bestbigkk.web.validator.AccessLimit;
import com.bestbigkk.web.validator.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * 切面:防止xss攻击 记录log  参数验证
 * @author liugh
 * @since 2018-05-03
 */
@Aspect
@Configuration
@Slf4j
public class AspectCenter {

    @Autowired
    private AccessLimitAspect accessLimitAspect;
    @Autowired
    private LogRecordAspect logRecordAspect;

    @Pointcut("execution(* com.bestbigkk.web.controller..*(..))  ")
    public void aspect() { }

    @Around(value = "aspect()")
    public Object validationPoint(ProceedingJoinPoint pjp)throws Throwable{

        Method method = currentMethod(pjp,pjp.getSignature().getName());

        //是否需要限流
        if (method.isAnnotationPresent(AccessLimit.class)) {
            accessLimitAspect.doHandlerAspect(pjp, method, null);
        }

        return  pjp.proceed(pjp.getArgs());
    }

    @AfterThrowing(value = "aspect()", throwing = "e")
    public Object afterThrowing(JoinPoint joinPoint, Throwable e) throws Throwable {
        Method method = currentMethod(joinPoint, joinPoint.getSignature().getName());

        //是否需要记录日志
        if (method.isAnnotationPresent(LogRecord.class)) {
            logRecordAspect.doHandlerAspect(joinPoint, method, e);
        }

        return null;
    }


    /**
     * 获取目标类的所有方法，找到当前要执行的方法
     */
    private Method currentMethod (JoinPoint joinPoint , String methodName ) {
        Method[] methods  = joinPoint.getTarget().getClass().getMethods();
        Method  resultMethod = null;
        for ( Method method : methods ) {
            if ( method.getName().equals( methodName ) ) {
                resultMethod = method;
                break;
            }
        }
        return resultMethod;
    }


}
