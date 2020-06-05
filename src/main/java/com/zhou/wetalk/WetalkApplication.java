package com.zhou.wetalk;

import com.zhou.wetalk.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.zhou.wetalk.mapper")
public class WetalkApplication {
    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(WetalkApplication.class, args);
    }
}
