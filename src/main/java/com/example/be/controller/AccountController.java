package com.example.be.controller;

import com.example.be.dto.request.UserRequestADMIN;
import com.example.be.entity.User;
import com.example.be.exception.CustomerException;
import com.example.be.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
@CrossOrigin(origins = "http://localhost:5173")
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/admin/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable("username") String username) {
        User user = userService.findAdminByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateUserAdmin(@PathVariable Long id, @RequestBody UserRequestADMIN user) {
        try {
            userService.updateAdmin(id, user);
            return ResponseEntity.ok("Cập nhật thành công!");
        } catch (IllegalArgumentException e) {
            // Trả về lỗi mật khẩu không đúng với mã HTTP 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            // Trả về lỗi không tìm thấy người dùng với mã HTTP 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Trả về lỗi khác với mã HTTP 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @PutMapping("/admin/{id}/toggle-delete")
    public ResponseEntity<User> toggleDeleteUser(@PathVariable("id") Long accountId, @RequestBody User user) {
        User user1 = userService.getUserById(accountId)
                .orElseThrow(() -> new CustomerException("User not found with id: " + accountId));

        user1.setIsActive(user.getIsActive());
        User updatedUser = userService.saveUser(user1);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userService.saveUser(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/admin/{id}/restore")
    public ResponseEntity<Void> restoreUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            user.get().setIsActive(true);
            userService.saveUser(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/countuser")
    public long countUserNotAdmin(){
        return userService.countUserNotAdmin();
    }

    @GetMapping("/admin/countemployee")
    public long countEmployee(){
        return userService.countEmployee();
    }

    @GetMapping("/admin/getuser")
    public List<User> getListUser(){
        return userService.getListUsers();
    }

    @GetMapping("/admin/getemployee")
    public List<User> getListEmployee(){
        return userService.getListEmployee();
    }

}
