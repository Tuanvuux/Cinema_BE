package com.example.be.controller;

import com.example.be.entity.User;
import com.example.be.exception.CustomerException;
import com.example.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/toggle-delete")
    public ResponseEntity<User> toggleDeleteStatus(@PathVariable("id") Long accountId, @RequestBody Map<String, Boolean> statusMap) {
        Boolean isActive = statusMap.get("isActive");
        if (isActive == null) {
            return ResponseEntity.badRequest().build();
        }

        User user = userService.getUserById(accountId)
                .orElseThrow(() -> new CustomerException("Account not found with id: " + accountId));

        user.setIsActive(isActive);
        User updatedMovie = userService.saveUser(user);

        return ResponseEntity.ok(updatedMovie);
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userService.saveUser(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreUser(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            user.get().setIsActive(true);
            userService.saveUser(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
