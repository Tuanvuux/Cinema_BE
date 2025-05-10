package com.example.be.controller;

import com.example.be.dto.request.UserRequest;
import com.example.be.dto.request.VerifyRequest;
import com.example.be.entity.User;
import com.example.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Đăng ký + gửi mã xác nhận
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest request) {
        try {
            userService.sendVerificationCode(request);
            return ResponseEntity.status(HttpStatus.OK).body("Đã gửi mã xác nhận đến email!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Xác nhận mã và tạo tài khoản
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyRequest request) {
        try {
            userService.verifyAndCreateUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tài khoản đã được kích hoạt!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
