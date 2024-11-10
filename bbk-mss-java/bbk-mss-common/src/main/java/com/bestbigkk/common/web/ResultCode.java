package com.bestbigkk.common.web;

/**
* @author: 开
* @date: 2020-03-23 19:16:40
* @describe: 响应状态枚举
*/
public enum ResultCode {

    /**
     * 正常
     */
    OK(200, "操作成功!"),
    BAD_REQUEST(400, "无效的请求!"),
    UN_AUTHORIZATION(401, "身份未认证/权限不足, 请求无法被接受!"),
    DENY(403, "操作被禁止!"),
    NOT_FOUND(404, "资源未找到"),
    BUSINESS(1314, "服务未达到预期!"),
    SERVER_ERROR(500, "服务器错误"),
    SERVER_BUSY(502, "服务器繁忙");



    public int code;
    public String msg;

    ResultCode(int i, String s) {
        this.code = i;
        this.msg = s;
    }
}
