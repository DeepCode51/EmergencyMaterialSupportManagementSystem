package com.bestbigkk.web.validator;

import com.bestbigkk.web.validator.impl.PropertyInValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
* @author: 开
* @date: 2020-01-18 17:27:58
* @describe: 校验给定的字段是否在指定集合
*/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PropertyInValidator.class})
public @interface PropertyIn {

    String message() default "传递的值不在允许的集合";

    String[] values() default {};

    boolean require() default false;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
