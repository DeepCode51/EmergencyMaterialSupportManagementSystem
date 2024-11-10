package com.bestbigkk.web.response.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: 开
 * @date: 2020-01-18 17:30:48
 * @describe: 使用该注解，指定响应数据需要进行包装
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@RestController
public @interface RW {

    @AliasFor(annotation = RestController.class)
    String value() default "";

}
