package com.example.be.service;

import com.example.be.Jwt.JwtUtil;
import com.example.be.entity.User;
import com.example.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Tạo và trả về token nếu thông tin hợp lệ
            return jwtUtil.generateToken(username);
        }
        return null;
    }

    public boolean findAccount(String username) {
        return userRepository.existsByUsername(username);
    }
}
