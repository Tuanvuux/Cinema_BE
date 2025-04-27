package com.example.be.controller;
import com.example.be.dto.request.LoginRequest;
import com.example.be.dto.response.TokenResponse;
import com.example.be.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }

        // Đăng nhập thành công, trả về token
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
