package com.example.be.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class EmployeeRequest {
    private String fullName;
    private String username;
    private String password;
}
