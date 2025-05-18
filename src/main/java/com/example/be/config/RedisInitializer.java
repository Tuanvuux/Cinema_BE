package com.example.be.config;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RedisInitializer {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisInitializer(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void enableKeyspaceNotifications() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        try {
            Properties config = connection.getConfig("notify-keyspace-events");
            String currentConfig = config.getProperty("notify-keyspace-events", "");
            if (!currentConfig.contains("Ex")) {
                String newConfig = currentConfig + "Ex";
                connection.setConfig("notify-keyspace-events", newConfig);
                System.out.println("Enabled notify-keyspace-events: " + newConfig);
            } else {
                System.out.println("notify-keyspace-events already enabled: " + currentConfig);
            }
        } catch (Exception e) {
            System.err.println("Failed to set notify-keyspace-events: " + e.getMessage());
        } finally {
            connection.close();
        }
    }
}
