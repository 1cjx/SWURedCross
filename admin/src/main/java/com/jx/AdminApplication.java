package com.jx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.jx.mapper")
@EnableSwagger2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AdminApplication {
        public static void main(String[] args) {
            SpringApplication.run(AdminApplication.class, args);
        }
}