package com.zhou.wetalk.global.hock;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 20:11
 * @ClassName UnifyResponse
 * @see
 */
public class UnifyResponse {
    protected int code;
    protected String message;
    protected String requestUrl;

    public UnifyResponse(int code, String  message, String requestUrl){
        this.code = code;
        this.message = message;
        this.requestUrl = requestUrl;
    }
    public UnifyResponse(){};

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestUrl() {
        return requestUrl;
    }
}