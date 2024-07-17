package com.jx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xunfei.client")
@Data
public class XFConfig {
    private String appId;

    private String apiSecret;

    private String apiKey;

    private String hostUrl;

    private Integer maxResponseTime;

}
