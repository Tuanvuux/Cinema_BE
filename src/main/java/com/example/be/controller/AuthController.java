package com.example.be.controller;
import com.example.be.dto.request.EmployeeRequest;
import com.example.be.dto.request.UserRequest;
import com.example.be.dto.request.VerifyRequest;
import com.example.be.entity.User;
import com.example.be.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

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

}
