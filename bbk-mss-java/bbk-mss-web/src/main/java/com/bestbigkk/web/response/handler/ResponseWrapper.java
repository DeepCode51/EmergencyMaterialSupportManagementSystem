package com.bestbigkk.web.response.handler;

import com.alibaba.fastjson.JSONObject;
import com.bestbigkk.web.response.RWrapper;
import com.bestbigkk.web.response.Tips;
import com.bestbigkk.web.response.annotation.RW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author: 开
 * @date: 2020-01-18 17:29:30
 * @describe: 对直接响应的数据进行包装
 */
@Slf4j
@ControllerAdvice(annotations = RW.class)
public class ResponseWrapper implements ResponseBodyAdvice {

    private static final String TARGET_FORMAT = "application/json;charset=UTF-8";

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        log.info("returnType: [{}]", returnType);
        log.info("converterType: [{}]", converterType);
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // Content-Type不为@RW中指定的produces，不进行包装
        if(!TARGET_FORMAT.equals(selectedContentType.toString())){
            return body;
        }

        /*
            如果响应的为String类型数据，直接包装为Wrapper对象返回会出现转换异常 String can not cast to Wrapper...
            所以直接转换为Json字符串。
        */
        boolean toJsonString = selectedConverterType.equals(StringHttpMessageConverter.class);

        // 如果响应已经是统一响应体，则直接返回body
        if(body instanceof RWrapper){
            return body;
        }

        // 只有正常返回的结果才会进入这个判断流程，所以返回正常成功的状态码
        final RWrapper success = RWrapper.success(body);
        final String s = Tips.getAndRemove();
        if (Objects.nonNull(s)) {
            success.setMsg(s);
        }
        return toJsonString ? JSONObject.toJSONString(success) : success;
    }
}
