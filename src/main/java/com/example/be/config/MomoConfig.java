package com.example.be.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "momo")
@Getter
@Setter
public class MomoConfig {
    private String partnerCode;
    private String accessKey;
    private String secretKey;
    private String requestType;
    private String endpoint;
    private String redirectUrl;
    private String ipnUrl;
}
