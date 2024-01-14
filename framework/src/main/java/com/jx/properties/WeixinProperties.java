package com.jx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix ="weixin")
@Data
public class WeixinProperties {
    private  String jscode2sessionUrl; //登录凭证校验请求地址
    private String appid;//小程序 appid
    private String secret; //小程序 appSecret
}
