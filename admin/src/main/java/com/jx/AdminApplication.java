package com.jx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.jx.mapper")
@EnableSwagger2
public class AdminApplication {
        public static void main(String[] args) {
            SpringApplication.run(AdminApplication.class, args);
        }
}