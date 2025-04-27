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
        // Kiểm tra tài khoản có tồn tại không
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Tài khoản không tồn tại
            return null;
        }

        // Kiểm tra mật khẩu
        if (passwordEncoder.matches(password, user.getPassword())) {
            // Mật khẩu đúng, tạo token và trả về
            return jwtUtil.generateToken(username);
        }

        // Mật khẩu sai
        return null;
    }

    public boolean findAccount(String username) {
        return userRepository.existsByUsername(username);
    }
}
