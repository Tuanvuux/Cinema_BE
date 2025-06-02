package com.example.be.service;

import com.example.be.Jwt.JwtUtil;
import com.example.be.constants.AppConstants;
import com.example.be.constants.ErrorConstants;
import com.example.be.dto.response.UserResponse;
import com.example.be.entity.PasswordResetToken;
import com.example.be.entity.User;
import com.example.be.exception.CustomerException;
import com.example.be.repository.PasswordResetTokenRepository;
import com.example.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

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
    public void processForgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email không tồn tại!");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiryDate);
        tokenRepository.save(resetToken);

        String resetLink = AppConstants.Client_URL + "/reset-password?token=" + token;
        emailService.sendResetPasswordEmail(user.getEmail(), resetLink);
    }


    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn!"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token đã hết hạn!");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // xoá sau khi dùng
    }



    public boolean findAccount(String username) {
        return userRepository.existsByUsername(username);
    }
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomerException(ErrorConstants.ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new CustomerException("Mật khẩu cũ không đúng!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
