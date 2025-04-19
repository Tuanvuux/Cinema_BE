package com.example.be.dto.request;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserRequest {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private LocalDate birthday;
    private String address;
    private String phone;
    private String gender;
}
