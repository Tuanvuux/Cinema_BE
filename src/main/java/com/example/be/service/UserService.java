package com.example.be.service;

import com.example.be.dto.request.EmployeeRequest;
import com.example.be.dto.request.UserRequest;
import com.example.be.dto.request.UserRequestADMIN;
import com.example.be.dto.response.UserInforDTO;
import com.example.be.entity.Movie;
import com.example.be.entity.User;
import com.example.be.enums.Role;
import com.example.be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        userRepository.save(user);
    }

    public void registerEmployee(EmployeeRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên nhân viên đã tồn tại!");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setRole(Role.EMPLOYEE.toString());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public UserInforDTO findByUsername(String username){
        UserInforDTO userInforDTO = new UserInforDTO();
        return userInforDTO;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findAdminByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public User findUserById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public void updateAdmin(Long id, UserRequestADMIN updateRequest) {
        User adminToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng"));

        // Cập nhật thông tin cơ bản
        adminToUpdate.setUsername(updateRequest.getUsername());
        adminToUpdate.setEmail(updateRequest.getEmail());
        adminToUpdate.setBirthday(updateRequest.getBirthday());
        adminToUpdate.setGender(updateRequest.getGender());
        adminToUpdate.setPhone(updateRequest.getPhone());
        adminToUpdate.setFullName(updateRequest.getFullName());

        // Xử lý cập nhật mật khẩu nếu có
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            // Kiểm tra mật khẩu hiện tại
            if (updateRequest.getCurrentPassword() == null || updateRequest.getCurrentPassword().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng nhập mật khẩu hiện tại");
            }

            // Kiểm tra mật khẩu hiện tại có chính xác không
            if (!passwordEncoder.matches(updateRequest.getCurrentPassword(), adminToUpdate.getPassword())) {
                throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác");
            }

            // Mã hóa và lưu mật khẩu mới
            String hashedPassword = passwordEncoder.encode(updateRequest.getPassword());
            adminToUpdate.setPassword(hashedPassword);
        }

        // Lưu thông tin cập nhật vào database
        userRepository.save(adminToUpdate);
    }

    public long countUserNotAdmin(){
        return userRepository.countAllNonAdminUsers();
    }
}
