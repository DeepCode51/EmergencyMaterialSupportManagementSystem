package com.bestbigkk.web.validator;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
* @author: 开
* @date: 2020-03-24 15:05:12
* @describe: 限流增强
*/
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface AccessLimit {

    /**
     * 每秒向桶中放入令牌的数量   默认最大即不做限流
     * @return
     */
    double perSecond() default Double.MAX_VALUE;

    /**
     * 获取令牌的等待时间  默认0
     * @return
     */
    int timeOut() default 0;

    /**
     * 超时时间单位
     * @return
     */
    TimeUnit timeOutUnit() default TimeUnit.SECONDS;

}
