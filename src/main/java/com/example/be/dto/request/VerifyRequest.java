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
public class VerifyRequest {
    private String email;
    private String code;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String gender;
    private String address;
    private LocalDate birthday;
}
