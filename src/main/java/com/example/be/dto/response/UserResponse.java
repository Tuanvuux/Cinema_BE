package com.example.be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserResponse {

    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String gender;
    private String token;
    private Boolean isActive = true;

}