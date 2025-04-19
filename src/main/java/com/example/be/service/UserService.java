package com.example.be.service;

import com.example.be.dto.request.UserRequest;
import com.example.be.dto.response.UserInforDTO;
import com.example.be.entity.User;
import com.example.be.enums.Role;
import com.example.be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên người dùng đã tồn tại!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(Role.USER.toString());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setBirthday(request.getBirthday());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        userRepository.save(user);
    }

    public UserInforDTO findByUsername(String username){
        UserInforDTO userInforDTO = new UserInforDTO();
        return userInforDTO;
    }
}
