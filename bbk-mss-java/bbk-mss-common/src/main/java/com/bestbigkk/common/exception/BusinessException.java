package com.bestbigkk.common.exception;

import com.bestbigkk.common.web.ResultCode;
import lombok.Data;

/**
* @author: 开
* @date: 2020-03-24 09:26:55
* @describe: 常规业务异常
*/
@Data
public class BusinessException extends RuntimeException {
    private ResultCode resultCode;

    public BusinessException(String message) {
        super(message);
        resultCode = ResultCode.BAD_REQUEST;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        resultCode = ResultCode.BAD_REQUEST;
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode == null ? ResultCode.BAD_REQUEST : resultCode;
    }
}
