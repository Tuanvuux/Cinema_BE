package com.example.be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // endpoint WebSocket
                .setAllowedOrigins("http://localhost:5173, https://venerable-pastelito-875d4b.netlify.app/") // ✅ Cho phép frontend React kết nối
                .withSockJS(); // dùng SockJS nếu frontend dùng SockJS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Broker trả dữ liệu về client
        config.setApplicationDestinationPrefixes("/app"); // Prefix để client gửi lên server
    }
}
