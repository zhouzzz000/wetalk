package com.zhou.wetalk.global.hock;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/9
 * @Time 15:55
 * @ClassName UnifyJsonResponse
 * @see
 */
public class UnifyJsonResponse {
    protected UnifyJsonResult unifyJsonResult;
    protected HttpHeaders headers;
    protected HttpStatus status;

    public UnifyJsonResponse(UnifyJsonResult unifyJsonResult, HttpHeaders headers, HttpStatus status) {
        this.unifyJsonResult = unifyJsonResult;
        this.headers = headers;
        this.status = status;
    }
    public UnifyJsonResponse(UnifyJsonResult unifyJsonResult, HttpStatus status) {
        this.unifyJsonResult = unifyJsonResult;
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.status = status;
    }

    public UnifyJsonResponse(UnifyJsonResult unifyJsonResult) {
        this.unifyJsonResult = unifyJsonResult;
        this.headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.status = HttpStatus.OK;
    }
    public ResponseEntity<UnifyJsonResult> getResponseEntity(){
        return new ResponseEntity<>(this.unifyJsonResult,headers,status);
    }
}