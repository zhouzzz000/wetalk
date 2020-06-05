package com.zhou.wetalk.exception.http;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 20:04
 * @ClassName NotFoundException
 * @see
 */
public class NotFoundException extends HttpException{
    public NotFoundException(int code){
        this.httpStatusCode = 404;
        this.code = code;
    }
}