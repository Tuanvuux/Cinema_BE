package com.example.be.controller;

import com.example.be.dto.response.UserInforDTO;
import com.example.be.entity.ShowTime;
import com.example.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(@RequestParam Long userId) {
        UserInforDTO userInfo = userService.getUserInfoByUserId(userId);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("người dùng không tồn tại với userId= : " + userId);
        }
    }
    @PutMapping("/userInfo/{userId}")
    public ResponseEntity<?> updateUserInfo(@PathVariable Long userId,
                                            @RequestBody UserInforDTO userInfoDTO) {
        userInfoDTO.setUserId(userId); // Gán userId vào DTO nếu chưa có
        boolean updated = userService.updateUserInfo(userInfoDTO);

        if (updated) {
            return ResponseEntity.ok(userInfoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy người dùng với ID = " + userId);
        }
    }

}
