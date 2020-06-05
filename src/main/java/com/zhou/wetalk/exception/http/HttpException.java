package com.zhou.wetalk.exception.http;

import com.zhou.wetalk.exception.RuntimeBaseException;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 20:01
 * @ClassName HttpException
 * @see
 */
public class HttpException extends RuntimeBaseException {
    protected Integer code;
    protected Integer httpStatusCode=500;
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }
    public Integer getCode() {
        return code;
    }
}