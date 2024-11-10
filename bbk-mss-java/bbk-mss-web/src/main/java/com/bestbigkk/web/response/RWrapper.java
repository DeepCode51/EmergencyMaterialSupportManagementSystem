package com.bestbigkk.web.response;

import com.bestbigkk.common.web.ResultCode;
import com.bestbigkk.common.utils.RequestContextHolderUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: 开
 * @date: 2020-03-23 19:12:40
 * @describe: 普通响应
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RWrapper<T> implements Result {

    private static final long serialVersionUID = 874200365941306385L;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime timeStamp;
    private Boolean status;
    private String path;
    private Integer code;
    private String msg;
    private T data;

    public static RWrapper success() {
        RWrapper<Object> result = new RWrapper<>();
        result.setResultCode(ResultCode.OK);
        return result;
    }

    public static <T>RWrapper success(T data) {
        RWrapper<T> result = new RWrapper<>();
        result.setResultCode(ResultCode.OK);
        result.setData(data);
        return result;
    }

    public static RWrapper failure(ResultCode resultCode) {
        RWrapper<Object> result = new RWrapper<>();
        result.setResultCode(resultCode);
        return result;
    }

    public static RWrapper failure(HttpServletRequest servletRequest, ResultCode resultCode, String msg) {
        RWrapper<Object> result = new RWrapper<>();
        result.setResultCode(resultCode, servletRequest);
        if (Objects.nonNull(msg)) {
            result.setMsg(msg);
        }
        return result;
    }

    public static <T> RWrapper failure(ResultCode resultCode, T data) {
        RWrapper<T> result = new RWrapper<>();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public static <T> RWrapper failure(ResultCode resultCode, String msg, T data) {
        RWrapper<T> result = new RWrapper<>();
        result.setResultCode(resultCode);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    private void setResultCode(ResultCode code, HttpServletRequest servletRequest) {
        this.code = code.code;
        this.msg = code.msg;
        this.timeStamp = LocalDateTime.now();
        this.status = code.equals(ResultCode.OK);

        servletRequest = Objects.isNull(servletRequest) ? RequestContextHolderUtils.getRequest() : servletRequest;
        this.path = servletRequest.getRequestURI();
    }

    private void setResultCode(ResultCode code) {
        setResultCode(code, null);
    }

}
