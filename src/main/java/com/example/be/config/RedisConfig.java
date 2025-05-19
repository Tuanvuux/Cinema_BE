package com.example.be.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Properties;

@Configuration
public class RedisConfig {

    private final RedisConnectionFactory connectionFactory;

    public RedisConfig(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @PostConstruct
    public void enableNotifyKeyspaceEvents() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            Properties config = connection.getConfig("notify-keyspace-events");
            String current = config.getProperty("notify-keyspace-events", "");
            if (!current.contains("Ex")) {
                String newConfig = current + "Ex";
                connection.setConfig("notify-keyspace-events", newConfig);
                System.out.println("Enabled notify-keyspace-events: " + newConfig);
            } else {
                System.out.println("notify-keyspace-events already enabled: " + current);
            }
        } catch (Exception e) {
            System.err.println("Failed to enable notify-keyspace-events: " + e.getMessage());
        }
    }
}
