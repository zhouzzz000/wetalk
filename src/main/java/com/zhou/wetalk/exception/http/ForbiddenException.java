package com.zhou.wetalk.exception.http;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 20:03
 * @ClassName ForbiddenException
 * @see
 */
public class ForbiddenException extends HttpException {
    public ForbiddenException(int code)
    {
        this.code = code;
        this.httpStatusCode = 403;
    }
}