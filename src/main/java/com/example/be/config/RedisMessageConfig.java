package com.example.be.config;

import com.example.be.service.SeatLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisMessageConfig {

    private final RedisExpirationListener expirationListener;

    @Autowired
    SeatLockService seatLockService;

    // Sử dụng constructor injection để truyền SeatLockService vào RedisExpirationListener
    public RedisMessageConfig(RedisExpirationListener expirationListener) {
        this.expirationListener = expirationListener;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(expirationListener, new PatternTopic("__keyevent@0__:expired"));
        return container;
    }
}
