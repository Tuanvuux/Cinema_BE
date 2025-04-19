package com.example.be.controller;
import com.example.be.dto.request.UserRequest;
import com.example.be.entity.User;
import com.example.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/register")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký tài khoản thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
