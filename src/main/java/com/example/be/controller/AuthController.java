package com.example.be.controller;
import com.example.be.dto.request.*;
import com.example.be.dto.response.UserResponse;
import com.example.be.entity.User;
import com.example.be.repository.UserRepository;
import com.example.be.service.LoginService;
import com.example.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginService loginService;


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

    @PostMapping("/admin/create-employee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRequest request) {
        boolean success = userService.registerEmployee(request);
        if (!success) {
            // Trả về lỗi khi username đã tồn tại nhưng không throw exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Đăng ký tài khoản nhân viên thành công!");
    }


    @GetMapping("/admin/check-username/{username}")
    public ResponseEntity<?> checkUsernameExists(@PathVariable String username) {
        boolean exists = userRepository.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UserResponse userResponse = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());

        if (userResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }

        if (Boolean.FALSE.equals(userResponse.getIsActive())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Tài khoản bị khóa hoặc chưa được kích hoạt!");
        }
        return ResponseEntity.ok(userResponse);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        loginService.processForgotPassword(request.getEmail());
        return ResponseEntity.ok("Email đặt lại mật khẩu đã được gửi.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        loginService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Mật khẩu đã được cập nhật thành công.");
    }
    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        loginService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        return "Đổi mật khẩu thành công!";
    }

}
