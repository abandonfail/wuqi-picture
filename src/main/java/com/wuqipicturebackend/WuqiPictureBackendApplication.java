package com.wuqipicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@MapperScan("com.wuqipicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class WuqiPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WuqiPictureBackendApplication.class, args);
    }

}
