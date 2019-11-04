package com.tdoer.auth;

import com.tdoer.springboot.autoconfigure.EnableErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages= {"com.tdoer.interfaces.bedrock", "com.tdoer.interfaces.auth"})
@EnableEurekaClient
@EnableErrorHandler({AuthErrorCodes.class})
@ComponentScan(basePackages = {"com.tdoer.auth", "com.tdoer.delegate.bedrock"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
