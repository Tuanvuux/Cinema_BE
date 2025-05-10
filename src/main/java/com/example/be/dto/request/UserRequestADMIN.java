package com.example.be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestADMIN {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String password;        // New password
    private String currentPassword; // Current password for verification
    private LocalDate birthday;
    private String address;
    private String phone;
    private String role;
    private String gender;
    private Boolean isActive;
}
