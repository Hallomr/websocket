package com.example.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {
    private String clusterNodes;
    private String password;
    private int expireSeconds;
    private int commandTimeout;
}
