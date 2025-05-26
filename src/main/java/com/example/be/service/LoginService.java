package com.example.be.service;

import com.example.be.Jwt.JwtUtil;
import com.example.be.constants.ErrorConstants;
import com.example.be.dto.response.UserResponse;
import com.example.be.entity.User;
import com.example.be.exception.CustomerException;
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

    public UserResponse login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomerException(ErrorConstants.ACCOUNT_NOT_FOUND);
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            // ✅ Truyền toàn bộ user vào generateToken
            String token = jwtUtil.generateToken(username);

            UserResponse userResponse = new UserResponse();
            userResponse.setUserId(user.getUserId());
            userResponse.setUsername(user.getUsername());
            userResponse.setRole(user.getRole());
            userResponse.setEmail(user.getEmail());
            userResponse.setToken(token);
            userResponse.setFullName(user.getFullName());
            userResponse.setGender(user.getGender());
            userResponse.setIsActive(user.getIsActive());
            return userResponse;
        }

        throw new CustomerException(ErrorConstants.INVALID_PASSWORD);
    }


    public boolean findAccount(String username) {
        return userRepository.existsByUsername(username);
    }
}
