package com.bestbigkk.web.validator.impl;

import com.bestbigkk.web.validator.PropertyIn;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
* @author: 开
* @date: 2020-01-18 17:27:25
* @describe: 校验给定字段是否在允许的集合里面实现
*/
public class PropertyInValidator implements ConstraintValidator<PropertyIn,String> {

    private PropertyIn propertyIn;

    @Override
    public void initialize(PropertyIn constraintAnnotation) {
        this.propertyIn = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean empty = StringUtils.isEmpty(value);
        if (empty) {
            return !propertyIn.require();
        }
        String[] values = propertyIn.values();
        for (String property : values) {
            if (property.equals(value)) {
                return true;
            }
        }
        return false;
    }

}
