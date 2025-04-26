package com.example.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Tắt CSRF nếu không cần bảo vệ CSRF
                .cors(Customizer.withDefaults())  // Tắt CORS nếu không cần cấu hình CORS
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/**").permitAll()  // Cho phép tất cả các API mà không cần xác thực
                        .anyRequest().authenticated()  // Các yêu cầu khác yêu cầu xác thực
                )
                .httpBasic(Customizer.withDefaults());  // Bật Basic HTTP Authentication

        return http.build();
    }
}
