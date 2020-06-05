package com.zhou.wetalk.global.hock;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.zhou.wetalk.exception.http.HttpException;
import com.zhou.wetalk.global.configuration.ExceptionCodeConfiguration;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 20:06
 * @ClassName GlobalExceptionAdvice
 * @see
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionAdvice {
    @Autowired
    private ExceptionCodeConfiguration codeConfiguration;
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e){
        String method = req.getMethod();
        String url = req.getRequestURI();
        String urlInfo = method + " " + url;
        return new UnifyResponse(9999, e.getMessage(),urlInfo);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(code= HttpStatus.NOT_FOUND)
    public UnifyResponse handleHttpRequestMethodNotSupportedException(HttpServletRequest req, Exception e){
        String method = req.getMethod();
        String url = req.getRequestURI();
        String urlInfo = method + " " + url;
        return new UnifyResponse(10002, codeConfiguration.getMessage(10002),urlInfo);
    }

    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e){
        String method = req.getMethod();
        String url = req.getRequestURI();
        String urlInfo = method + " " + url;
        HttpHeaders headers = new HttpHeaders();
        UnifyResponse msg = new UnifyResponse(e.getCode(),codeConfiguration.getMessage(e.getCode()),urlInfo);
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(msg,headers,httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnifyResponse handleBeanValidation(HttpServletRequest req, MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        // 获得字段校验失败的错误信息
        List<FieldError> fieldErrors = result.getFieldErrors();
        // 获得非字段校验失败的信息
        List<ObjectError> objectErrors = result.getGlobalErrors();

        String requsetUrl = req.getRequestURI();
        String method = req.getMethod();
        String urlInfo = method + " " + requsetUrl;

        String messages = this.formationAllErrorMessages(objectErrors, fieldErrors);
        return new UnifyResponse(10001,messages,urlInfo);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnifyResponse handleConstraintException(HttpServletRequest req, ConstraintViolationException e) {
        String requsetUrl = req.getRequestURI();
        String method = req.getMethod();
        String urlInfo = method + " " + requsetUrl;
        return  new UnifyResponse(10001,e.getMessage(),urlInfo);
    }

    private String formationAllErrorMessages(List<ObjectError> errors){
        StringBuffer errorMsg = new StringBuffer();
        errors.forEach(error-> errorMsg.append(error.getDefaultMessage()).append(";"));
        return errorMsg.toString();
    }

    private String formationAllErrorMessages(List<ObjectError> objectErrors, List<FieldError> fieldErrors){
        StringBuffer errorMsg = new StringBuffer();
        objectErrors.forEach(error-> errorMsg.append(error.getDefaultMessage()).append(";"));
        fieldErrors.forEach(error-> errorMsg.append(error.getField()).append(error.getDefaultMessage()).append(";"));
        return errorMsg.toString();
    }
}