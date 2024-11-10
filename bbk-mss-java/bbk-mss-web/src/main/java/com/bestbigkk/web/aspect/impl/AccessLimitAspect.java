package com.bestbigkk.web.aspect.impl;

import com.bestbigkk.common.utils.RequestContextHolderUtils;
import com.bestbigkk.common.aspect.AbstractAspect;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.web.ResultCode;
import com.bestbigkk.web.validator.AccessLimit;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
* @author: 开
* @date: 2020-03-24 15:04:07
* @describe: 限流切面， 借助Guava中的令牌桶，进行实现。限制各个url的访问速率
*/
@Slf4j
@Component
public class AccessLimitAspect extends AbstractAspect {

    /**使用url做为key,存放令牌桶 防止每次重新创建令牌桶*/
    private static  final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    @Override
    public Object doHandlerAspect(JoinPoint joinPoint, Method method, Throwable throwable)throws Throwable {
        execute(joinPoint,method, throwable);
        return null;
    }

    @Override
    public Object execute(JoinPoint pjp, Method method, Throwable throwable) {

        log.info("尝试从令牌桶获取令牌");

        AccessLimit lxRateLimit = method.getAnnotation(AccessLimit.class);
        final HttpServletRequest request = RequestContextHolderUtils.getRequest();

        // 或者url(存在map集合的key)
        String url = request.getRequestURI();
        RateLimiter rateLimiter;
        if (!limitMap.containsKey(url)) {
            // 为这个Url创建令牌桶
            rateLimiter = RateLimiter.create(lxRateLimit.perSecond());
            limitMap.put(url, rateLimiter);
            log.info("为请求{},创建令牌桶,容量{}",url,lxRateLimit.perSecond());
        }
        rateLimiter = limitMap.get(url);

        //获取令牌
        if (!rateLimiter.tryAcquire(lxRateLimit.timeOut(), lxRateLimit.timeOutUnit())) {
            throw new BusinessException(ResultCode.SERVER_BUSY, "");
        }
        return null;
    }
}
