package com.example.be.dto.response;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserInforDTO {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private LocalDate birthday;
    private String address;
    private String phone;
    private String gender;
    private LocalDateTime createdAt;
}
