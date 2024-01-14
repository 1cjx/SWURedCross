package com.jx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jx.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("jxTTT", "http://www.jx.com", "1500206893@qq.com");
        return new ApiInfoBuilder()
                .title("红十字会志愿服务系统-小程序端")
                .description("微信小程序所使用的所有接口")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}