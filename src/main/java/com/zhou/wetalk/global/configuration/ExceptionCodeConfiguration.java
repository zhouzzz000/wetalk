package com.zhou.wetalk.global.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName wetalk
 * @Author zhouzzz
 * @Date 2020/4/7
 * @Time 20:07
 * @ClassName ExceptionCodeConfiguration
 * @see
 */
@PropertySource(value = "classpath:config/exception-code.properties", encoding = "utf-8")
@ConfigurationProperties(prefix = "zhou")
@Component
public class ExceptionCodeConfiguration {
    private Map<Integer,String> codes = new HashMap<>();

    public Map<Integer, String> getCodes() {
        return codes;
    }

    public void setCodes(Map<Integer, String> codes) {
        this.codes = codes;
    }

    public String getMessage(int code)
    {
        return codes.get(code);
    }
}