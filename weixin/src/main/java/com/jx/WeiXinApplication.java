package com.jx;

import com.jx.utils.DotenvLoader;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.jx.mapper")
@EnableSwagger2
public class WeiXinApplication {
    public static void main(String[] args) {
        // 加载环境变量
        DotenvLoader.loadDotenv();
        SpringApplication.run(WeiXinApplication.class, args);
    }
}